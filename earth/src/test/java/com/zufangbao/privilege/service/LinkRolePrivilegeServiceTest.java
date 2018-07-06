package com.zufangbao.privilege.service;

import com.zufangbao.sun.entity.security.LinkPrivilegeButton;
import com.zufangbao.sun.yunxin.service.LinkRolePrivilegeService;
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
public class LinkRolePrivilegeServiceTest {

    @Autowired
    LinkRolePrivilegeService linkRolePrivilegeService;

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findLinkPrivilegeButtonIds_Success() {
        Long roleId = 1L;
        List<Long> linkPrivilegeButtonIds = linkRolePrivilegeService.findLinkPrivilegeButtonIds(roleId);
        Assert.assertTrue(linkPrivilegeButtonIds.size() > 0);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_delLinkRolePrivilege_success() {
        Long roleId = 1L;
        boolean b = linkRolePrivilegeService.delLinkRolePrivilege(roleId);
        Assert.assertTrue(b);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_batchSaveLinkRolePrivilege_success() {
        Long roleId = 1L;
        List<Long> linkPrivilegeButtonIds = new ArrayList<>();
        linkPrivilegeButtonIds.add(1L);
        linkPrivilegeButtonIds.add(2L);
        boolean b = linkRolePrivilegeService.batchSaveLinkRolePrivilege(roleId, linkPrivilegeButtonIds);
        Assert.assertTrue(b);
    }
}
