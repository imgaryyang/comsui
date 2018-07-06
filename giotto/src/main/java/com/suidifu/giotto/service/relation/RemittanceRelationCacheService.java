package com.suidifu.giotto.service.relation;

import java.util.List;

public interface RemittanceRelationCacheService {

	void add(RemittanceRelation relation, String relationUuid, List<String> uuids);

	List<String> get(RemittanceRelation relation, String relationUuid);

	void delete(RemittanceRelation relation, String relationUuid);


}
