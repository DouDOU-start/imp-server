package cn.hanglok.algoSched.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen
 * @version 1.0
 * @className TemplateConfig
 * @description TODO
 * @date 2024/1/24
 */
@Data
@Configuration
public class TemplateConfig {
    @Value("${template.path}")
    private String path;
}
