package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.entity.Result;
import com.zufangbao.sun.ledgerbook.DuplicateAssetsException;
import com.zufangbao.sun.yunxin.entity.NFQLoanInformation;
import com.zufangbao.sun.yunxin.entity.NFQRepaymentPlan;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Dec 14, 2016 4:05:47 PM 
* 类说明 
*/
public interface AssetPackageHandler {

    Result importAssetPackagesViaExcel(List<NFQLoanInformation> loanInformationList, List<NFQRepaymentPlan> repaymentPlanInformationList, Long financialContractId,
                                       String operatorName, String ipAddress) throws IOException, InvalidFormatException, DuplicateAssetsException;


    void verifyInputParam(List<NFQLoanInformation> loanInformationList, List<NFQRepaymentPlan> repaymentPlanList,
                          Long financialContractNo);
}
