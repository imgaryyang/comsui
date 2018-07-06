package com.zufangbao.earth.report.factory;

import com.zufangbao.earth.report.constant.ExportOpsReportCodes;
import com.zufangbao.earth.report.model.ReportJobBaseModel;
import com.zufangbao.earth.report.wrapper.*;
import com.zufangbao.earth.report.wrapper.IReportWrapper;
import com.zufangbao.earth.report.wrapper.ReportWrapper1;
import com.zufangbao.earth.report.wrapper.ReportWrapper11;
import com.zufangbao.earth.report.wrapper.ReportWrapper12;
import com.zufangbao.earth.report.wrapper.ReportWrapper17;
import com.zufangbao.earth.report.wrapper.ReportWrapper18;
import com.zufangbao.earth.report.wrapper.ReportWrapper19;
import com.zufangbao.earth.report.wrapper.ReportWrapper2;
import com.zufangbao.earth.report.wrapper.ReportWrapper20;
import com.zufangbao.earth.report.wrapper.ReportWrapper21;
import com.zufangbao.earth.report.wrapper.ReportWrapper3;
import com.zufangbao.earth.report.wrapper.ReportWrapper4;
import com.zufangbao.earth.report.wrapper.ReportWrapper5;
import com.zufangbao.earth.report.wrapper.ReportWrapper6;
import com.zufangbao.earth.report.wrapper.ReportWrapper7;
import com.zufangbao.earth.report.wrapper.ReportWrapper8;
import com.zufangbao.earth.report.wrapper.ReportWrapper9;
import com.zufangbao.earth.report.wrapper.async.AsyncReportWrapper16;
import com.zufangbao.earth.report.wrapper.async.IAsyncReportWrapper;
import com.zufangbao.sun.yunxin.entity.deduct.DeductApplicationQeuryModel;
import com.zufangbao.sun.yunxin.entity.model.BankReconciliationQueryModel;
import com.zufangbao.sun.yunxin.entity.model.ContractQueryModel;
import com.zufangbao.sun.yunxin.entity.model.GuaranteeOrderModel;
import com.zufangbao.sun.yunxin.entity.model.SettlementOrderQueryModel;
import com.zufangbao.sun.yunxin.entity.model.assetset.AssetSetQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentRecordQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentPlan.OverdueAssetQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repaymentPlan.RepaymentPlanQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.InterestQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.LoansQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseQueryModel;
import com.zufangbao.wellsfargo.yunxin.model.ThirdPartyVoucherCommandLogQueryModel;

import java.util.HashMap;
import java.util.Map;

/**
 * 导出配置信息
 * @author zhanghongbing
 *
 */
public class ExportConfiguration {

	public final static Map<String, Class<?>> REPORT_CODES_PARAMS_CLASS_MAPPER = new HashMap<String, Class<?>>() {
		private static final long serialVersionUID = -6785331267408950537L;
		{
			put(ExportOpsReportCodes.REPORT_ASSET_CONTRACT_LIST, ContractQueryModel.class);
			put(ExportOpsReportCodes.REPORT_ASSET_BATCH_LIST_REPAYMENT_PLAN, HashMap.class);
			put(ExportOpsReportCodes.REPORT_ASSET_REPAYMENT_PLAN_LIST, RepaymentPlanQueryModel.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_LIST_OVERDUE, OverdueAssetQueryModel.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_LIST_MANAGEMENT, AssetSetQueryModel.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_GUARANTEE_LIST, GuaranteeOrderModel.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_GUARANTEE_CLEAR, SettlementOrderQueryModel.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_REPURCHASE_LIST, RepurchaseQueryModel.class);
			put(ExportOpsReportCodes.REPORT_CAPITAL_REMITTANCE_ONLINE_PAY_LIST, HashMap.class);
			put(ExportOpsReportCodes.REPORT_CAPITAL_DEDUCT_AUDIT_BILL, HashMap.class);
			put(ExportOpsReportCodes.REPORT_CAPITAL_DEDUCT_TODAY_REPAYMENT_LIST, HashMap.class);
			put(ExportOpsReportCodes.REPORT_CAPITAL_PROJECT_ACCOUNT_CASH_FLOW_LIST, BankReconciliationQueryModel.class);
//			put(ExportOpsReportCodes.REPORT_PROJECT_INFO_LIST, ProjectInformationQueryModel.class);
			put(ExportOpsReportCodes.REPORT_LOAN_SCALE_LIST, LoansQueryModel.class);
			put(ExportOpsReportCodes.REPORT_RECEIVABLE_INTEREST_LIST, InterestQueryModel.class);
			put(ExportOpsReportCodes.REPORT_PART_REPAYMENT_LIST, ReportJobBaseModel.class);
			put(ExportOpsReportCodes.REPORT_THIRD_PARTY_PAY_VOUCHER, ThirdPartyVoucherCommandLogQueryModel.class);
			put(ExportOpsReportCodes.REPORT_DEDUCT_APPLICATION, DeductApplicationQeuryModel.class);
			put(ExportOpsReportCodes.REPORT_BENEFICIARY_AUDIT_RESULT, HashMap.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_ORDER_LIST, RepaymentOrderQueryModel.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_RECORD_LIST, RepaymentRecordQueryModel.class);
			put(ExportOpsReportCodes.REPORT_FIRST_OVERDUE_RATE, HashMap.class);
		}
	};
	
