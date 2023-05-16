package group.bison.netty.modules.springcontext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RootSpringContextAware implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(RootSpringContextHolder.get() == null) {
            RootSpringContextHolder.setRootApplicationContext(applicationContext);
        } else if(RootSpringContextHolder.get() != null && applicationContext != RootSpringContextHolder.get() && (applicationContext instanceof GenericApplicationContext)){
            ((GenericApplicationContext)applicationContext).setParent(RootSpringContextHolder.get());
        }
    }
}
