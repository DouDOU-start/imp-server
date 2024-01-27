package cn.hanglok.algoSched.entity.res;

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
    ERROR(400, "error"),
    BUSY(50001, "Algorithm Resource is busy...");

    private final int code;
    private final String msg;

     ResCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
