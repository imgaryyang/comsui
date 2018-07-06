package com.suidifu.morganstanley.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/13 <br>
 * Time:上午3:09 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Log4j2
public class JSONUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JSONUtils() {
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClass    元素类
     * @return CollectionType Collection类型
     */
    private static CollectionType getCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return OBJECT_MAPPER.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    /**
     * 获取包含指定元素类的list
     *
     * @param jsonString json字符串
     * @param clazz      元素类
     * @param <T>        元素类泛型
     * @return 包含元素类的list
     */
    public static <T> List<T> parseCollection(String jsonString, Class<T> clazz) {
        List<T> result;

        try {
            result = OBJECT_MAPPER.readValue(jsonString, getCollectionType(ArrayList.class, clazz));
        } catch (IOException e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
            throw new ApiException(ApiMessage.SYSTEM_ERROR);
        }
        return result;
    }

    public static <T> List<T> getDetailModel(String jsonText, Class<T> clazz) {
        List<T> result = JSON.parseArray(jsonText, clazz);
        if (CollectionUtils.isEmpty(result)) {
            throw new ApiException(ApiMessage.WRONG_FORMAT);
        }
        return result;
    }
}