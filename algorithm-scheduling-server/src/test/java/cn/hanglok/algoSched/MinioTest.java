package cn.hanglok.algoSched;

import cn.hanglok.algoSched.service.MinioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Allen
 * @version 1.0
 * @className MinioTest
 * @description TODO
 * @date 2023/12/28
 */
@SpringBootTest
public class MinioTest {

    @Autowired
    MinioService minioService;

    @Test
    public void test() {
        String[] objects = {
                "output/547bc2a6-f6a1-406b-a0f7-ce754e87591a/Fusion-0.0.1/segmentation.mha",
                "output/547bc2a6-f6a1-406b-a0f7-ce754e87591a/centerline_datastructure-1023/centerline.txt",
                "output/547bc2a6-f6a1-406b-a0f7-ce754e87591a/nodule_detection-2023_12_6/target.json"
        };
        minioService.zipObject(objects, "output/547bc2a6-f6a1-406b-a0f7-ce754e87591a/result.zip");
    }

}
