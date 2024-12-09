package com.youpeng.jpowl.core.output;

import com.youpeng.jpowl.core.model.MonitorEvent;
import com.youpeng.jpowl.core.enums.OutputSourceType;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 文件输出源抽象基类
 */
public abstract class AbstractFileOutputSource extends AbstractOutputSource {
    protected final File file;
    protected final AtomicBoolean closed = new AtomicBoolean(false);
    
    protected AbstractFileOutputSource(Map<String, Object> properties) {
        super(properties);
        String path = getProperty("path", "logs/monitor.log");
        this.file = new File(path);
        createParentDirs();
    }
    
    private void createParentDirs() {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
    
    @Override
    public OutputSourceType getType() {
        return OutputSourceType.FILE;
    }
    
    @Override
    public void close() {
        closed.set(true);
    }
    
    protected void checkClosed() {
        if (closed.get()) {
            throw new IllegalStateException("Output source is closed");
        }
    }
} 