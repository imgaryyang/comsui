package com.zufangbao.earth.yunxin.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.demo2do.core.web.resolver.Secure;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.model.AssetSetDetailShowModel;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.earth.web.controller.BaseController;
import com.zufangbao.earth.yunxin.api.handler.MutableFeeDetailApiHandler;
import com.zufangbao.earth.yunxin.api.model.query.MutableFeeDetailsResultModel;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.spec.global.GlobalSpec;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.api.model.modify.OverdueChargesModifyModelParameter;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.company.corp.App;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.entity.repayment.order.RefundOrder;
import com.zufangbao.sun.entity.repayment.order.RefundStatus;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.handler.DeductApplicationCoreOperationHandler;
import com.zufangbao.sun.ledgerbook.InvalidModifyBalanceException;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.service.*;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentApplication;
import com.zufangbao.sun.yunxin.entity.api.PrepaymentType;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlanImporter;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlanShowModel;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.CreateMutableFeeModel;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeModel;
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeReasonCode;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentManagementExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentPlanDetailExcelVO;
import com.zufangbao.sun.yunxin.entity.model.OverdueChargesModifyModel;
import com.zufangbao.sun.yunxin.entity.model.QueryAppModel;
import com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail;
import com.zufangbao.sun.yunxin.entity.model.api.ExtraChargeSpec;
import com.zufangbao.sun.yunxin.entity.model.assetset.AssetSetQueryModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.AssetSetShowModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.OverdueFeeShowModel;
import com.zufangbao.sun.yunxin.entity.model.prepayment.PrepaymentDetailModel;
import com.zufangbao.sun.yunxin.entity.model.prepayment.PrepaymentQueryModel;
import com.zufangbao.sun.yunxin.entity.model.prepayment.PrepaymentShowModel;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.MutableFeeDetailLog;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.service.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.BalanceRefundOrderShowModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.RepaymentPlanDocumentModel;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.RepaymentRecordDetail;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.LedgerBookVirtualAccountHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.VoucherHandler;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.yunxin.PaymentRecordAssetModel;
import com.zufangbao.wellsfargo.yunxin.handler.JournalVoucherHandler;
import com.zufangbao.wellsfargo.yunxin.handler.ModifyOverDueFeeHandlerProxy;
import com.zufangbao.wellsfargo.yunxin.handler.PrepaymentHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RefundOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentPlanHandlerNoSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import com.zufangbao.sun.yunxin.entity.api.AssetRefundMode;

/**
 * @author louguanyang
 */
@Controller
@RequestMapping("/assets")
@MenuSetting("menu-finance")
public class AssetSetController extends BaseController {

    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private PrepaymentApplicationService prepaymentApplicationService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SettlementOrderService settlementOrderService;
    @Autowired
    private TransferApplicationService transferApplicationService;
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private AssetPackageService assetPackageService;
    @Autowired
    private AppService appService;
    @Autowired
    private PrepaymentHandler prepaymentHandler;
    @Autowired
    private LedgerBookService ledgerBookService;
    @Autowired
    private LedgerBookStatHandler ledgerBookStatHandler;
    @Autowired
    private LedgerBookVirtualAccountHandler ledgerBookVirtualAccountHandler;
    @Autowired
    private DeductApplicationCoreOperationHandler deductApplicationHandler;
    @Autowired
    private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;
    @Autowired
    private SystemOperateLogService systemOperateLogService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private LedgerItemService ledgerItemService;
    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;
    @Autowired
    private PrincipalHandler principalHandler;
    @Autowired
    private DeductPlanService deductPlanService;
    @Autowired
    private VoucherHandler voucherHandler;
    @Autowired
    private JournalVoucherService journalVoucherService;
    @Autowired
    private DeductApplicationService deductApplicationService;
    @Autowired
    private DeductApplicationCoreOperationHandler deductApplicationCoreOperationHandler;
    @Autowired
    private ExtraChargeSnapShotService overdueFeeLogService;
    private static final Log logger = LogFactory.getLog(AssetSetController.class);
    @Autowired
    private FinancialContractService financialContractService;
    @Autowired
    private ModifyOverDueFeeHandlerProxy modifyOverDueFeeHandlerProxy;
    @Autowired
    MutableFeeDetailApiHandler mutableFeeDetailApiHandler;
    @Autowired
    private RepaymentPlanHandlerNoSession repaymentPlanHandlerNoSession;

    @Autowired
	private JournalVoucherHandler journalVoucherHandler;
	@Autowired
	private RepaymentOrderHandler repaymentOrderHandler;
	@Autowired
	private RefundOrderHandler refundOrderHandler;
	@Autowired
	private SystemOperateLogHandler systemOperateLogHandler;
	@Autowired
	private RefundOrderService refundOrderService;
	@Autowired
	private VirtualAccountService virtualAccountService;
	
