package group.bison.netty.modules.servletcontext;

import group.bison.netty.modules.springcontext.RootSpringContextHolder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Method;

public class ServletContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Class webApplicationContextClass = null;
        try {
            webApplicationContextClass = Class.forName("org.springframework.web.context.ConfigurableWebApplicationContext");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(webApplicationContextClass == null) {
            return;
        }

        if(RootSpringContextHolder.get() == null || !(webApplicationContextClass.isAssignableFrom(RootSpringContextHolder.get().getClass()))) {
            return;
        }

        if(!(webApplicationContextClass.isAssignableFrom(applicationContext.getClass()))) {
            return;
        }

        try {
            Method getServletConfigMethod = RootSpringContextHolder.get().getClass().getMethod("getServletConfig");
            Object servletConfigObj = getServletConfigMethod.invoke(RootSpringContextHolder.get());
            Method setServletConfigMethod = applicationContext.getClass().getMethod("setServletConfig", Class.forName("javax.servlet.ServletConfig"));
            setServletConfigMethod.invoke(applicationContext, servletConfigObj);

            Method getServletContextMethod = RootSpringContextHolder.get().getClass().getMethod("getServletContext");
            Object servletContextObj = getServletContextMethod.invoke(RootSpringContextHolder.get());
            Method setServletContextMethod = applicationContext.getClass().getMethod("setServletContext", Class.forName("javax.servlet.ServletContext"));
            setServletContextMethod.invoke(applicationContext, servletContextObj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
