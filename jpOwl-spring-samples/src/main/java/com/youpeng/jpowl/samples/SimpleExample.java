public class SimpleExample {
    public static void main(String[] args) {
        JpOwlCoreConfig config = new JpOwlCoreConfig.Builder()
            .outputType(OutputType.FILE)
            .filePath("/path/to/log")
            .build();
            
        JpOwlCore jpOwl = new JpOwlCore(config);
        
        try {
            jpOwl.logTransaction("test", 100);
        } finally {
            jpOwl.shutdown();
        }
    }
} 