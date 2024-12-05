@Configuration
@EnableAspectJAutoProxy
public class JpOwlConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public JpOwlCore jpOwlCore(JpOwlProperties properties) {
        JpOwlCoreConfig config = new JpOwlCoreConfig.Builder()
            .outputType(properties.getOutputType())
            .filePath(properties.getFilePath())
            .bufferSize(properties.getBufferSize())
            .build();
        return new JpOwlCore(config);
    }
    
    @Bean
    public MonitorAspect monitorAspect(JpOwlCore jpOwlCore) {
        return new MonitorAspect(jpOwlCore);
    }
    
    @Bean
    @ConditionalOnProperty(prefix = "jpowl.alert", name = "enabled", havingValue = "true")
    public AlertExtension alertExtension(JpOwlProperties properties) {
        return new AlertExtension(properties.getAlert());
    }
} 