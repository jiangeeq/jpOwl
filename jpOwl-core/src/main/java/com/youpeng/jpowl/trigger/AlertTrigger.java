package com.youpeng.jpowl.trigger;

import com.youpeng.jpowl.aggregator.MetricAggregator;
import com.youpeng.jpowl.model.MonitorModel;
import org.springframework.stereotype.Component;

@Component
public class AlertTrigger implements Trigger {
    private final String alertMethod; // "email", "dingding", "sms"

    public AlertTrigger(String alertMethod) {
        this.alertMethod = alertMethod;
    }

    @Override
    public void execute(MonitorModel model) {
        String key = model.getMessage();
        long failureCount = MetricAggregator.getFailureCount(key);
        long executionCount = MetricAggregator.getExecutionCount(key);

        if (failureCount > 10 || executionCount > 100) {
            sendAlert(model);
        }
    }

    private void sendAlert(MonitorModel model) {
        if ("email".equals(alertMethod)) {
            sendEmailAlert(model);
        } else if ("dingding".equals(alertMethod)) {
            sendDingDingAlert(model);
        } else if ("sms".equals(alertMethod)) {
            sendSmsAlert(model);
        }
    }

    private void sendEmailAlert(MonitorModel model) {
        System.out.println("Sending email alert for: " + model.getMessage());
    }

    private void sendDingDingAlert(MonitorModel model) {
        System.out.println("Sending DingDing alert for: " + model.getMessage());
    }

    private void sendSmsAlert(MonitorModel model) {
        System.out.println("Sending SMS alert for: " + model.getMessage());
    }
}
