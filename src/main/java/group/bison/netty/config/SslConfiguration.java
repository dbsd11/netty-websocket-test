package group.bison.netty.config;

import java.security.Security;
import java.util.concurrent.atomic.AtomicReference;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import net.tongsuo.TongsuoProvider;

@Configuration
public class SslConfiguration {

    public static AtomicReference<BouncyCastleProvider> BC = new AtomicReference<>();

    public static AtomicReference<TongsuoProvider> Tongsuo_Security_Provider = new AtomicReference<TongsuoProvider>();

    static {
        try {
            BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
            Security.addProvider(bouncyCastleProvider);
            BC.set(bouncyCastleProvider);    
        } catch (Throwable e) {
            System.out.println("add BouncyCastleProvider failed");
        }
        
        try {
            TongsuoProvider tongsuoProvider = new TongsuoProvider();
            Security.addProvider(tongsuoProvider);
            Tongsuo_Security_Provider.set(tongsuoProvider);
        } catch (Throwable e) {
            System.out.println("add TongsuoProvider failed");
        }
    }
}