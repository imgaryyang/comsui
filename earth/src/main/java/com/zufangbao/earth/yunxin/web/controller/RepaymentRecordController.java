package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentRecordQueryModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.RepaymentRecordDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.yunxin.handler.JournalVoucherHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("RepaymentRecordController")
@RequestMapping("/repayment-record")
@MenuSetting("menu-finance")
public class RepaymentRecordController extends BaseController {

	@Autowired
	private PrincipalHandler principalHandler;

	@Autowired
	private JsonViewResolver jsonViewResolver;

	private static final Log logger = LogFactory.getLog(RepaymentRecordController.class);

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private JournalVoucherService journalVoucherService;

	@Autowired
	private JournalVoucherHandler journalVoucherHandler;

	private int MAX_EXPORT = 30000;

	/**
     * 还款记录列表页-optionsData
     */
	@RequestMapping(value = "/repayment/optionsData", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-record-list")
	public @ResponseBody String getRepaymentOrderOptions(@Secure Principal principal) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();

			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

			result.put("queryAppModels", queryAppModels);
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("获取配置数据错误");
		}
	}

	/**
     * 还款记录列表页
     */
	@RequestMapping(value = "/repaymentRecord/query", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-record-list")
	public @ResponseBody String queryRepaymentRecord(@ModelAttribute RepaymentRecordQueryModel repaymentRecordQueryModel,
			@Secure Principal principal, Page page) {
		try {

			List<JournalVoucher> journalVoucherList = journalVoucherService.getJournalVouchersByRepaymentRecordQueryModel(repaymentRecordQueryModel, page);

			List<RepaymentRecordDetail> recordDetailList = journalVoucherHandler.getRepaymentRecordDetailBy(journalVoucherList);

			int count = journalVoucherService.getCountRepaymentRecords(repaymentRecordQueryModel);

			Map<String, Object> data = new HashMap<>();
			data.putIfAbsent("list", recordDetailList);
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	/**
     * 导出还款记录的总数
     */
	@RequestMapping(value = "/repaymentRecord/countNums", method = RequestMethod.GET)
	@MenuSetting("submenu-repayment-record-list")
	public @ResponseBody String repaymentRecordNums(@ModelAttribute RepaymentRecordQueryModel repaymentRecordQueryModel){

		try{
			//判断是否是一个信托
			repaymentRecordQueryModel.judgeFinancialContractOnlyOne();

			int count = journalVoucherService.getCountRepaymentRecords(repaymentRecordQueryModel);
            if (count > MAX_EXPORT) {
				return jsonViewResolver.errorJsonResult("文件导出超过数量限制，请缩小查询区间！");
			}

			Map<String, Object> data = new HashMap<>();
			data.putIfAbsent("size", count);
			return jsonViewResolver.sucJsonResult(data);
    	}catch (Exception e) {
    		logger.error("repaymentRecordNums. FullStackTrace:"
					+ ExceptionUtils.getFullStackTrace(e));
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

}




