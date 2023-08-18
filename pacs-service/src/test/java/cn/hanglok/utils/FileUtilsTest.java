package cn.hanglok.utils;

import cn.hanglok.pacs.config.FileConfig;
import cn.hanglok.pacs.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author Allen
 * @version 1.0
 * @className FileUtilsTest
 * @description 文件工具测试类
 * @date 2023/7/6 11:22
 */
@SpringBootTest
public class FileUtilsTest {

    Logger logger = LoggerFactory.getLogger(FileUtils.class);

    @Test
    public void toZipTest() throws FileNotFoundException {
        FileUtils.toZip(
                "/Users/allen/imp-upload/dicom/阳江市人民医院",
                new FileOutputStream("/Users/allen/imp-fileDir/tmp/ttt.zip"),
                true
        );
    }


    @Test
    public void copyFile() {

        long seriesId = 350L;

        String institutionName = "阳江市人民医院";
        String patientUid = "315351";
        String studiesUid = "1.2.840.113704.1.111.5156.1391646736.206";
        String seriesUid = "1.2.840.113704.1.111.7124.1391647338.18";

        String sourcePath = FileConfig.dicomLocation + File.separator +
                institutionName + File.separator +
                patientUid + File.separator +
                studiesUid + File.separator +
                seriesUid;

        if (!new File(sourcePath).exists()) {
            logger.error("系列影像路径不存在");
            return;
        }

        // 创建临时目录
        File file;
        while (true) {
            file = new File(FileConfig.tmpLocation + File.separator + System.currentTimeMillis());
            if (!file.exists()) {
                file.mkdirs();
                break;
            }
        }

        // 复制DICOM文件
        FileUtils.copyFolder(sourcePath, file.getAbsolutePath() + File.separator + "dicom");

        // 复制标注文件
        File[] files = new File(FileConfig.labelLocation)
                .listFiles((dir1, name) -> name.substring(0, name.indexOf(".")).equals(String.valueOf(seriesId)));

        for (File labelFile : files) {
            FileUtils.copyFile(
                    labelFile,
                    new File(file.getAbsolutePath() + File.separator + labelFile.getName())
            );
        }

    }

}
