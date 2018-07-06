/**
 * 
 */
package com.zufangbao.earth.update.wrapper;

import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_FILE_MAPPER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;
import com.zufangbao.sun.utils.StringUtils;

/**
 *线下支付单作废
 */
@Component
public class UpdateWrapper7 extends IUpdateBaseWraper implements IUpdateWrapper<UpdateWrapperModel> {
	@Autowired
	private UpdateSqlCacheManager updateSqlCacheManager;
	@Autowired
	private LedgerBookStatHandler ledgerBookStatHandler;
	//select sql
	private final String offlineBill = "offlineBill";
	private final String sourceDocument = "sourceDocument";
	private final String journalVoucher = "journalVoucher";
	private final String rentOrderAndAssetSet = "rentOrderAndAssetSet";
	private final String financialContract = "financialContract";
	private final String rentOrder = "rentOrder";
	private final String contract = "contract";
	//update or insert sql
	private final String updateOfflineBill = "updateOfflineBill";
	private final String updateSourceDocument = "updateSourceDocument";
	private final String updateJournalVoucher = "updateJournalVoucher";
	private final String updateRentOrder = "updateRentOrder";
	private final String updateAssetSet1 = "updateAssetSet1";
	private final String updateAssetSet2 = "updateAssetSet2";
	private final String updateGuarantee1 = "updateGuarantee1";
	private final String updateContract = "updateContract";
	private final String insertLedgerBookShelf = "insertLedgerBookShelf";
	
	private String updateOfflineBillXmlSql = "";
	private String updateSourceDocumentXmlSql = "";
	private String updateJournalVoucherXmlSql = "";
	private String updateRentOrderXmlSql = "";
	private String updateAssetSet1XmlSql = "";
	private String updateAssetSet2XmlSql = "";
	private String updateGuarantee1XmlSql = "";
	private String updateContractXmlSql = "";
	private String insertLedgerBookShelfXmlSql = "";
	
	@Override
	public String wrap(UpdateWrapperModel paramsBean) throws Exception {
		//组建参数
		StringBuffer montageSql = new StringBuffer();
		Map<String, Object> xmlSqlMap = updateSqlCacheManager.getSqlParam().get(UPDATE_CODES_WRAPPER_FILE_MAPPER.get("7"));
		Map<String, Object> selectSqlMap = this.getParamMap(paramsBean);
		
		updateOfflineBillXmlSql = (String)xmlSqlMap.get(updateOfflineBill);
		updateSourceDocumentXmlSql = (String)xmlSqlMap.get(updateSourceDocument);
		updateJournalVoucherXmlSql = (String)xmlSqlMap.get(updateJournalVoucher);
		updateRentOrderXmlSql = (String)xmlSqlMap.get(updateRentOrder);
		updateAssetSet1XmlSql = (String)xmlSqlMap.get(updateAssetSet1);
		updateAssetSet2XmlSql = (String)xmlSqlMap.get(updateAssetSet2);
		updateGuarantee1XmlSql = (String)xmlSqlMap.get(updateGuarantee1);
		updateContractXmlSql = (String)xmlSqlMap.get(updateContract);
		insertLedgerBookShelfXmlSql = (String)xmlSqlMap.get(insertLedgerBookShelf);
		
		/**
		 * 组建线下支付单(offlineBill)更新语句
		 */
		String updateOfflineBillSql = createOfflineBillSql(selectSqlMap, xmlSqlMap);
		if(StringUtils.isEmpty(updateOfflineBillSql)){
			return montageSql.toString();
		}
		montageSql.append(updateOfflineBillSql);
		
		/**
		 * 组建sourceDocument更新语句
		 */
		String updateSourceDocumentSql = createSourceDocumentSql(selectSqlMap, xmlSqlMap);
		if(StringUtils.isEmpty(updateSourceDocumentSql)){
			return montageSql.toString();
		}
		montageSql.append(updateSourceDocumentSql);
		
		/**
		 * 组建journalVoucher更新语句
		 */
		String updateJournalVoucherSql = createjournalVoucherSql(selectSqlMap, xmlSqlMap);
		if(StringUtils.isEmpty(updateJournalVoucherSql)){
			return montageSql.toString();
		}
		montageSql.append(updateJournalVoucherSql);
		
		//获取 AssetSet,rentOrder,journalVoucherUuid　组合参数
		List<Map<String, Object>> groupParamList=this.getSql(rentOrderAndAssetSet, selectSqlMap, xmlSqlMap);
		if(CollectionUtils.isEmpty(groupParamList)){
			return montageSql.toString();
		}
		selectSqlMap.put("groupParamList", groupParamList);
		/**
		 * 组建rentOrder更新语句
		 */
		String updateRentOrderSql = createRentOrderSql(selectSqlMap, xmlSqlMap);
		if(StringUtils.isEmpty(updateRentOrderSql)){
			return montageSql.toString();
		}
		montageSql.append(updateRentOrderSql);
		/**
		 * 组建AssetSet更新语句
		 */
		String updateAssetSetSql = createAssetSetSql(selectSqlMap, xmlSqlMap);
		if(StringUtils.isEmpty(updateAssetSetSql)){
			return montageSql.toString();
		}
		montageSql.append(updateAssetSetSql);
		/**
		 * 组建Contract更新语句
		 */
		String udpateContractSql = createContractSql(selectSqlMap, xmlSqlMap);
		if(StringUtils.isEmpty(udpateContractSql)){
			return montageSql.toString();
		}
		montageSql.append(udpateContractSql);
		/**
		 * 组建ledgerBookShelf
		 */
		String updateLedgerBookShelfSql = createLedgerBookShelfSql(selectSqlMap, xmlSqlMap);
		montageSql.append(updateLedgerBookShelfSql);
		
		return montageSql.toString();
	}
	
