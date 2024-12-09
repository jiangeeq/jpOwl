/**
 * Redis输出源配置类
 */
@Configuration
@ConditionalOnClass(RedisOutputSource.class)
@ConditionalOnProperty(prefix = JpOwlConstants.Output.REDIS_PREFIX, name = "enabled", havingValue = "true")
public class RedisOutputSourceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisOutputSource redisOutputSource(JpOwlProperties properties) {
        RedisProperties redisProps = properties.getOutput().getRedis();
        return new RedisOutputSource(redisProps);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisLoadDetector redisLoadDetector(RedisOutputSource redisOutputSource) {
        return new RedisLoadDetector(redisOutputSource);
    }
} 