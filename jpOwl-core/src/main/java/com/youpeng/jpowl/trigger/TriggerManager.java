package com.youpeng.jpowl.trigger;

import com.youpeng.jpowl.model.MonitorModel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TriggerManager {
    private final List<Trigger> triggers = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    public void removeTrigger(Trigger trigger) {
        triggers.remove(trigger);
    }

    public void fireTriggers(MonitorModel model) {
        triggers.forEach(trigger -> 
            executor.submit(() -> {
                try {
                    trigger.execute(model);
                } catch (Exception e) {
                    // 处理触发器执行异常
                    e.printStackTrace();
                }
            })
        );
    }

    public void shutdown() {
        executor.shutdown();
    }
} 