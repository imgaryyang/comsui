package com.suidifu.bridgewater.handler.mielong;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.task.CombineNoticeTask;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;

public class TestRunning extends AbstractNotRollBackBaseTest{
	
	@Autowired
	CombineNoticeTask combineNoticeTask;
	
	String remittanceApplicationUuid = "2c2a6c5f-0260-4631-902c-ac1634b3e650";

	private static Log logger = LogFactory.getLog(TestRunning.class);

	@Autowired
	private TestRunningHandler testRunningHandler;
	@Test
	public void test1030_1154() {
	}
	
	@Test
	public void test1030_1041() {
		String remittanceApplicationUuid = "0c7b1d46-2fd1-4b2e-a49d-775bef3ca620";
		
		String detailUuid = "9fc3b06c-d9d8-4dad-a5d9-df3fb283c241";
		
		selectDetailByApplicationUuid(remittanceApplicationUuid);
		
		selectDetailByDetailUuid(detailUuid);
	}
	private void selectDetailByApplicationUuid(String applicationUuid) {
		String sentence = "select remittance_application_uuid as remittanceApplicationUuid , financial_contract_uuid as financialContractUuid , financial_contract_id as executionStatus , remittance_application_detail_uuid as  checkRequestNo from  t_remittance_application_detail  where remittance_application_uuid =:remittanceApplicationUuid";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("remittanceApplicationUuid", applicationUuid);
		List<RemittanceSqlModel> modes = this.genericDaoSupport.queryForList(sentence, parameters, RemittanceSqlModel.class);
		for (RemittanceSqlModel mode : modes) {
			logger.info("remittance_application_detail_uuid:["+mode.getCheckRequestNo()+"],remittance_application_uuid:["+mode.getRemittanceApplicationUuid()+"],financial_contract_uuid:["+mode.getFinancialContractUuid()+"],financial_contract_id:["+mode.getExecutionStatus()+"],total_count:["+mode.getPlanNotifyNumber()+"].");
			System.out.println("remittance_application_detail_uuid:["+mode.getCheckRequestNo()+"],remittance_application_uuid:["+mode.getRemittanceApplicationUuid()+"],financial_contract_uuid:["+mode.getFinancialContractUuid()+"],financial_contract_id:["+mode.getExecutionStatus()+"],total_count:["+mode.getPlanNotifyNumber()+"].");
		}
	}
	
	private void selectDetailByDetailUuid(String detailUuid) {
		String sentence = "select remittance_application_uuid as remittanceApplicationUuid , financial_contract_uuid as financialContractUuid , financial_contract_id as executionStatus , remittance_application_detail_uuid as  checkRequestNo from  t_remittance_application_detail  where remittance_application_detail_uuid =:remittanceApplicationDetailUuid";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("remittanceApplicationDetailUuid", detailUuid);
		List<RemittanceSqlModel> modes = this.genericDaoSupport.queryForList(sentence, parameters, RemittanceSqlModel.class);
		for (RemittanceSqlModel mode : modes) {
			logger.info("remittance_application_detail_uuid:["+mode.getCheckRequestNo()+"],remittance_application_uuid:["+mode.getRemittanceApplicationUuid()+"],financial_contract_uuid:["+mode.getFinancialContractUuid()+"],financial_contract_id:["+mode.getExecutionStatus()+"],total_count:["+mode.getPlanNotifyNumber()+"].");
			System.out.println("remittance_application_detail_uuid:["+mode.getCheckRequestNo()+"],remittance_application_uuid:["+mode.getRemittanceApplicationUuid()+"],financial_contract_uuid:["+mode.getFinancialContractUuid()+"],financial_contract_id:["+mode.getExecutionStatus()+"],total_count:["+mode.getPlanNotifyNumber()+"].");
		}
	}
	
	@Test
	public void test235235235() {
		int plan_total_count = 1;
		String detailUuid ="165165e8-ab1c-4a34-ba42-ced56eed64fd";
		String sql = "update t_remittance_application_detail set total_count=:totalCount where remittance_application_detail_uuid=:remittanceApplicationDetailUuid";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("totalCount", plan_total_count);
		parameters.put("remittanceApplicationDetailUuid", detailUuid);

		this.genericDaoSupport.executeSQL(sql, parameters);
		
	}
	
