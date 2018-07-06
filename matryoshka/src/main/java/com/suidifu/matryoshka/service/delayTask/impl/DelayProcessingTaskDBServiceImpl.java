package com.suidifu.matryoshka.service.delayTask.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.delayTask.DelayProcessingTaskLog;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.exception.SunException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * Created by louguanyang on 2017/5/3.
 */
@Service("delayProcessingTaskDBService")
public class DelayProcessingTaskDBServiceImpl implements DelayProcessingTaskService {
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final Log LOGGER = LogFactory.getLog(DelayProcessingTaskDBServiceImpl.class);

    @Autowired private GenericDaoSupport genericDaoSupport;

    @Override
    public void saveOrUpdate(DelayProcessingTask task) throws SunException {
        try {
            genericDaoSupport.saveOrUpdate(task);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SunException("save or update by db exception " + e.getMessage());
        }
    }

    @Override
    public void delete(String delayProcessingTaskUuid) throws SunException {
        try {
            if (StringUtils.isEmpty(delayProcessingTaskUuid)) {
                LOGGER.warn("delayProcessingTaskUuid is empty !");
                return;
            }
            String hql = "DELETE FROM DelayProcessingTask WHERE uuid=:uuid";
            genericDaoSupport.executeHQL(hql, "uuid", delayProcessingTaskUuid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SunException("delete by db exception " + e.getMessage());
        }
    }



    @Override
    public DelayProcessingTask getByUuid(String delayProcessingTaskUuid) {
        if (StringUtils.isEmpty(delayProcessingTaskUuid)) {
            return null;
        }
        String hql = "from DelayProcessingTask where uuid=:uuid";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("uuid", delayProcessingTaskUuid);
        List<DelayProcessingTask> taskList = this.genericDaoSupport.searchForList(hql, parameters);
        if(CollectionUtils.isEmpty(taskList)){
            return null;
        }
        return taskList.get(0);
    }

    @Override
    public List<DelayProcessingTask> get_by_repaymentPlanUuid(String repaymentPlanUuid) {
        if (StringUtils.isEmpty(repaymentPlanUuid)) {
            return null;
        }
        String hql = "from DelayProcessingTask where repaymentPlanUuid=:repaymentPlanUuid";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("repaymentPlanUuid", repaymentPlanUuid);
        List<DelayProcessingTask> taskList = this.genericDaoSupport.searchForList(hql, parameters);
        if(CollectionUtils.isEmpty(taskList)){
            return Collections.emptyList();
        }
        return taskList;
    }

    @Override
    public List<DelayProcessingTask> get_by_repurchaseDocUuid(String repurchaseDocUuid) {
        if (StringUtils.isEmpty(repurchaseDocUuid)) {
            return null;
        }
        String hql = "from DelayProcessingTask where repurchaseDocUuid=:repurchaseDocUuid";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("repurchaseDocUuid", repurchaseDocUuid);
        List<DelayProcessingTask> taskList = this.genericDaoSupport.searchForList(hql, parameters);
        if(CollectionUtils.isEmpty(taskList)){
            return Collections.emptyList();
        }
        return taskList;
    }


    public List<DelayProcessingTask> getByConfigUuid(String configUuid, String financialContractUuid){
        String hql = "from DelayProcessingTask where configUuid=:configUuid and taskExecuteDate <= :time and executeStatus =:executeStatus and financialContractUuid=:financialContractUuid";
        Map<String, Object> map = new HashMap<>();
        map.put("configUuid",configUuid);
        map.put("time", DateUtils.getToday());
        map.put("executeStatus",0);
        map.put("financialContractUuid",financialContractUuid);
        List<DelayProcessingTask> tasks = this.genericDaoSupport.searchForList(hql, map);
        return tasks;
    }

    public void updateByTask(DelayProcessingTask task) throws Exception{
        try {
            genericDaoSupport.saveOrUpdate(task);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SunException("save or update by db exception " + e.getMessage());
        }
    }

    public List<DelayProcessingTaskLog> getDelayProcessingTaskLogByConfigUuid(){
        String hql = "select * from t_delay_processing_task where task_execute_date <= :time and execute_status =:executeStatus";
        Map<String, Object> map = new HashMap<>();
        map.put("time", DateUtils.getToday());
        map.put("executeStatus",1);
        List<DelayProcessingTaskLog> tasks = this.genericDaoSupport.queryForList(hql, map, DelayProcessingTaskLog.class);
        return tasks;
    }

    public int saveDelayProcessingTaskLog(List<DelayProcessingTaskLog> logs) throws Exception{
        if(logs.size() == 0){
            return 0;
        }
        StringBuffer hql = new StringBuffer("insert into t_delay_processing_task_log(uuid,config_uuid,repayment_plan_uuid,task_execute_date," +
                "work_params,execute_status,create_time,last_modify_time,financial_contract_uuid,customer_uuid,contract_uuid)" +
                "values (?,?,?,?,?,?,?,?,?,?,?)");
        try {
            int[] cnt = this.namedParameterJdbcTemplate.getJdbcOperations().batchUpdate(hql.toString(), new BQUpdateBatchPreparedStatementSetter(logs));
            return cnt.length;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SunException("save or update by db exception " + e.getMessage());
        }
    }

    public void delDelayProcessingTask(List<DelayProcessingTaskLog> logs) throws Exception{
        if(logs.size() == 0){
            return ;
        }
        StringBuffer hql = new StringBuffer("delete from t_delay_processing_task where uuid in (");
        for(DelayProcessingTaskLog taskLog : logs){
            hql.append("'"+taskLog.getUuid()+"',");
        }
        String sql = hql.substring(0,hql.length()-1) + " ) and execute_status = 1";
        Map<String, Object> map = new HashMap<>();
        this.genericDaoSupport.executeSQL(sql, map);
    }

    private class BQUpdateBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
        final List temList;

        public BQUpdateBatchPreparedStatementSetter(List list){
            temList = list;
        }
        public int getBatchSize() {
            return temList.size();
        }

        public void setValues(PreparedStatement ps, int i)
                throws SQLException {
            Date now = new Date();
            DelayProcessingTaskLog delayProcessingTaskLog = (DelayProcessingTaskLog)temList.get(i);
            ps.setString(1, delayProcessingTaskLog.getUuid());
            ps.setString(2, delayProcessingTaskLog.getConfigUuid());
            ps.setString(3, delayProcessingTaskLog.getRepaymentPlanUuid());
            ps.setString(4, DateUtils.format(delayProcessingTaskLog.getTaskExecuteDate(),DateUtils.LONG_DATE_FORMAT));
            ps.setString(5, delayProcessingTaskLog.getWorkParams());
            ps.setInt(6, delayProcessingTaskLog.getExecuteStatus());
            ps.setString(7, DateUtils.format(now,DateUtils.LONG_DATE_FORMAT));
            ps.setString(8, DateUtils.format(now,DateUtils.LONG_DATE_FORMAT));
            ps.setString(9, delayProcessingTaskLog.getFinancialContractUuid());
            ps.setString(10, delayProcessingTaskLog.getCustomerUuid());
            ps.setString(11, delayProcessingTaskLog.getContractUuid());
        }
    }

    private class BQDeteleBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
        final List temList;

        public BQDeteleBatchPreparedStatementSetter(List list){
            temList = list;
        }
        public int getBatchSize() {
            return temList.size();
        }

        public void setValues(PreparedStatement ps, int i)
                throws SQLException {
            DelayProcessingTaskLog delayProcessingTaskLog = (DelayProcessingTaskLog)temList.get(i);
            ps.setString(1, delayProcessingTaskLog.getUuid());
        }
    }

    public List<DelayProcessingTask> getDelayProcessingTaskByFinancialContract(String financialContractUuid){
        String hql = "from DelayProcessingTask where taskExecuteDate <= :time and executeStatus =:executeStatus and financialContractUuid=:financialContractUuid";
        Map<String, Object> map = new HashMap<>();
        map.put("time", com.demo2do.core.utils.DateUtils.addDays(DateUtils.getToday(),1));
        map.put("executeStatus",0);
        map.put("financialContractUuid",financialContractUuid);
        List<DelayProcessingTask> tasks = this.genericDaoSupport.searchForList(hql, map);
        return tasks;
    }
}
