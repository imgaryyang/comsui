package test;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.barclays.handler.PaymentAsyncHandler;
import com.suidifu.coffer.entity.PaymentGateWayNameSpace;
import com.suidifu.coffer.entity.QueryCreditModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml", })
public class HandlerTest {

	@Autowired
	PaymentAsyncHandler paymentAsyncHandler;
	
	@Test
	public void test1() throws InterruptedException {
		paymentAsyncHandler.queryDebit(new QueryCreditModel("001", "2015-05-23", "2015-12-02", "50", "1", "0", "2", "03", null, new Date(), "001",PaymentGateWayNameSpace.GATEWAY_TYPE_UNIONPAYGZ, 1,"aaa"), "");
		Thread.sleep(100000);
	}
}
