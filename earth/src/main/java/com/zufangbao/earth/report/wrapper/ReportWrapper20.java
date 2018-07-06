/**
 * 
 */
package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.repayment.order.*;
import com.zufangbao.sun.service.RepaymentOrderService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentOrderBaseItemExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentOrderRepaymentItemExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentOrderRepurchaseItemExcelVO;
import com.zufangbao.sun.yunxin.entity.model.repaymentOrder.RepaymentOrderQueryModel;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentWay;
import org.apache.commons.collections.CollectionUtils;
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

/**
 * @author hjl
 *
 */
@Component
public class ReportWrapper20 extends ReportBaseWrapper implements IReportWrapper<RepaymentOrderQueryModel>  {
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	@Autowired
	private RepaymentOrderService repaymentorderservice;
	
	@Override
	public ExportEventLogModel wrap(RepaymentOrderQueryModel paramsBean, HttpServletRequest request, HttpServletResponse response,
			ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {
		if(CollectionUtils.isEmpty(paramsBean.getFinancialContractUuidList())) {
			return exportEventLogModel;
		}
		if(StringUtils.isEmpty(paramsBean.getStartDateString())){
			paramsBean.setStartDateString(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		}
		Map<String, Object> params = buildParams(paramsBean);
		String repaymentdetailsql = getCachedSql("reportWrapper20-repaymentdetail", params);
		String repurchasedetailsql = getCachedSql("reportWrapper20-repurchasedetail", params);
		exportEventLogModel.recordStartLoadDataTime();
		//根据信托UUid名称查询出信托项目
		String fileName = String.format("文件导出_还款订单_%s.zip", DateUtils.format(new Date(), "yyyyMMddHHmmss"));
		ZipOutputStream zip = openZipOutputStream(response, fileName);
		
		//组建还款订单明细
		PrintWriter repaymentdetail_printWriter = putNextZipEntry(zip, "文件导出_还款订单_还款明细");
		
		ExportableRowCallBackHandler<RepaymentOrderRepaymentItemExcelVO> repaymentDetailcallBack = new ExportableRowCallBackHandler<RepaymentOrderRepaymentItemExcelVO>(RepaymentOrderRepaymentItemExcelVO.class, repaymentdetail_printWriter, new ReportVOBuilder<RepaymentOrderRepaymentItemExcelVO>() {
			@Override
			public RepaymentOrderRepaymentItemExcelVO buildRow(ResultSet rs) throws SQLException {
				return buildRepaymentOrderRepaymentDetailsExportStyle(rs);
			}
		});
		genericDaoSupport.query(repaymentdetailsql, params, repaymentDetailcallBack);
		
		//组建回购单明细
		PrintWriter repurchasedetail_printWriter = putNextZipEntry(zip, "文件导出_还款订单_回购明细");
		ExportableRowCallBackHandler<RepaymentOrderRepurchaseItemExcelVO> repurchaseDetailcallBack = new ExportableRowCallBackHandler<RepaymentOrderRepurchaseItemExcelVO>(RepaymentOrderRepurchaseItemExcelVO.class, repurchasedetail_printWriter, new ReportVOBuilder<RepaymentOrderRepurchaseItemExcelVO>() {
			@Override
			public RepaymentOrderRepurchaseItemExcelVO buildRow(ResultSet rs) throws SQLException {
				return buildRepaymentPlanDetailsExportStyle1(rs);
			}
		});
		genericDaoSupport.query(repurchasedetailsql, params, repurchaseDetailcallBack);
		
		closeZipOutputStream(zip, response);
		
		exportEventLogModel.recordAfterLoadDataComplete(repurchaseDetailcallBack);
		
		return exportEventLogModel;
	}
	
	private Map<String, Object> buildParams(RepaymentOrderQueryModel queryModel) {
		Map<String,Object> params=new HashMap<>();
		if(queryModel.getRepaymentStatus()!=-1){
		RepaymentStatus repaymentStatus=EnumUtil.fromOrdinal(RepaymentStatus.class,queryModel.getRepaymentStatus());
		repaymentorderservice.buildRepaymentStatusParams(params,repaymentStatus);
		}
		if(queryModel.getFirstRepaymentWayGroup()!=-1){
			params.put("repaymentWay",queryModel.getFirstRepaymentWayGroup());
		}
		if(queryModel.getSourceStatus()!=-1){
			params.put("repaymentsourceStatus", queryModel.getSourceStatus());
		}
		params.put("financialContractUuids", queryModel.getFinancialContractUuidList());
		params.put("repaymentOrderuuid", queryModel.getOrderUuid());
		params.put("repaymentOrderAmount", queryModel.getOrderAmount());
		params.put("repaymentfirstCustomerName", queryModel.getFirstCustomerName());
		params.put("repaymentorderUniqueId", queryModel.getOrderUniqueId());
		params.put("repaymentsortField", queryModel.getSortField());
		
		String time=DateUtils.format(queryModel.getStartDateValue(),DateUtils.DATE_FORMAT);
		String startdate=time+" 00:00:00";
		String enddate=time+" 23:59:59";
		params.put("repaymentOrderstartDate", startdate);
		params.put("repaymentOrderendDate", enddate);
		//params.put("isAsc", queryModel.isAsc());
		return params;
	}
	
	private <T extends RepaymentOrderBaseItemExcelVO> T buildRepaymentOrderPublicWay(ResultSet rs, T vo){
		try {
			vo.setOrderUuid(rs.getString("orderUuid"));
			vo.setOrderUniqueId(rs.getString("orderUniqueId"));
			vo.setFirstCustomerName(rs.getString("firstCustomerName"));
			vo.setOrderAmount(rs.getString("orderAmount"));
			//组建多个状态
			RepaymentOrder repaymentorder = new RepaymentOrder(EnumUtil.fromOrdinal(OrderAliveStatus.class,Integer.parseInt(rs.getString("orderAliveStatus"))),
																			EnumUtil.fromOrdinal(OrderCheckStatus.class,Integer.parseInt(rs.getString("orderCheckStatus"))),
																			EnumUtil.fromOrdinal(OrderPayStatus.class,Integer.parseInt(rs.getString("orderPayStatus"))),
																			EnumUtil.fromOrdinal(OrderRecoverStatus.class,Integer.parseInt(rs.getString("OrderRecoverStatus"))));
			String orderStatus=repaymentorder.transferToRepaymentStatus().getChineseName();
			vo.setOrderStatus(orderStatus);
			vo.setOrderCreateTime(rs.getString("orderCreateTime"));
			vo.setOrderLastModifiedTime(rs.getString("orderLastModifiedTime"));
			vo.setRepaymentBusinessNo(rs.getString("repaymentBusinessNo"));
			vo.setContractNo(rs.getString("contractNo"));
			vo.setRepaymentPlanTime(rs.getString("repaymentPlanTime"));
			vo.setRepaymentWay(EnumUtil.fromOrdinal(RepaymentWay.class,Integer.parseInt(rs.getString("repaymentWay"))).getChineseName());
			vo.setOrderCheckStatus(EnumUtil.fromOrdinal(OrderCheckStatus.class,OrderCheckStatus.VERIFICATION_SUCCESS.getOrdinal()).getChineseName());
			vo.setOrderRecoverStatus(EnumUtil.fromOrdinal(OrderRecoverStatus.class,Integer.parseInt(rs.getString("detailPayStatus"))).getChineseName());
			vo.setRemark(rs.getString("remark"));
			vo.setFinancialProduct(rs.getString("financialProduct"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vo;
	}
	//构建还款订单还款明细Model
	private RepaymentOrderRepaymentItemExcelVO buildRepaymentOrderRepaymentDetailsExportStyle(ResultSet rs) throws SQLException {
		RepaymentOrderRepaymentItemExcelVO vo = new RepaymentOrderRepaymentItemExcelVO();
		buildRepaymentOrderPublicWay(rs,vo);
		vo.setRepaymentAmount(rs.getString("repaymentAmount"));
		//构造还款订单明细
		builderRepaymentFeeDetail(vo,rs);
		return vo;
	}
	//构建还款订单回购明细Model
	private RepaymentOrderRepurchaseItemExcelVO buildRepaymentPlanDetailsExportStyle1(ResultSet rs) throws SQLException {
		RepaymentOrderRepurchaseItemExcelVO vo =new RepaymentOrderRepurchaseItemExcelVO();
		buildRepaymentOrderPublicWay(rs,vo);
		//构造回购单明细
		builderRepurchaseFeeDetail(vo,rs);
		return vo;
	}
	
	private void builderRepaymentFeeDetail(RepaymentOrderRepaymentItemExcelVO vo,ResultSet rs){
		try {
			String AmountGroup=rs.getString("AmountGroup");
			String[] AmountS=AmountGroup.split(",");
			vo.setRepaymentPrincipal(AmountS[0]);
			vo.setRepaymentInterest(AmountS[1]);
			vo.setLoanServicesFee(AmountS[2]);
			vo.setTechniqueServiceFee(AmountS[3]);
			vo.setOthersFee(AmountS[4]);
			vo.setOverduePenaltyInterest(AmountS[5]);
			vo.setOverdueBreachOfContract(AmountS[6]);
			vo.setOverdueServiceFee(AmountS[7]);
			vo.setOverdueOthersFee(AmountS[8]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void builderRepurchaseFeeDetail(RepaymentOrderRepurchaseItemExcelVO vo,ResultSet rs){
		try {
			String AmountGroup=rs.getString("AmountGroup");
			String[] AmountS=AmountGroup.split(",");
			vo.setRepurchasePrincipal(AmountS[0]);
			vo.setRepurchaseInterest(AmountS[1]);
			vo.setRepurchasePenaltyInterest(AmountS[2]);
			vo.setRepurchaseOthersFee(AmountS[3]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
