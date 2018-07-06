package com.suidifu.dowjones.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * ExcelUtil工具类实现功能:
 * 导出时传入list<T>,即可实现导出为一个excel,其中每个对象Ｔ为Excel中的一条记录.
 * 导入时读取excel,得到的结果是一个list<T>.T是自己定义的对象.
 * 需要导出的实体对象只需简单配置注解就能实现灵活导出,通过注解您可以方便实现下面功能:
 * 1.实体属性配置了注解就能导出到excel中,每个属性都对应一列.
 * 2.列名称可以通过注解配置.
 * 3.导出到哪一列可以通过注解配置.
 * 4.鼠标移动到该列时提示信息可以通过注解配置.
 * 5.用注解设置只能下拉选择不能随意填写功能.
 * 6.用注解设置是否只导出标题而不导出内容,这在导出内容作为模板以供用户填写时比较实用.
 */
public class ExcelUtil<T> {

    private Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     * 生成单页表
     *
     * @param sheetName 工作表的名称
     * @param list 每个sheet中数据的行数,此数值必须小于65536
     * @param output java输出流
     */
    public boolean exportExcel(List<T> list, String sheetName, OutputStream output) {
        @SuppressWarnings("unchecked")
        List<T>[] lists = new ArrayList[1];
        lists[0] = list;

        String[] sheetNames = new String[1];
        sheetNames[0] = sheetName;

        return exportExcel(lists, sheetNames, output);
    }

