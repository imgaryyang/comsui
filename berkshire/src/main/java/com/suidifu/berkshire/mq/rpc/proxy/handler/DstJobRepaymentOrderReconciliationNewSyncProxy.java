package com.suidifu.berkshire.mq.rpc.proxy.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.berkshire.utils.OrikaMapper;
import com.suidifu.hathaway.mq.annotations.v2.MicroService;
import com.suidifu.owlman.microservice.handler.RepaymentOrderReconciliationHandler;
import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobRepaymentOrderReconciliation;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/13 <br>
 * @time: 20:02 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Component("dstJobRepaymentOrderReconciliationNewSyncProxy")
public class DstJobRepaymentOrderReconciliationNewSyncProxy implements RepaymentOrderReconciliationHandler {
    @Autowired
    private DstJobRepaymentOrderReconciliation dstJobRepaymentOrderReconciliation;

    /**
     * stage three
     *
     * @param list
     * @return
     */
    @Override
    @MicroService(beanName = "repaymentOrderReconciliation",
            methodName = "fetchVirtualAccountAndBusinessPaymentVoucherTransfer",
            sync = true,
            vhostName = "/business",
            exchangeName = "exchange-business",
            routingKey = "repayment-order-reconciliation")
    public boolean fetchVirtualAccountAndBusinessPaymentVoucherTransfer(List<RepaymentOrderReconciliationParameters> list) {
        //owlman项目的model转换为wellsfargo项目的entity，然后传入下方方法参数
        List<com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters> to =
                OrikaMapper.mapAsList(list,
                        com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters.class);

        return dstJobRepaymentOrderReconciliation.fetch_virtual_account_and_business_payment_voucher_transfer(to);
    }

    /**
     * stage fourth
     *
     * @param list
     * @return
     */
    @Override
    @MicroService(beanName = "repaymentOrderReconciliation",
            methodName = "repaymentOrderRecoverDetails",
            sync = true,
            vhostName = "/business",
            exchangeName = "exchange-business",
            routingKey = "repayment-order-reconciliation")
    public boolean repaymentOrderRecoverDetails(List<RepaymentOrderReconciliationParameters> list) {
        //owlman项目的model转换为wellsfargo项目的entity，然后传入下方方法参数
        List<com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters> to =
                OrikaMapper.mapAsList(list,
                        com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters.class);

        return dstJobRepaymentOrderReconciliation.repayment_order_recover_details(to);
    }

    /**
     * stage fifth
     *
     * @param list
     * @return
     */
    @Override
    @MicroService(beanName = "repaymentOrderReconciliation",
            methodName = "unfreezeCapital",
            sync = true,
            vhostName = "/business",
            exchangeName = "exchange-business",
            routingKey = "repayment-order-reconciliation")
    public boolean unfreezeCapital(List<RepaymentOrderReconciliationParameters> list) {
        //owlman项目的model转换为wellsfargo项目的entity，然后传入下方方法参数
        List<com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters> to =
                OrikaMapper.mapAsList(list,
                        com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters.class);

        return dstJobRepaymentOrderReconciliation.unfreeze_capical(to);
    }
}