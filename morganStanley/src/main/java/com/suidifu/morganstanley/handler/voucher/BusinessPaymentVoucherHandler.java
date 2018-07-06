package com.suidifu.morganstanley.handler.voucher;

import com.suidifu.morganstanley.model.request.voucher.BusinessPaymentVoucher;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.wellsfargo.yunxin.handler.vouchertask.BusinessPaymentVoucherTaskHandler;

import java.util.List;

/**
 * 商户付款凭证接口
 *
 * @author louguanyang
 */
public interface BusinessPaymentVoucherHandler extends BusinessPaymentVoucherTaskHandler {
    void checkByRequestNo(String requestNo);

    void saveLog(BusinessPaymentVoucher model, String ip);

    List<CashFlow> submitBusinessPaymentVoucher(BusinessPaymentVoucher model);

    void undoBusinessPaymentVoucher(BusinessPaymentVoucher model);
}