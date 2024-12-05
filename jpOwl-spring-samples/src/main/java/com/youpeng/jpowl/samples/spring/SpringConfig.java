@Configuration
@EnableJpOwl
public class SpringConfig {
    
    @Bean
    public JpOwlProperties jpOwlProperties() {
        JpOwlProperties properties = new JpOwlProperties();
        properties.setFilePath("/var/log/jpowl.log");
        properties.setBufferSize(1024);
        return properties;
    }
} 