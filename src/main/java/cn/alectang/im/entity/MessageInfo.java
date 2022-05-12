package cn.alectang.im.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author alectang
 * @since 2022-04-27
 */
@TableName("t_message_info")
public class MessageInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long suid;

    private Long tuid;

    private String content;

    private Integer states;

    public Long getSuid() {
        return suid;
    }

    public void setSuid(Long suid) {
        this.suid = suid;
    }
    public Long getTuid() {
        return tuid;
    }

    public void setTuid(Long tuid) {
        this.tuid = tuid;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Integer getStates() {
        return states;
    }

    public void setStates(Integer states) {
        this.states = states;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
            "suid=" + suid +
            ", tuid=" + tuid +
            ", content=" + content +
            ", states=" + states +
        "}";
    }
}
