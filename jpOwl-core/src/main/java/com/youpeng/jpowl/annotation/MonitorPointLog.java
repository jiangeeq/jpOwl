package com.youpeng.jpowl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MonitorPointLog {
    // 触发器任务
    String trigger() default "";
    // 日志升级
    String levelUp() default "";
    // 日志降级
    String levelDown() default "";
    // 追加内容
    String appendMessage() default "";
    // 指定时间区间
    String beginTime() default "";
    String endTime() default "";
    // 次数阈值
    String threshold() default "";
    // 时间区别：每分钟，每秒
    String period() default "";
    // 数据模型
    String model() default "";
}
