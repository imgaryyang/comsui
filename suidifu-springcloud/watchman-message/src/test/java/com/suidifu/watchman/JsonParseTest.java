package com.suidifu.watchman;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suidifu.watchman.message.amqp.request.AmqpRequest;
import org.junit.Assert;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-03-18 21:33
 * description:
 */
public class JsonParseTest {


    public void run(List<AmqpRequest> amqpRequests,String a, Integer b){

    }

    @Test
    public void getParamType() throws Exception{

        Class<?> clazz = Class.forName("com.suidifu.watchman.JsonParseTest");

        Method[] methods = clazz.getMethods();

        Method method = methods[0];

//        Class<?>[] paramsType = method.getParameterTypes();
//
//        for (Class<?> param : paramsType
//                ) {
//
//            if(param instanceof List.class){
//                System.out.println("list :"+param.getName());
//            }else{
//                System.out.println(param.getName());
//            }
//
//
//        }
        Type[] parameterTypes = method.getGenericParameterTypes();

        List<String> params = new ArrayList<>();

        for (Type param: parameterTypes
             ) {

            if((param instanceof ParameterizedType)) {

                Type[] typeArgument = ((ParameterizedType) param).getActualTypeArguments();

                if (typeArgument != null && typeArgument.length > 0) {

                    params.add(typeArgument[0].getTypeName());
                }
            }
            else{
                params.add(param.getTypeName());
            }
        }

        System.out.println("param :"+JSON.toJSONString(params));

        AmqpRequest amqpRequest = new AmqpRequest();

        amqpRequest.setParams(new Object[]{new ArrayList<AmqpRequest>(){{add(new AmqpRequest());}},"string",12});
        amqpRequest.setParamTypes(params.toArray(new String[3]));

        String amqpRequestJson = JSON.toJSONString(amqpRequest);

        AmqpRequest amqpRequest1 = JSON.parseObject(amqpRequestJson,AmqpRequest.class);

        for (Object param: amqpRequest1.getParams()
             ) {

            if(param instanceof JSONArray){

                Object obj1 = JSON.parseObject(param.toString(),Class.forName(amqpRequest1.getParamTypes()[0]));

                if(obj1 instanceof List){

                    System.out.println(" amqprequest from list is "+((List<AmqpRequest>) obj1).get(0).getUuid() );
                }
            }

            System.out.println("param value :"+param+", is jsonobject :" + (param instanceof JSONObject));
        }
    }

    @Test
    public void parseList(){

        List<AmqpRequest>  list = new ArrayList<>();

        Type[] types = list.getClass().getGenericInterfaces();

        for (Type type : types
                ) {

            System.out.println("type is "+type.getTypeName());
//            System.out.println(((ParameterizedType) type).getActualTypeArguments()[0].getClass().getName());
        }

        Type fstType = types[0];

        System.out.println("fstType :"+ fstType.getTypeName());

        Type[] params = ((ParameterizedType) types[0]).getActualTypeArguments();

        for (Type param: params
             ) {
           System.out.println("param is "+param.getTypeName());
        }

//        System.out.println(params[0].getClass().getName());
//
//        AmqpRequest amqpRequest1 = new AmqpRequest();
//
//        amqpRequest1.setBean("bean");
//
//        list.add(amqpRequest1);
//
//        List<?> parsedJsonList = JSON.parseArray(JSON.toJSONString(list),params[0].getClass());
//
//        Object obj1 = parsedJsonList.get(0);
//
//        Assert.assertEquals("bean",((AmqpRequest)obj1).getBean() );
    }
}
