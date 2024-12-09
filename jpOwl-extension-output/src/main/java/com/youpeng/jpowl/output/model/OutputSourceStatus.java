package com.youpeng.jpowl.output.model;

import com.youpeng.jpowl.output.core.OutputSource;
import com.youpeng.jpowl.output.core.OutputSourceType;

import java.time.Instant;

public class OutputSourceStatus {
    private OutputSourceType type;
    private Status status;
    private Instant createdTime;
    private Instant lastUpdatedTime;
    private String lastError;

    public enum Status {
        CREATED,
        RUNNING,
        STOPPED,
        ERROR
    }

    public static OutputSourceStatus create(){
        return new OutputSourceStatus();
    }
    public OutputSourceStatus type(OutputSourceType type) {
        this.type = type;
        return this;
    }

    public OutputSourceStatus status(Status status) {
        this.status = status;
        return this;
    }

    public OutputSourceStatus createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public OutputSourceStatus lastUpdatedTime(Instant lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
        return this;
    }

    public OutputSourceStatus lastError(String lastError) {
        this.lastError = lastError;
        return this;
    }
    public OutputSourceStatus build() {
        return this;
    }

    public OutputSourceType getType() {
        return type;
    }

    public void setType(OutputSourceType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Instant lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
}
