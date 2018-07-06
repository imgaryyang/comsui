package giotto.service;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.handler.FastHandler;
import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.model.FastAssetSet;
import com.zufangbao.gluon.opensdk.Md5Util;
import giotto.BaseTest;
import giotto.TestGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * MD5UpdateTest
 *
 * @author whb
 * @date 2017/5/25
 */
@Transactional
@Rollback
public class MD5UpdateTest extends BaseTest {

    @Autowired private GenericDaoSupport genericDaoSupport;

    @Autowired private FastHandler fastHandler;

    @Test
    @Sql("classpath:test/t_common_insert.sql")
    public void testMD5Update() throws GiottoException {
        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID
                , TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, true);
        String updateMD5Sql = "update asset_set set asset_uuid = :val where id = :id and '" + getMd5Val(fastAssetSet).concat(
                "' = MD5(concat(asset_initial_value, asset_principal_value, asset_interest_value, asset_fair_value, asset_status, on_account_status, guarantee_status, settlement_status))");

        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("id", fastAssetSet.getId());
        long updateMD5Start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            paramMap.put("val", i+"");
            genericDaoSupport.executeSQL(updateMD5Sql, paramMap);
        }
        long updateMD5Diff = System.currentTimeMillis() - updateMD5Start;


        String updateSql = "update asset_set set asset_uuid = :val where id = :id";
        Map<String, Object> paramMap2 = new HashMap<>(2);
        paramMap2.put("id", fastAssetSet.getId());
        long updateStart = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            paramMap2.put("val", i);
            genericDaoSupport.executeSQL(updateSql, paramMap2);
        }
        long updateDiff = System.currentTimeMillis() - updateStart;

        System.out.println("update md5 execute : " + updateMD5Diff);
        System.out.println("update execute : " + updateDiff);

    }

    @Test
    @Sql("classpath:test/t_common_insert.sql")
    public void testMD5UpdateOne() throws GiottoException {
        FastAssetSet fastAssetSet = fastHandler.getByKey(FastAssetSetKeyEnum.ASSET_SET_UUID
                , TestGenerator.ASSET_UUID_EXSIT, FastAssetSet.class, true);
        String updateMD5Sql = "update asset_set set asset_uuid = :val where id = :id and '" + getMd5ValOne(fastAssetSet).concat(
                "' = MD5(asset_initial_value)");

        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("id", fastAssetSet.getId());
        long updateMD5Start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            paramMap.put("val", i+"");
            genericDaoSupport.executeSQL(updateMD5Sql, paramMap);
        }
        long updateMD5Diff = System.currentTimeMillis() - updateMD5Start;


        String updateSql = "update asset_set set asset_uuid = :val where id = :id";
        Map<String, Object> paramMap2 = new HashMap<>(2);
        paramMap2.put("id", fastAssetSet.getId());
        long updateStart = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            paramMap2.put("val", i);
            genericDaoSupport.executeSQL(updateSql, paramMap2);
        }
        long updateDiff = System.currentTimeMillis() - updateStart;

        System.out.println("update md5 execute : " + updateMD5Diff);
        System.out.println("update execute : " + updateDiff);

    }

    private String getMd5Val(FastAssetSet fastAssetSet) {
        String initialVal = fastAssetSet.getAssetInitialValue() == null ? "" : fastAssetSet.getAssetInitialValue().toString();
        String assetPriVal = fastAssetSet.getAssetPrincipalValue() == null ? "" : fastAssetSet.getAssetPrincipalValue().toString();
        String assetInterVal = fastAssetSet.getAssetInitialValue() == null ? "" : fastAssetSet.getAssetInitialValue().toString();
        String assetFairVal = fastAssetSet.getAssetFairValue() == null ? "" : fastAssetSet.getAssetFairValue().toString();
        String assetStatus = fastAssetSet.getAssetStatus() == null ? "" : fastAssetSet.getAssetStatus().toString();
        String assetAccoutStatus = fastAssetSet.getOnAccountStatus() == null ? "" : fastAssetSet.getOnAccountStatus().toString();
        String guarStatus = fastAssetSet.getGuaranteeStatus() == null ? "" : fastAssetSet.getGuaranteeStatus().toString();
        String setStatus = fastAssetSet.getSettlementStatus() == null ? "" : fastAssetSet.getSettlementStatus().toString();

        return Md5Util.encode(initialVal.concat(assetPriVal).concat(assetInterVal).concat(assetFairVal).concat(assetStatus)
            .concat(assetAccoutStatus).concat(guarStatus).concat(setStatus));
    }

    private String getMd5ValOne(FastAssetSet fastAssetSet) {
        String initialVal = fastAssetSet.getAssetInitialValue() == null ? "" : fastAssetSet.getAssetInitialValue().toString();
        return Md5Util.encode(initialVal);
    }

}