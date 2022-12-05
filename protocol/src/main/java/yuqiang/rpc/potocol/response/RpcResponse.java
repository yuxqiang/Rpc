package yuqiang.rpc.potocol.response;

import yuqiang.rpc.potocol.base.RpcMessage;

/**
 * rpc响应类
 */
public class RpcResponse extends RpcMessage {

    private String error;

    private Object result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
