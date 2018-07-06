/**
 * 
 */
package com.demo2do.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author Downpour
 */
public abstract class EnumUtils {
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Enum[]> scan(String basePackage) {
		
		Map<String, Enum[]> enums = new HashMap<String, Enum[]>();
		
		// first find out all the enum classes according to the entity packages
        ClasspathScanner<Enum<?>> scanner = new ClasspathScanner<Enum<?>>();
        scanner.findImplementations(Enum.class, new String[]{basePackage});
        
        for(Class<?> clazz : scanner.getClasses()) {
        	
        	Alias alias = AnnotationUtils.findAnnotation(clazz, Alias.class);
        	String key = alias != null ? alias.value() : clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
        	
        	try {
				enums.put(key, (Enum[]) clazz.getMethod("values").invoke(clazz));
			} catch (Exception e){
				e.printStackTrace();
			}
        }
		
		return enums;
	}
	
}
