package giotto.service;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.model.FastAssetSet;
import com.suidifu.giotto.service.FastService;
import giotto.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * FastCacheServiceTest
 *
 * @author whb
 * @date 2017/7/26
 */
@Transactional
@Rollback
public class FastCacheServiceTest extends BaseTest {

    @Autowired
    @Qualifier(value = "fastCacheService")
    private FastService fastService;

    @Test
    public void testAdd() throws GiottoException {
        FastAssetSet fastAssetSet = new FastAssetSet();
        fastAssetSet.setAssetUuid(UUID.randomUUID().toString());
        fastAssetSet.setSingleLoanContractNo("abc");
        fastAssetSet.setActiveDeductApplicationUuid("uuid");
        fastService.add(fastAssetSet);
    }
}
