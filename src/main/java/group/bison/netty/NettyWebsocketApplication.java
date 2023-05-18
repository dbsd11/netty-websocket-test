package group.bison.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.alipay.sofa.ark.springboot.ArkAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class, ArkAutoConfiguration.class})
@EnableWebFlux
public class NettyWebsocketApplication {

    public static void main(String[] args) throws Exception {
        try {
            System.setProperty("sofa.ark.embed.enable", "true");
            System.setProperty("com.alipay.sofa.boot.jvmFilterEnable", "true");
            System.setProperty("com.alipay.sofa.boot.dynamicJvmServiceCacheEnable", "true");
            System.setProperty("com.alipay.sofa.boot.skipJvmSerialize", "true");
            SpringApplication application = new SpringApplication(NettyWebsocketApplication.class);
            application.setWebApplicationType(WebApplicationType.REACTIVE);
            application.run(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


