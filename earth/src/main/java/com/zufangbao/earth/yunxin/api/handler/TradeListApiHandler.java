package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.earth.yunxin.api.model.PagedTradeList;
import com.zufangbao.earth.yunxin.api.model.query.AccountTradeDetailModel;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.AccountSide;

import java.util.Date;

/**
 * Created by whb on 17-7-3.
 */
public interface TradeListApiHandler {
    /**
     * 专户流水查询接口
     * @param productNo 信托产品编号
     * @param capitalAccountNo 帐号
     * @param paymentInstitutionName 网关
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @param pageNumber 页数
     * @return
     */
	PagedTradeList findAccountTradeListBy(String productNo,
                                          String capitalAccountNo,
                                          PaymentInstitutionName paymentInstitutionName,
                                          Date startDate,
                                          Date endDate,
                                          Integer pageNumber,
                                          AccountSide accountSide);

    /**
     * 验证接口是否正确
     * @param accountTradeDetailModel
     * @return
     */
    Integer verifyAccountTradeDetailModel(AccountTradeDetailModel accountTradeDetailModel);

}
