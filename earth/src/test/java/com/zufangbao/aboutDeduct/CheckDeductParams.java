package com.zufangbao.aboutDeduct;


import com.zufangbao.cucumber.method.PrepaymentCucumberMethod;
import com.zufangbao.refactor.method.RefactorMethod;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.service.PrepaymentApplicationService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by wubo on 2018/4/9.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml" })
public class CheckDeductParams {

    @Autowired
	private RepaymentPlanService repaymentPlanService;

    @Autowired
		private PrepaymentApplicationService prepaymentApplicationService;

    @Autowired
	private DeductApplicationService deductApplicationService;

    ImportAssetMethod importAssetMethod = new ImportAssetMethod();
    DeductBaseMethod deductBaseMethod = new DeductBaseMethod();
    String uniqueId = "";
    String repaymentPlanNo = "";
    String contractNo="";
    PrepaymentCucumberMethod Prepayment=new PrepaymentCucumberMethod();

    @Before
    public void importAsset(){
        uniqueId = UUID.randomUUID().toString();

        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 0));
        String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
        String applyDate = DateUtils.format(DateUtils.addDays(DateUtils.parseDate(firstPlanDate), -1));    //

        importAssetMethod.importAssetPackage3("3000","WB123",uniqueId,"1000",firstPlanDate,secondPlanDate,thirdPlanDate); //
        repaymentPlanNo = new QueryRepaymentInfo().query_i_RepaymentPlan(uniqueId,0);  //
			System.out.println("repaymentPlanNo   "+repaymentPlanNo);
    }

    /**
     * 一笔还款计划成功
     */
    @Test
    public void testDeduct1() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "1000";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "1000";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "1000";
        String techFee = "0";
        String totalOverdueFee = "0";
        String re = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,uniqueId,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        System.out.println(repaymentPlanNo);
        if (!re.contains("成功!")) {
            throw new Exception("扣款参数校验失败，提交失败!");
        }
        List<DeductApplication> deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
        while (true){
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo); //
            if (null == assetSet){
                throw new Exception("还款计划不存在！");
            }

					  if (PaymentStatus.SUCCESS == assetSet.getPaymentStatus()){     //
							  break;
						}

            Thread.sleep(3000);
        }
    }

    /**
     * 部分扣款成功
     * @throws Exception
     */

    @Test
    public void testDeduct2() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "500";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "500";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "500";
        String techFee = "0";
        String totalOverdueFee = "0";
        String re = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,uniqueId,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        System.out.println(repaymentPlanNo);
        if (!re.contains("成功!")) {
            throw new Exception("扣款参数校验失败，提交失败!");
        }

        while (true){
            AssetSet assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
            if (null == assetSet){
                throw new Exception("还款计划不存在!");
            }

            if (OnAccountStatus.PART_WRITE_OFF == assetSet.getOnAccountStatus()){
                break;
            }
            Thread.sleep(1000);
        }
    }
    /**
     * 扣款订单参数校验--requestNo为空                     通过
     * @throws Exception
     */
    @Test
    public void testDeduct3() throws Exception{
    	String requestNo="";
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("请求唯一标识［requestNo］，不能为空！");
    	Assert.assertEquals(true,outcom);    	
  	  	
    }
    /**
     * 扣款订单参数校验--requestNo 重复                         通过
     * @throws Exception
     */
    @Test
    public void testDeduct4() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="500.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="500";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="500";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	if(!result.contains("成功")){
    		throw new RuntimeException("扣款订单参数校验失败");
    	} 
    	
    	while(true){
    		AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
    		if(null==assetSet){
    			throw new RuntimeException("还款计划不存在!");
    		}

    		if(OnAccountStatus.PART_WRITE_OFF==assetSet.getOnAccountStatus()){
    			break;
    		}
    		Thread.sleep(1000);
    	}
    	String result2=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
				, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
				otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
				techFee, totalOverdueFee);
    	boolean outcom=result2.contains("请求编号重复!");
    	Assert.assertEquals(true,outcom); 
    	
        
    }
    /**
     * 扣款订单参数校验--deductld 为空                    通过
     * @throws Exception
     */
    @Test
    public void testDeduct5() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId="";
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("扣款唯一编号不能为空！");
    	Assert.assertEquals(true,outcom);
    	  	    	       
    }
    
    /**
     * 扣款订单参数校验--financialProductCode 为空                 通过
     * @throws Exception
     */
    @Test
    public void testDeduct6() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("信托产品代码不能为空!");
    	Assert.assertEquals(true,outcom);
        
    }
    /**
     * 扣款订单参数校验--financialProductCode 错误     通过
     * @throws Exception
     */
    @Test
    public void testDeduct7() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123456";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    //	boolean outcom=result.contains("financialProductCode不存在");
    //	Assert.assertEquals(true,outcom);
			 if (!result.contains("成功")){
			 	throw new RuntimeException("扣款参数校验失败");
			 }
			 AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
			 List<DeductApplication> deductApplicationList;
			 while (true){
			 	deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
			 	if (DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
			 		break;
				}
			 }
			 System.out.println(deductApplicationList.get(0).getExecutionStatus());
			 System.out.println(assetSet.getExecutingStatus());
			 System.out.println(assetSet.getExecutingStatus());
			 Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());
			 Assert.assertEquals(ExecutingStatus.PROCESSING.PROCESSING,assetSet.getExecutingStatus());
			 Assert.assertEquals(DeductionStatus.NOT_DEDUCTING,assetSet.getDeductionStatus());


    }

    /**
     * 扣款订单参数校验--apiCalledTime 为空              通过
     * @throws Exception
     */
    @Test
    public void testDeduct8() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime="";
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("接口调用时间,时间格式有误！");
    	Assert.assertEquals(true,outcom);
    		    	
    	       
    }
    
    /**
     * 扣款订单参数校验--apiCalledTime 时间格式错误                通过
     * @throws Exception
     */
    @Test
    public void testDeduct9() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime="2018-04-20 10:00:00";
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);

			boolean outcom=result.contains("接口调用时间,时间格式有误！");
			Assert.assertEquals(true,outcom);
        
    }
    
    /**
     * 扣款订单参数校验-- uniqueId和contractNo二选一      contractNo为空      通过
     * @throws Exception
     */
    @Test
    public void testDeduct10() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	if (!result.contains("成功")){
    		throw new RuntimeException("扣款订单参数校验失败");
			}

			AssetSet assetSet;
			List<DeductApplication> deductApplicationList;

			while (true){
				assetSet=repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanNo);
				if (null==assetSet){
					throw new RuntimeException("还款订单不存在");
				}

				deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
				if (DeductApplicationExecutionStatus.SUCCESS==deductApplicationList.get(0).getExecutionStatus()&&
						DeductionStatus.SUCCESS==assetSet.getDeductionStatus()
						&&assetSet.getOnAccountStatus()==OnAccountStatus.WRITE_OFF){
					break;
				}
				Thread.sleep(2000);
			}
			Assert.assertEquals(PaymentStatus.SUCCESS,assetSet.getPaymentStatus());

    }
    /**
     * 扣款订单参数校验-- uniqueId和contractNo 都填 两者不一致 优先使用uniqueId uniqueid正确     通过
     * @throws Exception
     */
    @Test
    public void testDeduct11() throws Exception{
    	String contractNo=uniqueId+1;   //不一致
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	if(!result.contains("成功")){
    		throw new RuntimeException("扣款订单参数校验错误");
    	}
    	AssetSet assetSet;

			while (true){
				assetSet=repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanNo);
				if (null==assetSet){
					throw new RuntimeException("还款订单不存在");
				}
				List<DeductApplication> deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
    	  if (DeductApplicationExecutionStatus.SUCCESS==deductApplicationList.get(0).getExecutionStatus()
					&&DeductionStatus.SUCCESS==assetSet.getDeductionStatus()){
    	  	break;
				}
				Thread.sleep(2000);
			}
      Assert.assertEquals(PaymentStatus.SUCCESS,assetSet.getPaymentStatus());

    }
	/**
	 * 扣款订单参数校验-- uniqueId和contractNo 都填 两者不一致 优先使用uniqueId uniqueid错误     通过
	 * @throws Exception
	 */
	@Test
	public void testDeduct38() throws Exception{
		String contractNo=uniqueId;   //不一致
		String requestNo=UUID.randomUUID().toString();
		String deductId=UUID.randomUUID().toString();
		String financialProductCode="WB123";
		String apiCalledTime=DateUtils.format(new Date());
		String amount="1000.00";
		String repaymentType="1";
		String payAcNo="";
		String payerName="";
		String bankCode="";
		String mobile="";
		String idCardNum="";
		String loanFee="0.00";
		String otherFee="0.00";
		String repaymentAmount="1000.00";
		String repaymentInterest="0.00";
		String repayScheduleNo="";
		String repaymentPrincipal="1000.00";
		String techFee="0.00";
		String totalOverdueFee="0.00";
		String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId+1, contractNo, apiCalledTime
				, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
				otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal,
				techFee, totalOverdueFee);
		if(!result.contains("成功")){
			throw new RuntimeException("扣款订单参数校验错误");
		}
		AssetSet assetSet;
		List<DeductApplication> deductApplicationList;

		while (true){
			assetSet=repaymentPlanService.getRepaymentPlanByRepaymentPlanNo(repaymentPlanNo);
			if (null==assetSet){
				throw new RuntimeException("还款订单不存在");
			}

			deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
			System.out.println(deductApplicationList.get(0).getExecutionStatus());
			if (DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
				break;
			}
			Thread.sleep(2000);
		}
		Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());

	}
    /**
     * 扣款订单参数校验-- uniqueId和contractNo 都填 两者一致     通过
     * @throws Exception
     */
    @Test
    public void testDeduct12() throws Exception{
    	String contractNo=uniqueId;   //一致
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("成功");
    	Assert.assertEquals(true,outcom);
    }
    
    /**
     * 扣款订单参数校验-- uniqueId和contractNo 都不填            通过
     * @throws Exception
     */
    @Test
    public void testDeduct13() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, "", contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);

			boolean outcom=result.contains("请选填其中一种编号［uniqueId，contractNo］！");
    	Assert.assertEquals(true,outcom);
        
    }
    
    /**
     * 扣款订单参数校验-- amout 不填                      通过
     * @throws Exception
     */
    @Test
    public void testDeduct14() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="";   //扣款金额
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("扣款金额格式有误！");
    	Assert.assertEquals(true,outcom);
        
    }
    /**
     * 扣款订单参数校验-- amout 超出还款总金额                 通过
     * @throws Exception
     */
    @Test
    public void testDeduct15() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1100.00";   //扣款金额
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	if(!result.contains("成功")){
    		throw new RuntimeException("扣款参数校验失败，提交失败!");
    	}
    	AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
    	List<DeductApplication> deductApplicationList;
    	if (null==assetSet){
    		throw new RuntimeException("还款计划不存在");
			}

    	while(true){
    		deductApplicationList = deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
    		if(DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()) {
					break;
				}
				Thread.sleep(1000);
    	}
      Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());

    }
    /**
     * 扣款订单参数校验-- amout 小于还款总金额            通过
     * @throws Exception
     */
    @Test
    public void testDeduct16() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="500.00";   //扣款金额
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	if(!result.contains("成功")) {
				throw new RuntimeException("扣款参数校验错误,提交失败");

			}
    	AssetSet assetSet =repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
			if (null==assetSet){
				throw new RuntimeException("还款计划不存在");
			}
    	List<DeductApplication> deductApplicationList;
    	while (true){
    		deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
    		if (DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
    			break;
				}
				Thread.sleep(2000);

			}
			System.out.println(deductApplicationList.get(0).getExecutionStatus());
    	System.out.println(assetSet.getExecutingStatus());
    	System.out.println(assetSet.getDeductionStatus());
			Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());
    }
    /**
     * 扣款订单参数校验-- repaymentPlanNo和repayScheduleNo 二选一    repayScheduleNo为空      通过
     * @throws Exception
     */
    @Test
    public void testDeduct17() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("成功");
        Assert.assertEquals(true,outcom);
    }
    
    /**
     * 扣款订单参数校验-- repaymentPlanNo和repayScheduleNo  都填  repayScheduleNo存在错误      通过
     * @throws Exception
     */
    @Test
    public void testDeduct18() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="bsfgulsgs";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	if (!result.contains("成功")){
    		throw  new RuntimeException("扣款参数校验失败");
			}
			AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
    	List<DeductApplication> deductApplicationList;
    	if (null==assetSet){
    		throw new RuntimeException("还款计划不存在");
    		
			}
			while (true){
    		deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
    		if (DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
    			 break;
				}
				Thread.sleep(2000);
			}
			Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());

    }
    
    /**
     * 扣款订单参数校验-- repaymentPlanNo和repayScheduleNo  都不填          通过
     * @throws Exception
     */
    @Test
    public void testDeduct19() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, "", repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("商户还款计划编号、还款计划编号，二选一，必须填写其中一项");
    	Assert.assertEquals(true, outcom);
        
    }
    
    /**
     * 扣款订单参数校验-- repaymentAmount  小于计划还款本金            通过
     * @throws Exception
     */
    @Test
    public void testDeduct20() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="500.00";   //小于计划还款本金
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	if(!result.contains("成功")){
    		throw new RuntimeException("扣款参数校验失败，提交失败!");
    	}
    	AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
    	List<DeductApplication> deductApplicationList;
    	if (null==assetSet){
    		throw new RuntimeException("还款计划不存在");
			}
    	while(true){
    		deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
    		if (DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
    			break;
				}
				Thread.sleep(2000);
    	}
			Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());

        
    }
    /**
     * 扣款订单参数校验-- repaymentAmount 大于实际还款总金额    通过
     * @throws Exception
     */
    @Test
    public void testDeduct21() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1100.00";   //大于实际还款总金额
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	if(!result.contains("成功")){
    		throw new RuntimeException("扣款参数校验失败，提交失败!");
    	}
    	AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
    	List<DeductApplication> deductApplicationList;
    	if (null==assetSet){
    		throw new RuntimeException("还款计划不存在");
			}
    	while(true){
    		deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);

				if (DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
					break;
				}
    		Thread.sleep(2000);
    	}
			Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());

    }
    
    /**
     * 扣款订单参数校验-- repaymentAmount 不填           通过
     * @throws Exception
     */
    @Test
    public void testDeduct22() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="";   //不填
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    
    /**
     * 扣款订单参数校验-- repaymentPrincipal  为负数   通过
     * @throws Exception
     */
    @Test
    public void testDeduct23() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="-100.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom =result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    
    /**
     * 扣款订单参数校验-- repaymentPrincipal  不填    通过
     * @throws Exception
     */
    @Test
    public void testDeduct24() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    
    /**
     * 扣款订单参数校验-- repaymentinterest  为负数  通过
     * @throws Exception
     */
    @Test
    public void testDeduct25() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="-20.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    /**
     * 扣款订单参数校验-- repaymentinterest   不填   通过
     * @throws Exception
     */
    @Test
    public void testDeduct26() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    /**
     * 扣款订单参数校验-- loanFee  不填     通过
     * @throws Exception
     */
    @Test
    public void testDeduct27() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    /**
     * 扣款订单参数校验-- loanFee  为负数       通过
     * @throws Exception
     */
    @Test
    public void testDeduct28() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="-10";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
			boolean outcom=result.contains("还款计划明细金额不能为空！");
			Assert.assertEquals(true,outcom);
        
    }
    /**
     * 扣款订单参数校验-- techFee  不填     通过
     * @throws Exception
     */
    @Test
    public void testDeduct29() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    /**
     * 扣款订单参数校验-- techFee  为负数         通过
     * @throws Exception
     */
    @Test
    public void testDeduct30() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="-10.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom =result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    
    /**
     * 扣款订单参数校验-- otherFee  不填       通过
     * @throws Exception
     */
    @Test
    public void testDeduct31() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    /**
     * 扣款订单参数校验-- otherFee 为负数    通过
     * @throws Exception
     */
    @Test
    public void testDeduct32() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="-10.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划明细金额不能为空！");
    	Assert.assertEquals(true,outcom);
        
    }
    
    /**
     * 扣款订单参数校验-- repaymentType  提前还款    通过
     * @throws Exception
     */
    @Test
    public void testDeduct33() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="0";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
			if(!result.contains("成功")){
				throw new RuntimeException("扣款参数校验失败，提交失败!");
			}
			AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
			List<DeductApplication> deductApplicationList;
			if (null==assetSet){
				throw new RuntimeException("还款计划不存在");
			}
			while(true){
				deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);

				if (DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
					break;
				}
				Thread.sleep(2000);
			}
			Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());

        
    }

    /**
     * 扣款订单参数校验-- repaymentType  不填      没通过
     * @throws Exception
     */
    @Test
    public void testDeduct34() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("HTTP Status 400");
    	Assert.assertEquals(true,outcom);
        
    }
    
    //
    
    
    /**
     * 扣款订单参数校验-- totalOverdueFee  不填       通过
     * @throws Exception
     */
    @Test
    public void testDeduct35() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划逾期费用明细金额不合法");
    	Assert.assertEquals(true,outcom);
        
    }
    
    /**
     * 扣款订单参数校验-- totalOverdueFee  为负数    通过
     * @throws Exception
     */
    @Test
    public void testDeduct36() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="-100.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	boolean outcom=result.contains("还款计划逾期费用明细金额不合法");
    	Assert.assertEquals(true,outcom);
        
    }
    
    
    /**
     * 扣款订单参数校验-- totalOverdueFee   提前还款类型存在逾期费用    通过
     * @throws Exception
     */
    @Test
    public void testDeduct37() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="0";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1000.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="100";  //逾期费用
			String result=deductBaseMethod.deductRepaymentPlanNEW1(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
					, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
					otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal,
					techFee, totalOverdueFee);
			AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
			List<DeductApplication> deductApplicationList;
			if (null==assetSet){
				throw new RuntimeException("还款计划不存在");
			}
			while (true){
				deductApplicationList = deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
				if(DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
					break;
				}
				Thread.sleep(1000);
			}
			Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());
			Assert.assertEquals(ExecutingStatus.PROCESSING,assetSet.getExecutingStatus());
			Assert.assertEquals(DeductionStatus.NOT_DEDUCTING,assetSet.getDeductionStatus());

        
    }

    
    /**
     *   逾期还款                                     通过
     * @throws Exception
     */
    @Test
    public void testDeduct39() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1100.00";
    	String repaymentType="2";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="1100.00";
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="1000.00";
    	String techFee="0.00";
    	String totalOverdueFee="100";

    	String provinceCode="";
			String cityCode="";
			String gateway="";
			String penaltyFee="";
			String latePenalty="50";
			String lateFee="50";
			String lateOtherCost="";


			String result=deductBaseMethod.deductRepaymentPlanNEW2(requestNo,deductId,financialProductCode,
					uniqueId,contractNo, apiCalledTime, amount, repaymentType,
					payAcNo,  payerName, bankCode, mobile,  idCardNum, loanFee,
					otherFee,repaymentAmount, repaymentInterest, repayScheduleNo,
					repaymentPlanNo,  repaymentPrincipal,  techFee,  totalOverdueFee,
					provinceCode,  cityCode, gateway,  penaltyFee, latePenalty, lateFee,
					lateOtherCost);

			boolean outcom=result.contains("还款计划逾期费用明细金额不合法");
			Assert.assertEquals(true,outcom);

        
    }
    
    
    /**
     *   包含两个还款计划  扣款金额等于两个还款计划的还款总金额之和     通过
     * @throws Exception
     */
    @Test
    public void testDeduct40() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1000.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="500.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="500.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW3(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
			AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
			List<DeductApplication> deductApplicationList;
			if (null==assetSet){
				throw new RuntimeException("还款计划不存在");
			}
			while (true){
				deductApplicationList = deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
				System.out.println(deductApplicationList.get(0).getExecutionStatus());
				System.out.println(assetSet.getExecutingStatus());
				System.out.println(assetSet.getDeductionStatus());
				if(DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
					break;
				}
				Thread.sleep(1000);
			}
			Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());
			Assert.assertEquals(ExecutingStatus.PROCESSING,assetSet.getExecutingStatus());
			Assert.assertEquals(DeductionStatus.NOT_DEDUCTING,assetSet.getDeductionStatus());
        
    }
    
    
    /**
     *   包含两个还款计划  扣款金额小于两个还款计划的还款总金额之和
     * @throws Exception
     */
   /* @Test
    public void testDeduct41() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="800.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="500.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="500.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW3(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
    	if(result.contains("成功")){
    		throw new RuntimeException("扣款参数校验失败，提交失败!");
    	}
    	AssetSet assetSet;
    	while(true){
    		assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
    		if(null==assetSet){
    			throw new RuntimeException("还款计划不存在!");
    		}
    		if(PaymentStatus.SUCCESS!=assetSet.getPaymentStatus()){
    			break;
    		}
    		Thread.sleep(2000);
    		
    	}
    	Assert.assertEquals(PaymentStatus.UNUSUAL,assetSet.getPaymentStatus());
    }*/
    
    /**
     *   包含两个还款计划  扣款金额大于两个还款计划的还款总金额之和    通过
     * @throws Exception
     */
    @Test
    public void testDeduct42() throws Exception{
    	String requestNo=UUID.randomUUID().toString();
    	String deductId=UUID.randomUUID().toString();
    	String financialProductCode="WB123";
    	String apiCalledTime=DateUtils.format(new Date());
    	String amount="1100.00";  
    	String repaymentType="1";
    	String payAcNo="";
    	String payerName="";
    	String bankCode="";
    	String mobile="";
    	String idCardNum="";
    	String loanFee="0.00";
    	String otherFee="0.00";
    	String repaymentAmount="500.00";   
    	String repaymentInterest="0.00";
    	String repayScheduleNo="";
    	String repaymentPrincipal="500.00";
    	String techFee="0.00";
    	String totalOverdueFee="0.00";
    	String result=deductBaseMethod.deductRepaymentPlanNEW3(requestNo, deductId, financialProductCode, uniqueId, contractNo, apiCalledTime
    													, amount, repaymentType, payAcNo, payerName, bankCode, mobile, idCardNum, loanFee,
    													otherFee, repaymentAmount, repaymentInterest, repayScheduleNo, repaymentPlanNo, repaymentPrincipal, 
    													techFee, totalOverdueFee);
			AssetSet assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
			List<DeductApplication> deductApplicationList;
			if (null==assetSet){
				throw new RuntimeException("还款计划不存在");
			}
			while (true){
				deductApplicationList = deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
				if(DeductApplicationExecutionStatus.PROCESSING!=deductApplicationList.get(0).getExecutionStatus()){
					break;
				}
				Thread.sleep(1000);
			}
			Assert.assertEquals(DeductApplicationExecutionStatus.FAIL,deductApplicationList.get(0).getExecutionStatus());
			Assert.assertEquals(ExecutingStatus.PROCESSING,assetSet.getExecutingStatus());
			Assert.assertEquals(DeductionStatus.NOT_DEDUCTING,assetSet.getDeductionStatus());
        
    }
    
    /**
     * 全额扣款完成-再次扣款           通过
     */
    @Test
    public void testDeduct43() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "1000";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "1000";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "1000";
        String techFee = "0";
        String totalOverdueFee = "0";
        String re = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        System.out.println(repaymentPlanNo);
        if (!re.contains("成功!")) {
            throw new Exception("扣款参数校验失败，提交失败!");
        }

			  AssetSet assetSet;
        while (true){
					assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo); //

					if (null == assetSet){
						throw new RuntimeException("还款计划不存在！");
					}
            if (PaymentStatus.SUCCESS == assetSet.getPaymentStatus()&&DeductionStatus.SUCCESS==assetSet.getDeductionStatus()){     //
               break;
            }

            Thread.sleep(4000);
        }
        System.out.println("#######################");
        //再次扣款
        requestNo = UUID.randomUUID().toString();
        deductId = UUID.randomUUID().toString();
        String result = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        if (!result.contains("成功!")) {
            throw new Exception("扣款参数校验失败，提交失败!");
        }
        List<DeductApplication> deductApplicationList;

        while(true){

					assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
					deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
					System.out.println(repaymentPlanNo);

					if (DeductApplicationExecutionStatus.FAIL==deductApplicationList.get(0).getExecutionStatus()){
						break;
					}

        	Thread.sleep(4000);
        }



        
    }
    
    /**
     * 扣款中-再次扣款    第一次扣款请求成功第二次扣款请求失败       通过
     */
    @Test
    public void testDeduct44() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "1000";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "1000";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "1000";
        String techFee = "0";
        String totalOverdueFee = "0";
        String re = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        System.out.println(repaymentPlanNo);
        if (!re.contains("成功!")) {
            throw new Exception("扣款参数校验失败，提交失败!");
        }
        List<DeductApplication> deductApplicationList;
			  AssetSet assetSet;
        while (true){
            assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo); //
            if (null == assetSet){
                throw new RuntimeException("还款计划不存在！");
            }

            if (PaymentStatus.PROCESSING == assetSet.getPaymentStatus()&&ExecutingStatus.PROCESSING==assetSet.getExecutingStatus()){     //
                break;
            }

            Thread.sleep(3000);
        }
        //再次扣款
        requestNo = UUID.randomUUID().toString();
        deductId = UUID.randomUUID().toString();
        String result = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        if (!re.contains("成功!")) {
            throw new Exception("扣款参数校验失败，提交失败!");
        }
        while(true){
					assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
					deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
        	if (PaymentStatus.SUCCESS==assetSet.getPaymentStatus()&&DeductionStatus.SUCCESS==assetSet.getDeductionStatus()) {
						break ;
					}
        	Thread.sleep(3000);
        }
        Assert.assertEquals(DeductApplicationExecutionStatus.FAIL, deductApplicationList.get(0).getExecutionStatus());
			  Assert.assertEquals(DeductApplicationExecutionStatus.SUCCESS, deductApplicationList.get(1).getExecutionStatus());
    }
    /**
     * 部分扣款完成后再次部分扣款            通过
     */
    @Test
    public void testDeduct45() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "500";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "500";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "500";
        String techFee = "0";
        String totalOverdueFee = "0";
        String re = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        System.out.println(repaymentPlanNo);
        if (!re.contains("成功!")) {
            throw new Exception("扣款参数校验失败，提交失败!");
        }
			  AssetSet assetSet;
        List<DeductApplication> deductApplicationList;
        while (true){
					assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo); //
					deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);

            if (null == assetSet){
                throw new RuntimeException("还款计划不存在！");
            }

           if (DeductionStatus.SUCCESS==assetSet.getDeductionStatus()&&deductApplicationList.get(0).getExecutionStatus()==DeductApplicationExecutionStatus.SUCCESS){
           	break;
					 }

            Thread.sleep(1000);
        }
        //再次扣款
        requestNo = UUID.randomUUID().toString();
        deductId = UUID.randomUUID().toString();
        String result = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        if (!re.contains("成功!")) {
            throw new Exception("扣款参数校验失败，提交失败!");
        }

        while (true){
					assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
					deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);

					if (DeductionStatus.SUCCESS == assetSet.getDeductionStatus()&&PaymentStatus.SUCCESS==assetSet.getPaymentStatus()){     //已核销
                break;
            }

            Thread.sleep(1000);
        }
		   	Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
			  Assert.assertEquals(OnAccountStatus.WRITE_OFF,assetSet.getOnAccountStatus());
    }
    
    /**
     * 提前还款的还款计划扣款   前提：三笔还款计划各1000，提前成一期3000   扣款1000   通过
     */
    @Test
    public void testDeduct46() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "1000";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "1000";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "1000";
        String techFee = "0";
        String totalOverdueFee = "0";

        String applyDate = DateUtils.format(new Date());    //

			  //申请提前还款
        Prepayment.applyPrepaymentPlan(uniqueId, "3000", "3000", applyDate);
			  //生成还款计划编号
			  repaymentPlanNo = prepaymentApplicationService.getPrepaymentNumber(this.uniqueId);
        
        String result = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        if(!result.contains("成功")){
        	throw new RuntimeException("扣款参数校验失败");
        }
			  AssetSet assetSet ;

			  List<DeductApplication> deductApplicationList;
        while(true) {
					assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
					if(null==assetSet){
						throw new RuntimeException("还款计划不存在");
					}

					deductApplicationList = deductApplicationService
							.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);


					if (DeductionStatus.SUCCESS==assetSet.getDeductionStatus()
							&&deductApplicationList.get(0).getExecutionStatus()==DeductApplicationExecutionStatus.SUCCESS){
						break;
					}
					Thread.sleep(3000);
				}


        Assert.assertEquals(OnAccountStatus.PART_WRITE_OFF, assetSet.getOnAccountStatus());
			  Assert.assertEquals(PaymentStatus.PROCESSING, assetSet.getPaymentStatus());
			  Assert.assertEquals(ExecutingStatus.PROCESSING, assetSet.getExecutingStatus());
    }
    
    
    
    /**
     * 对被提前还款的还款计划扣款3000          通过
     */
    @Test
    public void testDeduct47() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "3000";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "3000";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "3000";
        String techFee = "0";
        String totalOverdueFee = "0";

        String applyDate = DateUtils.format(new Date());    //

       //申请提前还款
        Prepayment.applyPrepaymentPlan(uniqueId, "3000", "3000", applyDate);

        repaymentPlanNo = prepaymentApplicationService.getPrepaymentNumber(uniqueId);

        String result = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        if(!result.contains("成功")){
        	throw new RuntimeException("扣款参数校验失败");
        }

			  AssetSet assetSet ;
        List<DeductApplication> deductApplicationList;
        while(true){
        	assetSet=repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
        	deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
        	if (null==assetSet){
        		 throw new RuntimeException("还款计划不存在");
					}

        	if (DeductionStatus.SUCCESS==assetSet.getDeductionStatus()
							&&DeductApplicationExecutionStatus.SUCCESS==deductApplicationList.get(0).getExecutionStatus()){
        		break;
					}
        	Thread.sleep(2000);
        }
        System.out.println(assetSet.getDeductionStatus());

        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
			  Assert.assertEquals(OnAccountStatus.WRITE_OFF,assetSet.getOnAccountStatus());
    }
    
    /**
     * 变更还款计划的还款计划扣款  前提：三期还款计划各1000，变更后第一期金额1000第二期2000  对第一期还款计划扣款1000   通过
     */
    @Test
    public void testDeduct48() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "1000";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "1000";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "1000";
        String techFee = "0";
        String totalOverdueFee = "0";


        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 0));
        String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));    //
			  System.out.println(firstPlanDate);
			  System.out.println(secondPlanDate);
        RefactorMethod refactorMethod=new RefactorMethod();
       //变更还款计划
        refactorMethod.modifyRepaymentPlanForTwo(uniqueId, "1000", "2000", "0", firstPlanDate, secondPlanDate, null, null, "9", UUID.randomUUID().toString(),UUID.randomUUID().toString());
        
        repaymentPlanNo = refactorMethod.queryRepaymentPlan(financialProductCode,uniqueId,0);


        String result = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        if(!result.contains("成功")){
        	throw new RuntimeException("扣款参数校验失败");
        }
			  AssetSet assetSet ;
        List<DeductApplication> deductApplicationList;
        while(true){
					assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
					deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
					if(null==assetSet){
						throw new RuntimeException("还款计划不存在");
					}
					System.out.println("***************************");
					System.out.println("PaymentStatus   " + assetSet.getPaymentStatus());
					System.out.println("OnAccountStatus   " + assetSet.getOnAccountStatus());
					System.out.println("ExecutingStatus   " + assetSet.getExecutingStatus());
					System.out.println("DeductionStatus   " + assetSet.getDeductionStatus());

					System.out.println("deductApplicationExecutionStatus0   "+deductApplicationList.get(0).getExecutionStatus());

					if(DeductionStatus.SUCCESS==assetSet.getDeductionStatus()
							&&deductApplicationList.get(0).getExecutionStatus()==DeductApplicationExecutionStatus.SUCCESS
							&&OnAccountStatus.WRITE_OFF==assetSet.getOnAccountStatus()){
        		break ;
        	}
        	Thread.sleep(4000);
        }

        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());

    }

    
    /**
     * 变更还款计划的还款计划扣款  对提前后生成的还款计划扣款3000     通过
     */
    @Test
    public void testDeduct49() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "3000";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "3000";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "3000";
        String techFee = "0";
        String totalOverdueFee = "0";

        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 0));

        RefactorMethod refactorMethod=new RefactorMethod();
       //变更还款计划
        refactorMethod.modifyRepaymentPlanForOne(uniqueId, "3000", "0", firstPlanDate,  null, "9", "", UUID.randomUUID().toString());
        
        repaymentPlanNo =refactorMethod.queryRepaymentPlan(financialProductCode,uniqueId,0); ;


        String result = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);

        if(!result.contains("成功")){
        	throw new RuntimeException("扣款参数校验失败");
        }
        List<DeductApplication> deductApplicationList;
			  AssetSet assetSet ;
        while(true){
					assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
					if(null==assetSet){
						throw new RuntimeException("还款计划不存在");
					}
					deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
					if (DeductApplicationExecutionStatus.SUCCESS==deductApplicationList.get(0).getExecutionStatus()&&
							DeductionStatus.SUCCESS==assetSet.getDeductionStatus()
							&&assetSet.getOnAccountStatus()==OnAccountStatus.WRITE_OFF){
						break;
					}
        	Thread.sleep(2000);
        }
        Assert.assertEquals(PaymentStatus.SUCCESS, assetSet.getPaymentStatus());
    }
    
    /**
     * 对回购中的还款计划扣款   对三期的还款计划的贷款合同进行回购，再查询还款计划，再对锁定中的还款计划进行扣款  通过
     */
    @Test
    public void testDeduct50() throws Exception{
        String requestNo = UUID.randomUUID().toString();
        String deductId = UUID.randomUUID().toString();
        String financialProductCode = "WB123";
        String apiCalledTime = DateUtils.format(new Date());
        String amount = "1000";
        String repaymentType = "1";
        String notifyUrl= "";
        String payAcNo = "";
        String payerName = "";
        String bankCode = "";
        String mobile = "";
        String idCardNum = "";
        String loanFee = "0";
        String otherFee = "0";
        String repaymentAmount = "1000";
        String repaymentInterest = "0";
        String repayScheduleNo = "";
        String repaymentPrincipal = "1000";
        String techFee = "0";
        String totalOverdueFee = "0";

        RefactorMethod refactorMethod=new RefactorMethod();
       //回购
        refactorMethod.applyRepurchase(financialProductCode, uniqueId, "3000", "0", "3000");

        String result = deductBaseMethod.deductRepaymentPlanNEW(requestNo,deductId,financialProductCode,uniqueId,contractNo,apiCalledTime,
                amount,repaymentType,notifyUrl,payAcNo,payerName,bankCode,mobile,idCardNum,loanFee,otherFee,repaymentAmount,
                repaymentInterest,repayScheduleNo,repaymentPlanNo,repaymentPrincipal,techFee,totalOverdueFee);
        if(!result.contains("成功")){
        	throw new RuntimeException("扣款参数校验失败");
        }
			  AssetSet assetSet;
        List<DeductApplication> deductApplicationList;
        while(true){
					assetSet = repaymentPlanService.getRepaymentPlanByRepaymentCode(repaymentPlanNo);
					if(null==assetSet){
						throw new RuntimeException("还款计划不存在");
					}
					deductApplicationList=deductApplicationService.getDeductApplicationByRepaymentPlanNo(repaymentPlanNo);
					System.out.println("deductAppliction  :"+deductApplicationList.get(0).getExecutionStatus());

        	if(DeductApplicationExecutionStatus.FAIL==deductApplicationList.get(0).getExecutionStatus()){
        		break ;
        	}
        	Thread.sleep(2000);
        }
    }


}