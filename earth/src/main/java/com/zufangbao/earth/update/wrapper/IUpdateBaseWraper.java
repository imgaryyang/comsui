package com.zufangbao.earth.update.wrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.BeanUtils;
import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.sun.ledgerbook.AssetCategory;
import com.zufangbao.sun.ledgerbook.AssetConvertor;
import com.zufangbao.sun.ledgerbook.LedgerBook;
import com.zufangbao.sun.ledgerbook.LedgerItemService;
import com.zufangbao.sun.service.LedgerBookService;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;

public class IUpdateBaseWraper {

	@Autowired
	protected GenericDaoSupport genericDaoSupport;
	@Autowired
	private LedgerBookService ledgerBookService;
	@Autowired
	private RepaymentPlanService repaymentPlanService;
	@Autowired
	private LedgerItemService ledgerItemService;
	
	@SuppressWarnings("unused")
	public List<Map<String, Object>> getSql(String templateName,Map<String, Object> slSqlMap,Map<String, Object> sqlMap) throws Exception{
		String select = (String)sqlMap.get(templateName);
		String slSql = FreemarkerUtil.process(select, slSqlMap);
		return genericDaoSupport.queryForList(slSql, slSqlMap);
	}
	
	public Map<String, Object> getParamMap(UpdateWrapperModel model) throws IllegalAccessException, InvocationTargetException{
		Map<String,Object> map=BeanUtils.describe(model);
		return map;
	}
	
	//处理list,讲list转化为String
    public String changeListToStr(List<String> paramList){
    	if(CollectionUtils.isEmpty(paramList)){
    		return null;
    	}
    	StringBuffer strBuff = new StringBuffer("");
    	
    	for(String param : paramList){
    		if(StringUtils.isEmpty(param)){
    			continue;
    		}
    		strBuff.append("'").append(param).append("'").append(",");
    	}
    	if(strBuff.length() < 0){
    		return null;
    	}
    	return strBuff.substring(0, strBuff.length()-1);
    }
    
  //获取slSqlMap中某个参数的List
    public List<String> getInnerParamKeyListByslSqlMap(Map<String, Object> slSqlMap, String sqlMapKey,String innerParamKey){
    	List<Map<String, Object>> innerParamList= (List<Map<String, Object>>) slSqlMap.get(sqlMapKey);
    	if(innerParamList == null || innerParamList.size()==0){
    		return Collections.emptyList();
    	}
    	List<String> innerParamKeyList = new ArrayList<>();
    	for(Map<String, Object> innerParam:innerParamList){
    		String innerParamValue= (String)innerParam.get(innerParamKey);
    		innerParamKeyList.add(innerParamValue);
    	}
    	return innerParamKeyList;
    }
    
  //处理传入的List模型的String
    public List<String> handlingUploadListStr(String listStr){
    	if(StringUtils.isEmpty(listStr)){
    		return Collections.emptyList();
    	}
  	  	List<String> list= JsonUtils.parseArray(listStr, String.class);
       return list;
    }
    
    //组建账本冲销数据
    public String selectAndCreateLedgerItems(String journalVoucherUuid,String ledgerBookNo,String assetUuid,String insertSql10) throws Exception{
    	LedgerBook ledgerBook=ledgerBookService.getBookByBookNo(ledgerBookNo);
    	AssetSet myAssetSet=repaymentPlanService.getUniqueRepaymentPlanByUuid(assetUuid);
    	AssetCategory assetCategory=AssetConvertor.convertAnMeiTuLoanAssetToAssetCategory(myAssetSet);
    	List<Map<String, Object>> valuesMap = ledgerItemService.create_roll_back_ledgers_by_voucher_values(ledgerBook, assetCategory, journalVoucherUuid, "", "");
    	StringBuffer insertSql = new StringBuffer(); 
    	for (Map<String, Object> value : valuesMap){
    		insertSql.append(FreemarkerUtil.process(insertSql10,value));
    	}
    	return insertSql.toString();
    }
    
    //返回出为空的参数备注
    public String returnParamIsNullRemark(List<Map<String, Object>> selectResultList,List<String> selectParamList,String selectParamKey,String selectResultkey,String explain){
    	StringBuffer returnRemark = new StringBuffer(explain);
    	//组装一个map查询后，传入参数还剩多少存在
    	Map<String, String> selectParamMap= new HashMap<>();
    	for(Map<String, Object> result : selectResultList){
			String selectParam= (String)result.get(selectParamKey);
			String selectResult = (String)result.get(selectResultkey);
			selectParamMap.put(selectParam, selectResult);
		}
    	for(String param :selectParamList){
    		String result=selectParamMap.get(param);
    		if(StringUtils.isEmpty(result)){
    			returnRemark.append(param).append(" , ");
    		}
    	}
    	returnRemark.append("--  ").append("\n");
    	return returnRemark.toString();
    	
    }
}
