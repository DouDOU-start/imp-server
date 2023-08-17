package cn.hanglok.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Allen
 * @version 1.0
 * @className MySqlConfig
 * @description TODO
 * @date 2023/6/7 14:18
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {
    public static String url;
    public static String username;
    public static String password;
    public static String scriptLocation;

    public void setUrl(String url) {
        DataSourceConfig.url = url;
    }

    public void setUsername(String username) {
        DataSourceConfig.username = username;
    }

    public void setPassword(String password) {
        DataSourceConfig.password = password;
    }

    public void setScriptLocation(String scriptLocation) {
        DataSourceConfig.scriptLocation = scriptLocation;
    }
}
