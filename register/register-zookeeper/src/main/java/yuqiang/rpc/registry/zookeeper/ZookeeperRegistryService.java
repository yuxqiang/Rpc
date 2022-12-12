package yuqiang.rpc.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import yuqiang.rpc.common.helper.RpcServiceHelper;
import yuqiang.rpc.loadbalancer.api.ServerLoadbalancer;
import yuqiang.rpc.loadbalancer.random.RandomServerLoadbalancer;
import yuqiang.rpc.potocol.meta.ServiceMeta;
import yuqiang.rpc.register.api.RegisterService;
import yuqiang.rpc.register.api.config.RegisterConfig;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ZookeeperRegistryService implements RegisterService {

    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 3;
    public static final String ZK_BASE_PATH = "/binghe_rpc";

    private ServiceDiscovery<ServiceMeta> serviceDiscovery;

    private ServerLoadbalancer<ServiceInstance<ServiceMeta>> serverLoadbalancer;

    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion(), serviceMeta.getServiceGroup()))
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceInstance);

    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(serviceMeta.getServiceName())
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokeHashCode) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        return this.selectOneServiceInstance((List<ServiceInstance<ServiceMeta>>) serviceInstances, invokeHashCode).getPayload();
    }


    private ServiceInstance<ServiceMeta> selectOneServiceInstance(List<ServiceInstance<ServiceMeta>> serviceInstances, int invokeHashCode) {
        return serverLoadbalancer.select(serviceInstances, invokeHashCode);
    }

    @Override
    public void destory() throws Exception {
        serviceDiscovery.close();
    }

    @Override
    public void init(RegisterConfig registryConfig) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryConfig.getRegistryAddr(), new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
        client.start();
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
        this.serviceDiscovery.start();
        this.serverLoadbalancer = new RandomServerLoadbalancer<ServiceInstance<ServiceMeta>>();
//        //增强型负载均衡策略
//        if (registryConfig.getRegistryLoadBalanceType().toLowerCase().contains(RpcConstants.SERVICE_ENHANCED_LOAD_BALANCER_PREFIX)){
//            this.serviceEnhancedLoadBalancer = ExtensionLoader.getExtension(ServiceLoadBalancer.class, registryConfig.getRegistryLoadBalanceType());
//        }else{
//            this.serviceLoadBalancer = ExtensionLoader.getExtension(ServiceLoadBalancer.class, registryConfig.getRegistryLoadBalanceType());
//        }
    }
}
