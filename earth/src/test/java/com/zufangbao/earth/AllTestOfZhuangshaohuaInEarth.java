package com.zufangbao.earth;


import com.zufangbao.earth.web.controller.notice.NoticeControllerTest;
import com.zufangbao.earth.yunxin.handler.impl.NoticeHandlerTest;
import com.zufangbao.earth.yunxin.handler.impl.SystemOperateLogHandlerTest;
import com.zufangbao.earth.yunxin.service.ModifyOverDueFeeLogServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;



@RunWith(Suite.class)
@SuiteClasses({
		NoticeControllerTest.class,
		NoticeHandlerTest.class,
		SystemOperateLogHandlerTest.class,
		ModifyOverDueFeeLogServiceTest.class
	})
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })
public class AllTestOfZhuangshaohuaInEarth {
	@Test
	public void testsada(){
		
	}
	
}
