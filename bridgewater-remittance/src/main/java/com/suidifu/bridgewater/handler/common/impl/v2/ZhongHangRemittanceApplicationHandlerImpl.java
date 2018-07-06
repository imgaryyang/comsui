package com.suidifu.bridgewater.handler.common.impl.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.bridgewater.handler.v2.ZhongHangRemittanceApplicationHandler;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_remittance_function_point;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationContentValue;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.yunxin.entity.Dictionary;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.exception.DictionaryNotExsitException;
import com.zufangbao.sun.yunxin.service.DictionaryService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component("zhongHangRemittanceApplicationHandler")
public class ZhongHangRemittanceApplicationHandlerImpl implements ZhongHangRemittanceApplicationHandler {

	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;

	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;

	@Autowired
	private DictionaryService dictionaryService;

	@Value("#{config['zhonghang.merId']}")
	private String YX_API_MERID;

	private static final Log logger = LogFactory.getLog(ZhongHangRemittanceApplicationHandlerImpl.class);
	
	/**
	 * 查询放款结果（批量回调通知用）
	 * 结构：
	 * paidDetails : [
	 * 		{
	 * 			"DetailNo" : "", //明细编号
	 * 			"Status" : "", //状态 0:未回调，1:成功，2:失败，3：根据业务不执行，4:异常
	 * 			"Result" : "", //放款结果
	 * 			"BankSerialNo" : "", //银行流水号列表
	 * 			"ActExcutedTime" : "", //实际完成时间
	 * 		}
	 * ]
	 */
	@Override
	public String getRemittanceResultsForBatchNotice(RemittanceSqlModel remittanceSqlMode, String remittanceApplicationUuid) {
		if(StringUtils.isEmpty(remittanceApplicationUuid)) {
			return StringUtils.EMPTY;
		}
		
		String sql ="SELECT "
				+ "`remittance_application_uuid` AS 'RemmittanceApplicationUuid', "
				+ "`business_record_no` AS 'DetailNo', "
				+ "CASE execution_status "
				+ "WHEN 2 THEN 1 "
				+ "WHEN 3 THEN 2 "
				+ "WHEN 4 THEN 4 "
				+ "WHEN 5 THEN 3 "
				+ "END AS 'Status',"
				+ "`execution_remark` AS 'Result', "
				+ "("
				+ " SELECT"
				+ "   GROUP_CONCAT(trp.transaction_serial_no) "
				+ " FROM "
				+ "   `t_remittance_plan` trp "
				+ " WHERE "
				+ "   trp.remittance_application_detail_uuid = trad.remittance_application_detail_uuid "
				+ "   AND trp.execution_status =:executionStatus "
				+ "   AND trp.transaction_type =:transactionType "
				+ ") AS 'BankSerialNo', "
				+ "`complete_payment_date` AS 'ActExcutedTime' "
				+ "FROM "
				+ "`t_remittance_application_detail` trad "
				+ "WHERE "
				+ "trad.remittance_application_uuid = :remittanceApplicationUuid";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("executionStatus", ExecutionStatus.SUCCESS.ordinal());
		params.put("transactionType", com.zufangbao.sun.yunxin.entity.remittance.AccountSide.CREDIT.ordinal());
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		
		List<Map<String, Object>> allPaidDetails = this.genericDaoSupport.queryForList(sql, params);
		
		//以放款申请uuid分组，放款明细
		Map<String, List<Map<String, Object>>> groupedPaidDetails = allPaidDetails.stream().collect(Collectors.groupingBy(map -> (String) map.get
				("RemmittanceApplicationUuid")));

		Map<String, Object> paidNoticInfos = new HashMap<String, Object>();
			if(groupedPaidDetails.containsKey(remittanceApplicationUuid)) {
				paidNoticInfos.put("UniqueId", remittanceSqlMode.getContractUniqueId());
				paidNoticInfos.put("ReferenceId", remittanceSqlMode.getRequestNo());
				//剔除多余字段RemmittanceApplicationUuid
				List<Map<String, Object>> paidDetails = groupedPaidDetails.get(remittanceApplicationUuid);
				for (Map<String, Object> paidDetail : paidDetails) {
					paidDetail.remove("RemmittanceApplicationUuid");
				}
				paidNoticInfos.put("PaidDetails", paidDetails);
			}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("requestId", UUID.randomUUID().toString());
		result.put("referenceId", remittanceSqlMode.getRequestNo());
		result.put("orderNo", remittanceSqlMode.getRemittanceId());
		result.put("amount", remittanceSqlMode.getActualTotalAmount());
		result.put("status", remittanceSqlMode.getExecutionStatus());
		result.put("comment", remittanceSqlMode.getExecutionRemark());
		
		result.put("paidNoticInfos", paidNoticInfos);
		
		return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
	}
	
	private void updateRemittanceExecuteIfFailed(int actualNotifyNumber,int planNotifyNumber,String oppositeKeyWord, String remittanceApplicationUuid){

		this.genericDaoSupport.executeSQL(
				"UPDATE t_remittance_application "
						+ "SET actual_notify_number = (actual_notify_number + 1) "
						+ "WHERE remittance_application_uuid = :remittanceApplicationUuid", "remittanceApplicationUuid", remittanceApplicationUuid);
		logger.info(
            GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec
                .RawData("处理除了响应成功的情况,如响应超时,异常,响应失败等等情况"));
		logger.info(
            GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec
                .RawData("当前plan_notify_number:["+ planNotifyNumber +"]"));
		logger.info(
            GloableLogSpec.AuditLogHeaderSpec() + bridgewater_remittance_function_point.SEND_RESULT_TO_OUTLIER_SYSTEM + oppositeKeyWord+ GloableLogSpec
                .RawData("自增后actual_notify_number:["+ (actualNotifyNumber + 1) +"]操作"));


	}

	public Map<String, String> buildHeaderParamsForNotifyRemittanceResult(
			String content,String financialContractUuid){
		Map<String, String> headerParams = new HashMap<String, String>();
		headerParams.put("Content-Type", "application/json");
		headerParams.put("merId", YX_API_MERID);
		
		String context = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,
            FinancialContractConfigurationCode.ALLOW_SIGN.getCode());
		
		if (StringUtils.equals(context, FinancialContractConfigurationContentValue.SIGN)) {
		Dictionary dictionary;
		try {
			dictionary = dictionaryService.getDictionaryByCode(DictionaryCode.PLATFORM_PRI_KEY.getCode());
		} catch (DictionaryNotExsitException e) {
			logger.error(".#ZhongHangRemittanceApplicationHandlerImpl get private key fail");
			e.printStackTrace();
			return headerParams;
		}
		String signData =  ApiSignUtils.rsaSign(content, dictionary.getContent());
		headerParams.put("sign", signData);
		}
		
		return headerParams;
	}
	
}
