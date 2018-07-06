package com.suidifu.dowjones.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/29 <br>
 * @time: 下午10:18 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
public class ArrayUtils {
    private ArrayUtils() {
    }

    public static String[] mergeStringArrays(String[] array1, String[] array2) {
        String[] result = org.apache.commons.lang3.ArrayUtils.addAll(array1, array2);
        return removeNullAndRepeatedElement(result);
    }

    public static String[] removeNullAndRepeatedElement(String[] targetArray) {
        return (String[]) Arrays.stream(targetArray).filter(Objects::nonNull).distinct().
                collect(Collectors.toList()).toArray(new String[0]);
    }
}