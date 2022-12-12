package yuqiang.rpc.register.api;

import yuqiang.rpc.potocol.meta.ServiceMeta;
import yuqiang.rpc.register.api.config.RegisterConfig;

public interface RegisterService {
    /**
     * 服务注册
     * @param serviceMeta
     * @throws Exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 取消注册
     * @param serviceMeta
     * @throws Exception
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;


    /**
     * 服务发现
     * @param serviceName
     * @param invokeHashCode
     * @return
     * @throws Exception
     */
    ServiceMeta discovery(String  serviceName,int invokeHashCode) throws Exception;


    /**
     * 服务销毁
     * @param serviceName
     * @param invokeHashCode
     * @return
     * @throws Exception
     */
    void destory() throws Exception;

    default void init(RegisterConfig registerConfig) throws Exception{

    }

}
