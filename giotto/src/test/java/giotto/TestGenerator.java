package giotto;

import com.suidifu.giotto.keyenum.FastAssetSetKeyEnum;
import com.suidifu.giotto.keyenum.FastContractKeyEnum;
import com.suidifu.giotto.keyenum.FastCustomerKeyEnum;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * TestGenerator
 *
 * @author whb
 * @date 2017/5/24
 */

public class TestGenerator {

    public static final String ASSET_UUID_EXSIT = "asset_set_uuid_1";
    public static final String ASSET_UUID_NOT_EXSIT = "not_asset_set_uuid_1";
    public static final String ASSET_SNOW_FLAKE_ID_EXSIT = "DK_00101111";
    public static final String ASSET_SNOW_FLAKKE_ID_NOT_EXSIT = "NOT_DK_00101111";

    public static final String CONTRACT_NO_EXSIT = "TEST_DKHD-001";
    public static final String CONTRACT_NO_NOT_EXSIT = "NOT_TEST_DKHD-001";
    public static final String CONTRACT_UNIQUEID_EXSIT = "test_contract_unique_1";
    public static final String CONTRACT_UNIQUEID_NOT_EXSIT = "not_test_contract_unique_1";
    public static final String CONTRACT_UUID_EXSIT = "test_contract_uuid_1";
    public static final String CONTRACT_UUID_NOT_EXSIT = "not_test_contract_uuid_1";

    public static final String CUSTOMER_UUID_EXSIT = "1111db75ebb24fa0993f4fa25775023";
    public static final String CUSTOMER_UUID_NOT_EXSIT = "1111db75ebb24fa0993f4fa_NOT";
    public static final String CUSTOMER_ACCOUNT_EXSIT = "1234567899";
    public static final String CUSTOMER_ACCOUNT_NOT_EXSIT = "12345678999";


    public static final String ITEM_ORDER_DETAIL_UUID = "a01466bd-572a-4534-beb6-e6ed8926ac67";
    public static final String ITEM_ORDER_UUID = "8995491f-bf6b-414d-a055-163fc92ce0f4";
    public static final String ITEM_MER_ID = "mer_id";


    public static void delRedisKey(StringRedisTemplate redisTemplate) {
        Set<String> fastAssetKeySet = redisTemplate.keys(FastAssetSetKeyEnum.PREFIX_KEY.concat("*"));
        redisTemplate.delete(fastAssetKeySet);

        Set<String> fastContractKeySet = redisTemplate.keys(FastContractKeyEnum.PREFIX_KEY.concat("*"));
        redisTemplate.delete(fastContractKeySet);

        Set<String> fastCustomerKeySet = redisTemplate.keys(FastCustomerKeyEnum.PREFIX_KEY.concat("*"));
        redisTemplate.delete(fastCustomerKeySet);

        Set<String> itemKeySet = redisTemplate.keys(FastRepaymentOrderItemKeyEnum.PREFIX_KEY.concat("*"));
        redisTemplate.delete(itemKeySet);
    }
}
