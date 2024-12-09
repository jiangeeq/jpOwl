/**
 * 监控配置类
 * 负责配置监控相关的组件
 */
@Configuration
@ConditionalOnClass(MonitorManager.class)
public class MonitorConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MonitorManager monitorManager(OutputHandler outputHandler) {
        return new MonitorManager(outputHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public MonitorAspect monitorAspect(MonitorManager monitorManager) {
        return new MonitorAspect(monitorManager);
    }
} 