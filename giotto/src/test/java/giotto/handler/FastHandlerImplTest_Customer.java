package giotto.handler;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.suidifu.giotto.exception.GiottoException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastCustomerKeyEnum;
import com.suidifu.giotto.model.FastCustomer;
import com.suidifu.giotto.service.FastService;

import giotto.BaseTest;
import giotto.TestGenerator;

/**FastHandlerImplTest_Customer
 * 
 * @author jt
 * @date  2017/6/2
 */

@Transactional
@Rollback
public class FastHandlerImplTest_Customer extends BaseTest {
	
	@Autowired private FastHandler fastHandler;
    @Autowired private StringRedisTemplate redisTemplate;
    @Autowired private FastService fastCacheService;

    @After
    public void clear() {
        TestGenerator.delRedisKey(redisTemplate);
    }
    
	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testGetByKeyList() throws Exception{
        //从缓存读数据
		List<FastCustomer> fastCustomerList =
                fastCacheService.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class);
        //是否读到
		Assert.assertEquals(true, CollectionUtils.isEmpty(fastCustomerList));

        List<FastCustomer> fastCustomerHandlerList = 
        		fastHandler.getByKeyList(FastCustomerKeyEnum.CUSTOMER_UUID, 
        				TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
        
        for (int i = 0; i < fastCustomerHandlerList.size(); i++) {
        	Assert.assertNotEquals(null, fastCustomerHandlerList.get(i));
        	Assert.assertEquals(TestGenerator.CUSTOMER_UUID_EXSIT, fastCustomerHandlerList.get(i).getCustomerUuid());
		}

        fastCustomerList =
                fastCacheService.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class);
        Assert.assertEquals(false, CollectionUtils.isEmpty(fastCustomerList));
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testGetByKey() throws GiottoException {
		List<FastCustomer> fastCustomerList =
                fastCacheService.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class);
        //是否读到
		Assert.assertEquals(true, CollectionUtils.isEmpty(fastCustomerList));

		FastCustomer fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
        Assert.assertNotEquals(null, fastCustomer);
        Assert.assertEquals(TestGenerator.CUSTOMER_UUID_EXSIT, fastCustomer.getCustomerUuid());

        fastCustomerList =
                fastCacheService.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class);
        Assert.assertEquals(false, CollectionUtils.isEmpty(fastCustomerList));
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testDelByKey() throws GiottoException {
		FastCustomer fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
		Assert.assertNotEquals(null, fastCustomer);
       Assert.assertEquals(TestGenerator.CUSTOMER_UUID_EXSIT, fastCustomer.getCustomerUuid());

       fastHandler.delByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, false);
       fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
       Assert.assertEquals(null, fastCustomer);
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testAdd() throws GiottoException {
		FastCustomer fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
		Assert.assertNotEquals(null, fastCustomer);
       Assert.assertEquals(TestGenerator.CUSTOMER_UUID_EXSIT, fastCustomer.getCustomerUuid());

       fastHandler.delByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, false);
       FastCustomer astCustomer1 = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
        Assert.assertEquals(null, astCustomer1);

        fastHandler.add(fastCustomer, false);
        fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
        Assert.assertNotEquals(null, fastCustomer);
	}

//	@Test
//	@Sql("classpath:test/t_common_insert.sql")
//	public void testUpdateSuccess() {
//		FastCustomer fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
//		Assert.assertNotEquals(null, fastCustomer);
//       Assert.assertEquals(TestGenerator.CUSTOMER_UUID_EXSIT, fastCustomer.getCustomerUuid());
//
//       String updateSql = "update customer set name = :name where customer_uuid = :customerUuid";
//       Map<String, Object> paramMap = new HashMap<>(2);
//       paramMap.put("name", "hello");
//       paramMap.put("customerUuid", TestGenerator.CUSTOMER_UUID_EXSIT);
//       int count = fastHandler.update(updateSql, paramMap, fastCustomer);
//
//       Assert.assertEquals(1, count);
//
//       fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
//        Assert.assertEquals("hello", fastCustomer.getName());
//	}
//
//	@Test
//	@Sql("classpath:test/t_common_insert.sql")
//	public void testUpdatefalse(){
//		FastCustomer fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
//		Assert.assertNotEquals(null, fastCustomer);
//       Assert.assertEquals(TestGenerator.CUSTOMER_UUID_EXSIT, fastCustomer.getCustomerUuid());
//
//       fastCustomer.setMobile("137");
//
//       String updateSql = "update customer set name = :name where customer_uuid = :customerUuid";
//       Map<String, Object> paramMap = new HashMap<>(2);
//       paramMap.put("name", "hello");
//       paramMap.put("customerUuid", TestGenerator.CUSTOMER_UUID_EXSIT);
//       int count = fastHandler.update(updateSql, paramMap, fastCustomer);
//
//        Assert.assertEquals(0, count);
//
//        fastCustomer = fastHandler.getByKey(FastCustomerKeyEnum.CUSTOMER_UUID, TestGenerator.CUSTOMER_UUID_EXSIT, FastCustomer.class, false);
//        Assert.assertNotEquals("hello", fastCustomer.getName());
//	}

}
