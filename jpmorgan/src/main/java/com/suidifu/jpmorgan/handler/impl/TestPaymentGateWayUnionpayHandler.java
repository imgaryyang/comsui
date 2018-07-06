package com.suidifu.jpmorgan.handler.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.entity.Result;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.coffer.entity.BusinessProcessStatus;
import com.suidifu.coffer.entity.BusinessRequestStatus;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;

@Component("TestPaymentGateWayUnionpayHandler")
public class TestPaymentGateWayUnionpayHandler implements PaymentHandler {

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	private long i = 0;
	
	private static Log logger = LogFactory.getLog(TestPaymentGateWayUnionpayHandler.class);
	
	
	@Override
	public Result executeBatchPay(List<PaymentOrder> paymentOrderList,WorkingContext context) {
	
		return null;
	}

	@Override
	public CreditResult executeSinglePay(PaymentOrder paymentOrder,WorkingContext context) {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("workerUUid", context.getWorkerUuid());
		parms.put("orderUUid", paymentOrder.getUuid());
		parms.put("currentTime", new Date());
		parms.put("slot", context.getWorkingSlot());
		String sql = "INSERT INTO payment_order_test(`worker_uuid`, `order_uuid`, `slot`,`occupy_time`) values (:workerUUid, :orderUUid, :slot, :currentTime)";
		// insert workerUUid orerUUid
		genericDaoSupport.executeSQL(sql, parms);
		return null;
	}
	
	@Override
	public Result isTerminated(int remains) throws Exception {
		if(remains == 3) {
			throw new Exception();
		}
		return null;
	}
	
	
	@Override
	public QueryCreditResult executeQueryPaymentStatus(PaymentOrder paymentOrder,WorkingContext context) {
		

		i++;
		Result result = new Result();
//		boolean sign = RandomUtils.nextBoolean();
		if(i % 47 == 0) {
			QueryCreditResult queryCreditResult = new QueryCreditResult();
			queryCreditResult.setRequestStatus(BusinessRequestStatus.FINISH);
			queryCreditResult.setProcessStatus(BusinessProcessStatus.FAIL);
			return queryCreditResult;
			
		}
		else {
			QueryCreditResult queryCreditResult = new QueryCreditResult();
			queryCreditResult.setRequestStatus(BusinessRequestStatus.FINISH);
			queryCreditResult.setProcessStatus(BusinessProcessStatus.SUCCESS);
			return queryCreditResult;
		}
		
	}
	/*

	@Override
	public Result executeQueryPaymentStatus(PaymentOrder paymentOrder,WorkingContext context) {
		   
			Result result = new Result();
			return result.success();

	}	
	
/*
	@Override
	public Result executeQueryPaymentStatus(PaymentOrder paymentOrder,WorkerContext context) {
		   
			Result result = new Result();
			return result.fail();

	}	
	
	*/

	@Override
	public QueryCreditResult handleCallback(Map<String, String> callbackParms) {
		// TODO Auto-generated method stub
		return null;
	}
}
