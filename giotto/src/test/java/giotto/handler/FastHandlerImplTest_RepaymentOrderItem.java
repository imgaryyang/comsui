package giotto.handler;

import java.util.ArrayList;
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
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.model.FastRepaymentOrderItem;
import com.suidifu.giotto.service.FastService;

import giotto.BaseTest;
import giotto.TestGenerator;

/**FastHandlerImplTest_RepaymentOrderItem
 * 
 * @author zfj
 * @date  2017/6/2
 */

@Transactional
@Rollback
public class FastHandlerImplTest_RepaymentOrderItem extends BaseTest {
	
	@Autowired private FastHandler fastHandler;
    @Autowired private StringRedisTemplate redisTemplate;
    @Autowired private FastService fastCacheService;

    @After
    public void clear() {
//        TestGenerator.delRedisKey(redisTemplate);
    }
    
	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testGetMerIdByKeyList() throws Exception{
        //从缓存读数据
		List<FastRepaymentOrderItem> fastRepaymentOrderItemList =
                fastCacheService.getByKey(FastRepaymentOrderItemKeyEnum.MER_ID,
                		TestGenerator.ITEM_MER_ID, FastRepaymentOrderItem.class);
        //是否读到
		Assert.assertEquals(true, CollectionUtils.isEmpty(fastRepaymentOrderItemList));

        List<FastRepaymentOrderItem> FastRepaymentOrderItemHandlerList = 
        		fastHandler.getByKeyList(FastRepaymentOrderItemKeyEnum.MER_ID,
        				TestGenerator.ITEM_MER_ID, FastRepaymentOrderItem.class, false);
        
        for (int i = 0; i < FastRepaymentOrderItemHandlerList.size(); i++) {
        	Assert.assertNotEquals(null, FastRepaymentOrderItemHandlerList.get(i));
        	Assert.assertEquals(TestGenerator.ITEM_MER_ID, FastRepaymentOrderItemHandlerList.get(i).getMerId());
		}

        fastRepaymentOrderItemList =
                fastCacheService.getByKey(FastRepaymentOrderItemKeyEnum.MER_ID,
                		TestGenerator.ITEM_MER_ID, FastRepaymentOrderItem.class);
        Assert.assertEquals(false, CollectionUtils.isEmpty(fastRepaymentOrderItemList));
	}
	
	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testGetOrderUuidByKeyList() throws Exception{
        //从缓存读数据
		List<FastRepaymentOrderItem> fastRepaymentOrderItemList =
                fastCacheService.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_UUID,
                		TestGenerator.ITEM_ORDER_UUID, FastRepaymentOrderItem.class);
        //是否读到
		Assert.assertEquals(true, CollectionUtils.isEmpty(fastRepaymentOrderItemList));

        List<FastRepaymentOrderItem> FastRepaymentOrderItemHandlerList = 
        		fastHandler.getByKeyList(FastRepaymentOrderItemKeyEnum.ORDER_UUID,
        				TestGenerator.ITEM_ORDER_UUID, FastRepaymentOrderItem.class, false);
        
        for (int i = 0; i < FastRepaymentOrderItemHandlerList.size(); i++) {
        	Assert.assertNotEquals(null, FastRepaymentOrderItemHandlerList.get(i));
        	Assert.assertEquals(TestGenerator.ITEM_ORDER_UUID, FastRepaymentOrderItemHandlerList.get(i).getOrderUuid());
		}

