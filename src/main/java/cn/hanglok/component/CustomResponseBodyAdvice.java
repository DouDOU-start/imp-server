package cn.hanglok.component;

import cn.hanglok.entity.res.Res;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Allen
 * @version 1.0
 * @className CustomResponseBodyAdvice
 * @description 控制器响应前置配置
 * @date 2023/7/4 15:34
 */
@RestControllerAdvice
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NotNull MethodParameter methodParameter, @NotNull Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                       @NotNull MethodParameter returnType,
                                       @NotNull MediaType selectedContentType,
                                       @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                       @NotNull ServerHttpRequest request,
                                       @NotNull ServerHttpResponse response) {

        if (body instanceof Res) {
            if (200 != ((Res<?>) body).getCode()) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
            }
        }

        return body;
    }
}