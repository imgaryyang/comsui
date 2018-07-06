package com.zufangbao.earth.yunxin.api.model.query;

import java.util.List;

/**
 * Created by whb on 17-7-3.
 */
public class AccountTradeListResultModel {
    /**
     * 信托产品代码
     */
    private String productCode;
    /**
     * 帐号
     */
    private String capitalAccountNo;
    /**
     * 网关
     */
    private Integer paymentInstitutionName;
    /**
     * 总数量
     */
    private Boolean hasNextPage;
    /**
     * 流水详情
     */
    private List<AccountTradeList> accountTradeList;

    public AccountTradeListResultModel(String productCode,
                                       String capitalAccountNo,
                                       Integer paymentInstitutionName,
                                       Boolean hasNextPage,
                                       List<AccountTradeList> accountTradeList) {
        this.productCode = productCode;
        this.capitalAccountNo = capitalAccountNo;
        this.paymentInstitutionName = paymentInstitutionName;
        this.hasNextPage = hasNextPage;
        this.accountTradeList = accountTradeList;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCapitalAccountNo() {
        return capitalAccountNo;
    }

    public void setCapitalAccountNo(String capitalAccountNo) {
        this.capitalAccountNo = capitalAccountNo;
    }

    public Integer getPaymentInstitutionName() {
        return paymentInstitutionName;
    }

    public void setPaymentInstitutionName(Integer paymentInstitutionName) {
        this.paymentInstitutionName = paymentInstitutionName;
    }

    public List<AccountTradeList> getAccountTradeList() {
        return accountTradeList;
    }

    public void setAccountTradeList(List<AccountTradeList> accountTradeList) {
        this.accountTradeList = accountTradeList;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
