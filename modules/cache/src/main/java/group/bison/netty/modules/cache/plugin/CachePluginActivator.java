package group.bison.netty.modules.cache.plugin;

import com.alipay.sofa.ark.spi.model.PluginContext;
import com.alipay.sofa.ark.spi.service.PluginActivator;

public class CachePluginActivator implements PluginActivator {

    @Override
    public void start(PluginContext context) {
        System.out.println("start cache plugin");
    }

    @Override
    public void stop(PluginContext context) {
        System.out.println("stop cache plugin");
    }
}