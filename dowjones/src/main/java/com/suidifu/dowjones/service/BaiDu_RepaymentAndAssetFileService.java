package com.suidifu.dowjones.service;

import java.util.Date;

/**
 * Created by zxj on 2018/1/22.
 */
public interface BaiDu_RepaymentAndAssetFileService {

    /**
     * 还款文件 资产文件
     * 当日生成的还款记录（不含回购）以及变更（提前还款）
     *
     * @param financialContractUuid
     * @param yesterday
     */
    void doFileData(String financialContractUuid, Date yesterday);

    /**
     * 新增还款文件 新增资产文件
     * 当日放款成功的贷款合同
     *
     * @param financialContractUuid
     * @param yesterday
     */
    void doIncrementalFileData(String financialContractUuid, Date yesterday);
}
