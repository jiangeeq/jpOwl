package com.youpeng.jpowl.output.model;

/**
 * 负载状态
 */
public class LoadStatus {
    // CPU使用率 (0-100)
    private double cpuUsage;
    // 内存使用率 (0-100)
    private double memoryUsage;
    // 连接数使用率 (0-100)
    private double connectionUsage;
    // 磁盘IO使用率 (0-100)
    private double diskIoUsage;
    // 系统负载值
    private double systemLoad;
    // 是否过载
    private boolean overloaded;

    public static LoadStatus create(){
        return new LoadStatus();
    }
    public LoadStatus build(){
        return this;
    }
    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public double getConnectionUsage() {
        return connectionUsage;
    }

    public void setConnectionUsage(double connectionUsage) {
        this.connectionUsage = connectionUsage;
    }

    public double getDiskIoUsage() {
        return diskIoUsage;
    }

    public void setDiskIoUsage(double diskIoUsage) {
        this.diskIoUsage = diskIoUsage;
    }

    public double getSystemLoad() {
        return systemLoad;
    }

    public void setSystemLoad(double systemLoad) {
        this.systemLoad = systemLoad;
    }

    public boolean isOverloaded() {
        return overloaded;
    }

    public void setOverloaded(boolean overloaded) {
        this.overloaded = overloaded;
    }
    public LoadStatus cpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
        return this;
    }

    public LoadStatus memoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
        return this;
    }

    public LoadStatus connectionUsage(double connectionUsage) {
        this.connectionUsage = connectionUsage;
        return this;
    }

    public LoadStatus diskIoUsage(double diskIoUsage) {
        this.diskIoUsage = diskIoUsage;
        return this;
    }

    public LoadStatus systemLoad(double systemLoad) {
        this.systemLoad = systemLoad;
        return this;
    }

    public LoadStatus overloaded(boolean overloaded) {
        this.overloaded = overloaded;
        return this;
    }
}
