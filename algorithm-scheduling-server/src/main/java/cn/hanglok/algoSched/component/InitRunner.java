package cn.hanglok.algoSched.component;

import cn.hanglok.algoSched.socket.SchedulingSocket;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Allen
 * @version 1.0
 * @className InitRunner
 * @description TODO
 * @date 2023/9/18
 */
@Component
public class InitRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws IOException {
        SchedulingSocket.start();
    }
}
