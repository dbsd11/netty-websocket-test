package group.bison.netty.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import com.alipay.sofa.runtime.api.annotation.SofaReference;

import group.bison.netty.modules.statusreport.facade.StatusReportService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/status")
public class StatusReportController implements ApplicationContextAware {

    @SofaReference
    private StatusReportService statusReportService;

    private ApplicationContext applicationContext;

    @RequestMapping("/report")
    public Mono reportFromHttp(@RequestBody String body) {
        String processResult = statusReportService.onReport("http", body);
        return Mono.just(processResult);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }
}
