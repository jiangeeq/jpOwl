@Configuration
@ConditionalOnClass(JpOwl.class)
@EnableConfigurationProperties(JpOwlProperties.class)
public class JpOwlAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public JpOwl jpOwl(JpOwlProperties properties) {
        JpOwl jpOwl = new JpOwl();
        jpOwl.setLogPrefix(properties.getLogPrefix());
        jpOwl.setLogLevel(properties.getLogLevel());
        return jpOwl;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public MonitorManager monitorManager(OutputHandler outputHandler) {
        return new MonitorManager(outputHandler);
    }
    
    @Bean
    @ConditionalOnProperty(prefix = "jpowl.trigger", name = "enabled", havingValue = "true")
    public AlertTrigger alertTrigger(JpOwlProperties properties) {
        return new AlertTrigger(properties.getTrigger().getType());
    }
} 