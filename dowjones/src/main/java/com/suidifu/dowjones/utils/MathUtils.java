package com.suidifu.dowjones.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/22 <br>
 * @time: 下午5:16 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
public class MathUtils {
    private MathUtils() {
    }

    public static int[] initOverDueDays(int periodDays, int graceDay) {
        int[] result = new int[3];

        for (int i = 0; i < 3; i++) {
            result[i] = periodDays * (i + 1) + graceDay;
        }
        return result;
    }

    public static BigDecimal divide(BigDecimal numerator, BigDecimal denominator) {
        return numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_DOWN);
    }
}