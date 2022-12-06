package yuqiang.rpc.proxy.api.callback;

public interface AsyncRpcCallback {

    void onSuccess(Object result);

    void onException(Exception e);
}
