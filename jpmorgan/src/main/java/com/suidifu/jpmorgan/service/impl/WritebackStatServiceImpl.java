package com.suidifu.jpmorgan.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.BusinessStatus;
import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.WritebackStat;
import com.suidifu.jpmorgan.service.WritebackStatService;

@Service("writebackStatService")
public class WritebackStatServiceImpl extends GenericServiceImpl<WritebackStat>
		implements WritebackStatService {
	

	@Override
	public void update(WritebackStat writebackStat, GatewaySlot gatewaySlotUpdate, Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		BigDecimal amount = gatewaySlotUpdate.getTransactionAmount();
		if(BusinessStatus.Success.ordinal() == gatewaySlotUpdate.getBusinessStatus()) {
			long successNum = writebackStat.getSuccessNum() + 1;
			BigDecimal successAmount = writebackStat.getSuccessAmount();
			BigDecimal successAmountSum = successAmount.add(amount);
			BigDecimal failedAmount = writebackStat.getFailedAmount();
			long failedNum = writebackStat.getFailedNum();
			
			params.put("successNum", successNum);
			params.put("successAmount", successAmountSum);
			params.put("failedNum", failedNum);
			params.put("failedAmount", failedAmount);
		}else {
			
			long failedNum = writebackStat.getFailedNum() + 1;
			BigDecimal failedAmount = writebackStat.getFailedAmount();
			BigDecimal failedAmountSum = failedAmount.add(amount);
			long successNum = writebackStat.getSuccessNum();
			BigDecimal successAmount = writebackStat.getSuccessAmount();
			
			params.put("successNum", successNum);
			params.put("successAmount", successAmount);
			params.put("failedNum", failedNum);
			params.put("failedAmount", failedAmountSum);
		}
		
		params.put("id", writebackStat.getId());
		params.put("endTime", date);
		
		String sql = "UPDATE writeback_stat SET success_num =:successNum, success_amount =:successAmount, failed_num =:failedNum, failed_amount =:failedAmount, end_time =:endTime WHERE id =:id";
		genericDaoSupport.executeSQL(sql, params);
	}
	
	
	public List<WritebackStat> select(String paymentChannelUuid) {
		if(StringUtils.isEmpty(paymentChannelUuid)) {
			return Collections.EMPTY_LIST;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paymentChannelUuid", paymentChannelUuid);
		String sql = "select * from writeback_stat where payment_channel_uuid =:paymentChannelUuid AND date_add(start_time, interval 1800 second) > end_time";
		return genericDaoSupport.queryForList(sql, params, WritebackStat.class);
	}

	@Override
	public void insert(String paymentChannelUuid, GatewaySlot gatewaySlotUpdate, Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		BigDecimal amount = gatewaySlotUpdate.getTransactionAmount();
		if(BusinessStatus.Success.ordinal() == gatewaySlotUpdate.getBusinessStatus()) {
			
			params.put("successNum", 1);
			params.put("successAmount", amount);
			params.put("failedNum", 0);
			params.put("failedAmount", BigDecimal.ZERO);
		} else {
			
			params.put("successNum", 0);
			params.put("successAmount", BigDecimal.ZERO);
			params.put("failedNum", 1);
			params.put("failedAmount", amount);
		}
		
		params.put("paymentChannelUuid", paymentChannelUuid);
		params.put("endTime", date);
		params.put("startTime", date);	
		
		String sql = "INSERT INTO writeback_stat(`payment_channel_uuid`, `success_num`, `success_amount`, `failed_num`, `failed_amount`, `start_time`, `end_time`) VALUES (:paymentChannelUuid, :successNum, :successAmount, :failedNum, :failedAmount, :startTime, :endTime)";
		genericDaoSupport.executeSQL(sql, params);
		
	}

}
