package com.youpeng.jpowl.output.provider.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.youpeng.jpowl.output.core.AbstractOutputSource;
import com.youpeng.jpowl.output.core.OutputSourceType;
import com.youpeng.jpowl.output.model.MonitorData;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import co.elastic.clients.elasticsearch._types.Time;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Elasticsearch输出源实现
 * 支持将监控数据写入Elasticsearch集群
 * 提供单条写入和批量写入能力，使用BulkProcessor进行异步批量处理
 */
public class ElasticsearchOutputSource extends AbstractOutputSource {
    private final ElasticsearchClient client;
    private final String indexName;
    private final BulkIngester<BulkOperation> bulkIngester;
    private final ElasticsearchConfig config;
    /**
     * 构造函数
     * @param properties 配置属性，包含ES连接信息和索引配置
     */
    public ElasticsearchOutputSource(Map<String, Object> properties) {
        super(properties);
        this.config = ElasticsearchConfig.fromMap(properties);
        this.client = createClient(config);
        this.indexName = config.getIndexName();
        this.bulkIngester = createBulkIngester(config);
    }
    
    /**
     * 创建ES客户端
     */
    private ElasticsearchClient createClient(ElasticsearchConfig config) {
        // 创建 RestClient
        RestClientBuilder builder = RestClient.builder(
                Arrays.stream(config.getHosts().split(","))
                        .map(host -> {
                            String[] parts = host.split(":");
                            return new HttpHost(parts[0], Integer.parseInt(parts[1]), "http");
                        })
                        .toArray(HttpHost[]::new)
        );

        // 配置认证
        if (config.getUsername() != null && config.getPassword() != null) {
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(config.getUsername(), config.getPassword())
            );
            builder.setHttpClientConfigCallback(httpClientBuilder -> 
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }
        
        // 创建传输层和客户端
        ElasticsearchTransport transport = new RestClientTransport(
            builder.build(), new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
    
    /**
     * 创建BulkProcessor用于异步批量处理
     */
    private BulkIngester<BulkOperation> createBulkIngester(ElasticsearchConfig config) {
        return BulkIngester.of(builder -> builder
            .client(client)
            .maxOperations(config.getBulkActions())
            .maxSize(config.getBulkSizeMb() * 1024 * 1024)
            .flushInterval(config.getFlushInterval(), TimeUnit.SECONDS)
            .listener(new BulkListener<BulkOperation>() {
                @Override
                public void beforeBulk(long l, BulkRequest bulkRequest, List<BulkOperation> list) {
                    logger.debug("Executing bulk [{}] with {} operations", l, list.size());

                }

                @Override
                public void afterBulk(long l, BulkRequest bulkRequest, List<BulkOperation> list, BulkResponse bulkResponse) {
                    logger.debug("Executed bulk [{}] with {} operations response[{}]", l, list.size(), bulkResponse);

                }

                @Override
                public void afterBulk(long l, BulkRequest bulkRequest, List<BulkOperation> list, Throwable throwable) {
                    logger.error("Failed to execute bulk [{}]", l, throwable);

                }
            })
        );
    }
    
    @Override
    public void write(MonitorData data) {
        try {
            BulkOperation operation = BulkOperation.of(op -> op
                .index(idx -> idx
                    .index(indexName)
                    .document(convertToMap(data))
                )
            );
            bulkIngester.add(operation);
        } catch (Exception e) {
            logger.error("Failed to write data to Elasticsearch", e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void writeBatch(List<MonitorData> dataList) {
        try {
            List<BulkOperation> operations = dataList.stream()
                .map(data -> BulkOperation.of(op -> op
                    .index(idx -> idx
                        .index(indexName)
                        .document(convertToMap(data))
                    )
                ))
                .collect(Collectors.toList());
                
            client.bulk(b -> b
                .operations(operations)
                .timeout(Time.of(t -> t.time(String.valueOf(config.getFlushInterval()))))
            );
        } catch (Exception e) {
            logger.error("Failed to write batch data to Elasticsearch", e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public OutputSourceType getType() {
        return OutputSourceType.ELASTICSEARCH;
    }
    
    /**
     * 将监控数据转换为Map
     */
    private Map<String, Object> convertToMap(MonitorData data) {
        Map<String, Object> source = new HashMap<>();
        source.put("timestamp", data.getTimestamp());
        source.put("metricName", data.getMetricName());
        source.put("value", data.getValue());
        source.put("tags", data.getTags());
        return source;
    }
    
    /**
     * 关闭资源
     */
    @Override
    public void close() {
        try {
            // 先关闭 bulkIngester，确保所有待处理的数据都被发送
            if (bulkIngester != null) {
                bulkIngester.close();
            }
            
            // 关闭传输层和客户端
            if (client != null) {
                client._transport().close();
            }
        } catch (Exception e) {
            logger.error("Error closing ElasticsearchOutputSource", e);
            throw new RuntimeException("Failed to close ElasticsearchOutputSource", e);
        }
    }
} 