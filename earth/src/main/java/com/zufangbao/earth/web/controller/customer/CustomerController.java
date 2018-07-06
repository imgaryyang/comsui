package com.zufangbao.earth.web.controller.customer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.app.AppController;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.company.CompanyType;
import com.zufangbao.sun.entity.customer.*;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.model.CustomerQueryModel;
import com.zufangbao.sun.yunxin.entity.model.CustomerShowModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyEnterpriseModel;
import com.zufangbao.sun.yunxin.entity.model.ModifyPersonModel;
import com.zufangbao.sun.yunxin.handler.CustomerHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/customer")
@MenuSetting("menu-financial")
public class CustomerController extends BaseController{
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerHandler customerHandler;

	private static final Log logger = LogFactory.getLog(AppController.class);
	
	// 获取下拉框选项-列表页
	@RequestMapping("/optionData")
	@MenuSetting("submenu-customer")
	public @ResponseBody String getOptionData() {
		try{
			Map<String, Object> data = new HashMap<>();
			data.put("customerStyle", EnumUtil.getKVList(CustomerStyle.class));
			data.put("IDType", EnumUtil.getKVList(IDType.class));
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("##CustomerController-getOptionData## get option data error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("下拉框获取失败！");
		}
	}
	
	// 获取下拉框选项-个人客户信息详情页
	@RequestMapping("/optionData/person")
	@MenuSetting("submenu-customer")
	public @ResponseBody String getOptionData_person() {
		try{
			Map<String, Object> data = new HashMap<>();
			data.put("sex", EnumUtil.getKVList(Sex.class));
			data.put("IDType_person", EnumUtil.getKVListIncludes(IDType.class, Arrays.asList(IDType.IDType_0,IDType.IDType_1,IDType.IDType_2,IDType.IDType_3,IDType.IDType_4)));
			data.put("maritalStatus", EnumUtil.getKVList(MaritalStatus.class));
			data.put("education", EnumUtil.getKVList(Education.class));
			data.put("degree", EnumUtil.getKVList(Degree.class));
			data.put("residentialStatus", EnumUtil.getKVList(ResidentialStatus.class));
			data.put("occupation", EnumUtil.getKVList(Occupation.class));
			data.put("duty", EnumUtil.getKVList(Duty.class));
			data.put("title", EnumUtil.getKVList(Title.class));
			data.put("industry", EnumUtil.getKVList(Industry.class));
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("##CustomerController-getOptionData_person## get option data error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("下拉框获取失败！");
		}
	}
	
	// 获取下拉框选项-企业客户信息详情页
	@RequestMapping("/optionData/enterprise")
	@MenuSetting("submenu-customer")
	public @ResponseBody String getOptionData_enterprise() {
		try{
			Map<String, Object> data = new HashMap<>();
			data.put("IDType_enterprise", EnumUtil.getKVListIncludes(IDType.class, Arrays.asList(IDType.IDType_5,IDType.IDType_6,IDType.IDType_7,IDType.IDType_8)));
			data.put("industry", EnumUtil.getKVList(Industry.class));
			data.put("companyType", EnumUtil.getKVList(CompanyType.class));
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("##CustomerController-getOptionData_enterprise## get option data error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("下拉框获取失败！");
		}
	}
	
	// 贷款客户管理-查询
	@RequestMapping(value="/search", method = RequestMethod.GET)
	@MenuSetting("submenu-customer")
	public @ResponseBody String search(@ModelAttribute CustomerQueryModel queryModel, Page page){
		try{
			if (queryModel == null) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			// 查找的客户都是正常的客户
			if (Objects.isNull(queryModel.getStatus())) {
				queryModel.setStatus(true);
			}

			Map<String, Object> dataMap;
			dataMap = customerHandler.searchCustomer(queryModel, page);
			
			return jsonViewResolver.sucJsonResult(dataMap,SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
		}catch(Exception e){
			logger.error("#CustomerController search#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误！");
		}
	}
	
	// 导出预览
	@RequestMapping(value = "/preview-exprot-customer", method = RequestMethod.GET)
	@MenuSetting("submenu-customer")
	public @ResponseBody String previewExportCustomer(
			HttpServletRequest request,
			@ModelAttribute CustomerQueryModel queryModel,
			HttpServletResponse response) {
		try {
			Page page = new Page(0, 10);
			List<CustomerShowModel> customerExcelVOs = customerHandler
					.getCustomerExcelVO(queryModel, page);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list", customerExcelVOs);
			return jsonViewResolver.sucJsonResult(data,
					SerializerFeature.DisableCircularReferenceDetect,
					SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#previewExportCustomer  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("导出预览失败");
		}
	}
	
	//导出限制
	@RequestMapping(value="/export/limit", method = RequestMethod.GET)
	@MenuSetting("submenu-customer")
	public @ResponseBody String limit(@ModelAttribute CustomerQueryModel queryModel, Page page){
		try{
			if (queryModel == null) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			int limit = customerService.queryCount(queryModel);
			if (limit > 100000) {
				return jsonViewResolver.errorJsonResult("数据已超过100000条！");
			}
			return jsonViewResolver.sucJsonResult();
		}catch(Exception e){
			logger.error("#CustomerController limit#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("导出失败！");
		}
	}
	
	/**
	 * 导出
	 */
	@RequestMapping(value = "/export")
	@MenuSetting("submenu-customer")
	public @ResponseBody String exportCustomer(@ModelAttribute CustomerQueryModel queryModel, HttpServletResponse response,@Secure Principal principal) {
		ExportEventLogModel exportEventLogModel = new ExportEventLogModel("21", principal);
		try {
			exportEventLogModel.recordStartLoadDataTime();
			List<CustomerShowModel> customerExcelVOs = customerHandler.getCustomerExcelVO(queryModel, null);
			if (customerExcelVOs.size() > 100000) {
				return jsonViewResolver.errorJsonResult("数据已超过100000条！");
			}
			exportEventLogModel.recordAfterLoadDataComplete(customerExcelVOs.size());
			
			ExcelUtil<CustomerShowModel> excelUtil = new ExcelUtil<CustomerShowModel>(CustomerShowModel.class);
			List<String> csvData = excelUtil.exportDatasToCSV(customerExcelVOs);

			Map<String, List<String>> csvs = new HashMap<String, List<String>>();
			csvs.put("贷款客户表", csvData);
			exportZipToClient(response, "贷款客户表"+DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") +".zip", GlobalSpec.UTF_8, csvs);

			exportEventLogModel.recordEndWriteOutTime();
			return jsonViewResolver.sucJsonResult();
		} catch (Exception e) {
			logger.error("#exportCustomer# occur error.");
			e.printStackTrace();
			exportEventLogModel.setErrorMsg(e.getMessage());
			return jsonViewResolver.errorJsonResult("导出失败");
		}finally {
			logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
		}
	}
	
	// 个人客户信息详情
	@RequestMapping(value = "/person/detail", method = RequestMethod.GET)
	@MenuSetting("submenu-customer")
	public @ResponseBody String personDetail(@RequestParam(value = "customerUuid") String customerUuid) {
		try {
			if (StringUtils.isBlank(customerUuid)) {
				return jsonViewResolver.errorJsonResult("个人客户信息详情获取失败！！！");
			}
			Map<String, Object> data = customerHandler.getPersonDetail(customerUuid);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##CustomerController personDetail## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("个人客户信息详情获取失败！！！");
		}
	}
	
	// 企业客户信息详情
	@RequestMapping(value = "/enterprise/detail", method = RequestMethod.GET)
	@MenuSetting("submenu-customer")
	public @ResponseBody String enterpriseDetail(@RequestParam(value = "customerUuid") String customerUuid) {
		try {
			if (StringUtils.isBlank(customerUuid)) {
				return jsonViewResolver.errorJsonResult("企业客户信息详情获取失败！！！");
			}
			Map<String, Object> data = customerHandler.getEnterpriseDetail(customerUuid);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect);
		} catch (Exception e) {
			logger.error("##CustomerController enterpriseDetail## error!!");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("企业客户信息详情获取失败！！！");
		}
	}
	
	// 详情页-相关贷款合同信息
	@RequestMapping(value="/relatedContract", method = RequestMethod.GET)
	@MenuSetting("submenu-customer")
	public @ResponseBody String getRelatedContract(@RequestParam(value = "customerUuid") String customerUuid){
		try{
			Customer customer = customerService.getPersonCustomer(customerUuid);
			if (customer == null) {
				return jsonViewResolver.errorJsonResult("参数错误！");
			}
			String idNumber = customer.getAccount();
			Map<String, Object> data = customerHandler.extractContractInfoByIDNumber(idNumber);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
		}catch(Exception e){
			logger.error("#CustomerController getRelatedContract#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("相关贷款合同信息获取错误！");
		}
	}

	// 个人客户信息-修改
	@RequestMapping(value="/person/modify", method = RequestMethod.POST)
	@MenuSetting("submenu-customer")
	public @ResponseBody String modifyPerson(@ModelAttribute ModifyPersonModel queryModel,
			@Secure Principal principal, HttpServletRequest request){
		try{
			String msg = customerHandler.modifyPerson(queryModel, principal.getId(), IpUtil.getIpAddress(request));
			if (StringUtils.isNotBlank(msg)) {
				return jsonViewResolver.errorJsonResult(msg);
			}
			return jsonViewResolver.sucJsonResult();
		}catch(Exception e){
			logger.error("#CustomerController modifyPerson#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
	
	// 企业客户信息-修改
	@RequestMapping(value="/enterprise/modify", method = RequestMethod.POST)
	@MenuSetting("submenu-customer")
	public @ResponseBody String modifyEnterprise(@ModelAttribute ModifyEnterpriseModel queryModel,
			@Secure Principal principal, HttpServletRequest request){
		try{
			String msg = customerHandler.modifyEnterprise(queryModel, principal.getId(), IpUtil.getIpAddress(request));
			if (StringUtils.isNotBlank(msg)) {
				return jsonViewResolver.errorJsonResult(msg);
			}
			return jsonViewResolver.sucJsonResult();
		}catch(Exception e){
			logger.error("#CustomerController modifyEnterprise#  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误！");
		}
	}
}
