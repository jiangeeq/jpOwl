@ConfigurationProperties(prefix = "jpowl")
@Validated
public class JpOwlProperties {
    /**
     * 是否启用jpOwl
     */
    private boolean enabled = true;

    /**
     * 输出源配置
     */
    private Output output = new Output();

    /**
     * 监控配置
     */
    private Monitor monitor = new Monitor();

    @Data
    public static class Output {
        /**
         * 输出源列表
         */
        private List<OutputSourceProperties> sources = new ArrayList<>();
    }

    @Data
    public static class OutputSourceProperties {
        /**
         * 输出源类型
         */
        private OutputSourceType type = OutputSourceType.FILE;

        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 输出源特定配置
         */
        private Map<String, Object> properties = new HashMap<>();
    }

    @Data
    public static class Monitor {
        /**
         * 是否启用方法监控
         */
        private boolean methodEnabled = true;

        /**
         * 采样率
         */
        @Range(min = 0, max = 100)
        private int samplingRate = 100;
    }
} 