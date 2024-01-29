package cn.hanglok.algoSched.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen
 * @version 1.0
 * @className TokenConfig
 * @description TODO
 * @date 2024/1/29
 */
@Data
@Configuration
public class TokenConfig {
    @Value("${token}")
    private String value;
}
