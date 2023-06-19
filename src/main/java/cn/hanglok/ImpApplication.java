package cn.hanglok;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Allen
 */
@EnableAspectJAutoProxy
@SpringBootApplication
@MapperScan("cn.hanglok.mapper")
public class ImpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImpApplication.class, args);
    }

}