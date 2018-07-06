/**
 * 
 */
package com.zufangbao.earth.report.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.*;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentRecordRepaymentExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentRecordRepurchaseExcelVO;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentRecordQueryModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;


@Component
public class ReportWrapper21 extends ReportBaseWrapper implements IReportWrapper<RepaymentRecordQueryModel>  {
	private static final String YYYY_MM_DD = "yyyy-MM-dd";
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	@Autowired
	private RepaymentOrderService repaymentorderservice;
	

	@Override
	public ExportEventLogModel wrap(RepaymentRecordQueryModel paramsBean,
			HttpServletRequest request, HttpServletResponse response,
			ExportEventLogModel exportEventLogModel,Principal principal) throws Exception {
		if(CollectionUtils.isEmpty(paramsBean.getFinancialContractUuidList())) {
			return exportEventLogModel;
		}
		
		Map<String, Object> params = buildParams(paramsBean);
		String repaymentdetailsql = getCachedSql("reportWrapper21-repaymentRecord", params);
		String repurchasedetailsql = getCachedSql("reportWrapper21-repurchaseRecord", params);
		exportEventLogModel.recordStartLoadDataTime();
		//根据信托UUid名称查询出信托项目
		String fileName = String.format("文件导出_还款记录_%s.zip", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		
		//组建还款记录
		PrintWriter repaymentdetail_printWriter = putNextZipEntry(zip, "还款明细");
		
		ExportableRowCallBackHandler<RepaymentRecordRepaymentExcelVO> repaymentDetailcallBack = new ExportableRowCallBackHandler<RepaymentRecordRepaymentExcelVO>(RepaymentRecordRepaymentExcelVO.class, repaymentdetail_printWriter, new ReportVOBuilder<RepaymentRecordRepaymentExcelVO>() {
			@Override
			public RepaymentRecordRepaymentExcelVO buildRow(ResultSet rs) throws SQLException {
				return buildRepaymentOrderRepaymentDetailsExportStyle(rs);
			}
		});
		genericDaoSupport.query(repaymentdetailsql, params, repaymentDetailcallBack);
		
		//组建回购记录
		PrintWriter repurchasedetail_printWriter = putNextZipEntry(zip, "回购明细");
		ExportableRowCallBackHandler<RepaymentRecordRepurchaseExcelVO> repurchaseDetailcallBack = new ExportableRowCallBackHandler<RepaymentRecordRepurchaseExcelVO>(RepaymentRecordRepurchaseExcelVO.class, repurchasedetail_printWriter, new ReportVOBuilder<RepaymentRecordRepurchaseExcelVO>() {
			@Override
			public RepaymentRecordRepurchaseExcelVO buildRow(ResultSet rs) throws SQLException {
				return buildRepaymentPlanDetailsExportStyle1(rs);
			}
		});
		genericDaoSupport.query(repurchasedetailsql, params, repurchaseDetailcallBack);
		
		closeZipOutputStream(zip, response);
		
		exportEventLogModel.recordAfterLoadDataComplete(repurchaseDetailcallBack);
		
		return exportEventLogModel;
	}
	
	private Map<String, Object> buildParams(RepaymentRecordQueryModel queryModel) {
		Map<String,Object> params=new HashMap<>();
		
		params.put("financialContractUuid", CollectionUtils.isEmpty(queryModel.getFinancialContractUuidList())?"":queryModel.getFinancialContractUuidList().get(0));
		params.put("repaymentPlanNo", queryModel.getRepaymentBusinessNo());
		params.put("contractNo", queryModel.getContractNo());
		params.put("repaymentsortField", queryModel.getSortField());
		
		params.put("startActualRecycleDate", queryModel.getStartActualRecycleDate());
		params.put("endActualRecycleDate", queryModel.getEndActualRecycleDate());
		
		params.put("startHappenDate", queryModel.getStartHappenDate());
		params.put("endHappenDate", queryModel.getEndHappenDate());
		//params.put("isAsc", queryModel.isAsc());
		return params;
	}
	
//	private String getRepaymentExecutionStateCode(ResultSet resultSet)
//			throws SQLException {
//		int executingStatus = resultSet.getInt("executingStatus");
//		if (executingStatus == ExecutingStatus.UNEXECUTED.ordinal()) {
//			if (resultSet.getInt("timeInterval") == TimeInterval.IMMATURE
//					.ordinal()) {
//				return "X00";
//			}
//			return "X10";
//		}
//
//		if (executingStatus == ExecutingStatus.PROCESSING.ordinal()) {
//			if (resultSet.getInt("deductionStatus") == DeductionStatus.LOCAL_PROCESSING.ordinal()
//					|| resultSet.getInt("deductionStatus") == DeductionStatus.OPPOSITE_PROCESSING.ordinal()) {
//				return "1X1";
//			}
//			return "0X1";
//		}
//
//		return "XX" + executingStatus;
//	}

	private String getRepaymentExecutionStateCode(int executingStatus,int timeInterval,int deductionStatus)
			throws SQLException {
		if (executingStatus == ExecutingStatus.UNEXECUTED.ordinal()) {
			if (timeInterval == TimeInterval.IMMATURE
					.ordinal()) {
				return "X00";
			}
			return "X10";
		}

		if (executingStatus == ExecutingStatus.PROCESSING.ordinal()) {
			if (deductionStatus == DeductionStatus.LOCAL_PROCESSING.ordinal()
					|| deductionStatus == DeductionStatus.OPPOSITE_PROCESSING.ordinal()) {
				return "1X1";
			}
			return "0X1";
		}

		return "XX" + executingStatus;
	}

	private <T extends RepaymentRecordRepaymentExcelVO> T buildRepaymentRecordRepayment(ResultSet rs, T vo){
		try {
			
			String assetGroup=rs.getString("asset");
			String[] assetArray = StringUtils.split(assetGroup, ",");

			if(ArrayUtils.isNotEmpty(assetArray)){
				String planDate = assetArray[0];
				String outerRepaymentPlanNo = assetArray[1];

				String executingStatus = assetArray[2];
				int executingStatusInt =  Integer.parseInt(executingStatus);

				String time_interval = assetArray[3];
				int timeIntervalInt = Integer.parseInt(time_interval);

				String deduction_status = assetArray[4];
				int deductionStatusInt = Integer.parseInt(deduction_status);

				vo.setPlanDate(planDate);
				vo.setRepayScheduleNo(outerRepaymentPlanNo);

				String repaymentStatusCode = getRepaymentExecutionStateCode(executingStatusInt, timeIntervalInt, deductionStatusInt);
				RepaymentExecutionState stateEnum = RepaymentExecutionStateMapUtil.getRepaymentExecutionStateMap().get(repaymentStatusCode);
				if(stateEnum != null) {
					vo.setRepaymentStatus(stateEnum.getChineseName());
				}
			}


			if(rs.getString("journalVoucherType").equals("7")){
				if(rs.getString("appendix") != null){
					Map<String,Object> appendixMap = parseAppendix(rs.getString("appendix"));
					String clearingTime = (String)appendixMap.get("clearing_time");
					vo.setAccountedDate(clearingTime);
				}
			}else {
				vo.setAccountedDate(rs.getString("actualRecyleDate"));
			}

			vo.setActualRecycleDate(rs.getString("actualRecyleDate"));
			vo.setConractUniqueid(rs.getString("uniqueid"));
			vo.setContractNo(rs.getString("contractNo"));
			vo.setHappenDate(rs.getString("happenDate"));
//			vo.setDelayDays(rs.getString("delayDays"));
			vo.setRepaymentPlanNo(rs.getString("repaymentPlanNo"));
			vo.setRepaymentWay(rs.getString("repaymentWay"));

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	private Map<String,Object> parseAppendix(String appendix){
		Map<String,Object> appendixMap = new HashMap<String,Object>();
		try {
			appendixMap = JSON.parseObject(appendix,new TypeReference<Map<String,Object>>() {});
		} catch(Exception e){
			
		}
		if(appendixMap==null){
			appendixMap = new HashMap<String,Object>();
		}
		return appendixMap;
	}
	
	private <T extends RepaymentRecordRepurchaseExcelVO> T buildRepaymentRecordRepurchase(ResultSet rs, T vo){
		try {
			
			String repurchaseGroup=rs.getString("repurch");
			String[] repurchaseArray = StringUtils.split(repurchaseGroup, ",");

			if(ArrayUtils.isNotEmpty(repurchaseArray)){

				String repo_start_date = repurchaseArray[0];
				String repo_end_date = repurchaseArray[1];
				String repurchase_status = repurchaseArray[2];
				int repurchase_status_int = Integer.parseInt(repurchase_status);

				vo.setEndDate(repo_end_date);
				vo.setStartDate(repo_start_date);

				RepurchaseStatus repurchaseStatus = EnumUtil.fromOrdinal(RepurchaseStatus.class,repurchase_status_int);
				vo.setRepurchaseStatus(repurchaseStatus==null?"":repurchaseStatus.getChineseName());
			}

			vo.setAccountedDate(rs.getString("accountedDate"));
			vo.setActualRecycleDate(rs.getString("actualRecyleDate"));
			vo.setConractUniqueid(rs.getString("uniqueid"));
			vo.setContractNo(rs.getString("contractNo"));
			vo.setHappenDate(rs.getString("happenDate"));
			vo.setRepaymentWay(rs.getString("repaymentWay"));
			vo.setRepurchaseUuid(rs.getString("repaymentPlanNo"));

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	//构建还款订单还款明细Model
	private RepaymentRecordRepaymentExcelVO buildRepaymentOrderRepaymentDetailsExportStyle(ResultSet rs) throws SQLException {
		RepaymentRecordRepaymentExcelVO vo = new RepaymentRecordRepaymentExcelVO();
		buildRepaymentRecordRepayment(rs,vo);
		vo.setTotalFee(rs.getString("bookingAmount"));
		//构造还款订单明细
		builderRepaymentFeeDetail(vo,rs);
		return vo;
	}
	//构建还款订单回购明细Model
	private RepaymentRecordRepurchaseExcelVO buildRepaymentPlanDetailsExportStyle1(ResultSet rs) throws SQLException {
		RepaymentRecordRepurchaseExcelVO vo =new RepaymentRecordRepurchaseExcelVO();
		buildRepaymentRecordRepurchase(rs,vo);
		vo.setTotalFee(rs.getString("bookingAmount"));
		//构造回购单明细
		builderRepurchaseFeeDetail(vo,rs);
		return vo;
	}
	
	private void builderRepaymentFeeDetail(RepaymentRecordRepaymentExcelVO vo,ResultSet rs){
		try {

			String AmountGroup=rs.getString("amount");
			String[] amountArray = StringUtils.split(AmountGroup, ",");

			if(ArrayUtils.isNotEmpty(amountArray)){

				vo.setLoanAssetPrincipal(amountArray[0]);
				vo.setLoanAssetInterest(amountArray[1]);
				vo.setLoanServiceFee(amountArray[2]);
				vo.setLoanTechFee(amountArray[3]);
				vo.setLoanOtherFee(amountArray[4]);
				vo.setOverdueFeePenalty(amountArray[5]);
				vo.setOverdueFeeObligation(amountArray[6]);
				vo.setOverdueFeeService(amountArray[7]);
				vo.setOverdueFeeOther(amountArray[8]);
			}

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void builderRepurchaseFeeDetail(RepaymentRecordRepurchaseExcelVO vo,ResultSet rs){
		try {

			String AmountGroup=rs.getString("amount");
			String[] amountArray = StringUtils.split(AmountGroup, ",");
			
			if(ArrayUtils.isNotEmpty(amountArray)){
				vo.setRepurchasePrincipal(amountArray[0]);
				vo.setRepurchaseInterest(amountArray[1]);
				vo.setRepurchasePenalty(amountArray[2]);
				vo.setRepurchasePenalty(amountArray[3]);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private Date getDate(String Date) {
		if (StringUtils.isEmpty(Date) || DateUtils.asDay(Date) == null) {
			return DateUtils.parseDate(DateUtils.today(), YYYY_MM_DD);
		}
		return DateUtils.parseDate(Date, YYYY_MM_DD);
	}
	



}
