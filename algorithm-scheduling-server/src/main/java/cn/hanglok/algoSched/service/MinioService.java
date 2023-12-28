package cn.hanglok.algoSched.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Allen
 * @version 1.0
 * @className MinioService
 * @description TODO
 * @date 2023/12/26
 */
public interface MinioService {
    void uploadFile(MultipartFile file, String uploadDir);

    String getObjectUrl(String objectName, Integer expires);

    void zipObject(String[] objects, String zipPath);
}
