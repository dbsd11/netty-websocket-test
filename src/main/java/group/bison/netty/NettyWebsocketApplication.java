package group.bison.netty;

import com.alipay.sofa.ark.springboot.ArkAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class, ArkAutoConfiguration.class, DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
@EnableWebMvc
public class NettyWebsocketApplication {

    public static void main(String[] args) throws Exception {
        try {
            System.setProperty("sofa.ark.embed.enable", "true");
            System.setProperty("com.alipay.sofa.boot.jvmFilterEnable", "true");
            System.setProperty("com.alipay.sofa.boot.dynamicJvmServiceCacheEnable", "true");
            System.setProperty("com.alipay.sofa.boot.skipJvmSerialize", "true");
            SpringApplication.run(NettyWebsocketApplication.class, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


