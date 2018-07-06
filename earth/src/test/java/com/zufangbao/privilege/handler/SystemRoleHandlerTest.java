package com.zufangbao.privilege.handler;

import com.zufangbao.earth.yunxin.handler.SystemButtonHandler;
import com.zufangbao.earth.yunxin.handler.SystemRoleHandler;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chengll on 17-4-17.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml" })
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SystemRoleHandlerTest {

    @Autowired
    SystemRoleHandler systemRoleHandler;

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_createSystemRole_Success() {
        //这里只有roleName 不能为空
        String roleName = "test_role";
        String roleRemark = "备注";
        String menuIds = "1,2";
        String buttonIds = "1,2";

        boolean isCreated = systemRoleHandler.createSystemRole(roleName, roleRemark, menuIds, buttonIds);

        Assert.assertTrue(isCreated);

    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_delSystemRoleRelation_Success() {
        boolean b = systemRoleHandler.delSystemRoleRelation(1L);
        Assert.assertTrue(b);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_delSystemRoleRelation_errorParam() {
        boolean b = systemRoleHandler.delSystemRoleRelation(0L);
        Assert.assertFalse(b);
    }

    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_editSystemRole_success() {
        Long roleId = 1L;
        String roleName = "test";
        String roleRemark = "";
        String menuIds = "";
        String buttonIds= "";
        boolean b = systemRoleHandler.editSystemRole(roleId,roleName,roleRemark,menuIds,buttonIds);
        Assert.assertTrue(b);
    }
    @Test
    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
    public void test_editSystemRole_errorParam() {
        Long roleId = 1L;
        String roleName = "ROLE_SUPER_USER"; //重复,为空等情况
        String roleRemark = "";
        String menuIds = "";
        String buttonIds= "";
        boolean b = systemRoleHandler.editSystemRole(roleId,roleName,roleRemark,menuIds,buttonIds);
        Assert.assertFalse(b);
    }

}
