package com.suidifu.morganstanley.handler.repayment;

import com.suidifu.morganstanley.model.request.repayment.OverdueFeeDetail;
import com.zufangbao.sun.api.model.modify.ModifyOverdueParams;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;
import java.util.Map;

/**
 * @author louguanyang at 2017/10/13 22:39
 */
public interface OverdueFeeApiHandler {

    /**
     * 检查是否存在重复请求编号
     *
     * @param requestNo 请求编号
     * @param ip        IP地址
     */
    void checkRequestNo(@NotBlank String requestNo, @NotBlank String ip);

    /**
     * 保存请求Log
     *
     * @param requestNo   请求编号
     * @param requestData 请求数据
     * @param ip          请求IP地址
     */
    void saveLog(@NotBlank String requestNo, @NotBlank String requestData, @NotBlank String ip);

    /**
     * 处理 变更逾期费用
     *
     * @param paramsList 变更逾期费用数据集
     * @return 处理失败数据List
     */
    List<Map<String, String>> modifyOverdueFee(List<ModifyOverdueParams> paramsList);

    /**
     * 校验请求数据, 返回封装数据列表
     *
     * @param overdueFeeDetails 变更逾期费用请求List
     * @return 封装数据列表
     */
    List<ModifyOverdueParams> verifyAndReturnParamsList(List<OverdueFeeDetail> overdueFeeDetails);
}
