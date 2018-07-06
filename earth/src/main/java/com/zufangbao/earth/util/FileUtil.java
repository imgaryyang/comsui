package com.zufangbao.earth.util;

import com.demo2do.core.utils.StringUtils;
import com.zufangbao.earth.exception.BusinessSystemException;
import com.zufangbao.sun.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by zfj on 17-7-4.
 */
public class FileUtil {

        /**
         * Save String to file.
         *
         * @param msg      the msg
         * @param fileName the file name
         * @param encode   the encode
         * @throws IOException                       the io exception
         * @throws BusinessSystemException the notify job location corrupt exception
         * @author whb
         * @date 2017 -06-22
         */
        public static void saveToFile(String msg, String fileName, String encode)
                throws IOException, BusinessSystemException {
            if (StringUtils.isEmpty(fileName)) {
                throw new BusinessSystemException("save string to file error : fileName is empty.");
            }
            FileUtils.write(new File(fileName), msg, encode);
        }

        /**
         * Read File to string string.
         *
         * @param fileName the file name
         * @param encode   the encode
         * @return the string
         * @throws IOException                       the io exception
         * @throws BusinessSystemException the notify job location corrupt exception
         * @author whb
         * @date 2017 -06-22
         */
        public static String readToString(String fileName, String encode)
                throws IOException, BusinessSystemException {
            if (StringUtils.isEmpty(fileName)) {
                throw new BusinessSystemException("read file to string error : fileName is empty.");
            }
            return FileUtils.readFileToString(new File(fileName), encode);
        }
}