    public boolean exportExcelXlsx(List<T> list, String sheetName, OutputStream output) {
        @SuppressWarnings("unchecked")
        List<T>[] lists = new ArrayList[1];
        lists[0] = list;

        String[] sheetNames = new String[1];
        sheetNames[0] = sheetName;

        return exportExcelXlsx(lists, sheetNames, output);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *  @param sheetNames 工作表的名称
     * @param output java输出流
     */
    private boolean exportExcel(List<T>[] lists, String[] sheetNames, OutputStream output) {
        if (lists.length != sheetNames.length) {
            System.out.println("数组长度不一致");
            return false;
        }
        // 产生工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();

        for (int ii = 0; ii < lists.length; ii++) {
            List<T> list = lists[ii];
            String sheetName = sheetNames[ii];

            List<Field> fields = getMappedFiled(clazz, null);

            HSSFSheet sheet = workbook.createSheet();
            // 产生工作表对象
            workbook.setSheetName(ii, sheetName);

            HSSFRow row;
            HSSFCell cell;
            // 产生单元格
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
            style.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
            // 产生一行 标题行
            row = sheet.createRow(0);
            // 写入各个字段的列头名称
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                ExcelVoAttribute attr = field.getAnnotation(ExcelVoAttribute.class);
                // 获得列号
                int col = getExcelCol(attr.column());
                // 创建列
                cell = row.createCell(col);
                // 设置列中写入内容为String类型
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                // 写入列名
                cell.setCellValue(attr.name());
                cell.setCellStyle(style);
            }

            int startNo = 0;
            int endNo = list.size();
            // 写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                // 得到导出对象.
                T vo = (T) list.get(i);
                for (int j = 0; j < fields.size(); j++) {
                    // 获得field.
                    Field field = fields.get(j);
                    // 设置实体类私有属性可访问
                    field.setAccessible(true);
                    ExcelVoAttribute attr = field.getAnnotation(ExcelVoAttribute.class);
                    try {
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            // 创建cell
                            cell = row.createCell(getExcelCol(attr.column()));
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            // 如果数据存在就填入,不存在填入空格.
                            cell.setCellValue(
                                field.get(vo) == null ? "" : String.valueOf(field.get(vo)));
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            output.flush();
            workbook.write(output);
            output.close();
            workbook.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed ");
            return false;
        }

    }


    /**
     * 对list数据源将其里面的数据导入到excel表单
     *  @param sheetNames 工作表的名称
     * @param output java输出流
     */
    private boolean exportExcelXlsx(List<T>[] lists, String[] sheetNames, OutputStream output) {
        if (lists.length != sheetNames.length) {
            System.out.println("数组长度不一致");
            return false;
        }
        // 产生工作薄对象
        XSSFWorkbook hwb = new XSSFWorkbook();

        for (int ii = 0; ii < lists.length; ii++) {
            List<T> list = lists[ii];
            String sheetName = sheetNames[ii];

            List<Field> fields = getMappedFiled(clazz, null);

            XSSFSheet sheet = hwb.createSheet(sheetName);

            XSSFRow row;
            XSSFCell cell;
            // 产生单元格
            XSSFCellStyle style = hwb.createCellStyle();
            style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
            style.setFillBackgroundColor(HSSFColor.GREY_40_PERCENT.index);
            // 产生一行 标题行
            row = sheet.createRow(0);
            // 写入各个字段的列头名称
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                ExcelVoAttribute attr = field.getAnnotation(ExcelVoAttribute.class);
                // 获得列号
                int col = getExcelCol(attr.column());
                // 创建列
                cell = row.createCell(col);
                // 设置列中写入内容为String类型
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                // 写入列名
                cell.setCellValue(attr.name());
                cell.setCellStyle(style);
            }

            int startNo = 0;
            int endNo = list.size();
            // 写入各条记录,每条记录对应excel表中的一行
            for (int i = startNo; i < endNo; i++) {
                row = sheet.createRow(i + 1 - startNo);
                // 得到导出对象.
                T vo = (T) list.get(i);
                for (int j = 0; j < fields.size(); j++) {
                    // 获得field.
                    Field field = fields.get(j);
                    // 设置实体类私有属性可访问
                    field.setAccessible(true);
                    ExcelVoAttribute attr = field.getAnnotation(ExcelVoAttribute.class);
                    try {
                        // 根据ExcelVOAttribute中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                        if (attr.isExport()) {
                            // 创建cell
                            cell = row.createCell(getExcelCol(attr.column()));
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            // 如果数据存在就填入,不存在填入空格.
                            cell.setCellValue(
                                field.get(vo) == null ? "" : String.valueOf(field.get(vo)));
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            output.flush();
            hwb.write(output);
            output.close();
            hwb.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed ");
            return false;
        }

    }

    /**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     */
    public static int getExcelCol(String col) {
        col = col.toUpperCase();
        // 从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
        int count = -1;
        char[] cs = col.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            count += (cs[i] - 64) * Math.pow(26, cs.length - 1 - i);
        }
        return count;
    }

    /**
     * 得到实体类所有通过注解映射了数据表的字段
     */
    private List<Field> getMappedFiled(Class<? super T> class1, List<Field> fields) {
        if (fields == null) {
            fields = new ArrayList<Field>();
        }

        // 得到所有定义字段
        Field[] allFields = class1.getDeclaredFields();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields) {
            if (field.isAnnotationPresent(ExcelVoAttribute.class)) {
                fields.add(field);
            }
        }
        if (class1.getSuperclass() != null && !class1.getSuperclass().equals(Object.class)) {
            getMappedFiled(class1.getSuperclass(), fields);
        }

        return fields;
    }

    public String exportOneRowDatasToOneRowCSV(T data)
    {
        List<Field> fields = getMappedFiled(clazz, null);

        String OneRowCSV = "";
        for (int j = 0; j < fields.size(); j++) {
            Field field = fields.get(j);// 获得field.
            field.setAccessible(true);// 设置实体类私有属性可访问
            ExcelVoAttribute attr = field.getAnnotation(ExcelVoAttribute.class);
            try {
                if (attr.isExport()) {
                    String fieldStr = field.get(data) == null ? "" : String.valueOf(field.get(data));
                    fieldStr = fieldStr.replace(",",";");
                    OneRowCSV += fieldStr + ",";
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        OneRowCSV = OneRowCSV.substring(0, OneRowCSV.length()>0?OneRowCSV.length()-1:0);
        return OneRowCSV;

    }
    public String exportDatasToCSVHeader()
    {
        List<Field> fields = getMappedFiled(clazz, null);
        String ColumnStr = "";
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            ExcelVoAttribute attr = field.getAnnotation(ExcelVoAttribute.class);
            String name = attr.name();
            ColumnStr += name + ",";
        }
        ColumnStr = ColumnStr.substring(0, ColumnStr.length()>0?ColumnStr.length()-1:0);
        return ColumnStr;

    }

    public List<String> exportDatasToCSV(List<T> list) {
        List<String> CSVdata = new ArrayList<String>();
        String ColumnStr = exportDatasToCSVHeader();
        CSVdata.add(ColumnStr);
        // 写入各条记录,每条记录对应excel表中的一行
        for (T vo : list) {
            String OneRowCSV = exportOneRowDatasToOneRowCSV(vo);
            CSVdata.add(OneRowCSV);
        }
        return CSVdata;
    }

    /**
     * 获取实体类所有通过注解了数据表的字段名
     * @return
     */
    public Map<String,String> buildCsvHeader() {
        Map<String,String> headers = new LinkedHashMap<>();
        Field[] allFields = clazz.getDeclaredFields();
        for (Field field : allFields) {
            ExcelVoAttribute attr = field.getAnnotation(ExcelVoAttribute.class);
            String key = field.getName();
            String name = attr.name();
            headers.put(key, name);
        }

        return headers;
    }

    public static void writeCSVToOutputStream(List<String> csvData, FileOutputStream outputStream, Boolean isUseBom) throws IOException {
        BufferedOutputStream bufStream = new BufferedOutputStream(outputStream);
//        PrintWriter outPutStream = new PrintWriter(bufStream);
        bufStream.flush();
        if (isUseBom) {
        bufStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
//        bufStream.write("\uFFFE".getBytes());
//        bufStream.write(new byte[] { (byte) 0xFF, (byte) 0xFE });
        }
        for (int i = 0 ; i < csvData.size();i++) {
            if (i != 0){
//                outPutStream.println();
                bufStream.write("\n".getBytes());
            }
//            outPutStream.print( csvData.get(i));
            bufStream.write(csvData.get(i).getBytes());
        }
//        outPutStream.flush();
//        outPutStream.close();
        bufStream.close();
    }

    public static void readCsv(String path) {
        try {
            File file = new File(path);
            FileReader fReader = null;
            fReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fReader);
            String[] strs = new String[0];
            strs = csvReader.readNext();
            if(strs != null && strs.length > 0){
                for(String str : strs)
                    if(null != str && !str.equals(""))
                        System.out.print(str + " , ");
                System.out.println("\n---------------");
            }
            List<String[]> list = null;
            list = csvReader.readAll();
            for(String[] ss : list){
                for(String s : ss)
                    if(null != s && !s.equals(""))
                        System.out.print(s + " , ");
                System.out.println();
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}