package group.bison.netty.websocket.tests;

import net.tongsuo.TongsuoProvider;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@RunWith(JUnit4.class)
public class HttpClientTest {

    @Test
    public void test() throws Exception {
        long connectStartTime = System.currentTimeMillis();

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
        SSLContext sc = SSLContext.getInstance("TLSv1.3", new TongsuoProvider());
        sc.init(null, tms, new SecureRandom());
        SSLSocketFactory ssf = sc.getSocketFactory();

        URL serverUrl = new URL("https://localhost/status/report");
        HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");
        // set SSLSocketFactory
        conn.setSSLSocketFactory(ssf);
        conn.setDoOutput(true);
        conn.connect();
        System.out.println("connect success cost time :"
                + (System.currentTimeMillis() - connectStartTime));

        System.out.println("used cipher suite:");
        System.out.println(conn.getCipherSuite());

        conn.getOutputStream().write("{\"online\":false}".getBytes());
        conn.getOutputStream().flush();

        System.out.println(IOUtils.toString(conn.getInputStream()));

        conn.disconnect();
    }
}
