package com.zufangbao.earth.yunxin.api.model.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.demo2do.core.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * 批量还款计划查询接口
 * @author zhanghongbing
 *
 */
public class RepaymentPlanBatchQueryModel {

	/**
	 * 请求唯一编号
	 */
	private String requestNo;

	/**
	 * 贷款合同唯一编号JSONArray
	 */
	private String uniqueIds;
	
	/**
	 * 贷款合同唯一编号List
	 */
	private List<String> uniqueIdsList;

	/**
	 * 信托产品代码
	 */
	private String productCode;

	/**
	 * 计划还款日（yyyy-MM-dd）
	 */
	private String planRepaymentDate;
	
	/**
	 * 校验失败信息
	 */
	private String checkFailedMsg;

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public String getUniqueIds() {
		return uniqueIds;
	}

	public void setUniqueIds(String uniqueIds) {
		this.uniqueIds = uniqueIds;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPlanRepaymentDate() {
		return planRepaymentDate;
	}
	
	@JSONField(serialize = false)
	public Date getDateTypePlanRepaymentDate() {
		try {
			return DateUtils.parseDate(this.planRepaymentDate, new String[] {"yyyy-MM-dd"});
		} catch (Exception e) {
			return null;
		}
	}

	public void setPlanRepaymentDate(String planRepaymentDate) {
		this.planRepaymentDate = planRepaymentDate;
	}
	
	@JSONField(serialize = false)
	public String getCheckFailedMsg() {
		return checkFailedMsg;
	}
	public void setCheckFailedMsg(String checkFailedMsg) {
		this.checkFailedMsg = checkFailedMsg;
	}

	@JSONField(serialize = false)
	public List<String> getUniqueIdsList() {
		if(CollectionUtils.isNotEmpty(this.uniqueIdsList)) {
			return uniqueIdsList;
		}
		this.uniqueIdsList = JsonUtils.parseArray(this.uniqueIds, String.class);
		return uniqueIdsList;
	}

	@JSONField(serialize = false)
	public List<String> getUniqueIdsList(String batchQuerySize) {
		if(CollectionUtils.isNotEmpty(this.uniqueIdsList)) {
			return uniqueIdsList;
		}
		this.uniqueIdsList = JsonUtils.parseArray(this.uniqueIds, String.class);
		try {
			int sub_size = Integer.valueOf(batchQuerySize);
			if (CollectionUtils.isNotEmpty(this.uniqueIdsList) && this.uniqueIdsList.size() > sub_size) {
				this.uniqueIdsList = this.uniqueIdsList.subList(0, sub_size);
			}
		}catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return uniqueIdsList;
	}

	@JSONField(serialize = false)
	public boolean isValid() {
		if(StringUtils.isEmpty(this.requestNo)) {
			this.checkFailedMsg = "请求唯一标识［requestNo］，不能为空！";
			return false;
		}
		if(StringUtils.isEmpty(this.productCode)) {
			this.checkFailedMsg = "信托产品代码［productCode］，不能为空！";
			return false;
		}
		if(CollectionUtils.isEmpty(getUniqueIdsList()) && getDateTypePlanRepaymentDate() == null) {
			this.checkFailedMsg = "贷款合同唯一编号［uniqueIds］、计划还款日［planRepaymentDate］，不能同时为空！";
			return false;
		}
		return true;
	}
	
}
