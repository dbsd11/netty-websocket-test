package group.bison.netty;

import net.tongsuo.TongsuoProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.security.Security;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableWebFlux
public class NettyWebsocketApplication {

    public static BouncyCastleProvider BC = new BouncyCastleProvider();

    public static TongsuoProvider Tongsuo_Security_Provider = new TongsuoProvider();

    public static void main(String[] args) throws Exception {
        try {
            Security.addProvider(BC);
            Security.addProvider(Tongsuo_Security_Provider);

            SpringApplication.run(NettyWebsocketApplication.class, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}


