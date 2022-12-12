package yuqiang.rpc.serialization.api;

import yuqiang.rpc.constants.RpcConstants;
import yuqiang.rpc.spi.annotation.SPI;

/**
 * 序列化接口
 */
@SPI(RpcConstants.SERIALIZATION_JDK)
public interface Serialization {
    /**
     * 序列化
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> cls);
}
