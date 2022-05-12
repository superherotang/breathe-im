package cn.alectang.im.handler;


import cn.alectang.im.entity.ClientInfo;
import cn.alectang.im.entity.MessageInfo;
import cn.alectang.im.mapper.ClientInfoMapper;
import cn.alectang.im.mapper.MessageInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Log4j2
public class MessageEventHandler {

    @Resource
    SocketIOServer server;

    @Resource
    ClientInfoMapper clientInfoMapper;


    @Resource
    MessageInfoMapper messageInfoMapper;

    public static ConcurrentMap<String, SocketIOClient> socketIOClientMap =
            new ConcurrentHashMap<>();

    //roomMsg事件消息接收入口
    @OnEvent(value = "roomMsg") //value值与前端自行商定
    public void onRoomEvent(SocketIOClient client, AckRequest ackRequest, MessageInfo msg) throws Exception {

        log.info(msg);

        server.getBroadcastOperations().sendEvent("roomMsg", msg);
    }

    //singleMsg事件消息接收入口
    @OnEvent(value = "singleMsg")
    public void onSingleEvent(SocketIOClient client, AckRequest ackRequest, MessageInfo msg) throws Exception {
        log.info(client.getSessionId());
        log.info(msg);
        //查询是否有用户
        QueryWrapper<ClientInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("uid",msg.getTuid());
        ClientInfo clientInfo=clientInfoMapper.selectOne(queryWrapper);
        //如果用户在线发送消息
        if (clientInfo!=null||clientInfo.getConnected()==1){
            server.getClient(UUID.fromString(clientInfo.getUuid())).sendEvent("singleMsg",msg);
            //持久化存入数据库
            msg.setStates(0);
            messageInfoMapper.insert(msg);
        }else {
            //如果用户离线存入数据库
            messageInfoMapper.insert(msg);
        }
    }


    //socket添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {

        log.info("--------------------客户端" + client.getSessionId() + "已断开连接--------------------");
        String uid = client.getHandshakeData().getSingleUrlParam("uid");
        QueryWrapper<ClientInfo> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        ClientInfo clientInfo =clientInfoMapper.selectOne(queryWrapper);

        clientInfo.setConnected(0);
        clientInfoMapper.update(clientInfo,queryWrapper);

        client.disconnect();
    }

    //socket添加connect事件，当客户端发起连接时调用
    @OnConnect
    public void onConnect(SocketIOClient client) {
        if (client != null) {
            String uid = client.getHandshakeData().getSingleUrlParam("uid");

            //查询用户
            QueryWrapper<ClientInfo> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("uid",uid);
            ClientInfo clientInfo =clientInfoMapper.selectOne(queryWrapper);



            if (clientInfo != null)
            {

                //更新
                clientInfo.setConnected(1);
                clientInfo.setUuid(client.getSessionId().toString());
                clientInfoMapper.update(clientInfo,queryWrapper);
            }else {
                ClientInfo newClientInfo=new ClientInfo();
                newClientInfo.setUid(Long.valueOf(uid));
                newClientInfo.setConnected(1);
                newClientInfo.setUuid(client.getSessionId().toString());

                //新增
                clientInfoMapper.insert(newClientInfo);
            }

            HandshakeData client_mac = client.getHandshakeData();
            String mac = client_mac.getUrl();
            //存储SocketIOClient，用于向不同客户端发送消息
            socketIOClientMap.put(mac, client);

            log.info("--------------------客户端" + client.getSessionId() + "连接成功---------------------");
        } else {
            log.error("客户端为空");
        }
    }

    /**
     * 广播消息 函数可在其他类中调用
     */
    public static void sendBroadcast(byte[] data) {
        for (SocketIOClient client : socketIOClientMap.values()) { //向已连接的所有客户端发送数据,map实现客户端的存储
            if (client.isChannelOpen()) {
                client.sendEvent("msg", data);
            }
        }
    }
}