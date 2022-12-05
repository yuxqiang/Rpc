package yuqiang.rpc.potocol.base;

import java.io.Serializable;

public class RpcMessage implements Serializable {

    /**
     * 是否单向发送
     */
    private boolean oneway;

    public boolean isOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    /**
     * 是否异步调用
     */
    private boolean async;
}
