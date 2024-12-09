package com.youpeng.jpowl.core.model;

import com.youpeng.jpowl.core.enums.AlertLevel;

/**
 * MonitorModel构建器
 */
public class MonitorModelBuilder {
    private String id;
    private AlertLevel level = AlertLevel.INFO;
    private String title;
    private String content;
    private String source;
    private Object data;

    public static MonitorModelBuilder create() {
        return new MonitorModelBuilder();
    }

    public MonitorModelBuilder id(String id) {
        this.id = id;
        return this;
    }

    public MonitorModelBuilder level(AlertLevel level) {
        this.level = level;
        return this;
    }

    public MonitorModelBuilder title(String title) {
        this.title = title;
        return this;
    }

    public MonitorModelBuilder content(String content) {
        this.content = content;
        return this;
    }

    public MonitorModelBuilder source(String source) {
        this.source = source;
        return this;
    }

    public MonitorModelBuilder data(Object data) {
        this.data = data;
        return this;
    }

    public MonitorModel build() {
        return new MonitorModel(id, level, title, content, source, data);
    }
} 