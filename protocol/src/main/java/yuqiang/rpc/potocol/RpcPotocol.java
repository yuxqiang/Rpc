package yuqiang.rpc.potocol;

import yuqiang.rpc.potocol.header.RpcHeader;

import java.io.Serializable;

public class RpcPotocol<T> implements Serializable {

    /**
     * 消息头
     */
    private RpcHeader header;

    /**
     * 消息体
     */
    private T body;

    public RpcHeader getHeader() {
        return header;
    }

    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