	@Test
	public void test347347() {
		
		iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		
		iRemittanceApplicationService.getRemittanceSqlModelListBy(Arrays.asList("",""));
		
	}
	
	@Test
	public void test3265356() {
		String remittancePlanUuid = "e00f73a2-9558-411f-9585-71e265cdedbc";
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("remittancePlanUuid", remittancePlanUuid);
		
		this.genericDaoSupport.executeSQL("UPDATE t_remittance_plan_exec_log set  financial_contract_uuid = 'xx6236236afga' where  remittance_plan_uuid =:remittancePlanUuid ", params);
		
		
		this.genericDaoSupport.executeSQL("UPDATE t_remittance_plan set  financial_contract_uuid = '2xx36236xxadas' where  remittance_plan_uuid =:remittancePlanUuid ", params);

		
	}
	
	@Test
	public void test() {
		combineNoticeTask.run();
	}
	
	@Test
	public void test214214() {
		String remittanceApplicationUuid = "e5a01477-431f-4382-a124-a8e550eb6f8f";
		long financialContractId = 38L;
		String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
		
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractId", financialContractId);
		params.put("financialContractUuid", financialContractUuid);
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		params.put("checkRequestNo", UUID.randomUUID().toString());

		String detail_sql = " UPDATE t_remittance_application_detail SET financial_contract_id =:financialContractId,financial_contract_uuid =:financialContractUuid WHERE remittance_application_uuid =:remittanceApplicationUuid ";
		genericDaoSupport.executeSQL(detail_sql, params);

	}
	
	@Test
	public void test2356236236() {
		String remittanceApplicationUuid = "32bdb67e-6c87-442d-a50c-50f2aee7755e";
		RemittanceSqlModel remittanceSqlMode = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
		
		System.out.println(JsonUtils.toJsonString(remittanceSqlMode));
	}
	
	@Test
	public void test111() {
		String remittanceApplicationUuid = "2c2a6c5f-0260-4631-902c-ac1634b3e650";
		RemittanceApplication remittanceApplication = iRemittanceApplicationService.getRemittanceApplicationBy(remittanceApplicationUuid);

		updateRemittanceInfoAfterSendFailBy(remittanceApplication.getRemittanceApplicationUuid(), "fail_message");

		RemittanceApplication remittanceApplication2 = iRemittanceApplicationService.getRemittanceApplicationBy(remittanceApplicationUuid);
		
		System.out.println(remittanceApplication2.getExecutionStatus());
	}
	private void updateRemittanceInfoAfterSendFailBy(String remittanceApplicationUuid, String executionRemark) {
		Map<String, Object> paramsForFail = new HashMap<String, Object>();
		paramsForFail.put("executionStatus", ExecutionStatus.FAIL.ordinal());
		paramsForFail.put("executionRemark", executionRemark);
		paramsForFail.put("lastModifiedTime", new Date());
		paramsForFail.put("remittanceApplicationUuid", remittanceApplicationUuid);
		
		executeHqlForRemittanceExceptApplicationFail("t_remittance_application_detail", paramsForFail);
		executeHqlForRemittanceExceptApplicationFail("t_remittance_plan", paramsForFail);
		executeHqlForRemittanceExceptApplicationFail("t_remittance_plan_exec_log", paramsForFail);
		
		executeHqlForRemittanceApplicationFail(remittanceApplicationUuid, paramsForFail);
	}
	
	private void executeHqlForRemittanceExceptApplicationFail(String tableName, Map<String, Object> paramsForFail) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ");
		buffer.append(tableName);
		buffer.append(" SET execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_application_uuid =:remittanceApplicationUuid");
		
		genericDaoSupport.executeSQL(buffer.toString(), paramsForFail);
	}
	
	private void executeHqlForRemittanceApplicationFail(String remittanceApplicationUuid, Map<String, Object> paramsForFail) {
		Map<String, Object> params = new HashMap<>(paramsForFail);
		params.put("checkRequestNo", UUID.randomUUID().toString());
		
		String sql = "UPDATE t_remittance_application SET check_request_no =:checkRequestNo,version_lock =:newVersion, execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime WHERE remittance_application_uuid =:remittanceApplicationUuid and version_lock =:oldVersion ";
		
		iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, sql, params);
	}
}		
