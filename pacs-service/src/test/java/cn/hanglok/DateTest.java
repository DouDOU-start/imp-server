package cn.hanglok;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Allen
 * @version 1.0
 * @className DateTest
 * @description TODO
 * @date 2023/6/6 10:19
 */
public class DateTest {

    @Test
    public void DateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date;
        try {
            date = format.parse("20210511");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println(date);
    }
}
