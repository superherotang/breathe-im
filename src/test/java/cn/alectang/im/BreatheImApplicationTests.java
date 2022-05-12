package cn.alectang.im;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;

@SpringBootTest
class BreatheImApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9090);
        config.setMaxFramePayloadLength(1024 * 1024);
        config.setMaxHttpContentLength(1024 * 1024);

        final SocketIOServer server = new SocketIOServer(config);

        server.addEventListener("message_event", byte[].class, new DataListener<byte[]>() {
            @Override
            public void onData(SocketIOClient client, byte[] data, AckRequest ackRequest) {
                client.sendEvent("message_event", data);
            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }


}
