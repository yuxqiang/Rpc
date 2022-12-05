package yuqiang.rpc.provider.common.handler;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuqiang.rpc.potocol.RpcProtocol;
import yuqiang.rpc.potocol.enumeration.RpcType;
import yuqiang.rpc.potocol.header.RpcHeader;
import yuqiang.rpc.potocol.request.RpcRequest;
import yuqiang.rpc.potocol.response.RpcResponse;

import java.util.Map;

/**
 * RPC服务提供者的Handler处理类
 */
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private final Logger logger = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcRequestRpcProtocol) throws Exception {
        logger.info("RPC提供者收到的数据为====>>> " + JSONObject.toJSONString(rpcRequestRpcProtocol));
        logger.info("handlerMap中存放的数据如下所示：");
        for (Map.Entry<String, Object> entry : handlerMap.entrySet()) {
            logger.info(entry.getKey() + " === " + entry.getValue());
        }
        RpcHeader rpcHeader = rpcRequestRpcProtocol.getHeader();
        RpcRequest rpcRequest = rpcRequestRpcProtocol.getBody();
        //将请求头的消息类型设置为响应类型
        rpcHeader.setMsgType((byte) RpcType.RESPONSE.getType());
        //构建协议题响应数据
        RpcProtocol<RpcResponse> rpcResponseRpcProtocol = new RpcProtocol<>();
        RpcResponse response = new RpcResponse();
        response.setResult("数据交互成功");
        response.setAsync(rpcRequest.isAsync());
        response.setOneway(rpcRequest.isOneway());
        rpcResponseRpcProtocol.setBody(response);
        rpcResponseRpcProtocol.setHeader(rpcHeader);
        //直接返回数据
        channelHandlerContext.writeAndFlush(rpcResponseRpcProtocol);
    }
}
