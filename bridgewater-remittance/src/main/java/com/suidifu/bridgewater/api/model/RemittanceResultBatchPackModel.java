package com.suidifu.bridgewater.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by chengll on 17-4-6.
 * 对查询结果进行批量封装
 */
public class RemittanceResultBatchPackModel {


    /**
     * 功能代码
     */
    private String fn;

    /**
     * 当前批量请求编号
     */
    private String requestNo;

    /**
     * 校验失败信息
     */
    private String checkFailedMsg;
    
    /**
     * 信托合同代码
     */
    private String productCode;

    /**
     * 封装批量查询对象字符串
     */
    private String remittanceResultBatchQueryModels;


    /**
     * 封装批量查询对象
     */
    private List<RemittanceResultBatchQueryModel> remittanceResultBatchQueryModelList;

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    @JSONField(serialize = false)
    public List<RemittanceResultBatchQueryModel> getRemittanceResultBatchQueryModelList() {
        if(CollectionUtils.isNotEmpty(this.remittanceResultBatchQueryModelList)) {
            return remittanceResultBatchQueryModelList;
        }
        this.remittanceResultBatchQueryModelList = JsonUtils.parseArray(this.remittanceResultBatchQueryModels, RemittanceResultBatchQueryModel.class);
        if(CollectionUtils.isNotEmpty(remittanceResultBatchQueryModelList)) {
            return remittanceResultBatchQueryModelList;
        }
        return null;
    }

    public void setRemittanceResultBatchQueryModelList(List<RemittanceResultBatchQueryModel> remittanceResultBatchQueryModelList) {
        this.remittanceResultBatchQueryModelList = remittanceResultBatchQueryModelList;
    }

    public void setRemittanceResultBatchQueryModels(String remittanceResultBatchQueryModels) {
        this.remittanceResultBatchQueryModels = remittanceResultBatchQueryModels;
    }

    public String getRemittanceResultBatchQueryModels() {
        return remittanceResultBatchQueryModels;
    }

    public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@JSONField(serialize = false)
    public String getCheckFailedMsg() {
        return checkFailedMsg;
    }

    public void setCheckFailedMsg(String checkFailedMsg) {
        this.checkFailedMsg = checkFailedMsg;
    }

    @JSONField(serialize = false)
    public boolean isValid() {

        if (StringUtils.isEmpty(this.fn)) {
            this.checkFailedMsg = "功能代码唯一标识［fn］,不能为空!";
            return false;
        }

        if(StringUtils.isEmpty(this.requestNo)) {
            this.checkFailedMsg = "批量请求唯一标识［requestNo］，不能为空！";
            return false;
        }

        if(CollectionUtils.isEmpty(this.getRemittanceResultBatchQueryModelList())) {
            this.checkFailedMsg = "批量请求中请求信息 [remittanceResultBatchQueryModels] , 不能为空";
            return false;
        }
        
        for (int i = 0; i < remittanceResultBatchQueryModelList.size(); i++) {
            if(!remittanceResultBatchQueryModelList.get(i).isValid()){
                this.checkFailedMsg = remittanceResultBatchQueryModelList.get(i).getCheckFailedMsg();
                return false;
            }
        }
        return true;
    }

}

