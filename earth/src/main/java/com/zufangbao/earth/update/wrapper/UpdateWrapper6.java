package com.zufangbao.earth.update.wrapper;

import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_FILE_MAPPER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.sun.ledgerbook.stat.LedgerBookStatHandler;


/**
 * Created by wq on 17-5-10.
 */

@Component
public class UpdateWrapper6 extends IUpdateBaseWraper implements IUpdateWrapper<UpdateWrapperModel> {
    @Autowired
    private UpdateSqlCacheManager updateSqlCacheManager;
    @Autowired
    private LedgerBookStatHandler ledgerBookStatHandler;
    
    private final String updateRentOrder="updateRentOrder";
    private final String updateAssetSet1="updateAssetSet1";
    private final String updateAssetSet2="updateAssetSet2";
    private final String updateAssetSet3="updateAssetSet3";
    private final String updateDeductPlan="updateDeductPlan";
    private final String updateDeductApplication="updateDeductApplication";
    private final String updateJournalVoucher="updateJournalVoucher";
    private final String updateSourceDocument="updateSourceDocument";
    private final String updateSourceDocumentDetail="updateSourceDocumentDetail";
    private final String insertLedgerBookShelf="insertLedgerBookShelf";
    private final String journalVoucher="journalVoucher";
    private final String sourceDocument="sourceDocument";
    private final String deductPlan="deductPlan";
    private final String sourceDocumentDetail="sourceDocumentDetail";
    private final String deductApplication="deductApplication";
    private final String assetSet="assetSet";
    private final String rentOrder="rentOrder";
    private final String financialContract="financialContract";
    private final String updateContract = "updateContract";

