package com.youpeng.jpowl.logging.decorator;

import com.youpeng.jpowl.logging.model.LogEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostInfoDecorator implements LogEventDecorator {
    private final String hostName;
    private final String hostAddress;
    
    public HostInfoDecorator() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            this.hostName = localHost.getHostName();
            this.hostAddress = localHost.getHostAddress();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Failed to get host info", e);
        }
    }
    
    @Override
    public LogEvent decorate(LogEvent event) {
        return event
            .addMdc("hostName", hostName)
            .addMdc("hostAddress", hostAddress);
    }

} 