package com.suidifu.morganstanley.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.List;

/**
 * 文件操作
 */
@Log4j2
public class CSVUtils {
    private CSVUtils() {
    }

    /**
     * 生成为CVS文件
     *
     * @param exportData 源数据List
     *                   csv文件的列表头map
     * @param outPutPath 文件路径
     */
    @SuppressWarnings("rawtypes")
    public static void createCSVFile(List<String> exportData, String outPutPath) {
        File file = new File(outPutPath);
        log.info("文件路径：{}", outPutPath);
        try (// UTF-8使正确读取分隔符","
             BufferedWriter csvFileOutputStream = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            if (!file.exists()) {
                log.info("当前文件不存在，创建文件");
                file.createNewFile();
            }

            if (exportData != null && !exportData.isEmpty()) {
                for (String data : exportData) {
                    csvFileOutputStream.append(data).append("\r");
                }
            }
            csvFileOutputStream.flush();
            log.info("输出完成");
        } catch (IOException e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
        }
    }
}