    @Override
    public String wrap(UpdateWrapperModel paramsBean) throws Exception {
        StringBuffer montageSql = new StringBuffer();
        Map<String, Object> sqlMap = updateSqlCacheManager.getSqlParam().get(UPDATE_CODES_WRAPPER_FILE_MAPPER.get("6"));
        Map<String, Object> slSqlMap = this.getParamMap(paramsBean);

        // 从模板中获取update语句
        String updateSql1 = (String) sqlMap.get(updateJournalVoucher);
        String updateSql2 = (String) sqlMap.get(updateRentOrder);
        String updateSql3 = (String) sqlMap.get(updateAssetSet1);
        String updateSql4 = (String) sqlMap.get(updateAssetSet2);
        String updateSql5 = (String) sqlMap.get(updateAssetSet3);
        String updateSql6 = (String) sqlMap.get(updateSourceDocumentDetail);
        String updateSql7 = (String) sqlMap.get(updateSourceDocument);
        String updateSql8 = (String) sqlMap.get(updateDeductPlan);
        String updateSql9 = (String) sqlMap.get(updateDeductApplication);
        String insertSql10 = (String) sqlMap.get(insertLedgerBookShelf);
        String updateSql11 = (String) sqlMap.get(updateContract);
        
        List<String> deductPlanUuidList= handlingUploadListStr((String)slSqlMap.get("deductPlanUuid"));
		  slSqlMap.put("deductPlanUuidList", deductPlanUuidList);
		  slSqlMap.put("deductPlanUuidListStr", changeListToStr(deductPlanUuidList));
		//更新deductPlan
	     montageSql.append(FreemarkerUtil.process(updateSql8, slSqlMap));
        List<Map<String, Object>> param1 = this.getSql(deductPlan , slSqlMap, sqlMap);
        if (null == param1 || 0==param1.size()) {
            return null;
        }
        slSqlMap.put("param1Result", param1);
        
        List<String> deductApplicationUuidList =getInnerParamKeyListByslSqlMap(slSqlMap, "param1Result", "deductApplicationUuid");
        slSqlMap.put("deductApplicationUuidList", deductApplicationUuidList);
        slSqlMap.put("deductApplicationUuidListStr", changeListToStr(deductApplicationUuidList));
        //更新deductApplication
        montageSql.append(FreemarkerUtil.process(updateSql9, slSqlMap));
        
        List<Map<String, Object>> param2 = this.getSql(sourceDocument , slSqlMap, sqlMap);
        if (null == param2 || 0==param2.size()) {
            return null;
        }
        slSqlMap.put("param2Result", param2);
        List<String> sourceDocumentUuidList =getInnerParamKeyListByslSqlMap(slSqlMap, "param2Result", "sourceDocumentUuid");
        slSqlMap.put("sourceDocumentUuidList", sourceDocumentUuidList);
        slSqlMap.put("sourceDocumentUuidListStr", changeListToStr(sourceDocumentUuidList));
        //更新sourceDocument
        montageSql.append(FreemarkerUtil.process(updateSql7, slSqlMap));
        
        // 从模板中获取查询语句进行填充和执行
        	List<Map<String, Object>> sddList = this.getSql(sourceDocumentDetail , slSqlMap, sqlMap);
        	if(CollectionUtils.isEmpty(sddList)){
        		return null;
        	}
        	Map<String, Object> sddUuidMap = new HashedMap();
        	Map<String,Object> updateParam=new HashedMap();
        	List<String> journalVoucherUuidList= new ArrayList<>();
        	List<String> orderNoList= new ArrayList<>();
        	List<String> contractUuidList = new ArrayList<>();
        	//循环处理sourceDocumentDetail
	        	for(Map<String, Object> sddMap:sddList){
	        		  String sddUUid=(String)sddMap.get("sourceDocumentDetailUuid");
	               sddUuidMap.put("sourceDocumentDetailUuid",sddUUid);
	               List<Map<String, Object>> param3 = this.getSql(journalVoucher , sddUuidMap, sqlMap);
	               if (null == param3 || 0==param3.size()) {
	            	   		continue;
	                 }
	               updateParam.putAll(param3.get(0));
	               journalVoucherUuidList.add((String)param3.get(0).get("journalVoucherUuid"));
	               orderNoList.add((String)param3.get(0).get("orderNo"));
	               String orderNo=(String)updateParam.get("orderNo");
	               List<Map<String, Object>> param4 = this.getSql(rentOrder , new HashedMap(){{put("orderNo",orderNo);}}, sqlMap);
	               if (null == param4 || 0==param4.size()) {
	            	   		continue;
	               }
	               int orderType=(int)param4.get(0).get("orderType");
	               String assetSetUuid=(String)param4.get(0).get("assetSetUuid");

	               List<Map<String, Object>> param7=this.getSql(assetSet ,updateParam, sqlMap);
	               if (null == param7 || 0==param7.size()) {
	                   continue;
	               }
	               contractUuidList.add((String)param7.get(0).get("contractUuid"));
	               String financialContractUuid=(String)param7.get(0).get("financialContractUuid");
	               List<Map<String, Object>> param5 =this.getSql(financialContract,new HashedMap(){{put("financialContractUuid",financialContractUuid);}},sqlMap);
	               if (null == param5 || 0==param5.size()) {
	            	   		continue;
	               }
	               String ledgerBookNo=(String)param5.get(0).get("ledgerBookNo");
	               //实收金额
	               BigDecimal receiveAmount=ledgerBookStatHandler.get_banksaving_amount_of_asset( ledgerBookNo, (String)updateParam.get("assetUuid"));///////////
	               //回滚金额
	               BigDecimal amount =(BigDecimal)sddMap.get("amount");

	               HashedMap paramMap=new HashedMap();
	               paramMap.put("assetSetUuid",assetSetUuid);
	               paramMap.put("orderNo",orderNo);
	               paramMap.put("orderType",orderType);
	               
	               //账本导入脚本
	               String insertSql = selectAndCreateLedgerItems((String)updateParam.get("journalVoucherUuid"), ledgerBookNo, (String)updateParam.get("assetUuid"),insertSql10);
	               montageSql.append(insertSql);
	               
	               List<Map<String, Object>> param6=this.getSql("maxPayOutTime" ,paramMap, sqlMap);
	               if (null == param6 || 0==param6.size()) {
	                   continue;
	               }
	               Date actualRecycleDate=(Date)param6.get(0).get("maxPayOutTime");
	               Date confirmRecycleDate=(Date)param7.get(0).get("confirmRecycleDate");

	               updateParam.put("actualRecycleDate",actualRecycleDate);
	               updateParam.put("confirmRecycleDate",confirmRecycleDate);
	               
	               //判断参数，更新assetSet
	               if(orderType==0){
	                   if(receiveAmount.compareTo(amount)==0){//未核销
	                       if(confirmRecycleDate==null){
	                           confirmRecycleDate=actualRecycleDate;
	                       }
	                       montageSql.append(FreemarkerUtil.process(updateSql3, updateParam));

	                   }else if(amount.compareTo(receiveAmount)==-1){// 核销中
	                       montageSql.append(FreemarkerUtil.process(updateSql4, updateParam));
	                   	}
	               }else if(orderType==1){
	                   montageSql.append(FreemarkerUtil.process(updateSql5, updateParam));
	               }
	               //更新sourceDocumentDetail
	               montageSql.append(FreemarkerUtil.process(updateSql6, sddUuidMap));
	        }
	       String journalVoucherUuidListStr = changeListToStr(journalVoucherUuidList);
	       String orderNoListStr = changeListToStr(orderNoList);
	       String contractUuidListStr = changeListToStr(contractUuidList);
	       updateParam.put("orderNoListStr", orderNoListStr);
	       updateParam.put("journalVoucherUuidListStr", journalVoucherUuidListStr);
	       updateParam.put("contractUuidListStr", contractUuidListStr);
		    //updateJV
		    montageSql.append(FreemarkerUtil.process(updateSql1, updateParam));
		    //updateRentOrder
		    montageSql.append(FreemarkerUtil.process(updateSql2, updateParam));
		    //updateContract
		    montageSql.append(FreemarkerUtil.process(updateSql11, updateParam));
	       return montageSql.toString();
        
    }
    
}
