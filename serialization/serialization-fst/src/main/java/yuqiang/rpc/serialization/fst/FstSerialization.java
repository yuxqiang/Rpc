package yuqiang.rpc.serialization.fst;

import org.nustaq.serialization.FSTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuqiang.rpc.common.exception.SerializerException;
import yuqiang.rpc.serialization.api.Serialization;
import yuqiang.rpc.spi.annotation.SPIClass;

@SPIClass
public class FstSerialization implements Serialization {
    private final Logger logger = LoggerFactory.getLogger(FstSerialization.class);

    @Override
    public <T> byte[] serialize(T obj) {
        logger.info("execute fst serialize...");
        if (obj == null){
            throw new SerializerException("serialize object is null");
        }
        FSTConfiguration conf = FSTConfiguration.getDefaultConfiguration();
        return conf.asByteArray(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        logger.info("execute fst deserialize...");
        if (data == null){
            throw new SerializerException("deserialize data is null");
        }
        FSTConfiguration conf = FSTConfiguration.getDefaultConfiguration();
        return (T) conf.asObject(data);
    }
}
