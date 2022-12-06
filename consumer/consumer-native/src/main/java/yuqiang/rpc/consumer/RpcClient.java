package yuqiang.rpc.consumer;

import yuqiang.rpc.consumer.common.RpcConsumer;
import yuqiang.rpc.proxy.api.jdk.JdkProxyFactory;

public class RpcClient {
    private String serviceVersion;
    private String serviceGroup;
    private long timeout;
    private String serializationType;
    /**
     * 是否异步调用
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    public RpcClient(String serviceVersion, String serviceGroup, long timeout, String serializationType, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    public <T> T create(Class<T> interfaceClass) {
        JdkProxyFactory jdkProxyFactory = new JdkProxyFactory(serviceVersion, serviceGroup, timeout, RpcConsumer.getInstance(), serializationType, async, oneway);
        return jdkProxyFactory.getProxy(interfaceClass);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();

    }
}