	@RequestMapping("")
    @MenuSetting("submenu-payment-asset")
    public ModelAndView detail() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/{assetSetUuid}/detail", params = "format=json")
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String getRepaymentPlanDetailData(@PathVariable("assetSetUuid") String assetSetUuid) {
        try {
            AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
            Contract contract = repaymentPlan.getContract();
            ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(repaymentPlan.getContract());
            RepaymentChargesDetail planChargesDetail = repaymentPlanHandler.getPlanRepaymentChargesDetail(repaymentPlan);
            //获取逾期费用实收金额
            OverdueFeeShowModel overdueActualPriceShowModel = getOverdueActualPrice(repaymentPlan);
            Date chargesModifyTime = repaymentPlanExtraChargeService.getExtraChargesLastModifyTime(repaymentPlan.getAssetUuid());
            LedgerBook book = ledgerBookService.getBookByAsset(repaymentPlan);
            BigDecimal accountBalance = ledgerBookVirtualAccountHandler.get_balance_of_customer(book.getLedgerBookNo(), contract.getCustomerUuid());

            BigDecimal actualPaidAmount = BigDecimal.ZERO;
            if (repaymentPlan.isPaidOff()) {
                actualPaidAmount = repaymentPlan.getAmount();
            } else if (repaymentPlan.getOnAccountStatus() == OnAccountStatus.PART_WRITE_OFF) {
                actualPaidAmount = ledgerBookStatHandler.get_banksaving_amount_of_asset(book.getLedgerBookNo(), repaymentPlan.getAssetUuid());
            }

            List<DeductApplication> deductApplications = new ArrayList<DeductApplication>();
            String code = repaymentPlan.getSingleLoanContractNo();
            Map<String, List<DeductApplication>> deductApplciationMap = deductApplicationCoreOperationHandler.getDeductApplicationDetailInRepaymentPlanCodeList
                    (Arrays.asList(code));
            if (deductApplciationMap != null) {
                deductApplications = deductApplciationMap.get(code);
            }
            List<String> deductApplicationUuidList = deductApplicationCoreOperationHandler.getDeductApplicationUuidListByDeductApplications(deductApplications);
            List<DeductPlan> deductPlans = deductPlanService.getDeductPlanByDeductApplicationUuidList(deductApplicationUuidList);
            List<Order> allOrderList = orderService.getOrderListByAssetSetUuid(repaymentPlan.getAssetUuid());
            List<Order> normalOrders = allOrderList.stream().filter(o -> o.isNormalOrder()).collect(Collectors.toList());
            List<Order> guaranteeOrders = allOrderList.stream().filter(o -> o.isGuaranteeOrder()).collect(Collectors.toList());
            List<SettlementOrder> settlementOrders = settlementOrderService.getSettlementOrderListBy(repaymentPlan);
            List<JournalVoucher> journalVouchers = journalVoucherService.getBalancePaymentBillBy(repaymentPlan.getAssetUuid());
            List<PaymentRecordAssetModel> paymentRecords = getPaymentRecords(deductPlans, journalVouchers);
            List<RepaymentPlanDocumentModel> vouchers = voucherHandler.genRepaymentPlanDocumentModel(repaymentPlan);
			List<RepaymentRecordDetail> repaymentRecordDetails= journalVoucherHandler
					.getRepaymentRecordDetailListByAssetSet(repaymentPlan.getAssetUuid());
            boolean allowSplitSettlement = repaymentPlanHandler.allowSplitSettlement(repaymentPlan);
			FinancialContract financialContract = assetPackageService.getFinancialContract(repaymentPlan.getContract());
			List<RepaymentChargesDetail> amountClassifyList = repaymentPlanHandler.getRepaymentChargesDetailList(repaymentPlan);
			//入账资金明细
			RepaymentChargesDetail actualChargesDetail = amountClassifyList.get(0);
			//在途资金明细
			RepaymentChargesDetail intransitChargesDetail = amountClassifyList.get(1);
			//实还总资金明细
			RepaymentChargesDetail paidUpChargesDetail = new RepaymentChargesDetail(ledgerBookStatHandler.get_banksaving_amount_of_asset(book, repaymentPlan
	                    .getAssetUuid()));

			AssetSetDetailShowModel data = new AssetSetDetailShowModel(repaymentRecordDetails,financialContract,repaymentPlan, contractAccount,
					planChargesDetail, chargesModifyTime, contract, normalOrders, guaranteeOrders, settlementOrders, deductApplications, paymentRecords, 
					accountBalance, overdueActualPriceShowModel, vouchers, true, allowSplitSettlement, actualPaidAmount,actualChargesDetail,
					intransitChargesDetail,paidUpChargesDetail,null);

			Map<String, Object> result = new HashMap<String, Object>();
			result.put("data", data);
			result.put("auditOverdueStatusList", AuditOverdueStatus.getOrdinalMsgMap());
			result.put("mutableFeeReasonCode", EnumUtil.getKVList(MutableFeeReasonCode.class));
			return jsonViewResolver.sucJsonResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(GlobalCodeSpec.CODE_FAILURE);
		}
	}

    private List<PaymentRecordAssetModel> getPaymentRecords(List<DeductPlan> dps, List<JournalVoucher> jvs) {
        List<PaymentRecordAssetModel> models = new ArrayList<PaymentRecordAssetModel>();
        if (CollectionUtils.isNotEmpty(dps)) {
            for (DeductPlan dp : dps) {
                DeductApplication dApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(dp.getDeductApplicationUuid());
                models.add(new PaymentRecordAssetModel(dp, dApplication));
            }
        }
        if (CollectionUtils.isNotEmpty(jvs)) {
            models.addAll(jvs.stream().map(PaymentRecordAssetModel::new).collect(Collectors.toList()));
        }
        return models.stream().sorted((o1, o2) -> o2.getStateChangeTime().compareTo(o1.getStateChangeTime())).collect(Collectors.toList());
    }

    /**
     * 提前还款作废
     */
    @RequestMapping(value = "/invalidate", method = RequestMethod.POST)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String invalidate(HttpServletRequest request,
                      @Secure Principal principal,
                      @RequestParam(value = "assetUuid") String assetUuid,
                      @RequestParam(value = "comment", required = false) String comment) {
        try {
            AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid);
            if (repaymentPlan == null) {
                return jsonViewResolver.errorJsonResult("还款计划不存在！");
            }

            PrepaymentApplication application = prepaymentApplicationService.getUniquePrepaymentApplicationByRepaymentPlanUuid(repaymentPlan.getAssetUuid());
            String errMsg = prepaymentHandler.invalid_prepayment_and_undo_frozen_be_pred_repayment_plan(application, repaymentPlan);
            if (StringUtils.isNotBlank(errMsg)) {
                return jsonViewResolver.errorJsonResult(errMsg);
            }

            String recordContent = "作废提前还款计划:" + repaymentPlan.getAssetUuid() + ",作废原因:" + comment;
            SystemOperateLog log = SystemOperateLog.createAssetSetLog(principal.getId(), repaymentPlan, recordContent, IpUtil.getIpAddress(request),
                    LogFunctionType.ASSET_SET_INVALIDATE, LogOperateType.INVALIDATE);
            systemOperateLogService.save(log);

            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            logger.error("##invalidate## occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("操作失败，请稍后重试");
        }
    }


