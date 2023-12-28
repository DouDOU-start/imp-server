package cn.hanglok.algoSched;

import cn.hanglok.algoSched.service.DockerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Allen
 * @version 1.0
 * @className cn.hanglok.algoSched.DockerTest
 * @description TODO
 * @date 2023/12/21
 */
@SpringBootTest
public class DockerTest {

    @Autowired
    DockerService dockerService;

    @Test
    public void test() {
        dockerService.mergeLungSegmentation("547bc2a6-f6a1-406b-a0f7-ce754e87591a");
    }

}
