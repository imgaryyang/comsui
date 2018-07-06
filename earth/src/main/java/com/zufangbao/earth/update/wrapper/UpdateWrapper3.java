package com.zufangbao.earth.update.wrapper;

import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_FILE_MAPPER;

import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.ChartOfAccount;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerBookHandler;
import com.zufangbao.sun.ledgerbook.LedgerTradeParty;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanExtraChargeHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wq on 17-4-26.
 */
@Component
public class UpdateWrapper3 extends IUpdateBaseWraper implements IUpdateWrapper<UpdateWrapperModel> {

	private final String assetSet = "assetSet";
	private final String ledgerBookShelf = "ledgerBookShelf";
	private final String updateAssetSet1 = "updateAssetSet1";
	private final String updateAssetSet2 = "updateAssetSet2";
	private final String updateAssetSetExtraCharge = "updateAssetSetExtraCharge";
	private final String insertLedgerBookShelf="insertLedgerBookShelf";

	@SuppressWarnings("unchecked")
	private static Map<Integer, Object> getValues = new HashedMap() {
		{
			put(0, ChartOfAccount.SND_UNEARNED_LOAN_ASSET_INTEREST);
			put(1, ChartOfAccount.SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE);
			put(2, ChartOfAccount.SND_UNEARNED_LOAN_ASSET_TECH_FEE);
			put(3, ChartOfAccount.SND_UNEARNED_LOAN_ASSET_OTHER_FEE);
		}
	};

	@Autowired
	private RepaymentPlanExtraChargeHandler repaymentPlanExtraChargeHandler;

	@Autowired
	private LedgerBookHandler anMeiTuLedgerBookHandler;
	@Autowired
	private UpdateSqlCacheManager updateSqlCacheManager;

	@SuppressWarnings("unchecked")
	@Override

	public String wrap(UpdateWrapperModel paramsBean) throws Exception {
		StringBuffer montageSql = new StringBuffer();
		Map<String, Object> sqlMap = updateSqlCacheManager.getSqlParam().get(UPDATE_CODES_WRAPPER_FILE_MAPPER.get("3"));
		Map<String, Object> slSqlMap = this.getParamMap(paramsBean);
		BigDecimal asset_fair_value = new BigDecimal(0);
		
		// 从模板中获取update语句
		String updateSql1 = (String) sqlMap.get(updateAssetSet1);
		String updateSql2 = (String) sqlMap.get(updateAssetSet2);
		String updateSql3 = (String) sqlMap.get(updateAssetSetExtraCharge);
		String insertSql = (String) sqlMap.get(insertLedgerBookShelf);

		slSqlMap.put("singleLoanContractNo", null==paramsBean.getSingleLoanContractNo()?null:Arrays.asList(paramsBean.getSingleLoanContractNo().split(",")));
		String[] values = ((String) slSqlMap.get("valueAndFees")).split(",");
		
		// 从模板中获取查询语句进行填充和执行
		List<Map<String, Object>> param = this.getSql(assetSet, slSqlMap, sqlMap);
		if (null == param || 0 == param.size()) {
			return null;
		}

		List<Model> modelList = new ArrayList<>();
		List<String> assetUuid = this.getModelAndAssetUuid(modelList, param);
		slSqlMap.put("assetUuid", assetUuid.toString());
		
		// 从模板中获取查询语句进行填充和执行
		List<Map<String, Object>> param2 = this.getSql(ledgerBookShelf, new HashedMap(){{put("assetUuid",assetUuid);}}, sqlMap);
		if (null == param2 || 0 == param2.size()) {
			return null;
		}
		String ledgerBookNo = (String) param2.get(0).get("ledgerBookNo");
		String ledgerBookOwnerId = (String) param2.get(0).get("ledgerBookOwnerId");
		String firstPartyId = (String) param2.get(0).get("firstPartyId");
		String singo = slSqlMap.get("singleLoanContractNo").toString();
		slSqlMap.put("singleLoanContractNo", singo.replace("[","'").replace("]","'").replaceAll(",","','").trim());

		// insert1
		montageSql.append(FreemarkerUtil.process(updateSql1, slSqlMap));

		//insert3
		Map<String, BigDecimal> account_amount_map = new HashedMap();
		for (int i = 0; i < values.length; i++) {
			if (StringUtils.isNotEmpty(values[i])) {
				account_amount_map.put((String) getValues.get(i), new BigDecimal(values[i]));
				if(i==0){
					slSqlMap.put("assetInterestValue", (String)values[i]);
					continue;
				}
				slSqlMap.put("accountAmount", values[i]);
				slSqlMap.put("secondAccountName", (String) getValues.get(i));
				montageSql.append(FreemarkerUtil.process(updateSql3, slSqlMap));
			}
		}
		

		//insert2
		Map<String, Map<String, BigDecimal>> valueAndFees = repaymentPlanExtraChargeHandler
				.getAssetSetsExtraChargesMapBy(assetUuid);
		Map<String, Object> insert2Param = new HashedMap();
		for (String uuid : assetUuid) {
			Map<String, BigDecimal> fees = valueAndFees.get(uuid);
			for (Map.Entry<String, BigDecimal> map : fees.entrySet()) {
				if (account_amount_map.containsKey(map.getKey())) {
					asset_fair_value = asset_fair_value.add(account_amount_map.get(map.getKey()));
					continue;
				}
				asset_fair_value = asset_fair_value.add(map.getValue());
			}
			insert2Param.put("assetUuid", uuid);
			insert2Param.put("assetFairValue", asset_fair_value);
			montageSql.append(FreemarkerUtil.process(updateSql2, insert2Param));
		}

		
		//insert4
		LedgerBook book = new LedgerBook(ledgerBookNo, ledgerBookOwnerId);// (ledgerBookNo)
		for (Model model : modelList) {
			AssetCategory assetCategory = createAssetGategory(model);
			LedgerTradeParty ledgerTradeParty = new LedgerTradeParty(firstPartyId, model.getCustomerUuid());
			List<Map<String, Object>> paramList = anMeiTuLedgerBookHandler.get_refresh_receivable_mutable_fee(book, assetCategory,
					ledgerTradeParty, account_amount_map);
				for(Map<String, Object> params: paramList){
					montageSql.append(FreemarkerUtil.process(insertSql, params));
				}
			}
		return montageSql.toString();

	}

	
	
	
	
	
	
