package com.suidifu.owlman.microservice.model;

import com.zufangbao.sun.entity.account.BankAccountAdapter;
import com.zufangbao.sun.entity.account.VirtualAccount;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.VirtualAccountStatus;
import com.zufangbao.sun.yunxin.entity.model.BaseQueryModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 账户列表详情模型
 *
 * @author dcc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class VirtualAccountDetailModel extends BaseQueryModel {
    //账户编号
    private String virtualAccountNo;
    //账户名称
    private String virtualAccountName;
    //客户类型
    private CustomerType customerType;
    //账户余额
    private BigDecimal balance;
    //账户状态
    private VirtualAccountStatus virtualAccountStatus;
    //创建时间
    private Date createTime;

    //信托合同名称
    private String financialContractName;
    //信托合同编号
    private String financialContractNo;
    //贷款合同编号
    private String contractNo;

    // 编号
    private String number;
    //姓名
    private String accountName;
    //身份证号
    private String idCardNo;
    //开户行
    private String bankName;
    //银行账户
    private String accountNo;
    //所在地
    private String location;

    //银行卡列表
    private List<BankAccountAdapter> bankAccountAdapterList;

    //日志
    private String objectUuid;

    private String contractId;

    private String financialContractUuid;

    public VirtualAccountDetailModel(VirtualAccount virtualAccount, FinancialContract financialContract,
                                     Contract contract, List<BankAccountAdapter> bankAccountAdapterList, BankAccountAdapter bankAccountAdapter) {

        this.virtualAccountName = virtualAccount.getVirtualAccountAlias();
        this.virtualAccountNo = virtualAccount.getVirtualAccountNo();
        this.virtualAccountStatus = EnumUtil.fromOrdinal(VirtualAccountStatus.class, virtualAccount.getVirtualAccountStatus());
        this.createTime = virtualAccount.getCreateTime();
        this.balance = virtualAccount.getTotalBalance();
        this.contractNo = contract == null ? "" : contract.getContractNo();
        this.financialContractName = financialContract == null ? "" : financialContract.getContractName();
        this.financialContractNo = financialContract == null ? "" : financialContract.getContractNo();
        this.financialContractUuid = financialContract == null ? "" : financialContract.getFinancialContractUuid();
        this.customerType = EnumUtil.fromOrdinal(CustomerType.class, virtualAccount.getCustomerType());

        this.number = contract == null ? "" : contract.getCustomer().getSource();
        this.objectUuid = virtualAccount.getVirtualAccountUuid();
        this.contractId = contract == null ? "" : contract.getId().toString();
        this.bankAccountAdapterList = bankAccountAdapterList;

        if (bankAccountAdapter != null) {

            this.accountName = bankAccountAdapter.getAccountName() == null ? "" : bankAccountAdapter.getAccountName();
            this.accountNo = bankAccountAdapter.getAccountNo() == null ? "" : bankAccountAdapter.getAccountNo();
            this.bankName = bankAccountAdapter.getBankName() == null ? "" : bankAccountAdapter.getBankName();
            if (bankAccountAdapter.getProvince() != null && bankAccountAdapter.getCity() != null) {
                this.location = bankAccountAdapter.getProvince() + bankAccountAdapter.getCity();
            } else if (bankAccountAdapter.getProvince() != null && bankAccountAdapter.getCity() == null) {
                this.location = bankAccountAdapter.getProvince();
            } else if (bankAccountAdapter.getProvince() == null && bankAccountAdapter.getCity() != null) {
                this.location = bankAccountAdapter.getCity();
            } else {
                this.location = "";
            }
            this.idCardNo = bankAccountAdapter.getIdCardNo() == null ? "" : bankAccountAdapter.getIdCardNo();
        }

    }

}