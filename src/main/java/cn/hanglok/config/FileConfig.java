package cn.hanglok.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Allen
 * @version 1.0
 * @className FileConfig
 * @description TODO
 * @date 2023/6/6 14:18
 */
@Component
@ConfigurationProperties(prefix = "immp.file")
public class FileConfig {
    public static String location;

    public void setLocation(String location) {
        FileConfig.location = location;
    }

}
