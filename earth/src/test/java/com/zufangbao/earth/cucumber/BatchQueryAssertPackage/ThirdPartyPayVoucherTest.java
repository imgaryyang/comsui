package com.zufangbao.earth.cucumber.BatchQueryAssertPackage;

import com.zufangbao.cucumber.method.BaseTestMethod;
import com.zufangbao.cucumber.method.commandUrl;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.service.ThirdVoucherService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by dzz on 17-4-27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
"classpath:/local/applicationContext-*.xml"
})
public class ThirdPartyPayVoucherTest extends commandUrl {
    @Autowired
    private IRemittanceApplicationService remittanceApplicationService;
    @Autowired
    private ThirdVoucherService thirdVoucherService;
    String productCode="G31700";
    String totalamount="1500";
    String amount="500";

    BaseTestMethod baseTestMethod=new BaseTestMethod();

    @Test
    public void test(){
        List<Thread> threads = new ArrayList<Thread>();
        for(int i=0;i<1;i++){
            Thread thread = new Thread(new ThirdPartPayVoucherThread(i));
            threads.add(thread);
        }
        threads.forEach(f ->{f.start();});
        threads.forEach(f -> {
            try {
                f.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


    }

    class ThirdPartPayVoucherThread implements Runnable{
        private int i;
        private ThirdPartPayVoucherThread(int i){
            this.i = i;
        }
        @Override
        public void run() {
            String uniqueId= UUID.randomUUID().toString();//合同编号
            String transactionRequestNo=uniqueId+1;//交易请求号
            System.out.println("开始放款"+i);
            baseTestMethod.makeLoan(productCode, uniqueId,totalamount,OuterInternat_command);
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
            System.out.println("开始导入资产包");
            baseTestMethod.importAssetPackage(OuterInternat_modifyUrl,totalamount,productCode,uniqueId,"0",amount, DateUtils.format(new Date(),"yyyy-MM-dd"),DateUtils.format(DateUtils.addDays(new Date(),1),"yyyy-MM-dd"),DateUtils.format(DateUtils.addDays(new Date(),2),"yyyy-MM-dd"));

//            System.out.println("开始查询还款计划");
//            String RepaymentPlanNo=baseTestMethod.queryFirstRepaymentPlan(uniqueId,0);
//
//            System.out.println("开始制造第三方凭证");
//            baseTestMethod.MakeVOucher(NewOuterInternat_command,transactionRequestNo,uniqueId,RepaymentPlanNo,totalamount,productCode);



        }
    }
}
