package com.suidifu.jpmorgan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.jpmorgan.entity.AccountSide;
import com.suidifu.jpmorgan.entity.BusinessStatus;
import com.suidifu.jpmorgan.entity.CommunicationStatus;
import com.suidifu.jpmorgan.entity.GatewayType;
import com.suidifu.jpmorgan.entity.OccupyCommunicationStatus;
import com.suidifu.jpmorgan.entity.OccupyStatus;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.spec.PaymentTaskSpec;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml", })
public class PaymentOrderServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private PaymentOrderService paymentOrderService;
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	 
	
	
	@Test
	@Sql("classpath:test/testNativeSql.sql")
	public void testNativeSql() {
		List<PaymentOrder> queryForList = genericDaoSupport.queryForList("select * from payment_order", PaymentOrder.class);
		Assert.assertEquals(1, queryForList.size());
		Assert.assertEquals(queryForList.get(0).getBusinessStatus(), BusinessStatus.Inqueue);
		Assert.assertNull(queryForList.get(0).getAccountSide());
	}
	
	@Test
	@Sql("classpath:test/testTaskInQueue.sql")
	public void testTaskInQueue() {
		
		List<PaymentOrder> taskList = new ArrayList<PaymentOrder>();

		PaymentOrder paymentOrder1 = new PaymentOrder("ee85778a-d0a4-4e1d-af20-985c112eb064", AccountSide.CREDIT, "", "571907757810703", "CB", "张建明", "6226227705568339", "305100000013", new BigDecimal("0.01"), "10", "超级网银测试1", "997914de-a946-49b4-9b80-0246e5192de7", GatewayType.SuperBank, BusinessStatus.Processing, CommunicationStatus.Inqueue, OccupyStatus.Free, OccupyCommunicationStatus.Ready, OccupyStatus.Free, OccupyCommunicationStatus.Ready, OccupyStatus.Free, OccupyCommunicationStatus.Ready, "4afeb097-0e06-40ca-ad23-4623296da1e2",null,null,null);
		PaymentOrder paymentOrder2 = new PaymentOrder("67183fb6-c20d-4837-afe0-c1f3029cded5", AccountSide.CREDIT, "", "571907757810703", "CB", "王小轲", "6222620170003397044", "301290000007", new BigDecimal("0.02"), "10", "超级网银测试2", "2a07d739-48db-42da-a67a-e1484ad3b75f", GatewayType.SuperBank, BusinessStatus.Processing, CommunicationStatus.Inqueue, OccupyStatus.Free, OccupyCommunicationStatus.Ready, OccupyStatus.Free, OccupyCommunicationStatus.Ready, OccupyStatus.Free, OccupyCommunicationStatus.Ready, "fea3dc96-69a1-4d71-9165-ff1b54c5d874",null,null,null);

		taskList.add(paymentOrder1);
		taskList.add(paymentOrder2);
		
		//paymentOrderService.taskInQueue(taskList, "");
		
		List<PaymentOrder> paymentOrderList = genericDaoSupport.searchForList("FROM PaymentOrder");
		
		Assert.assertEquals(2, paymentOrderList.size());
		
	}
	
	
	@Test
	@Sql("classpath:test/testPeekIdleTasks.sql")
	public void testPeekIdleTasksSlot1() {
		
		List<Integer> modPriority = new ArrayList<Integer>() {
			{
			//add(3);
			//add(5);
			add(2);
		   }
		};
		
		//List<PaymentOrder> idleTasks = paymentOrderService.peekIdleTasks("", PaymentTaskSpec.PEEK_IDLE_LIMIT, modPriority , 1);
		
		//Assert.assertEquals(2, idleTasks.size());
		
	}
	
	
	@Test
	@Sql("classpath:test/testPeekIdleTasksSlot2.sql")
	public void testPeekIdleTasksSlot2() {
		
		List<Integer> modPriority = new ArrayList<Integer>() {
			{
			//add(3);
			//add(5);
			add(2);
		   }
		};
		
		//List<PaymentOrder> idleTasks = paymentOrderService.peekIdleTasks("", PaymentTaskSpec.PEEK_IDLE_LIMIT, modPriority, 1);
		
		//Assert.assertEquals(3, idleTasks.size());
		
	}
	
	
	@Test
	@Sql("classpath:test/testAtomOccupy.sql")
	public void testAtomOccupy() {
		
		//PaymentOrder dstPaymentOrder = genericDaoSupport.queryForList("select * from payment_order where id =:id", "id", 1l, PaymentOrder.class).get(0);
		PaymentOrder dstPaymentOrder = (PaymentOrder) (genericDaoSupport.searchForList("from PaymentOrder where id =:id", "id", 1l).get(0));
		
		boolean occupySuccess = paymentOrderService.atomOccupy(1, dstPaymentOrder, 3, "030f6ca5-558a-47d6-a259-6cf9d6842f6c", "");
		
		Assert.assertTrue(occupySuccess);
		
		String sourceAccessVersion = dstPaymentOrder.getAccessVersion();
		
		//dstPaymentOrder = (PaymentOrder) (genericDaoSupport.searchForList("from PaymentOrder where id =:id", "id", 1l).get(0));
		List<String> resultList = genericDaoSupport.queryForList("select access_version from payment_order where id =:id", "id", 1l, String.class);
		String accessVersion = resultList.get(0);
		
		Assert.assertNotEquals(sourceAccessVersion, accessVersion);
		
		List<Integer> resultList1 = genericDaoSupport.queryForSingleColumnList("select business_status from payment_order where id =:id", "id", 1l, Integer.class);
		int businessStatus = resultList1.get(0);
		
		Assert.assertEquals(BusinessStatus.Processing.ordinal(), businessStatus);
		
		List<Integer> resultList2 = genericDaoSupport.queryForSingleColumnList("select communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int communicationStatus = resultList2.get(0);
		
		Assert.assertEquals(CommunicationStatus.Process.ordinal(), communicationStatus);
		
		List<Integer> resultList3 = genericDaoSupport.queryForSingleColumnList("select fst_communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int fst_communication_status = resultList3.get(0);
		
		Assert.assertEquals(OccupyCommunicationStatus.Processing.ordinal(), fst_communication_status);
		
		List<Integer> resultList4 = genericDaoSupport.queryForSingleColumnList("select fst_occupy_status from payment_order where id =:id", "id", 1l, Integer.class);
		int fst_occupy_status = resultList4.get(0);
		
		Assert.assertEquals(OccupyStatus.Occupied.ordinal(), fst_occupy_status);
		
		List<Integer> resultList5 = genericDaoSupport.queryForSingleColumnList("select snd_communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int snd_communication_status = resultList5.get(0);
		
		Assert.assertEquals(OccupyCommunicationStatus.Ready.ordinal(), snd_communication_status);
		
		
		List<Integer> resultList6 = genericDaoSupport.queryForSingleColumnList("select trd_communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int trd_communication_status = resultList6.get(0);
		
		
		Assert.assertEquals(OccupyCommunicationStatus.Ready.ordinal(), trd_communication_status);
		
		List<String> resultList7 = genericDaoSupport.queryForSingleColumnList("select fst_occuppied_worker_uuid from payment_order where id =:id", "id", 1l, String.class);
		String fst_occuppied_worker_uuid = resultList7.get(0);
		
		Assert.assertEquals("030f6ca5-558a-47d6-a259-6cf9d6842f6c", fst_occuppied_worker_uuid);
	}
	

	@Test
	@Sql("classpath:test/testAtomSentOut.sql")
	public void testAtomSentOut() {
		
		PaymentOrder dstPaymentOrder = (PaymentOrder) (genericDaoSupport.searchForList("from PaymentOrder where id =:id", "id", 1l).get(0));

		String sourceAccessVersion = dstPaymentOrder.getAccessVersion();
		
		boolean sentOutSuccess = paymentOrderService.atomSentOut(1, dstPaymentOrder, 3, "");
		
		//dstPaymentOrder = (PaymentOrder) (genericDaoSupport.searchForList("from PaymentOrder where id =:id", "id", 1l).get(0));

		Assert.assertTrue(sentOutSuccess);
		
		List<String> resultList = genericDaoSupport.queryForList("select access_version from payment_order where id =:id", "id", 1l, String.class);
		String accessVersion = resultList.get(0);
		Assert.assertNotEquals(sourceAccessVersion, accessVersion);
		
		
		List<Integer> resultList3 = genericDaoSupport.queryForSingleColumnList("select fst_communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int fst_communication_status = resultList3.get(0);
		Assert.assertEquals(OccupyCommunicationStatus.Sending.ordinal(), fst_communication_status);

		List<Integer> resultList4 = genericDaoSupport.queryForSingleColumnList("select communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int communication_status = resultList4.get(0);
		Assert.assertNotEquals(CommunicationStatus.Success.ordinal(), communication_status);
	}
	
	
	@Test
	@Sql("classpath:test/testAtomFeedBack.sql")
	public void testAtomFeedBack() {
		
		PaymentOrder dstPaymentOrder = (PaymentOrder) (genericDaoSupport.searchForList("from PaymentOrder where id =:id", "id", 1l).get(0));

		String sourceAccessVersion = dstPaymentOrder.getAccessVersion();
		
		boolean feedBackSuccess = paymentOrderService.atomFeedBack(1, dstPaymentOrder, 3, "");
		
		//dstPaymentOrder = (PaymentOrder) (genericDaoSupport.searchForList("from PaymentOrder where id =:id", "id", 1l).get(0));

		Assert.assertTrue(feedBackSuccess);
		
		List<String> resultList = genericDaoSupport.queryForList("select access_version from payment_order where id =:id", "id", 1l, String.class);
		String accessVersion = resultList.get(0);
		Assert.assertNotEquals(sourceAccessVersion, accessVersion);
		
		List<Integer> resultList3 = genericDaoSupport.queryForSingleColumnList("select fst_communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int fst_communication_status = resultList3.get(0);
		
		Assert.assertEquals(OccupyCommunicationStatus.Done.ordinal(), fst_communication_status);

		List<Integer> resultList4 = genericDaoSupport.queryForSingleColumnList("select communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int communication_status = resultList4.get(0);
		Assert.assertEquals(CommunicationStatus.Success.ordinal(), communication_status);
	}
	
	
	@Test
	@Sql("classpath:test/testAtomForceStop.sql")
	public void testAtomForceStop() {
		
		PaymentOrder dstPaymentOrder = (PaymentOrder) (genericDaoSupport.searchForList("from PaymentOrder where id =:id", "id", 1l).get(0));

		String sourceAccessVersion = dstPaymentOrder.getAccessVersion();
		
		boolean forceStopSuccess = paymentOrderService.atomForceStop(1, dstPaymentOrder, 3, "");
		
		//dstPaymentOrder = (PaymentOrder) (genericDaoSupport.searchForList("from PaymentOrder where id =:id", "id", 1l).get(0));

		Assert.assertTrue(forceStopSuccess);
		
		List<String> resultList = genericDaoSupport.queryForList("select access_version from payment_order where id =:id", "id", 1l, String.class);
		String accessVersion = resultList.get(0);
		Assert.assertNotEquals(sourceAccessVersion, accessVersion);
		
		List<Integer> resultList3 = genericDaoSupport.queryForSingleColumnList("select fst_communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int fst_communication_status = resultList3.get(0);
		
		Assert.assertEquals(OccupyCommunicationStatus.Done.ordinal(), fst_communication_status);
		

		List<Integer> resultList4 = genericDaoSupport.queryForSingleColumnList("select communication_status from payment_order where id =:id", "id", 1l, Integer.class);
		int communication_status = resultList4.get(0);

		Assert.assertEquals(CommunicationStatus.Process.ordinal(), communication_status);
	}
	
	
	@Test
	@Sql("classpath:test/testGetSendingTimeOutTasks.sql")
	public void testGetSendingTimeOutTasks() {
		
		List<PaymentOrder> timeOutTasks = paymentOrderService.getSendingTimeOutTasks(PaymentTaskSpec.DEFAULT_SENDING_TIMEOUT_MINUTES, "");
		
		Assert.assertEquals(1, timeOutTasks.size());
	}
	
	
}
