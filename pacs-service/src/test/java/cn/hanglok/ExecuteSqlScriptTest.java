package cn.hanglok;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author Allen
 * @version 1.0
 * @className ExecuteSqlScriptTest
 * @description TODO
 * @date 2023/8/16
 */
public class ExecuteSqlScriptTest {
    public static void main(String[] args) {

        String jdbcUrl = "jdbc:mysql://10.8.6.15:3366/IMP";
        String username = "root";
        String password = "hanglok8888";

        String sqlFilePath = "/Users/allen/code/imp-service/src/main/resources/sql/init.sql";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            executeSqlScript(connection, sqlFilePath);
            System.out.println("SQL script executed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeSqlScript(Connection connection, String sqlFilePath) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath));
             Statement statement = connection.createStatement()) {

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
        }
    }
}
