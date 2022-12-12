package yuqiang.rpc.loadbalancer.api;

import java.util.List;

public interface ServerLoadbalancer<T> {

    /**
     * 负载均衡的方式选择一个节点
     * @param servers
     * @param hashCode
     * @return
     */
    T select(List<T> servers,int hashCode);
}
