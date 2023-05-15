package group.bison.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.alipay.sofa.ark.springboot.ArkAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class, ArkAutoConfiguration.class})
@EnableWebFlux
public class NettyWebsocketApplication {

    public static void main(String[] args) throws Exception {
        try {
            SpringApplication.run(NettyWebsocketApplication.class, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


