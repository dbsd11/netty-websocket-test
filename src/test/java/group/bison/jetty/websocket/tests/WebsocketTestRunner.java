package group.bison.jetty.websocket.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class WebsocketTestRunner {

    @Test
    public void test () throws Exception {
        for(int i =0; i< 1000; i++) {
            HttpClientTest.main(new String[0]);
        }
    }
}
