package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.dao.FinancialContractDAO;
import com.suidifu.dowjones.model.Const;
import com.suidifu.dowjones.service.CashFingerPrinterServiceV2;
import com.suidifu.dowjones.utils.DateUtils;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class CashFingerPrinter1stJob extends QuartzJobBean {

    @Resource
    private FinancialContractDAO financialContractDAO;

    @Autowired
    private CashFingerPrinterServiceV2 cashFingerPrinterServiceV2;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        List<String> financialContractUuids = financialContractDAO.getAllFinancialContractUuid();

//        financialContractUuids = Arrays
//            .asList("3b12ac75-4c58-4375-a733-78c7810efebb", "429d5892-51dd-42fc-aa7e-e333778fd32f","e0970224-5e9b-477a-82f3-33ab252545b8");

        financialContractUuids = Const.CLIENT_PRODUCT;

        Date yesterday = DateUtils.yesterday();

        for (String financialContractContractUuid : financialContractUuids) {
            cashFingerPrinterServiceV2.doCashFingerPrinter_DebitCashFlow(financialContractContractUuid, yesterday);
        }

    }

}
