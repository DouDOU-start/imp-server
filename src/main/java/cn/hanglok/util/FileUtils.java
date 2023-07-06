package cn.hanglok.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.springframework.util.FileCopyUtils.BUFFER_SIZE;

/**
 * @author Allen
 * @version 1.0
 * @className FileUtils
 * @description 文件工具类
 * @date 2023/6/6 14:59
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * multipartFile转File
     *
     * @param multipartFile multipartFile
     * @return File
     */
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

    /**
     * 创建目录
     *
     * @param pathName 目录地址
     */
    public static void mkdir(String pathName) {
        File file = new File(pathName);
        if (!file.exists()) {
            logger.info(String.format("目录`%s`不存在，创建目录", pathName));
            if (file.mkdirs()) {
                logger.info(String.format("目录`%s`创建成功", pathName));
            }
        }
    }

    /**
     * 压缩ZIP
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param keepDirStructure 是否保留原来的目录结构,true: 保留目录结构;
     *                         false: 所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
            long end = System.currentTimeMillis();
            logger.info("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            logger.error("压缩ZIP失败", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception 压缩失败异常
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean keepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (keepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), true);
                    } else {
                        compress(file, zos, file.getName(), false);
                    }

                }
            }
        }
    }

    /**
     * 复制文件夹
     *
     * @param sourcePath 源文件夹路径
     * @param targetPath 目标文件夹路径
     */
    public static void copyFolder(String sourcePath, String targetPath) {

        //源文件夹路径
        File sourceFile = new File(sourcePath);
        //目标文件夹路径
        File targetFile = new File(targetPath);

        if (!sourceFile.exists()) {
            logger.error("源文件夹不存在，" + sourcePath);
            return;
        }
        if (!sourceFile.isDirectory()) {
            logger.error("源文件夹不是目录，" + sourcePath);
            return;
        }
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        if (!targetFile.isDirectory()) {
            logger.error("目标文件夹不是目录");
            return;
        }

        File[] files = sourceFile.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            //文件要移动的路径
            String movePath = targetFile + File.separator + file.getName();
            if (file.isDirectory()) {
                //如果是目录则递归调用
                copyFolder(file.getAbsolutePath(), movePath);
            } else {
                //如果是文件则复制文件
                try (FileInputStream inputStream = new FileInputStream(file);
                     FileOutputStream outputStream = new FileOutputStream(movePath);
                     FileChannel inputChannel = inputStream.getChannel();
                     FileChannel outputChannel = outputStream.getChannel()) {
                    outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                } catch (Exception e) {
                    logger.error("复制文件夹发生异常", e);
                }
            }
        }
    }

    public static void copyFile(File source, File dest) {
        try (FileInputStream inputStream = new FileInputStream(source);
             FileOutputStream outputStream = new FileOutputStream(dest);
             FileChannel inputChannel = inputStream.getChannel();
             FileChannel outputChannel = outputStream.getChannel()) {
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            logger.error("复制文件发生异常", e);
        }
    }

}