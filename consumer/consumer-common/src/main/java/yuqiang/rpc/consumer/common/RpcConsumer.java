package yuqiang.rpc.consumer.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yuqiang.rpc.consumer.common.handler.RpcConsumerHandler;
import yuqiang.rpc.consumer.common.initialzer.RpcConsumerInitializer;
import yuqiang.rpc.potocol.RpcProtocol;
import yuqiang.rpc.potocol.request.RpcRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RpcConsumer {

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
        eventLoopGroup.shutdownGracefully();
    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception {
        String serviceAddress = "127.0.0.1";
        int port = 21880;
        String key = serviceAddress.concat("_").concat(String.valueOf(port));
        RpcConsumerHandler rpcConsumerHandler = handlerMap.get(key);
        if(rpcConsumerHandler==null){
            rpcConsumerHandler=getRpcConsumerHandler(serviceAddress,port);
            handlerMap.put(key,rpcConsumerHandler);
        }else if(!rpcConsumerHandler.getChannel().isActive()){
            rpcConsumerHandler.close();
            rpcConsumerHandler=getRpcConsumerHandler(serviceAddress,port);
            handlerMap.put(key,rpcConsumerHandler);
        }
        rpcConsumerHandler.sendRequest(protocol);
    }

    private RpcConsumerHandler getRpcConsumerHandler(String serviceAddress, int port) throws InterruptedException {
        ChannelFuture channelFuture= bootstrap.connect(serviceAddress,port).sync();
        channelFuture.addListener((ChannelFutureListener) listener->{
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
