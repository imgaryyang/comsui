package com.suidifu.dowjones.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.hadoop.hdfs.security.token.block.DataEncryptionKey;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 21:34 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
public interface CashFingerPrinterServiceV2 {
    void doCashFingerPrinter_DebitCashFlow(String financialContractUuid, Date time);
    void doCashFingerPrinter_RepaymentOrderSummary(String financialContractUuid, Date time);
    void doCashFingerPrinter_OnlinePaymentDetailsInTransit(String financialContractUuid, Date time);
    void doCashFingerPrinter_BankCorporateCashFlow(String financialContractUuid, Date time);
}