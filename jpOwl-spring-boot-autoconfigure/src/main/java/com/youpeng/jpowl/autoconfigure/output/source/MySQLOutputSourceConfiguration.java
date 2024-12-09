/**
 * MySQL输出源配置类
 */
@Configuration
@ConditionalOnClass(MySQLOutputSource.class)
@ConditionalOnProperty(prefix = JpOwlConstants.Output.MYSQL_PREFIX, name = "enabled", havingValue = "true")
public class MySQLOutputSourceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MySQLOutputSource mysqlOutputSource(JpOwlProperties properties) {
        MySQLProperties mysqlProps = properties.getOutput().getMysql();
        return new MySQLOutputSource(mysqlProps);
    }

    @Bean
    @ConditionalOnMissingBean
    public MySQLLoadDetector mysqlLoadDetector(MySQLOutputSource mysqlOutputSource) {
        return new MySQLLoadDetector(mysqlOutputSource);
    }
} 