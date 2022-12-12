package yuqiang.rpc.proxy.api.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuqiang.rpc.proxy.api.BaseProxyFactory;
import yuqiang.rpc.spi.annotation.SPIClass;

import java.lang.reflect.Proxy;

@SPIClass
public class JdkProxyFactory extends BaseProxyFactory {
    private final Logger logger = LoggerFactory.getLogger(JdkProxyFactory.class);
    @Override
    public <T> T getProxy(Class<T> clazz) {
        logger.info("基于JDK动态代理...");
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},objectProxy);

    }
}
