package group.bison.netty.modules.statusreport.ark;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.sofa.ark.api.ArkClient;
import com.alipay.sofa.runtime.SofaBizUninstallEventHandler;
import com.alipay.sofa.runtime.invoke.DynamicJvmServiceProxyFinder;
import com.alipay.sofa.runtime.spring.AfterBizStartupEventHandler;
import com.alipay.sofa.runtime.spring.FinishStartupEventHandler;

@Configuration
@ConditionalOnExpression("T(Thread).currentThread().getContextClassLoader().getClass().getName().startsWith(\"com.alipay.sofa\")")
public class ArkClientConfiguration  {

    {
        System.out.println("inited ArkClientConfiguration by classLoader: " + Thread.currentThread().getContextClassLoader());
    }

    @Bean
    public static DynamicJvmServiceProxyFinder dynamicJvmServiceProxyFinder() {
        DynamicJvmServiceProxyFinder dynamicJvmServiceProxyFinder = DynamicJvmServiceProxyFinder.getDynamicJvmServiceProxyFinder();
        ArkClient.getInjectionService().inject(dynamicJvmServiceProxyFinder);
        return dynamicJvmServiceProxyFinder;
    }

    @Bean
    public static AfterBizStartupEventHandler afterBizStartupEventHandler() {
        AfterBizStartupEventHandler afterBizStartupEventHandler = new AfterBizStartupEventHandler();
        ArkClient.getEventAdminService().register(afterBizStartupEventHandler);
        return afterBizStartupEventHandler;
    }

    @Bean
    public static FinishStartupEventHandler finishStartupEventHandler() {
        FinishStartupEventHandler finishStartupEventHandler = new FinishStartupEventHandler();
        ArkClient.getEventAdminService().register(finishStartupEventHandler);
        return finishStartupEventHandler;
    }

    @Bean
    public static SofaBizUninstallEventHandler sofaBizUninstallEventHandler() {
        SofaBizUninstallEventHandler sofaBizUninstallEventHandler = new SofaBizUninstallEventHandler();
        ArkClient.getEventAdminService().register(sofaBizUninstallEventHandler);
        return sofaBizUninstallEventHandler;
    }
}


