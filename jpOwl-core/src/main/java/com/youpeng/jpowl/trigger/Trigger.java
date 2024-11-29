package com.youpeng.jpowl.trigger;

import com.youpeng.jpowl.model.MonitorModel;

public interface Trigger {
    void execute(MonitorModel model);
}
