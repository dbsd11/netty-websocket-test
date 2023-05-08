package group.bison.netty.websocket.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/status")
public class StatusReportController implements WebSocketHandler {

    @RequestMapping("/report")
    public Mono reportFromHttp(@RequestBody String body) {
        String processResult = processStatusReport("http", body);
        return Mono.just(processResult);
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<String> source = Flux.just("client connected to server");
        Mono<Void> output1 = session.send(source.map(session::textMessage));

        Flux<WebSocketMessage> reponseMessageFlux = session.receive().map(message -> message.getPayloadAsText())
                .map(body -> {
                    String processResult = processStatusReport("websocket", body);
                    return session.textMessage(processResult);
                });
        Mono<Void> output2 = output1.then(session.send(reponseMessageFlux));
        return output2.then();
    }

    String processStatusReport(String from, String reportContent) {
        System.out.println("receive from " + from + " status report: " + reportContent);

        if("websocket".equals(from) && reportContent.contains("seq:")) {
            int seq = Integer.valueOf(reportContent.substring(reportContent.indexOf("seq:") + 4, reportContent.indexOf("seq:") + 7).trim()).intValue();
            return from + " ok " + "seq: " + seq;
        }
        return from + " ok ";
    }
}
