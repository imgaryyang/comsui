package com.zufangbao.cucumber.method.method;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;

/**
 * Created by juxer on 17-3-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:/local/applicationContext-*.xml",
})
public class testThird111{

    String productCode = "G31700";
    String deductId =  UUID.randomUUID().toString();
    String uniqueId = "xwq1131";
    String totalAmount = "1500";
    String amount = "500";
    String repaymentPlanNo = "";
    String result = "";
//    String firstPlanDate = DateUtils.format(DateUtils.addDays(new Date(), 0));
    String firstPlanDate = DateUtils.format(DateUtils.addDays(new Date(), -1));
    String secondPlanDate = DateUtils.format(DateUtils.addDays(new Date(), 11));
    String thirdPlanDate = DateUtils.format(DateUtils.addDays(new Date(), 12));
    String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), -1));

    @Autowired
    private DeductApplicationDetailService deductApplicationDetailService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;

    /**
     * 放款，导入资产包
     */
//    @Autowired
//    private PrepaymentCucumberMethod prepaymentCucumberMethod;
    PrepaymentCucumberMethod prepaymentCucumberMethod = new PrepaymentCucumberMethod();
    @Test
 	public void makeLoan() throws Throwable{
//        //放款
//        prepaymentCucumberMethod.makeLoan(productCode, uniqueId, amount);
////        while(true){
////            int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
////
////            if(executionStatus == 2){
////                break;
////            }
////            Thread.sleep(10000);
////        }
//        Thread.sleep(90000);
//        //导入资产包

        prepaymentCucumberMethod.importAssetPackage(totalAmount, productCode, uniqueId, "0", amount, firstPlanDate, secondPlanDate, thirdPlanDate);
    }
    
    /**
     * 扣款
     */
//    @Autowired
//    private QueryDeductInfoTestMethod queryDeductInfoTestMethod;
    QueryDeductInfoTestMethod queryDeductInfoTestMethod = new QueryDeductInfoTestMethod();
    @Test
    public void deduct111() throws Throwable{
        //进行扣款
        //第一次扣款
        repaymentPlanNo = queryDeductInfoTestMethod.query_i_RepaymentPlan(uniqueId, 0);//查询第1个还款计划
        queryDeductInfoTestMethod.deductRepaymentPlan(UUID.randomUUID().toString(), repaymentPlanNo, uniqueId, productCode, "500", "1");//1.zhengchang 0 tiqian 2 yuqi
//        while(true){
//            AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
//            ExecutingStatus executingStatus = asset.getExecutingStatus();
//            if(ExecutingStatus.SUCCESSFUL == executingStatus){
//                break;
//            }
//            Thread.sleep(10000);
//        }
//        Thread.sleep(50000);
//        //第二次扣款
//        repaymentPlanNo=queryDeductInfoTestMethod.query_i_RepaymentPlan(uniqueId, 1);//查询第2个还款计划
//        queryDeductInfoTestMethod.deductRepaymentPlan(UUID.randomUUID().toString(), repaymentPlanNo, uniqueId, productCode, amount, "0");
//        while(true){
//            AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
//            ExecutingStatus executingStatus = asset.getExecutingStatus();
//            if(ExecutingStatus.SUCCESSFUL == executingStatus){
//                break;
//            }
//            Thread.sleep(10000);
//        }
//        Thread.sleep(50000);
//        //第三次扣款
//        repaymentPlanNo=queryDeductInfoTestMethod.query_i_RepaymentPlan(uniqueId, 2);//查询第3个还款计划
//        queryDeductInfoTestMethod.deductRepaymentPlan(UUID.randomUUID().toString(), repaymentPlanNo, uniqueId, productCode, amount, "0");
//        while(true){
//            AssetSet asset = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
//            ExecutingStatus executingStatus = asset.getExecutingStatus();
//            if(ExecutingStatus.SUCCESSFUL == executingStatus){
//                break;
//            }
//            Thread.sleep(10000);
//        }
    }

    @Test
    public void prepaymentTest111(){
        prepaymentCucumberMethod.applyPrepaymentPlan(uniqueId,"1500","1500",DateUtils.format(new Date()));
    }
    
    /**
     * 变更还款信息
     */
//    @Autowired
//    private ModifyApiMethod modifyApiMethod;
    ModifyApiMethod modifyApiMethod = new ModifyApiMethod();
    @Test
    public void modifyRepaymentInformation(){
    	String payerName = null;
    	String bankCode = "C10104";
    	String bankName = "中国银行";
    	modifyApiMethod.modifyRepaymentInformation(uniqueId,payerName,bankCode,bankName);
    }
    
    
}
