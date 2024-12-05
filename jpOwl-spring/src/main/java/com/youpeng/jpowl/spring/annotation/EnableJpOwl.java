@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(JpOwlConfiguration.class)
public @interface EnableJpOwl {
    String value() default "";
    
    // 是否启用方法监控
    boolean enableMethodMonitor() default true;
    
    // 是否启用告警
    boolean enableAlert() default false;
} 