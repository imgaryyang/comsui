package com.suidifu.morganstanley.handler.repayment;

import com.suidifu.morganstanley.model.request.repayment.ModifyRepaymentPlan;
import com.zufangbao.sun.api.model.repayment.RepaymentPlanModifyDetail;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.wellsfargo.yunxin.handler.AssetSetApiHandler;

import java.util.List;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/10/16 <br>
 * Time:下午9:34 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
public interface AssetSetHandler extends AssetSetApiHandler {
    List<RepaymentPlanModifyDetail> modifyRepaymentPlan(Contract contract, Integer oldActiveVersionNo,
                                                        ModifyRepaymentPlan modifyRepaymentPlan, String ip);

}
