package com.youpeng.jpowl.output.provider.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicLong;

public class AsyncFileChannel {
    private final AsynchronousFileChannel channel;
    private final AtomicLong position = new AtomicLong(0);
    
    private AsyncFileChannel(AsynchronousFileChannel channel) {
        this.channel = channel;
    }
    
    public static AsyncFileChannel open(Path path) {
        try {
            AsynchronousFileChannel channel = AsynchronousFileChannel.open(
                path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            return new AsyncFileChannel(channel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to open file channel", e);
        }
    }
    
    public void write(ByteBuffer buffer, long position) {
        channel.write(buffer, position, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer result, Void attachment) {
                AsyncFileChannel.this.position.addAndGet(result);
            }
            
            @Override
            public void failed(Throwable exc, Void attachment) {
                throw new RuntimeException("Failed to write to file", exc);
            }
        });
    }
    
    public long size() {
        return position.get();
    }
    
    public void close() throws IOException {
        channel.close();
    }
} 