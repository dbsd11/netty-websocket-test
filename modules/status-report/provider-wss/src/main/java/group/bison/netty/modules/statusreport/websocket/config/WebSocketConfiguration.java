package group.bison.netty.modules.statusreport.websocket.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import group.bison.netty.modules.springcontext.RootSpringContextHolder;
import group.bison.netty.modules.statusreport.provider_wss.StatusReportWssServiceImpl;

@Configuration
public class WebSocketConfiguration {

    @Bean
    public HandlerMapping websocketHandlerMapping(List<WebSocketHandler> webSocketHandlerList) {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);

        Map<String, WebSocketHandler> url2HandlerMap = new HashMap<>();
        for (WebSocketHandler webSocketHandler : webSocketHandlerList) {
            if(webSocketHandler instanceof StatusReportWssServiceImpl) {
                url2HandlerMap.put("/status/report/wss", webSocketHandler);
            }
        }
        simpleUrlHandlerMapping.setUrlMap(url2HandlerMap);

        if(RootSpringContextHolder.get() != null) {
            ApplicationContext rootApplicationContext = RootSpringContextHolder.get();
            
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)rootApplicationContext.getAutowireCapableBeanFactory();
            if(beanFactory.containsBean("websocketHandlerMappingInParent")) {
                beanFactory.destroySingleton("websocketHandlerMappingInParent");
            }
            beanFactory.registerSingleton("websocketHandlerMappingInParent", simpleUrlHandlerMapping);

            DispatcherHandler dispatcherHandler = rootApplicationContext.getBean(DispatcherHandler.class);
            dispatcherHandler.setApplicationContext(rootApplicationContext);
        }
        return simpleUrlHandlerMapping;
    }
}
