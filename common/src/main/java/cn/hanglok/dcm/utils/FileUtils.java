package cn.hanglok.dcm.utils;

import java.io.File;
import java.util.function.Function;

/**
 * @author Allen
 * @version 1.0
 * @className FileUtils
 * @description TODO
 * @date 2023/9/6
 */
public class FileUtils {
    /**
     * 递归遍历文件夹并对文件执行操作
     *
     * @param folder 文件夹
     * @param func   操作方式
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
