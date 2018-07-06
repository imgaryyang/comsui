package com.zufangbao.earth.report.wrapper;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;
import com.zufangbao.earth.model.report.ExportEventLogModel;
import com.zufangbao.sun.entity.firstOverdueRate.FirstOverdueRate;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.yunxin.entity.excel.FirstOverdueRateExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentPlanExcelVO;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.wellsfargo.api.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.ZipOutputStream;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * Created by zxj on 2018/3/23.
 * 报表管理-首逾率
 */
@Component
public class ReportWrapper22 extends ReportBaseWrapper implements IReportWrapper<HashMap> {
    @Autowired
    private GenericDaoSupport genericDaoSupport;
    @Autowired
    private SystemOperateLogService systemOperateLogService;

    @Override
    public ExportEventLogModel wrap(HashMap paramsBean, HttpServletRequest request, HttpServletResponse response, ExportEventLogModel exportEventLogModel, Principal principal) throws Exception {
        String financialContractUuids = (String) paramsBean.get("financialContractUuids");
        List<String> financialContractUuidList = JsonUtils.parseArray(financialContractUuids, String.class);
        if (financialContractUuidList != null && financialContractUuidList.isEmpty()) {
            return exportEventLogModel;
        }

        String date = (String) paramsBean.get("date");
        Date assetRecycleDate = date == null ? null : DateUtils.parseDate(date, "yyyy-MM-dd");

        HashMap<String, Object> params = new HashMap<>();
        params.put("financialContractUuidList", financialContractUuidList);
        if (assetRecycleDate != null) {
            params.put("assetRecycleDate",assetRecycleDate);
        }

        String sql = getCachedSql("reportWrapper22", params);

        exportEventLogModel.recordStartLoadDataTime();

        String fileName = getFileName(DateUtils.asDay(new Date()));
        ZipOutputStream zip = openZipOutputStream(response, fileName);
        PrintWriter printWriter = putNextZipEntry(zip, "首逾率");

        ExportableRowCallBackHandler<FirstOverdueRateExcelVO> callBackHandler = new ExportableRowCallBackHandler<FirstOverdueRateExcelVO>(FirstOverdueRateExcelVO.class, printWriter, new ReportVOBuilder<FirstOverdueRateExcelVO>() {
            @Override
            public FirstOverdueRateExcelVO buildRow(ResultSet rs) throws SQLException {
                return buildFirstOverdueRateExcelVO(rs);
            }
        });
        genericDaoSupport.query(sql,params,callBackHandler);
        // 写出报表
        closeZipOutputStream(zip, response);

        exportEventLogModel.recordAfterLoadDataComplete(callBackHandler);

        SystemOperateLog log = new SystemOperateLog(principal.getId(), IpUtils.getIpAddress(request), LogFunctionType.EXPORTFIRSTOVERDUERATE, LogOperateType.EXPORT);
        log.setRecordContent("导出首逾率，信托合同uuid为【"+financialContractUuids+"】，日期为【"+date+"】，导出记录为"+ callBackHandler.getResultSize()+"条");
        systemOperateLogService.save(log);

        return exportEventLogModel;
    }
    private String getFileName(Date create_date) {
        return String.format("首逾率%s.zip", DateUtils.format(create_date, "yyyy_MM_dd"));
    }
    private FirstOverdueRateExcelVO buildFirstOverdueRateExcelVO(ResultSet rs) throws SQLException {
        FirstOverdueRateExcelVO excelVO = new FirstOverdueRateExcelVO();
        excelVO.setContractName(rs.getString("contract_name"));
        excelVO.setContractNo(rs.getString("contract_no"));
        excelVO.setDate(rs.getString("asset_recycle_date"));

        BigDecimal remainingPrincipalValue1 = rs.getBigDecimal("remaining_principal_value_1") == null ? new BigDecimal(0) : rs.getBigDecimal("remaining_principal_value_1");
        BigDecimal remainingPrincipalValue = rs.getBigDecimal("remaining_principal_value") == null ? new BigDecimal(0) : rs.getBigDecimal("remaining_principal_value");
        BigDecimal remainingInterestValue1 = rs.getBigDecimal("remaining_interest_value_1") == null ? new BigDecimal(0) : rs.getBigDecimal("remaining_interest_value_1");
        BigDecimal remainingInterestValue = rs.getBigDecimal("remaining_interest_value") == null ? new BigDecimal(0) : rs.getBigDecimal("remaining_interest_value");
        BigDecimal offlinePrincipalValue1 = rs.getBigDecimal("offline_principal_value_1") == null ? new BigDecimal(0) : rs.getBigDecimal("offline_principal_value_1");
        BigDecimal offlinePrincipalValue = rs.getBigDecimal("offline_principal_value") == null ? new BigDecimal(0) : rs.getBigDecimal("offline_principal_value");
        BigDecimal offlineInterestValue1 = rs.getBigDecimal("offline_interest_value_1") == null ? new BigDecimal(0) : rs.getBigDecimal("offline_interest_value_1");
        BigDecimal offlineInterestValue = rs.getBigDecimal("offline_interest_value") == null ? new BigDecimal(0) : rs.getBigDecimal("offline_interest_value");
        BigDecimal assetPrincipalValue1 = rs.getBigDecimal("asset_principal_value_1") == null ? new BigDecimal(0) : rs.getBigDecimal("asset_principal_value_1");
        BigDecimal assetPrincipalValue = rs.getBigDecimal("asset_principal_value") == null ? new BigDecimal(0) : rs.getBigDecimal("asset_principal_value");
        BigDecimal assetInterestValue1 = rs.getBigDecimal("asset_interest_value_1") == null ? new BigDecimal(0) : rs.getBigDecimal("asset_interest_value_1");
        BigDecimal assetInterestValue = rs.getBigDecimal("asset_interest_value") == null ? new BigDecimal(0) : rs.getBigDecimal("asset_interest_value");

        BigDecimal rate1 = new BigDecimal(0);
        BigDecimal rate2 = new BigDecimal(0);
        BigDecimal rate3 = new BigDecimal(0);
        BigDecimal rate4 = new BigDecimal(0);
        if (assetPrincipalValue1.compareTo(BigDecimal.ZERO) != 0) {
            rate1 = remainingPrincipalValue1.subtract(offlinePrincipalValue1).divide(assetPrincipalValue1, 2,ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2);
            rate2 = remainingPrincipalValue1.add(remainingInterestValue1).subtract(offlinePrincipalValue1).subtract(offlineInterestValue1).divide(assetPrincipalValue1.add(assetInterestValue1), 2,ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2);
        }
        if (assetPrincipalValue.compareTo(BigDecimal.ZERO) != 0) {
            rate3 = remainingPrincipalValue.subtract(offlinePrincipalValue).divide(assetPrincipalValue, 2,ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2);
            rate4 = remainingPrincipalValue.add(remainingInterestValue).subtract(offlinePrincipalValue).subtract(offlineInterestValue).divide(assetPrincipalValue.add(assetInterestValue), 2,ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2);
        }

        excelVO.setRate1(rate1 + "%");
        excelVO.setRate2(rate2 + "%");
        excelVO.setRate3(rate3 + "%");
        excelVO.setRate4(rate4 + "%");
        return excelVO;
    }
}
