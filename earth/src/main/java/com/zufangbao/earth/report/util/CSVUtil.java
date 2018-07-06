package com.zufangbao.earth.report.util;

import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.utils.excel.ExcelVoAttribute;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * csv导出工具
 * @author zhanghongbing
 *
 * @param <T> 导出vo类
 */
public class CSVUtil<T> {
	
	public static final String CSV_DATA_SEPARATOR = ",";
	
	public static final String CSV_DATA_SEPARATOR_REPLACE = ";";

	/**
	 * 导出字段
	 */
	private List<Field> exportFields = new ArrayList<Field>();

	public CSVUtil(Class<T> clazz) {
        initExportFields(clazz, exportFields);
	}

	public String buildCsvSingleRowData(T data) {
		StringBuffer buffer = new StringBuffer();
		for (Field field : exportFields) {
			field.setAccessible(true);// 设置实体类私有属性可访问
			try {
				String fieldStr = field.get(data) == null ? "" : String.valueOf(field.get(data));
				fieldStr = fieldStr.replace(CSV_DATA_SEPARATOR, CSV_DATA_SEPARATOR_REPLACE);
				buffer.append(fieldStr);
				buffer.append(CSV_DATA_SEPARATOR);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return buffer.substring(0, buffer.length() > 0 ? buffer.length() - 1 : 0);
	}
	
	public String buildCsvSingleRowDataV2(T data) {
		StringBuffer buffer = new StringBuffer();
		for (Field field : exportFields) {
			field.setAccessible(true);// 设置实体类私有属性可访问
			try {
				String fieldStr = field.get(data) == null ? "" : String.valueOf(field.get(data));
				if(fieldStr.contains(",")&&!(fieldStr.startsWith("\"")&&fieldStr.endsWith("\""))){
					fieldStr="\""+fieldStr+"\"";
				}
				buffer.append(fieldStr);
				buffer.append(CSV_DATA_SEPARATOR);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		buffer.substring(0, buffer.length());
		return buffer.toString();
	}
	
	public String buildCsvHeader() {
		StringBuffer buffer = new StringBuffer();
		for (Field field : exportFields) {
			ExcelVoAttribute attr = field.getAnnotation(ExcelVoAttribute.class);
			String name = attr.name();
			buffer.append(name);
			buffer.append(CSV_DATA_SEPARATOR);
		}
		return buffer.substring(0, buffer.length() > 0 ? buffer.length() - 1 : 0);
	}
	
	public List<String> buildCsvDatas(List<T> list) {
		List<String> csvDatas = new ArrayList<String>();
		if(CollectionUtils.isEmpty(list)) {
			Collections.emptyList();
		}
		String csvHeader = buildCsvHeader();
		csvDatas.add(csvHeader);
		
		for (T vo : list) {
			String csvSingleRowData = buildCsvSingleRowData(vo);
			csvDatas.add(csvSingleRowData);
		}
		return csvDatas;
	}

	private void initExportFields(Class<? super T> clazz, List<Field> fields) {
		if (fields == null) {
			fields = new ArrayList<Field>();
		}

		Field[] allFields = clazz.getDeclaredFields();// 得到所有定义字段
		// 得到所有field并存放到一个list中.
		for (Field field : allFields) {
			if (!field.isAnnotationPresent(ExcelVoAttribute.class)) {
				continue;
			}
			ExcelVoAttribute attr = field.getAnnotation(ExcelVoAttribute.class);
			if (attr.isExport()) {
				fields.add(field);
			}
		}
		if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
			initExportFields(clazz.getSuperclass(), fields);
		}
		Collections.sort(fields, new Comparator<Field>() {  
	      @Override  
	      public int compare(Field field1, Field field2) {  
            		int filedColumn1=ExcelUtil.getExcelCol(field1.getAnnotation(ExcelVoAttribute.class).column());
            		int filedColumn2=ExcelUtil.getExcelCol(field2.getAnnotation(ExcelVoAttribute.class).column());
                int i = filedColumn1-filedColumn2;  
                return i;  
	            }  
	        });
	}
	
}
