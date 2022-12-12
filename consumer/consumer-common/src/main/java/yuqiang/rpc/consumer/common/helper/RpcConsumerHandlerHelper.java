package yuqiang.rpc.consumer.common.helper;

import yuqiang.rpc.consumer.common.handler.RpcConsumerHandler;
import yuqiang.rpc.potocol.meta.ServiceMeta;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcConsumerHandlerHelper {
    private static Map<String, RpcConsumerHandler> rpcConsumerHandlerMap;

    static {
        rpcConsumerHandlerMap = new ConcurrentHashMap<>();
    }

    public static String getKey(ServiceMeta key) {
        return key.getServiceAddr().concat("_").concat(String.valueOf(key.getServicePort()));
    }

    public static void put(ServiceMeta key, RpcConsumerHandler value) {
        rpcConsumerHandlerMap.put(getKey(key), value);
    }

    public static RpcConsumerHandler get(ServiceMeta key) {
        return rpcConsumerHandlerMap.get(getKey(key));
    }

    public static void closeRpcConsumerHandler() {
        Collection<RpcConsumerHandler> rpcConsumerHandler = rpcConsumerHandlerMap.values();
        if (rpcConsumerHandler!=null){
            rpcConsumerHandler.forEach(rpcConsumerHandler1 -> {
                rpcConsumerHandler1.close();
            });
        }
        rpcConsumerHandler.clear();
    }
}
