
package com.zufangbao.earth.web.controller.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.PrincipalInfoModel;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.service.SecretKeyService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.SystemButtonHandler;
import com.zufangbao.earth.yunxin.handler.SystemRoleHandler;
import com.zufangbao.gluon.spec.global.GlobalMsgSpec;
import com.zufangbao.sun.entity.company.Company;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractType;
import com.zufangbao.sun.entity.security.LinkRoleMenu;
import com.zufangbao.sun.entity.security.LinkRolePrincipal;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.security.SystemButton;
import com.zufangbao.sun.entity.security.SystemButtonForm;
import com.zufangbao.sun.entity.security.SystemMenu;
import com.zufangbao.sun.entity.security.SystemMenuForm;
import com.zufangbao.sun.entity.security.SystemRole;
import com.zufangbao.sun.entity.security.SystemRoleForm;
import com.zufangbao.sun.entity.security.SystemRoleQueryModel;
import com.zufangbao.sun.entity.security.SystemRoleState;
import com.zufangbao.sun.entity.security.UserGroup;
import com.zufangbao.sun.entity.security.UserManageForm;
import com.zufangbao.sun.entity.user.TUser;
import com.zufangbao.sun.service.AppService;
import com.zufangbao.sun.service.CompanyService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.model.PrincipalDetailShowModel;
import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractQueryModel;
import com.zufangbao.sun.yunxin.entity.model.financialcontract.FinancialContractShowModel;
import com.zufangbao.sun.yunxin.entity.model.principal.PrincipalQueryModel;
import com.zufangbao.sun.yunxin.service.LinkRoleMenuService;
import com.zufangbao.sun.yunxin.service.LinkRolePrincipalService;
import com.zufangbao.sun.yunxin.service.LinkRolePrivilegeService;
import com.zufangbao.sun.yunxin.service.SystemMenuService;
import com.zufangbao.sun.yunxin.service.SystemRoleService;
import com.zufangbao.sun.yunxin.service.TUserService;
import com.zufangbao.sun.yunxin.service.UserGroupService;
import com.zufangbao.sun.yunxin.service.privilege.SystemButtonService;

/**
 * @author louguanyang
 */
@Controller
@MenuSetting("menu-system")
public class PrincipalController extends BaseController {

  private static final Log logger = LogFactory.getLog(PrincipalController.class);
  private static final String COMPANY_LIST = "companyList";
  private static final String GROUP_LIST = "groupList";
  /**
   * ModelAndView name index
   */
  private static final String VIEW_NAME_INDEX = "index";
  private static final String USER_NOT_LOGIN = "用户未登录";
  private static final String USER_NAME_EMPTY = "username 不能为空";
  private static final String AUTHORITY_ERROR = GlobalMsgSpec.GeneralErrorMsg.MSG_NO_AUTHORITY;
  private static final String USER_NOT_EXIST = "用户不存在";
  private static final String SYSTEM_ERROR = "系统错误";
  private static final String MENUS = "menus";
  private static final String RESOURCES = "resources";
  private static final String ROLES = "roles";
  private static final String SYSTEM_ROLE_EXIST = "角色名已存在";
  @Autowired
  private PrincipalService principalService;
  @Autowired
  private CompanyService companyService;
  @Autowired
  private SystemRoleService systemRoleService;
  @Autowired
  private SystemMenuService systemMenuService;
  @Autowired
  private UserGroupService userGroupService;
  @Autowired
  private LinkRoleMenuService linkRoleMenuService;
  @Autowired
  private LinkRolePrincipalService linkRolePrincipalService;
  @Autowired
  private PrincipalHandler principalHandler;
  @Autowired
  public FinancialContractService financialContractService;
  @Autowired
  private TUserService tUserService;
  @Autowired
  private AppService appService;
  @Autowired
  private CacheManager cacheManager;
  @Autowired
  private SystemButtonHandler systemButtonHandler;
  @Autowired
  private SystemButtonService systemButtonService;
  @Autowired
  private SystemRoleHandler systemRoleHandler;
  @Autowired
  private LinkRolePrivilegeService linkRolePrivilegeService;
  @Autowired
  private SecretKeyService secretKeyService;

  @MenuSetting("submenu-update-password")
  @RequestMapping("/post-update-password")
  public ModelAndView postToUpdatePasswordPage(@Secure Principal principal) {
    return new ModelAndView(VIEW_NAME_INDEX);
  }

  private static final String RESULT_OF_UPDATE_SUCCESS = "修改成功";

  @RequestMapping(value = "/update-password", method = RequestMethod.POST)
  public @ResponseBody
  String updatePassword(@Secure Principal principal, String oldPassword, String newPassword) {
    String result = this.principalService.updatePassword(principal, oldPassword, newPassword);
    if (RESULT_OF_UPDATE_SUCCESS.equals(result)) {
      return jsonViewResolver.sucJsonResult();
    }
    return jsonViewResolver.errorJsonResult(result);
  }
  @RequestMapping(value="/show-principal",method=RequestMethod.GET)
	public @ResponseBody String showPrincipal(@Secure Principal principal) {
		TUser tUser = principalHandler.getTUser(principal);
		PrincipalDetailShowModel detailModel = new PrincipalDetailShowModel(tUser);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("detailModel", detailModel);
		return jsonViewResolver.sucJsonResult(result);
	}


	@RequestMapping(value="/update-principal",method=RequestMethod.POST)
	public @ResponseBody String updatePrincipal(@Secure Principal principal, @ModelAttribute PrincipalDetailShowModel principalDetailShowModel) {
		TUser tUser = principalHandler.getTUser(principal);
		String result = this.principalService.updatePrincipal(tUser,principalDetailShowModel);
		if (result.equals("修改成功"))
			return jsonViewResolver.sucJsonResult();
		else
			return jsonViewResolver.errorJsonResult(result);
	}

  /**
   * http://localhost:9090/create-user-role
   *
   * @return company, group companyList(id, fullName) groupList(id, groupName)
   */
  @RequestMapping(value = "create-user-role", method = RequestMethod.GET)
  public ModelAndView postToCreateUserRole() {
    try {
      ModelAndView result = new ModelAndView("principal/create-user-role");
      List<Company> companyList = companyService.getAllCompany();
      List<UserGroup> userGroups = userGroupService.getAllUserGroup();
      result.addObject(COMPANY_LIST, companyList);
      result.addObject(GROUP_LIST, userGroups);
      return result;
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return pageViewResolver.errorSpec();
    }
  }

