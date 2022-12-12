package yuqiang.rpc.codec;


import yuqiang.rpc.serialization.api.Serialization;
import yuqiang.rpc.serialization.jdk.JdkSerialization;
import yuqiang.rpc.spi.loader.ExtensionLoader;

public interface RpcCodec {
    default Serialization getSerialization(String serializationType){
        //return new JdkSerialization();
        return ExtensionLoader.getExtension(Serialization.class, serializationType);
    }
}
