package cn.hanglok.algoSched.utils;

/**
 * @author Allen
 * @version 1.0
 * @className StringUtils
 * @description TODO
 * @date 2024/1/3
 */
public class StringUtils {
    public static String truncateString(String str, int length) {
        if (str == null) {
            return null;
        }
        return str.length() > length ? str.substring(0, length) + "..." : str;
    }
}
