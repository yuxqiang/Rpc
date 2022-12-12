package yuqiang.rpc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import yuqiang.rpc.common.exception.RegistryException;
import yuqiang.rpc.consumer.common.RpcConsumer;
import yuqiang.rpc.proxy.api.ProxyFactory;
import yuqiang.rpc.proxy.api.async.IAsyncObjectProxy;
import yuqiang.rpc.proxy.api.config.ProxyConfig;
import yuqiang.rpc.proxy.api.jdk.JdkProxyFactory;
import yuqiang.rpc.proxy.api.object.ObjectProxy;
import yuqiang.rpc.register.api.RegisterService;
import yuqiang.rpc.register.api.config.RegisterConfig;
import yuqiang.rpc.registry.zookeeper.ZookeeperRegistryService;
import yuqiang.rpc.spi.loader.ExtensionLoader;

public class RpcClient {
    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);

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

    private RegisterService registerService;

    private String proxy;
    public RpcClient(String registryAddress, String registryType,String proxy,String serviceVersion, String serviceGroup, long timeout, String serializationType, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.proxy=proxy;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.registerService=this.getRegistryService(registryAddress, registryType);
    }

    public <T> T create(Class<T> interfaceClass) {
        //JdkProxyFactory jdkProxyFactory = new JdkProxyFactory();

        ProxyFactory proxyFactory = ExtensionLoader.getExtension(ProxyFactory.class,proxy);
        proxyFactory.init(new ProxyConfig(interfaceClass,serviceVersion,serviceGroup,serializationType,timeout,RpcConsumer.getInstance(), async, oneway,registerService));
        return proxyFactory.getProxy(interfaceClass);
    }

    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass, serviceVersion, serviceGroup, serializationType, timeout, RpcConsumer.getInstance(), async, oneway,registerService);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }

    private RegisterService getRegistryService(String registryAddress, String registryType) {
        if (StringUtils.isEmpty(registryType)){
            throw new IllegalArgumentException("registry type is null");
        }
        //TODO 后续SPI扩展
        RegisterService registryService = new ZookeeperRegistryService();
        try {
            registryService.init(new RegisterConfig(registryAddress, registryType));
        } catch (Exception e) {
            logger.error("RpcClient init registry service throws exception:{}", e);
            throw new RegistryException(e.getMessage(), e);
        }
        return registryService;
    }
}
