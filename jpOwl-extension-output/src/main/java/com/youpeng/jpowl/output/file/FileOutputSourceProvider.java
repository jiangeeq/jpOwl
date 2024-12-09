package com.youpeng.jpowl.output.file;

import com.youpeng.jpowl.core.spi.OutputSourceProvider;
import com.youpeng.jpowl.core.api.OutputSource;
import com.youpeng.jpowl.core.enums.OutputSourceType;
import java.util.Map;

public class FileOutputSourceProvider implements OutputSourceProvider {
    @Override
    public OutputSource createOutputSource(Map<String, Object> properties) {
        return new FileOutputSource(properties);
    }
    
    @Override
    public OutputSourceType getType() {
        return OutputSourceType.FILE;
    }
} 