package com.zufangbao.earth.api.test.post;

import com.zufangbao.cucumber.method.BaseTestMethod;
import com.zufangbao.cucumber.method.commandUrl;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by dzz on 17-5-23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class RepaymentOrderApiTest extends commandUrl {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;

    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String productCode="G31700";//信托代码
    String totalAmount="1500";//放款总金额
    String amount="1500";//资产包每期还款计划的钱
    String uniqueId1="b590fc00-5a7f-4c0c-8b5c-e38d0419b644";
    String uniqueId2="fb68d3f8-8ead-4aca-a10a-b14789121ee0";
    String uniqueId="";

    //@Before
    public void init(){
        //循环生成2个贷款合同
        for (int i=0;i<2;i++){
            if(i==1){
                uniqueId2 = UUID.randomUUID().toString();//合同编号
                uniqueId=uniqueId2;
            }
            else {
                uniqueId1= UUID.randomUUID().toString();//合同编号
                uniqueId=uniqueId1;
            }

            System.out.println("开始放款：");
            baseTestMethod.makeLoan(productCode, uniqueId,totalAmount,OuterInternat_command);
            while(true){
                int executionStatus = remittanceApplicationService.queryExecutionStatus(uniqueId);
                if(executionStatus == 2){
                    break;
                }
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //开始导入资产包,对应有三期还款计划
            System.out.println("开始导入资产包：");
            baseTestMethod.importAssetPackage(OuterInternat_modifyUrl,totalAmount,productCode,uniqueId,"0",amount,"2017-6-28","2017-7-28","2017-8-28");
        }
    }
/**
 * 测试场景--正常冒烟测试--是否能整个流程跑完
 * */
    @Test
    public void test1(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId1,0);
        System.out.println("第一个贷款合同的还款编号:"+repaymentNumber);
        String repaymentNumber1=baseTestMethod.queryFirstRepaymentPlan(uniqueId2,0);
        System.out.println("第二个贷款合同的还款编号:"+repaymentNumber1);
        System.out.println("还款订单接口：");
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId1,uniqueId2,totalAmount,repaymentNumber,repaymentNumber1,productCode);
    }
    /**
     * 测试场景--连续发两个相同的订单，能否保证唯一性，进行校验
     * */
    @Test
    public void test2(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId1,0);
        System.out.println("第一个贷款合同的还款编号:"+repaymentNumber);
        String repaymentNumber1=baseTestMethod.queryFirstRepaymentPlan(uniqueId2,0);
        System.out.println("第二个贷款合同的还款编号:"+repaymentNumber1);
        System.out.println("还款订单接口：");
        for (int i = 0; i < 2; i++) {
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId1,uniqueId2,totalAmount,repaymentNumber,repaymentNumber1,productCode);
        }
    }
    /**
     * 测试场景--测试订单创建前的校验是否成功
     * 1、 订单请求编号唯一；
     * 2、 商户订单号唯一；
     * 3、 信托产品代码是否存在；
     * 4、 订单总金额等于明细金额总和；
     * */
    @Test
    public void test3(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId1,0);
        System.out.println("第一个贷款合同的还款编号:"+repaymentNumber);
        String repaymentNumber1=baseTestMethod.queryFirstRepaymentPlan(uniqueId2,0);
        System.out.println("第二个贷款合同的还款编号:"+repaymentNumber1);
        System.out.println("还款订单接口：");
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId1,uniqueId2,totalAmount,repaymentNumber,repaymentNumber1,productCode);
    }
    /**
     * 测试场景--测试对处于“扣款中”、“作废”、“还款成功”、“已回购”、“违约”的还款计划能否生成还款订单
     * */
    @Test
    public void test4() {
        String repaymentNumber = baseTestMethod.queryFirstRepaymentPlan(uniqueId1, 0);
        System.out.println("第一个贷款合同的还款编号:" + repaymentNumber);
        System.out.println("对第一个贷款合同的还款计划进行扣款：");
//        baseTestMethod.deductRepaymentPlan(repaymentNumber, uniqueId1, productCode, totalAmount, "0");
        String repaymentNumber1 = baseTestMethod.queryFirstRepaymentPlan(uniqueId2, 0);
        System.out.println("第二个贷款合同的还款编号:" + repaymentNumber1);
        System.out.println("还款订单接口：");
        for (int i = 0; i < 2; i++) {
            baseTestMethod.repaymentOrder(RepayMentOrderUrl, uniqueId1, uniqueId2, totalAmount, repaymentNumber, repaymentNumber1, productCode);
        }
    }
    /**
     * 测试场景--一个还款订单包含同一个贷款合同的两个还款计划，能否成功
     */
    @Test
    public void test5(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId1,0);
        System.out.println("第一个贷款合同的还款编号:"+repaymentNumber);
        String repaymentNumber1=baseTestMethod.queryFirstRepaymentPlan(uniqueId1,1);
        System.out.println("第二个贷款合同的还款编号:"+repaymentNumber1);
        System.out.println("还款订单接口：");
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId1,uniqueId1,totalAmount,repaymentNumber,repaymentNumber1,productCode);
    }
    /**
     * 测试场景--一个还款计划订单包含同一个贷款合同的同一个还款计划
     * */
    @Test
    public void test6(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId1,0);
        System.out.println("第一个贷款合同的还款编号:"+repaymentNumber);
        System.out.println("还款订单接口：");
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId1,uniqueId1,totalAmount,repaymentNumber,repaymentNumber,productCode);
    }
    /**
     * 格式校验的测试--订单生成时间-设定还款时间<7自然日，若设定还款时间大于订单生成时间，能否通过校验
     * 假设订单生成日为2017-5-23,设置还款时间为2017-7-30 00:00:00
     * */
    @Test
    public void test7(){
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId1,0);
        System.out.println("第一个贷款合同的还款编号:"+repaymentNumber);
        String repaymentNumber1=baseTestMethod.queryFirstRepaymentPlan(uniqueId2,0);
        System.out.println("第二个贷款合同的还款编号:"+repaymentNumber1);
        System.out.println("还款订单接口：");
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId1,uniqueId2,totalAmount,repaymentNumber,repaymentNumber1,productCode);
    }
    /**
     * 测试场景--回购状态为“已回购”、“违约”的回购单，能否提交还款订单
     * 提示：需要自己寻找一个符合条件的回购单
     * */
    @Test
    public void test8(){
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId1,uniqueId2,totalAmount,"","",productCode);
    }
    /**
     *测试场景--一个还款计划订单能否包含贷款合同的提前还款计划
     * */
    @Test
    public void test9(){
        System.out.println("第一个贷款合同申请提前还款：");
        baseTestMethod.applyPrepaymentPlan(uniqueId1,"1500","1500","2017-5-13");
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId1,0);
        System.out.println("第一个贷款合同的还款编号:"+repaymentNumber);
        System.out.println("第二个贷款合同申请提前还款：");
        baseTestMethod.applyPrepaymentPlan(uniqueId2,"1500","1500","2017-5-13");
        String repaymentNumber1=baseTestMethod.queryFirstRepaymentPlan(uniqueId2,0);
        System.out.println("第二个贷款合同的还款编号:"+repaymentNumber1);
        System.out.println("还款订单接口：");
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId1,uniqueId2,totalAmount,repaymentNumber,repaymentNumber1,productCode);
    }
    /**
     * 测试场景--一个还款计划订单中能否包含两个贷款合同的已经锁定的还款计划
     */
    @Test
    public void test10(){
        System.out.println("第一个贷款合同申请提前还款：");
        baseTestMethod.applyPrepaymentPlan(uniqueId1,"1500","1500","2017-5-13");
        String repaymentNumber=baseTestMethod.queryFirstRepaymentPlan(uniqueId1,1);
        System.out.println("第一个贷款合同的还款编号:"+repaymentNumber);
        System.out.println("第二个贷款合同申请提前还款：");
        baseTestMethod.applyPrepaymentPlan(uniqueId2,"1500","1500","2017-5-13");
        String repaymentNumber1=baseTestMethod.queryFirstRepaymentPlan(uniqueId2,1);
        System.out.println("第二个贷款合同的还款编号:"+repaymentNumber1);
        System.out.println("还款订单接口：");
        baseTestMethod.repaymentOrder(RepayMentOrderUrl,uniqueId1,uniqueId2,totalAmount,repaymentNumber,repaymentNumber1,productCode);
    }
}
