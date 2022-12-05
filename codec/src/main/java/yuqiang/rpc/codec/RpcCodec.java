package yuqiang.rpc.codec;


import yuqiang.rpc.serialization.api.Serialization;
import yuqiang.rpc.serialization.jdk.JdkSerialization;

public interface RpcCodec {
    default Serialization getJdkSerialization(){
        return new JdkSerialization();
    }
}
