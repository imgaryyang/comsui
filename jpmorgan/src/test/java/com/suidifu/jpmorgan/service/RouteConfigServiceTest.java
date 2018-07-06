package com.suidifu.jpmorgan.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml", })
@TransactionConfiguration(defaultRollback=false)
@Transactional()
public class RouteConfigServiceTest {
	
	/*//@Autowired
	private RouteConfigService routeConfigService;
	
	*/
	
	
	@Test
	public void testAppendMap() {
		Map<String, Object> parms1 = new HashMap<String, Object>();
		parms1.put("aa", "aa");
		
		Map<String, Object> parms2 = new HashMap<String, Object>();
		parms2.put("bb", "bb");
		
		parms1.putAll(parms2);
		
		Assert.assertEquals(2, parms1.size());
	}
	
	
	/*@Test
	public void testSave(){
		RouteConfig rc = new RouteConfig();
		rc.setIp("127.0.0.1");
		rc.setPort("8080");
		rc.setUrl("localhost://example.com");
		rc.setGatewayOnlineStatus(GatewayOnlineStatus.Online);
		//rc.setGatewayType("GZ");
		rc.setGatewayWorkingStatus(GatewayWorkingStatus.Working);
		rc.setGatewayType(GatewayType.UnionPay);
		rc.setEffectiveDate(new Date());
		routeConfigService.save(rc);
	}

	
	@Test
	@Sql("classpath:test/testRouteConfigService.sql")
	public void listUrlTest(){
		
		GatewayType gatewayType = GatewayType.UnionPay;
		
		List<String>  urls = routeConfigService.listUrl(gatewayType);
		
	}
	
	
	@Test
	public void findTest(){
		
		RouteConfigQueryModel queryModel = new RouteConfigQueryModel();
		
		List<RouteConfig> routeConfigs = routeConfigService.find(queryModel);
		
	}*/

}
