package com.suidifu.dowjones.service;

import java.util.Date;

/**
 * Created by zxj on 2018/2/7.
 */
public interface ABS_FileService {

    /**
     * 生成实际还款文件
     * @param time
     */
    void doActualRepaymentFileData(Date time);

    /**
     * 贷款合同变更文件
     * @param time
     */
    void doContractChangeFileData(Date time);

    /**
     * 贷款合同未尝情况文件
     * @param time
     */
    void doCurrentContractDetailFileData(Date time);

}
