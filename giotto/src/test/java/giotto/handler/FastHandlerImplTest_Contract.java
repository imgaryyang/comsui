package giotto.handler;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
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
import com.suidifu.giotto.keyenum.FastContractKeyEnum;
import com.suidifu.giotto.model.FastContract;
import com.suidifu.giotto.service.FastService;

import giotto.BaseTest;
import giotto.TestGenerator;

/**FastHandlerImplTest_Contract
 * 
 * @author zfj
 * @date   2017/6/2
 */
@Transactional
@Rollback
public class FastHandlerImplTest_Contract extends BaseTest {
	
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
		List<FastContract> fastContractList =
                fastCacheService.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class);
        //是否读到
		Assert.assertEquals(true, CollectionUtils.isEmpty(fastContractList));

        List<FastContract> fastContractHandlerList = 
        		fastHandler.getByKeyList(FastContractKeyEnum.CONTRACT_NO, 
        				TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
        
        for (int i = 0; i < fastContractHandlerList.size(); i++) {
        	Assert.assertNotEquals(null, fastContractHandlerList.get(i));
        	Assert.assertEquals(TestGenerator.CONTRACT_NO_EXSIT, fastContractHandlerList.get(i).getContractNo());
		}

        fastContractList =
                fastCacheService.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class);
        Assert.assertEquals(false, CollectionUtils.isEmpty(fastContractList));
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testGetByKey() throws GiottoException {
        //从缓存读数据
		List<FastContract> FastContractList =
                fastCacheService.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class);
        //是否读到
		Assert.assertEquals(true, CollectionUtils.isEmpty(FastContractList));

		 FastContract fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
        Assert.assertNotEquals(null, fastContract);
        Assert.assertEquals(TestGenerator.CONTRACT_NO_EXSIT, fastContract.getContractNo());

        FastContractList =
                fastCacheService.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class);
        Assert.assertEquals(false, CollectionUtils.isEmpty(FastContractList));
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testDelByKey() throws GiottoException {
		FastContract fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
		Assert.assertNotEquals(null, fastContract);
       Assert.assertEquals(TestGenerator.CONTRACT_NO_EXSIT, fastContract.getContractNo());

       fastHandler.delByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, false);
       fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
       Assert.assertEquals(null, fastContract);
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testAdd() throws GiottoException {
		FastContract fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
        Assert.assertNotEquals(null, fastContract);
        Assert.assertEquals(TestGenerator.CONTRACT_NO_EXSIT, fastContract.getContractNo());

        fastHandler.delByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, false);
        FastContract fastContract1 = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
        Assert.assertEquals(null, fastContract1);

        fastHandler.add(fastContract, false);
        fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
        Assert.assertNotEquals(null, fastContract);
	}

//	@Test
//	@Sql("classpath:test/t_common_insert.sql")
//	public void testUpdateSuccess() {
//		FastContract fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
//		Assert.assertNotEquals(null, fastContract);
//        Assert.assertEquals(TestGenerator.CONTRACT_NO_EXSIT, fastContract.getContractNo());
//
//        String updateSql = "update contract set repayment_plan_operate_logs = :repaymentPlanOperateLogs where unique_id = :uniqueId";
//        Map<String, Object> paramMap = new HashMap<>(2);
//        paramMap.put("repaymentPlanOperateLogs", "hello");
//        paramMap.put("uniqueId", TestGenerator.CONTRACT_UNIQUEID_EXSIT);
//        int count = fastHandler.update(updateSql, paramMap, fastContract);
//        Assert.assertEquals(1, count);
//
//        fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
//        Assert.assertEquals("hello", fastContract.getRepaymentPlanOperateLogs());
//	}
//
//	@Test
//	@Sql("classpath:test/t_common_insert.sql")
//	public void testUpdatefalse(){
//		FastContract fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
//		Assert.assertNotEquals(null, fastContract);
//        Assert.assertEquals(TestGenerator.CONTRACT_NO_EXSIT, fastContract.getContractNo());
//
//        fastContract.setTotalAmount(new BigDecimal(20.00));
//
//        String updateSql = "update contract set repayment_plan_operate_logs = :repaymentPlanOperateLogs where unique_id = :uniqueId";
//        Map<String, Object> paramMap = new HashMap<>(2);
//        paramMap.put("repaymentPlanOperateLogs", "hello");
//        paramMap.put("uniqueId", TestGenerator.CONTRACT_UNIQUEID_EXSIT);
//        int count = fastHandler.update(updateSql, paramMap, fastContract);
//        Assert.assertEquals(0, count);
//
//        fastContract = fastHandler.getByKey(FastContractKeyEnum.CONTRACT_NO, TestGenerator.CONTRACT_NO_EXSIT, FastContract.class, false);
//
//        Assert.assertNotEquals("hello", fastContract.getRepaymentPlanOperateLogs());
//	}

}
