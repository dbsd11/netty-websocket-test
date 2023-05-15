package group.bison.netty.modules.statusreport.ark;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.sofa.runtime.api.annotation.SofaClientFactory;
import com.alipay.sofa.runtime.api.aware.ClientFactoryAware;
import com.alipay.sofa.runtime.api.client.ClientFactory;
import com.alipay.sofa.runtime.api.client.ServiceClient;
import com.alipay.sofa.runtime.api.client.param.ServiceParam;

import group.bison.netty.modules.statusreport.facade.StatusReportService;

@Configuration
public class ArkAutoConfiguration {

    {
        System.out.println("inited ArkAutoConfiguration ClassLoader:" + Thread.currentThread().getContextClassLoader());
    }

    @Bean
    public static ClientFactoryAware clientFactory(@Autowired StatusReportService statusReportService) {
        return (clientFactory) -> {
            ServiceClient serviceClient = clientFactory.getClient(ServiceClient.class);
            ServiceParam serviceParam = new ServiceParam();
            serviceParam.setInstance(statusReportService);
            serviceParam.setInterfaceType(StatusReportService.class);
            serviceClient.service(serviceParam);
        };
    }
    
}
