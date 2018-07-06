package com.suidifu.matryoshka.service;

import com.demo2do.core.service.GenericService;
import com.suidifu.matryoshka.snapshot.RepurchaseDocSnapshot;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;

public interface RepurchaseSnapshotService extends GenericService<RepurchaseDoc>{
	RepurchaseDocSnapshot getEffectiveRepurchaseDocSnapshotBy(Long contractId);
}
