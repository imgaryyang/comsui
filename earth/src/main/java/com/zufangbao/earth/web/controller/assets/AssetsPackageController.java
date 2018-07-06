/**
 *
 */
package com.zufangbao.earth.web.controller.assets;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.util.ExceptionUtils;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.handler.AssetPackageHandler;
import com.zufangbao.earth.yunxin.handler.LoanBatchHandler;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.AssetPackageService;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.NFQLoanInformation;
import com.zufangbao.sun.yunxin.entity.NFQRepaymentPlan;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

/**
 * 资产包管理
 *
 * @author wk
 *
 */
@RestController
@RequestMapping("/assets-package")
@MenuSetting("menu-data")
public class AssetsPackageController  extends  BaseController{
	@Autowired
	private AssetPackageService assetPackageService;
    @Autowired
    private LoanBatchHandler loanBatchHandler;
    @Autowired
    private AssetPackageHandler  assetPackageHandler;

	@RequestMapping(value = "excel-create-assetData", method = RequestMethod.POST)
	public @ResponseBody String importAssetPackageExcel(
			@RequestParam(value = "financialContractNo", required = true) Long financialContractNo,
			MultipartFile file, String app_id, @Secure Principal principal, HttpServletRequest request) {

		if(file == null) {
			return returnErrorMessage("请选择要导入的资产包");
		}

		try {
            String ipAddress = IpUtil.getIpAddress(request);
            InputStream inputStream = file.getInputStream();
			Workbook workbook = WorkbookFactory.create(inputStream);
			List<NFQLoanInformation> loanInformationList = new ExcelUtil<>(
					NFQLoanInformation.class).importExcelHighVersion(0, workbook);
			List<NFQRepaymentPlan> repaymentPlanList = new ExcelUtil<>(
					NFQRepaymentPlan.class).importExcelHighVersion(1, workbook);
			assetPackageHandler.verifyInputParam(loanInformationList, repaymentPlanList, financialContractNo);
			Result result =assetPackageHandler.importAssetPackagesViaExcel(loanInformationList, repaymentPlanList, financialContractNo,  principal.getName(), ipAddress);

			if(result.isValid()) {
				Long loanBatchId = (Long) result.get("loanBatchId");
				loanBatchHandler.generateLoanBacthSystemLog(principal, ipAddress, LogFunctionType.ASSETPACKAGEIMPORT ,LogOperateType.IMPORT,loanBatchId);
			}
			return JsonUtils.toJsonString(result);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return returnErrorMessage("资产包中数据格式有误！！！");
		} catch (Exception e) {
			e.printStackTrace();
			return returnErrorMessage(ExceptionUtils.getCauseErrorMessage(e));
		}
	}


	private String returnErrorMessage(String message) {
		Result result = new Result();
		result.fail().setMessage(message);
		return JsonUtils.toJsonString(result);
	}

}
