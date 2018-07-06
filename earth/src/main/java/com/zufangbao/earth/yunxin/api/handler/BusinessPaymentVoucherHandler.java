package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.earth.yunxin.api.model.command.BusinessPaymentVoucherCommandModel;
import com.zufangbao.sun.entity.contract.ContractActiveSourceDocumentMapper;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.voucher.CreateBusinessPaymentVoucherModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.AppAccountModelForVoucherController;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherCreateAccountInfoModel;
import com.zufangbao.wellsfargo.yunxin.handler.vouchertask.BusinessPaymentVoucherTaskHandler;

import java.util.List;
import java.util.Map;
/**
 * 商户付款凭证接口
 * 
 * @author louguanyang
 *
 */
public interface BusinessPaymentVoucherHandler extends BusinessPaymentVoucherTaskHandler {

	List<CashFlow> businessPaymentVoucher(BusinessPaymentVoucherCommandModel model, String ip);
	
	void undoBusinessPaymentVoucher(BusinessPaymentVoucherCommandModel model, String ip);
		
	void invalidSourceDocument(Long detailId);
	
	List<CashFlow> matchCashflow(Long voucherId);

	ContractActiveSourceDocumentMapper connectionCashFlow(Long voucherId, String cashFlowUuid);


	void createBusinessPaymentVoucher(CreateBusinessPaymentVoucherModel createBusinessPaymentVoucherModel);

	List<Map<String, Object>> getAppAccountInfos(AppAccountModelForVoucherController appAccountModel);

	List<VoucherCreateAccountInfoModel> getAppAccountInfoClasses(AppAccountModelForVoucherController appAccountModel);

	void deleteJob(String voucherUuid, Principal principal, String ipAddress);
	
	boolean checkVoucher(Long voucherId);

}
