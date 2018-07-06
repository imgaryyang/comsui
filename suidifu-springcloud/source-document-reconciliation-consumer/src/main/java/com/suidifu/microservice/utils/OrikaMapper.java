package com.suidifu.microservice.utils;

import java.util.List;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * 使用orika工具进行对象和对象之间的拷贝映射
 *
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 14:21 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public class OrikaMapper {
    private static final MapperFacade mapperFacade;
    private static final MapperFactory mapperFactory;

    static {
        mapperFactory = new DefaultMapperFactory.Builder().
                useAutoMapping(true).mapNulls(true).build();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    private OrikaMapper() {
        //no instance
    }

    public static <S, D> void map(S from, D to) {
        mapperFacade.map(from, to);
    }

    public static <S, D> D map(S from, Class<D> clazz) {
        return mapperFacade.map(from, clazz);
    }

    public static <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass) {
        return mapperFacade.mapAsList(source, destinationClass);
    }

    public static MapperFacade getMapperFacade() {
        return mapperFacade;
    }

    public static MapperFactory getMapperFactory() {
        return mapperFactory;
    }
}