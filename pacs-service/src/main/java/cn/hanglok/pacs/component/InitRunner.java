package cn.hanglok.pacs.component;

import cn.hanglok.pacs.config.FileConfig;
import cn.hanglok.pacs.util.DataBaseUtils;
import cn.hanglok.pacs.util.FileUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Allen
 * @version 1.0
 * @className InitRunner
 * @description 程序启动执行初始化操作
 * @date 2023/6/29 10:50
 */
@Component
public class InitRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        createUploadFileDir();
        DataBaseUtils.executeInitScript();
    }

    public void createUploadFileDir() {
        FileUtils.mkdir(FileConfig.dicomLocation);
        FileUtils.mkdir(FileConfig.labelLocation);
        FileUtils.mkdir(FileConfig.labelBakLocation);
        FileUtils.mkdir(FileConfig.tmpLocation);
    }
}
