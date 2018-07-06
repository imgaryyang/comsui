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
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.giotto.service.FastService;

import giotto.BaseTest;
import giotto.TestGenerator;

/**FastHandlerImplTest_AssetSet
 * 
 * @author zfj
 * @date   2017/6/2
 */
@Transactional
@Rollback
public class FastHandlerImplTest_AssetSet extends BaseTest {
	
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
		List<FastAssetSet> fastAssetSetList =
                fastCacheService.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class);
        //是否读到
		Assert.assertEquals(true, CollectionUtils.isEmpty(fastAssetSetList));

        List<FastAssetSet> fastAssetSetHandlerList = 
        		fastHandler.getByKeyList(FastAssetSetKeyEnum.ASSET_SET_UUID, 
        				TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class,false);
        
        for (int i = 0; i < fastAssetSetHandlerList.size(); i++) {
        	Assert.assertNotEquals(null, fastAssetSetHandlerList.get(i));
        	Assert.assertEquals(TestGenerator.ASSET_UUID_EXSIT, fastAssetSetHandlerList.get(i).getAssetUuid());
		}

        fastAssetSetList =
                fastCacheService.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class);
        Assert.assertEquals(false, CollectionUtils.isEmpty(fastAssetSetList));
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testGetByKey() throws GiottoException {
        //从缓存读数据
		List<FastAssetSet> fastAssetSetList =
                fastCacheService.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class);
        //是否读到
		Assert.assertEquals(true, CollectionUtils.isEmpty(fastAssetSetList));

        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
        Assert.assertNotEquals(null, fastAssetSet);
        Assert.assertEquals(TestGenerator.ASSET_UUID_EXSIT, fastAssetSet.getAssetUuid());

        fastAssetSetList =
                fastCacheService.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class);
        Assert.assertEquals(false, CollectionUtils.isEmpty(fastAssetSetList));
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testDelByKey() throws GiottoException {
		FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
		Assert.assertNotEquals(null, fastAssetSet);
       Assert.assertEquals(TestGenerator.ASSET_UUID_EXSIT, fastAssetSet.getAssetUuid());

       fastHandler.delByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, false);
       fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
       Assert.assertEquals(null, fastAssetSet);
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testAdd() throws GiottoException {
        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
        Assert.assertNotEquals(null, fastAssetSet);
        Assert.assertEquals(TestGenerator.ASSET_UUID_EXSIT, fastAssetSet.getAssetUuid());

        fastHandler.delByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, false);
        FastAssetSet fastAssetSet1 = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
        Assert.assertEquals(null, fastAssetSet1);

        fastHandler.add(fastAssetSet, false);
        fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
        Assert.assertNotEquals(null, fastAssetSet);
	}

//	@Test
//	@Sql("classpath:test/t_common_insert.sql")
//	public void testUpdateSuccess() {
//        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
//        Assert.assertNotEquals(null, fastAssetSet);
//        Assert.assertEquals(TestGenerator.ASSET_UUID_EXSIT, fastAssetSet.getAssetUuid());
//
//        String updateSql = "update asset_set set comment = :comment where asset_uuid = :assetSetUuid";
//        Map<String, Object> paramMap = new HashMap<>(2);
//        paramMap.put("comment", "hello");
//        paramMap.put("assetSetUuid", TestGenerator.ASSET_UUID_EXSIT);
//        int count = fastHandler.update(updateSql, paramMap, fastAssetSet);
//        Assert.assertEquals(1, count);
//
//        fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
//        Assert.assertEquals("hello", fastAssetSet.getComment());
//	}
//
//	@Test
//	@Sql("classpath:test/t_common_insert.sql")
//	public void testUpdatefalse(){
//        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
//        Assert.assertNotEquals(null, fastAssetSet);
//        Assert.assertEquals(TestGenerator.ASSET_UUID_EXSIT, fastAssetSet.getAssetUuid());
//
//        fastAssetSet.setAssetFairValue(new BigDecimal(20.00));
//
//        String updateSql = "update asset_set set comment = :comment where asset_uuid = :assetSetUuid";
//        Map<String, Object> paramMap = new HashMap<>(2);
//        paramMap.put("comment", "hello");
//        paramMap.put("assetSetUuid", TestGenerator.ASSET_UUID_EXSIT);
//        int count = fastHandler.update(updateSql, paramMap, fastAssetSet);
//
//        Assert.assertEquals(0, count);
//
//        fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID, TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, false);
//
//        Assert.assertNotEquals("hello", fastAssetSet.getComment());
//	}

}
