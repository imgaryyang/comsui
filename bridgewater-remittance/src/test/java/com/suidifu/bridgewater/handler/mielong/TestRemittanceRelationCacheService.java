package com.suidifu.bridgewater.handler.mielong;

import com.suidifu.giotto.service.relation.RemittanceRelation;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationDetail;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Collectors;

public class TestRemittanceRelationCacheService extends AbstractNotRollBackBaseTest {
	private String remittancePlanUuid = "269548eb-9963-4eae-9c4e-daaf36e8e4f1";

	private String remittanceApplicationDetailUuid_1 = "c1bda00d-c3bb-4e40-9f99-88c04d145b3b";

	private String remittanceApplicationDetailUuid_2 = "54f6e54c-d0e9-4056-89c8-d7b194c773d3";

	private String remittanceApplicationUuid = "400efe7d-6603-421b-a4e5-f68e1f5e9589";

	@Test
	@Sql({"classpath:test/remittance/delete_all_table_remittance.sql",
			"classpath:test/remittance/test_remittance_ww1.sql"})
	public void testAdd() {
		List<RemittanceApplicationDetail> details = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByApplicationUuid(remittanceApplicationUuid);
		List<String> detail_uuid_list = details.stream().map(RemittanceApplicationDetail::getRemittanceApplicationDetailUuid).collect(Collectors.toList());
		Assert.assertEquals(2, details.size());
		String detail_uuid_1 = detail_uuid_list.get(0);
		String detail_uuid_2 = detail_uuid_list.get(1);
		List<RemittancePlan> plans = iRemittancePlanService.getRemittancePlanListBy(remittanceApplicationUuid);
		List<String> plan_uuid_list = plans.stream().map(RemittancePlan::getRemittancePlanUuid).collect(Collectors.toList());
		Assert.assertEquals(2, plans.size());
		List<RemittancePlan> plan_list_0 = iRemittancePlanService.getRemittancePlanListByRemittanceApplicationDetailUuid(detail_uuid_1);
		Assert.assertEquals(1, plan_list_0.size());
		List<RemittancePlan> plan_list_1 = iRemittancePlanService.getRemittancePlanListByRemittanceApplicationDetailUuid(detail_uuid_2);
		Assert.assertEquals(1, plan_list_1.size());
		List<String> plan_uuid_list_1 = plan_list_0.stream().map(RemittancePlan::getRemittancePlanUuid).collect(Collectors.toList());
		List<String> plan_uuid_list_2 = plan_list_1.stream().map(RemittancePlan::getRemittancePlanUuid).collect(Collectors.toList());
		remittanceRelationCacheService.add(RemittanceRelation.RA_RD, remittanceApplicationUuid, detail_uuid_list);
		remittanceRelationCacheService.add(RemittanceRelation.RD_RP, detail_uuid_1, plan_uuid_list_1);
		remittanceRelationCacheService.add(RemittanceRelation.RD_RP, detail_uuid_2, plan_uuid_list_2);
		remittanceRelationCacheService.add(RemittanceRelation.RA_RP, remittanceApplicationUuid, plan_uuid_list);

	}

	@Test
	@Sql({"classpath:test/remittance/delete_all_table_remittance.sql",
			"classpath:test/remittance/test_remittance_ww1.sql"})
	public void testGet() {
		System.out.println(remittanceRelationCacheService.get(RemittanceRelation.RA_RD, remittanceApplicationUuid));
		System.out.println(remittanceRelationCacheService.get(RemittanceRelation.RD_RP, remittanceApplicationDetailUuid_1));
		System.out.println(remittanceRelationCacheService.get(RemittanceRelation.RD_RP, remittanceApplicationDetailUuid_2));
		System.out.println(remittanceRelationCacheService.get(RemittanceRelation.RA_RP, remittanceApplicationUuid));
	}

	@Test
	public void testDelete() {
		remittanceRelationCacheService.delete(RemittanceRelation.RA_RP, "400efe7d-6603-421b-a4e5-f68e1f5e9589");
	}
}
