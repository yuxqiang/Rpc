package yuqiang.rpc.loadbalancer.random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import yuqiang.rpc.loadbalancer.api.ServerLoadbalancer;

import java.util.List;
import java.util.Random;

public class RandomServerLoadbalancer<T> implements ServerLoadbalancer<T> {

    private final static Logger logger = LoggerFactory.getLogger(ServerLoadbalancer.class);

    @Override
    public T select(List<T> servers, int hashCode) {
        logger.info("基于随机算法的负载均衡策略");
        if (CollectionUtils.isEmpty(servers)) {
            return null;
        }
        Random r = new Random();
        return servers.get(r.nextInt(servers.size()));
    }
}
