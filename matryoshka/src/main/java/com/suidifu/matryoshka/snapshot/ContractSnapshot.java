package com.suidifu.matryoshka.snapshot;

import com.zufangbao.sun.entity.contract.Contract;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 贷款合同快照
 * Created by louguanyang on 2017/4/20.
 */
public class ContractSnapshot {
    /**
     * 贷款合同唯一编号（外部）
     */
    private String uniqueId;

    /**
     * 贷款合同唯一编号
     */
    private String uuid;
    
    /**
     * 
	 * 贷款合同编号
	 */
    private String contractNo;

    /**
     * 信托合同uuid
     */
    private String financialContractUuid;

    /**
     * 贷款开始日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 贷款结束日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 客户唯一标识
     */
    @Deprecated
    private String customerUuid;

    private CustomerAccountSnapshot customerAccountSnapshot;

    public ContractSnapshot() {
        super();
    }

    public ContractSnapshot(String uniqueId, String uuid, String financialContractUuid, Date beginDate, Date endDate) {
        super();
        this.uniqueId = uniqueId;
        this.uuid = uuid;
        this.financialContractUuid = financialContractUuid;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public ContractSnapshot(String uniqueId, String uuid, String financialContractUuid, Date beginDate, Date endDate,
                            String customerUuid) {
        this.uniqueId = uniqueId;
        this.uuid = uuid;
        this.financialContractUuid = financialContractUuid;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.customerUuid = customerUuid;
    }

    public ContractSnapshot(Contract contract){
    	this.uniqueId = contract.getUniqueId();
    	this.uuid = contract.getUuid();
    	this.financialContractUuid = contract.getFinancialContractUuid();
    	this.beginDate = contract.getBeginDate();
    	this.endDate = contract.getEndDate();
    	this.customerUuid = contract.getCustomerUuid();
    	this.contractNo = contract.getContractNo();
    }
    
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFinancialContractUuid() {
        return financialContractUuid;
    }

    public void setFinancialContractUuid(String financialContractUuid) {
        this.financialContractUuid = financialContractUuid;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public CustomerAccountSnapshot getCustomerAccountSnapshot() {
        return customerAccountSnapshot;
    }

    public void setCustomerAccountSnapshot(CustomerAccountSnapshot customerAccountSnapshot) {
        this.customerAccountSnapshot = customerAccountSnapshot;
    }

    /**
     * 获取身份证号
     * @return
     */
    public String getIdCardNo() {
        try {
            return this.getCustomerAccountSnapshot().getAccount();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

}
