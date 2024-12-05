public class JpOwlCoreConfig {
    private OutputType outputType = OutputType.FILE;
    private String filePath;
    private int bufferSize = 1024;
    private LogLevel defaultLogLevel = LogLevel.INFO;
    
    public enum OutputType {
        FILE, MEMORY, ELASTICSEARCH, MONGODB
    }
    
    // Builder模式构建配置
    public static class Builder {
        private final JpOwlCoreConfig config = new JpOwlCoreConfig();
        
        public Builder outputType(OutputType outputType) {
            config.outputType = outputType;
            return this;
        }
        
        public Builder filePath(String filePath) {
            config.filePath = filePath;
            return this;
        }
        
        public Builder bufferSize(int bufferSize) {
            config.bufferSize = bufferSize;
            return this;
        }
        
        public JpOwlCoreConfig build() {
            return config;
        }
    }
} 