	private List<String> getModelAndAssetUuid(List<Model> modelList, List<Map<String, Object>> param) {
		List<String> assetUuid = new ArrayList<>();
		for (Map<String, Object> map : param) {
			Model model = new Model();
			model.setDefault_date(null==map.get("defaultDate")?null:map.get("defaultDate").toString());
			model.setRelated_lv_1_asset_outer_idenity((String) map.get("relatedLv1AssetOuterIdenity"));
			model.setContract_id( map.get("contractId").toString());
			model.setContract_uuid((String) map.get("contractUuid"));
			model.setRelated_lv_1_asset_uuid((String) map.get("relatedLv1AssetUuid"));
			model.setCustomerUuid((String) map.get("customerUuid"));
			modelList.add(model);
			assetUuid.add((String) map.get("asset_uuid"));
		}
		return assetUuid;
	}

	public static class Model {
		private String default_date;
		private String related_lv_1_asset_outer_idenity;
		private String contract_id;
		private String contract_uuid;
		private String related_lv_1_asset_uuid;
		private String customerUuid;

		public String getDefault_date() {
			return default_date;
		}

		public String getRelated_lv_1_asset_outer_idenity() {
			return related_lv_1_asset_outer_idenity;
		}

		public String getContract_id() {
			return contract_id;
		}

		public String getContract_uuid() {
			return contract_uuid;
		}

		public String getRelated_lv_1_asset_uuid() {
			return related_lv_1_asset_uuid;
		}

		public void setDefault_date(String default_date) {
			this.default_date = default_date;
		}

		public void setRelated_lv_1_asset_outer_idenity(String related_lv_1_asset_outer_idenity) {
			this.related_lv_1_asset_outer_idenity = related_lv_1_asset_outer_idenity;
		}

		public void setContract_id(String contract_id) {
			this.contract_id = contract_id;
		}

		public void setContract_uuid(String contract_uuid) {
			this.contract_uuid = contract_uuid;
		}

		public void setRelated_lv_1_asset_uuid(String related_lv_1_asset_uuid) {
			this.related_lv_1_asset_uuid = related_lv_1_asset_uuid;
		}

		public String getCustomerUuid() {
			return customerUuid;
		}

		public void setCustomerUuid(String customerUuid) {
			this.customerUuid = customerUuid;
		}
	}

	private AssetCategory createAssetGategory(Model set) {
		Date date = DateUtils.parseDate(set.getDefault_date(), "yyyy-MM-dd HH:mm:ss");
		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setFstLvLAssetUuid(set.getRelated_lv_1_asset_uuid());
		assetCategory.setFstLvLAssetOuterIdentity(set.getRelated_lv_1_asset_outer_idenity());
		assetCategory.setDefaultDate(DateUtils.parseDate(DateUtils.format(date)));
		assetCategory.setAmortizedDate(date);
		assetCategory.setRelatedContractId(Long.parseLong(set.getContract_id()));
		assetCategory.setRelatedContractUuid(set.getContract_uuid());
		return assetCategory;
	}

}