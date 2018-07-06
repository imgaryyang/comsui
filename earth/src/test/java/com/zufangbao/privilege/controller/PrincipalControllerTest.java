//package com.zufangbao.privilege.controller;
//
///**
// * Created by chengll on 17-4-17.
// */
//
//import javax.annotation.Resource;
//import javax.transaction.Transactional;
//
//import com.demo2do.core.entity.Result;
//import com.demo2do.core.utils.JsonUtils;
//import com.zufangbao.earth.web.controller.system.PrincipalController;
//import com.zufangbao.sun.entity.security.SystemRoleQueryModel;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import com.demo2do.core.persistence.GenericDaoSupport;
//import com.zufangbao.sun.entity.security.Principal;
//import com.zufangbao.earth.service.PrincipalService;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={
//
//        "classpath:/local/applicationContext-*.xml",
//        "classpath:/DispatcherServlet.xml"
//
//})
//@Transactional
//@TransactionConfiguration(defaultRollback = true)
//public class PrincipalControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
//    @Resource
//    private PrincipalController principalController;
//    @Resource
//    private GenericDaoSupport genericDaoSupport;
//    @Resource
//    private PrincipalService principalService;
//
//    private static Log logger = LogFactory.getLog(PrincipalControllerTest.class);
//
//    @Test
//    @Sql("classpath:test/yunxin/principal/before_create_role.sql")
//    public void testBeforeCreateSystemRole_Success() {
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        String message = principalController.toCreateSystemRole();
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("0", result.getCode());
//    }
//
//
//
//
//    //http://localhost:9090/create-systemrole?nameroleName=test&roleRemark=test2&menuIds=8,9&buttonIds=1,2
//
//    @Test
//    @Sql("classpath:test/yunxin/principal/ready_create_role.sql")
//    public void createSystemRole_Success() {
//
//        String roleName = "TEST_ROLE";
//        String roleRemark = "备注信息";
//        SystemRoleQueryModel queryModel= new SystemRoleQueryModel();
//        queryModel.setRoleName(roleName);
//        queryModel.setRoleRemark(roleRemark);
//        //传入不存的menuIds
//        String menuIds = "8,9";
//        String buttonIds = "1";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        String message = principalController.createRole(queryModel, menuIds, buttonIds);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("0", result.getCode());
//    }
//    @Test
//    @Sql("classpath:test/yunxin/principal/ready_create_role.sql")
//    public void createSystemRole_nullParams() {
//        String roleName = "";
//        //备注信息可以为空
//        String roleRemark = "";
//        SystemRoleQueryModel queryModel= new SystemRoleQueryModel();
//        queryModel.setRoleName(roleName);
//        queryModel.setRoleRemark(roleRemark);
//        String menuIds = "";
//        String buttonIds =  "";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        String message = principalController.createRole(queryModel, menuIds, buttonIds);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("-1", result.getCode());
//    }
//    @Test
//    @Sql("classpath:test/yunxin/principal/ready_create_role.sql")
//    public void createSystemRole_errorParams() {
//
//        String roleName = "TEST_ROLE";
//        String roleRemark = "备注信息";
//        SystemRoleQueryModel queryModel= new SystemRoleQueryModel();
//        queryModel.setRoleName(roleName);
//        queryModel.setRoleRemark(roleRemark);
//        //传入不存的menuIds
//        String menuIds = "5,6";
//        String buttonIds = "1";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        String message = principalController.createRole(queryModel, menuIds, buttonIds);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("-1", result.getCode());
//    }
//
//
//    //http://localhost:9090/before-edit-systemrole?name=zhanghongbing&id=1
//    @Test
//    @Sql("classpath:test/yunxin/principal/before_edit_role.sql")
//    public void testBeforeEditSystemRole_Success() {
//        String name = "zhanghongbing";
//        String id = "1";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        Principal principal= new Principal();
//        principal.setName(name);
//        String message = principalController.editRole(principal, id);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("0", result.getCode());
//    }
//    @Test
//    @Sql("classpath:test/yunxin/principal/before_edit_role.sql")
//    public void testBeforeEditSystemRole_nullParams() {
//        String name = "";
//        String id = "1";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        Principal principal= new Principal();
//        principal.setName(name);
//        String message = principalController.editRole(principal, id);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("-1", result.getCode());
//    }
//    @Test
//    @Sql("classpath:test/yunxin/principal/before_edit_role.sql")
//    public void testBeforeEditSystemRole_errorParams() {
//        String name = "dsfds";
//        String id = "2";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        Principal principal= new Principal();
//        principal.setName(name);
//        String message = principalController.editRole(principal, id);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("-1", result.getCode());
//    }
//
//    //http://localhost:9090/edit-systemrole?name=zhanghongbing&id=2&roleName=TEST_USER_ROLE&roleRemark=test2&menuIds=8&buttonIds=1
//    @Test
//    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
//    public void testEditSystemRole_Success() {
//        String name = "zhanghongbing";
//        String id = "2";
//        String menuIds = "8";
//        String buttonIds = "1";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        Principal principal= new Principal();
//        principal.setName(name);
//        SystemRoleQueryModel queryModel = new SystemRoleQueryModel();
//        queryModel.setId(id);
//        queryModel.setRoleName("TEST_USER_ROLE");
//        //备注可以为空
//        queryModel.setRoleRemark("");
//
//        String message = principalController.editRole(queryModel, menuIds, buttonIds);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("0", result.getCode());
//    }
//    @Test
//    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
//    public void testEditSystemRole_nullParams() {
//        //不为空
//        String name = "zhanghongbing";
//        //不为空
//        String id = "";
//       /*
//        1.menuIds和buttonIds可以同时为空
//        2.menuIds不为空时,buttonIds可以为空
//        3.buttonIds不为空时,必定存在menuIds (最后一项,并没有处理,后台代码会通过buttonIds查找相应的menuIds)
//        */
//        String menuIds = "8";
//        String buttonIds = "1";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        Principal principal= new Principal();
//        principal.setName(name);
//        SystemRoleQueryModel queryModel = new SystemRoleQueryModel();
//        //不为空
//        queryModel.setId(id);
//        //不为空
//        queryModel.setRoleName("TEST_USER_ROLE");
//        //备注可以为空
//        queryModel.setRoleRemark("");
//
//        String message = principalController.editRole(queryModel, menuIds, buttonIds);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("-1", result.getCode());
//    }
//    @Test
//    @Sql("classpath:test/yunxin/principal/ready_edit_role.sql")
//    public void testEditSystemRole_errorParams() {
//        String name = "zhanghongbing";
//        String id = "2";
//        String menuIds = "8";
//        //不存在的buttonId
//        String buttonIds = "100";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//
//        Principal principal= new Principal();
//        principal.setName(name);
//        SystemRoleQueryModel queryModel = new SystemRoleQueryModel();
//        queryModel.setId(id);
//        queryModel.setRoleName("TEST_USER_ROLE");
//        queryModel.setRoleRemark("");
//
//        String message = principalController.editRole(queryModel, menuIds, buttonIds);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("0", result.getCode());
//    }
//
//
//
//
//    @Test
//    @Sql("classpath:test/yunxin/principal/ready_del_role.sql")
//    public void testDelSystemRole_Success() {
//        String name = "zhanghongbing";
//        String roleId = "1";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        Principal principal= new Principal();
//        principal.setName(name);
//        String message = principalController.delRole(principal, roleId);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("0", result.getCode());
//    }
//    @Test
//    @Sql("classpath:test/yunxin/principal/ready_get_auth_buttons.sql")
//    public void get_auth_buttons() {
//        String menuKey = "submenu-assets-contract";
//        String name  = "zhanghongbing";
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        Principal principal= new Principal();
//        principal.setName(name);
//        String message = principalController.getAuthButtonsBy(name, menuKey);
//        logger.info(message);
//        Result result = JsonUtils.parse(message, Result.class);
//        Assert.assertEquals("0", result.getCode());
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//}
//
