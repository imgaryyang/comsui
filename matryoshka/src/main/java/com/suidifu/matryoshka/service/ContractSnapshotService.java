package com.suidifu.matryoshka.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.zufangbao.sun.entity.contract.Contract;

public interface ContractSnapshotService extends GenericService<Contract> {
	ContractSnapshot get_contractSnapshot_by_contractUuid(String contractUuid);

	ContractSnapshot get_contractSnapshot_by_uniqueId_or_contractNo(String uniqueId, String contractNo);

	ContractSnapshot getContractSnapshot(Contract contract);

	Contract getContractBy(String uniqueId, String contractNo);

}