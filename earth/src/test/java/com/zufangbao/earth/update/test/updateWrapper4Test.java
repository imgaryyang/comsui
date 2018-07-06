package com.zufangbao.earth.update.test;

import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.earth.update.wrapper.IUpdateWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by wq on 17-5-2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"
})
public class updateWrapper4Test {

    @Autowired
    private IUpdateWrapper updateWrapper4;

    @Test
    @Sql("classpath:test/testUpdateWrapper4.sql")
    public void test(){
        UpdateWrapperModel model=new UpdateWrapperModel();
        model.setContractNo("G00000");
        model.setOfflineBillNo("XX273138F8CE27B3A7");
        try {
            System.out.println(updateWrapper4.wrap(model));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
