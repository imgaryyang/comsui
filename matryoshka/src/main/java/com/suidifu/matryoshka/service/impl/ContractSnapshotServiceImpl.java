package com.suidifu.matryoshka.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.matryoshka.service.ContractSnapshotService;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.suidifu.matryoshka.snapshot.CustomerAccountSnapshot;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.CustomerService;

@Service("contractSnapshotService")
public class ContractSnapshotServiceImpl extends GenericServiceImpl<Contract> implements ContractSnapshotService {
	@Autowired
	private GenericDaoSupport genericDaoSupport;

	public Contract getContract(String contractUuid) {
		if (StringUtils.isEmpty(contractUuid)) {
			return null;
		}
		Filter filter = new Filter();
		filter.addEquals("uuid", contractUuid);
		List<Contract> contractList = list(Contract.class, filter);
		if (CollectionUtils.isEmpty(contractList)) {
			return null;
		}
		return contractList.get(0);
	}

	@SuppressWarnings("unchecked")
	public Contract getContractByContractNo(String contractNo) {
		if (StringUtils.isEmpty(contractNo)) {
			return null;
		}
		List<Contract> contractLists = this.genericDaoSupport
				.searchForList("from Contract where contractNo=:contractNo", "contractNo", contractNo);
		if (contractLists.size() == 0) {
			return null;
		}
		return contractLists.get(0);
	}

	public Contract getContractByUniqueId(String uniqueId) {
		if (StringUtils.isBlank(uniqueId)) {
			return null;
		}
		Filter filter = new Filter();
		filter.addEquals("uniqueId", uniqueId);
		List<Contract> contracts = this.list(Contract.class, filter);
		if (CollectionUtils.isEmpty(contracts)) {
			return null;
		}
		return contracts.get(0);
	}

	@Override
	public Contract getContractBy(String uniqueId, String contractNo) {
		
		Contract contract=null;
		
		if (StringUtils.isNotEmpty(uniqueId)) {
			contract =  getContractByUniqueId(uniqueId);
			if(null != contract) {
				return contract;
			}
		}
		if (StringUtils.isNotEmpty(contractNo)) {
			contract = getContractByContractNo(contractNo);
		}
		return contract;
	}

	@Override
	public ContractSnapshot get_contractSnapshot_by_contractUuid(String contractUuid) {
		Contract contract = getContract(contractUuid);
		if (null == contract) {
			return null;
		}
		return getContractSnapshot(contract);
	}

	@Override
	public ContractSnapshot getContractSnapshot(Contract contract) {
		try {
			ContractSnapshot snapshot = new ContractSnapshot(contract);
			CustomerAccountSnapshot customerAccountSnapshot = getCustomerAccountSnapshot(contract);
			snapshot.setCustomerAccountSnapshot(customerAccountSnapshot);
			return snapshot;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ContractSnapshot get_contractSnapshot_by_uniqueId_or_contractNo(String uniqueId, String contractNo) {
		Contract contract = getContractBy(uniqueId, contractNo);
		if (null == contract) {
			return null;
		}
		return getContractSnapshot(contract);
	}

	@Autowired
	CustomerService customerService;

	@Autowired
	ContractAccountService contractAccountService;

	private CustomerAccountSnapshot getCustomerAccountSnapshot(Contract contract) {
		Customer customer = customerService.getCustomer(contract.getCustomerUuid());
		ContractAccount contractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
		CustomerAccountSnapshot customerAccountSnapshot = new CustomerAccountSnapshot(contract.getUuid(), customer,
				contractAccount);
		return customerAccountSnapshot;
	}

}
