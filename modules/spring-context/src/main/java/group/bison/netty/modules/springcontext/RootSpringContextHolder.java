package group.bison.netty.modules.springcontext;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.context.ApplicationContext;

public class RootSpringContextHolder {

    private static volatile AtomicReference<ApplicationContext> rootApplicationContextHolder = new AtomicReference<ApplicationContext>();

    public static void setRootApplicationContext(ApplicationContext rootApplicationContext) {
        rootApplicationContextHolder.set(rootApplicationContext);
    }

    public static ApplicationContext get() {
        return rootApplicationContextHolder.get();
    }
}
