package com.suidifu.jpmorgan.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.DistributeStat;

public interface DistributeStatService extends GenericService<DistributeStat> {
	public List<DistributeStat> select(String paymentChannelUuid);
	public void insert(Date date, String paymentChannelUuid, BigDecimal amount);
	public void update(Date date, DistributeStat distributeStat, BigDecimal amount);
}
