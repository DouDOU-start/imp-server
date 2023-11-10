package cn.hanglok.algoSched.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Allen
 * @version 1.0
 * @className SocketConfig
 * @description TODO
 * @date 2023/9/18
 */
@Component
@ConfigurationProperties(prefix = "algorithm.socket")
public class SocketConfig {
    public static int port;

    public void setPort(int port) {
        SocketConfig.port = port;
    }
}
