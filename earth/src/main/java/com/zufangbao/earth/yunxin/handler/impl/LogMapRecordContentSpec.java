package com.zufangbao.earth.yunxin.handler.impl;

import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.notice.NoticeLog;
import com.zufangbao.sun.entity.order.Order;
import com.zufangbao.sun.yunxin.log.FinancialContractLog;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.TemporaryRepurchaseLog;

import java.util.HashMap;
import java.util.Map;

public class LogMapRecordContentSpec {

	public static final Map<LogFunctionType, String> logFunctionTypeMatchRecordContentHeadName = new HashMap<LogFunctionType, String>() {
		private static final long serialVersionUID = 1550498008776487957L;
		{
			put(LogFunctionType.EDITFINANCIALCONTRACT, "编辑信托合同");
			put(LogFunctionType.EDITORDER, "编辑结算单");
			put(LogFunctionType.ADDOFFLINEBILL, "新增线下支付单");
			put(LogFunctionType.ASSETPACKAGEIMPORT, "导入资产包,导入批次:");
			put(LogFunctionType.EXPORTREPAYEMNTPLAN, "导出还款计划");
			put(LogFunctionType.DELETELOANBATCH, "删除放款批次");
			put(LogFunctionType.ACTIVELOANBATCH, "激活放款批次");
			put(LogFunctionType.ADDFINANCIALCONTRACT, "新增信托合同,信托产品代码为");
			put(LogFunctionType.ONLINEBILLEXPORTCHECKING, "对账单导出支付单");
			put(LogFunctionType.ONLINEBILLEXPORTDAILYRETURNLIST, "每日还款清单导出支付单");
			put(LogFunctionType.GUARANTEEEXPORT, "导出担保单");
			put(LogFunctionType.ADDOFFLINEBILL, "新增线下支付单");
			put(LogFunctionType.GUARANTEELAPSE, "作废担保单");
			put(LogFunctionType.GUARANTEEACTIVE, "激活担保单");
			put(LogFunctionType.JOURNAL_VOUCHER_AUDIT,"对账");
			put(LogFunctionType.ADDPREPAIDORDER,"创建充值单");
			put(LogFunctionType.ADDREVOKEORDER,"创建充值撤销单");
			put(LogFunctionType.ADDPAYMENTORDER,"创建退款单");
			put(LogFunctionType.ADDNOTICE,"新建公告");
			put(LogFunctionType.EDITNOTICE,"编辑公告");
			put(LogFunctionType.CANCELNOTICE,"作废公告");
			put(LogFunctionType.RELEASENOTICE,"发布公告");
			put(LogFunctionType.MANUAL_CREATE_NORMAL_ORDER,"新增结算单");
			put(LogFunctionType.MANUAL_CLOSE_NORMAL_ORDER,"关闭结算单");
			put(LogFunctionType.MANUAL_EDIT_OVERDUE_CHARGES,"修改逾期费用");
			put(LogFunctionType.ADDVIRTUALACCOUNT,"创建账户");
			put(LogFunctionType.ADDBANKCARD,"新增银行卡");
			put(LogFunctionType.UPDATEBANKCARD,"编辑银行卡");
			put(LogFunctionType.BINDINGBANKCARD,"绑定银行卡");
			put(LogFunctionType.TIEDUPBANKCARD,"解绑银行卡");
			put(LogFunctionType.REVOKERECHARGEORDER,"作废充值单");
			put(LogFunctionType.REVOKERPAYMENTORDER,"支付退款");
			put(LogFunctionType.CREATE_AUDIT_JOB,"创建对账任务");
			put(LogFunctionType.REDO_AUDIT_JOB,"重新对账");
			put(LogFunctionType.LOCAL_UNISSUED,"本端多账核单");
			put(LogFunctionType.COUNTER_UNISSUED,"对端多账核单");
			put(LogFunctionType.CHECK_OPPOSITE_EXECUTION_STATUS, "对端交易状态核单");
			//新增
			put(LogFunctionType.EDIT_PAYMENT_BANK_ACCOUNT, "编辑客户账户信息");
			put(LogFunctionType.ADD_TEMPORARY_REPURCHASE,"新增临时回购任务");
			put(LogFunctionType.DELETE_TEMPORARY_REPURCHASE,"删除临时回购任务");
			put(LogFunctionType.MODIFY_TEMPORARY_REPURCHASE,"修改临时回购任务");
			put(LogFunctionType.MODIFY_TEMPORARY_REPURCHASE,"修改临时回购任务");
			
			put(LogFunctionType.ADDTAG,"新增标签");
			put(LogFunctionType.MODIFYTAG,"编辑标签");
			put(LogFunctionType.ASSET_REFUND,"资产退款");
			
		}

	};
	public static final Map<String, HashMap<String, String>> recordContentHeadNameMatchRecordContent = new HashMap<String, HashMap<String, String>>() {
		private static final long serialVersionUID = 2734018021978827240L;

		{
			put("编辑信托合同",
					(HashMap<String, String>) FinancialContractLog.filedNameCorrespondStringName);
			put("编辑结算单",
					(HashMap<String, String>) Order.filedNameCorrespondStringName);
			put("编辑公告",
					(HashMap<String, String>) NoticeLog.filedNameCorrespondStringName);
			put("发布公告",
					(HashMap<String, String>) NoticeLog.filedNameCorrespondStringName);
			put("作废公告",
					(HashMap<String, String>) NoticeLog.filedNameCorrespondStringName);
			put("编辑客户账户信息", (HashMap<String, String>) ContractAccount.filedNameCorrespondStringName);
			put("修改临时回购任务",
					(HashMap<String, String>) TemporaryRepurchaseLog.filedNameCorrespondStringName);
		}

	};
}
