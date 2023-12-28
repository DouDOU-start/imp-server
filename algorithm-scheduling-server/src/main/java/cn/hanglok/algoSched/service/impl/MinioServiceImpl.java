package cn.hanglok.algoSched.service.impl;

import cn.hanglok.algoSched.service.MinioService;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Allen
 * @version 1.0
 * @className MinioServiceImpl
 * @description TODO
 * @date 2023/12/26
 */
@Service
public class MinioServiceImpl implements MinioService {
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    @SneakyThrows
    public void uploadFile(MultipartFile file, String uploadDir) {

        String objectName = uploadDir + file.getOriginalFilename();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    /**
     * 获取对象外链接
     *
     * @param objectName 对象名
     * @param expires    过期时间，时间单位/s
     * @return url
     */
    @Override
    @SneakyThrows
    public String getObjectUrl(String objectName, Integer expires) {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expires)
                        .build()
        );
    }

    @Override
    public void zipObject(String[] objects, String zipPath) {
        try {

            // 用于存储ZIP文件的内存输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            // 获取并添加多个对象到ZIP文件
            for (String objectName : objects) {
                try (InputStream is = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build())) {

                    String fileName = new File(objectName).getName();

                    // 创建ZIP条目并写入数据
                    ZipEntry ze = new ZipEntry(fileName);
                    zos.putNextEntry(ze);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                }
            }

            // 完成ZIP文件的创建
            zos.close();

            // 将ZIP文件上传到MinIO
            try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(zipPath)
                                .stream(bais, bais.available(), -1)
                                .build());

                System.out.println("ZIP文件上传成功");
            }

        } catch (MinioException e) {
            System.out.println("错误发生: " + e);
        } catch (Exception e) {
            System.out.println("其他错误: " + e);
        }
    }

}
