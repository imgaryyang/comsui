package com.suidifu.giotto.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.keyenum.FastRepaymentOrderItemKeyEnum;
import com.suidifu.giotto.util.SqlAndParamTuple;
import com.zufangbao.gluon.opensdk.Md5Util;
import org.apache.commons.lang.StringUtils;

/**
 * FastRepaymentOrderItem
 *
 * @author whb
 * @date 2017/5/26
 */

public class FastRepaymentOrderItem extends FastCacheObject {

    private Long id;
    /**
     * 订单详情uuid
     */
    private String orderDetailUuid;
    /**
     * 贷款合同唯一编号
     */
    private String contractUniqueId;

    private String contractNo;

    private String contractUuid;
    
    private BigDecimal amount;

    private Integer detailAliveStatus;

    private Integer detailPayStatus;

    private Integer repaymentWay;

    /**
     * 还款业务编号(还款计划uuid，或者回购单uuid)
     */
    private String repaymentBusinessNo;
    /**
     * 还款业务类型
     */
    private Integer repaymentBusinessType;
    /**
     * 设置还款时间
     */
    private Date repaymentPlanTime;
    /**
     * 商户订单uuid
     */
    private String orderUuid;
    /**
     * 商户订单号
     */
    private String orderUniqueId;
    /**
     * 备注
     */
    private String remark;
    
    private Date createTime;
    
    private Date lastModifiedTime;
    /**
     * 还款计划期数
     */
    private int currentPeriod;
    /**
     * 对方还款计划唯一标识
     */
	private String repayScheduleNo;
    private Integer identificationMode;
    private Integer receivable_in_advance_status;

    private String stringField1;
    private String stringField2;
    private String stringField3;


    private Date dateField1;
    private Date dateField2;
    private Date dateField3;

    private BigDecimal decimalField1;
    private BigDecimal decimalField2;
    private BigDecimal decimalField3;

    private String merId;
    
    private String repaymentBusinessUuid;

    private String financialContractUuid;
    
    /**
     * 金额明细
     */
    private String chargeDetail;
	@Override
    public String obtainAddCacheKey() throws GiottoException {
        if (StringUtils.isBlank(getOrderDetailUuid()) && StringUtils.isBlank(getOrderUuid()) &&
                StringUtils.isBlank(getMerId()) && StringUtils.isBlank(getRepaymentBusinessUuid())) {
            throw new GiottoException("all keys value is null.");
        }
        String result = FastRepaymentOrderItemKeyEnum.PREFIX_KEY;

        if (StringUtils.isBlank(getOrderDetailUuid())) {
            result = result.concat(":");
        } else {
            result = result.concat(getOrderDetailUuid()).concat(":");
        }
        if (StringUtils.isBlank(getOrderUuid())) {
            result = result.concat(":");
        } else {
            result = result.concat(getOrderUuid()).concat(":");
        }
        if (StringUtils.isBlank(getMerId())) {
            result = result.concat(":");
        } else {
            result = result.concat(getMerId()).concat(":");
        }
        if (StringUtils.isBlank(getRepaymentBusinessUuid())) {
            result = result.concat(":");
        } else {
            result = result.concat(getRepaymentBusinessUuid());
        }
        return result;
    }

    @Override
    public List<String> obtainAddCacheKeyList() {
        return new ArrayList<String>() {{
            if (StringUtils.isNotBlank(getOrderDetailUuid())) {
                add(FastRepaymentOrderItemKeyEnum.PREFIX_KEY.concat(getOrderDetailUuid()));
            }
            if (StringUtils.isNotBlank(getOrderUuid())) {
                add(FastRepaymentOrderItemKeyEnum.PREFIX_KEY.concat(getOrderUuid()));
            }
            if (StringUtils.isNotBlank(getMerId())) {
                add(FastRepaymentOrderItemKeyEnum.PREFIX_KEY.concat(getMerId()));
            }
            if (StringUtils.isNotBlank(getRepaymentBusinessUuid())) {
                add(FastRepaymentOrderItemKeyEnum.PREFIX_KEY.concat(getRepaymentBusinessUuid()));
            }
        }};
    }

