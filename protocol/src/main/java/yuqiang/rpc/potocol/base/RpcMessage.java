package yuqiang.rpc.potocol.base;

import java.io.Serializable;

public class RpcMessage implements Serializable {

    /**
     * 是否单向发送
     */
    private boolean oneway;

    /**
     * 是否异步调用
     */
    private boolean async;
}
