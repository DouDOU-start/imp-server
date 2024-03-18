package cn.hanglok.algoSched.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen
 * @version 1.0
 * @className CallbackConfig
 * @description TODO
 * @date 2024/3/18
 */
@Data
@Configuration
public class CallbackConfig {
    @Value("${callback.url}")
    private String url;
}
