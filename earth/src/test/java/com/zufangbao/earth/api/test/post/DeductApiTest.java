package com.zufangbao.earth.api.test.post;

import com.zufangbao.cucumber.method.BaseTestMethod;
import com.zufangbao.cucumber.method.commandUrl;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by dzz on 17-5-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"
})
/**
 * 扣款接口-测试覆盖
 * */
public class DeductApiTest extends commandUrl {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;

    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String productCode="G31700";//信托代码
    String totalAmount="1500";//放款总金额
    String amount="1500";//资产包每期还款计划的钱
    String uniqueId= UUID.randomUUID().toString();
    @Before
    public void init(){
        for (int i=0;i<1;i++){

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
     * 测试场景--能否完成提前划拨，正常扣款，逾期扣款
     * */
    @Test
    public void test1(){
        System.out.println("扣款接口：");
    }
    /**
     * 测试场景--
     * */
}
