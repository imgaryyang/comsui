package com.suidifu.jpmorgan.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.suidifu.jpmorgan.entity.AccountSide;
import com.suidifu.jpmorgan.entity.BusinessStatus;
import com.suidifu.jpmorgan.entity.CommunicationStatus;
import com.suidifu.jpmorgan.entity.GatewayType;
import com.suidifu.jpmorgan.entity.OccupyCommunicationStatus;
import com.suidifu.jpmorgan.entity.OccupyStatus;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.service.PaymentOrderService;

@Component
public class SenderOccupyRequestTask {

	@Autowired
	private PaymentOrderService paymentOrderService;

	private static final Log logger = LogFactory
			.getLog(SenderOccupyRequestTask.class);
	
//	@Scheduled(fixedDelay = 5)
	public void saveOccupyRequest() {
		logger.info("Begin InsertOccupyRequest....");
		
		//paymentOrderService.taskInQueue(senderOccupyRequest(), "payment_order");
		
		logger.info("End InsertOccupyRequest ! ! !");
	}

	private List<PaymentOrder> senderOccupyRequest() {

		List<PaymentOrder> list = new ArrayList<PaymentOrder>();
		for (int i = 0; i < 100000; i++) {
			
			PaymentOrder paymentOrder = new PaymentOrder();
			
			paymentOrder.setUuid(UUID.randomUUID().toString());
			paymentOrder.setAccessVersion(UUID.randomUUID().toString());
			paymentOrder.setAccountSide(AccountSide.CREDIT.ordinal());
			paymentOrder.setSourceAccountName("");
			paymentOrder.setSourceAccountNo("571907757810703");

			//paymentOrder.setSourceBankInfo("CB");
			paymentOrder.setDestinationAccountName("张建明");
			paymentOrder.setDestinationAccountNo("6226227705568300");
			//paymentOrder.setDestinationBankInfo("305100000012");
			paymentOrder.setTransactionAmount(new BigDecimal("0.01"));
			paymentOrder.setCurrencyCode("10");
			paymentOrder.setPostscript("超级网银测试");
			paymentOrder.setOutlierTransactionUuid(UUID.randomUUID().toString());
			paymentOrder.setGatewayType(GatewayType.SuperBank.ordinal());
			paymentOrder.setBusinessStatus(BusinessStatus.Processing.ordinal());
			

			
				paymentOrder.setCommunicationStatus(CommunicationStatus.Inqueue.ordinal());   
				paymentOrder.setFstOccupyStatus(OccupyStatus.Free.ordinal());
				paymentOrder.setFstCommunicationStatus(OccupyCommunicationStatus.Ready.ordinal());
				
				paymentOrder.setSndOccupyStatus(OccupyStatus.Free.ordinal());
				paymentOrder.setSndCommunicationStatus(OccupyCommunicationStatus.Ready.ordinal());
				
				paymentOrder.setTrdOccupyStatus(OccupyStatus.Free.ordinal());
				paymentOrder.setTrdCommunicationStatus(OccupyCommunicationStatus.Ready.ordinal());
	

				paymentOrder.setSourceMessageTime(new Date());
				

			list.add(paymentOrder);
			
			
			
		}
		return list;
	}
}