    /**
     * 逾期费用更新
     */
    @RequestMapping(value = "/charges", params = "type=overdue", method = RequestMethod.POST)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String editOverdueCharges(String repaymentPlanUuid, String overdueCharges,
                              @Secure Principal principal, HttpServletRequest request) {
        try {
            AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(repaymentPlanUuid);
            if (repaymentPlan == null) {
                return jsonViewResolver.errorJsonResult("逾期费用更新失败，原因［还款计划不存在］");
            }
            if (!repaymentPlan.isExecutoryRepaymentPlan()) {
                return jsonViewResolver.errorJsonResult("逾期费用更新失败，原因［非执行中的还款计划］");
            }
            // 作废、扣款中、回购的还款计划，不允许更新逾期费用
            String activeDeductApplicationUuid = repaymentPlan.getActiveDeductApplicationUuid();
            if (!AssetSet.EMPTY_UUID.equals(activeDeductApplicationUuid)) {
                return jsonViewResolver.errorJsonResult("逾期费用更新失败，原因［还款计划已被锁定］");
            }
            OverdueChargesModifyModel overdueChargesModel = JsonUtils.parse(overdueCharges, OverdueChargesModifyModel.class);
            if (overdueChargesModel == null) {
                return jsonViewResolver.errorJsonResult("逾期费用更新失败，原因［逾期费用明细格式有误］");
            }
            if (!overdueChargesModel.isValid()) {
                return jsonViewResolver.errorJsonResult(overdueChargesModel.getCheckFailedMsg());
            }
            //放开运营对逾期费用的修改
//			FinancialContract financialContract = financialContractService.getFinancialContractBy(repaymentPlan.getFinancialContractUuid());
//			if(!financialContract.isSysCreatePenaltyFlag()){
//				return jsonViewResolver.errorJsonResult("逾期费用更新失败，原因[接口传输,不允许更改逾期费用］");
//			}
            RepaymentChargesDetail oldCharges = repaymentPlanHandler.getPlanRepaymentChargesDetail(repaymentPlan);

            OverdueChargesModifyModelParameter overdueChargesModifyModelParameter = new OverdueChargesModifyModelParameter(overdueChargesModel.getOverdueFeePenalty(),
                    overdueChargesModel.getOverdueFeeObligation(), overdueChargesModel.getOverdueFeeService(), overdueChargesModel.getOverdueFeeOther(),
                    repaymentPlan.getSingleLoanContractNo(), repaymentPlan.getRepayScheduleNo());
            modifyOverDueFeeHandlerProxy.modifyOverdueCharges(repaymentPlan.getContractUuid(), repaymentPlanUuid, overdueChargesModifyModelParameter, Priority
                    .RealTime.getPriority());

            // 保存逾期费用更新日志
            try {
                createAndSaveOverdueChargesModifyLog(principal, request, repaymentPlan, oldCharges, overdueChargesModel);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("修改逾期费用，日志记录异常！");
            }

            return jsonViewResolver.sucJsonResult();
        } catch (InvalidModifyBalanceException e1) {
            e1.printStackTrace();
            return jsonViewResolver.errorJsonResult("逾期费用更新失败，原因［逾期费用明细金额不得低于已实收金额］");
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("逾期费用更新失败，原因［系统错误］");
        }
    }

    private void createAndSaveOverdueChargesModifyLog(Principal principal, HttpServletRequest request,
                                                      AssetSet repaymentPlan, RepaymentChargesDetail oldCharges, OverdueChargesModifyModel newCharges) {
        BigDecimal oldPenalty = oldCharges.getOverdueFeePenalty();
        BigDecimal oldObligation = oldCharges.getOverdueFeeObligation();
        BigDecimal oldService = oldCharges.getOverdueFeeService();
        BigDecimal oldOther = oldCharges.getOverdueFeeOther();

        BigDecimal newPenalty = newCharges.getOverdueFeePenalty();
        BigDecimal newObligation = newCharges.getOverdueFeeObligation();
        BigDecimal newService = newCharges.getOverdueFeeService();
        BigDecimal newOther = newCharges.getOverdueFeeOther();

        StringBuffer logSb = new StringBuffer();
        logSb.append(getLogContentForOverdueCharges("逾期罚息", oldPenalty, newPenalty));
        logSb.append(getLogContentForOverdueCharges("逾期违约金", oldObligation, newObligation));
        logSb.append(getLogContentForOverdueCharges("逾期服务费", oldService, newService));
        logSb.append(getLogContentForOverdueCharges("逾期其他费用", oldOther, newOther));
        String logContent = logSb.toString();

        //系统操作日志
        SystemOperateLog systemOperateLog = SystemOperateLog.createOverdueChargesEditLog(principal.getId(),
                repaymentPlan, logContent, IpUtil.getIpAddress(request), LogFunctionType.MANUAL_EDIT_OVERDUE_CHARGES,
                LogOperateType.UPDATE);
        systemOperateLogService.save(systemOperateLog);
    }

    private String getLogContentForOverdueCharges(String item, BigDecimal oldValue, BigDecimal newValue) {
        if (!AmountUtils.equals(oldValue, newValue)) {
            return String.format("%s由【%s】变更为【%s】，", item, oldValue, newValue);
        }
        return "";
    }


