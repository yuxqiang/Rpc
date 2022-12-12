package yuqiang.rpc.consumer;

import yuqiang.rpc.consumer.common.RpcConsumer;
import yuqiang.rpc.proxy.api.async.IAsyncObjectProxy;
import yuqiang.rpc.proxy.api.config.ProxyConfig;
import yuqiang.rpc.proxy.api.jdk.JdkProxyFactory;
import yuqiang.rpc.proxy.api.object.ObjectProxy;

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
        JdkProxyFactory jdkProxyFactory = new JdkProxyFactory();
        jdkProxyFactory.init(new ProxyConfig(interfaceClass,serviceVersion,serviceGroup,serializationType,timeout,RpcConsumer.getInstance(), async, oneway));
        return jdkProxyFactory.getProxy(interfaceClass);
    }

    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass, serviceVersion, serviceGroup, serializationType, timeout, RpcConsumer.getInstance(), async, oneway);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }
}
