package yuqiang.rpc.proxy.api;

import yuqiang.rpc.proxy.api.config.ProxyConfig;

public interface ProxyFactory {

    <T> T getProxy(Class<T> clazz);

    default <T> void init(ProxyConfig<T> proxyConfig){};
}
