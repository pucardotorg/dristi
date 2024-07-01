package org.pucar.dristi.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Assert.notNull(applicationContext, "ApplicationContext must not be null");
        SpringContext.context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        Assert.state(context != null, "ApplicationContext has not been set");
        return context.getBean(beanClass);
    }

    public static ApplicationContext getApplicationContext() {
        Assert.state(context != null, "ApplicationContext has not been set");
        return context;
    }
}