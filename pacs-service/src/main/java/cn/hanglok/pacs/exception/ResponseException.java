package cn.hanglok.pacs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Allen
 * @version 1.0
 * @className ResponseException
 * @description TODO
 * @date 2023/6/14 10:07
 */
public class ResponseException {

    /**
     * UNPROCESSABLE_ENTITY 异常
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public static class UnprocessableEntityException extends RuntimeException {
    }
}
