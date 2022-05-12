package cn.alectang.im.runner;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value=1)
@Slf4j
public class SocketRunner implements CommandLineRunner {
    private final SocketIOServer server;

    @Autowired
    public SocketRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("--------------------前端socket.io通信启动成功！---------------------");
        server.start();
    }
}
