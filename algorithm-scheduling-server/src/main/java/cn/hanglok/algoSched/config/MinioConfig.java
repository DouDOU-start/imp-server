package cn.hanglok.algoSched.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen
 * @version 1.0
 * @className MinioConfig
 * @description TODO
 * @date 2023/9/20
 */
@Data
@Configuration
public class MinioConfig {

    @Value("${minio.inner-url}")
    private String innerUrl;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.enable-public-network}")
    private Boolean enablePublicNetwork;

    @Value("${minio.public-url}")
    private String publicUrl;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    public MinioClient publicMinioClient() {
        return enablePublicNetwork ? MinioClient.builder()
                .endpoint(publicUrl)
                .credentials(accessKey, secretKey)
                .build() : null;
    }

}
