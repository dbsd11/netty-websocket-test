package group.bison.netty.modules.oauth2.plugin;

import com.alipay.sofa.ark.spi.model.PluginContext;
import com.alipay.sofa.ark.spi.service.PluginActivator;

public class Oauth2PluginActivator implements PluginActivator {

    @Override
    public void start(PluginContext context) {
        System.out.println("start oauth2 plugin");
    }

    @Override
    public void stop(PluginContext context) {
        System.out.println("start oauth2 plugin");
    }
}