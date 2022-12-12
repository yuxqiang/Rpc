package yuqiang.rpc.spi.factory;

import yuqiang.rpc.spi.annotation.SPI;
import yuqiang.rpc.spi.annotation.SPIClass;
import yuqiang.rpc.spi.loader.ExtensionLoader;

import java.util.Optional;

@SPIClass
public class SpiExtenSionFactory implements ExtensionFactory {
    @Override
    public <T> T getExtension(String key, Class<T> clazz) {
        return Optional.ofNullable(clazz).
                filter(Class::isInterface).
                filter(cla->cla.isAnnotationPresent(SPI.class)).
                map(ExtensionLoader::getExtensionLoader).
                map(ExtensionLoader::getDefaultSpiClassInstance).orElse( null);
    }
}
