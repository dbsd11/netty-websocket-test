// package group.bison.jetty.websocket.tests;

// import java.net.URI;
// import java.nio.ByteBuffer;
// import java.security.Security;

// import javax.net.ssl.SSLContext;
// import javax.net.ssl.SSLSocketFactory;
// import javax.websocket.ClientEndpoint;
// import javax.websocket.CloseReason;
// import javax.websocket.Endpoint;
// import javax.websocket.EndpointConfig;
// import javax.websocket.MessageHandler;
// import javax.websocket.Session;

// import org.bouncycastle.jce.provider.BouncyCastleProvider;
// import org.eclipse.jetty.client.HttpClient;
// import org.eclipse.jetty.util.ssl.SslContextFactory;
// import org.eclipse.jetty.websocket.jsr356.ClientContainer;
// import org.eclipse.jetty.websocket.jsr356.JettyClientContainerProvider;

// import com.aliyun.gmsse.GMProvider;

// public class WebsocketClientTest {

//     static {
//         System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
//         System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
//     }

//     public static void main(String[] args) throws Exception {
//         long connectStartTime = System.currentTimeMillis();

//         ClientContainer container = (ClientContainer) JettyClientContainerProvider.getWebSocketContainer();
//         container.setDefaultMaxSessionIdleTimeout(30l * 24 * 60 * 60 * 1000);
//         container.getClient().setConnectTimeout(30l * 24 * 60 * 60 * 1000);

//         GMProvider provider = new GMProvider();
//         Security.addProvider(provider);
//         Security.addProvider(new BouncyCastleProvider());

//         SslContextFactory sslContextFactory = container.getClient().getHttpClient().getSslContextFactory();
//         sslContextFactory.setProtocol("TLS");
//         sslContextFactory.setProvider("BC");

//         Session session = container.connectToServer(MyClientEndpoint.class, new URI(
//                 "wss://localhost:10101/status/report/wss"));
//         session.setMaxIdleTimeout(30l * 24 * 60 * 60 * 1000);
//         System.out.println("connect success cost time :"
//                 + (System.currentTimeMillis() - connectStartTime));
        
//         for(int i =0; i< 20; i++) {
//             try {
//                 session.getBasicRemote().sendPing(ByteBuffer.wrap("{}".getBytes()));
//                 session.getBasicRemote().sendText("report seq: " + i + " device online: " + (i % 2 == 0));
//                 Thread.sleep(1000);
//             } catch (Exception e) {
//                 e.printStackTrace();
//                 break;
//             }
//         }

//         container.stop();
//     }

//     @ClientEndpoint
//     public static class MyClientEndpoint extends Endpoint {

//         @Override
//         public void onOpen(Session session, EndpointConfig config) {
//             System.out.println("websocket connection is opened.");
//             session.addMessageHandler(new MyClientMessageListener());
//         }

//         @Override
//         public void onClose(Session session, CloseReason closeReason) {
//             System.out.println("websocket connection is closed." + closeReason);
//         }

//         @Override
//         public void onError(Session session, Throwable thr) {
//             System.out.println("websocket connection is error." + thr);
//         }

//     }

//     public static class MyClientMessageListener implements MessageHandler.Whole<String> {

//         @Override
//         public void onMessage(String message) {
//             try {
//                 System.out.println("i receive a message=" + message);
//             } catch (Exception e) {
//             }

//         }
//     }
// }
