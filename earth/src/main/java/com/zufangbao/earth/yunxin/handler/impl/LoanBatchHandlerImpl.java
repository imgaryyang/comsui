package com.zufangbao.earth.yunxin.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.earth.yunxin.handler.LoanBatchHandler;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.AssetPackage;
import com.zufangbao.sun.entity.financial.LoanBatch;
import com.zufangbao.sun.entity.repayment.job.ImportAssetPackageJob;
import com.zufangbao.sun.entity.repayment.job.ImportAssetPackageJobLog;
import com.zufangbao.sun.entity.repayment.job.ImportAssetPackageProcessLog;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.entity.tag.TagConfigMap;
import com.zufangbao.sun.entity.tag.TagConfigType;
import com.zufangbao.sun.handler.tag.TagOpsHandler;
import com.zufangbao.sun.service.*;
import com.zufangbao.sun.service.tag.TagConfigService;
import com.zufangbao.sun.utils.excel.ExcelUtil;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.excel.RepaymentPlanExcelVO;
import com.zufangbao.sun.yunxin.entity.model.loanbatch.LoanBatchQueryModel;
import com.zufangbao.sun.yunxin.entity.model.loanbatch.LoanBatchShowModel;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.SystemOperateLogRequestParam;
import com.zufangbao.sun.yunxin.service.LoanBatchService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("LoanBatchHandler")
public class LoanBatchHandlerImpl implements LoanBatchHandler {

    @Autowired
    private LoanBatchService loanBatchService;
    @Autowired
    private AssetPackageService assetPackageService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private RepaymentPlanHandler repaymentPlanHandler;
    @Autowired
    private HouseService houseService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContractAccountService contractAccountService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private SystemOperateLogHandler systemOperateLogHandler;
    @Autowired
    private ImportAssetPackageJobLogService importAssetPackageJobLogService;
    @Autowired
    private ImportAssetPackageJobService importAssetPackageJobService;
    @Autowired
    private ImportAssetPackageProcessLogService importAssetPackageProcessLogService;
    @Autowired
    private TagOpsHandler tagOpsHandler;

    @Override
    public Map<String, List<String>> createExcelList(Long loanBatchId) {

        List<Contract> contractList = getAllContractByLoanBatch(loanBatchId);
        Map<String, List<String>> csvs = new HashMap<String, List<String>>();
        for (Contract contract : contractList) {
            List<AssetSet> assetSetList = repaymentPlanService.get_all_open_repayment_plan_list(contract);
            List<RepaymentPlanExcelVO> repaymentPlanExcelVOList = new ArrayList<RepaymentPlanExcelVO>();
            for (AssetSet assetSet : assetSetList) {
                RepaymentPlanExcelVO repaymentPlanExcelVO = createRepaymentPlanExcelVO(contract, assetSet);
                repaymentPlanExcelVOList.add(repaymentPlanExcelVO);
            }
            ExcelUtil<RepaymentPlanExcelVO> excelUtil = new ExcelUtil<RepaymentPlanExcelVO>(RepaymentPlanExcelVO.class);
            List<String> csvData = excelUtil.exportDatasToCSV(repaymentPlanExcelVOList);
            csvs.put(contract.getContractNo(), csvData);
        }
        return csvs;
    }

    @Override
    public List<RepaymentPlanExcelVO> previewRepaymentPlanExcelVO(Long loanBatchId, int limit) {
        if (loanBatchId == null) {
            return Collections.emptyList();
        }
        List<Contract> contractList = getAllContractByLoanBatch(loanBatchId);
        if (CollectionUtils.isEmpty(contractList)) {
            return Collections.emptyList();
        }
        Contract contract = contractList.get(0);
        List<RepaymentPlanExcelVO> repaymentPlanExcelVOList = new ArrayList<RepaymentPlanExcelVO>();
        List<AssetSet> assetSetList = repaymentPlanService.get_all_open_repayment_plan_list(contract);
        for (AssetSet assetSet : assetSetList) {
            RepaymentPlanExcelVO repaymentPlanExcelVO = createRepaymentPlanExcelVO(contract, assetSet);
            repaymentPlanExcelVOList.add(repaymentPlanExcelVO);
            if (repaymentPlanExcelVOList.size() >= limit) {
                break;
            }
        }
        return repaymentPlanExcelVOList;
    }

