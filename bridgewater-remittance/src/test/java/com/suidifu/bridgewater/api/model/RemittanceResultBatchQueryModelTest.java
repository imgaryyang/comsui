package com.suidifu.bridgewater.api.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by chengll on 17-4-7.
 */
public class RemittanceResultBatchQueryModelTest {


    private String oriRequestNo = "123456";

    @Test
    public void testIsValid_errorOriRequestNo() {
        RemittanceResultBatchQueryModel model = new RemittanceResultBatchQueryModel();
        Assert.assertFalse(model.isValid());
        Assert.assertEquals("请求列表中存在为空的原始放款请求号［oriRequestNo］！", model.getCheckFailedMsg());
    }

    @Test
    public void testIsValid_errorUniqueId() {
        RemittanceResultBatchQueryModel model = new RemittanceResultBatchQueryModel();
        model.setOriRequestNo(oriRequestNo);
        Assert.assertFalse(model.isValid());
        Assert.assertEquals("请求列表中存在为空的贷款合同唯一编号［uniqueId］！", model.getCheckFailedMsg());
    }
}
