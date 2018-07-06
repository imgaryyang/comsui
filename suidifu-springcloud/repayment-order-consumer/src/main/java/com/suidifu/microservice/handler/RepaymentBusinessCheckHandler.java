package com.suidifu.microservice.handler;

import static com.suidifu.owlman.microservice.spec.ThirdPartVoucherSourceMapSpec.repaymentBusinessCheckTable;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.owlman.microservice.model.RepaymentOrderCheck;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderCheckException;
import com.zufangbao.sun.api.model.repayment.RepaymentOrderDetail;
import com.zufangbao.sun.utils.SpringContextUtil;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by zhusy on 2017/7/13.
 */
public interface RepaymentBusinessCheckHandler {
    static RepaymentBusinessCheckHandler repaymentBusinessCheckFactory(String repaymentBusinessType) {
        return SpringContextUtil.getBean(repaymentBusinessCheckTable.get(repaymentBusinessType));
    }

    void checkRepaymentNo(RepaymentOrderDetail repaymentOrderDetail, RepaymentOrderCheck checkModel)
            throws RepaymentOrderCheckException, GiottoException;

    BigDecimal getTotalRepaymentBusinessAmount(RepaymentOrderCheck checkModel);

    Map<String, BigDecimal> getTotalCharges(RepaymentOrderCheck checkModel);
}