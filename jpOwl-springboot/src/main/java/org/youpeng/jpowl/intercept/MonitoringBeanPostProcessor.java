package org.youpeng.jpowl.intercept;

import com.youpeng.jpowl.core.MonitorManager;
import com.youpeng.jpowl.model.Event;
import com.youpeng.jpowl.model.Transaction;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Proxy;

@Component
public class MonitoringBeanPostProcessor implements BeanPostProcessor {
    private final MonitorManager monitorManager = new MonitorManager();

    /**
     * 创建一个 BeanPostProcessor 来代理Service和Repository：
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Service.class) || bean.getClass().isAnnotationPresent(Repository.class)) {
            return Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
                Transaction transaction = new Transaction(method.getName());
                try {
                    Object result = method.invoke(bean, args);
                    transaction.complete();
                    return result;
                } catch (Exception e) {
                    Event event = new Event("Exception", e.getMessage());
                    transaction.addEvent(event);
                    throw e;
                } finally {
                    monitorManager.logTransaction(transaction);
                }
            });
        }
        return bean;
    }
}
