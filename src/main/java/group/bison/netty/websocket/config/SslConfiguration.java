package group.bison.netty.websocket.config;

import net.tongsuo.TongsuoProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class SslConfiguration {

    public static BouncyCastleProvider BC = new BouncyCastleProvider();

    public static TongsuoProvider Tongsuo_Security_Provider = new TongsuoProvider();

    static {
        Security.addProvider(BC);
        Security.addProvider(Tongsuo_Security_Provider);
    }
}
