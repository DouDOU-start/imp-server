package cn.hanglok.controller;

import cn.hanglok.util.DicomUtils;
import cn.hanglok.util.OkHttpUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import cn.hanglok.util.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.function.Function;

/**
 * @author Allen
 * @version 1.0
 * @className FileTest
 * @description TODO
 * @date 2023/5/30 14:48
 */
@SpringBootTest
public class FileTest {

    private final Logger logger = LoggerFactory.getLogger(FileTest.class);

    @Test
    public void uploadDicomFile() {
        String uploadUrl = "http://127.0.0.1:8080/file/dicom";
        File folder = new File("/Users/allen/hanglok/影像数据");
        Function<File, Void> func = file -> {
            if (DicomUtils.verify(file)  && ! file.getName().equals("DICOMDIR")) {
                OkHttpUtils.uploadFile(uploadUrl, file);
            }
            return null;
        };
        FileUtils.scanFolder(folder, func);
    }
}
