package yuqiang.rpc.consumer.common.callback;

public interface AsyncRpcCallback {

    void onSuccess(Object result);

    void onException(Exception e);
}
