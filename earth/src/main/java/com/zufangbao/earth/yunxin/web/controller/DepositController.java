package com.zufangbao.earth.yunxin.web.controller;

import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.web.controller.financial.CapitalControllerSpec.URL;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.GlobalRuntimeException;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.CashFlowService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.SourceDocumentStatus;
import com.zufangbao.sun.yunxin.entity.model.AccountsPrepaidModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import com.zufangbao.wellsfargo.exception.BankCancelPaymentException;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.AccountDepositDetailModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.CashFlowAutoIssueHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentDetailService;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.SourceDocumentService;
import com.zufangbao.wellsfargo.yunxin.handler.SourceDocumentHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller()
@RequestMapping(URL.CAPITAL_NAME)
@MenuSetting(URL.CAPITAL_MENU)
public class DepositController extends BaseController {

    @Autowired
    private SourceDocumentService sourceDocumentService;

    @Autowired
    private SourceDocumentHandler sourceDocumentHandler;

    @Autowired
    private CashFlowAutoIssueHandler cashFlowAutoIssueHandler;

    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private SystemOperateLogHandler systemOperateLogHandler;

    @Autowired
    private PrincipalHandler principalHandler;

	@Autowired
	public FinancialContractService financialContractService;
	
	@Autowired
	private SourceDocumentDetailService sourceDocumentDetailService;private static Log logger = LogFactory.getLog(DepositController.class);
	
	@RequestMapping(value = "customer-account-manage/deposit-receipt-list/show/options", method = RequestMethod.GET)
	public @ResponseBody String getOption(@Secure Principal principal) {
		try {
			HashMap<String, Object> result = new HashMap<String, Object>();
			List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
			List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

            HashMap<String, String> sourceDocumentStatusList = new HashMap<>();
            sourceDocumentStatusList.put(SourceDocumentStatus.SIGNED.getOrdinal() + "", SourceDocumentStatus.SIGNED.getChineseName());
            sourceDocumentStatusList.put(SourceDocumentStatus.INVALID.getOrdinal() + "", SourceDocumentStatus.INVALID.getChineseName());

            result.put("queryAppModels", queryAppModels);
            result.put("sourceDocumentStatusList", sourceDocumentStatusList);
            result.put("customerTypeList", EnumUtil.getKVList(CustomerType.class));
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("列表选项获取错误");
        }
    }

