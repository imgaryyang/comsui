package com.suidifu.matryoshka.delayTask;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.suidifu.matryoshka.delayTask.enums.DelayTaskConfigStatus;
import com.suidifu.matryoshka.delayTask.enums.DelayTaskConfigType;
import com.suidifu.matryoshka.delayTask.enums.DelayTaskTriggerType;

/**
 * 后置处理任务配置表
 * Created by louguanyang on 2017/5/3.
 */
@Entity
@Table(name = "t_delay_processing_task_config")
public class DelayProcessingTaskConfig {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * uuid
     */
    private String uuid;


    /**
     * 一级产品目录代码
     */
    private String productLv1Code;

    /**
     * 一级产品目录名称
     */
    private String productLv1Name;

    /**
     * 二级产品目录代码
     */
    private String productLv2Code;

    /**
     * 二级产品目录名称
     */
    private String productLv2Name;

    /**
     * 三级产品目录代码
     */
    private String productLv3Code;

    /**
     * 三级产品目录名称
     */
    private String productLv3Name;

    /**
     * Task 类型 实时:批量
     *
     * @see DelayTaskConfigType
     **/
    private Integer typeCode = DelayTaskConfigType.BATCH.getCode();

    /**
     * 任务类型 变更后置，核销后置。。。。
     *
     * @see DelayTaskTriggerType
     **/
    private Integer triggerTypeCode = DelayTaskTriggerType.MODIFY_REPAYMENT_PLAN.getCode();

    /**
     * 脚本MD5Version
     */
    private String executeCodeVersion;

    /**
     * 可用控制状态:1可用 0不可用
     *
     * @see DelayTaskConfigStatus
     */
    private Integer status = DelayTaskConfigStatus.INVALID.getCode();

    /**
     * 预留字段
     **/
    private Date dateFieldOne;

    private Date dateFieldTwo;

    private Date dateFieldThree;

    private Long longFieldOne;

    private Long longFieldTwo;

    private Long longFieldThree;

    private String stringFieldOne;

    private String stringFieldTwo;

    private String stringFieldThree;

    private BigDecimal decimalFieldOne;

    private BigDecimal decimalFieldTwo;

    private BigDecimal decimalFieldThree;

    public DelayProcessingTaskConfig() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProductLv1Code() {
        return productLv1Code;
    }

    public void setProductLv1Code(String productLv1Code) {
        this.productLv1Code = productLv1Code;
    }

    public String getProductLv1Name() {
        return productLv1Name;
    }

    public void setProductLv1Name(String productLv1Name) {
        this.productLv1Name = productLv1Name;
    }

    public String getProductLv2Code() {
        return productLv2Code;
    }

    public void setProductLv2Code(String productLv2Code) {
        this.productLv2Code = productLv2Code;
    }

    public String getProductLv2Name() {
        return productLv2Name;
    }

    public void setProductLv2Name(String productLv2Name) {
        this.productLv2Name = productLv2Name;
    }

    public String getProductLv3Code() {
        return productLv3Code;
    }

    public void setProductLv3Code(String productLv3Code) {
        this.productLv3Code = productLv3Code;
    }

    public String getProductLv3Name() {
        return productLv3Name;
    }

    public void setProductLv3Name(String productLv3Name) {
        this.productLv3Name = productLv3Name;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getTriggerTypeCode() {
        return triggerTypeCode;
    }

    public void setTriggerTypeCode(Integer triggerTypeCode) {
        this.triggerTypeCode = triggerTypeCode;
    }

    public String getExecuteCodeVersion() {
        return executeCodeVersion;
    }

    public void setExecuteCodeVersion(String executeCodeVersion) {
        this.executeCodeVersion = executeCodeVersion;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getDateFieldOne() {
        return dateFieldOne;
    }

    public void setDateFieldOne(Date dateFieldOne) {
        this.dateFieldOne = dateFieldOne;
    }

    public Date getDateFieldTwo() {
        return dateFieldTwo;
    }

    public void setDateFieldTwo(Date dateFieldTwo) {
        this.dateFieldTwo = dateFieldTwo;
    }

    public Date getDateFieldThree() {
        return dateFieldThree;
    }

    public void setDateFieldThree(Date dateFieldThree) {
        this.dateFieldThree = dateFieldThree;
    }

    public Long getLongFieldOne() {
        return longFieldOne;
    }

    public void setLongFieldOne(Long longFieldOne) {
        this.longFieldOne = longFieldOne;
    }

    public Long getLongFieldTwo() {
        return longFieldTwo;
    }

    public void setLongFieldTwo(Long longFieldTwo) {
        this.longFieldTwo = longFieldTwo;
    }

    public Long getLongFieldThree() {
        return longFieldThree;
    }

    public void setLongFieldThree(Long longFieldThree) {
        this.longFieldThree = longFieldThree;
    }

    public String getStringFieldOne() {
        return stringFieldOne;
    }

    public void setStringFieldOne(String stringFieldOne) {
        this.stringFieldOne = stringFieldOne;
    }

    public String getStringFieldTwo() {
        return stringFieldTwo;
    }

    public void setStringFieldTwo(String stringFieldTwo) {
        this.stringFieldTwo = stringFieldTwo;
    }

    public String getStringFieldThree() {
        return stringFieldThree;
    }

    public void setStringFieldThree(String stringFieldThree) {
        this.stringFieldThree = stringFieldThree;
    }

    public BigDecimal getDecimalFieldOne() {
        return decimalFieldOne;
    }

    public void setDecimalFieldOne(BigDecimal decimalFieldOne) {
        this.decimalFieldOne = decimalFieldOne;
    }

    public BigDecimal getDecimalFieldTwo() {
        return decimalFieldTwo;
    }

    public void setDecimalFieldTwo(BigDecimal decimalFieldTwo) {
        this.decimalFieldTwo = decimalFieldTwo;
    }

    public BigDecimal getDecimalFieldThree() {
        return decimalFieldThree;
    }

    public void setDecimalFieldThree(BigDecimal decimalFieldThree) {
        this.decimalFieldThree = decimalFieldThree;
    }
}
