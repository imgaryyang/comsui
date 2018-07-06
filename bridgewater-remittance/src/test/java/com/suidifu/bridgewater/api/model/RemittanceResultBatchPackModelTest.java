package com.suidifu.bridgewater.api.model;

import com.demo2do.core.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengll on 17-4-7.
 */
public class RemittanceResultBatchPackModelTest {


    /**
     * 功能代码
     */
    private String fn = "100011";

    /**
     * 当前批量请求编号
     */
    private String requestNo ="9999";

    /**
     * 校验失败信息
     */
    private String checkFailedMsg;

    /**
     * 封装批量查询对象
     */
    private List<RemittanceResultBatchQueryModel> remittanceResultBatchQueryModels ;

    @Test
    public void testIsValid_errorFn() {
        RemittanceResultBatchPackModel model = new RemittanceResultBatchPackModel();
        Assert.assertFalse(model.isValid());
        Assert.assertEquals("功能代码唯一标识［fn］,不能为空!", model.getCheckFailedMsg());
    }

    @Test
    public void testIsValid_errorRequestNo() {
        RemittanceResultBatchPackModel model = new RemittanceResultBatchPackModel();
        model.setFn(this.fn);
        Assert.assertFalse(model.isValid());
        Assert.assertEquals("批量请求唯一标识［requestNo］，不能为空！", model.getCheckFailedMsg());
    }

    @Test
    public void testIsValid_nullBatchQueryModelList() {
        RemittanceResultBatchPackModel model = new RemittanceResultBatchPackModel();
        model.setFn(this.fn);
        model.setRequestNo(this.requestNo);
        Assert.assertFalse(model.isValid());
        Assert.assertEquals("批量请求中请求信息 [remittanceResultBatchQueryModels] , 不能为空", model.getCheckFailedMsg());
    }

    @Test
    public void testIsValid_exsitNullDetailBatchQueryModelList() {
        List<RemittanceResultBatchQueryModel> queryModels = new ArrayList<>();
        RemittanceResultBatchQueryModel queryModel1 = new RemittanceResultBatchQueryModel();
        RemittanceResultBatchQueryModel queryModel2 = new RemittanceResultBatchQueryModel();
        queryModel1.setOriRequestNo("1111");
        queryModel1.setUniqueId("1111");
        queryModels.add(queryModel1);
        queryModel2.setOriRequestNo("2222");
        queryModel2.setUniqueId("2222");
        queryModels.add(queryModel2);
        String str = JsonUtils.toJsonString(queryModels);

        RemittanceResultBatchPackModel model = new RemittanceResultBatchPackModel();
        model.setFn(this.fn);
        model.setRequestNo(this.requestNo);
        model.setRemittanceResultBatchQueryModels(str);
        Assert.assertTrue(model.isValid());


        /*RemittanceResultBatchQueryModel queryModel3 = new RemittanceResultBatchQueryModel();
        queryModel3.setOriRequestNo("");
        queryModel3.setUniqueId("3333");
        queryModels.add(queryModel3);
        Assert.assertFalse(model.isValid());
        Assert.assertEquals("请求列表中存在为空的原始放款请求号［oriRequestNo］！", model.getCheckFailedMsg());
        RemittanceResultBatchQueryModel queryModel4 = new RemittanceResultBatchQueryModel();*/
      /*  queryModel4.setOriRequestNo("4444");
        queryModel4.setUniqueId("");
        queryModels.add(queryModel4);
        Assert.assertFalse(model.isValid());
        System.out.println(model.getCheckFailedMsg());
        Assert.assertEquals("请求列表中存在为空的贷款合同唯一编号［uniqueId］！", model.getCheckFailedMsg());*/
    }


}
