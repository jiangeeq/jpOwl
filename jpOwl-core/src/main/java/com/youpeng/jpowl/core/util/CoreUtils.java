package com.youpeng.jpowl.core.util;

import com.youpeng.jpowl.core.constants.CoreConstants;
import java.net.InetAddress;
import java.util.UUID;

/**
 * 核心工具类
 * 提供各种通用工具方法
 * 
 * @author youpeng
 * @since 1.0.0
 */
public final class CoreUtils {
    
    private CoreUtils() {}  // 防止实例化
    
    /**
     * 获取主机名
     *
     * @return 主机名，如果获取失败则返回unknown
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return CoreConstants.UNKNOWN_SOURCE;
        }
    }
    
    /**
     * 获取应用名称
     *
     * @return 应用名称，如果未配置则返回unknown
     */
    public static String getAppName() {
        return System.getProperty(
            CoreConstants.PROP_APP_NAME, 
            CoreConstants.UNKNOWN_SOURCE
        );
    }
    
    /**
     * 生成唯一ID
     *
     * @return UUID字符串
     */
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 获取调用者信息
     *
     * @return 调用者类名和方法名
     */
    public static String getCallerInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            StackTraceElement caller = stackTrace[3];
            return caller.getClassName() + "." + caller.getMethodName();
        }
        return CoreConstants.UNKNOWN_SOURCE;
    }
} 