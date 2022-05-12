package cn.alectang.im.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SocketIOConfig {

    @Value("${socket.port}")
    private Integer port;

    @Value("${socket.workCount}")
    private Integer workCount;

    @Value("${socket.allowCustomRequests}")
    private Boolean allowCustomRequests;

    @Value("${socket.upgradeTimeout}")
    private Integer upgradeTimeout;

    @Value("${socket.pingTimeout}")
    private Integer pingTimeout;

    @Value("${socket.pingInterval}")
    private Integer pingInterval;

    @Value("${socket.maxFramePayloadLength}")
    private Integer maxFramePayloadLength;

    @Value("${socket.maxHttpContentLength}")
    private Integer maxHttpContentLength;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(port);

        com.corundumstudio.socketio.SocketConfig socketConfig = new com.corundumstudio.socketio.SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        config.setMaxHttpContentLength(maxHttpContentLength);
        config.setMaxFramePayloadLength(maxFramePayloadLength);

        return new SocketIOServer(config);
    }

    /**
     * 开启SocketIOServer注解支持
     *
     * @param socketServer
     * @return
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}