package group.bison.netty.ark.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.sofa.ark.springboot.condition.ConditionalOnArkEnabled;
import com.alipay.sofa.ark.springboot.processor.ArkEventHandlerProcessor;
import com.alipay.sofa.ark.springboot.processor.ArkServiceInjectProcessor;

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
}


