package yuqiang.rpc.provider.common.handler;

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuqiang.rpc.common.helper.RpcServiceHelper;
import yuqiang.rpc.common.threadpool.ServerThreadPool;
import yuqiang.rpc.potocol.RpcProtocol;
import yuqiang.rpc.potocol.enumeration.RpcStatus;
import yuqiang.rpc.potocol.enumeration.RpcType;
import yuqiang.rpc.potocol.header.RpcHeader;
import yuqiang.rpc.potocol.request.RpcRequest;
import yuqiang.rpc.potocol.response.RpcResponse;

import java.lang.reflect.Method;
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

        ServerThreadPool.submit(() -> {
            RpcHeader rpcHeader = rpcRequestRpcProtocol.getHeader();
            RpcRequest rpcRequest = rpcRequestRpcProtocol.getBody();
            //将请求头的消息类型设置为响应类型
            rpcHeader.setMsgType((byte) RpcType.RESPONSE.getType());
            //构建协议题响应数据
            RpcProtocol<RpcResponse> rpcResponseRpcProtocol = new RpcProtocol<>();
            RpcResponse response = new RpcResponse();
            try {
                Object result = handle(rpcRequest);
                rpcHeader.setStatus((byte) RpcStatus.SUCCESS.getCode());
                response.setResult("数据交互成功");
                response.setAsync(rpcRequest.isAsync());
                response.setOneway(rpcRequest.isOneway());
            } catch (Throwable t) {
                response.setError(t.toString());
                rpcHeader.setStatus((byte) RpcStatus.FAIL.getCode());
                logger.error("RPC Server handle request error", t);
            }
            rpcResponseRpcProtocol.setBody(response);
            rpcResponseRpcProtocol.setHeader(rpcHeader);
            //直接返回数据
            channelHandlerContext.writeAndFlush(rpcResponseRpcProtocol).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.debug("Send response for request " + rpcHeader.getRequestId());
                }
            });

        });
    }


    private Object handle(RpcRequest request) throws Throwable {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getVersion(), request.getGroup());
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        logger.debug(serviceClass.getName());
        logger.debug(methodName);
        if (parameterTypes != null && parameterTypes.length > 0){
            for (int i = 0; i < parameterTypes.length; ++i) {
                logger.debug(parameterTypes[i].getName());
            }
        }

        if (parameters != null && parameters.length > 0){
            for (int i = 0; i < parameters.length; ++i) {
                logger.debug(parameters[i].toString());
            }
        }
        return invokeMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
    }

    public Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        logger.info("use jdk reflect type invoke method...");
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }
}
