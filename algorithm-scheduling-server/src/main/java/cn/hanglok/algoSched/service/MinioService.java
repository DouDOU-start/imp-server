package cn.hanglok.algoSched.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Allen
 * @version 1.0
 * @className MinioService
 * @description TODO
 * @date 2023/12/26
 */
public interface MinioService {
    void uploadFile(MultipartFile file, String uploadDir);

    String getObjectUrl(String objectName, Integer expires) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    void zipObject(String[] objects, String zipPath);
}
