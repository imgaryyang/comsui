package com.zufangbao.earth.yunxin.api.model.query;


import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.AccountSide;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


/**
 * Created by whb on 17-7-3.
 */
public class AccountTradeDetailModel {
    /**
     * 唯一请求编号
     */
    private String requestNo;

    /**
     * 产品编号
     */
    private String productCode;

    /**
     * 帐号
     */
    private String capitalAccountNo;

    /**
     * 网关
     */
    private PaymentInstitutionName paymentInstitutionName;
    /**
     * 借贷
     */
    private AccountSide accountSide;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 页数
     */
    private Integer page;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
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

    public PaymentInstitutionName getPaymentInstitutionName() {
        return paymentInstitutionName;
    }

    public void setPaymentInstitutionName(Integer paymentInstitutionName) {
        this.paymentInstitutionName = PaymentInstitutionName.fromValue(paymentInstitutionName);
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime){
        if(! StringUtils.isEmpty(startTime)){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            try {
                this.startTime = dateFormat.parse(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime){
        if(! StringUtils.isEmpty(endTime)){
            //2016-08-20 00:00:00
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            try {
                this.endTime = dateFormat.parse(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public int hasError(){
        if (StringUtils.isEmpty(requestNo)) {
            return ApiResponseCode.CONTRACT_INFO_BLANK;
        } else if (StringUtils.isEmpty(productCode)){
            return ApiResponseCode.REQUEST_INFO_BLANK;
        } else if (null == paymentInstitutionName){
            return ApiResponseCode.PAYMENT_INSTITUTION_NAME_BLANK;
        } else if (Objects.equals(paymentInstitutionName, PaymentInstitutionName.DIRECTBANK)
                && StringUtils.isEmpty(capitalAccountNo)){
            return ApiResponseCode.CAPITAL_ACCOUNT_NO_BLANK;
        } else if (null == startTime){
            return ApiResponseCode.START_TIME_BLANK;
        } else if (null == endTime){
            return ApiResponseCode.END_TIME_BLANK;
        } else if (null == page || 0>= page){
            return ApiResponseCode.PAGE_INFO_BLANK;
        } else {
            return 0;
        }
    }

    public boolean hasErrorBetweenStartAndEnd(){
        return ! Objects.equals(startTime.compareTo(endTime), -1);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public AccountSide getAccountSide() {
        return accountSide;
    }

    public void setAccountSide(String accountSide) {
        if (! StringUtils.equals(Integer.toString(AccountSide.CREDIT.getOrdinal()), accountSide)
                && ! StringUtils.equals(Integer.toString(AccountSide.DEBIT.getOrdinal()), accountSide)) {
            this.accountSide = null;
        } else {
            this.accountSide = AccountSide.fromValue(Integer.valueOf(accountSide));
        }
    }

    @Override
    public String toString() {
        return "AccountTradeDetailModel{" +
                "requestNo='" + requestNo + '\'' +
                ", productCode='" + productCode + '\'' +
                ", capitalAccountNo='" + capitalAccountNo + '\'' +
                ", paymentInstitutionName=" + paymentInstitutionName +
                ", accountSide=" + accountSide +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", page=" + page +
                '}';
    }
}
