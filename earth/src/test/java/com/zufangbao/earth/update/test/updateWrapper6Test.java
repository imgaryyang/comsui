package com.zufangbao.earth.update.test;

import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.earth.update.wrapper.IUpdateWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

/**
 * Created by wq on 17-5-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"
})
@Transactional()
@Rollback(true)
public class updateWrapper6Test {
    @Autowired
    private IUpdateWrapper updateWrapper6;

    //单个扣款计划
    @Test
    @Sql("classpath:test/testUpdateWrapper6.sql")
    public void test(){
        try {
            UpdateWrapperModel model = new UpdateWrapperModel();
            model.setDeductPlanUuid("['b1581eb6-2509-4055-894b-1dc4d812d9cc','b1581eb6-2509-4055-894b-1dc4d812d9cc']");
            String insertSql= updateWrapper6.wrap(model);
            System.out.print("==="+insertSql+"===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //多个扣款计划
    //ToDo
    @Test
    @Sql("classpath:test/testUpdateWrapper6_manyDeduct.sql")
    public void test_manyDeduct(){
        try {
            UpdateWrapperModel model = new UpdateWrapperModel();
            model.setDeductPlanUuid("['b1581eb6-2509-4055-894b-1dc4d812d9cc','b1581eb6-2509-4055-894b-1dc4d812d9cc']");
            String insertSql= updateWrapper6.wrap(model);
            System.out.print("==="+insertSql+"===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
