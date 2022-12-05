package yuqiang.rpc.potocol.header;

import java.io.Serializable;

/**
 * 请求头
 */
public class RpcHeader implements Serializable {

    /**
     * 魔数
     */
    private short magic;

    /**
     * 消息类型
     */
    private byte msgType;

    /**
     * 状态
     */
    private byte status;

    /**
     * 消息ID
     */
    private long requestId;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 消息长度
     */
    private int msgLen;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public int getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }
}
