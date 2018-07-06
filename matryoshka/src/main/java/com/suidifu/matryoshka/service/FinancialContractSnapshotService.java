/**
 * 
 */
package com.suidifu.matryoshka.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.matryoshka.snapshot.FinancialContractSnapshot;
import com.zufangbao.sun.entity.financial.FinancialContract;

/**
 * @author zjm
 *
 */

public interface FinancialContractSnapshotService extends GenericService<FinancialContract> {

	FinancialContractSnapshot get_financialcontractsnapshot_by_financialcontractuuid(String financialContractUuid);

	FinancialContractSnapshot get_financialcontractsnapshot_by_contractno(String contractNo);
}
