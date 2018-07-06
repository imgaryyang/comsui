package com.zufangbao.earth.api.test.post;

import com.demo2do.core.persistence.support.Filter;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationDetailService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplicationDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
//
		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml" })

@Transactional()
public class CommandBatchDeductStatusQueryApiPost extends BaseApiTestPost{
	@Autowired
	DeductApplicationService deductApplicationService;
	@Autowired
	DeductApplicationDetailService deductApplicationDetailService;
	
	
	@Test
	public void batchDedcutStatusApiTestPost(){
		List<DeductApplication> deductApplicationList = deductApplicationService.list(DeductApplication.class,new Filter());
		deductApplicationList = deductApplicationList.stream().limit(5000).collect(Collectors.toList());
		List<String> deductIdList =  deductApplicationList.stream().map(f->f.getDeductId()).collect(Collectors.toList());
		String ja  = JsonUtils.toJSONString(deductIdList);
		
		
		List<String> codeList = deductApplicationDetailService.loadAll(DeductApplicationDetail.class).stream().limit(5000).map(hb -> hb.getRepaymentPlanCode()).collect(Collectors.toList());
		Map<String,String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100006");
		requestParams.put("requestNo", UUID.randomUUID().toString());
	//	requestParams.put("deductIdList",ja);
		requestParams.put("repaymentPlanCodeList",JsonUtils.toJSONString(codeList));
		requestParams.put("repaymentType","0");
		try {
			long start = System.currentTimeMillis();
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
		    long end = System.currentTimeMillis();
		    System.out.println(sr);
		    System.out.println("=============扣款状态查询时间============"+(end-start)+"[ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	@Test
	public void batchDedcutStatusApiTestPost1(){
		
		
		Map<String,String> requestParams = new HashMap<String, String>();
		requestParams.put("fn", "100006");
		requestParams.put("requestNo", UUID.randomUUID().toString());
		requestParams.put("deductIdList","[\"1b5588cb-4f4a-478a-897b-c6a658b971cb\"]");
		requestParams.put("repaymentPlanCodeList",null);
		requestParams.put("repaymentType","0");
		try {
			long start = System.currentTimeMillis();
			String sr = PostTestUtil.sendPost(QUERY_URL_TEST, requestParams, getIdentityInfoMap(requestParams));
		    System.out.println(sr);
			long end = System.currentTimeMillis();
		    System.out.println("=============扣款状态查询时间============"+(end-start)+"[ms]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void insertData(){
		String sql = "";
		for(int i= 1000;i<6500;i++){
			        sql+="INSERT INTO `t_deduct_application` ( `deduct_application_uuid`, `deduct_id`, `request_no`, `financial_contract_uuid`, `financial_product_code`, `contract_unique_id`, `repayment_plan_code_list`, `contract_no`, `planned_deduct_total_amount`, `actual_deduct_total_amount`, `notify_url`, `transcation_type`, `repayment_type`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `ip`, `last_modified_time`, `record_status`, `is_available`, `api_called_time`, `transaction_recipient`, `customer_name`) VALUES ('"+i+"', '"+i+"', '"+i+"', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'G31700', 'ee05da81-3b06-497b-a672-1c0a96fbcdd1', '[\"ZC274FC7CA6FDA0F0F\"]', '云信信2016-236-DK(22571906)号', '185.26', '185.26', '', '1', '0', '2', '', '2016-10-20 13:53:37', 'yntrust', '10.10.2.28', '2016-10-20 13:54:02', '2', '0', '2016-10-20 00:00:00', '1', '何玉轩');"+
		               "INSERT INTO `t_deduct_application_detail` (`deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`) VALUES ('"+i+"', '"+i+"', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'ee05da81-3b06-497b-a672-1c0a96fbcdd1', 'ZC274FC7CA6FDA0F0F', '033a4fa0-725f-40c7-9289-81d2ea33c83d', '"+i+"', '0', '1', '2016-10-20 13:53:37', '', 'yntrust', '2016-10-20 13:53:37', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE', '20000.01.01', '159.66', '1');"+
				       "INSERT INTO `t_deduct_application_detail` ( `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`) VALUES ('"+i+"', '"+i+"', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'ee05da81-3b06-497b-a672-1c0a96fbcdd1', 'ZC274FC7CA6FDA0F0F', '033a4fa0-725f-40c7-9289-81d2ea33c83d', '"+i+"', '0', '1', '2016-10-20 13:53:37', '', 'yntrust', '2016-10-20 13:53:37', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST', '20000.01.02', '25.60', '1');"+
				       "INSERT INTO `t_deduct_application_detail` ( `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`) VALUES ('"+i+"', '"+i+"', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'ee05da81-3b06-497b-a672-1c0a96fbcdd1', 'ZC274FC7CA6FDA0F0F', '033a4fa0-725f-40c7-9289-81d2ea33c83d', '"+i+"', '0', '1', '2016-10-20 13:53:37', '', 'yntrust', '2016-10-20 13:53:37', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE', '20000.01.04', '0.00', '1');"+
				       "INSERT INTO `t_deduct_application_detail` ( `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`) VALUES ('"+i+"', '"+i+"', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'ee05da81-3b06-497b-a672-1c0a96fbcdd1', 'ZC274FC7CA6FDA0F0F', '033a4fa0-725f-40c7-9289-81d2ea33c83d', '"+i+"', '0', '1', '2016-10-20 13:53:37', '', 'yntrust', '2016-10-20 13:53:37', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE', '20000.01.03', '0.00', '1');"+
				       "INSERT INTO `t_deduct_application_detail` ( `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`) VALUES ('"+i+"', '"+i+"', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'ee05da81-3b06-497b-a672-1c0a96fbcdd1', 'ZC274FC7CA6FDA0F0F', '033a4fa0-725f-40c7-9289-81d2ea33c83d', '"+i+"', '0', '1', '2016-10-20 13:53:37', '', 'yntrust', '2016-10-20 13:53:37', 'FST_RECIEVABLE_ASSET', '20000', 'SND_RECIEVABLE_LOAN_ASSET', '20000.01', 'TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE', '20000.01.05', '0.00', '1');"+
				       "INSERT INTO `t_deduct_application_detail` ( `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`) VALUES ('"+i+"', '"+i+"', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'ee05da81-3b06-497b-a672-1c0a96fbcdd1', 'ZC274FC7CA6FDA0F0F', '033a4fa0-725f-40c7-9289-81d2ea33c83d', '"+i+"', '0', '1', '2016-10-20 13:53:37', '', 'yntrust', '2016-10-20 13:53:37', 'TOTAL_OVERDUE_FEE', NULL, NULL, NULL, NULL, NULL, '0.00', '1');"+
				       "INSERT INTO `t_deduct_application_detail` ( `deduct_application_detail_uuid`, `deduct_application_uuid`, `financial_contract_uuid`, `contract_unique_id`, `repayment_plan_code`, `asset_set_uuid`, `request_no`, `repayment_type`, `transaction_type`, `create_time`, `execution_remark`, `creator_name`, `last_modify_time`, `first_account_name`, `first_account_uuid`, `second_account_name`, `second_account_uuid`, `third_account_name`, `third_account_uuid`, `account_amount`, `is_total`) VALUES ('"+i+"', '"+i+"', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'ee05da81-3b06-497b-a672-1c0a96fbcdd1', 'ZC274FC7CA6FDA0F0F', '033a4fa0-725f-40c7-9289-81d2ea33c83d', '"+i+"', '0', '1', '2016-10-20 13:53:37', '', 'yntrust', '2016-10-20 13:53:37', 'TOTAL_RECEIVABEL_AMOUNT', NULL, NULL, NULL, NULL, NULL, '185.26', '0');"+
				       "INSERT INTO `t_deduct_plan` ( `deduct_plan_uuid`, `deduct_application_uuid`, `deduct_application_detail_uuid`, `financial_contract_uuid`, `contract_unique_id`, `contract_no`, `payment_gateway`, `payment_channel_uuid`, `pg_account`, `pg_clearing_account`, `transaction_type`, `transaction_remark`, `cp_bank_code`, `cp_bank_card_no`, `cp_bank_account_holder`, `cp_id_type`, `cp_id_number`, `cp_bank_province`, `cp_bank_city`, `cp_bank_name`, `planned_payment_date`, `complete_payment_date`, `planned_total_amount`, `actual_total_amount`, `execution_precond`, `execution_status`, `execution_remark`, `create_time`, `creator_name`, `last_modified_time`, `transaction_serial_no`, `repayment_type`) VALUES ('"+i+"', '"+i+"', '"+i+"', '4eac7451-8808-4e3f-8101-2abaddcfd14b', 'ee05da81-3b06-497b-a672-1c0a96fbcdd1', '云信信2016-236-DK(22571906)号', NULL, 'f1ccca57-7c80-4429-b226-8ad31a729609', NULL, NULL, '1', NULL, 'C10305', '6226225503220152', '何玉轩', '0', '370303199606224827', '370000', '370300', '中国民生银行', NULL, '2016-10-20 13:44:26', '185.26', '185.26', NULL, '2', '处理成功', '2016-10-20 13:53:37', 'yntrust', '2016-10-20 13:54:02', NULL, NULL);";
		}
		
		 System.out.println("===============");
		File file  =  new File("test.sql");
		try {
			FileUtils.writeStringToFile(file, sql);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

