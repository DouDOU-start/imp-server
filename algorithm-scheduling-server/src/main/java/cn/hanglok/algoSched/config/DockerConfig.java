package cn.hanglok.algoSched.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen
 * @version 1.0
 * @className DockerConfig
 * @description 远程 Docker配置
 * @date 2024/1/2
 */
@Data
@Configuration
public class DockerConfig {
    @Value("${docker.host}")
    private String host;
}
