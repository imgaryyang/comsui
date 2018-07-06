package com.suidifu.bridgewater.handler.test.scripts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.sun.api.model.deduct.RepaymentDetail;
import com.zufangbao.sun.api.model.deduct.RepaymentType;

/**
 * Created by zsh2014 on 17-5-14.
 */
public class ZhonghangDeductServices  implements CustomizeServices {
    public static final String EMPTY = " ";

    public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {

    	try{
        String fn = (String)preRequest.getOrDefault("fn", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String requestNo = (String)preRequest.getOrDefault("requestNo", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String deductId = (String)preRequest.getOrDefault("deductId", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String financialProductCode = (String)preRequest.getOrDefault("financialProductCode", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String apiCalledTime = (String)preRequest.getOrDefault("apiCalledTime", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String uniqueId = (String)preRequest.getOrDefault("uniqueId", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String contractNo = (String)preRequest.getOrDefault("contractNo", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String transType = (String)preRequest.getOrDefault("transType", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String amount = (String)preRequest.getOrDefault("amount", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String accountName = (String)preRequest.getOrDefault("accountName", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String accountNo = (String)preRequest.getOrDefault("accountNo", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String notifyUrl = (String)preRequest.getOrDefault("notifyUrl", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String repaymentType=(String)preRequest.getOrDefault("repaymentType", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String mobile = (String)preRequest.getOrDefault("mobile", com.zufangbao.sun.utils.StringUtils.EMPTY);

        String batchDeductId = (String)preRequest.getOrDefault("batchDeductId", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String batchDeductApplicationUuid = (String)preRequest.getOrDefault("batchDeductApplicationUuid", com.zufangbao.sun.utils.StringUtils.EMPTY);
        String repaymentDetails = (String)preRequest.getOrDefault("repaymentDetails", com.zufangbao.sun.utils.StringUtils.EMPTY);
        
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(fn)){
            postRequest.put("errorMsg","功能代码不能为空");
            return false;
        }
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(requestNo)){
            postRequest.put("errorMsg","请求编号不能为空");
            return false;
        }
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(deductId)){
            postRequest.put("errorMsg","扣款编号不能为空");
            return false;
        }
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(financialProductCode)){
            postRequest.put("errorMsg","产品代码不能为空");
            return false;
        }
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(apiCalledTime)){
            postRequest.put("errorMsg","接口调用时间不能为空");
            return false;
        }
        
        Date parseDate = com.zufangbao.sun.utils.DateUtils.parseDate(apiCalledTime, "yyyy-MM-dd");
        if(null == parseDate) {
             postRequest.put("errorMsg","接口调用时间格式错误");
             return false;
        }
        
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(uniqueId) || com.zufangbao.sun.utils.StringUtils.isEmpty(contractNo)){
            postRequest.put("errorMsg","贷款合同唯一编号和贷款合同编号不能为空");
            return false;
        }
        
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(amount)){
            postRequest.put("errorMsg","扣款金额不能为空");
            return false;
        }
        
        BigDecimal deductAmount = null;
        try {
            deductAmount = new BigDecimal(amount);
            if(deductAmount.compareTo(BigDecimal.ZERO) <= 0) {
                postRequest.put("errorMsg","扣款金额需大于0");
                return false;
            }
            if(deductAmount.scale() > 2) {
                postRequest.put("errorMsg","扣款金额小数点后保留2位");
                return false;
            }
        } catch (Exception e) {
            postRequest.put("errorMsg","扣款金额格式错误");
            return false;
        }
        
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(accountName)){
            postRequest.put("errorMsg","账户名不能为空");
            return false;
        }
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(accountNo)){
            postRequest.put("errorMsg","账户号不能为空");
            return false;
        }
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(notifyUrl)){
            postRequest.put("errorMsg","回调地址不能为空");
            return false;
        }
            
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(repaymentType)){
            
            postRequest.put("errorMsg","还款计划类型不能为空");
            return false;
        }
        
        List<Integer> repaymentTypeList = new ArrayList<>();
        
        repaymentTypeList.add(RepaymentType.ADVANCE.ordinal());
        repaymentTypeList.add(RepaymentType.NORMAL.ordinal());
        repaymentTypeList.add(RepaymentType.OVERDUE.ordinal());
        
        try {
        	
        		Integer repaymentTypeInt = new Integer(repaymentType);
        		
        		if(!repaymentTypeList.contains(repaymentTypeInt)) {
        			   postRequest.put("errorMsg","还款计划类型值不符合预期，期待值是0，1，2，实际上是:"+repaymentType);
        	           return false;
        		}
        	
        }catch (Exception e) {
        	   postRequest.put("errorMsg","还款计划类型格式错误");
           return false;
		}
       
        if(com.zufangbao.sun.utils.StringUtils.isEmpty(repaymentDetails)) {
        	   postRequest.put("errorMsg","还款计划不能为空");
               return false;
        }
        
        List<RepaymentDetail> repaymentDetailList = ( List<RepaymentDetail> )com.demo2do.core.utils.JsonUtils.parseArray(repaymentDetails, RepaymentDetail.class);
        
        if(repaymentDetailList.size() !=1) {
     	  
        		postRequest.put("errorMsg","目前还款计划只能为1个");
            return false;
        }
        
        RepaymentDetail repaymentDetail = (RepaymentDetail)repaymentDetailList.get(0);
        
        if(repaymentDetail.getCurrentPeriod() <0) {
        		
        		postRequest.put("errorMsg","还款计划金额期数不能<0");
        	
	        return false;
        }
        
        if(9999!=repaymentDetail.getCurrentPeriod()){
            
        if(null == repaymentDetail.getTotalAmount() || (BigDecimal.ZERO.compareTo(repaymentDetail.getTotalAmount()) == 0)) {
        		
         	postRequest.put("errorMsg","还款计划金额总金额不能为空或为0");
        	
	        return false;
        }
        
        if(new BigDecimal(amount).compareTo(repaymentDetail.getTotalAmount()) != 0) {
        	
	        	postRequest.put("errorMsg","扣款金额和还款计划金额不一致");
	        	
	        return false;
        }
        }
        if(StringUtils.isBlank(transType)) {
        		postRequest.put("errorMsg","transType不能为空");
        	
	        return false;
        }
        
        
        
        List<Integer> transTypeList = new ArrayList<>();
        
        transTypeList.add(0);
        transTypeList.add(1);
        
        try {
        	
        	   Integer transTypeInt = new Integer(transType);
        		
        	   if(!transTypeList.contains(transTypeInt)) {
        		   
        			postRequest.put("errorMsg","transType期待值为0，1，实际上为"+transType);
                	
        	        return false;
        	   }
        	
        }catch (Exception e) {
        	
			e.printStackTrace();
			
			postRequest.put("errorMsg","transType格式解析错误");
        	
	        return false;
		}
        
        postRequest.put("requestNo",requestNo);
        postRequest.put("apiCalledTime", apiCalledTime);
        postRequest.put("deductId",deductId);
        postRequest.put("transType",transType);
        postRequest.put("financialProductCode",financialProductCode);
        postRequest.put("accountHolderName",accountName);
        postRequest.put("deductAccountNo",accountNo);
        postRequest.put("deductAmount",amount);
        postRequest.put("mobile",mobile);
        postRequest.put("notifyUrl", notifyUrl);
        postRequest.put("batchDeductId", batchDeductId);
        postRequest.put("batchDeductApplicationUuid", batchDeductApplicationUuid);
        postRequest.put("repaymentDetails", repaymentDetails);
        
        postRequest.put("contractNo", (String)preRequest.getOrDefault("contractNo", com.zufangbao.sun.utils.StringUtils.EMPTY));
        
        postRequest.put("uniqueId", (String)preRequest.getOrDefault("uniqueId", com.zufangbao.sun.utils.StringUtils.EMPTY));
        postRequest.put("repaymentType", repaymentType);
        
        
        return true;
        
    	} catch (Exception e) {
        	
    		 e.printStackTrace();
    		
    		 postRequest.put("errorMsg","system error["+e.getMessage()+"]");
             
    		 return false;
        	
        }
    }


    public Date contactDateAndTime(String orderDate,String orderTime){
        StringBuffer stringBuffer = new StringBuffer(orderDate);
        return com.zufangbao.gluon.opensdk.DateUtils.parseDate(stringBuffer.append(EMPTY).append(orderTime).toString(),"yyyyMMdd HHmmss");
    }
}