	//处理批量offline_bill
	private String createOfflineBillSql(Map<String, Object> selectSqlMap,Map<String, Object> xmlSqlMap) throws Exception{
		List<String> offlineBillNoList= handlingUploadListStr((String)selectSqlMap.get("offlineBillNo"));
		if(CollectionUtils.isEmpty(offlineBillNoList)){
			return null;
		}
		selectSqlMap.put("offlineBillNoList", offlineBillNoList);
		List<Map<String, Object>> offlineBillUuidList = this.getSql(offlineBill, selectSqlMap, xmlSqlMap);
		if(CollectionUtils.isEmpty(offlineBillUuidList)){
			return null;
		}
		selectSqlMap.put("offlineBillUuidList", offlineBillUuidList);
		//组建备注
		String remark=returnParamIsNullRemark(offlineBillUuidList, offlineBillNoList, "offlineBillNoResult", "offlineBillUuid", " -- offlineBillNo查询offlineBillUuid未查询到值的有:");
		List<String> innerOfflineBillUuidList = getInnerParamKeyListByslSqlMap(selectSqlMap, "offlineBillUuidList", "offlineBillUuid");
		selectSqlMap.put("innerOfflineBillUuidList", innerOfflineBillUuidList);
		String offlineBillParamStr=changeListToStr(innerOfflineBillUuidList);
		//拼接update语句
		Map<String,Object > updateParam = new HashMap<>();
		updateParam.put("offlineBillNo", offlineBillParamStr);
		return FreemarkerUtil.process(updateOfflineBillXmlSql, updateParam)+remark;
	}
	
	//处理批量source_document
	private String createSourceDocumentSql(Map<String, Object> selectSqlMap,Map<String, Object> xmlSqlMap) throws Exception{
		List<Map<String, Object>> sourceDocumentUuidList = this.getSql(sourceDocument, selectSqlMap, xmlSqlMap);
		if(CollectionUtils.isEmpty(sourceDocumentUuidList)){
			return null;
		}
		selectSqlMap.put("sourceDocumentUuidList", sourceDocumentUuidList);
		//组建备注
		String remark=returnParamIsNullRemark(sourceDocumentUuidList,(List<String>)selectSqlMap.get("innerOfflineBillUuidList") , "offlineBillUuidResult", "sourceDocumentUuid", " -- offlineBillUuid查询sourceDocumentUuid未查询到值的有:");
		List<String> innerSourceDocumentUuidList = getInnerParamKeyListByslSqlMap(selectSqlMap, "sourceDocumentUuidList", "sourceDocumentUuid");
		selectSqlMap.put("innerSourceDocumentUuidList", innerSourceDocumentUuidList);
		String sourceDocumentParamStr=changeListToStr(innerSourceDocumentUuidList);
		//拼接update语句
		Map<String,Object > updateParam = new HashMap<>();
		updateParam.put("sourceDocumentUuid", sourceDocumentParamStr);
		return FreemarkerUtil.process(updateSourceDocumentXmlSql, updateParam)+remark;
	}
	
