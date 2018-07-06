/**
 *
 */
package morganStanley.handler.impl;

import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.suidifu.swift.notifyserver.notifyserver.Exceptions.NotifyJobLocationCorruptException;
import com.suidifu.swift.utils.FileUtil;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.files.FileSignal;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static com.zufangbao.sun.utils.FilenameUtils.SIGNAL_FILE_SPLIT;
import static com.zufangbao.sun.utils.FilenameUtils.TXT_EXTENSIONS;

/**
 * @author hjl
 */
public class CreateSignalFile {

    public static void main(String[] args) throws IOException, NotifyJobLocationCorruptException {
        String TEST_MERID = "t_test_zfb";
        String TEST_SECRET = "123456";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG" +
                "/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK" +
                "+Le7CWKtv8MQL" +
                "+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD" +
                "/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU" +
                "+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV" +
                "+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx" +
                "/eMcITaLq8l1qzZ907UXY+Mfs=";
        String scanPath = "/home/hjl/桌面/测试用文件夹";
        File scanDirectory = new File(scanPath);
        Iterator itFile = FileUtils.iterateFiles(scanDirectory, TXT_EXTENSIONS, false);
        while (itFile.hasNext()) {
            File biz_file = (File) itFile.next();
            String signal_path = biz_file.getAbsolutePath() + SIGNAL_FILE_SPLIT;
            File signal_file = new File(signal_path);
            if (signal_file.exists()) {
                continue;
            }
            String biz_content = FileUtil.readToString(biz_file.getAbsolutePath(), FilenameUtils.UTF_8);
            FileSignal fileSignal = new FileSignal();
            fileSignal.setMerId(TEST_MERID);
            fileSignal.setSecret(TEST_SECRET);
            String sign = ApiSignUtils.rsaSign(biz_content, privateKey);
            fileSignal.setSign(sign);
            fileSignal.setFinancialProductCode("G31700");
            fileSignal.setRequestNo(UUIDUtil.random32UUID());
            fileSignal.setNotifyType(0);
            fileSignal.setFnCategory("repaymentOrder");
            String signal_file_content = JsonUtils.toJSONString(fileSignal);
            FileUtils.write(signal_file, signal_file_content, FilenameUtils.UTF_8);
        }
    }
}
