package group.bison.jetty.websocket.tests;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.*;

import io.netty.handler.ssl.JdkSslContext;
import io.netty.handler.ssl.SslHandler;
import org.apache.commons.io.IOUtils;
import net.tongsuo.TongsuoProvider;
import org.openjsse.legacy8ujsse.sun.security.ssl.SSLEngineImpl;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClientTest {

    public static void main(String[] args) throws Exception {
        long connectStartTime = System.currentTimeMillis();

         //加密套件，多个以:分隔
        String ciperSuites = "TLS_SM4_GCM_SM3:TLS_SM4_CCM_SM3";
        TongsuoProvider
        TrustManager[] tms = new TrustManager[] { new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType)throws CertificateException{
            }

        } };

        // init SSLSocketFactory        
        SSLContext sc = SSLContext.getInstance("TLSv1.3", new TongsuoProvider());
        sc.init(null, tms, new SecureRandom());
        SSLSocketFactory ssf = sc.getSocketFactory();

        URL serverUrl = new URL("https://localhost/status/report");
        HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
        conn.setRequestMethod("POST");
        // set SSLSocketFactory
        conn.setSSLSocketFactory(ssf);
        conn.connect();
        System.out.println("connect success cost time :"
                + (System.currentTimeMillis() - connectStartTime));

        System.out.println("used cipher suite:");
        System.out.println(conn.getCipherSuite());

        conn.getOutputStream().write("{\"online\":false}".getBytes());
        conn.getOutputStream().flush();

        System.out.println(IOUtils.toString(conn.getInputStream()));
    }

}