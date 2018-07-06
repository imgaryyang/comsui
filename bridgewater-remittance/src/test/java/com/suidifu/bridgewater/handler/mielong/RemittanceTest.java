package com.suidifu.bridgewater.handler.mielong;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.remittance.NotifyStatus;

public class RemittanceTest extends AbstractNotRollBackBaseTest {

	@Test
	@Sql({"classpath:test/remittance/test_remittance_ww3.sql" })
	public void testForGetMode() {
		/**	386ba9cc-c451-4367-8a35-3d6f3318b1a2
			c761962e-1a8d-4174-8517-eee4dc39bfcb
			4c9bab1e-d6dd-4ac9-aa3f-54463743180a
			5438aab8-ea58-48ea-b6de-be26ba305cf4**/
		
		String remittanceApplicationUuid = "386ba9cc-c451-4367-8a35-3d6f3318b1a2";
		
		RemittanceSqlModel mode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		Assert.assertEquals(remittanceApplicationUuid, mode.getRemittanceApplicationUuid());
		
		List<String> uuids = new ArrayList<>();
		uuids.add("386ba9cc-c451-4367-8a35-3d6f3318b1a2");
		uuids.add("c761962e-1a8d-4174-8517-eee4dc39bfcb");
		uuids.add("4c9bab1e-d6dd-4ac9-aa3f-54463743180a");
		uuids.add("5438aab8-ea58-48ea-b6de-be26ba305cf4");
		List<RemittanceSqlModel> modeList =  iRemittanceApplicationService.getRemittanceSqlModelListBy(uuids);
		Assert.assertEquals(4, modeList.size());

	}
	
	@Test
	@Sql({"classpath:test/remittance/test_remittance_ww3.sql" })
	public void testForUpdateRemittanceExecuteIfFailedTrue() {
		String remittanceApplicationUuid = "386ba9cc-c451-4367-8a35-3d6f3318b1a2";
		RemittanceSqlModel mode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		Assert.assertTrue(NotifyStatus.FIRST_PUSH_TO_OUTLIER.ordinal() == mode.getNotifyStatus());
		Assert.assertTrue(mode.getActualNotifyNumber() == 1);
		Assert.assertTrue(mode.getPlanNotifyNumber() == 3);

		iRemittanceApplicationHandler.updateRemittanceExecuteIfFailed(remittanceApplicationUuid, true);
		RemittanceSqlModel mode2 = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		Assert.assertTrue(NotifyStatus.RETRY_PUSH_TO_OUTLIER.ordinal() == mode2.getNotifyStatus());
		Assert.assertTrue(mode2.getActualNotifyNumber() == 2);
		Assert.assertTrue(mode2.getPlanNotifyNumber() == 3);
	}
	
	@Test
	@Sql({"classpath:test/remittance/test_remittance_ww3.sql" })
	public void testForUpdateRemittanceExecuteIfFailedFalse() {
		String remittanceApplicationUuid = "386ba9cc-c451-4367-8a35-3d6f3318b1a2";
		RemittanceSqlModel mode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		Assert.assertTrue(NotifyStatus.FIRST_PUSH_TO_OUTLIER.ordinal() == mode.getNotifyStatus());
		Assert.assertTrue(mode.getActualNotifyNumber() == 1);
		Assert.assertTrue(mode.getPlanNotifyNumber() == 3);

		iRemittanceApplicationHandler.updateRemittanceExecuteIfFailed(remittanceApplicationUuid, false);
		RemittanceSqlModel mode2 = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		Assert.assertTrue(NotifyStatus.FIRST_PUSH_TO_OUTLIER.ordinal() == mode2.getNotifyStatus());
		Assert.assertTrue(mode2.getActualNotifyNumber() == 2);
		Assert.assertTrue(mode2.getPlanNotifyNumber() == 3);
	}
	
	@Test
	@Sql({"classpath:test/remittance/test_remittance_ww3.sql" })
	public void testForUpdateRemittanceExecuteIfSuccess() {
		String remittanceApplicationUuid = "386ba9cc-c451-4367-8a35-3d6f3318b1a2";
		RemittanceSqlModel mode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		Assert.assertTrue(NotifyStatus.FIRST_PUSH_TO_OUTLIER.ordinal() == mode.getNotifyStatus());
		Assert.assertTrue(mode.getActualNotifyNumber() == 1);
		Assert.assertTrue(mode.getPlanNotifyNumber() == 3);

		iRemittanceApplicationHandler.updateRemittanceExecuteIfSuccess(remittanceApplicationUuid);
		RemittanceSqlModel mode2 = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		Assert.assertTrue(NotifyStatus.FIRST_PUSH_TO_OUTLIER.ordinal() == mode2.getNotifyStatus());
		Assert.assertTrue(mode2.getActualNotifyNumber() == 2);
		Assert.assertTrue(mode2.getPlanNotifyNumber() == 2);
	}
	
	@Test
	@Sql({"classpath:test/remittance/test_remittance_ww3.sql" })
	public void testForRemittanceResultQuery() {
		
		/**lastModifyTime 2017-11-03 16:41:16	2017-11-08 14:20:03 */
		/**54939cdb-40eb-496a-b3c6-f9c5467cd5cd	79475f11-733f-41a8-8518-c57b98da79ce */
		
		Date queryStartDate = DateUtils.parseDate("2017-11-02 00:00:00", "yyyy-MM-dd HH:mm:ss");
		int limit = 100;
		List<String> uuids = iRemittanceApplicationHandler.getRemittanceApplicationUuidsInOppositeProcessing(queryStartDate, limit, new Date());
		Assert.assertTrue(uuids.size() == 2);
		Assert.assertTrue(uuids.contains("54939cdb-40eb-496a-b3c6-f9c5467cd5cd"));
		Assert.assertTrue(uuids.contains("79475f11-733f-41a8-8518-c57b98da79ce"));
		
	}
	@Test
	public void test() {
		Date queryStatusDelay = org.apache.commons.lang.time.DateUtils.addMinutes(new Date(), -60);
		System.out.println(DateUtils.format(queryStatusDelay,"yyyy-MM-dd HH:mm:ss"));

	}
}
