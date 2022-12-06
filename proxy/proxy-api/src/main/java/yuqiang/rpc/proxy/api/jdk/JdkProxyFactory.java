package yuqiang.rpc.proxy.api.jdk;

import yuqiang.rpc.proxy.api.consumer.Consumer;
import yuqiang.rpc.proxy.api.object.ObjectProxy;

import java.lang.reflect.Proxy;

public class JdkProxyFactory {
    private String serviceVersion;
    private String serviceGroup;
    private long timeout = 15000;
    private Consumer consumer;
    private String serializationType;
    /**
     * 是否异步调用
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    public JdkProxyFactory(String serviceVersion, String serviceGroup, long timeout, Consumer consumer, String serializationType, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},new ObjectProxy<>(clazz,serviceVersion,serviceGroup,serializationType,timeout,consumer,async,oneway));

    }
}
