package yuqiang.rpc.proxy.api.async;

import yuqiang.rpc.proxy.api.future.RpcFuture;

public interface IAsyncObjectProxy {
    /**
     * 异步调用代理对象方法
     * @param functionName
     * @param args
     * @return
     */
    RpcFuture call(String functionName, Object... args);
}
