package com.zufangbao.earth.web.controller.deduct;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.service.PrincipalService;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.api.handler.ThirdPartVoucherFrontEndShowHandler;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.handler.DeductApplicationCoreOperationHandler;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.deduct.DeductApplicationQeuryModel;
import com.zufangbao.sun.yunxin.entity.excel.DeductApplicationExcelVO;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.deduct.DeductApplicationDetailShowModel;
import com.zufangbao.sun.yunxin.entity.model.deduct.QueryDeductApplicationShowModel;
import com.zufangbao.sun.yunxin.entity.model.deduct.ThirdPartVoucherDeductShowModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/deduct")
@MenuSetting("menu-finance")
public class DeductController extends BaseController {

	private static final Log logger = LogFactory.getLog(DeductController.class);
	
	@Autowired
	private DeductApplicationCoreOperationHandler deductApplicationHandler;
	
	@Autowired
	private ThirdPartVoucherFrontEndShowHandler thirdPartVoucherFrontEndShowHandler;
	
	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private PrincipalService principalService;

	@Autowired
	public FinancialContractService financialContractService;
	
	@RequestMapping(value = "/application/options")
	@MenuSetting("submenu-deduct")
	public @ResponseBody String getOption(@Secure Principal principal, HttpServletRequest request) {
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			result.put("orderStatus", EnumUtil.getKVList(DeductApplicationExecutionStatus.class));
			result.put("repaymentType",EnumUtil.getKVList(RepaymentType.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			logger.error("#showDeductApplicationPage# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("列表选项获取错误");
		}
	}
	
	@RequestMapping(value="/application/query")
	@MenuSetting("submenu-deduct")
	public @ResponseBody String queryDeductApplication(DeductApplicationQeuryModel deductApplicationQeuryModel,Page page){
		try {
			
			List<QueryDeductApplicationShowModel> showModels =  deductApplicationHandler.queryDeductApplicationShowModel(deductApplicationQeuryModel,page);
			int total = deductApplicationHandler.countQueryDeductApplicationNumber(deductApplicationQeuryModel);
			
			Map<String, Object> data = new HashMap<>();
			data.put("deductApplicationQeuryModel",deductApplicationQeuryModel);
			data.put("list", showModels);
			data.put("size", total);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#queryDeductApplicationPage# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误");
		}
	}
	
	// 扣款订单列表导出预览
	@RequestMapping(value = "/application/preview-exprot-application", method = RequestMethod.GET)
	@MenuSetting("submenu-deduct")
	public @ResponseBody String previewExportApplication(HttpServletRequest request, @ModelAttribute DeductApplicationQeuryModel queryModel, HttpServletResponse response) {
		try {
			Page page = new Page(0, 10);
			List<DeductApplicationExcelVO> deductApplicationExcelVOs = deductApplicationHandler.getDeductApplicationExcelVO(queryModel, page);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("list", deductApplicationExcelVOs);
			return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#previewExportApplication  occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("扣款订单列表导出预览失败");
		}
	}
	
	//扣款订单列表--统计金额（扣款金额、实际扣款金额，差值（扣款金额-实际扣款金额））
	@RequestMapping(value="/application/amountStatistics")
	@MenuSetting("submenu-deduct")
	public @ResponseBody String amountStatistics(DeductApplicationQeuryModel deductApplicationQeuryModel){
		try {
			Map<String, Object> data = deductApplicationHandler.amountStatistics(deductApplicationQeuryModel);
			return jsonViewResolver.sucJsonResult(data,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			logger.error("#amountStatistics# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("扣款相关金额统计错误");
		}
	}

	@RequestMapping(value="application/detail/{deductId}")
	@MenuSetting("submenu-deduct")
	public @ResponseBody String showDeductApplicationDetails(@PathVariable(value = "deductId") String deductId){
		try {
			Map<String,Object> data=new HashMap<>();
			List<ThirdPartVoucherDeductShowModel> thirdVouchershowModels = thirdPartVoucherFrontEndShowHandler.fetchThirdPartVoucherFrontEndShowModelByDeductInformation(deductId);
			DeductApplicationDetailShowModel showModel = deductApplicationHandler.assembleDeductApplicationDetailShowModel(deductId);
			showModel.setThirdPartVoucherDeductShowModels(thirdVouchershowModels);
			data.put("showModel", showModel);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			logger.error("#showDeductApplicationDetails# occur error.");
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询错误");
		}
	}
}
