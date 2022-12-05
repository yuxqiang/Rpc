package yuqiang.rpc.potocol.header;

import yuqiang.rpc.common.id.IdFactory;
import yuqiang.rpc.constants.RpcConstants;
import yuqiang.rpc.potocol.enumeration.RpcType;

public class RpcHeaderFactory {
    public static RpcHeader getRequestHeader(String serializationType){
        RpcHeader header = new RpcHeader();
        long requestId = IdFactory.getId();
        header.setMagic(RpcConstants.MAGIC);
        header.setRequestId(requestId);
        header.setMsgType((byte) RpcType.REQUEST.getType());
        header.setStatus((byte) 0x1);
        header.setSerializationType(serializationType);
        return header;

    }
}
