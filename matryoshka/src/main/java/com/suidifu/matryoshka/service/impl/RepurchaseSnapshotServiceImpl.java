package com.suidifu.matryoshka.service.impl;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.matryoshka.service.RepurchaseSnapshotService;
import com.suidifu.matryoshka.snapshot.RepurchaseDocSnapshot;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("repurchaseSnapshotService")
public class RepurchaseSnapshotServiceImpl extends GenericServiceImpl<RepurchaseDoc>
		implements RepurchaseSnapshotService {

	@SuppressWarnings("unchecked")
	public RepurchaseDoc getRepurchaseDocBy(Long contractId) {
		if (contractId == null) {
			return null;
		}
		String queryStr = "FROM RepurchaseDoc WHERE contractId =:contractId AND repurchaseStatus != :invalid";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("contractId", contractId);
		param.put("invalid", RepurchaseStatus.INVALID);
		List<RepurchaseDoc> repurchaseDocs = this.genericDaoSupport.searchForList(queryStr, param);
		if (CollectionUtils.isNotEmpty(repurchaseDocs) && repurchaseDocs.size() == 1) {
			return repurchaseDocs.get(0);
		}
		return null;
	}

	@Override
	public RepurchaseDocSnapshot getEffectiveRepurchaseDocSnapshotBy(Long contractId) {
		RepurchaseDoc repurchaseDoc = this.getRepurchaseDocBy(contractId);
		if (repurchaseDoc == null)
			return null;
		return new RepurchaseDocSnapshot(repurchaseDoc);
	}

}
