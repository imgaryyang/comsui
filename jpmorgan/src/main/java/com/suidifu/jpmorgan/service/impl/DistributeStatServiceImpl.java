package com.suidifu.jpmorgan.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.DistributeStat;
import com.suidifu.jpmorgan.service.DistributeStatService;

@Service("distributeStatservice")
public class DistributeStatServiceImpl extends GenericServiceImpl<DistributeStat> implements DistributeStatService  {

	@Override
	public List<DistributeStat> select(String paymentChannelUuid) {
		if(StringUtils.isEmpty(paymentChannelUuid)){
			return Collections.EMPTY_LIST;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paymentChannelUuid", paymentChannelUuid);
		String sql = "SELECT * from distribute_stat WHERE payment_channel_uuid =:paymentChannelUuid AND date_add(start_time, interval 1800 second) > end_time";
		return genericDaoSupport.queryForList(sql, params, DistributeStat.class);
	}

	@Override
	public void insert(Date date, String paymentChannelUuid, BigDecimal amount) {
		if(StringUtils.isEmpty(paymentChannelUuid)){
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("paymentChannelUuid", paymentChannelUuid);
		params.put("startTime",date);
		params.put("endTime", date);
		params.put("distributeTotalAmount", amount);
		params.put("distributeTotalCount", 1);
		
		String sql = "INSERT INTO distribute_stat (`payment_channel_uuid`, `distribute_total_count`, `distribute_total_amount`, `start_time`, `end_time`) VALUES (:paymentChannelUuid, :distributeTotalCount, :distributeTotalAmount, :startTime, :endTime)";
		genericDaoSupport.executeSQL(sql, params);
	}

	@Override
	public void update(Date date, DistributeStat distributeStat, BigDecimal amount) {
	
		if(distributeStat == null) {
			return;
		}
		BigDecimal distributeStatAmount = distributeStat.getDistributeTotalAmount();
		BigDecimal statAmount = distributeStatAmount.add(amount);
		long distributeNum = distributeStat.getDistributeTotalCount() + 1;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("distributeStatId", distributeStat.getId());
		params.put("endTime", date);
		params.put("distributeTotalAmount", statAmount);
		params.put("distributeTotalCount", distributeNum);
		String sql = "UPDATE distribute_stat SET distribute_total_count =:distributeTotalCount, end_time =:endTime, distribute_total_amount =:distributeTotalAmount WHERE id =:distributeStatId";
		genericDaoSupport.executeSQL(sql, params);
	}
}