    @Override
    public SqlAndParamTuple obtainInsertSqlAndParam() {
        String addSql = "insert into repayment_order_item(" +
                "order_detail_uuid, contract_unique_id, contract_no," +
                "contract_uuid, amount, detail_alive_status, detail_pay_status," +
                "repayment_way, repayment_business_no," +
                "repayment_business_type, repayment_plan_time, order_uuid," +
                "order_unique_id, string_field_1, string_field_2," +
                "string_field_3, date_field_1, date_field_2," +
                "date_field_3, decimal_field_1, decimal_field_2," +
                "decimal_field_3, remark,repayment_business_uuid,mer_id,financial_contract_uuid,create_time,last_modified_time,current_period,repay_schedule_no,identification_mode,receivable_in_advance_status,charge_detail) values (:orderDetailUuid, :contractUniqueId, :contractNo," +
                ":contractUuid, :amount, :detailAliveStatus, :detailPayStatus," +
                ":repaymentWay, :repaymentBusinessNo," +
                ":repaymentBusinessType, :repaymentPlanTime, :orderUuid," +
                ":orderUniqueId, :stringField1, :stringField2," +
                ":stringField3, :dateField1, :dateField2," +
                ":dateField3, :decimalField1, :decimalField2," +
                ":decimalField3, :remark," +
                ":repaymentBusinessUuid, :merId, :financialContractUuid, :createTime, :lastModifiedTime , :currentPeriod, :repayScheduleNo, :identificationMode, :receivable_in_advance_status, :chargeDetail)";
        String fastJson = JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss",SerializerFeature.WriteMapNullValue);
        return new SqlAndParamTuple(addSql, JsonUtils.parse(fastJson));
    }

    @Override
    public String obtainQueryCheckMD5Sql(String updateSql) {
        int startIndex = updateSql.indexOf("where");
        String selectSql = "select count(1) from repayment_order_item ".concat(updateSql.substring(startIndex, updateSql.length()));

        String aliveStatus = this.getDetailAliveStatus() == null ? "" : this.getDetailAliveStatus().toString();
        String payStatus = this.getDetailPayStatus() == null ? "" : this.getDetailPayStatus().toString();
        String reWay = this.getRepaymentWay() == null ? "" : this.getRepaymentWay().toString();
        String reType = this.getRepaymentBusinessType() == null ? "" : this.getRepaymentBusinessType().toString();

        String md5Val = Md5Util.encode(aliveStatus.concat(payStatus).concat(reWay).concat(reType));
        String md5Sql = "MD5(concat(IFNULL(detail_alive_status,''),IFNULL(detail_pay_status,'')," +
                "IFNULL(repayment_way,'')," +
                "IFNULL(repayment_business_type,'')))";
        return selectSql.concat(" and '").concat(md5Val).concat("'=").concat(md5Sql);
    }

    public String obtainUpdateCheckMD5Sql(String updateSql) {
        return null;
    }

    @Override
    public String obtainQueryListKeyValue() {
        return this.orderUuid;
    }

	public String getChargeDetail() {
		return chargeDetail;
	}

