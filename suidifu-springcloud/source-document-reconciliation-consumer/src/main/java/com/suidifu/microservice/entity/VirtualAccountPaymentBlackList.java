package com.suidifu.microservice.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class VirtualAccountPaymentBlackList {

    @Id
    @GeneratedValue
    private Long id;

    private String virtualAccountUuid;

    private String virtualAccountNo;

    private String assetUuid;

    private String assetNo;

    private Date createTime;

    private boolean isValid;

    public VirtualAccountPaymentBlackList(String virtualAccountUuid,
                                          String virtualAccountNo, String assetUuid, String assetNo,
                                          Date createTime, boolean isValid) {
        super();
        this.virtualAccountUuid = virtualAccountUuid;
        this.virtualAccountNo = virtualAccountNo;
        this.assetUuid = assetUuid;
        this.assetNo = assetNo;
        this.createTime = createTime;
        this.isValid = isValid;
    }

    public VirtualAccountPaymentBlackList() {
        super();
    }

    public VirtualAccountPaymentBlackList(String virtualAccountUuid,
                                          String virtualAccountNo, String assetUuid, String assetNo) {
        super();
        this.virtualAccountUuid = virtualAccountUuid;
        this.virtualAccountNo = virtualAccountNo;
        this.assetUuid = assetUuid;
        this.assetNo = assetNo;
        this.createTime = new Date();
        this.isValid = Boolean.TRUE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVirtualAccountUuid() {
        return virtualAccountUuid;
    }

    public void setVirtualAccountUuid(String virtualAccountUuid) {
        this.virtualAccountUuid = virtualAccountUuid;
    }

    public String getVirtualAccountNo() {
        return virtualAccountNo;
    }

    public void setVirtualAccountNo(String virtualAccountNo) {
        this.virtualAccountNo = virtualAccountNo;
    }

    public String getAssetUuid() {
        return assetUuid;
    }

    public void setAssetUuid(String assetUuid) {
        this.assetUuid = assetUuid;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

}
