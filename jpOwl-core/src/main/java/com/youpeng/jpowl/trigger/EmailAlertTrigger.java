package com.youpeng.jpowl.trigger;

import com.youpeng.jpowl.model.MonitorModel;
import com.youpeng.jpowl.model.Transaction;

public class EmailAlertTrigger implements Trigger {
    @Override
    public void execute(MonitorModel model) {
        // 触发发送邮件的逻辑
        if (model instanceof Transaction) {
            Transaction transaction = (Transaction) model;
            if (transaction.getDuration() > 5000) {
                sendEmailAlert(transaction);
            }
        }
    }

    private void sendEmailAlert(Transaction transaction) {
        // 实现发送邮件的逻辑
        System.out.println("Sending email alert for long transaction: " + transaction.getMessage());
    }
}