package test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.suidifu.barclays.service.QueryCreditResultService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml", })
public class QueryCreditResultServiceTest {
	@Autowired
	QueryCreditResultService queryCreditResultService;

}
