package cn.hanglok.controller;

import cn.hanglok.common.http.OkHttpUtils;
import cn.hanglok.pacs.util.DicomUtils;
import lombok.extern.slf4j.Slf4j;
import cn.hanglok.pacs.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.function.Function;

/**
 * @author Allen
 * @version 1.0
 * @className FileTest
 * @description TODO
 * @date 2023/5/30 14:48
 */
@Slf4j
public class FileTest {

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
