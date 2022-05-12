package cn.alectang.im.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author alectang
 * @since 2022-04-27
 */
@TableName("t_client_info")
public class ClientInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 连接状态
     */
    private Integer connected;

    /**
     * 用户session
     */
    private String uuid;

    /**
     * 最后连接时间
     */
    private LocalDateTime lastConnected;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    public Integer getConnected() {
        return connected;
    }

    public void setConnected(Integer connected) {
        this.connected = connected;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public LocalDateTime getLastConnected() {
        return lastConnected;
    }

    public void setLastConnected(LocalDateTime lastConnected) {
        this.lastConnected = lastConnected;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
            "uid=" + uid +
            ", connected=" + connected +
            ", uuid=" + uuid +
            ", lastConnected=" + lastConnected +
        "}";
    }
}