	public final static Map<String, Class<? extends IReportWrapper<?>>> REPORT_CODES_WRAPPER_BEAN_MAPPER = new HashMap<String, Class<? extends IReportWrapper<?>>>() {
		private static final long serialVersionUID = 2550377183423707773L;
		{
			put(ExportOpsReportCodes.REPORT_ASSET_CONTRACT_LIST, ReportWrapper1.class);
			put(ExportOpsReportCodes.REPORT_ASSET_BATCH_LIST_REPAYMENT_PLAN, ReportWrapper2.class);
			put(ExportOpsReportCodes.REPORT_ASSET_REPAYMENT_PLAN_LIST, ReportWrapper3.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_LIST_OVERDUE, ReportWrapper4.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_LIST_MANAGEMENT, ReportWrapper5.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_GUARANTEE_LIST, ReportWrapper6.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_GUARANTEE_CLEAR, ReportWrapper7.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_REPURCHASE_LIST, ReportWrapper8.class);
			put(ExportOpsReportCodes.REPORT_CAPITAL_REMITTANCE_ONLINE_PAY_LIST, ReportWrapper9.class);
			put(ExportOpsReportCodes.REPORT_CAPITAL_DEDUCT_AUDIT_BILL, null);
			put(ExportOpsReportCodes.REPORT_CAPITAL_DEDUCT_TODAY_REPAYMENT_LIST, ReportWrapper11.class);
			put(ExportOpsReportCodes.REPORT_CAPITAL_PROJECT_ACCOUNT_CASH_FLOW_LIST, ReportWrapper12.class);
//			put(ExportOpsReportCodes.REPORT_PROJECT_INFO_LIST, ReportWrapper13.class);
			put(ExportOpsReportCodes.REPORT_LOAN_SCALE_LIST, null);
			put(ExportOpsReportCodes.REPORT_RECEIVABLE_INTEREST_LIST, null);
			put(ExportOpsReportCodes.REPORT_PART_REPAYMENT_LIST, null);
			put(ExportOpsReportCodes.REPORT_THIRD_PARTY_PAY_VOUCHER, ReportWrapper17.class);
			put(ExportOpsReportCodes.REPORT_DEDUCT_APPLICATION, ReportWrapper18.class);
			put(ExportOpsReportCodes.REPORT_BENEFICIARY_AUDIT_RESULT, ReportWrapper19.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_ORDER_LIST, ReportWrapper20.class);
			put(ExportOpsReportCodes.REPORT_REPAYMENT_RECORD_LIST, ReportWrapper21.class);
            put(ExportOpsReportCodes.REPORT_FIRST_OVERDUE_RATE, ReportWrapper22.class);
        }
	};
	
	public final static Map<String, Class<? extends IAsyncReportWrapper<?>>> REPORT_CODES_ASYNC_WRAPPER_BEAN_MAPPER = new HashMap<String, Class<? extends IAsyncReportWrapper<?>>>() {
		private static final long serialVersionUID = 6895380754464395809L;
		{
			put(ExportOpsReportCodes.REPORT_LOAN_SCALE_LIST, null);
			put(ExportOpsReportCodes.REPORT_RECEIVABLE_INTEREST_LIST, null);
			put(ExportOpsReportCodes.REPORT_PART_REPAYMENT_LIST, AsyncReportWrapper16.class);
		}
	};
	
}