	public void setChargeDetail(String chargeDetail) {
		this.chargeDetail = chargeDetail;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderDetailUuid() {
        return orderDetailUuid;
    }

    public void setOrderDetailUuid(String orderDetailUuid) {
        this.orderDetailUuid = orderDetailUuid;
    }

    public String getContractUniqueId() {
        return contractUniqueId;
    }

    public void setContractUniqueId(String contractUniqueId) {
        this.contractUniqueId = contractUniqueId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractUuid() {
        return contractUuid;
    }

    public void setContractUuid(String contractUuid) {
        this.contractUuid = contractUuid;
    }

    public Integer getDetailAliveStatus() {
        return detailAliveStatus;
    }

    public void setDetailAliveStatus(Integer detailAliveStatus) {
        this.detailAliveStatus = detailAliveStatus;
    }

    public Integer getDetailPayStatus() {
        return detailPayStatus;
    }

    public void setDetailPayStatus(Integer detailPayStatus) {
        this.detailPayStatus = detailPayStatus;
    }

    public Integer getRepaymentWay() {
        return repaymentWay;
    }

    public void setRepaymentWay(Integer repaymentWay) {
        this.repaymentWay = repaymentWay;
    }

    public String getRepaymentBusinessNo() {
        return repaymentBusinessNo;
    }

    public void setRepaymentBusinessNo(String repaymentBusinessNo) {
        this.repaymentBusinessNo = repaymentBusinessNo;
    }

    public Integer getRepaymentBusinessType() {
        return repaymentBusinessType;
    }

    public void setRepaymentBusinessType(Integer repaymentBusinessType) {
        this.repaymentBusinessType = repaymentBusinessType;
    }

    public Date getRepaymentPlanTime() {
        return repaymentPlanTime;
    }

    public void setRepaymentPlanTime(Date repaymentPlanTime) {
        this.repaymentPlanTime = repaymentPlanTime;
    }

    public String getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    public String getOrderUniqueId() {
        return orderUniqueId;
    }

    public void setOrderUniqueId(String orderUniqueId) {
        this.orderUniqueId = orderUniqueId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStringField1() {
        return stringField1;
    }

    public void setStringField1(String stringField1) {
        this.stringField1 = stringField1;
    }

    public String getStringField2() {
        return stringField2;
    }

    public void setStringField2(String stringField2) {
        this.stringField2 = stringField2;
    }

    public String getStringField3() {
        return stringField3;
    }

    public void setStringField3(String stringField3) {
        this.stringField3 = stringField3;
    }

    public Date getDateField1() {
        return dateField1;
    }

    public void setDateField1(Date dateField1) {
        this.dateField1 = dateField1;
    }

    public Date getDateField2() {
        return dateField2;
    }

    public void setDateField2(Date dateField2) {
        this.dateField2 = dateField2;
    }

    public Date getDateField3() {
        return dateField3;
    }

    public void setDateField3(Date dateField3) {
        this.dateField3 = dateField3;
    }

    public BigDecimal getDecimalField1() {
        return decimalField1;
    }

    public void setDecimalField1(BigDecimal decimalField1) {
        this.decimalField1 = decimalField1;
    }

    public BigDecimal getDecimalField2() {
        return decimalField2;
    }

    public void setDecimalField2(BigDecimal decimalField2) {
        this.decimalField2 = decimalField2;
    }

    public BigDecimal getDecimalField3() {
        return decimalField3;
    }

    public void setDecimalField3(BigDecimal decimalField3) {
        this.decimalField3 = decimalField3;
    }

    @Override
	public FastKey getColumnName() {
		return FastRepaymentOrderItemKeyEnum.ORDER_DETAIL_UUID;
	}

	@Override
	public String getColumnValue() {
		return orderDetailUuid;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getRepaymentBusinessUuid() {
		return repaymentBusinessUuid;
	}

	public void setRepaymentBusinessUuid(String repaymentBusinessUuid) {
		this.repaymentBusinessUuid = repaymentBusinessUuid;
	}
	
    public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

    public String getFinancialContractUuid() {
        return financialContractUuid;
    }

    public void setFinancialContractUuid(String financialContractUuid) {
        this.financialContractUuid = financialContractUuid;
    }

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(Date lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public int getCurrentPeriod() {
		return currentPeriod;
	}

	public String getRepayScheduleNo() {
		return repayScheduleNo;
	}

	public void setCurrentPeriod(int currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public void setRepayScheduleNo(String repayScheduleNo) {
		this.repayScheduleNo = repayScheduleNo;
	}

	public Integer getIdentificationMode() {
		return identificationMode;
	}

	public void setIdentificationMode(Integer identificationMode) {
		this.identificationMode = identificationMode;
	}
	

	public Integer getReceivable_in_advance_status() {
		return receivable_in_advance_status;
	}

	public void setReceivable_in_advance_status(Integer receivable_in_advance_status) {
		this.receivable_in_advance_status = receivable_in_advance_status;
	}

	public FastRepaymentOrderItem() {
    }

    public FastRepaymentOrderItem(Long id, String orderDetailUuid, String contractUniqueId
            , String contractNo, String contractUuid,BigDecimal amount, Integer detailAliveStatus
            , Integer detailPayStatus, Integer repaymentWay
            , String repaymentBusinessNo, Integer repaymentBusinessType, Date repaymentPlanTime
            , String orderUuid, String orderUniqueId, String remark, String stringField1
            , String stringField2, String stringField3, Date dateField1, Date dateField2
            , Date dateField3, BigDecimal decimalField1, BigDecimal decimalField2
            , BigDecimal decimalField3, String merId, String repaymentBusinessUuid, String financialContractUuid, Date createTime, Date lastModifiedTime, int currentPeriod, String repayScheduleNo,Integer identificationMode, Integer receivable_in_advance_status,String chargeDetail) {
        this.id = id;
        this.orderDetailUuid = orderDetailUuid;
        this.contractUniqueId = contractUniqueId;
        this.contractNo = contractNo;
        this.contractUuid = contractUuid;
        this.amount = amount;
        this.detailAliveStatus = detailAliveStatus;
        this.detailPayStatus = detailPayStatus;
        this.repaymentWay = repaymentWay;
        this.repaymentBusinessNo = repaymentBusinessNo;
        this.repaymentBusinessType = repaymentBusinessType;
        this.repaymentPlanTime = repaymentPlanTime;
        this.orderUuid = orderUuid;
        this.orderUniqueId = orderUniqueId;
        this.remark = remark;
        this.stringField1 = stringField1;
        this.stringField2 = stringField2;
        this.stringField3 = stringField3;
        this.dateField1 = dateField1;
        this.dateField2 = dateField2;
        this.dateField3 = dateField3;
        this.decimalField1 = decimalField1;
        this.decimalField2 = decimalField2;
        this.decimalField3 = decimalField3;
        this.merId = merId;
        this.repaymentBusinessUuid = repaymentBusinessUuid;
        this.financialContractUuid = financialContractUuid;
        this.createTime = createTime;
        this.lastModifiedTime = lastModifiedTime;
        this.currentPeriod=currentPeriod;
        this.repayScheduleNo=repayScheduleNo;
        this.identificationMode=identificationMode;
        this.receivable_in_advance_status=0;
        this.chargeDetail=chargeDetail;
    }
}
