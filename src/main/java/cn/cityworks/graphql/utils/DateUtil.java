package cn.cityworks.graphql.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 日期转字符串
     *
     * @param date
     * @param formart 格式
     * @return
     */
    public static String format(Date date, String formart) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formart);
        try {
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串转日期
     *
     * @param date
     * @param formart 格式
     * @return
     */
    public static Date parseDate(String date, String formart) {
        if (date.equals("now")) {
            return new Date();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formart);
        try {
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