    /**
     * @param assetSet
     * @return
     */
    private RepaymentPlanExcelVO createRepaymentPlanExcelVO(Contract contract, AssetSet assetSet) {
        RepaymentPlanExcelVO repaymentPlanExcelVO = new RepaymentPlanExcelVO();
        repaymentPlanExcelVO.setUniqueId(contract.getUniqueId());
        repaymentPlanExcelVO.setRepaymentDate(DateUtils.format(assetSet.getAssetRecycleDate()));
        repaymentPlanExcelVO.setRepatriatedEarnings(assetSet.getAssetInterestValue().toString());
        repaymentPlanExcelVO.setPrincipal(assetSet.getAssetPrincipalValue().toString());
        return repaymentPlanExcelVO;
    }

    /**
     * @param sheet
     */
    @Deprecated
    private void insertDataFormatExplanRow(HSSFSheet sheet) {
        int afterRows = sheet.getPhysicalNumberOfRows();
        sheet.shiftRows(2, afterRows - 1, 1);
        HSSFRow nounRow = sheet.createRow(2);
        nounRow.createCell(0).setCellValue("字符串");
        nounRow.createCell(1).setCellValue("日期");
        nounRow.createCell(2).setCellValue("货币");
        nounRow.createCell(3).setCellValue("货币");
        nounRow.createCell(4).setCellValue("货币");
        nounRow.createCell(5).setCellValue("货币");
        nounRow.createCell(6).setCellValue("货币");
        nounRow.createCell(7).setCellValue("货币");
        nounRow.createCell(8).setCellValue("货币");
        nounRow.createCell(9).setCellValue("货币");
        nounRow.createCell(10).setCellValue("货币");
        nounRow.createCell(11).setCellValue("货币");
        nounRow.createCell(12).setCellValue("数据字典");
        nounRow.createCell(13).setCellValue("数据字典");
        nounRow.createCell(14).setCellValue("数据字典");
        nounRow.createCell(15).setCellValue("日期");
        nounRow.createCell(16).setCellValue("日期");
        nounRow.createCell(17).setCellValue("百分数");
        nounRow.createCell(18).setCellValue("整数");
        nounRow.createCell(19).setCellValue("数据字典");
        nounRow.createCell(20).setCellValue("字符串");
    }

