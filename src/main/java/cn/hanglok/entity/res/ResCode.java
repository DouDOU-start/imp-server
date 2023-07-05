package cn.hanglok.entity.res;

import lombok.Getter;

/**
 * @author Allen
 * @version 1.0
 * @className RCode
 * @description TODO
 * @date 2023/7/4 14:19
 */
@Getter
public enum ResCode {

    OK(200, "ok"),
    ERROR(400, "error");

    private final int code;
    private final String msg;

    ResCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
