package group.bison.netty.modules.statusreport.provider_wss;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
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

@Service
public class StatusReportWssServiceImpl implements WebSocketHandler {

    {
        System.out.println("inited StatusReportWssServiceImpl ClassLoader:" + Thread.currentThread().getContextClassLoader());
    }

    public WebsocketResponse onReport(String from, WebSocketMessages.StatusReportValue statusReportValue) {
        System.out.println("receive from " + from + " status report online: " + statusReportValue.getOnline());
        WebsocketResponse websocketResponse = WebsocketResponse.newBuilder().setCode(0).setMessage(from + " ok ").build();
        return websocketResponse;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<String> source = Flux.just("client connected to server");
        Mono<Void> output1 = session.send(source.map(session::textMessage));

        Flux<WebSocketMessage> reponseMessageFlux = session.receive().map(message -> {
                    WebSocketMessage reponseMessage = null;
                    if(message.getType() == Type.TEXT) {
                        // do nothing
                        reponseMessage = session.textMessage("hello");
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
                                WebsocketResponse websocketResponse = onReport("websocket", statusReportValue);
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
                }).mapNotNull(Function.identity());
        Mono<Void> output2 = output1.then(session.send(reponseMessageFlux));
        return output2.then();
    }
}
