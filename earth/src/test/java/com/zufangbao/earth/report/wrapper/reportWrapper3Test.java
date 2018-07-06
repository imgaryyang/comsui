package com.zufangbao.earth.report.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zufangbao.earth.report.factory.SqlCacheManager;
import com.zufangbao.earth.report.util.FreemarkerUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

		"classpath:/local/applicationContext-*.xml"})
@Transactional
@WebAppConfiguration(value="webapp")
public class reportWrapper3Test {
	
	@Autowired
	private SqlCacheManager sqlCacheManager;
	 
	@SuppressWarnings("serial")
	@Test
	public void testSql1(){
		String sqlTest="SELECT tmp_asst.*, c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, ca.payer_name AS customerName, ca.pay_ac_no AS payAcNo, ca.id_card_num AS idCardNum, ca.bank AS bank, ca.province AS province, ca.city AS city, (SELECT CONCAT_WS(\",\", SUM(CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END), SUM(CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END), SUM(CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END)) FROM asset_set_extra_charge asstec WHERE asstec.asset_set_uuid = tmp_asst.asset_uuid) AS chargeArray FROM ( SELECT asset_uuid, contract_id, financial_contract_uuid AS financialContractUuid, single_loan_contract_no AS repaymentPlanNo, asset_recycle_date AS assetRecycleDate, actual_recycle_date AS actualRecycleDate, asset_principal_value AS planPrincipal, asset_interest_value AS planInterest FROM asset_set WHERE financial_contract_uuid IN (:financialContractUuids)  AND single_loan_contract_no = :singleLoanContractNo   AND asset_recycle_date >=:repaymentPlanStartDate   AND asset_recycle_date <=:repaymentPlanEndDate   AND date(actual_recycle_date) >=:actualRepaymentStartDate   AND date(actual_recycle_date) <=:actualRepaymentEndDate    AND plan_type = 1 AND active_status = 0 AND can_be_rollbacked = 1     AND time_interval IN (1,2) AND executing_status = 0     AND repayment_plan_type = 1 AND contract_funding_status = 0 AND asset_status = 1   ) tmp_asst INNER JOIN contract c ON tmp_asst.contract_id = c.id  LEFT JOIN contract_account ca ON tmp_asst.contract_id = ca.contract_id AND ca.contract_account_type = 0 AND ca.thru_date = '2900-01-01 00:00:00' WHERE 1 = 1 ";
		Map<String, Object> params=new HashMap<>();
		List<String> financialContractUuids=new ArrayList<String>(){
			{
				add("1234");
				add("5678");
				add("1234");
				add("5678");
			}
		};
		params.put("financialContractUuids", financialContractUuids);
		params.put("repaymentPlanStartDate", "2017-12-11");
		params.put("repaymentPlanEndDate", "2017-12-11");
		params.put("actualRepaymentStartDate", "2017-12-11");
		params.put("actualRepaymentEndDate", "2017-12-11");
		params.put("singleLoanContractNo", "456231656");

		params.put("planStatus", 1);
		params.put("repaymentStatus", 1);
		params.put("repaymentType", 1);
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper3"), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testSql2(){
		String sqlTest="SELECT tmp_asst.*, c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, ca.payer_name AS customerName, ca.pay_ac_no AS payAcNo, ca.id_card_num AS idCardNum, ca.bank AS bank, ca.province AS province, ca.city AS city, (SELECT CONCAT_WS(\",\", SUM(CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END), SUM(CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END), SUM(CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END)) FROM asset_set_extra_charge asstec WHERE asstec.asset_set_uuid = tmp_asst.asset_uuid) AS chargeArray FROM ( SELECT asset_uuid, contract_id, financial_contract_uuid AS financialContractUuid, single_loan_contract_no AS repaymentPlanNo, asset_recycle_date AS assetRecycleDate, actual_recycle_date AS actualRecycleDate, asset_principal_value AS planPrincipal, asset_interest_value AS planInterest FROM asset_set WHERE financial_contract_uuid IN (:financialContractUuids)  AND single_loan_contract_no = :singleLoanContractNo   AND asset_recycle_date >=:repaymentPlanStartDate   AND asset_recycle_date <=:repaymentPlanEndDate   AND date(actual_recycle_date) >=:actualRepaymentStartDate   AND date(actual_recycle_date) <=:actualRepaymentEndDate    AND plan_type = 1 AND active_status = 0 AND can_be_rollbacked = 0     AND time_interval IN (1,2) AND executing_status = 0     AND repayment_plan_type = 1 AND contract_funding_status = 1 AND asset_status = 1   ) tmp_asst INNER JOIN contract c ON tmp_asst.contract_id = c.id  AND c.contract_no =:contractNo  LEFT JOIN contract_account ca ON tmp_asst.contract_id = ca.contract_id AND ca.contract_account_type = 0 AND ca.thru_date = '2900-01-01 00:00:00' WHERE 1 = 1  AND ca.payer_name =:customerName ";
		Map<String, Object> params=new HashMap<>();
		List<String> financialContractUuids=new ArrayList<String>(){
			{
				add("1234");
				add("5678");
				add("1234");
				add("5678");
			}
		};
		params.put("financialContractUuids", financialContractUuids);
		params.put("repaymentPlanStartDate", "2017-12-11");
		params.put("repaymentPlanEndDate", "2017-12-11");
		params.put("actualRepaymentStartDate", "2017-12-11");
		params.put("actualRepaymentEndDate", "2017-12-11");
		params.put("singleLoanContractNo", "456231656");

		params.put("planStatus", 2);
		params.put("repaymentStatus", 1);
		params.put("repaymentType", 2);
		
		params.put("contractNo", 987654321);
		params.put("customerName", "zhanglongfei");
		
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper3"), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
	@SuppressWarnings("serial")
	@Test
	
	public void testSql3(){
		String sqlTest="SELECT tmp_asst.*, c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, ca.payer_name AS customerName, ca.pay_ac_no AS payAcNo, ca.id_card_num AS idCardNum, ca.bank AS bank, ca.province AS province, ca.city AS city, (SELECT CONCAT_WS(\",\", SUM(CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END), SUM(CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END), SUM(CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END)) FROM asset_set_extra_charge asstec WHERE asstec.asset_set_uuid = tmp_asst.asset_uuid) AS chargeArray FROM ( SELECT asset_uuid, contract_id, financial_contract_uuid AS financialContractUuid, single_loan_contract_no AS repaymentPlanNo, asset_recycle_date AS assetRecycleDate, actual_recycle_date AS actualRecycleDate, asset_principal_value AS planPrincipal, asset_interest_value AS planInterest FROM asset_set WHERE financial_contract_uuid IN (:financialContractUuids)  AND single_loan_contract_no = :singleLoanContractNo   AND asset_recycle_date >=:repaymentPlanStartDate   AND asset_recycle_date <=:repaymentPlanEndDate   AND date(actual_recycle_date) >=:actualRepaymentStartDate   AND date(actual_recycle_date) <=:actualRepaymentEndDate    AND active_status = 2     AND executing_status = 1     AND repayment_plan_type = 2 AND asset_status = 1   ) tmp_asst INNER JOIN contract c ON tmp_asst.contract_id = c.id  AND c.contract_no =:contractNo  LEFT JOIN contract_account ca ON tmp_asst.contract_id = ca.contract_id AND ca.contract_account_type = 0 AND ca.thru_date = '2900-01-01 00:00:00' WHERE 1 = 1  AND ca.payer_name =:customerName ";
		Map<String, Object> params=new HashMap<>();
		List<String> financialContractUuids=new ArrayList<String>(){
			{
				add("1234");
				add("5678");
				add("1234");
				add("5678");
			}
		};
		params.put("financialContractUuids", financialContractUuids);
		params.put("repaymentPlanStartDate", "2017-12-11");
		params.put("repaymentPlanEndDate", "2017-12-11");
		params.put("actualRepaymentStartDate", "2017-12-11");
		params.put("actualRepaymentEndDate", "2017-12-11");
		params.put("singleLoanContractNo", "456231656");

		params.put("planStatus", 3);
		params.put("repaymentStatus", 2);
		params.put("repaymentType", 3);
		
		
		params.put("contractNo", 987654321);
		params.put("customerName", "zhanglongfei");
		
		
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper3"), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
		
		
	}
	
}
