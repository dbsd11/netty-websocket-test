package group.bison.netty.modules.oauth2.plugin;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import com.alipay.sofa.ark.spi.model.PluginContext;
import com.alipay.sofa.ark.spi.service.PluginActivator;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
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