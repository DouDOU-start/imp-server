package cn.hanglok.pacs.util;

import cn.hanglok.pacs.config.DataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Allen
 * @version 1.0
 * @className DataBaseUtils
 * @description TODO
 * @date 2023/8/17
 */
public class DataBaseUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseUtils.class);

    public static void executeInitScript() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DataSourceConfig.scriptLocation));
             Connection connection = DriverManager.getConnection(
                     DataSourceConfig.url,
                     DataSourceConfig.username,
                     DataSourceConfig.password
             );
             Statement statement = connection.createStatement())
        {

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.startsWith("--")) {
                    sb.append(line);
                }
            }

            String[] sqlQueries = sb.toString().split(";");

            for (String sqlQuery : sqlQueries) {
                statement.executeUpdate(sqlQuery);
            }

            logger.info("数据库初始化成功");

        } catch (IOException | SQLException e) {
            logger.error("数据库初始化失败：" +e);
        }
    }
}
