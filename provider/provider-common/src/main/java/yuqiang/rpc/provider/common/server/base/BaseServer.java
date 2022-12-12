package yuqiang.rpc.provider.common.server.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import yuqiang.rpc.codec.RpcDecoder;
import yuqiang.rpc.codec.RpcEncoder;
import yuqiang.rpc.constants.RpcConstants;
import yuqiang.rpc.provider.common.handler.RpcProviderHandler;
import yuqiang.rpc.provider.common.server.api.Server;
import yuqiang.rpc.register.api.RegisterService;
import yuqiang.rpc.register.api.config.RegisterConfig;
import yuqiang.rpc.registry.zookeeper.ZookeeperRegistryService;

import java.util.HashMap;
import java.util.Map;

public class BaseServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(BaseServer.class);

    //主机域名或者IP地址
    protected String host = "127.0.0.1";
    //端口号
    protected int port = 27110;
    //存储的是实体类关系
    protected Map<String, Object> handlerMap = new HashMap<>();

    protected RegisterService registerService;

    /**
     * 反射类型
     */
    private String reflectType;

    public BaseServer(String serverAddress, String registerAddress,String registryType,String reflectType) {
        if (!StringUtils.isEmpty(serverAddress)) {
            String[] serverArray = serverAddress.split(":");
            this.host = serverArray[0];
            this.port = Integer.parseInt(serverArray[1]);
        }
        if (reflectType == null || reflectType == "") {
            reflectType= RpcConstants.PROXY_JDK;
        }
        this.reflectType = reflectType;
        this.registerService = this.getRegistryService(registerAddress, registryType);
    }

    @Override
    public void startNettyServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    //TODO 预留编解码，需要实现自定义协议
                                    .addLast(new RpcDecoder())
                                    .addLast(new RpcEncoder())
                                    .addLast(new RpcProviderHandler(reflectType,handlerMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.info("Server started on {}:{}", host, port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("RPC Server start error", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }

    private RegisterService getRegistryService(String registryAddress, String registryType) {
        //TODO 后续扩展支持SPI
        RegisterService registryService = null;
        try {
            registryService = new ZookeeperRegistryService();
            registryService.init(new RegisterConfig(registryAddress, registryType));
        }catch (Exception e){
            logger.error("RPC Server init error", e);
        }
        return registryService;
    }
}
