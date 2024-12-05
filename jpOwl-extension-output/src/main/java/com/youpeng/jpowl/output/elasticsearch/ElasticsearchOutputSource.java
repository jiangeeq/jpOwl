public class ElasticsearchOutputSource implements OutputSource {
    private final RestHighLevelClient client;
    private final String indexName;
    private final BulkProcessor bulkProcessor;
    
    public ElasticsearchOutputSource(Map<String, Object> properties) {
        this.client = createClient(properties);
        this.indexName = (String) properties.get("indexName");
        this.bulkProcessor = createBulkProcessor();
    }
    
    @Override
    public void write(MonitorData data) {
        IndexRequest request = new IndexRequest(indexName)
            .source(convertToMap(data), XContentType.JSON);
        bulkProcessor.add(request);
    }
    
    @Override
    public void writeBatch(List<MonitorData> dataList) {
        BulkRequest bulkRequest = new BulkRequest();
        for (MonitorData data : dataList) {
            bulkRequest.add(new IndexRequest(indexName)
                .source(convertToMap(data), XContentType.JSON));
        }
        client.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new BulkResponseListener());
    }
    
    @Override
    public OutputSourceType getType() {
        return OutputSourceType.ELASTICSEARCH;
    }
} 