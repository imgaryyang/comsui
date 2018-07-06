package com.suidifu.morganstanley.servers;

import com.suidifu.morganstanley.exception.MorganStanleyException;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

/**
 * ${my_title}
 * @author louguanyang at 2017/8/16 14:07
 */
@javax.transaction.Transactional
@Rollback
public class FileRepositoryRedisPersistenceTest {

    @Autowired
    @Qualifier("FileRepositoryRedisPersistence")
    private FileRepositoryRedisPersistence fileRepositoryRedisPersistence;

    @Before
    public void purgePersistenceTestData() throws MorganStanleyException {
        fileRepositoryRedisPersistence.deleteAll("123456");
        fileRepositoryRedisPersistence.deleteAll("abcd");
    }


    @Test
    public void test_pushToTail_list() throws Exception {
        List<String> bizIdList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            bizIdList.add(i + "");
        }
        String key = "123456";
        fileRepositoryRedisPersistence.pushToTail(key, bizIdList);
        List<String> idList = fileRepositoryRedisPersistence.peekJobsFromHead(key);
        Assert.assertTrue(CollectionUtils.isNotEmpty(idList));
        Assert.assertEquals(1000, idList.size());

        String value = fileRepositoryRedisPersistence.get(key);
        Assert.assertEquals("0", value);
    }

    @Test
    public void test_pushToTail() throws Exception {
        String key = "abcd";
        String value = "123456";
        fileRepositoryRedisPersistence.pushToTail(key, value);
        String valueInRedis = fileRepositoryRedisPersistence.get(key);
        Assert.assertEquals(value, valueInRedis);
    }

}