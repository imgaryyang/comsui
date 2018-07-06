package com.zufangbao.earth.update.test;

import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.earth.update.wrapper.IUpdateWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * Created by wq on 17-5-3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"
})
public class updateWrapper5Test {
    @Autowired
    private IUpdateWrapper updateWrapper5;

    @Test
    @Sql("classpath:test/testUpdateWrapper5.sql")
    public void test(){
        try {
        String[] vounerNo = {
                "205ba8f9-3d23-4ca8-a007-26b8d6d911ae-02"
        };


        for (String voucherNo:vounerNo){
            UpdateWrapperModel model = new UpdateWrapperModel();
            model.setVoucherNo(voucherNo);
            System.out.print(updateWrapper5.wrap(model));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

