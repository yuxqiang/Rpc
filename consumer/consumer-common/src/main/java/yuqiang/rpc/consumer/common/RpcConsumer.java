package yuqiang.rpc.consumer.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuqiang.rpc.common.helper.RpcServiceHelper;
import yuqiang.rpc.common.threadpool.ClientThreadPool;
import yuqiang.rpc.consumer.common.helper.RpcConsumerHandlerHelper;
import yuqiang.rpc.potocol.meta.ServiceMeta;
import yuqiang.rpc.proxy.api.consumer.Consumer;
import yuqiang.rpc.proxy.api.future.RpcFuture;
import yuqiang.rpc.consumer.common.handler.RpcConsumerHandler;
import yuqiang.rpc.consumer.common.initialzer.RpcConsumerInitializer;
import yuqiang.rpc.potocol.RpcProtocol;
import yuqiang.rpc.potocol.request.RpcRequest;
import yuqiang.rpc.register.api.RegisterService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcConsumer implements Consumer {

    private final static Logger logger = LoggerFactory.getLogger(RpcConsumer.class);

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventLoopGroup;

    private static volatile RpcConsumer instance;

    private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();

    private RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new RpcConsumerInitializer());
    }

    public static RpcConsumer getInstance() {
        if (instance == null) {
            synchronized (RpcConsumer.class) {
                if (instance == null) {
                    instance = new RpcConsumer();
                }
            }
        }
        return instance;
    }

    public void close() {
        RpcConsumerHandlerHelper.closeRpcConsumerHandler();
        eventLoopGroup.shutdownGracefully();
        ClientThreadPool.shutdown();

    }

    @Override
    public RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, RegisterService registerService) throws Exception {
        String serviceKey = RpcServiceHelper.buildServiceKey(protocol.getBody().getClassName(), protocol.getBody().getVersion(), protocol.getBody().getGroup());
        RpcRequest request = protocol.getBody();
        Object[] params = request.getParameters();
        int invokerHashCode = (params == null || params.length <= 0) ? serviceKey.hashCode() : params[0].hashCode();
        ServiceMeta serviceMeta = registerService.discovery(serviceKey, invokerHashCode);
        if (serviceMeta != null) {
            RpcConsumerHandler handler = RpcConsumerHandlerHelper.get(serviceMeta);
            //缓存中无RpcClientHandler
            if (handler == null) {
                handler = getRpcConsumerHandler(serviceMeta.getServiceAddr(), serviceMeta.getServicePort());
                RpcConsumerHandlerHelper.put(serviceMeta, handler);
            } else if (!handler.getChannel().isActive()) {  //缓存中存在RpcClientHandler，但不活跃
                handler.close();
                handler = getRpcConsumerHandler(serviceMeta.getServiceAddr(), serviceMeta.getServicePort());
                RpcConsumerHandlerHelper.put(serviceMeta, handler);
            }
            return handler.sendRequest(protocol, request.isAsync(), request.isOneway());
        }
        return null;
    }

    private RpcConsumerHandler getRpcConsumerHandler(String serviceAddress, int port) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();
        channelFuture.addListener((ChannelFutureListener) listener -> {
            if (channelFuture.isSuccess()) {
                logger.info("connect rpc server {} on port {} success.", serviceAddress, port);
            } else {
                logger.error("connect rpc server {} on port {} failed.", serviceAddress, port);
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
    }
}