	//处理批量journal_voucher
	private String createjournalVoucherSql(Map<String, Object> selectSqlMap,Map<String, Object> xmlSqlMap) throws Exception{
		List<Map<String, Object>> journalVoucherUuidList = this.getSql(journalVoucher, selectSqlMap, xmlSqlMap);
		if(CollectionUtils.isEmpty(journalVoucherUuidList)){
			return null;
		}
		selectSqlMap.put("journalVoucherUuidList", journalVoucherUuidList);
		//组建备注
		String remark=returnParamIsNullRemark(journalVoucherUuidList,(List<String>)selectSqlMap.get("innerSourceDocumentUuidList") , "sourceDocumentUuidResult", "journalVoucherUuid", " -- sourceDocumentUuid查询journalVoucherUuid未查询到值的有:");
		List<String> innerJournalVoucherUuidList = getInnerParamKeyListByslSqlMap(selectSqlMap, "journalVoucherUuidList", "journalVoucherUuid");
		selectSqlMap.put("innerJournalVoucherUuidList", innerJournalVoucherUuidList);
		String JournalVoucherParamStr=changeListToStr(innerJournalVoucherUuidList);
		//拼接update语句
		Map<String,Object > updateParam = new HashMap<>();
		updateParam.put("journalVoucherUuid", JournalVoucherParamStr);
		return FreemarkerUtil.process(updateJournalVoucherXmlSql, updateParam)+remark;
	}
	
	//处理批量rent_order
	private String createRentOrderSql(Map<String, Object> selectSqlMap ,Map<String, Object> xmlSqlMap) throws Exception{
		List<String> journalVoucherUuidList = getInnerParamKeyListByslSqlMap(selectSqlMap, "groupParamList", "journalVoucherUuid");
		//组建备注
		String remark=returnParamIsNullRemark((List<Map<String, Object>>)selectSqlMap.get("groupParamList") ,journalVoucherUuidList , "journalVoucherUuid", "orderNo", " -- journalVoucherUuid查询orderNo未查询到值的有:");
		List<String> orderNoList = getInnerParamKeyListByslSqlMap(selectSqlMap, "groupParamList", "orderNo");
		String orderNoParamStr=changeListToStr(orderNoList);
		//拼接update语句
		Map<String,Object > updateParam = new HashMap<>();
		updateParam.put("orderNo", orderNoParamStr);
		return FreemarkerUtil.process(updateRentOrderXmlSql, updateParam)+remark;
	};
	
	//批量处理AssetSet
	private String createAssetSetSql(Map<String, Object> selectSqlMap ,Map<String, Object> xmlSqlMap) throws Exception{
		List<String> journalVoucherUuidList = getInnerParamKeyListByslSqlMap(selectSqlMap, "groupParamList", "journalVoucherUuid");
		List<Map<String, Object>> groupParamList= (List<Map<String, Object>>) selectSqlMap.get("groupParamList");
		//组建备注
		String remark=returnParamIsNullRemark(groupParamList ,journalVoucherUuidList , "journalVoucherUuid", "assetUuid"," -- journalVoucherUuid查询assetUuid未查询到值的有:");
		List<String> innerAssetUuidList = getInnerParamKeyListByslSqlMap(selectSqlMap, "groupParamList", "assetUuid");
		selectSqlMap.put("innerAssetUuidList", innerAssetUuidList);
		StringBuffer assetSetBuff= new StringBuffer();
		List<String> assetUuidRecordList = new ArrayList<>();
		Map<String,BigDecimal> assetUuidAndAmountMap= createAssetUuidAndAmountMap(groupParamList);
		//单次处理AssetSet
		for(Map<String, Object> groupParam:groupParamList){
			if(assetUuidRecordList.contains((String)groupParam.get("assetUuid"))){
				continue;
			}
			assetUuidRecordList.add((String)groupParam.get("assetUuid"));
			//查询ledgerBookNo
			selectSqlMap.put("financialContractUuid", (String)groupParam.get("financialContractUuid"));
			List<Map<String, Object>> ledgerBookNoList= this.getSql(financialContract, selectSqlMap, xmlSqlMap);
			//查询rentOrder
			selectSqlMap.put("orderNo", (String)groupParam.get("orderNo"));
			List<Map<String, Object>> rentOrderList= this.getSql(rentOrder, selectSqlMap, xmlSqlMap);
			if(CollectionUtils.isEmpty(ledgerBookNoList) || CollectionUtils.isEmpty(rentOrderList)){
				continue;
			}
			String ledgerBookNo = (String) ledgerBookNoList.get(0).get("ledgerBookNo");
			int orderType = (int)rentOrderList.get(0).get("orderType");
			
			//实收金额
            BigDecimal receiveAmount=ledgerBookStatHandler.get_banksaving_amount_of_asset( ledgerBookNo, (String)groupParam.get("assetUuid"));///////////
            //回滚金额
            BigDecimal amount =assetUuidAndAmountMap.get((String)groupParam.get("assetUuid"));
           Map<String, Object> updateParam = new HashMap<>(); 
           updateParam.put("assetUuid", (String)groupParam.get("assetUuid"));
            //判断参数，更新assetSet
            if(orderType==0){
                if(receiveAmount.compareTo(amount)==0){//未核销
                	assetSetBuff.append(FreemarkerUtil.process(updateAssetSet1XmlSql, updateParam));

                }else if(amount.compareTo(receiveAmount)==-1){// 核销中
                	assetSetBuff.append(FreemarkerUtil.process(updateAssetSet2XmlSql, updateParam));
                	}
            }else if(orderType==1){
            			assetSetBuff.append(FreemarkerUtil.process(updateGuarantee1XmlSql, updateParam));
            }
            //====单次结束
		}
		return assetSetBuff.toString()+remark;
	};
	
