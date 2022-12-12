package yuqiang.rpc.spi.factory;

import yuqiang.rpc.spi.annotation.SPI;

@SPI("spi")
public interface ExtensionFactory {
    /**
     * 扩展类对象
     * @param ket
     * @param clazz
     * @return
     * @param <T>
     */
    <T> T getExtension(String ket, Class<T> clazz);
}
