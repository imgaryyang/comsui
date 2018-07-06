package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.dao.FinancialContractDAO;
import com.suidifu.dowjones.service.CashFingerPrinterServiceV2;
import com.suidifu.dowjones.utils.DateUtils;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class CashFingerPrinterBankCorporateCashFlowJob extends QuartzJobBean {

    @Resource
    private FinancialContractDAO financialContractDAO;

    @Autowired
    private CashFingerPrinterServiceV2 cashFingerPrinterServiceV2;

    @Override
    protected void executeInternal(JobExecutionContext context) {

        List<String> financialContractUuids = financialContractDAO.getAllFinancialContractUuid();

        Date yesterday = DateUtils.yesterday();

        for (String financialContractContractUuid : financialContractUuids) {
            cashFingerPrinterServiceV2.doCashFingerPrinter_BankCorporateCashFlow(financialContractContractUuid, yesterday);
        }

    }

}
