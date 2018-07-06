package com.suidifu.morganstanley.utils;

import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.wellsfargo.annotation.CheckValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.SYSTEM_ERROR;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/29 <br>
 * Time:上午2:54 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@Slf4j
public class AnnotationUtils {
    /**
     * 校验字段是否为空
     *
     * @param obj
     * @return 如果返回null表示：校验通过
     */
    public static String checkValue(Object obj) {
        return parseAnnotation(CheckValue.class, obj);
    }

    private static String parseAnnotation(Class<? extends Annotation> aClazz, Object obj) {
        StringBuilder sb = new StringBuilder();
        boolean flag = false;
        List<Field> toCheckFields = new ArrayList<>();

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            Annotation annotation = field.getAnnotation(aClazz);
            if (annotation == null) {
                continue;
            }
            if (annotation instanceof CheckValue) {
                toCheckFields.add(field);
            }
        }

        try {
            if (toCheckFields.size() == 2) {
                Field firstField = toCheckFields.get(0);
                Field secondField = toCheckFields.get(1);
                firstField.setAccessible(true);
                secondField.setAccessible(true);
                String firstValue = (String) firstField.get(obj);
                String secondValue = (String) secondField.get(obj);

                if (StringUtils.isEmpty(firstValue) && StringUtils.isEmpty(secondValue)) {
                    sb.append("请选填其中一种编号［").append(firstField.getName()).append("，").append(secondField.getName()).append("］");
                    flag = true;
                }
            }
        } catch (IllegalAccessException e) {
            log.error("解析注解出错：{}", ExceptionUtils.getStackTrace(e));
            throw new ApiException(SYSTEM_ERROR);
        }

        if (flag) {
            return sb.toString();
        } else {
            return null;
        }
    }
}