    @RequestMapping(value = "/{assetId}/update-refund", method = RequestMethod.POST)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String updateRefund(@Secure Principal principal, @PathVariable("assetId") Long assetId,
                        @RequestParam(value = "refundAmount") BigDecimal refundAmount, @RequestParam("comment") String comment) {
        try {
            AssetSet assetSet = repaymentPlanService.load(AssetSet.class, assetId);
            if (assetSet == null || !assetSet.isClearAssetSet()) {
                return jsonViewResolver.errorJsonResult("还款信息不存在或未还款！");
            }
            if (refundAmount == null) {
                return jsonViewResolver.errorJsonResult("请输入退款金额！");
            }
            assetSet.setRefundAmount(refundAmount);
            assetSet.setComment(comment);
            repaymentPlanService.save(assetSet);
            logger.info("refund: assetId[" + assetId + "],principalName[" + principal.getName() + "],refundAmount["
                    + refundAmount + "],comment[" + comment + "].");
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    @RequestMapping(value = "options", method = RequestMethod.GET)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String getRepaymentPlansPageOptions(@Secure Principal principal, Page page, HttpServletRequest request) {
        try {
            Map<String, Object> result = new HashMap();

            List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
            List<QueryAppModel> queryAppModels = financialContractService.getQueryAppModelsBy(financialContracts);

            result.put("queryAppModels", queryAppModels);
            result.put("paymentStatusList", EnumUtil.getKVListIncludes(RepaymentExecutionState.class, Arrays.asList(RepaymentExecutionState.PROCESSING,
                    RepaymentExecutionState.DEDUCTING, RepaymentExecutionState.SUCCESS, RepaymentExecutionState.PAYMENTING)));
            result.put("auditOverdueStatusList", EnumUtil.getKVList(AuditOverdueStatus.class));// 逾期状态
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取配置数据错误");
        }
    }

    @RequestMapping("/query")
    @MenuSetting("submenu-financial-contract")
    public @ResponseBody
    String queryRepaymentPlans(@Secure Principal principal,
                               @ModelAttribute AssetSetQueryModel assetSetQueryModel, Page page) {
        try {
            assetSetQueryModel.setActiveStatus(AssetSetActiveStatus.OPEN.ordinal());
            int size = repaymentPlanHandler.countAssetSetIds(assetSetQueryModel);
            List<AssetSetShowModel> assetSetModelList = repaymentPlanHandler.generateAssetSetModelList(assetSetQueryModel, page);
            Map<String, Object> data = new HashMap<>();
            data.put("assetSetQueryModel", assetSetQueryModel);
            data.put("list", assetSetModelList);
            data.put("size", size);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    //费用弹框
    @RequestMapping(value = "/{assetSetUuid}/feeDetail", method = RequestMethod.GET)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String getFeeDetail(@PathVariable("assetSetUuid") String assetSetUuid) {
        try {
            AssetSet repaymentPlan = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
            LedgerBook book = ledgerBookService.getBookByAsset(repaymentPlan);

            RepaymentChargesDetail planChargesDetail = repaymentPlanHandler.getPlanRepaymentChargesDetail(repaymentPlan);
            RepaymentChargesDetail paidUpChargesDetail = new RepaymentChargesDetail(ledgerBookStatHandler.get_banksaving_amount_of_asset(book, repaymentPlan
                    .getAssetUuid()));

            BigDecimal unPaidPrincipalValue = planChargesDetail.getLoanAssetPrincipal().subtract(paidUpChargesDetail.getLoanAssetPrincipal());
            BigDecimal unPaidInterestValue = planChargesDetail.getLoanAssetInterest().subtract(paidUpChargesDetail.getLoanAssetInterest());
            BigDecimal unPaidTotalOtherFee = planChargesDetail.getTotalFee().subtract(paidUpChargesDetail.getTotalFee()).subtract(unPaidPrincipalValue).subtract
                    (unPaidInterestValue);
            Map<String, Object> unPaid = new HashMap<String, Object>();
            unPaid.put("unPaidPrincipalValue", unPaidPrincipalValue);
            unPaid.put("unPaidInterestValue", unPaidInterestValue);
            unPaid.put("unPaidTotalOtherFee", unPaidTotalOtherFee);

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("planChargesDetail", planChargesDetail);
            result.put("paidUpChargesDetail", paidUpChargesDetail);
            result.put("unPaidDetail", unPaid);

            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("费用查询失败！");
        }
    }

    // 还款列表-统计金额（应还款金额、实际还款金额、差值（应还款-实际还款））
    @RequestMapping("/amountStatistics")
    @MenuSetting("submenu-financial-contract")
    public @ResponseBody
    String amountStatistics(@ModelAttribute AssetSetQueryModel assetSetQueryModel) {
        try {
            assetSetQueryModel.setActiveStatus(AssetSetActiveStatus.OPEN.ordinal());
            Map<String, Object> data = repaymentPlanHandler.amountStatistics(assetSetQueryModel);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("还款相关金额统计错误");
        }
    }

    // 还款列表 -计划还款信息预览
    @RequestMapping(value = "/query/{assetSetId}/repaymentInfo", method = RequestMethod.GET)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String repaymentInfo(@PathVariable("assetSetId") Long assetId) {
        try {
            AssetSet assetSet = repaymentPlanService.load(AssetSet.class, assetId);
            Map<String, Object> data = new HashMap<>();
            data.put("assetPrincipalValue", assetSet.getAssetPrincipalValue());
            data.put("assetInterestValue", assetSet.getAssetInterestValue());
            data.put("assetInitialValue", assetSet.getAssetInitialValue());
            data.put("overDueDays", assetSet.getOverDueDays());
            data.put("penaltyInterestAmount", assetSet.getPenaltyInterestAmount());
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    @RequestMapping(value = "/{assetUuid}/update-comment", method = RequestMethod.POST)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String updateComment(@Secure Principal principal, @PathVariable("assetUuid") String assetUuid,
                         @RequestParam("comment") String comment, HttpServletRequest request) {
        try {
            logger.info("update comment: assetUuid[" + assetUuid + "], principalName[" + principal.getName() + "], comment["
                    + comment + "].");
            AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid);
            if (assetSet == null) {
                return jsonViewResolver.errorJsonResult("还款信息不存在！");
            }
            repaymentPlanHandler.updateCommentAndSaveLog(assetUuid, principal.getId(), comment, IpUtil.getIpAddress(request));
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    @RequestMapping(value = "/{assetUuid}/confirm-overdue", method = RequestMethod.POST)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String confirmOverdue(@Secure Principal principal, @PathVariable("assetUuid") String assetUuid,
                          @RequestParam("status") int status, @RequestParam("reason") String reason,
                          @RequestParam(value = "overdueDate", required = false, defaultValue = "") String overdueDate,
                          HttpServletRequest request) {
        try {
            logger.info("confirm overdue: assetUuid[" + assetUuid + "], principalName[" + principal.getName() + "], status["
                    + status + "], reason[" + reason + "], overdueDate[" + overdueDate + "].");
            AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid);
            FinancialContract financialContract = assetPackageService.getFinancialContract(assetSet.getContract());
            String errorMsg = checkParamReturnMsg(status, overdueDate, assetSet, financialContract);
            if (!StringUtils.isEmpty(errorMsg)) {
                return jsonViewResolver.errorJsonResult(errorMsg);
            }
            repaymentPlanHandler.updateOverdueStatusAndSaveLog(assetUuid, principal.getId(), AuditOverdueStatus.fromValue(status), reason, overdueDate, IpUtil
                    .getIpAddress(request));
            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    @Deprecated
    @RequestMapping(value = "exprot-repayment-plan-detail", method = RequestMethod.GET)
    @MenuSetting("submenu-assets-contract")
    public String exportRepaymentPlanDetail(HttpServletRequest request, @Secure Principal principal,
                                            @ModelAttribute AssetSetQueryModel assetSetQueryModel, HttpServletResponse response) {
        try {
            long start = System.currentTimeMillis();
            // 默认导出excel只导出有效的还款单，若要按页面筛选，则删去。
            assetSetQueryModel.setActiveStatus(AssetSetActiveStatus.OPEN.ordinal());

            List<RepaymentPlanDetailExcelVO> repaymentPlanDetailExcelVOs = repaymentPlanHandler.get_repayment_plan_Detail_excel(assetSetQueryModel, null);

            ExcelUtil<RepaymentPlanDetailExcelVO> excelUtil = new ExcelUtil<RepaymentPlanDetailExcelVO>(RepaymentPlanDetailExcelVO.class);
            List<String> csvData = excelUtil.exportDatasToCSV(repaymentPlanDetailExcelVOs);

            Map<String, List<String>> csvs = new HashMap<String, List<String>>();
            csvs.put("还款计划明细汇总表", csvData);
            exportZipToClient(response, create_repaymnet_plan_summary_file_name("zip"), GlobalSpec.UTF_8, csvs);
            logger.info("#exportRepaymentPlanDetail end. used [" + (System.currentTimeMillis() - start) + "]ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 还款计划明细汇总表--导出预览
    @Deprecated
    @RequestMapping(value = "preview-exprot-repayment-plan-detail", method = RequestMethod.GET)
    @MenuSetting("submenu-assets-contract")
    public @ResponseBody
    String previewExportRepaymentPlanDetail(HttpServletRequest request, @Secure Principal principal,
                                            @ModelAttribute AssetSetQueryModel assetSetQueryModel, HttpServletResponse response) {
        try {
            assetSetQueryModel.setActiveStatus(AssetSetActiveStatus.OPEN.ordinal());
            Page page = new Page(0, 10);
            List<RepaymentPlanDetailExcelVO> repaymentPlanDetailExcelVOs = repaymentPlanHandler.get_repayment_plan_Detail_excel(assetSetQueryModel, page);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("list", repaymentPlanDetailExcelVOs);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#previewExportRepaymentPlanDetail  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("还款计划明细汇总表导出预览失败");
        }
    }

    //	@RequestMapping(value = "exprot-repayment-management", method = RequestMethod.GET)
//	@MenuSetting("submenu-assets-contract")
    @Deprecated
    public String exportRepaymentManagement(HttpServletRequest request,
                                            @Secure Principal principal,
                                            @ModelAttribute AssetSetQueryModel assetSetQueryModel,
                                            HttpServletResponse response) {
        ExportEventLogModel exportEventLogModel = new ExportEventLogModel("5", principal);
        try {
            exportEventLogModel.recordStartLoadDataTime();

            // 默认导出excel只导出有效的还款单，若要按页面筛选，则删去。
            assetSetQueryModel.setActiveStatus(AssetSetActiveStatus.OPEN.ordinal());
            List<AssetSetShowModel> assetSetModelList = repaymentPlanHandler.generateAssetSetModelList(assetSetQueryModel, null);

            List<String> assetSetUuids = assetSetModelList.stream().map(as -> as.getAssetSetUuid()).collect(Collectors.toList());
            Map<String, RepaymentChargesDetail> chargesMap = repaymentPlanHandler.getRepaymentCharges(assetSetUuids);
//			List<RepaymentManagementExcelVO> excelVOs = assetSetModelList.stream().map(assetSet -> new RepaymentManagementExcelVO(assetSet, chargesMap.get(assetSet
// .getAssetSetUuid()))).collect(Collectors.toList());
            List<RepaymentManagementExcelVO> excelVOs = new ArrayList<RepaymentManagementExcelVO>();
            for (AssetSetShowModel assetSet : assetSetModelList) {
                Contract contract = contractService.getContract(assetSet.getContractId());
                ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
                RepaymentManagementExcelVO vo = new RepaymentManagementExcelVO(assetSet, contractAccount, chargesMap.get(assetSet.getAssetSetUuid()));
                excelVOs.add(vo);
            }

            exportEventLogModel.recordAfterLoadDataComplete(excelVOs.size());

            ExcelUtil<RepaymentManagementExcelVO> excelUtil = new ExcelUtil<RepaymentManagementExcelVO>(RepaymentManagementExcelVO.class);
            List<String> csvData = excelUtil.exportDatasToCSV(excelVOs);

            Map<String, List<String>> csvs = new HashMap<String, List<String>>();
            csvs.put("还款管理表", csvData);
            exportZipToClient(response, cretae_repayment_management_file_name("zip"), GlobalSpec.UTF_8, csvs);

            exportEventLogModel.recordEndWriteOutTime();
        } catch (Exception e) {
            e.printStackTrace();
            exportEventLogModel.setErrorMsg(e.getMessage());
        } finally {
            logger.info("#export report, record export event log info. " + JsonUtils.toJSONString(exportEventLogModel));
        }
        return null;
    }

    // 还款管理表--导出预览
    @RequestMapping(value = "preview-exprot-repayment-management", method = RequestMethod.GET)
    @MenuSetting("submenu-assets-contract")
    public @ResponseBody
    String previewExportRepaymentManagement(@ModelAttribute AssetSetQueryModel assetSetQueryModel) {
        try {
            assetSetQueryModel.setActiveStatus(AssetSetActiveStatus.OPEN.ordinal());
            Page page = new Page(0, 10);
            List<AssetSetShowModel> assetSetModelList = repaymentPlanHandler.generateAssetSetModelList(assetSetQueryModel, page);

            List<String> assetSetUuids = assetSetModelList.stream().map(as -> as.getAssetSetUuid()).collect(Collectors.toList());
            Map<String, RepaymentChargesDetail> chargesMap = repaymentPlanHandler.getRepaymentCharges(assetSetUuids);
//			List<RepaymentManagementExcelVO> excelVOs = assetSetModelList.stream().map(assetSet -> new RepaymentManagementExcelVO(assetSet, chargesMap.get(assetSet
// .getAssetSetUuid()))).collect(Collectors.toList());
            List<RepaymentManagementExcelVO> excelVOs = new ArrayList<RepaymentManagementExcelVO>();
            for (AssetSetShowModel assetSet : assetSetModelList) {
                Contract contract = contractService.getContract(assetSet.getContractId());
                ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
                RepaymentManagementExcelVO vo = new RepaymentManagementExcelVO(assetSet, contractAccount, chargesMap.get(assetSet.getAssetSetUuid()));
                excelVOs.add(vo);
            }

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("list", excelVOs);
            return jsonViewResolver.sucJsonResult(data, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            logger.error("#previewExportRepaymentManagement  occur error.");
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("还款管理表导出预览失败");
        }
    }

    private String create_overDue_download_file_name(String format) {
        return "逾期还款明细表" + "_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + "." + format;
    }

    private String cretae_repayment_management_file_name(String format) {
        return "还款管理表" + "_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + "." + format;
    }

    private String create_repaymnet_plan_summary_file_name(String format) {
        return "还款计划明细汇总表" + "_" + DateUtils.format(new Date(), "yyyyMMdd_HHmmssSSS") + "." + format;
    }

    private String checkParamReturnMsg(int status, String overdueDate, AssetSet assetSet,
                                       FinancialContract financialContract) {
        if (assetSet == null) {
            return "还款信息不存在！";
        }
        if (assetSet.getOverdueStatus() == AuditOverdueStatus.OVERDUE) {
            return "该还款计划已逾期";
        }
        AuditOverdueStatus overdueStatus = AuditOverdueStatus.fromValue(status);
        if (overdueStatus == null) {
            return "请选择逾期状态";
        }
        if (overdueStatus == AuditOverdueStatus.OVERDUE) {
            if (StringUtils.isEmpty(overdueDate)) {
                return "请选择逾期日期";
            }
            Date inputOverdueDate = DateUtils.asDay(overdueDate);

            Date overdueStartDate = assetSet.getOverdueStartDate(financialContract.getLoanOverdueStartDay());
            if (inputOverdueDate.before(overdueStartDate)) {
                return "逾期日期应不早于" + DateUtils.format(overdueStartDate);
            }
            Date actualRecycleDate = DateUtils.asDay(new Date());
            if (assetSet.isClearAssetSet()) {
                actualRecycleDate = assetSet.getActualRecycleDate();
            }
            if (inputOverdueDate.after(actualRecycleDate)) {
                return "逾期日期应不晚于" + DateUtils.format(actualRecycleDate);
            }
        }
        return "";
    }

    /**
     * 提前还款页面展示
     *
     * @param principal
     * @param page
     * @return
     */
    @RequestMapping(value = "/prepayment/list", method = RequestMethod.GET)
    @MenuSetting("submenu-prepayment-order")
    public ModelAndView showPrepaymentPlansPage(@Secure Principal principal, Page page) {
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @RequestMapping(value = "/prepayment/options", method = RequestMethod.GET)
    public @ResponseBody
    String getPrepaymentPlansPageOptions(@Secure Principal principal, Page page) {
        try {
            List<FinancialContract> financialContracts = principalHandler.get_can_access_financialContract_list(principal);
            List<App> apps = appService.loadAll(App.class);
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("financialContracts", financialContracts);
            result.put("apps", apps);
            result.put("paymentStatusList", EnumUtil.getKVList(RepaymentExecutionState.class));
            result.put("prepaymentTypes", EnumUtil.getKVList(PrepaymentType.class));
            return jsonViewResolver.sucJsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("获取配置数据错误");
        }
    }

    /**
     * 条件查询对应的提前还款计划
     *
     * @param queryModel
     * @param page
     * @return
     */
    @RequestMapping(value = "/prepayment/query", method = RequestMethod.GET)
    @MenuSetting("submenu-prepayment-order")
    public @ResponseBody
    String queryPrepaymentPlan(@ModelAttribute PrepaymentQueryModel queryModel, Page page) {
        try {
            int size = repaymentPlanHandler.countPrepayentPlan(queryModel);
            List<PrepaymentShowModel> assetSetModelList = prepaymentHandler.generatePrepaymentModelList(queryModel, page);
            Map<String, Object> data = new HashMap<>();
            data.put("list", assetSetModelList);
            data.put("size", size);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 提前还款详情页线上支付单列表
     *
     * @param principal
     * @param assetSetId
     * @return
     */
    @RequestMapping(value = "/prepayment/{assetSetId}/transferApplication", method = RequestMethod.GET)
    @MenuSetting("submenu-prepayment-order")
    public @ResponseBody
    String listTransferApplication(@Secure Principal principal, @PathVariable("assetSetId") Long assetSetId) {
        try {
            AssetSet assetSet = repaymentPlanService.load(AssetSet.class, assetSetId);
            List<DeductPlanShowModel> showModels = new ArrayList<>();

            List<DeductPlan> deductPlans = deductApplicationHandler.getDeductPlanListByAssetSet(assetSet);

            if (!CollectionUtils.isEmpty(deductPlans)) {
                for (DeductPlan deductPlan : deductPlans) {
                    DeductPlanShowModel showModel = DeductPlanImporter.convertFromTransferApplication(deductPlan);
                    showModels.add(showModel);
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("list", showModels);
            data.put("size", showModels.size());
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("系统错误");
        }
    }

    /**
     * 查询逾期费用
     */

    @RequestMapping(value = "/overdueFee/query/{assetSetUuid}", method = RequestMethod.GET)
    @MenuSetting("submenu-payment-asset")
    public @ResponseBody
    String queryOverdueFee(@Secure Principal principal, @PathVariable("assetSetUuid") String assetSetUuid, Page rawpage, int pageNumber) {
        try {
            AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetSetUuid);
            Page page = rebuildPage(rawpage, pageNumber);
            List<ExtraChargeSnapShot> overdueFeeList = overdueFeeLogService.getOverdueFeeLogExceptLastByAssetSetUuid(assetSet.getAssetUuid(), page);
            List<OverdueFeeShowModel> overdueFeeShowModelList = new ArrayList<OverdueFeeShowModel>();
            for (ExtraChargeSnapShot overdueFeeLog : overdueFeeList) {
                OverdueFeeShowModel overdueFeeShowModel = new OverdueFeeShowModel("", overdueFeeLog.getOverdueFeePenalty(), overdueFeeLog.getOverdueFeeObligation(),
                        overdueFeeLog.getOverdueFeeService(), overdueFeeLog.getOverdueFeeOther(), overdueFeeLog.getCreateTime(), OverdueFeeShowModel.INVALID);
                overdueFeeShowModelList.add(overdueFeeShowModel);
            }
            int size = overdueFeeLogService.countOverdueFeeLogExceptLastByAssetSetUuid(assetSet.getAssetUuid());
            Map<String, Object> data = new HashMap<>();
            data.put("list", overdueFeeShowModelList);
            data.put("size", size);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("查询逾期费用错误");
        }
    }

    private OverdueFeeShowModel getOverdueActualPrice(AssetSet repaymentPlan) {
        FinancialContract financialContract = financialContractService.getFinancialContractBy(repaymentPlan.getFinancialContractUuid());
        String ledgerBookNo = financialContract.getLedgerBookNo();
        String assetSetUuid = repaymentPlan.getAssetUuid();
        BigDecimal overdueFeePenalty = ledgerBookStatHandler.get_banksaving_amount_of_asset(ledgerBookNo, assetSetUuid, ExtraChargeSpec.PENALTY_KEY);
        BigDecimal overdueFeeObligation = ledgerBookStatHandler.get_banksaving_amount_of_asset(ledgerBookNo, assetSetUuid, ExtraChargeSpec
                .OVERDUE_FEE_OBLIGATION_KEY);
        BigDecimal overdueFeeService = ledgerBookStatHandler.get_banksaving_amount_of_asset(ledgerBookNo, assetSetUuid, ExtraChargeSpec.OVERDUE_FEE_SERVICE_FEE_KEY);
        BigDecimal overdueFeeOther = ledgerBookStatHandler.get_banksaving_amount_of_asset(ledgerBookNo, assetSetUuid, ExtraChargeSpec.OVERDUE_FEE_OTHER_FEE_KEY);
        return new OverdueFeeShowModel(OverdueFeeShowModel.PAID_UP, overdueFeePenalty, overdueFeeObligation, overdueFeeService, overdueFeeOther, ledgerItemService
                .get_last_overdue_booked_date(ledgerBookNo, repaymentPlan.getAssetUuid()), "");
    }

    private Page rebuildPage(Page rawpage, int pageNumber) {
        Page page = new Page();
        page.setCurrentPage(rawpage.getCurrentPage());
        page.setEveryPage(pageNumber);
        page.setBeginIndex((page.getCurrentPage() - 1) * page.getEveryPage());
        return page;
    }

    private Page rebuildPage(int currentPage, int perPageRecordNumber) {
        Page page = new Page();
        page.setCurrentPage(currentPage);
        page.setEveryPage(perPageRecordNumber);
        page.setBeginIndex((page.getCurrentPage() - 1) * page.getEveryPage());
        return page;
    }

    /**
     * 浮动费用明细查询接口
     */
    @RequestMapping(value = "/mutableFee/query/{singleLoanContractNo}", method = RequestMethod.GET)
    public @ResponseBody
    String queryMutableFeeDetails(@Secure Principal principal, @PathVariable("singleLoanContractNo") String singleLoanContractNo, Page page,
                                  @RequestParam(value = "pageIndex") String pageIndex, @RequestParam(value = "perPageRecordNumber") String perPageRecordNumber) {
        try {
            page = rebuildPage(Integer.valueOf(pageIndex), Integer.valueOf(perPageRecordNumber));
            MutableFeeDetailsResultModel mutableFeeDetails = mutableFeeDetailApiHandler.queryMutableFeeDetails(singleLoanContractNo, page);
            Map<String, Object> data = new HashMap<>();
            data.put("list", mutableFeeDetails.getMutableFeeDetailLogs());
            data.put("size", mutableFeeDetails.getCount());
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("浮动费用明细查询费用错误");
        }
    }

    @RequestMapping(value = "/mutableFee/queryOriginal/{singleLoanContractNo}", method = RequestMethod.GET)
    public @ResponseBody
    String queryOriginalMutableFeeDetail(@Secure Principal principal, @PathVariable("singleLoanContractNo") String singleLoanContractNo) {
        try {
            MutableFeeDetailLog mutableFeeDetailLog = mutableFeeDetailApiHandler.getMutableFeeDetailLog(singleLoanContractNo);
            Map<String, Object> data = new HashMap<>();
            data.put("model", mutableFeeDetailLog);
            return jsonViewResolver.sucJsonResult(data);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("浮动费用明细查询费用错误");
        }
    }

    @RequestMapping(value = "/mutableFee/modify", method = RequestMethod.POST)
    public @ResponseBody
    String modifyMutableFee(@Secure Principal principal, @ModelAttribute CreateMutableFeeModel createMutableFeeModel,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        try {
            MutableFeeModel mutableFeeModel = repaymentPlanHandlerNoSession.getMutableFeeModelFrom(createMutableFeeModel, principal.getUsername());
            if (null == mutableFeeModel)
                return jsonViewResolver.errorJsonResult("修改浮动费用错误");
            repaymentPlanHandlerNoSession.mutableFeeV2(mutableFeeModel, IpUtil.getIpAddress(request), principal.getId(), Priority.RealTime.getPriority());

            return jsonViewResolver.sucJsonResult();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonViewResolver.errorJsonResult("修改浮动费用错误");
        }
    }

	@RequestMapping(value="/{assetSetUuid}/queryRepaymentOrder", method= RequestMethod.GET)
	public @ResponseBody String queryRepaymentOrderByAssetId(@PathVariable("assetSetUuid") String assetSetUuid,Page page){
		try{
			if(StringUtils.isEmpty(assetSetUuid)){
				throw new ApiException("还款计划编号不能为空");
			}
			Map<String, Object> data=repaymentOrderHandler.buildRepaymentOrderSimpleModelMap(assetSetUuid, page);
			
			return jsonViewResolver.sucJsonResult(data);
		}catch (Exception e) {
			return jsonViewResolver.errorJsonResult(e, "系统错误");
		}
	}
	
	/**
	 * 资产退款
	 * @param AssetRefundMode
	 * @param principal
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/asset/refund", method = RequestMethod.POST)
	public @ResponseBody String	assetRefund(@ModelAttribute AssetRefundMode assetRefundMode,
			@Secure Principal principal,HttpServletRequest request){
		
//		RefundOrder refundOrder = null;
		
		try {
			
//			if(assetRefundMode.detailTotalAmount().compareTo(assetRefundMode.getTotalAmount()) != 0) {
//				throw new ApiException("退款明细金额与退款总金额不一致!");
//			}
//			
//			if(assetRefundMode.detailTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
//				throw new ApiException("退款金额应大于0");
//			}
//			
//			AssetSet assetSet = repaymentPlanService.getUniqueRepaymentPlanByUuid(assetRefundMode.getAssetUuid());
//			String contractUuid = assetSet.getContractUuid();
//			
//			//创建退款单
//			refundOrder = refundOrderHandler.createRefundOrder(assetRefundMode, contractUuid);
//			if(refundOrder == null) {
//				return jsonViewResolver.errorJsonResult("创建退款单错误");
//			}
//	      	logger.info("create refundOrder ，refundOrderUuid：["+refundOrder.getUuid()+"] and contractUuid：["+contractUuid+"]");
//			
//	      	//队列  异步 
//			logger.info("发送退款消息，contractUuid：["+contractUuid+"]");
//			refundOrderHandler.handleAssetRefund(contractUuid,refundOrder.getUuid(), assetRefundMode,  Priority.High.getPriority());
//			logger.info("退款成功，contractUuid：["+contractUuid+"],refundOrderNo:["+refundOrder.getRefundOrderNo()+"]");
//			
//			//退款单   log
//			SystemOperateLogRequestParam param = getSystemOperateLogrequestParamCreateRefund(principal, request,refundOrder);
//			systemOperateLogHandler.generateSystemOperateLog(param);
			
			return jsonViewResolver.sucJsonResult();
			
		} catch (Exception e) {
			
//			refundOrderService.updateRefundStatusByRefundOrderUuid(refundOrder.getUuid(), RefundStatus.REFUNDING);
			
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e, "系统错误");
		}
		
	}

	private SystemOperateLogRequestParam getSystemOperateLogrequestParamCreateRefund(Principal principal,
			HttpServletRequest request,RefundOrder refundOrder ) {
		SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(principal.getId(),
                IpUtil.getIpAddress(request), "退款金额为" + refundOrder.getAmount() + ",退款单号为[" + refundOrder.getRefundOrderNo() + "]", LogFunctionType.ASSET_REFUND,

                LogOperateType.ADD, RefundOrder.class, refundOrder, null, null);
		return param;

	}

}
