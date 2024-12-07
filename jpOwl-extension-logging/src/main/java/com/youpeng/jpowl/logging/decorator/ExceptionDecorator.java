package com.youpeng.jpowl.logging.decorator;


import com.youpeng.jpowl.logging.model.LogEvent;
import com.youpeng.jpowl.logging.model.LogLevel;

import java.util.*;

public class ExceptionDecorator implements LogEventDecorator {
    private static final int MAX_STACK_DEPTH = 20;  // 最大堆栈深度
    private static final String CAUSED_BY = "Caused by: ";

    @Override
    public LogEvent decorate(LogEvent event) {
        if (event.getLevel() == LogLevel.ERROR &&
            event.getMessage().contains("Exception")) {
            return event
                .addMdc("errorType", extractErrorType(event.getThrowable()))
                .addMdc("stackTrace", getStackTrace(event.getThrowable()))
                .addMdc("rootCause", Objects.requireNonNull(getRootCause(event.getThrowable())));
        }
        return event;
    }

    /**
     * 从日志事件中提取异常对象
     */
    private Throwable extractThrowable(LogEvent event) {
        Object[] params = event.getArgs();
        if (params != null) {
            for (Object param : params) {
                if (param instanceof Throwable) {
                    return (Throwable) param;
                }
            }
        }
        return null;
    }

    /**
     * 提取异常类型
     */
    private String extractErrorType(Throwable throwable) {
        StringBuilder errorType = new StringBuilder();
        errorType.append(throwable.getClass().getName());

        String message = throwable.getMessage();
        if (message != null && !message.isEmpty()) {
            errorType.append(": ").append(message);
        }

        return errorType.toString();
    }

    /**
     * 获取完整堆栈信息
     */
    private String getStackTrace(Throwable throwable) {
        StringBuilder stackTrace = new StringBuilder();

        // 处理主异常
        stackTrace.append(extractErrorType(throwable)).append("\n");
        addStackElements(throwable, stackTrace, "");

        // 处理cause链
        Throwable cause = throwable.getCause();
        Set<Throwable> dejaVu = new HashSet<>(); // 防止循环引用
        dejaVu.add(throwable);

        while (cause != null && !dejaVu.contains(cause)) {
            dejaVu.add(cause);
            stackTrace.append(CAUSED_BY)
                    .append(extractErrorType(cause))
                    .append("\n");
            addStackElements(cause, stackTrace, "\t");
            cause = cause.getCause();
        }

        return stackTrace.toString();
    }

    /**
     * 添加堆栈元素
     */
    private void addStackElements(Throwable throwable,
                                  StringBuilder stackTrace, String prefix) {
        StackTraceElement[] elements = throwable.getStackTrace();
        int depth = Math.min(elements.length, MAX_STACK_DEPTH);

        for (int i = 0; i < depth; i++) {
            stackTrace.append(prefix)
                    .append("\tat ")
                    .append(formatStackTraceElement(elements[i]))
                    .append("\n");
        }

        if (elements.length > depth) {
            stackTrace.append(prefix)
                    .append("\t... ")
                    .append(elements.length - depth)
                    .append(" more\n");
        }
    }

    /**
     * 格式化单个堆栈元素
     */
    private String formatStackTraceElement(StackTraceElement element) {
        StringBuilder sb = new StringBuilder();
        sb.append(element.getClassName())
                .append(".")
                .append(element.getMethodName());

        if (element.isNativeMethod()) {
            sb.append("(Native Method)");
        } else if (element.getFileName() != null) {
            sb.append("(")
                    .append(element.getFileName());
            if (element.getLineNumber() >= 0) {
                sb.append(":")
                        .append(element.getLineNumber());
            }
            sb.append(")");
        } else {
            sb.append("(Unknown Source)");
        }

        return sb.toString();
    }

    /**
     * 获取根本原因
     */
    private String getRootCause(Throwable throwable) {
        List<Throwable> chain = new ArrayList<>();
        Set<Throwable> dejaVu = new HashSet<>(); // 防止循环引用

        // 构建异常链
        while (throwable != null && !dejaVu.contains(throwable)) {
            chain.add(throwable);
            dejaVu.add(throwable);
            throwable = throwable.getCause();
        }

        // 获取最后一个异常（根本原因）
        if (!chain.isEmpty()) {
            Throwable root = chain.get(chain.size() - 1);
            return extractErrorType(root);
        }

        return null;
    }
} 