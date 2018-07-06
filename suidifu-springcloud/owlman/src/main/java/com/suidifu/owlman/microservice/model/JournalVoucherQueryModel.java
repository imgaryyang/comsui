package com.suidifu.owlman.microservice.model;

import java.util.Date;


public interface JournalVoucherQueryModel {

    int getType();

    Long getAppId();

    String getOrderNo();

    Date getStartDate();

    Date getEndDate();

    Long getCompanyId();

    void setCompanyId(Long companyId);

    String getEndTime();

    String getStartTime();

}