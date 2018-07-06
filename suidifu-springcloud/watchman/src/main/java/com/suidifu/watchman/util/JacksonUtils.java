package com.suidifu.watchman.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-11 13:57
 * description:用jackson实现工具类
 */
public class JacksonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param text
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T parseObject(String text, Class<T> clazz) throws IOException {
        return objectMapper.readValue(text, clazz);
    }

    /**
     * @param text
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> parseArray(String text, Class<T> clazz) throws IOException {
        return (List<T>) objectMapper.readValue(text, clazz);
    }

    /**
     * @param value
     * @return
     * @throws JsonProcessingException
     */
    public static String toJSONString(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}