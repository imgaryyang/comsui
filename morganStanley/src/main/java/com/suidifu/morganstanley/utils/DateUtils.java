package com.suidifu.morganstanley.utils;

import java.util.Date;

public class DateUtils extends com.zufangbao.sun.utils.DateUtils {
    /**
     * 完整格式
     * <p>
     * yyyyMMddHHmmss
     */
    private static final String DATE_FORMAT_FULL_DATE_TIME = "yyyyMMddHHmmss";

    /**
     * 获取完整格式的时间：yyyyMMddHHmmss
     *
     * @param date 要解析的日期
     * @return 解析后的日期(字符串类型)
     */
    public static String getFullDateTime(Date date) {
        return format(date, DATE_FORMAT_FULL_DATE_TIME);
    }
}