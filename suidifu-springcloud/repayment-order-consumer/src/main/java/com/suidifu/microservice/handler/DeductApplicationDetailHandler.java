/**
 *
 */
package com.suidifu.microservice.handler;

import com.suidifu.microservice.model.DeductDataContext;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;
import java.util.List;

/**
 * @author wukai
 *
 */
public interface DeductApplicationDetailHandler {

	List<DeductApplicationDetail> generateByRepaymentDetailList(DeductRequestModel deductRequestModel, DeductApplication deductApplication, Contract contract, FinancialContract financialContract);

	void getDeductApplicationDetailInfoModel(DeductDataContext deductDataContext);

	List<DeductApplicationDetail> generateByRepaymentDetailListV(DeductRequestModel deductRequestModel, DeductApplication deductApplication, String contractUuid, String financialContractUuid);
}
