package cn.hanglok.algoSched.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Allen
 * @version 1.0
 * @className MinioProperties
 * @description TODO
 * @date 2023/9/20
 */
@ConfigurationProperties(prefix = "minio")
@Component
@Data
public class MinioProperties {
    /**
     * 连接地址
     */
    private String endpoint;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 域名
     */
    private String nginxHost;
}
