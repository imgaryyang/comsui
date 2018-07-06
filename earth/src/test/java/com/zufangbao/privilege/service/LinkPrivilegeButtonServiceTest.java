package com.zufangbao.privilege.service;

import com.zufangbao.sun.entity.security.LinkPrivilegeButton;
import com.zufangbao.sun.yunxin.service.LinkPrivilegeButtonService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengll on 17-4-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class LinkPrivilegeButtonServiceTest {

    @Autowired
    private LinkPrivilegeButtonService linkPrivilegeButtonService;

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findLinkPrivilegeButton_Success() {
        Long buttonId = 1L;
        List<LinkPrivilegeButton> result = linkPrivilegeButtonService.findLinkPrivilegeButtonBy(buttonId);
        int size = result.size();
        Assert.assertTrue(size > 0);

    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_getLinkPrivilegeButtonBy() {
        Long buttonId = 1L;
        Long privilegeId = 1L;
        LinkPrivilegeButton result = linkPrivilegeButtonService.getLinkPrivilegeButtonBy(buttonId, privilegeId);
        Assert.assertNotNull(result);
    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_batchSaveLinkPrivilegeButton() {
        Long buttonId = 1L;
        Long buttonId2 = 2L;
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(buttonId);
        buttonIdList.add(buttonId2);
        Long privilegeId = 1L;
        boolean b = linkPrivilegeButtonService.batchSaveLinkPrivilegeButton(buttonIdList, privilegeId);
        Assert.assertTrue(b);
    }
}
