package com.zufangbao.privilege.service;

import com.zufangbao.sun.entity.security.LinkPrivilegeButton;
import com.zufangbao.sun.entity.security.PrivilegeStrategy;
import com.zufangbao.sun.entity.security.SystemPrivilege;
import com.zufangbao.sun.yunxin.service.LinkPrivilegeButtonService;
import com.zufangbao.sun.yunxin.service.SystemPrivilegeService;
import cucumber.api.java.cs.A;
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
public class SystemPrivilegeServiceTest {

    @Autowired
    private SystemPrivilegeService systemPrivilegeService;

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findSystemPrivilegeBy_Success() {
        Long privilegeId = 1L;
        SystemPrivilege systemPrivilege = systemPrivilegeService.findSystemPrivilegeBy(privilegeId);
        Assert.assertNotNull(systemPrivilege);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findSystemPrivilegeBy_errorParams() {
        Long privilegeId = 0L;
        SystemPrivilege systemPrivilege = systemPrivilegeService.findSystemPrivilegeBy(privilegeId);
        Assert.assertNull(systemPrivilege);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findSystemPrivilegeByStrategy_Success() {
        SystemPrivilege systemPrivilege = systemPrivilegeService.findSystemPrivilegeByStrategy(PrivilegeStrategy.HAS_ACCESS);
        Assert.assertNotNull(systemPrivilege);
    }

}
