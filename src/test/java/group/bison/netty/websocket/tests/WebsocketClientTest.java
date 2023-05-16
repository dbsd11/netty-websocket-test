 package group.bison.netty.websocket.tests;

 import java.net.URI;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.jsr356.ClientContainer;
import org.eclipse.jetty.websocket.jsr356.JettyClientContainerProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.protobuf.Any;

import group.bison.netty.protoc.messages.WebSocketMessages.Event;
import group.bison.netty.protoc.messages.WebSocketMessages.StatusReportValue;
import group.bison.netty.protoc.messages.WebSocketMessages.WebsocketRequest;
import group.bison.netty.protoc.messages.WebSocketMessages.WebsocketResponse;

 @RunWith(JUnit4.class)
 public class WebsocketClientTest {

     static {
         System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
         System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
     }

     @Test
     public void test() throws Exception {
         long connectStartTime = System.currentTimeMillis();

         ClientContainer container = (ClientContainer) JettyClientContainerProvider.getWebSocketContainer();
         container.setDefaultMaxSessionIdleTimeout(30l * 24 * 60 * 60 * 1000);
         container.getClient().setConnectTimeout(30l * 24 * 60 * 60 * 1000);

         TrustManager[] tms = new TrustManager[]{new X509TrustManager() {
             @Override
             public X509Certificate[] getAcceptedIssuers() {
                 return new X509Certificate[0];
             }

             @Override
             public void checkClientTrusted(X509Certificate[] certs, String authType) {
             }

             @Override
             public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
             }

         }};

         // init SSLSocketFactory
        //  SSLContext sc = SSLContext.getInstance("TLSv1.3", new TongsuoProvider());
        SSLContext sc = SSLContext.getInstance("TLS");
         sc.init(null, tms, new SecureRandom());

         SslContextFactory sslContextFactory = container.getClient().getHttpClient().getSslContextFactory();
         sslContextFactory.setSslContext(sc);

         Session session = container.connectToServer(MyClientEndpoint.class, new URI(
                 "wss://localhost/status/report/wss"));
         session.setMaxIdleTimeout(30l * 24 * 60 * 60 * 1000);
         System.out.println("connect success cost time :"
                 + (System.currentTimeMillis() - connectStartTime));
        
         for(int i =0; i< 20; i++) {
             try {
                 session.getBasicRemote().sendPing(ByteBuffer.wrap("{}".getBytes()));
                 if(i%3==0) {
                    StatusReportValue statusReportValue = StatusReportValue.newBuilder().setOnline(i % 2 == 0).build();
                    WebsocketRequest websocketRequest = WebsocketRequest.newBuilder().setEvent(Event.ON_LINE).setValue(Any.pack(statusReportValue)).build();
                    session.getBasicRemote().sendBinary(ByteBuffer.wrap(websocketRequest.toByteArray()));;
                 } else {
                    session.getBasicRemote().sendText("report seq: " + i + " device online: " + (i % 2 == 0));
                 }
                 Thread.sleep(1000);
             } catch (Exception e) {
                 e.printStackTrace();
                 break;
             }
         }

         container.stop();
     }

     @ClientEndpoint
     public static class MyClientEndpoint extends Endpoint {

         @Override
         public void onOpen(Session session, EndpointConfig config) {
             System.out.println("websocket connection is opened.");
             session.addMessageHandler(new MyClientMessageListener());
             session.addMessageHandler(new MyClientMessageListener2());
         }

         @Override
         public void onClose(Session session, CloseReason closeReason) {
             System.out.println("websocket connection is closed." + closeReason);
         }

         @Override
         public void onError(Session session, Throwable thr) {
             System.out.println("websocket connection is error." + thr);
         }

     }

     public static class MyClientMessageListener implements MessageHandler.Whole<String> {

         @Override
         public void onMessage(String message) {
             try {
                 System.out.println("i receive a message=" + message);
             } catch (Exception e) {
             }

         }
     }

     public static class MyClientMessageListener2 implements MessageHandler.Whole<byte[]> {

        @Override
        public void onMessage(byte[] bytes) {
            try {
                WebsocketResponse websocketResponse = WebsocketResponse.parseFrom(bytes);
                System.out.println("i receive a message=" + websocketResponse.toString());
            } catch (Exception e) {
            }
        }
    }
 }
