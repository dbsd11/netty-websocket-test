package group.bison.netty.websocket.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import group.bison.netty.websocket.controller.StatusReportController;

@Configuration
public class WebSocketConfiguration {

    @Bean
    public HandlerMapping websocketHandlerMapping(List<WebSocketHandler> webSocketHandlerList) {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);

        Map<String, WebSocketHandler> url2HandlerMap = new HashMap<>();
        for (WebSocketHandler webSocketHandler : webSocketHandlerList) {
            if(webSocketHandler instanceof StatusReportController) {
                url2HandlerMap.put("/status/report/wss", webSocketHandler);
            }
        }
        simpleUrlHandlerMapping.setUrlMap(url2HandlerMap);
        return simpleUrlHandlerMapping;
    }
}