  @RequestMapping(value = "getGroupList", method = RequestMethod.GET)
  public @ResponseBody
  String getGroupList() {
    try {
      Map<String, Object> result = new HashMap<>(2);
      List<UserGroup> userGroups = userGroupService.getAllUserGroup();
      result.put(GROUP_LIST, userGroups);
      return jsonViewResolver.sucJsonResult(result);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  @RequestMapping(value = "getCompanyList", method = RequestMethod.GET)
  public @ResponseBody
  String getCompanyList() {
    try {
      Map<String, Object> result = new HashMap<>();
      List<Company> companyList = companyService.getAllCompany();
      result.put(COMPANY_LIST, companyList);
      return jsonViewResolver.sucJsonResult(result);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/show-user-role/42<p>
   *
   * @return 用户名:		principal.name<p> 真实名字:		principal.tUser.name<p> 联系邮箱:		principal.tUser.email<p>
   * 联系电话:		principal.tUser.phone<p> 所属公司:		principal.tUser.company.fullName<p>
   * 备注:			principal.tUser.remark<p> 新增分组名:	principal.tUser.userGroup.groupName<p>
   * 分组id:		principal.tUser.userGroup.id<p> 权限:			auth<p> 公司数据:		companyList()<p>
   * 分组数据:		groupList<p>
   */
  @RequestMapping(value = "show-user-role/{principalId}", method = RequestMethod.GET)
  public @ResponseBody
  String showUserRole(@Secure Principal currentPrincipal,
      @PathVariable(value = "principalId") Long principalId) {
    try {
      if (currentPrincipal == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
//      if (!currentPrincipal.getId().equals(principalId) && !currentPrincipal.is_super_user_role()) {
//        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
//      }

      Map<String, Object> map = new HashMap<>();
      Map<String, Object> roleList = new HashMap<>();

      List<Company> companyList = companyService.getAllCompany();
      List<UserGroup> userGroups = userGroupService.getAllUserGroup();
      List<App> appList = principalService.get_can_access_app_list(currentPrincipal);
      // 角色
      List<SystemRole> systemRoles = systemRoleService.getUndeletedSystemRole();
      List<LinkRolePrincipal> roles = linkRolePrincipalService.getRolesBy(principalId);
      List<Long> roleIds = new ArrayList<>();
      roles.forEach(r -> roleIds.add(r.getRoleId()));

      for (SystemRole systemRole : systemRoles) {
        SystemRoleForm systemRoleForm = systemRole.toSystemRoleForm();
        boolean isContains = roleIds.contains(systemRole.getId());
        systemRoleForm.setChecked(isContains);
        roleList.put(systemRole.getRoleName(), systemRoleForm);
      }

      Principal principalInDb = principalService.getPrincipalById(principalId);
      int count = secretKeyService.countSecretKeyBy(principalId);
      map.put("count", count);
      map.put(COMPANY_LIST, companyList);
      map.put(GROUP_LIST, userGroups);
      map.put("appList", appList);
      map.put("financialContractTypeList", EnumUtil.getKVList(FinancialContractType.class));
      map.put("auth", roleList);
      // 避免兰查询的数据删除导致的js报错, jsp不报错但不会有principal数据传到前台.
      
      map.put("principal", principalInDb.principal4js());
      return jsonViewResolver.sucJsonResult(map);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/create-user-role<p> POST: username=fenglei&realname=%E5%86%AF%E7%A3%8A&email=shuxuejia0805%40126.com&phone=13073603650&companyId=6&groupId=1&remark=for+test<p>
   *
   * @return "创建用户失败，请联系管理员！"/"创建用户成功"并返回初始密码<p> isValidPrincipalInfo方法返回结果<p>
   * "请输入格式有效的用户名！"/"用户名已存在！"/"请输入真实名字！"
   */
  @RequestMapping(value = "create-user-role", method = RequestMethod.POST)
  public @ResponseBody
  String createUserRole(
      @ModelAttribute PrincipalInfoModel principalInfoModel,
      @Secure Principal creator
  ) {
    try {
      Map<String, Object> map = new HashMap<>();
      Result result = new Result();
      result = isValidPrincipalInfo(principalInfoModel, result, false);
      if (!result.isValid()) {
        return JsonUtils.toJsonString(result);
      }
      String originPassword = principalHandler.createPrincipal(principalInfoModel, creator.getId());
      Principal principal = principalService.getPrincipal(principalInfoModel.getUsername());

      map.put("pwd", originPassword);
      map.put("newprincipal", principal);
      return jsonViewResolver.sucJsonResult(map);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult("创建用户失败，请联系管理员");
    }
  }

  /**
   * http://localhost:9090/link-user-role?roleIds=1,2,3,9&principalid=42
   *
   * @return "用户已删除"/"至少选择一项"/"用户角色更改成功"/"系统错误"<p> 避免创建已删除或不存在的角色
   */
  @RequestMapping(value = "link-user-role", method = RequestMethod.GET)
  public @ResponseBody
  String linkUserRole(
      @Secure Principal currentPrincipal,
      @RequestParam("roleIds") String roleIds,
      @RequestParam("principalId") String principalid
  ) {
    try {
      if (currentPrincipal == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
      Long principalId = Long.parseLong(principalid);
//      if (!currentPrincipal.getId().equals(principalId) && !currentPrincipal.is_super_user_role()) {
//        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
//      }
      if (roleIds.length() == 0) {
        return jsonViewResolver.errorJsonResult("至少选择一项");
      }
      StringBuilder sb = new StringBuilder();
      linkRolePrincipalService.delLinkPrincipal(principalId);
      String regex = ",";
      String[] ids = roleIds.split(regex);
      for (String id : ids) {
        Long longId = Long.parseLong(id);
        SystemRole systemRole = systemRoleService.getSystemRoleBy(longId);
        if (systemRole == null || systemRole.getRoleState() == SystemRoleState.HALT) {
          continue;
        }
        if (sb.length() == 0) {
          sb.append(systemRole.getRoleName());
        } else {
          sb.append(regex).append(systemRole.getRoleName());
        }
        linkRolePrincipalService.newLinkRolePrincipal(principalId, longId);
      }
      Principal principalInDB = principalService.getPrincipalById(principalId);
      principalInDB.setAuthority(sb.toString());
      principalService.update(principalInDB);
      return jsonViewResolver.jsonResult("用户角色更改成功");
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/edit-user-role<p> POST: principalId=42&realname=%E5%86%AF%E7%A3%8A&email=larryleifeng%40163.com&phone=13073603650&companyId=6&groupId=1&remark=for+test
   *
   * @return "编辑用户成功！"/"编辑用户失败，请联系管理员！"<p> isValidPrincipalInfo的返回信息<p> "请输入格式有效的用户名！"/"用户名已存在！"/"请输入真实名字！"
   */
  @RequestMapping(value = "edit-user-role", method = RequestMethod.POST)
  public @ResponseBody
  String editUserRole(
      @ModelAttribute PrincipalInfoModel principalInfoModel,
      @Secure Principal creator
  ) {
    try {
      Result result = new Result();
      result = isValidPrincipalInfo(principalInfoModel, result, true);
      if (!result.isValid()) {
        return JsonUtils.toJsonString(result);
      }
      principalHandler.updatePrincipal(principalInfoModel, creator.getId());
      return JsonUtils.toJsonString(result.success().message("编辑用户成功！"));
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult("编辑用户失败，请联系管理员");
    }
  }

  /**
   * http://localhost:9090/create-group?name=zhenghangbo&groupName=test2
   *
   * @return "分组名无效"/"分组已存在"/"成功"/"系统错误"
   */
  @RequestMapping(value = "/create-group", method = RequestMethod.GET)
  public @ResponseBody
  String createGroup(@Secure Principal currentPrincipal,
      @RequestParam("groupName") String groupName) {
    try {
      if (StringUtils.isEmpty(groupName)) {
        return jsonViewResolver.errorJsonResult("分组名无效");
      }
      if (userGroupService.getUserGroupBy(groupName) != null) {
        return jsonViewResolver.errorJsonResult("分组已存在");
      }
      userGroupService.createUserGroup(groupName);
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/del-group?name=zhenghangbo&groupName=test2
   *
   * @return "分组名无效"/"该分组已有用户选中，不可删除"/"分组不存在或已删除"/"成功"/"系统错误"
   */
  @RequestMapping(value = "/del-group", method = RequestMethod.GET)
  public @ResponseBody
  String delGroup(@Secure Principal currentPrincipal, @RequestParam("groupName") String groupName) {
    try {
      if (StringUtils.isEmpty(groupName)) {
        return jsonViewResolver.errorJsonResult("分组名无效");
      }
      if (userGroupService.userGroupInUse(groupName)) {
        return jsonViewResolver.errorJsonResult("该分组已有用户选中，不可删除");
      }
      if (userGroupService.getUserGroupBy(groupName) == null) {
        return jsonViewResolver.errorJsonResult("分组不存在或已删除");
      }
      userGroupService.delUserGroup(groupName);
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/create-company?name=zhenghangbo&fullName=test
   *
   * @return "成功"/"公司名无效"/"公司已存在"/"系统错误"
   */
  @RequestMapping(value = "/create-company", method = RequestMethod.GET)
  public @ResponseBody
  String createCompany(@Secure Principal currentPrincipal,
      @RequestParam("fullName") String fullName) {
    try {
      if (StringUtils.isEmpty(fullName.trim())) {
        return jsonViewResolver.errorJsonResult("公司名无效");
      }
      if (companyService.getCompanyByname(fullName) != null) {
        return jsonViewResolver.errorJsonResult("公司已存在");
      }
      companyService.createCompany(fullName);
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/del-company?name=zhenghangbo&fullName=test
   *
   * @return "公司不存在已删除"/"公司名无效"/"该公司已有用户选中，不可删除"/"成功"/"系统错误"
   */
  @RequestMapping(value = "/del-company", method = RequestMethod.GET)
  public @ResponseBody
  String delCompany(@Secure Principal principal, @RequestParam("fullName") String fullName) {
    try {
      if (StringUtils.isEmpty(fullName.trim())) {
        return jsonViewResolver.errorJsonResult("公司名无效");
      }
      Company company = companyService.getCompanyByname(fullName);
      if (company == null) {
        return jsonViewResolver.errorJsonResult("公司不存在已删除");
      }
      if (companyService.userCompanyInUse(fullName)) {
        return jsonViewResolver.errorJsonResult("该公司已有用户选中，不可删除");
      }
      if (appService.existsByCompany(fullName)) {
        return jsonViewResolver.errorJsonResult("该公司不可删除");
      }
      companyService.delCompany(fullName);
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/before-create-systemrole?name=louguanyang
   *
   * @return list: 菜单树状结构(json字符串)<p> 选中的菜单向后台传递id值字符串, 如选中(id1, menu1), (id2, menu2)...<p> 则传值为:
   * id1,id2,...字符串, 注意: 逗号后不雅有空格<p> 复选框的value取id值<p>
   */
  @RequestMapping(value = "/before-create-systemrole", method = RequestMethod.GET)
  public @ResponseBody
  String toCreateSystemRole() {
    try {
      Map<String, Object> map = new HashMap<>();
      List<SystemMenu> systemMenus = systemMenuService.loadAllMenus();
      List<SystemButton> systemButtons = systemButtonService.loadAllButtons();
      List<SystemMenuForm> menus = new ArrayList<>();
      List<SystemButtonForm> buttons = new ArrayList<>();
      // 创建时菜单全部不选中
      systemMenus.forEach(m -> menus.add(m.toSystemMenuForm(false)));
      //创建时按钮也默认不选中
      systemButtons.forEach(b -> buttons.add(b.toSystemButtonForm(false)));
      map.put(MENUS, menus);
      map.put("buttons", buttons);
      return jsonViewResolver.sucJsonResult(map);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/create-systemrole?name=zhenghangbo&roleName=test&roleRemark=test2&menuIds=1,2,3,4,4Í5
   *
   * @return "角色名已存在"/"成功"/"系统错误"<p> 注意: 当树的level级菜单添加后, 对应的父菜单也会添加到角色所属权限中,
   * 以保证header/aside两个jsp文件的正常显示<p> 选中的菜单向后台传递id值字符串, 如选中(id1, menu1), (id2, menu2)...<p> 则传值为:
   * id1,id2,...字符串, 注意: 逗号后不雅有空格
   */
  @RequestMapping(value = "/create-systemrole", method = RequestMethod.GET)
  public @ResponseBody
  String createRole(@ModelAttribute SystemRoleQueryModel systemRoleQueryModel,
      @RequestParam("menuIds") String menuIds, @RequestParam("buttonIds") String buttonIds) {
    try {
      String roleName;
      String roleRemark = "";
      if (null == systemRoleQueryModel) {
        return jsonViewResolver.errorJsonResult("请求的角色信息为空");
      }
      roleName = systemRoleQueryModel.getRoleName();
      if (StringUtils.isEmpty(roleName)) {
        return jsonViewResolver.errorJsonResult("请求的角色名称为空");
      }
      SystemRole systemRole = systemRoleService.getSystemRoleBy(roleName);
      if (systemRole != null) {
        if (systemRole.getRoleState() == SystemRoleState.REGULAR) {
          return jsonViewResolver.errorJsonResult(SYSTEM_ROLE_EXIST);
        }
        if (systemRoleService.recoverSystemRole(systemRole.getId())) {
          cacheManager.getCache(RESOURCES).clear();
          cacheManager.getCache(ROLES).clear();
          return jsonViewResolver.sucJsonResult();
        }
        return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
      }
      if (systemRoleHandler.createSystemRole(roleName, roleRemark, menuIds, buttonIds)) {
        cacheManager.getCache(RESOURCES).clear();
        cacheManager.getCache(ROLES).clear();
        return jsonViewResolver.sucJsonResult();
      }
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/before-edit-systemrole?name=zhenghangbo&id=1
   *
   * @return 角色名:	data.roleName<p> 角色备注:	data.roleRemark<p> 菜单:		list(树形数据结构, check为true的复选框选中),
   * 复选框的value为id<p> id传入的是字符串<p>
   */
  @RequestMapping(value = "/before-edit-systemrole", method = RequestMethod.GET)
  public @ResponseBody
  String editRole(@Secure Principal principal, @RequestParam("id") String id) {
    try {
      Map<String, Object> map = new HashMap<>();
      if (null == principal) {
        return jsonViewResolver.errorJsonResult("账户不能为空!");
      }
      if (StringUtils.isEmpty(id)) {
        return jsonViewResolver.errorJsonResult("角色id不能为空!");
      }
      Long roleId = Long.parseLong(id);
      SystemRole systemRole = systemRoleService.getSystemRoleBy(roleId);
      if (null == systemRole) {
        return jsonViewResolver.errorJsonResult("角色不存在");
      }
      Principal principalIsExist = principalService.getPrincipal(principal.getName());
      if (null == principalIsExist) {
        return jsonViewResolver.errorJsonResult(USER_NOT_EXIST);
      }
      // 获取角色能访问的菜单ID列表存入MenusIds
      List<LinkRoleMenu> linkRoleMenus = linkRoleMenuService.getMenusBy(roleId);
      List<Long> menuIds = new ArrayList<>();
      for (LinkRoleMenu linkRoleMenu : linkRoleMenus) {
        menuIds.add(linkRoleMenu.getMenuId());
      }
      // 加载所有菜单
      List<SystemMenu> systemMenus = systemMenuService.loadAllMenus();
      // (menu, checkedOrNot)信息
      List<SystemMenuForm> menus = new ArrayList<>();
      // 加载所有的按钮
      List<SystemButton> systemButtons = systemButtonService.loadAllButtons();
      //(button, checkedOrNot)信息
      List<SystemButtonForm> buttons = new ArrayList<>();
      //通过角色id查找相应的权限buttonIds
      List<Long> linkPrivilegeButtonIds = linkRolePrivilegeService
          .findLinkPrivilegeButtonIds(roleId);
      List<Long> userOwnButtonIds = systemButtonHandler
          .getSystemButtonIdsByPrivilege(linkPrivilegeButtonIds);
      List<SystemButton> userRealAccessButtons = new ArrayList<>();
      // 将systemMenus转化为SystemMenuForm并初始化checked属性全为true
      //对访问的按钮id集合进行过滤(通过父级菜单来过滤)
      //这些按钮都是可访问的
      Map<Long, Object> checkMenuFormSet = new HashMap<>();
      systemMenus.forEach(m -> {
        // 编辑时, 菜单先全部选中
        checkMenuFormSet.put(m.getId(), m.toSystemMenuForm(false));
        List<SystemButton> realAccessButtons = systemButtonHandler
            .checkAccessButtonsByMenuId(userOwnButtonIds, m.getId());
        userRealAccessButtons.addAll(realAccessButtons);
      });

      // 当角色不能访问对应的菜单时, 节点的check属性置为false, 并依次重置父节点的check属性为false
      for (SystemMenu m : systemMenus) {
        Long menuId = m.getId();
        if (menuIds.contains(menuId) && !((SystemMenuForm) checkMenuFormSet.get(menuId))
            .isChecked()) {
          recheck(checkMenuFormSet, menuId);
        }
      }

      for (Map.Entry<Long, Object> entry : checkMenuFormSet.entrySet()) {
        menus.add((SystemMenuForm) entry.getValue());
      }

      //对可以访问的菜单,将节点的check属性设置为true ,不能访问的设置false
      userRealAccessButtons.forEach(
          userRealAccessButton -> buttons.add(userRealAccessButton.toSystemButtonForm(true)));
      systemButtons.removeAll(userRealAccessButtons);
      systemButtons
          .forEach(noAccessButton -> buttons.add(noAccessButton.toSystemButtonForm(false)));
      map.put("data", systemRole);
      map.put(MENUS, menus);
      map.put("buttons", buttons);
      return jsonViewResolver.sucJsonResult(map);

    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  private void recheck(Map<Long, Object> checkMenuFormSet, Long menuId) {
    SystemMenuForm systemMenuForm = (SystemMenuForm) checkMenuFormSet.get(menuId);
    systemMenuForm.setChecked(true);
    if (systemMenuForm.getParentId() != null) {
      recheck(checkMenuFormSet, systemMenuForm.getParentId());
    }
  }

  /**
   * http://localhost:9090/edit-systemrole?name=zhenghangbo&id=17&roleName=test9&roleRemark=test2&menuIds=1,2,34,56
   *
   * @return "角色名已删除"/"角色名已存在"/"系统错误"/"成功"<p> 注意: 更新角色能够访问的菜单表时, 会先清空之前配置过的菜单表, 然后新建<p>
   * menuIds为所选菜单的复选框的value值
   */
  @RequestMapping(value = "/edit-systemrole", method = RequestMethod.GET)
  public @ResponseBody
  String editRole(@ModelAttribute SystemRoleQueryModel systemRoleQueryModel,
      @RequestParam("menuIds") String menuIds,
      @RequestParam("buttonIds") String buttonIds) {
    try {
      String roleId;
      String roleNewName;
      if (null == systemRoleQueryModel) {
        return jsonViewResolver.errorJsonResult("角色对象为空!");
      }
      if (StringUtils.isEmpty(systemRoleQueryModel.getId()) &&
          StringUtils.isEmpty(systemRoleQueryModel.getRoleName())) {
        return jsonViewResolver.errorJsonResult("角色信息传入参数为空!");
      }
      roleId = systemRoleQueryModel.getId();
      roleNewName = systemRoleQueryModel.getRoleName();
      long longRoleId = Long.parseLong(roleId);
      SystemRole olderSystemRole = systemRoleService.getSystemRoleBy(longRoleId);
      if (olderSystemRole == null || olderSystemRole.getRoleState() == SystemRoleState.HALT) {
        return jsonViewResolver.errorJsonResult("角色名已删除");
      }
      if (systemRoleService.SystemRoleQ(roleNewName) &&
          !roleNewName.equals(olderSystemRole.getRoleName())) {
        return jsonViewResolver.errorJsonResult(SYSTEM_ROLE_EXIST);
      }
      String roleRemark = systemRoleQueryModel.getRoleRemark();
      boolean isEditSystemRole = systemRoleHandler
          .editSystemRole(longRoleId, roleNewName, roleRemark, menuIds, buttonIds);
      if (isEditSystemRole) {
        cacheManager.getCache(RESOURCES).clear();
        return jsonViewResolver.sucJsonResult();
      }
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * 角色名称一栏填写信息，失去焦点后即进行重复校验，提示操作者该名称已存在；<p> http://localhost:9090/validate-roleName?roleName=test3<p>
   *
   * @return "角色名已存在"/"角色名不存在"/"系统错误"
   */
  @RequestMapping(value = "/validate-roleName", method = RequestMethod.GET)
  public @ResponseBody
  String createRole(@RequestParam("roleName") String roleName) {
    try {
      SystemRole systemRole = systemRoleService.getSystemRoleBy(roleName);
      if (systemRole != null && systemRole.getRoleState() == SystemRoleState.REGULAR) {
        return jsonViewResolver.jsonResult(SYSTEM_ROLE_EXIST);
      }
      return jsonViewResolver.jsonResult("角色名不存在");
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * 跳转到系统角色 http://localhost:9090/show-systemrole
   */
  @MenuSetting("submenu-show-systemrole")
  @RequestMapping(value = "/show-systemrole", method = RequestMethod.GET)
  public ModelAndView toShowSystemRole(@Secure Principal principal) {
    try {
      return new ModelAndView(VIEW_NAME_INDEX);
    } catch (Exception e) {
      return pageViewResolver.errorSpec();
    }
  }

  /**
   * http://localhost:9090/show-systemrole/query?name=zhenghangbo&id=19&page=1<p>
   * http://localhost:9090/show-systemrole/query?name=zhenghangbo&roleName=test&page=1
   *
   * @return 页码部分, 查询数据总量: size<p> 查询结果: <p> 角色ID:		list.id<p> 角色名称:		list.roleName<p>
   * 当前用户:		list.userCount<p> 建立时间:		list.createTime<p> 备注:			list.roleRemark<p>
   * 状态:			lsit.roleState
   */
  @RequestMapping(value = "/show-systemrole/query", method = RequestMethod.GET)
  public @ResponseBody
  String queryRole(@Secure Principal principal,
      @ModelAttribute SystemRoleQueryModel systemRoleQueryModel, Page page) {
    try {
      Map<String, Object> data = new HashMap<>();
      String id = "";
      if (StringUtils.isNotEmpty(systemRoleQueryModel.getId())) {
        id = systemRoleQueryModel.getId();
      }
      String roleName = "";
      if (StringUtils.isNotEmpty(systemRoleQueryModel.getRoleName())) {
        roleName = systemRoleQueryModel.getRoleName();
      }

      List<SystemRoleForm> result = systemRoleService.getSystemRoleForm(id, roleName, page);
      int size = systemRoleService.countSystemRole(id, roleName);
      data.put("list", result);
      data.put("size", size);
      return jsonViewResolver.sucJsonResult(data);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * http://localhost:9090/del-systemrole?name=zhenghangbo&id=24
   *
   * @return "非法id!"/"成功"/"系统错误"<p> 注意: 删除角色名将删除所有使用此角色的(用户的角色数据)和此角色所选菜单数据<p> 删除角色不是物理删除
   */
  @RequestMapping(value = "/del-systemrole", method = RequestMethod.GET)
  public @ResponseBody
  String delRole(@Secure Principal principal, @RequestParam("id") String id) {
    try {
      if (StringUtils.isEmpty(id)) {
        return jsonViewResolver.errorJsonResult("传入的id为空");
      }
      Long roleId = Long.parseLong(id);
      if (!systemRoleService.deleteSystemRole(roleId) && !linkRolePrincipalService
          .delLinkRole(roleId) && !systemRoleHandler.delSystemRoleRelation(roleId)) {
        return jsonViewResolver.errorJsonResult("系统错误,删除角色失败");
      }
      cacheManager.getCache(RESOURCES).clear();
      cacheManager.getCache(ROLES).clear();
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * @return "请输入格式有效的用户名！"/"用户名已存在！"/"请输入真实名字！"
   */
  private Result isValidPrincipalInfo(PrincipalInfoModel principalInfoModel, Result result,
      boolean isUpdate) {
    if (!isUpdate) {
      if (!principalInfoModel.isValidUsername()) {
        return result.message("请输入格式有效的用户名！");
      }

      String username = principalInfoModel.getUsername();
      Principal principal = principalService.getPrincipal(username);
      if (principal != null) {
        return result.message("用户名已存在！");
      }
    }
    String realName = principalInfoModel.getRealname();
    if (StringUtils.isEmpty(realName)) {
      return result.message("请输入真实名字！");
    }
    return result.success();
  }

  /**
   * http://localhost:9090/create-user-role/username?username=zhenghangbo
   *
   * @return "请输入格式有效的用户名！"/"用户名已存在！"/"成功"
   */
  @RequestMapping(value = "create-user-role/username")
  public @ResponseBody
  String isValidPrincipalName(String username) {
    if (!PrincipalInfoModel.isValidUserName(username)) {
      return jsonViewResolver.errorJsonResult("请输入格式有效的用户名！");
    }
    Principal principal = principalService.getPrincipal(username);
    if (principal != null) {
      return jsonViewResolver.errorJsonResult("用户名已存在！");
    }
    return jsonViewResolver.sucJsonResult();
  }

  /**
   * 跳转页面<p> http://localhost:9090/show-user-list
   *
   * @return 所属公司:	companies(id, fullName)<p> 用户分组: 	groupList(id, groupName)<p> 用户角色:	roleList(id,
   * roleName)
   */
  @MenuSetting("submenu-role-list")
  @RequestMapping(value = "show-user-list", method = RequestMethod.GET)
  public ModelAndView showUser(@Secure Principal principal) {
    try {
      return new ModelAndView(VIEW_NAME_INDEX);
    } catch (Exception e) {
      logger.error("occur error:" + ExceptionUtils.getFullStackTrace(e));
      return pageViewResolver.errorSpec();
    }
  }

  @RequestMapping(value = "show-user-list/options", method = RequestMethod.GET)
  public @ResponseBody
  String getUserOptions(@Secure Principal principal) {
    try {
      Map<String, Object> result = new HashMap<>();
      List<Company> companies = companyService.getAllCompany();
      List<UserGroup> userGroups = userGroupService.getAllUserGroup();
      List<SystemRole> systemRole = systemRoleService.getUndeletedSystemRole();
      result.put("companies", companies);
      result.put(GROUP_LIST, userGroups);
      result.put("roleList", systemRole);
      return jsonViewResolver.sucJsonResult(result);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult("用户管理列表页面获取配置数据错误");
    }
  }

  /**
   * 根据用户ID(id)和账户名(accountName)查询<p> http://localhost:9090/show-user-list/query?name=zhenghangbo&id=12&accountName=yunxintest002&page=1<p>
   * 根据公司ID(companyId), 分组ID(groupId)和角色ID(authorityId)查询<p> http://localhost:9090/show-user-list/query?name=zhenghangbo&companyId=1&groupId=1&roleId=9&page=1
   *
   * @return 查询数据总量: size<p> 查询数据: list<p> 用户ID:		list.id<p> 用户角色:		list.role<p>
   * 账户名:		list.accountName<p> 持有者:		list.userName<p> 联系邮箱:		list.email<p> 联系号码:		list.number<p>
   * 所属公司:		list.company<p> 用户分组:		list.groupName<p> 状态:			list.status
   */
  @RequestMapping(value = "show-user-list/query", method = RequestMethod.GET)
  public @ResponseBody
  String queryUser(@Secure Principal principal, @ModelAttribute PrincipalQueryModel queryModel,
      Page page) {
    try {
      Map<String, Object> data = new HashMap<>();
      List<Map<String, Object>> principals;
      int size;
      if (StringUtils.isEmpty(queryModel.getRoleId())) {
        principals = principalService.queryPrincipalWithoutGroup(queryModel, page);
        size = principalService.countPrincipalWithoutGroup(queryModel);
      } else {
        principals = principalService.queryPrincipalWithGroup(queryModel, page);
        size = principalService.countPrincipalWithGroup(queryModel);
      }
      List<UserManageForm> showModels = new ArrayList<>();
      for (Map<String, Object> principal2 : principals) {
		int count =secretKeyService.countSecretKeyBy((Long) principal2.get("id"));
    	UserManageForm showModel = new UserManageForm(principal2,count);
        showModels.add(showModel);
      }
      data.put("list", showModels);
      data.put("size", size);
      return jsonViewResolver.sucJsonResult(data);
    } catch (NumberFormatException e) {
      return jsonViewResolver.errorJsonResult("id格式错误");
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  @RequestMapping(value = "delete-user-role/{id}", method = RequestMethod.GET)
  public @ResponseBody
  String deleteUser(@Secure Principal currentPrincipal, @PathVariable Long id) {
    try {
      if (currentPrincipal == null || currentPrincipal.getId() == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
//      if (!currentPrincipal.getId().equals(id) && !currentPrincipal.is_super_user_role()) {
//        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
//      }
      principalService.deleteUser(currentPrincipal);
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult("关闭失败");
    }
  }

  /**
   * 获取权限菜单 url:http://localhost:9090/get-auth-menus?username=louguanyang
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @RequestMapping(value = "get-auth-menus", method = RequestMethod.GET)
  public @ResponseBody
  String getAuthMenus(@Secure Principal currentPrincipal, String username) {
    try {
      if (currentPrincipal == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
      if (StringUtils.isEmpty(username)) {
        return jsonViewResolver.errorJsonResult(USER_NAME_EMPTY);
      }
      if (!StringUtils.equalsIngoreNull(username, currentPrincipal.getName())) {
        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
      }

      Long principalId = currentPrincipal.getId();
      Principal principal = principalService.getPrincipalById(principalId);

      Map<String, Object> params = new HashMap<>();
      params.put("id", principalId);
      params.put("authority", principal.getAuthority());
      params.put("username", username);
      List<SystemMenuForm> accessArray = new ArrayList<>();
      //过滤用户可以拥有菜单
      List<SystemMenu> menus = principalService.getMenusBy(principalId);
      for (SystemMenu systemMenu : menus) {
        Collection<String> auth = systemMenuService.getAuthBy(systemMenu.getId());
        SystemMenuForm systemMenuForm = systemMenu.toSystemMenuForm(false);
        systemMenuForm.setAuth(auth);
        accessArray.add(systemMenuForm);
      }
      //对所有的一级菜单地址进行变更，获取第一个可显示子菜单的URL
      List<SystemMenuForm> accessResults = new ArrayList<>();
      for (SystemMenuForm menu : accessArray) {
        if (menu.getSystemMenuLevel() == 0) {
          String menuUrl = getExistChildMenuUrl(menu.getId(), menus);
          logger.info("Result: " + menuUrl);
          menu.setUrl(menuUrl);
        }
        accessResults.add(menu);
      }
      params.put(MENUS, accessResults);
      params.put("modifyPasswordTimes", principal.getModifyPasswordTime());
      String menusJson = JsonUtils.toJsonString(params);
      logger.info("menusJson: " + menusJson);
      return jsonViewResolver.sucJsonResult(params);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * 修改bug: 因为主菜单固定使用的是当前第一个子菜单的URL，做权限的时候会被过滤掉 现在做一个动态替换，存在子菜单（按顺序），用子菜单替换(子菜单的链接可能不存在)
   */
  private String getExistChildMenuUrl(Long parentId, List<SystemMenu> privilegeMenus) {

    String url = "";
    List<SystemMenu> menuList = getMenu(parentId, privilegeMenus);
    SystemMenu parentMenu = systemMenuService.findMenuById(parentId);
    menuList.remove(parentMenu);
    if (CollectionUtils.isEmpty(menuList)) {
      return url;
    }
    if (menuList.get(0) == null) {
      return url;
    }
    for (SystemMenu menu : menuList) {
      logger.info("url: " + menu.getUrl());
      if (!StringUtils.isEmpty(menu.getUrl())) {
        return menu.getUrl();
      }
    }
    return url;
  }

  /**
   * 将菜单生成一棵遍历树
   */
  private List<SystemMenu> getMenu(Long id, List<SystemMenu> privilegeMenus) {
    if (id <= 0) {
      return Collections.emptyList();
    }
    List<SystemMenu> result = new ArrayList<>();
    //首先对当前节点进行判断
    SystemMenu currentMenu = systemMenuService.findMenuById(id);
    if (currentMenu == null || !privilegeMenus.contains(currentMenu)) {
      return Collections.emptyList();
    }
    result.add(currentMenu);
    List<SystemMenu> subMenus = systemMenuService.getSubMenus(currentMenu.getId());
    subMenus.retainAll(privilegeMenus);
    sortMenuList(subMenus);
    if (!CollectionUtils.isEmpty(subMenus)) {
      for (SystemMenu menu : subMenus) {
        result.addAll(getMenu(menu.getId(), privilegeMenus));
      }
    }
    return result;
  }

  private void sortMenuList(List<SystemMenu> menuList) {
    if (CollectionUtils.isEmpty(menuList)) {
      return;
    }
    /*
     * int compare(SystemMenu o1, SystemMenu o2) 返回一个基本类型的整型，
     * 返回负数表示：o1 小于o2，
     * 返回0 表示：o1和o2相等，
     * 返回正数表示：o1大于o2。
     */
    menuList.sort((o1, o2) -> {
      String o1Url = o1.getUrl();
      int o1SystemMenuLevel = o1.getSystemMenuLevel().ordinal();
      int o2SystemMenuLevel = o2.getSystemMenuLevel().ordinal();
      if (StringUtils.isEmpty(o1Url) && o1SystemMenuLevel > o2SystemMenuLevel) {
        return 1;
      }
      if (o1SystemMenuLevel != o2SystemMenuLevel) {
        return -1;
      }
      return Integer.compare(o1.getSeqNo(), o2.getSeqNo());
    });
  }

  /**
   * 根据用户名和页面菜单id获取相关权限的按钮 url:http://localhost:9090/get-auth-buttons?username=louguanyang&menuId=8
   *
   * @param username 用户名
   * @param mkey 相关页面菜单的mkey
   */
  @RequestMapping(value = "get-auth-buttons", method = RequestMethod.GET)
  public @ResponseBody
  String getAuthButtonsBy(@Secure Principal currentPrincipal, String username, String mkey) {
    try {
      if (StringUtils.isEmpty(username) || StringUtils.isEmpty(mkey)) {
        return jsonViewResolver.errorJsonResult("请求参数错误,出现空值");
      }
      if (currentPrincipal == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
      if (StringUtils.isEmpty(username)) {
        return jsonViewResolver.errorJsonResult(USER_NAME_EMPTY);
      }
      if (!StringUtils.equalsIngoreNull(username, currentPrincipal.getName())) {
        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
      }

      Long principalId = currentPrincipal.getId();

      List<String> buttonBKeyList = new ArrayList<>();
      //校验当前menu是否存在,该用户是否有权限访问
      SystemMenu parentMenu = systemMenuService.findMenuByMkey(mkey);
      List<SystemMenu> menus = principalService.getMenusBy(principalId);
      if (parentMenu == null || !menus.contains(parentMenu)) {
        return jsonViewResolver.errorJsonResult("传入错误的唯一参数mkey: " + mkey);
      }
      //根据用户信息获取该用户的有权限的按钮模块的id集合
      List<Long> linkPrivilegeButtonIds = systemButtonHandler
          .getLinkPrivilegeButtonId(principalId);
      //通过有权限的按钮模块集合获取相应的可访问按钮id集合
      List<Long> userOwnButtonIds = systemButtonHandler
          .getSystemButtonIdsByPrivilege(linkPrivilegeButtonIds);
      //对访问的按钮id集合进行过滤(通过父级菜单来过滤)
      List<SystemButton> userRealAccessButtons = systemButtonHandler
          .checkAccessButtonsByMenuId(userOwnButtonIds, parentMenu.getId());
      //组装
      HashMap<String, Object> params = new HashMap<>(2);
      for (SystemButton systemButton :
          userRealAccessButtons) {
        SystemButtonForm systemButtonForm = systemButton.toSystemButtonForm(false);
        systemButtonForm.setAuth(systemButtonHandler.getAuthByButtonId(systemButton.getId()));
        buttonBKeyList.add(systemButtonForm.getBkey());
      }
      params.put("elements", buttonBKeyList);
      return jsonViewResolver.sucJsonResult(params);

    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * 查询可以绑定的信托合同
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "bind-financial-contract/query", method = RequestMethod.GET)
  public @ResponseBody
  String queryBindFinancialContract(
      @Secure Principal currentPrincipal,
      @ModelAttribute FinancialContractQueryModel queryModel,
      Page page) {
    try {
      if (currentPrincipal == null || currentPrincipal.getId() == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
      Long principalId = queryModel.getPrincipalId();
      
//      if (!currentPrincipal.getId().equals(principalId) && !currentPrincipal.is_super_user_role()) {
//        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
//      }

      Map<String, Object> data = principalHandler.
          queryFinancialContractListByPrincipal(queryModel, page);
      List<FinancialContract> canAccessFinancialContractList = principalHandler
          .get_can_access_financialContract_list(principalId);
      List<FinancialContractShowModel> showList = new ArrayList<>();
      List<FinancialContract> list = (List<FinancialContract>) data.get("list");
      for (FinancialContract financialContract : list) {
        FinancialContractShowModel showModel = new FinancialContractShowModel(financialContract);
        if (CollectionUtils.isNotEmpty(canAccessFinancialContractList)
            && canAccessFinancialContractList.contains(financialContract)) {
          showModel.setBindState(1);
        }
        showList.add(showModel);
      }
      data.remove("list");
      data.put("list", showList);
      return jsonViewResolver.sucJsonResult(data);
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * 绑定信托合同
   *
   * @param financialContractIds 信托合同Id 列表
   * @param principalId 用户Id 列表
   */
  @RequestMapping(value = "bind-financial-contract/bind", method = RequestMethod.POST)
  public @ResponseBody
  String bindFinancialContract(
      @Secure Principal currentPrincipal,
      @RequestParam("financialContractIds") String financialContractIds,
      @RequestParam(value = "principalId") Long principalId) {
    try {
      if (currentPrincipal == null || currentPrincipal.getId() == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
//      if (!currentPrincipal.getId().equals(principalId) && !currentPrincipal.is_super_user_role()) {
//        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
//      }

      List<Long> financialContractIdList = getFinancialContractIdList(financialContractIds);
      principalHandler.bindFinancialContract(principalId, financialContractIdList);
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult("绑定失败，请联系管理员！");
    }
  }

  /**
   * 解除绑定
   *
   * @param financialContractIds 信托合同Id 列表
   * @param principalId 用户Id 列表
   */
  @RequestMapping(value = "bind-financial-contract/unbind", method = RequestMethod.POST)
  public @ResponseBody
  String unBindFinancialContract(
      @Secure Principal currentPrincipal,
      @RequestParam("financialContractIds") String financialContractIds,
      @RequestParam(value = "principalId") Long principalId) {
    try {
      if (currentPrincipal == null || currentPrincipal.getId() == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
//      if (!currentPrincipal.getId().equals(principalId) && !currentPrincipal.is_super_user_role()) {
//        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
//      }

      Principal principal = principalService.getPrincipalById(principalId);
      TUser tUser = principal.gettUser();

      List<Long> financialContractIdList = getFinancialContractIdList(financialContractIds);
      List<Long> existIdList = tUser.getFinancialContractIdList();
      existIdList.removeAll(financialContractIdList);
      List<Long> newList = existIdList.stream().distinct().collect(Collectors.toList());
      String newIdJson = com.zufangbao.sun.utils.JsonUtils.toJSONString(newList);
      tUser.setFinancialContractIds(newIdJson);
      tUserService.save(tUser);
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  private List<Long> getFinancialContractIdList(String financialContractIds) {
    List<Long> financialContractList = JsonUtils.parseArray(financialContractIds, Long.class);
    if (financialContractList == null) {
      return new ArrayList<>();
    }
    return financialContractList;
  }

  /**
   * 业务权限 - 全选
   */
  @RequestMapping(value = "bind-financial-contract/bindAll", method = RequestMethod.POST)
  public @ResponseBody
  String bindAllFinancialContract(
      @Secure Principal currentPrincipal,
      @RequestParam(value = "principalId") Long principalId) {
    try {
      if (currentPrincipal == null || currentPrincipal.getId() == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
//      if (!currentPrincipal.getId().equals(principalId) && !currentPrincipal.is_super_user_role()) {
//        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
//      }

      String msg = principalHandler.bindAllFinancialContract(principalId);
      if (StringUtils.isEmpty(msg)) {
        return jsonViewResolver.errorJsonResult(msg);
      }
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }

  /**
   * 业务权限 - 解除全选(全部解绑)
   */
  @RequestMapping(value = "bind-financial-contract/unbindAll", method = RequestMethod.POST)
  public @ResponseBody
  String unbindAllFinancialContract(
      @Secure Principal currentPrincipal,
      @RequestParam(value = "principalId") Long principalId) {
    try {
      if (currentPrincipal == null || currentPrincipal.getId() == null) {
        return jsonViewResolver.errorJsonResult(USER_NOT_LOGIN);
      }
//      if (!currentPrincipal.getId().equals(principalId) && !currentPrincipal.is_super_user_role())  {
//        return jsonViewResolver.errorJsonResult(AUTHORITY_ERROR);
//      }

      String msg = principalHandler.unbindAllFinancialContract(principalId);
      if (StringUtils.isEmpty(msg)) {
        return jsonViewResolver.errorJsonResult(msg);
      }
      return jsonViewResolver.sucJsonResult();
    } catch (Exception e) {
      logger.error(ExceptionUtils.getFullStackTrace(e));
      return jsonViewResolver.errorJsonResult(SYSTEM_ERROR);
    }
  }
}
