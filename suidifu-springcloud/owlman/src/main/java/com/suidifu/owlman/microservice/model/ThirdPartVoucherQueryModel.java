package com.suidifu.owlman.microservice.model;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.owlman.microservice.enumation.JournalVoucherStatus;
import com.suidifu.owlman.microservice.enumation.SecondJournalVoucherType;
import com.suidifu.owlman.microservice.enumation.ThirdJournalVoucherType;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.utils.EnumUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 zhenghangbo
 * @version 创建时间：Nov 23, 2016 5:10:19 PM
 * 类说明
 */
@Data
public class ThirdPartVoucherQueryModel {
    private String financialContractUuids;

    private String repaymentType;

    private String deductVoucherSource;

    private String paymentChannel;

    private String voucherStatus;

    private String specialAccountNo;

    private String payerName;

    private String payerBankAccountNo;

    public List<String> getFinancialContractUuidList() {
        List<String> financialContractIdList = JsonUtils.parseArray(this.financialContractUuids, String.class);
        if (financialContractIdList == null) {
            return new ArrayList<>();
        }
        return financialContractIdList;
    }

    public List<ThirdJournalVoucherType> getRepaymentTypeEnumList() {
        return EnumUtil.getEnumList(this.getRepaymentType(), ThirdJournalVoucherType.class);
    }

    public List<SecondJournalVoucherType> getDeductVoucherSourceEnumList() {
        return EnumUtil.getEnumList(this.getDeductVoucherSource(), SecondJournalVoucherType.class);
    }

    public List<CashFlowChannelType> getCashFlowChannelTypeEnumList() {
        return EnumUtil.getEnumList(this.getPaymentChannel(), CashFlowChannelType.class);
    }

    public List<JournalVoucherStatus> getJournalVoucherStatusEnumList() {
        return EnumUtil.getEnumList(this.getVoucherStatus(), JournalVoucherStatus.class);
    }
}