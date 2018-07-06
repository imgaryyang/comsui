package com.zufangbao.privilege.handler;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.yunxin.handler.SystemButtonHandler;
import com.zufangbao.sun.entity.security.SystemButton;
import com.zufangbao.sun.entity.security.SystemRoleQueryModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
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
public class SystemButtonHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    SystemButtonHandler systemButtonHandler;

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_checkAccessButtonsByMenuId_Success() {
        //对buttonIdList  通过父级菜单Id就行过滤,
        //存在buttonList 为空 menuId 对应的buttonList不为空
        //都不为空
        //存在buttonList不为空,menuId对应的buttonList不为空
        //操作: 取交集
        Long buttonId1 = 1L;
        Long buttonId2 = 2L;
        Long buttonId3 = 3L;
        Long menuId = 8L;
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(buttonId1);
        buttonIdList.add(buttonId2);
        buttonIdList.add(buttonId3);
        List<SystemButton> systemButtonList = systemButtonHandler.checkAccessButtonsByMenuId(buttonIdList, menuId);
        Assert.assertEquals(1,systemButtonList.size());
    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_checkAccessButtonsByMenuId_errorParams() {
        Long buttonId1 = 0L;
        Long menuId = 8L;
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(buttonId1);
        List<SystemButton> systemButtonList = systemButtonHandler.checkAccessButtonsByMenuId(buttonIdList, menuId);
        Assert.assertEquals(0,systemButtonList.size());
    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_getAuthByButtonId_Success() {
        Long buttonId1 = 1L;
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(buttonId1);
        List<String> auth = systemButtonHandler.getAuthByButtonId(buttonId1);
        logger.info("auth: " + auth);
        Assert.assertTrue(auth.size() > 0);
    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_getAuthByButtonId_errorParams() {
        Long buttonId1 = 0L;
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(buttonId1);
        List<String> auth = systemButtonHandler.getAuthByButtonId(buttonId1);
        logger.info("auth: " + auth);
        Assert.assertFalse(auth.size() > 0);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_getLinkPrivilegeButtonId_Success() {
     /* Long buttonId1 = 1L;
        Long buttonId2 = 2L;
        Long buttonId3 = 3L;
        Long menuId = 8L;
        List<Long> buttonIdList = new ArrayList<>();
        buttonIdList.add(buttonId1);
         buttonIdList.add(buttonId2);
        buttonIdList.add(buttonId3);*/
        Long pricipleId = 1L;
        List<Long> linkPrivilegeButtonId = systemButtonHandler.getLinkPrivilegeButtonId(pricipleId);
        logger.info("linkPrivilegeButtonIds: " + linkPrivilegeButtonId);
        Assert.assertTrue(linkPrivilegeButtonId.size() > 0);
    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_getLinkPrivilegeButtonId_errorParams() {
        Long pricipleId = 0L;
        List<Long> linkPrivilegeButtonId = systemButtonHandler.getLinkPrivilegeButtonId(pricipleId);
        logger.info("linkPrivilegeButtonIds: " + linkPrivilegeButtonId);
        Assert.assertFalse(linkPrivilegeButtonId.size() > 0);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_getSystemButtonIdsByPrivilege_Success() {
        List<Long> linkPrivilegeButton = new ArrayList<>();
        linkPrivilegeButton.add(1L);
        linkPrivilegeButton.add(2L);

        List<Long> buttonIdList = systemButtonHandler.getSystemButtonIdsByPrivilege(linkPrivilegeButton);
        logger.info("buttonIds: " + buttonIdList);
        Assert.assertTrue(buttonIdList.size() > 0);
    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_getSystemButtonIdsByPrivilege_errorParams() {
        List<Long> linkPrivilegeButton = new ArrayList<>();
        linkPrivilegeButton.add(-1L);
        linkPrivilegeButton.add(-2L);

        List<Long> buttonIdList = systemButtonHandler.getSystemButtonIdsByPrivilege(linkPrivilegeButton);
        logger.info("buttonIds: " + buttonIdList);
        Assert.assertFalse(buttonIdList.size() > 0);
    }
}
