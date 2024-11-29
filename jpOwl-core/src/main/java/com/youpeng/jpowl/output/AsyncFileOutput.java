package com.youpeng.jpowl.output;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsyncFileOutput implements OutputHandler {
    private final RetryPolicy<Object> retryPolicy;

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final String filePath;

    public AsyncFileOutput(String filePath) {
        this.filePath = filePath;
        new Thread(this::processQueue).start();
// 在数据传输和持久化过程中增加更多的容错机制，确保数据完整性。
        retryPolicy = new RetryPolicy<>()
                    .handle(IOException.class)
                    .withBackoff(1, 5, ChronoUnit.SECONDS)
                    .withMaxRetries(3);
        }
        private void processQueue() {
            Failsafe.with(retryPolicy).run(() -> {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                    while (true) {
                        String message = queue.take();
                        writer.write(message);
                        writer.newLine();
                        writer.flush(); //如果注释掉这行,数据可能不会被写入
                    }
                } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void write(String message) {
        queue.offer(message);
    }

}
