package group.bison.netty.modules.db.plugin;

import com.alipay.sofa.ark.spi.model.PluginContext;
import com.alipay.sofa.ark.spi.service.PluginActivator;

public class DBPluginActivator implements PluginActivator {

    @Override
    public void start(PluginContext context) {
        System.out.println("start db plugin");
    }

    @Override
    public void stop(PluginContext context) {
        System.out.println("stop db plugin");
    }
}