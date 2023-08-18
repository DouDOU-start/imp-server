package cn.hanglok.pacs.entity.res;

import lombok.Data;

/**
 * @author Allen
 * @version 1.0
 * @className R
 * @description TODO
 * @date 2023/7/4 14:03
 */
@Data
public class Res<T> {

    private int code;
    private String msg;
    private T data;

    private Res(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Res(ResCode resCode, T data) {
        this.code = resCode.getCode();
        this.msg = resCode.getMsg();
        this.data = data;
    }

    public static <T> Res<T> ok(T data) {
        return new Res<>(ResCode.OK, data);
    }

    public static <T> Res<T> error(ResCode errorCode) {
        return new Res<>(errorCode, null);
    }

    public static <T> Res<T> entity(int code, String msg, T data) {
        return new Res<>(code, msg, data);
    }
}
