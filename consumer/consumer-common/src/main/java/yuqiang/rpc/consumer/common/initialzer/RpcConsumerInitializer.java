package yuqiang.rpc.consumer.common.initialzer;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import yuqiang.rpc.codec.RpcDecoder;
import yuqiang.rpc.codec.RpcEncoder;
import yuqiang.rpc.consumer.common.handler.RpcConsumerHandler;

public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new RpcEncoder());
        channelPipeline.addLast(new RpcDecoder());
        channelPipeline.addLast(new RpcConsumerHandler());
    }
}
