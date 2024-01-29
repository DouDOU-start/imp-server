package cn.hanglok.algoSched.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Allen
 * @version 1.0
 * @className RequireValidToken
 * @description TODO
 * @date 2024/1/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireValidToken {
}