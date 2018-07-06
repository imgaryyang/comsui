package com.zufangbao.earth.cucumber.LoanCallBack;

import com.zufangbao.cucumber.method.BaseTestMethod;
import com.zufangbao.cucumber.method.commandUrl;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

/**
 * Created by dzz on 17-3-31.
 */
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class loanCallBackSteps extends commandUrl {

    BaseTestMethod baseTestMethod=new BaseTestMethod();
    String uniqueId= UUID.randomUUID().toString();//合同编号

    @Given("^有一个放款计划,云信使用接口进行放款$")
    public void 有一个放款计划_shi_yong_zi_ji_jie_kou_fang_kuan() throws Throwable {
        //开始copy yunxin 放款
        System.out.println("开始copy yunxin 放款");
        String url="http://192.168.1.155:9090/api/command";
        String notifyUrl="http://192.168.1.155:8888/testUrl";
        baseTestMethod.LoanCallBack(url,uniqueId,"1500",notifyUrl);
    }

    @When("^jiang fang kuan xin xi tong guo yun xin jie kou chuan di$")
    public void jiang_fang_kuan_xin_xi_tong_guo_yun_xin_jie_kou_chuan_di() throws Throwable {
    }

    @Then("^返回记录$")
    public void 返回记录() throws Throwable {
    }

}
