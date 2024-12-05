@ConfigurationProperties(prefix = "jpowl")
@Validated
public class JpOwlProperties {
    /**
     * 是否启用jpOwl
     */
    private boolean enabled = true;

    /**
     * 日志输出类型
     */
    private OutputType outputType = OutputType.FILE;

    /**
     * 日志文件路径
     */
    @NotEmpty(message = "日志文件路径不能为空")
    private String filePath;

    /**
     * 缓冲区大小
     */
    @Min(value = 64, message = "缓冲区大小不能小于64")
    @Max(value = 8192, message = "缓冲区大小不能大于8192")
    private int bufferSize = 1024;

    /**
     * 监控配置
     */
    private Monitor monitor = new Monitor();

    /**
     * 告警配置
     */
    private Alert alert = new Alert();

    @Data
    public static class Monitor {
        /**
         * 默认日志级别
         */
        private LogLevel logLevel = LogLevel.INFO;

        /**
         * 是否启用方法监控
         */
        private boolean methodMonitorEnabled = true;

        /**
         * 监控采样率(0-100)
         */
        @Range(min = 0, max = 100)
        private int samplingRate = 100;
    }

    @Data
    public static class Alert {
        /**
         * 是否启用告警
         */
        private boolean enabled = false;

        /**
         * 告警方式配置
         */
        private Map<String, AlertChannel> channels = new HashMap<>();
    }

    @Data
    public static class AlertChannel {
        /**
         * 是否启用该渠道
         */
        private boolean enabled = false;

        /**
         * 告警阈值
         */
        private int threshold = 100;

        /**
         * 告警间隔(秒)
         */
        private int interval = 300;
    }
} 