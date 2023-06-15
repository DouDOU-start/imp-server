package cn.hanglok.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author Allen
 * @version 1.0
 * @className FileUtils
 * @description 文件工具类
 * @date 2023/6/6 14:59
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static File convertTempFile(MultipartFile multipartFile) {
        File tempFile;
        try {
            tempFile = File.createTempFile("temp", multipartFile.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            logger.error("MultipartFile转File失败：", e);
            throw new RuntimeException(e);
        }

        logger.debug("MultipartFile转File成功！");

        return tempFile;
    }

    /**
     * 递归遍历文件夹并对文件执行操作
     * @param folder 文件夹
     * @param func 操作方式
     */
    public static void scanFolder(File folder, Function<File, Void> func) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles();

        if (files != null) {
            // 遍历文件和子文件夹
            for (File file : files) {
                if (file.isFile()) {
                    // 处理文件
//                    logger.info(file.getAbsolutePath());
                    func.apply(file);
                } else if (file.isDirectory()) {
                    // 处理子文件夹（递归调用）
                    scanFolder(file, func);
                }
            }
        }
    }

}