package group.bison.netty.websocket.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketMessage.Type;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.google.protobuf.Descriptors.Descriptor;

import group.bison.netty.protoc.messages.WebSocketMessages;
import group.bison.netty.protoc.messages.WebSocketMessages.StatusReportValue;
import group.bison.netty.protoc.messages.WebSocketMessages.WebsocketRequest;
import group.bison.netty.protoc.messages.WebSocketMessages.WebsocketResponse;
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

        Flux<WebSocketMessage> reponseMessageFlux = session.receive().map(message -> {
                    WebSocketMessage reponseMessage = null;
                    if(message.getType() == Type.TEXT) {
                        String processResult = processStatusReport("websocket", message.getPayloadAsText());
                        reponseMessage = session.textMessage(processResult);
                    } else if (message.getType() == Type.BINARY) {
                        try {
                            WebsocketRequest webSocketRequest = WebsocketRequest.parseFrom(message.getPayload().asByteBuffer());
                            String websocketRequestValueClassName = null;
                            
                            for(Descriptor messageType : WebSocketMessages.getDescriptor().getMessageTypes()) {
                                if(webSocketRequest.getValue().getTypeUrl().contains(messageType.getFullName())) {
                                    websocketRequestValueClassName = String.join("", WebSocketMessages.class.getName(), "$", messageType.getName());
                                    break;
                                }
                            }

                            if(StringUtils.equals(websocketRequestValueClassName, StatusReportValue.class.getName())) {
                                StatusReportValue statusReportValue = webSocketRequest.getValue().unpack(StatusReportValue.class);
                                WebsocketResponse websocketResponse = processStatusReport("websocket", statusReportValue);
                                reponseMessage = session.binaryMessage((dataBufferFactory) -> {
                                    DataBuffer bb = dataBufferFactory.allocateBuffer();
                                    bb.write(websocketResponse.toByteArray());
                                    return bb;
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }   
                    return reponseMessage;
                }).filter(obj -> obj != null);
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

    WebsocketResponse processStatusReport(String from, WebSocketMessages.StatusReportValue statusReportValue) {
        System.out.println("receive from " + from + " status report online: " + statusReportValue.getOnline());
        WebsocketResponse response = WebsocketResponse.newBuilder().setCode(0).setMessage(from + " ok ").build();
        return response;
    }
}
