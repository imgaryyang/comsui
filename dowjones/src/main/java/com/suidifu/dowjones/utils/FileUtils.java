package com.suidifu.dowjones.utils;

import com.suidifu.dowjones.vo.request.FileParameter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/3 <br>
 * @time: 17:39 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
@Slf4j
@Component
public class FileUtils {
    private String rootPath;

    private FileUtils() {
    }

    public static FileUtils getInstance() {
        return new FileUtils();
    }

    private static char byteToChar(byte[] b) {
        return (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
    }

    public String save(List<String> headers, List content, FileParameter fileParameter) throws IOException {
        String filePath = rootPath + fileParameter.getFilePath();
        String fileName = fileParameter.getFileName();
        log.info("\n\n\n\n\nfilePath is:{},fileName is:{}\n\n\n\n\n",
                filePath, fileName);
        org.apache.commons.io.FileUtils.forceMkdir(new File(filePath));

        String fullPath = filePath + fileName;

        try (FileWriter writer = new FileWriter(fullPath)) {
            writer.write(new String(Constants.BOM));
            for (int i = 0; i < headers.size(); i++) {
                writer.append(headers.get(i));
                if (i == headers.size() - 1) {
                    writer.append(Constants.RETURN);
                    break;
                }
                writer.append(Constants.COMMA);
            }

            fillContent(content, writer);
            writer.flush();
        }

        return fullPath;
    }

    public String save(List content, FileParameter fileParameter) throws IOException {
        String filePath = rootPath + fileParameter.getFilePath();
        String fileName = fileParameter.getFileName();
        log.info("\n\n\n\n\nfilePath is:{},fileName is:{}\n\n\n\n\n",
                filePath, fileName);
        org.apache.commons.io.FileUtils.forceMkdir(new File(filePath));

        String fullPath = filePath + fileName;
        try (FileWriter writer = new FileWriter(fullPath)) {
            fillContent(content, writer);
            writer.flush();
        }

        return fullPath;
    }

    private void fillContent(List content, FileWriter writer) throws IOException {
        for (Object element : content) {
            Field[] fields = element.getClass().getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].isAccessible()) {
                    fields[i].setAccessible(true);
                }

                try {
                    writer.append(fields[i].get(element) != null ? String.valueOf(fields[i].get(element)) : "");
                    if (i == fields.length - 1) {
                        writer.append(Constants.RETURN);
                        break;
                    }
                    writer.append(Constants.COMMA);
                } catch (IllegalAccessException e) {
                    log.error("IllegalAccessException is: {}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }

    public String saveWithHeader(String header, List collection, FileParameter fileParameter) throws IOException {
        String filePath = rootPath + fileParameter.getFilePath();
        log.info("filePath is:{}", filePath);
        String fileName = fileParameter.getFileName();
        org.apache.commons.io.FileUtils.forceMkdir(new File(filePath));

        String fullPath = filePath + fileName;
        try (FileWriter writer = new FileWriter(fullPath)) {
            writer.write(new String(Constants.BOM));
            writer.append(header);
            writer.append("\n");
            writeCollectionData(writer, collection);
            writer.flush();
        } catch (Exception e) {
            log.error("Exception is: {}", ExceptionUtils.getStackTrace(e));
        }

        return fullPath;
    }

    private void writeCollectionData(FileWriter writer, List collection) throws IOException, IllegalAccessException {
        for (Object element : collection) {
            Field[] fields = element.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (!fields[i].isAccessible()) {
                    fields[i].setAccessible(true);
                }
//                log.info("attribute is:{}", fields[i].get(element));
                writer.append(fields[i].get(element) == null ? "" : String.valueOf(fields[i].get(element)));
                if (i == fields.length - 1) {
                    writer.append("\n");
                    break;
                }
                writer.append(",");
            }
        }
    }

}