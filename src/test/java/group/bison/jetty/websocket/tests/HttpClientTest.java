// package group.bison.jetty.websocket.tests;

// import java.net.URL;

// import javax.net.ssl.HttpsURLConnection;
// import javax.net.ssl.SSLContext;
// import javax.net.ssl.SSLEngine;
// import javax.net.ssl.SSLSocketFactory;

// import org.apache.commons.io.IOUtils;

// public class HttpClientTest {

//     public static void main(String[] args) throws Exception {
//         long connectStartTime = System.currentTimeMillis();

//         // init SSLSocketFactory
//          provider = new GMProvider();
        
//         System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
//         SSLContext sc = SSLContext.getInstance("TLS", provider);
//         sc.init(null, null, null);
//         SSLSocketFactory ssf = sc.getSocketFactory();

//         URL serverUrl = new URL("https://127.0.0.1:443/status/report");
//         HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
//         conn.setRequestMethod("GET");
//         // set SSLSocketFactory
//         conn.setSSLSocketFactory(ssf);
//         conn.connect();
//         System.out.println("connect success cost time :"
//                 + (System.currentTimeMillis() - connectStartTime));

//         System.out.println("used cipher suite:");
//         System.out.println(conn.getCipherSuite());

//         conn.getOutputStream().write("{\"online\":false}".getBytes());
//         conn.getOutputStream().flush();

//         System.out.println(IOUtils.toString(conn.getInputStream()));
//     }

// }
