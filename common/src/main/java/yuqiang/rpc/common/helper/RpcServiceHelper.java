package yuqiang.rpc.common.helper;

public class RpcServiceHelper {

    /**
     *
     * @param serviceName
     * @param serviceVersion
     * @param group
     * @return serviceName#serviceVersion#group
     */
    public static String buildServiceKey(String serviceName, String serviceVersion, String group) {
        return String.join("#", serviceName, serviceVersion, group);
    }
}
