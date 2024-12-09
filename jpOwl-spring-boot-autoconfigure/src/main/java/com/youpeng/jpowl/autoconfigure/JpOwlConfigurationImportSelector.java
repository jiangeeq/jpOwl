/**
 * JpOwl配置导入选择器
 * 用于根据条件动态导入配置类
 */
public class JpOwlConfigurationImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
            JpOwlAutoConfiguration.class.getName(),
            OutputConfiguration.class.getName(),
            MonitorConfiguration.class.getName()
        };
    }
} 