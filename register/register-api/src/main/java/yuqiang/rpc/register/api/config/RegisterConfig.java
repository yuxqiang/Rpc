package yuqiang.rpc.register.api.config;

import java.io.Serializable;

public class RegisterConfig implements Serializable {
    /**
     * 注册地址
     */
    private String registryAddr;

    /**
     * 注册类型
     */
    private String registryType;

    public RegisterConfig(String registryAddr, String registryType) {
        this.registryAddr = registryAddr;
        this.registryType = registryType;
    }

    public String getRegistryAddr() {
        return registryAddr;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }
}
