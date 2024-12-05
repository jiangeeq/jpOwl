@Aspect
public class MonitorAspect {
    private final JpOwlCore jpOwlCore;
    
    public MonitorAspect(JpOwlCore jpOwlCore) {
        this.jpOwlCore = jpOwlCore;
    }
    
    @Around("@annotation(com.youpeng.jpowl.spring.annotation.Monitor)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().getName();
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = point.proceed();
            long duration = System.currentTimeMillis() - startTime;
            jpOwlCore.logTransaction(methodName, duration);
            return result;
        } catch (Throwable e) {
            jpOwlCore.logEvent("error", e.getMessage());
            throw e;
        }
    }
} 