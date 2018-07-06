package giotto.handler;

import java.util.HashMap;
import java.util.List;

import com.suidifu.giotto.exception.GiottoException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.CollectionUtils;

import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAccountTemplateKeyEnum;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.model.FastAccountTemplate;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.giotto.service.FastService;

import giotto.BaseTest;
import giotto.TestGenerator;

public class FastHandlerImplTest_AccountTemplate extends BaseTest{

	@Autowired private FastHandler fastHandler;
    @Autowired private StringRedisTemplate redisTemplate;
    @Autowired private FastService fastCacheService;
    
    @After
    public void clear() {
        TestGenerator.delRedisKey(redisTemplate);
    }
    
    @Test
//    @Sql("classpath:test/t_common_insert.sql")
    public void testGetByKey() throws GiottoException {
    	HashMap<FastKey,String> hashMap = new HashMap<FastKey,String>();
    	hashMap.put(FastAccountTemplateKeyEnum.LEDGER_BOOK_NO, "faac8a6e-70c4-4054-81b5-5d48e984097b");
    	hashMap.put(FastAccountTemplateKeyEnum.EVENT_TYPE, "17");
    	FastAccountTemplate fastAssetSetList = fastHandler.getByKey(hashMap,FastAccountTemplate.class, true);
    	System.out.println(fastAssetSetList);
    }
    @Test
    public void testGetByKeyList() throws GiottoException {
    	HashMap<FastKey,String> hashMap = new HashMap<FastKey,String>();
    	hashMap.put(FastAccountTemplateKeyEnum.LEDGER_BOOK_NO, "9ae73f91-bf2c-40ff-acf2-767ce81187c1");
    	hashMap.put(FastAccountTemplateKeyEnum.EVENT_TYPE, "17");
    	List<FastAccountTemplate> byKeyList = fastHandler.getByKeyList(hashMap, FastAccountTemplate.class, true);
    	System.out.println(byKeyList);
    }
}
