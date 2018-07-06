package com.zufangbao.earth.update.wrapper;

import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_FILE_MAPPER;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountDetail;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.TAccountInfo;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UpdateWrapper2 extends IUpdateBaseWraper implements IUpdateWrapper<UpdateWrapperModel> {

	public final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private final String assetSet = "assetSet";
	private final String ledgerBookShelf = "ledgerBookShelf";
	private final String customer = "customer";
	private final String repurchaseDoc = "repurchaseDoc";

	
	private final String insertLedgerBookShelf1 = "insertLedgerBookShelf1";
	private final String insertLedgerBookShelf2 = "insertLedgerBookShelf2";
	private final String rentOrder = "rentOrder";
	private final String insertAssetSet = "insertAssetSet";
	
	@Autowired
	private UpdateSqlCacheManager updateSqlCacheManager;
	
	@Autowired
	private BankAccountCache bankAccountCache;

	@Autowired
	private FinancialContractService financialContractService;
	
	
	@Override
	public String wrap(UpdateWrapperModel paramsBean) throws Exception {
		try {
			StringBuffer montageSql = new StringBuffer();
			String batchSerialUuid1 = UUID.randomUUID().toString();
			String batchSerialUuid2 = UUID.randomUUID().toString();
			String orderNo = UUID.randomUUID().toString();
			String repaymentBillId = UUID.randomUUID().toString();
			String uuid1 = UUID.randomUUID().toString();
			String uuid2 = UUID.randomUUID().toString();
			
			Map<String, Object> sqlMap = updateSqlCacheManager.getSqlParam().get(UPDATE_CODES_WRAPPER_FILE_MAPPER.get("2"));
			Map<String, Object> slSqlMap = this.getParamMap(paramsBean);
			slSqlMap.put("batchserialuuid1", batchSerialUuid1);
			slSqlMap.put("batchserialuuid2", batchSerialUuid2);
			slSqlMap.put("orderNo", orderNo);
			slSqlMap.put("repaymentBillId", repaymentBillId);
			slSqlMap.put("uuid1", uuid1);
			slSqlMap.put("uuid2", uuid2);
			slSqlMap.put("dueDate", DateUtils.format(DateUtils.parseDate(paramsBean.getPayoutTime(), DATE_FORMAT)));
			
			//从模板中获取insert语句
			String insertSql1 = (String)sqlMap.get(insertLedgerBookShelf1);
			String insertSql2 = (String)sqlMap.get(insertLedgerBookShelf2);
			String insertSql3 = (String)sqlMap.get(rentOrder);
			String insertSql4 = (String)sqlMap.get(insertAssetSet);
			
			
			
			//从模板中获取查询语句进行填充和执行
			List<Map<String, Object>> param1 = getSql(assetSet, slSqlMap, sqlMap);
			if (null == param1 || 0 == param1.size()) {
				return null;
			}
			slSqlMap.putAll(param1.get(0));
			slSqlMap.put("assetRecycleDate", DateUtils.format((Date)slSqlMap.get("assetRecycleDate"), DATE_FORMAT));		
			
			
			//从模板中获取查询语句进行填充和执行
			List<Map<String, Object>> param2 = getSql(ledgerBookShelf, slSqlMap, sqlMap);
			if (null == param2 || 0 == param2.size()) {
				return null;
			}
			slSqlMap.putAll(param2.get(0));
			
			
			//从模板中获取查询语句并进行填充
			List<Map<String, Object>> param3 = getSql(customer, slSqlMap, sqlMap);
			if (null == param3 || 0 == param3.size()) {
				return null;
			}
			slSqlMap.putAll(param3.get(0));
			
			
			//从模板中获取查询语句并进行填充
			List<Map<String, Object>> param4 =  getSql(repurchaseDoc, slSqlMap, sqlMap);
			if (null == param4 || 0 == param4.size()) {
				return null;
			}
			slSqlMap.putAll(param4.get(0));
			
			
			RepurchaseAmountDetail amountDetail = getrepurchaseAmountDetail(slSqlMap, paramsBean);
			slSqlMap.put("amountDetail",JSON.toJSONString(amountDetail, SerializerFeature.DisableCircularReferenceDetect));
			slSqlMap.put("contractId", slSqlMap.get("contractId")==null? null:((Long)slSqlMap.get("contractId")).toString());
			slSqlMap.put("customerId", slSqlMap.get("customerId")==null?null:((Long)slSqlMap.get("customerId")).toString());
			slSqlMap.put("financialContractId", slSqlMap.get("financialContractId")==null?null:((Long)slSqlMap.get("financialContractId")).toString());
			slSqlMap.put("assetSetId", slSqlMap.get("assetSetId")==null?null:((Long)slSqlMap.get("assetSetId")).toString());
			slSqlMap.putAll(getNameAndCode((String)slSqlMap.get("financialContractUuid"), paramsBean.getExtraChargeSpecKey()));


			montageSql.append(FreemarkerUtil.process(insertSql1, slSqlMap));
			montageSql.append(FreemarkerUtil.process(insertSql2, slSqlMap));
			montageSql.append(FreemarkerUtil.process(insertSql3, slSqlMap));
			montageSql.append(FreemarkerUtil.process(insertSql4, slSqlMap));
			
			return montageSql.toString();

		} catch (Exception e) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, e.getMessage());
		}
	}
	
	
	private RepurchaseAmountDetail getrepurchaseAmountDetail(Map<String, Object> slSqlMap,UpdateWrapperModel paramsBean){
		RepurchaseAmountDetail amountDetail = JSON.parseObject((String)slSqlMap.get("amountDetail"), RepurchaseAmountDetail.class);
		if(null!=paramsBean.getRepurchaseInterest()){
		amountDetail.setRepurchaseInterest(new BigDecimal(paramsBean.getRepurchaseInterest()));
		}
		if(null!=paramsBean.getRepurchaseOtherCharges()){
		amountDetail.setRepurchaseOtherCharges(new BigDecimal(paramsBean.getRepurchaseOtherCharges()));
		}
		if(null!=paramsBean.getRepurchasePenalty()){
		amountDetail.setRepurchasePenalty(new BigDecimal(paramsBean.getRepurchasePenalty()));
		}
		if(null!=paramsBean.getRepurchasePrincipal()){
		amountDetail.setRepurchasePrincipal(new BigDecimal(paramsBean.getRepurchasePrincipal()));
		}
		return amountDetail;
	}
	private Map<String,Object> getNameAndCode(String financialContractUuid, String extraChargeSpec){
		FinancialContract financialContract = financialContractService.getFinancialContractBy(financialContractUuid);
		DepositeAccountInfo depositeAccountInfo = bankAccountCache.extractFirstBankAccountFrom(financialContract);
		TAccountInfo accountInfo = ChartOfAccount.EntryBook().get(depositeAccountInfo.getDeposite_account_name());

		TAccountInfo detailAccountInfo= ChartOfAccount.EntryBook().get(extraChargeSpec);

		String fstAccountName = detailAccountInfo.getFirstLevelAccount().getAccountName();
		String fstAccountCode = detailAccountInfo.getFirstLevelAccount().getAccountCode();

		String sndAccountName=accountInfo.getSecondLevelAccount()==null?ChartOfAccount.SND_BANK_SAVING_GENERAL:accountInfo.getSecondLevelAccount().getAccountName();
		String sndAccountCode=accountInfo.getSecondLevelAccount()==null?ChartOfAccount.SND_BANK_SAVING_GENERAL_CODE:accountInfo.getSecondLevelAccount().getAccountCode();

		String trdAccountName = detailAccountInfo.getThirdLevelAccount().getAccountName();
		String trdAccountCode = detailAccountInfo.getThirdLevelAccount().getAccountCode();
		Map<String,Object> accountMaps = new HashMap<String,Object>();
		accountMaps.put("pFstBankName", fstAccountName);
		accountMaps.put("pFstBankId", fstAccountCode);
		accountMaps.put("pSndBankName", sndAccountName);
		accountMaps.put("pSndBankId", sndAccountCode);
		accountMaps.put("pTrdBankName", trdAccountName);
		accountMaps.put("pTrdBankId", trdAccountCode);
		return accountMaps;
	}

	
}
