public class OutputSourceManager {
    private final Map<OutputSourceType, OutputSource> outputSources = new ConcurrentHashMap<>();
    private final LoadingCache<String, OutputSource> outputSourceCache;
    
    public OutputSourceManager() {
        this.outputSourceCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, OutputSource>() {
                @Override
                public OutputSource load(String key) {
                    return createOutputSource(key);
                }
            });
    }
    
    public void registerOutputSource(OutputSource outputSource) {
        outputSources.put(outputSource.getType(), outputSource);
    }
    
    public void write(MonitorData data, OutputSourceType type) {
        OutputSource outputSource = outputSources.get(type);
        if (outputSource != null) {
            try {
                outputSource.write(data);
            } catch (Exception e) {
                log.error("Failed to write data to output source: " + type, e);
            }
        }
    }
    
    public void writeBatch(List<MonitorData> dataList, OutputSourceType type) {
        OutputSource outputSource = outputSources.get(type);
        if (outputSource != null) {
            try {
                outputSource.writeBatch(dataList);
            } catch (Exception e) {
                log.error("Failed to write batch data to output source: " + type, e);
            }
        }
    }
} 