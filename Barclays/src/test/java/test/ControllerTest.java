package test;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.service.CashFlowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.barclays.controller.PaymentAsynController;
import com.suidifu.coffer.entity.PaymentGateWayNameSpace;
import com.suidifu.coffer.entity.QueryCreditModel;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml", })
public class ControllerTest {
	@Autowired
	private CashFlowService cashFlowService;

	@Autowired
	protected GenericDaoSupport genericDaoSupport;
	@Test
	public void test() {
		PaymentAsynController tradeScheduleController =  new PaymentAsynController();
//		tradeScheduleController.asyncQueryStatus(new QueryCreditModel("001", "2015-05-23", "2015-12-02", "50", "1", "0", "2", "03", null, new Date(), "001",PaymentGateWayNameSpace.GATEWAY_TYPE_UNIONPAYGZ, 1,"aaa"));
	}

	@Test
	public void test1() {
		List<CashFlow> cashFlowList = genericDaoSupport.loadAll(CashFlow.class);
		String s = JSONObject.toJSONString(cashFlowList);
		File file = new File("/Users/dafuchen/a.txt");
		try {
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(s.getBytes(Charset.defaultCharset()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