	//批量处理合同
	private String createContractSql(Map<String, Object> selectSqlMap ,Map<String, Object> xmlSqlMap) throws Exception{
			List<Map<String, Object>> contractUuidList = this.getSql(contract, selectSqlMap, xmlSqlMap);
			selectSqlMap.put("contractUuidList", contractUuidList);
			//组建备注
			String remark=returnParamIsNullRemark(contractUuidList ,(List<String>)selectSqlMap.get("innerAssetUuidList") , "assetUuid", "contractUuid", " -- assetUuid查询contractUuid未查询到值的有:");
			List<String> innerContractUuidList = getInnerParamKeyListByslSqlMap(selectSqlMap, "contractUuidList", "contractUuid");
			String contractUuidParamStr=changeListToStr(innerContractUuidList);
			Map<String,Object > updateParam = new HashMap<>();
			updateParam.put("contractUuid", contractUuidParamStr);
			return FreemarkerUtil.process(updateContractXmlSql, updateParam)+remark;
	}
	
	private String createLedgerBookShelfSql(Map<String, Object> selectSqlMap ,Map<String, Object> xmlSqlMap) throws Exception{
		List<Map<String, Object>> groupParamList= (List<Map<String, Object>>) selectSqlMap.get("groupParamList");
		StringBuffer ledgerBookShelfBuff= new StringBuffer();
		for(Map<String, Object> groupParam:groupParamList){
			//获取jvUuid
			String journalVoucherUuid = (String) groupParam.get("journalVoucherUuid");
			//获取ledgerbookNo
			String financialContractUuid = (String) groupParam.get("financialContractUuid");
			selectSqlMap.put("financialContractUuid", financialContractUuid);
			List<Map<String, Object>> ledgerBookNoList= this.getSql(financialContract, selectSqlMap, xmlSqlMap);
			//获取AssertSetUuid
			String assertSetUuid = (String) groupParam.get("AssetUuid");
			if(CollectionUtils.isEmpty(ledgerBookNoList) || StringUtils.isEmpty(assertSetUuid) || StringUtils.isEmpty(journalVoucherUuid)){
				continue;
			}
			String insertLedgerItemSql = selectAndCreateLedgerItems(journalVoucherUuid, (String)ledgerBookNoList.get(0).get("ledgerBookNo"), assertSetUuid, insertLedgerBookShelfXmlSql);
			ledgerBookShelfBuff.append(insertLedgerItemSql);
		}
		return ledgerBookShelfBuff.toString();
	}
	
	//组建还款计划Uuid,和金额的Map
	private Map<String, BigDecimal> createAssetUuidAndAmountMap(List<Map<String, Object>> groupParamList){
		Map<String, BigDecimal> AssetUuidAndAmountMap = new HashMap<>();
		for(Map<String, Object> groupParam:groupParamList){
			String assetUuid = (String)groupParam.get("assetUuid");
			BigDecimal amount = new BigDecimal("0.00");
			if(groupParam.get("amount") != null){
				amount = amount.add((BigDecimal) groupParam.get("amount"));
			};
			if(AssetUuidAndAmountMap.get(assetUuid)!= null ){
				BigDecimal allAmount=AssetUuidAndAmountMap.get(assetUuid);
				amount = amount.add(allAmount);
			}
				AssetUuidAndAmountMap.put(assetUuid, amount);	
		}
		return AssetUuidAndAmountMap;
	}
}
