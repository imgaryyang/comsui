package com.zufangbao.earth;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.zufangbao.earth.api.test.post.BaseApiTestPost;

@RunWith(SpringJUnit4ClassRunner.class)
@SuiteClasses({})
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })


@TransactionConfiguration(defaultRollback = true)
public class AllTestOfZhenghangboInEarth  extends BaseApiTestPost{

}		
	
