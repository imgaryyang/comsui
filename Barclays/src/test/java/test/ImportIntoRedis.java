package test;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ReferenceResolver;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.handler.CashFlowHandler;
import com.zufangbao.sun.service.CashFlowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;


/**
 * @author dafuchen
 *         2017/12/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/context/applicationContext-*.xml",
        "classpath:/local/applicationContext-*.xml", })

public class ImportIntoRedis {
    Logger logger = LoggerFactory.getLogger(ImportIntoRedis.class);

    @Autowired
    private GenericDaoSupport genericDaoSupport;

    @Autowired
    private CashFlowHandler cashFlowHandler;

    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private ZSetOperations <String, CashFlow> cashFlowZSetOperations;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static String MER_ID = "6224080600234";

    @Test
    public void saveIntoRedis() {
        long startTime = System.currentTimeMillis();
        List<CashFlow> cashFlowList = genericDaoSupport.loadAll(CashFlow.class);
        logger.info("db query time {}", System.currentTimeMillis() - startTime);
        cashFlowService.saveInRedis(cashFlowList, cashFlowZSetOperations,stringRedisTemplate, MER_ID);
        logger.info("redis save time {}", System.currentTimeMillis() - startTime);
    }

}
