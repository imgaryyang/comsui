package com.suidifu.dowjones.utils;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/22 <br>
 * @time: 下午6:47 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public class SQLUtils {
    private SQLUtils() {
    }

    public static String wrapperSQL(String sql) {
        return "(" + sql + ") temp";
    }
}