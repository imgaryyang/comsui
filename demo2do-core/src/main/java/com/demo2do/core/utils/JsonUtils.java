/**
 * 
 */
package com.demo2do.core.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;

import com.alibaba.fastjson.JSON;

/**
 * @author Downpour
 */
public abstract class JsonUtils {
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object) {
		return JSON.toJSONString(object);
	}
	
	/**
	 * 
	 * @param jsonText
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> parse(String jsonText) {
		try {
			Map<String, Object> object = (Map<String, Object>) JSON.parse(jsonText);
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param jsonText
	 * @param clazz
	 * @return
	 */
	public static <T> T parse(String jsonText, Class<T> clazz) {
		try {
			T object = JSON.parseObject(jsonText, clazz);
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static Map<String, Object> parse(File file) {
		try {
			String jsonText = FileUtils.readFileToString(file);
			return (Map<String, Object>) JSON.parse(jsonText);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static Map<String, Object> parse(Resource resource) {
		try {
			String jsonText = FileUtils.readFileToString(resource.getFile());
			return (Map<String, Object>) JSON.parse(jsonText);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param jsonText
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String, Object>> parseArray(String jsonText) {
		try {
			List<Map<String, Object>> result = (List) JSON.parseArray(jsonText); 
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String, Object>> parseArray(File file) {
		try {
			String jsonText = FileUtils.readFileToString(file);
			return (List) JSON.parseArray(jsonText);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String, Object>> parseArray(Resource resource) {
		try {
			String jsonText = FileUtils.readFileToString(resource.getFile());
			return (List) JSON.parseArray(jsonText);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param jsonText
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(String jsonText, Class<T> clazz) {
		try {
			List<T> result = JSON.parseArray(jsonText, clazz);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(File file, Class<T> clazz) {
		try {
			String jsonText = FileUtils.readFileToString(file);
			return JSON.parseArray(jsonText, clazz);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * @param resource
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> parseArray(Resource resource, Class<T> clazz) {
		try {
			String jsonText = FileUtils.readFileToString(resource.getFile());
			return JSON.parseArray(jsonText, clazz);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
