package group.bison.netty.websocket.config;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import com.aliyun.gmsse.GMProvider;

@Configuration
public class TongsuoConfiguration {
    
    // private static final GMProvider provider = new GMProvider();

    static {
        // Security.addProvider(provider);
        Security.addProvider(new BouncyCastleProvider());

    }
}
