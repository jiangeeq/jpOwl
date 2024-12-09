/**
 * JpOwl自动配置类
 * 负责自动装配JpOwl的核心组件
 */
@Configuration
@ConditionalOnClass(JpOwl.class)
@EnableConfigurationProperties(JpOwlProperties.class)
@Import({
    OutputConfiguration.class,
    MonitorConfiguration.class
})
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
    @ConditionalOnProperty(prefix = "jpowl.trigger", name = "enabled", havingValue = "true")
    public AlertTrigger alertTrigger(JpOwlProperties properties) {
        return new AlertTrigger(properties.getTrigger().getType());
    }
} 