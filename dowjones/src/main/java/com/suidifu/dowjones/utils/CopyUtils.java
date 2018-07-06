package com.suidifu.dowjones.utils;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 14:21 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public class CopyUtils {

    public static MapperFactory init() {
        //创建mapper的工厂类
        return new DefaultMapperFactory.Builder().build();
    }
}