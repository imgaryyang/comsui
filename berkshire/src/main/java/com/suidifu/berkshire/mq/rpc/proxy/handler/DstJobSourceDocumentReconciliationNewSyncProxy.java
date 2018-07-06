package com.suidifu.berkshire.mq.rpc.proxy.handler;

import com.suidifu.berkshire.utils.OrikaMapper;
import com.suidifu.hathaway.mq.annotations.v2.MicroService;
import com.suidifu.owlman.microservice.handler.SourceDocumentReconciliationHandler;
import com.suidifu.owlman.microservice.model.SourceDocumentDetailReconciliationParameters;
import com.suidifu.owlman.microservice.model.SourceDocumentReconciliationParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobSourceDocumentReconciliation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/3/2 <br>
 * @time: 13:21 <br>
 * @mail: frank_wjs@hotmail.com <br>
 * <p>
 */
@Component("dstJobSourceDocumentReconciliationNewSyncProxy")
public class DstJobSourceDocumentReconciliationNewSyncProxy implements SourceDocumentReconciliationHandler {
    @Autowired
    private DstJobSourceDocumentReconciliation dstJobSourceDocumentReconciliation;

    @Override
    @MicroService(beanName = "sourceDocumentReconciliation",
            methodName = "criticalMarker",
            sync = true,
            vhostName = "/business",
            exchangeName = "exchange-business",
            routingKey = "source-document-reconciliation")
    public Map<String, String> criticalMarker(List<SourceDocumentDetailReconciliationParameters> list) {
        //owlman项目的model转换为wellsfargo项目的entity，然后传入下方方法参数
        List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentDetailReconciliationParameters> to =
                OrikaMapper.mapAsList(list,
                        com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentDetailReconciliationParameters.class);

        return dstJobSourceDocumentReconciliation.criticalMarker(to);
    }

    @Override
    @MicroService(beanName = "sourceDocumentReconciliation",
            methodName = "validateSourceDocumentDetailList",
            sync = true,
            vhostName = "/business",
            exchangeName = "exchange-business",
            routingKey = "source-document-reconciliation")
    public boolean validateSourceDocumentDetailList(List<SourceDocumentReconciliationParameters> list) {
        //owlman项目的model转换为wellsfargo项目的entity，然后传入下方方法参数
        List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters> to =
                OrikaMapper.mapAsList(list,
                        com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters.class);
        return dstJobSourceDocumentReconciliation.validateSourceDocumentDetailList(to);
    }

    @Override
    @MicroService(beanName = "sourceDocumentReconciliation",
            methodName = "fetchVirtualAccountAndBusinessPaymentVoucherTransfer",
            sync = true,
            vhostName = "/business",
            exchangeName = "exchange-business",
            routingKey = "source-document-reconciliation")
    public boolean fetchVirtualAccountAndBusinessPaymentVoucherTransfer(List<SourceDocumentReconciliationParameters> list) {
        //owlman项目的model转换为wellsfargo项目的entity，然后传入下方方法参数
        List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters> to =
                OrikaMapper.mapAsList(list,
                        com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters.class);

        return dstJobSourceDocumentReconciliation.fetch_virtual_account_and_business_payment_voucher_transfer(to);
    }


    @Override
    @MicroService(beanName = "sourceDocumentReconciliation",
            methodName = "sourceDocumentRecoverDetails",
            sync = true,
            vhostName = "/business",
            exchangeName = "exchange-business",
            routingKey = "source-document-reconciliation")
    public boolean sourceDocumentRecoverDetails(List<SourceDocumentReconciliationParameters> list) {
        //owlman项目的model转换为wellsfargo项目的entity，然后传入下方方法参数
        List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters> to =
                OrikaMapper.mapAsList(list,
                        com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters.class);

        return dstJobSourceDocumentReconciliation.source_document_recover_details(to);
    }

    @Override
    @MicroService(beanName = "sourceDocumentReconciliation",
            methodName = "unfreezeCapital",
            sync = true,
            vhostName = "/business",
            exchangeName = "exchange-business",
            routingKey = "source-document-reconciliation")
    public boolean unfreezeCapital(List<SourceDocumentReconciliationParameters> list) {
        //owlman项目的model转换为wellsfargo项目的entity，然后传入下方方法参数
        List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters> to =
                OrikaMapper.mapAsList(list,
                        com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.SourceDocumentReconciliationParameters.class);

        return dstJobSourceDocumentReconciliation.unfreeze_capical(to);
    }
}