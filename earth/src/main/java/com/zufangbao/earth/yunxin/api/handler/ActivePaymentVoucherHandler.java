package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.earth.yunxin.api.model.command.ActivePaymentVoucherCommandModel;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.contract.ContractActiveSourceDocumentMapper;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.model.voucher.*;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.SourceDocument;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 主动付款凭证接口
 * @author louguanyang
 *
 */
public interface ActivePaymentVoucherHandler {

	void save_file_to_service(MultipartHttpServletRequest fileRequest, String requestNo, String voucherNo) throws IOException;

	List<ContractActiveSourceDocumentMapper> submitActivePaymentVoucher(ActivePaymentVoucherCommandModel model, MultipartHttpServletRequest fileRequest) throws IOException;

	void checkRequestNoAndSaveLog(ActivePaymentVoucherCommandModel model, String ip);

	void undoActivePaymentVoucher(FinancialContract financialContract, String bankTransactionNo);

	VoucherCreateBaseModel searchAccountInfoByContractNo(String contractNo);

	List<ContractAccount> searchAccountInfoByName(String name);

//	void saveActiveVoucher(VoucherCreateModel model, Principal principal, String ip) throws IOException;

	void updateActiveVoucherComment(Long detailId, String comment, Principal principal, String ip);

	List<String> getActiveVoucherResource(Long detailId);

	String uploadSingleFileReturnUUID(MultipartFile file) throws IOException;
	
	List<VoucherCreateAccountInfoModel> getContractAccountInfoModelListBy(String paymentAccountNo);

	List<ContractActiveSourceDocumentMapper> submit(Principal principal, VoucherCreateSubmitModel model, String ip);

	void save(VoucherCreateSubmitModel model, Principal principal, String ipAddress);

	List<VoucherCreateContractInfoModel> getContractInfoModelList(ContractInfoQueryModel queryModel);

	List<VoucherCreateAssetInfoModel> getAssetInfoModelList(AssetInfoQueryModel queryModel);

	SourceDocument attachedSourceDocument(FinancialContract financialContract, Contract contract,
			List<CashFlow> cashFlowList, String bankTransactionNo, String comment, String voucherUuid,
			BigDecimal planBookingAmount);
}
