package org.youpeng.jpowl.intercept;

import com.youpeng.jpowl.core.MonitorManager;
import com.youpeng.jpowl.model.Event;
import com.youpeng.jpowl.model.Transaction;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MonitoringInterceptor implements HandlerInterceptor {
    private final MonitorManager monitorManager = new MonitorManager();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Transaction transaction = new Transaction("HTTP " + request.getMethod() + " " + request.getRequestURI());
        request.setAttribute("transaction", transaction);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Transaction transaction = (Transaction) request.getAttribute("transaction");
        if (transaction != null) {
            if (ex != null) {
                Event event = new Event("HTTP Exception", ex.getMessage());
                transaction.addEvent(event);
            }
            transaction.complete();
            monitorManager.logTransaction(transaction);
        }
    }
}