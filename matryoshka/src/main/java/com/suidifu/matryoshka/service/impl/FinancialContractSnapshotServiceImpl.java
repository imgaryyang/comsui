package com.suidifu.matryoshka.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.matryoshka.service.FinancialContractSnapshotService;
import com.suidifu.matryoshka.snapshot.FinancialContractSnapshot;
import com.zufangbao.sun.entity.financial.FinancialContract;

/**
 * @author zjm
 *
 */
@Service("financialContractSnapshotService")
public class FinancialContractSnapshotServiceImpl extends GenericServiceImpl<FinancialContract>
		implements FinancialContractSnapshotService {

	private FinancialContract getFinancialContractBy(String financialContractUuid) {
		if (StringUtils.isEmpty(financialContractUuid)) {
			return null;
		}
		Filter filter = new Filter();
		filter.addEquals("financialContractUuid", financialContractUuid);
		List<FinancialContract> financialContracts = this.list(FinancialContract.class, filter);
		if (CollectionUtils.isNotEmpty(financialContracts)) {
			return financialContracts.get(0);
		}
		return null;
	}

	private FinancialContract getUniqueFinancialContractBy(String contractNo) {
		if (StringUtils.isEmpty(contractNo)) {
			return null;
		}
		Filter filter = new Filter();
		filter.addEquals("contractNo", contractNo);
		List<FinancialContract> financialContracts = this.list(FinancialContract.class, filter);
		if (CollectionUtils.isEmpty(financialContracts)) {
			return null;
		}
		return financialContracts.get(0);
	}

	@Override
	public FinancialContractSnapshot get_financialcontractsnapshot_by_financialcontractuuid(
			String financialContractUuid) {
		if (StringUtils.isEmpty(financialContractUuid)) {
			return null;
		}
		FinancialContract financialContract = getFinancialContractBy(financialContractUuid);
		if (null == financialContract) {
			return null;
		}
		FinancialContractSnapshot snapshot = new FinancialContractSnapshot(financialContract);
		return snapshot;
	}

	@Override
	public FinancialContractSnapshot get_financialcontractsnapshot_by_contractno(String contractNo) {
		if (StringUtils.isEmpty(contractNo)) {
			return null;
		}
		FinancialContract financialContract = getUniqueFinancialContractBy(contractNo);
		if (null == financialContract) {
			return null;
		}
		FinancialContractSnapshot snapshot = new FinancialContractSnapshot(financialContract);
		return snapshot;
	}

}