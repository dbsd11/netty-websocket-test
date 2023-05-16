package group.bison.netty.modules.statusreport;

import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        try {
            System.setProperty("com.alipay.sofa.boot.jvmFilterEnable", "true");
            System.setProperty("com.alipay.sofa.boot.dynamicJvmServiceCacheEnable", "true");
            System.setProperty("com.alipay.sofa.boot.skipJvmSerialize", "true");
            
            context = new AnnotationConfigApplicationContext("group.bison.netty.modules", "com.alipay.sofa");
            context.start();
            System.out.println("started status report wss with args: " + Arrays.toString(args));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
