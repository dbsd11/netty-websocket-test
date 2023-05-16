package group.bison.netty.ark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.sofa.ark.springboot.condition.ConditionalOnArkEnabled;
import com.alipay.sofa.ark.springboot.processor.ArkEventHandlerProcessor;
import com.alipay.sofa.ark.springboot.processor.ArkServiceInjectProcessor;
import com.alipay.sofa.runtime.SofaBizUninstallEventHandler;
import com.alipay.sofa.runtime.invoke.DynamicJvmServiceProxyFinder;
import com.alipay.sofa.runtime.spring.AfterBizStartupEventHandler;
import com.alipay.sofa.runtime.spring.FinishStartupEventHandler;

@Configuration
@ConditionalOnArkEnabled
public class ArkServiceConfiguration {



    
    
    @Bean
    public static ArkServiceInjectProcessor serviceInjectProcessor() {
        return new ArkServiceInjectProcessor();
    }
 
    @Bean
    public static ArkEventHandlerProcessor arkEventHandlerProcessor() {
        return new ArkEventHandlerProcessor();
    }

    @Bean
    public static DynamicJvmServiceProxyFinder dynamicJvmServiceProxyFinder() {
        return DynamicJvmServiceProxyFinder.getDynamicJvmServiceProxyFinder();
    }

    @Bean
    public static AfterBizStartupEventHandler afterBizStartupEventHandler() {
        return new AfterBizStartupEventHandler();
    }

    @Bean
    public static FinishStartupEventHandler finishStartupEventHandler() {
        return new FinishStartupEventHandler();
    }

    @Bean
    public static SofaBizUninstallEventHandler sofaBizUninstallEventHandler() {
        return new SofaBizUninstallEventHandler();
    }
}


