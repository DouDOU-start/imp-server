package cn.hanglok.algoSched.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Allen
 * @version 1.0
 * @className MySqlConfig
 * @description TODO
 * @date 2024/10/14
 */
@Data
@Configuration
public class MySQLConfig {
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    private String host;
    private int port;
    private String database;

    @PostConstruct
    private void init() {
        parseJdbcUrl();
    }

    // 解析 JDBC URL 的方法
    private void parseJdbcUrl() {
        String regex = "^jdbc:mysql://([\\w.]+):(\\d+)/(\\w+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            this.host = matcher.group(1);
            this.port = Integer.parseInt(matcher.group(2));
            this.database = matcher.group(3);
        } else {
            throw new IllegalArgumentException("The JDBC URL is not in the expected format.");
        }
    }
}
