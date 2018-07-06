package com.zufangbao.privilege.service;

import com.zufangbao.earth.yunxin.handler.SystemButtonHandler;
import com.zufangbao.sun.entity.security.PrivilegeStrategy;
import com.zufangbao.sun.entity.security.SystemButton;
import com.zufangbao.sun.entity.security.SystemPrivilege;
import com.zufangbao.sun.yunxin.service.SystemPrivilegeService;
import com.zufangbao.sun.yunxin.service.privilege.SystemButtonService;
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
public class SystemButtonServiceTest {

    @Autowired
    private SystemButtonService systemButtonService;

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findSystemButtonById_Success() {
        Long buttonId = 1L;
        SystemButton systemButton = systemButtonService.findSystemButtonById(buttonId);
        Assert.assertNotNull(systemButton);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findSystemButtonById_errorParams() {
        Long buttonId = 0L;
        SystemButton systemButton = systemButtonService.findSystemButtonById(buttonId);
        Assert.assertNull(systemButton);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findSystemButtonListByIds_Success() {
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(1L);
        buttonIdList.add(2L);
        List<SystemButton> systemButtonList = systemButtonService.findSystemButtonListByIds(buttonIdList);
        Assert.assertTrue(systemButtonList.size() == 2);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findSystemButtonListByIds_nullParams() {
        List<Long> buttonIdList = new ArrayList<>();
        List<SystemButton> systemButtonList = systemButtonService.findSystemButtonListByIds(buttonIdList);
        Assert.assertFalse(systemButtonList.size() > 0);
    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findSystemButtonListByIds_errorParams() {
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(0L);
        buttonIdList.add(3L);
        List<SystemButton> systemButtonList = systemButtonService.findSystemButtonListByIds(buttonIdList);
        Assert.assertFalse(systemButtonList.size() > 0);
    }


    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findMenuKeyListByButtonIds_success() {
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(1L);
        buttonIdList.add(2L);
        List<String> menuKeyList = systemButtonService.findMenuKeyListByButtonIds(buttonIdList);
        Assert.assertTrue(menuKeyList.size() == 2 );

    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findMenuKeyListByButtonIds_nullParams() {
        List<Long> buttonIdList = new ArrayList<>();
        List<String> menuKeyList = systemButtonService.findMenuKeyListByButtonIds(buttonIdList);
        Assert.assertFalse(menuKeyList.size() > 0);

    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_findMenuKeyListByButtonIds_errorParams() {
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(0L);
        buttonIdList.add(3L);
        List<String> menuKeyList = systemButtonService.findMenuKeyListByButtonIds(buttonIdList);
        Assert.assertFalse(menuKeyList.size() > 0);

    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_loadSystemButtonByMenuKey_success() {
        String menuKey = "submenu-assets-contract";
        List<SystemButton> systemButtons = systemButtonService.loadSystemButtonByMenuKey(menuKey);
        Assert.assertTrue(systemButtons.size() > 0);

    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_loadSystemButtonByMenuKey_errorParams() {
        String menuKey = "submenu-asset";
        List<SystemButton> systemButtons = systemButtonService.loadSystemButtonByMenuKey(menuKey);
        Assert.assertFalse(systemButtons.size() > 0);
    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_loadSystemButtonByMenuKey_nullParams() {
        String menuKey = "";
        List<SystemButton> systemButtons = systemButtonService.loadSystemButtonByMenuKey(menuKey);
        Assert.assertFalse(systemButtons.size() > 0);
    }


}