    /**
     * @param sheet
     * @param titleCellsNum
     */
    @Deprecated
    private void insertFirstExplanationRow(HSSFSheet sheet, int titleCellsNum) {
        int rowsNum = sheet.getPhysicalNumberOfRows();
        sheet.shiftRows(0, rowsNum - 1, 1);
        HSSFRow explanationRow = sheet.createRow(0);
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, titleCellsNum);
        sheet.addMergedRegion(cra);
        explanationRow
                .createCell(0)
                .setCellValue(
                        "填写说明："
                                + "\n"
                                + "1，红色标题栏为必填项或条件必填项，其它为选填项"
                                + "\n"
                                + "2，所有涉及日期的栏位，均请按YYYYMMDD（4位年号+2位月号+2位日期）格式填写，如20100214代表2010年2月14日"
                                + "\n" + "3，所有提供选项列表的栏位，请从列表中选择相应的值，如行业类别");
    }

    /**
     * @param sheet
     */
    @Deprecated
    private int excelTitleRowCellsNum(HSSFSheet sheet) {
        return sheet.getRow(0).getLastCellNum();
    }

    /**
     * @param loanBatchId
     */
    private List<Contract> getAllContractByLoanBatch(Long loanBatchId) {
        List<Contract> contractList = new ArrayList<Contract>();
        List<AssetPackage> assetPackages = assetPackageService
                .getAssetPackageListByLoanBatchId(loanBatchId);
        for (AssetPackage assetPackage : assetPackages) {
            Contract contract = assetPackage.getContract();
            contractList.add(contract);
        }
        return contractList;
    }

    @Override
    public void deleteLoanBatchData(Principal principal, String ip,
                                    Long loanBatchId) throws Exception {

        LoanBatch loanBatch = loanBatchService.load(LoanBatch.class,
                loanBatchId);
        if (loanBatch.isAvailable()) {
            String errorMessage = "资产包已激活，无法删除！！！";
            throw new Exception(errorMessage);
        }
        List<AssetPackage> assetPackages = assetPackageService
                .getAssetPackageListByLoanBatchId(loanBatchId);
        List<Contract> contractList = new ArrayList<Contract>();
        for (AssetPackage assetPackage : assetPackages) {
            Contract contract = assetPackage.getContract();
            contractList.add(contract);
        }
        deleteLoanBacthAllAssetSet(contractList);
        deleteAssetpackageData(assetPackages);
        deleteLoanBatchContractRelatedData(contractList);
        loanBatchService.delete(loanBatch);
        this.generateLoanBacthSystemLog(principal, ip,
                LogFunctionType.DELETELOANBATCH, LogOperateType.DELETE,
                loanBatchId);

    }

    /**
     * @param assetPackages
     */
    private void deleteAssetpackageData(List<AssetPackage> assetPackages) {
        for (AssetPackage assetPackage : assetPackages) {
            assetPackageService.delete(assetPackage);
        }
    }

    /**
     * @param contractList
     */
    private void deleteLoanBatchContractRelatedData(List<Contract> contractList) {
        for (Contract contract : contractList) {
            List<ContractAccount> contractAccounts = contractAccountService
                    .getAllContractAccountBy(contract);
            if (!contractAccounts.isEmpty()) {
                for (ContractAccount contractAccount : contractAccounts) {
                    contractAccountService.delete(contractAccount);
                }
            }
            contractService.delete(contract);
            if (contract.getHouse() != null) {
                houseService.delete(contract.getHouse());
            }
            if (contract.getCustomer() != null) {
                customerService.delete(contract.getCustomer());
            }

        }
    }

    /**
     * @param contractList
     */
    private void deleteLoanBacthAllAssetSet(List<Contract> contractList) {
        for (Contract contract : contractList) {
            List<AssetSet> assetSets = repaymentPlanService.get_all_open_repayment_plan_list(contract);
            for (AssetSet assetSet : assetSets) {
                repaymentPlanService.delete(assetSet);
            }
        }
    }

    @Override
    public List<LoanBatchShowModel> generateLoanBatchShowModelList(
            LoanBatchQueryModel loanBatchQueryModel, Page page) {
        List<LoanBatch> loanBatchs = loanBatchService.queryLoanBatchIdsByQueryModel(loanBatchQueryModel, page);
        List<LoanBatchShowModel> loanBatchShowModelList = new ArrayList<LoanBatchShowModel>();
        for (LoanBatch loanBatch : loanBatchs) {
            List<AssetPackage> assetPackages = assetPackageService
                    .getAssetPackageListByLoanBatchId(loanBatch.getId());
            if (CollectionUtils.isEmpty(assetPackages)) {
                continue;
            }
            String status = LoanBatchShowModel.COMPLETE;
            String successNum = "";
            if (loanBatch.isAvailable()) {
                if (loanBatch.getCode().endsWith(LoanBatchService.ASYNC_IMPORT_FLAG_APPENDIX)) {
                    String[] results = checkAndGetAsyncInfo(loanBatch.getCode());
                    status = results[0];
                    successNum = results[1];
                }
            } else {
                status = LoanBatchShowModel.UNACTIVATED;
            }
            LoanBatchShowModel loanBatchVo = new LoanBatchShowModel(loanBatch,
                    assetPackages, status, successNum);
            if (tagOpsHandler.existNo(loanBatch.getCode(), TagConfigType.LOAN_BATCH.getCode())) {
                loanBatchVo.fileImport();
            }
            loanBatchShowModelList.add(loanBatchVo);
        }
        return loanBatchShowModelList;
    }

    private String[] checkAndGetAsyncInfo(String loanBatchCode) {
        String[] results = new String[2];
        results[0] = LoanBatchShowModel.COMPLETE;//status
        results[1] = "";//successNum
        ImportAssetPackageJobLog importAssetPackageJobLog = importAssetPackageJobLogService.checkAndGetByLoanBatch(loanBatchCode);
        if (null != importAssetPackageJobLog) {
            results[1] = "" + importAssetPackageJobLog.getSuccessSize();
            return results;
        }
        ImportAssetPackageJob importAssetPackageJob = importAssetPackageJobService.getImportAssetPackageJobByLoanBatch(loanBatchCode);
        if (null == importAssetPackageJob) {
            return results;
        }
        String requestNo = importAssetPackageJob.getRequestNo();
        List<ImportAssetPackageProcessLog> assetPackageProcessLogs = importAssetPackageProcessLogService.getImportAssetPackageProcessLogListByRequestNo(requestNo);
        if (CollectionUtils.isEmpty(assetPackageProcessLogs)) {
            results[0] = LoanBatchShowModel.CREATED;
            results[1] = "0";
        } else {
            results[0] = LoanBatchShowModel.PROCESSING;
            int successSize = 0;
            for (ImportAssetPackageProcessLog log : assetPackageProcessLogs) {
                if (log.getResultFlag() == 1) {
                    successSize++;
                }
            }
            results[1] = "" + successSize;
        }
        return results;
    }

    @Override
    public void activateLoanBatch(Long loanBatchId) {
        LoanBatch loanBatch = loanBatchService.load(LoanBatch.class,
                loanBatchId);
        loanBatchService.activationLoanBatch(loanBatch);
        //TODO
    }

    @Override
    public void generateLoanBacthSystemLog(Principal principal,
                                           String ipAddress, LogFunctionType logFunctionType,
                                           LogOperateType logOperateType, Long loanBatchId) throws Exception {
        LoanBatch loanBatch = loanBatchService.load(LoanBatch.class,
                loanBatchId);

        if (LogFunctionType.EXPORTREPAYEMNTPLAN == logFunctionType
                || LogFunctionType.ACTIVELOANBATCH == logFunctionType) {
            JudgeLoanBatchUuidIsEmptyAndAddUUid(loanBatch);
        }
        List<String> uuidList = new ArrayList<String>();
        uuidList.add(loanBatch.getUuid());
        SystemOperateLogRequestParam param = new SystemOperateLogRequestParam(
                principal.getId(), ipAddress, loanBatch.getCode(),
                logFunctionType, logOperateType, LoanBatch.class, loanBatch,
                null, uuidList);
        systemOperateLogHandler.generateSystemOperateLog(param);
    }

    private void JudgeLoanBatchUuidIsEmptyAndAddUUid(LoanBatch loanBatch) {
        if (StringUtils.isEmpty(loanBatch.getUuid())) {
            loanBatch.setLoanBatchUuid(UUID.randomUUID().toString());
            loanBatchService.saveOrUpdate(loanBatch);
        }
    }

    @Override
    public List<LoanBatch> queryLoanBatchs(LoanBatchQueryModel loanBatchQueryModel) {
        loanBatchQueryModel = loanBatchQueryModel == null ? new LoanBatchQueryModel() : loanBatchQueryModel;
        return loanBatchService.queryLoanBatchIdsByQueryModel(loanBatchQueryModel, null);
    }

}
