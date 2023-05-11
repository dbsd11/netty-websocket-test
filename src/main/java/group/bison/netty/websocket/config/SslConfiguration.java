package group.bison.netty.websocket.config;

import java.security.Security;
import java.util.concurrent.atomic.AtomicReference;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import net.tongsuo.TongsuoProvider;

@Configuration
public class SslConfiguration {

    public static AtomicReference<BouncyCastleProvider> BC = new AtomicReference();

    public static AtomicReference<TongsuoProvider> Tongsuo_Security_Provider = new AtomicReference<>();

    static {
        try {
            BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
            Security.addProvider(bouncyCastleProvider);
            BC.compareAndSet(null, bouncyCastleProvider);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
        try {
            TongsuoProvider tongsuoProvider = new TongsuoProvider();
            Tongsuo_Security_Provider.set(tongsuoProvider);
            Security.addProvider(tongsuoProvider);
            Tongsuo_Security_Provider.compareAndSet(null, tongsuoProvider);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
    }
}
