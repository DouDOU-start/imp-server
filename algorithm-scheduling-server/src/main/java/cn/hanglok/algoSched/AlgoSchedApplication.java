package cn.hanglok.algoSched;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Allen
 * @version 1.0
 * @className AlgoSchedApplication
 * @description 算法调度服务启动程序
 * @date 2023/9/18
 */
@SpringBootApplication
@MapperScan("cn.hanglok.algoSched.mapper")
public class AlgoSchedApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlgoSchedApplication.class, args);
    }
}