        fastRepaymentOrderItemList =
                fastCacheService.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_UUID,
                		TestGenerator.ITEM_ORDER_UUID, FastRepaymentOrderItem.class);
        Assert.assertEquals(false, CollectionUtils.isEmpty(fastRepaymentOrderItemList));
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testGetByKey() throws GiottoException {
        //从缓存读数据
		List<FastRepaymentOrderItem> fastRepaymentOrderItemList =
                fastCacheService.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
                		TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class);
        //是否读到
		Assert.assertEquals(true, CollectionUtils.isEmpty(fastRepaymentOrderItemList));

		FastRepaymentOrderItem fastRepaymentOrderItem = 
				fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
						TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
        Assert.assertNotEquals(null, fastRepaymentOrderItem);
        Assert.assertEquals(TestGenerator.ITEM_ORDER_DETAIL_UUID, fastRepaymentOrderItem.getOrderDetailUuid());

        fastRepaymentOrderItemList =
                fastCacheService.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
                		TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class);
        Assert.assertEquals(false, CollectionUtils.isEmpty(fastRepaymentOrderItemList));
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testDelByKey() throws GiottoException {
		FastRepaymentOrderItem fastRepaymentOrderItem = 
				fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
						TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
		Assert.assertNotEquals(null, fastRepaymentOrderItem);
       Assert.assertEquals(TestGenerator.ITEM_ORDER_DETAIL_UUID, fastRepaymentOrderItem.getOrderDetailUuid());

       fastHandler.delByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,TestGenerator.ITEM_ORDER_DETAIL_UUID, false);
       fastRepaymentOrderItem = 
    		   fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
    				   TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
       Assert.assertEquals(null, fastRepaymentOrderItem);
	}

	@Test
	@Sql("classpath:test/t_common_insert.sql")
	public void testAdd() throws GiottoException {
		FastRepaymentOrderItem fastRepaymentOrderItem = 
				fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
						TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
		Assert.assertNotEquals(null, fastRepaymentOrderItem);
       Assert.assertEquals(TestGenerator.ITEM_ORDER_DETAIL_UUID, fastRepaymentOrderItem.getOrderDetailUuid());
       
       fastHandler.delByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,TestGenerator.ITEM_ORDER_DETAIL_UUID, false);
       FastRepaymentOrderItem fastRepaymentOrderItem1 = 
    		   fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
    				   TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
        Assert.assertEquals(null, fastRepaymentOrderItem1);

        fastHandler.add(fastRepaymentOrderItem, false);
        fastRepaymentOrderItem = 
        		fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
        				TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
        Assert.assertNotEquals(null, fastRepaymentOrderItem);
	}

	@Test
//	@Sql("classpath:test/t_common_insert.sql")
	public void testGetByOrderUuid() throws GiottoException {
		List<FastRepaymentOrderItem> orderList = fastHandler.getListByKeyList(
				FastRepaymentOrderItemKeyEnum.ORDER_UUID, new ArrayList<String>(){{
					add("2f8f1853-b5f8-4f9f-a1a0-e7b96279e4f9");
					add("dad1368d-624f-45ee-891e-0dbc5e563918");
					add("a2e06776-01c1-422e-b5f6-5330898a51a0");
					add("a00116a2-2ec0-4711-8ad0-f7b88053aad1");
				}}, FastRepaymentOrderItem.class, true);
		Assert.assertNotEquals(null, orderList);
	}

//	@Test
//	@Sql("classpath:test/t_common_insert.sql")
//	public void testUpdateSuccess() {
//		FastRepaymentOrderItem fastRepaymentOrderItem =
//				fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
//						TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
//		Assert.assertNotEquals(null, fastRepaymentOrderItem);
//       Assert.assertEquals(TestGenerator.ITEM_ORDER_DETAIL_UUID, fastRepaymentOrderItem.getOrderDetailUuid());
//
//       String updateSql = "update repayment_order_item set order_unique_id = :orderUniqueId where order_detail_uuid = :orderDetailUuid";
//       Map<String, Object> paramMap = new HashMap<>(2);
//       paramMap.put("orderUniqueId", "hello");
//       paramMap.put("orderDetailUuid", TestGenerator.ITEM_ORDER_DETAIL_UUID);
//       int count = fastHandler.update(updateSql, paramMap, fastRepaymentOrderItem);
//       Assert.assertEquals(1, count);
//
//       fastRepaymentOrderItem =
//    		   fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
//    				   TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
//        Assert.assertEquals("hello", fastRepaymentOrderItem.getOrderUniqueId());
//	}
//
//	@Test
//	@Sql("classpath:test/t_common_insert.sql")
//	public void testUpdatefalse(){
//		FastRepaymentOrderItem fastRepaymentOrderItem =
//				fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
//						TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
//		Assert.assertNotEquals(null, fastRepaymentOrderItem);
//       Assert.assertEquals(TestGenerator.ITEM_ORDER_DETAIL_UUID, fastRepaymentOrderItem.getOrderDetailUuid());
//
//       fastRepaymentOrderItem.setDetailAliveStatus(1);
//
//       String updateSql = "update repayment_order_item set order_unique_id = :orderUniqueId where order_detail_uuid = :orderDetailUuid";
//       Map<String, Object> paramMap = new HashMap<>(2);
//       paramMap.put("orderUniqueId", "hello");
//       paramMap.put("orderDetailUuid", TestGenerator.ITEM_ORDER_DETAIL_UUID);
//       int count = fastHandler.update(updateSql, paramMap, fastRepaymentOrderItem);
//
//        Assert.assertEquals(0, count);
//
//        fastRepaymentOrderItem =
//        		fastHandler.getByKey(FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID,
//        				TestGenerator.ITEM_ORDER_DETAIL_UUID, FastRepaymentOrderItem.class, false);
//        Assert.assertNotEquals("hello", fastRepaymentOrderItem.getOrderUniqueId());
//	}

}
