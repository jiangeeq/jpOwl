/**
 * 输出处理配置类
 * 负责配置不同的输出处理器
 */
@Configuration
@ConditionalOnClass(OutputHandler.class)
@Import({
    RedisOutputSourceConfiguration.class,
    MySQLOutputSourceConfiguration.class
})
public class OutputConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OutputHandler outputHandler(JpOwlProperties properties) {
        OutputHandler handler = new OutputHandler();
        // 根据配置初始化不同的输出源
        if (properties.getOutput().getRedis().isEnabled()) {
            handler.addOutputSource(new RedisOutputSource(properties.getOutput().getRedis()));
        }
        if (properties.getOutput().getMysql().isEnabled()) {
            handler.addOutputSource(new MySQLOutputSource(properties.getOutput().getMysql()));
        }
        return handler;
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadManager loadManager(OutputHandler outputHandler) {
        return new LoadManager(outputHandler);
    }
} 