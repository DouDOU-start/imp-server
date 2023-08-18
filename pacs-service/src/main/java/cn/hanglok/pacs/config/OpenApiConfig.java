package cn.hanglok.pacs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen
 * @version 1.0
 * @className OpenApiConfig
 * @description TODO
 * @date 2023/6/15 13:53
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI immOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("影像管理平台 RESTful API")
                        .description("影像管理平台接口文档")
                        .version("v1.0.0")
                        .contact(new Contact().name(AuthorConfig.AUTHOR_NAME).email(AuthorConfig.EMAIL)));
    }
}
