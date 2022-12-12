package yuqiang.rpc.proxy.api.consumer;

import yuqiang.rpc.potocol.RpcProtocol;
import yuqiang.rpc.potocol.request.RpcRequest;
import yuqiang.rpc.proxy.api.future.RpcFuture;
import yuqiang.rpc.register.api.RegisterService;

public interface Consumer {

    /**
     * 消费者发送请求
     * @param protocol
     * @return
     */
    RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, RegisterService registerService) throws Exception;
}
