package com.suidifu.matryoshka.delayTask;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;

/**
 * 后置处理任务 Created by louguanyang on 2017/5/2.
 */
@Entity
@Table(name = "t_delay_processing_task")
public class DelayProcessingTask extends BaseDelayProcessingTask {
	@Id
	@GeneratedValue
	private Long id;

	public DelayProcessingTask() {
		super();
		this.setUuid(UUID.randomUUID().toString());
	}

	public DelayProcessingTask(PaymentPlanSnapshot paymentPlanSnapshot, Date taskExecuteDate, String workParams,
			String configUuid) {
		this();
		Date now = new Date();
		setCreateTime(now);
		setLastModifyTime(now);
		setTaskExecuteDate(taskExecuteDate);
		if (paymentPlanSnapshot != null) {
			setRepaymentPlanUuid(paymentPlanSnapshot.getAssetUuid());
			setFinancialContractUuid(paymentPlanSnapshot.getFinancialContractUuid());
			setContractUuid(paymentPlanSnapshot.getContractUuid());
			setCustomerUuid(paymentPlanSnapshot.getCustomerUuid());
		}
		setWorkParams(workParams);
		this.setConfigUuid(configUuid);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