    @RequestMapping("customer-account-manage/deposit-receipt-list/query")
    public @ResponseBody
    String queryAccountsPrepaid(@Secure Principal principal,
                                @ModelAttribute AccountsPrepaidModel accountsPrepaidModel, Page page) {
        try {
            List<SourceDocument> sourceDocumentList = sourceDocumentService.getSourceDocumentList(accountsPrepaidModel,
                    page);
            List<SourceDocumentVO> sourceDocumentVOList = sourceDocumentHandler
                    .castSourceDocumentVO(sourceDocumentList);

            int count = sourceDocumentService.count(accountsPrepaidModel);
            Map<String, Object> data = new HashMap<String, Object>();
            data.putIfAbsent("accountsPrepaidModel", accountsPrepaidModel);
            data.putIfAbsent("list", sourceDocumentVOList);
            data.putIfAbsent("size", count);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 账户充值  作废
     *
     * @param sourceDocumentUuid
     * @param remark
     * @param principal
     * @param request
     * @return
     */
    @RequestMapping(value = "customer-account-manage/deposit-receipt-list/recharge-cancel", method = RequestMethod.GET)
    public @ResponseBody
    String rechargeCancel(@RequestParam(value = "sourceDocumentUuid") String sourceDocumentUuid,
                          @RequestParam(value = "remark") String remark, @Secure Principal principal, HttpServletRequest request) {

		try {
			if (StringUtils.isEmpty(sourceDocumentUuid)) {
				return jsonViewResolver.errorJsonResult("充值单号为空");
			}
			SourceDocument sourceDocument = sourceDocumentService.getSourceDocumentBy(sourceDocumentUuid);
			if (sourceDocument == null) {
				logger.info("sourceDocument is null ");
				throw new ApiException("充值单不存在");
			}
			if (sourceDocument.getSourceDocumentStatus() == SourceDocumentStatus.INVALID) {
				logger.info(
						"sourceDocument is invalid  sourceDocumentNo [" + sourceDocument.getSourceDocumentNo() + "]");
				throw new ApiException("充值单已作废");
			}

			if(sourceDocument.sourceDocumentBeAbleToExcute()==false){
				throw new ApiException("充值单当前不可做操作");
			}

			//变更充值单执行状态为执行中
			sourceDocumentService.updateExcuteStatus(sourceDocumentUuid, SourceDocumentExcuteStatus.DOING);
			List<SourceDocumentDetail> sourceDocumentDetailList = sourceDocumentDetailService.getValidDeductSourceDocumentDetailsBySourceDocumentUuid(sourceDocumentUuid);
			for(SourceDocumentDetail sourceDocumentDetail : sourceDocumentDetailList){
				if(SourceDocumentDetailStatus.SUCCESS == sourceDocumentDetail.getStatus()){
					throw new ApiException("该充值单已经使用");
				}
			}
			BigDecimal bookingAmount = sourceDocument.getBookingAmount();
         SourceDocument sourceDocumentOfRevoke = cashFlowAutoIssueHandler.deposit_cancel(sourceDocumentUuid,
					sourceDocument.getOutlierDocumentUuid(), bookingAmount, remark, "");
			if (sourceDocumentOfRevoke == null) {
				logger.info("sourceDocumentOfRevoke is null ");
				throw new ApiException("作废出错");
			}

			//创建撤销单  sourceDocumentRevoke log 
			SystemOperateLogRequestParam param2 = getSystemOperateLogrequestParamCreateRevoke(principal, request,sourceDocumentOfRevoke);
			systemOperateLogHandler.generateSystemOperateLog(param2);
			
			
			// 作废充值单    sourceDocument log
			SystemOperateLogRequestParam paramer = getSystemOperateLogrequestParamRevoke(principal, request,sourceDocument,sourceDocumentOfRevoke);
			systemOperateLogHandler.generateSystemOperateLog(paramer);
			

			return jsonViewResolver.sucJsonResult("sourceDocumentNo", sourceDocumentOfRevoke.getSourceDocumentNo());
		} catch (BankCancelPaymentException e) {
			e.printStackTrace();
			
			sourceDocumentService.updateExcuteStatus(sourceDocumentUuid, SourceDocumentExcuteStatus.DONE);
			return jsonViewResolver.errorJsonResult("余额不足");
		}catch(GlobalRuntimeException e){
			return jsonViewResolver.errorJsonResult(e,"系统异常");
		}catch(ApiException e){
			return jsonViewResolver.errorJsonResult(e,"系统异常");
		}catch (Exception e) {
			e.printStackTrace();
			sourceDocumentService.updateExcuteStatus(sourceDocumentUuid, SourceDocumentExcuteStatus.DONE);
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamCreateRevoke(Principal principal,
			HttpServletRequest request,SourceDocument sourceDocument ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
				IpUtils.getIpAddress(request), ",撤销金额为"+sourceDocument.getBookingAmount()+",撤销单号为["+sourceDocument.getSourceDocumentNo()+"]", LogFunctionType.ADDREVOKEORDER,
				LogOperateType.ADD, SourceDocument.class, sourceDocument, null, null);
		return param;

	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamRevoke(Principal principal,
			HttpServletRequest request,SourceDocument sourceDocument ,SourceDocument sourceDocumentRevoke ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
				IpUtils.getIpAddress(request), ",作废金额为"+sourceDocument.getBookingAmount()+",撤销单号为["
						+ sourceDocumentRevoke.getSourceDocumentNo() + "]", LogFunctionType.REVOKERECHARGEORDER,
				LogOperateType.ADD, SourceDocument.class, sourceDocument, null, null);
		return param;

	}

	/**
	 * 账户充值 详情页
	 ** @param principal
	 * @param sourceDocumentUuid
	 * @return
	 */
	@RequestMapping(value = "customer-account-manage/deposit-receipt-list/detail-data", method = RequestMethod.GET)
	@MenuSetting("submenu-deposit-receipt-list")
	public @ResponseBody String depositDetail(@Secure Principal principal,@RequestParam("sourceDocumentUuid") String sourceDocumentUuid) {
		try {
			AccountDepositDetailModel accountDepositModel = sourceDocumentHandler.getAccountDepositDetail(sourceDocumentUuid);
			
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("accountDepositModel", accountDepositModel);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误");
		}
	}
	
}
