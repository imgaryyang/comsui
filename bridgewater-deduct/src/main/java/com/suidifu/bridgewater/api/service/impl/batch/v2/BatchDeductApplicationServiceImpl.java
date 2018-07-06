/**
 *
 */
package com.suidifu.bridgewater.api.service.impl.batch.v2;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchProcessStatus;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.NotifyStatus;
import com.suidifu.bridgewater.api.service.batch.v2.BatchDeductApplicationService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author wukai
 *
 */
@Service("batchDeductApplicationService")
public class BatchDeductApplicationServiceImpl extends
GenericServiceImpl<BatchDeductApplication> implements
		BatchDeductApplicationService {

	private static final Log logger = LogFactory.getLog(BatchDeductApplicationServiceImpl.class);

	@Override
	public boolean saveBatchDeductInfo(BatchDeductApplication batchDeductApplication) {
		try {
			Map<String, Object> parms = new HashMap<String, Object>();
			parms.put("batchDeductApplicationUuid", batchDeductApplication.getBatchDeductApplicationUuid());
			parms.put("batchDeductId", batchDeductApplication.getBatchDeductId());
			parms.put("requestNo", batchDeductApplication.getRequestNo());
			parms.put("filePath", batchDeductApplication.getFilePath());
			parms.put("notifyUrl", batchDeductApplication.getNotifyUrl());
			parms.put("batchProcessStatus", BatchProcessStatus.Created.ordinal());
			parms.put("batchNotifyStatus", NotifyStatus.Waiting.ordinal());
			parms.put("createTime", new Date());
			parms.put("lastModifiedTime", new Date());

			String sqlStr = "insert into t_batch_deduct_application "
					+ "(batch_deduct_application_uuid, batch_deduct_id, request_no, file_path, notify_url, batch_process_status, batch_notify_status, create_time, last_modified_time) "
					+ "values (:batchDeductApplicationUuid, :batchDeductId, :requestNo, :filePath, :notifyUrl, :batchProcessStatus, :batchNotifyStatus, :createTime, :lastModifiedTime)";

			genericDaoSupport.executeSQL(sqlStr, parms);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("saveBatchDeductInfo occur error!...");
			return false;
		}
	}

	@Override
	public List<BatchDeductApplication> loadWaitingProcessBatchDeductApplications() {
		return genericDaoSupport.searchForList("FROM BatchDeductApplication WHERE batchProcessStatus =:batchProcessStatus", "batchProcessStatus", BatchProcessStatus.Created);
	}

	@Override
	public void updateBatchDeductApplicationStatus(Long id,
			BatchProcessStatus batchProcessStatus,
			NotifyStatus batchNotifyStatus, Integer totalCount,
			Integer failCount, String erroMsg) {

		String updateSql = "UPDATE t_batch_deduct_application SET %s WHERE id=:id";

		Map<String,Object> parameter = new HashMap<String,Object>();

		parameter.put("id", id);

		List<String> setFiledSqlList = new ArrayList<String>();

		if(null != batchProcessStatus ){

			setFiledSqlList.add("batch_Process_Status=:batchProcessStatus");

			parameter.put("batchProcessStatus", batchProcessStatus.ordinal());
		}

		if(null != batchNotifyStatus){

			setFiledSqlList.add("batch_Notify_Status=:batchNotifyStatus");

			parameter.put("batchNotifyStatus", batchNotifyStatus.ordinal());
		}

		if(null != totalCount){

			setFiledSqlList.add("total_Count=:totalCount");

			parameter.put("totalCount", totalCount+"");
		}

		if(null != failCount){

			setFiledSqlList.add("fail_Count=:failCount");

			parameter.put("failCount", failCount+"");
		}

		if(StringUtils.isNotBlank(erroMsg)){

			setFiledSqlList.add("fail_Msg=:erroMsg");

			parameter.put("erroMsg", erroMsg+"");
		}

		if(CollectionUtils.isNotEmpty(setFiledSqlList)){

			setFiledSqlList.add("last_Modified_Time=:lastModifiedTime");

			parameter.put("lastModifiedTime", new Date());

			this.genericDaoSupport.executeSQL(String.format(updateSql, StringUtils.join(setFiledSqlList, ",")), parameter);
		}
	}

	@Override
	public List<BatchDeductApplication> getNeedToNotifyCounterBatchDeductApplications() {

		String sentence = "FROM BatchDeductApplication WHERE batchProcessStatus=:batchProcessStatus AND batchNotifyStatus=:batchNotifyStatus";

		Map<String,Object> parameters = new HashMap<String,Object>();

		parameters.put("batchProcessStatus", BatchProcessStatus.Done);
		parameters.put("batchNotifyStatus", NotifyStatus.Waiting);

		return this.genericDaoSupport.searchForList(sentence, parameters);
	}

	@Override
	public BatchDeductApplication getBatchDeductApplicationBy(
			String batchDeductApplicationUuid) {

		if (StringUtils.isEmpty(batchDeductApplicationUuid)) {

			return null;
		}

		String sentence = "FROM BatchDeductApplication WHERE batchDeductApplicationUuid=:batchDeductApplicationUuid";

		Map<String,Object> parameters = new HashMap<String,Object>();

		parameters.put("batchDeductApplicationUuid", batchDeductApplicationUuid);

		List<BatchDeductApplication> result = this.genericDaoSupport.searchForList(sentence, parameters);

		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}

	@Override
    public void increaseBatchDeductApplicationErrorCount(Long id) {

		String updateSql = "UPDATE t_batch_deduct_application SET fail_Count=fail_Count+1 WHERE id=:id";

		this.genericDaoSupport.executeSQL(updateSql, "id", id);

	}

	@Override
	public void updateBatchDeductApplicationByUuid(String batchDeductApplicationUuid, int actualCount) {

		if (StringUtils.isEmpty(batchDeductApplicationUuid)) {

			return;

		}

		String hql = "UPDATE BatchDeductApplication set actualCount =(actualCount+:actualCount) where batchDeductApplicationUuid =:batchDeductApplicationUuid";

		Map<String, Object> params = new HashMap<>();

		params.put("actualCount",actualCount );

		params.put("batchDeductApplicationUuid",batchDeductApplicationUuid );

		this.genericDaoSupport.executeHQL(hql, params);

	}

	@Override
	public String getCurrentAccessVersion(String batchDeductApplicationUuid) {
		List<String> resultList = genericDaoSupport.queryForSingleColumnList("SELECT version FROM t_batch_deduct_application WHERE batch_deduct_application_uuid =:batchDeductApplicationUuid",
				"batchDeductApplicationUuid", batchDeductApplicationUuid, String.class);

		if (CollectionUtils.isEmpty(resultList)) {
			return StringUtils.EMPTY;
		}
		return resultList.get(0);
	}
//	public void updateBatchDeductApplicationByUuid(String batchDeductApplicationUuid) {
//
//		if (StringUtils.isEmpty(batchDeductApplicationUuid)) {
//
//			return;
//
//		}
//
//		String hql = "UPDATE BatchDeductApplication set actualCount =(total where batchDeductApplicationUuid =:batchDeductApplicationUuid";
//
//		Map<String, Object> params = new HashMap<>();
//
//		params.put("actualCount",actualCount );
//
//		params.put("batchDeductApplicationUuid",batchDeductApplicationUuid );
//
//		this.genericDaoSupport.executeHQL(hql, params);
//
//	}

}
