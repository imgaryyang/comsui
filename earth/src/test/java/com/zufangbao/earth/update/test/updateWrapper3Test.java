package com.zufangbao.earth.update.test;

import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.earth.update.wrapper.IUpdateWrapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

/**
 * Created by wq on 17-4-26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml"
})
@TransactionConfiguration(defaultRollback = false)
public class updateWrapper3Test {

    @Autowired
    private IUpdateWrapper updateWrapper3;

    @Test
    @Sql("classpath:test/testUpdateWrapper3.sql")
    public void test(){
        UpdateWrapperModel model=new UpdateWrapperModel();
        model.setValueAndFees("0,0,,");
        model.setSingleLoanContractNo("ZC1424829272737099776,ZC1564910339117215744");

        try {
            System.out.println(updateWrapper3.wrap(model));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
