package com.zufangbao.earth.yunxin.handler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.zufangbao.sun.VO.ProjectInformationSQLReturnData;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.LoanBatch;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountDetail;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.ContractState;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.excel.LoanContractDetailCheckExcelVO;
import com.zufangbao.sun.yunxin.entity.excel.ProjectInformationExeclVo;
import com.zufangbao.sun.yunxin.entity.model.ContractQueryModel;
import com.zufangbao.sun.yunxin.handler.ContractHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml"})
@Transactional
public class ContractHandlerTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ContractHandler contractHandler;
    @Autowired
    private ContractService contractService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;
    @Autowired
    private FinancialContractService financialContractService;

    private Map<String, FinancialContract> financialContractsMap = new HashMap<String, FinancialContract>();

    @Before
    public void setUp() {
        financialContractsMap.put("d7b3b325-719a-42af-a129-0ac861f18ebe", financialContractService.load(FinancialContract.class, 1L));
    }

    @Test
    @Sql("classpath:test/yunxin/contract/testQueryLoanContractSingleAssetSet.sql")
    public void testQueryLoanContractSingleAssetSet() {
        ContractQueryModel queryModel = new ContractQueryModel();
        queryModel.setStartDateString("2016-04-17");
        queryModel.setEndDateString("2016-04-17");
        queryModel.setFinancialContractUuids("[\"d7b3b325-719a-42af-a129-0ac861f18ebe\"]");
        queryModel.setFinancialContractsMap(financialContractsMap);
        queryModel.setContractStateOrdinals("[0,1,2,3]");
        List<LoanContractDetailCheckExcelVO> LoanContracts = contractHandler.getLoanContractExcelVO(queryModel, null);

        Assert.assertEquals(1, LoanContracts.size());

        LoanContractDetailCheckExcelVO loanContract = LoanContracts.get(0);

        Assert.assertEquals("DCF-NFQ-LR903", loanContract.getFinancialContractNo());
        Assert.assertEquals("nongfenqi", loanContract.getAppName());
        Assert.assertEquals("sadas", loanContract.getAccountNo());
        Assert.assertEquals("云信信2016-78-DK(ZQ2016042522502)", loanContract.getContractNo());
        Assert.assertEquals("测试用户3", loanContract.getCustomerName());
        Assert.assertEquals("2016-04-17", loanContract.getLoanDate());
        Assert.assertEquals("2016-05-17", loanContract.getDueDate());
        Assert.assertEquals("2400.00", loanContract.getTotalAmount());
        Assert.assertEquals("2400.00", loanContract.getTotalInterest());
        Assert.assertEquals(1, loanContract.getPeriods());
        Assert.assertEquals("2400.00", loanContract.getRestSumInterest());
        Assert.assertEquals("0.00", loanContract.getRestSumPrincipal());
        Assert.assertEquals("锯齿形", loanContract.getRepaymentWay());
        Assert.assertEquals("15.60%", loanContract.getInterestRate());
        Assert.assertEquals("0.05%", loanContract.getPenaltyInterest());
        Assert.assertEquals(3, loanContract.getLoanOverdueEndDay());
        Assert.assertEquals(-1, loanContract.getRepaymentGraceTerm());
        Assert.assertEquals(null, loanContract.getIdCardNum());
        Assert.assertEquals("中国建设银行", loanContract.getBank());
        Assert.assertEquals("安徽省", loanContract.getProvince());
        Assert.assertEquals("亳州", loanContract.getCity());
        Assert.assertEquals("621************3015", loanContract.getPayAcNo());
        Assert.assertEquals("2016-04-17", loanContract.getBeginDate());

    }

    @Test
    @Sql("classpath:test/yunxin/contract/testQueryLoanContractForNormal.sql")
    public void testQueryLoanContractForNormal() {
        // 第三条资产为作废的资产
        ContractQueryModel queryModel = new ContractQueryModel();
        queryModel.setStartDateString("2016-04-17");
        queryModel.setEndDateString("2016-04-17");
        queryModel.setFinancialContractUuids("[\"d7b3b325-719a-42af-a129-0ac861f18ebe\"]");
        queryModel.setFinancialContractsMap(financialContractsMap);
        queryModel.setContractStateOrdinals("[0,1,2,3]");
        List<LoanContractDetailCheckExcelVO> LoanContracts = contractHandler.getLoanContractExcelVO(queryModel, null);
        Assert.assertEquals(1, LoanContracts.size());

        LoanContractDetailCheckExcelVO loanContract = LoanContracts.get(0);
        Assert.assertEquals("DCF-NFQ-LR903", loanContract.getFinancialContractNo());
        Assert.assertEquals("nongfenqi", loanContract.getAppName());
        Assert.assertEquals("sadas", loanContract.getAccountNo());
        Assert.assertEquals("云信信2016-78-DK(ZQ2016042522502)", loanContract.getContractNo());
        Assert.assertEquals("测试用户3", loanContract.getCustomerName());
        Assert.assertEquals("2016-04-17", loanContract.getLoanDate());
        Assert.assertEquals("2016-06-17", loanContract.getDueDate());
        Assert.assertEquals("4500.00", loanContract.getTotalAmount());
        Assert.assertEquals("4500.00", loanContract.getTotalInterest());
        Assert.assertEquals(2, loanContract.getPeriods());
        Assert.assertEquals("4500.00", loanContract.getRestSumInterest());
        Assert.assertEquals("0.00", loanContract.getRestSumPrincipal());
        Assert.assertEquals("锯齿形", loanContract.getRepaymentWay());
        Assert.assertEquals("15.60%", loanContract.getInterestRate());
        Assert.assertEquals("0.05%", loanContract.getPenaltyInterest());
        Assert.assertEquals(3, loanContract.getLoanOverdueEndDay());
        Assert.assertEquals(-1, loanContract.getRepaymentGraceTerm());
        Assert.assertEquals(null, loanContract.getIdCardNum());
        Assert.assertEquals("中国建设银行", loanContract.getBank());
        Assert.assertEquals("安徽省", loanContract.getProvince());
        Assert.assertEquals("亳州", loanContract.getCity());
        Assert.assertEquals("621************3015", loanContract.getPayAcNo());
        Assert.assertEquals("2016-04-17", loanContract.getBeginDate());

    }


    @Test
    @Sql("classpath:test/yunxin/contract/testQueryLoanContractForNOAssetSet.sql")
    public void testQueryLoanContractForNOAssetSet() {
        ContractQueryModel queryModel = new ContractQueryModel();
        queryModel.setStartDateString("2016-04-17");
        queryModel.setEndDateString("2016-04-17");
        queryModel.setFinancialContractUuids("[\"d7b3b325-719a-42af-a129-0ac861f18ebe\"]");
        queryModel.setFinancialContractsMap(financialContractsMap);
        queryModel.setContractStateOrdinals("[0,1,2,3]");
        List<LoanContractDetailCheckExcelVO> LoanContracts = contractHandler.getLoanContractExcelVO(queryModel, null);

        Assert.assertEquals(1, LoanContracts.size());

        LoanContractDetailCheckExcelVO loanContract = LoanContracts.get(0);
        Assert.assertEquals("DCF-NFQ-LR903", loanContract.getFinancialContractNo());
        Assert.assertEquals("nongfenqi", loanContract.getAppName());
        Assert.assertEquals("sadas", loanContract.getAccountNo());
        Assert.assertEquals("云信信2016-78-DK(ZQ2016042522502)", loanContract.getContractNo());
        Assert.assertEquals("测试用户3", loanContract.getCustomerName());
        Assert.assertEquals("2016-04-17", loanContract.getLoanDate());
        Assert.assertEquals(null, loanContract.getDueDate());
        Assert.assertEquals("0.00", loanContract.getTotalAmount());
        Assert.assertEquals("0", loanContract.getTotalInterest());
        Assert.assertEquals(0, loanContract.getPeriods());
        Assert.assertEquals("0", loanContract.getRestSumInterest());
        Assert.assertEquals("0", loanContract.getRestSumPrincipal());
        Assert.assertEquals("锯齿形", loanContract.getRepaymentWay());
        Assert.assertEquals("15.60%", loanContract.getInterestRate());
        Assert.assertEquals("0.05%", loanContract.getPenaltyInterest());
        Assert.assertEquals(3, loanContract.getLoanOverdueEndDay());
        Assert.assertEquals(-1, loanContract.getRepaymentGraceTerm());
        Assert.assertEquals(null, loanContract.getIdCardNum());
        Assert.assertEquals("中国建设银行", loanContract.getBank());
        Assert.assertEquals("安徽省", loanContract.getProvince());
        Assert.assertEquals("亳州", loanContract.getCity());
        Assert.assertEquals("621************3015", loanContract.getPayAcNo());
        Assert.assertEquals("2016-04-17", loanContract.getBeginDate());

    }


    @Test
    @Sql("classpath:test/yunxin/contract/testQueryLoanContractForAssetSetIsClearing.sql")
    public void testQueryLoanContractForAssetSetIsClearing() {
        ContractQueryModel queryModel = new ContractQueryModel();
        queryModel.setStartDateString("2016-04-17");
        queryModel.setEndDateString("2016-04-17");
        queryModel.setFinancialContractUuids("[\"d7b3b325-719a-42af-a129-0ac861f18ebe\"]");
        queryModel.setFinancialContractsMap(financialContractsMap);
        queryModel.setContractStateOrdinals("[0,1,2,3]");
        List<LoanContractDetailCheckExcelVO> LoanContracts = contractHandler.getLoanContractExcelVO(queryModel, null);

        Assert.assertEquals(1, LoanContracts.size());

        LoanContractDetailCheckExcelVO loanContract = LoanContracts.get(0);
        Assert.assertEquals("DCF-NFQ-LR903", loanContract.getFinancialContractNo());
        Assert.assertEquals("nongfenqi", loanContract.getAppName());
        Assert.assertEquals("sadas", loanContract.getAccountNo());
        Assert.assertEquals("云信信2016-78-DK(ZQ2016042522502)", loanContract.getContractNo());
        Assert.assertEquals("测试用户3", loanContract.getCustomerName());
        Assert.assertEquals("2016-04-17", loanContract.getLoanDate());
        Assert.assertEquals("2016-05-17", loanContract.getDueDate());
        Assert.assertEquals("4500.00", loanContract.getTotalAmount());
        Assert.assertEquals("4500.00", loanContract.getTotalInterest());
        Assert.assertEquals(2, loanContract.getPeriods());
        Assert.assertEquals("4500.00", loanContract.getRestSumInterest());
        Assert.assertEquals("0.00", loanContract.getRestSumPrincipal());
        Assert.assertEquals("锯齿形", loanContract.getRepaymentWay());
        Assert.assertEquals("15.60%", loanContract.getInterestRate());
        Assert.assertEquals("0.05%", loanContract.getPenaltyInterest());
        Assert.assertEquals(3, loanContract.getLoanOverdueEndDay());
        Assert.assertEquals(-1, loanContract.getRepaymentGraceTerm());
        Assert.assertEquals(null, loanContract.getIdCardNum());
        Assert.assertEquals("中国建设银行", loanContract.getBank());
        Assert.assertEquals("安徽省", loanContract.getProvince());
        Assert.assertEquals("亳州", loanContract.getCity());
        Assert.assertEquals("621************3015", loanContract.getPayAcNo());
        Assert.assertEquals("2016-04-17", loanContract.getBeginDate());

    }

    @Test
    @Sql("classpath:test/yunxin/contract/testCastProjectInformationShowVOs.sql")
    public void testCastProjectInformationShowVOs() {

        ProjectInformationSQLReturnData data = new ProjectInformationSQLReturnData();
        data.setId(325l);
        data.setMaxAssetRecycleDate("2029-08-17");
        List<ProjectInformationSQLReturnData> datas = new ArrayList<ProjectInformationSQLReturnData>();
        datas.add(data);
        List<ProjectInformationExeclVo> showVOs = contractHandler.castProjectInformationShowVOs(datas);
        Assert.assertEquals(1, showVOs.size());
        ProjectInformationExeclVo showVO = showVOs.get(0);
        Assert.assertEquals("2029-08-17", showVO.getExpectTerminalDate());
        Assert.assertEquals(null, showVO.getActualTermainalDate());
        Assert.assertEquals("云信信2016-78-DK(ZQ2016042522502)", showVO.getContractNo());
        Assert.assertEquals("0.00", showVO.getCurrentPeriodRepaymentAmount());
        Assert.assertEquals("2100.00", showVO.getCurrentPeriodRepaymentInterest());
        Assert.assertEquals("1/2", showVO.getRepaymentSchedule());
        Assert.assertEquals("测试用户3", showVO.getCustomerName());
        Assert.assertEquals("2016-04-17", showVO.getEffectDate());
        Assert.assertEquals("4500.00", showVO.getLoanAmount());
        Assert.assertEquals("未还", showVO.getRepaymentSituation());
        Assert.assertEquals("2029-08-17", showVO.getRepaymentDate());
    }

    @Test
    @Sql("classpath:test/yunxin/contract/testCastProjectInformationShowVONowDayThanLastRepayemtnDate.sql")
    public void testCastProjectInformationShowVONowDayThanLastRepayemtnDate() {

        ProjectInformationSQLReturnData data = new ProjectInformationSQLReturnData();
        data.setId(325l);
        data.setMaxAssetRecycleDate("2016-02-17");
        List<ProjectInformationSQLReturnData> datas = new ArrayList<ProjectInformationSQLReturnData>();
        datas.add(data);
        List<ProjectInformationExeclVo> showVOs = contractHandler.castProjectInformationShowVOs(datas);
        Assert.assertEquals(1, showVOs.size());
        ProjectInformationExeclVo showVO = showVOs.get(0);
        Assert.assertEquals("2016-02-17", showVO.getExpectTerminalDate());
        Assert.assertEquals(null, showVO.getActualTermainalDate());
        Assert.assertEquals("云信信2016-78-DK(ZQ2016042522502)", showVO.getContractNo());
        Assert.assertEquals("0.00", showVO.getCurrentPeriodRepaymentAmount());
        Assert.assertEquals("2100.00", showVO.getCurrentPeriodRepaymentInterest());
        Assert.assertEquals("1/2", showVO.getRepaymentSchedule());
        Assert.assertEquals("测试用户3", showVO.getCustomerName());
        Assert.assertEquals("2016-04-17", showVO.getEffectDate());
        Assert.assertEquals("4500.00", showVO.getLoanAmount());
        Assert.assertEquals("未还", showVO.getRepaymentSituation());
        Assert.assertEquals("2016-02-17", showVO.getRepaymentDate());
    }

    @Test
    @Sql("classpath:test/yunxin/contract/testCastProjectInformationShowVONowDayLessThanBeginDate.sql")
    public void testCastProjectInformationShowVONowDayLessThanBeginDate() {

        ProjectInformationSQLReturnData data = new ProjectInformationSQLReturnData();
        data.setId(325l);
        data.setMaxAssetRecycleDate("2029-10-17");
        List<ProjectInformationSQLReturnData> datas = new ArrayList<ProjectInformationSQLReturnData>();
        datas.add(data);
        List<ProjectInformationExeclVo> showVOs = contractHandler.castProjectInformationShowVOs(datas);
        Assert.assertEquals(1, showVOs.size());
        ProjectInformationExeclVo showVO = showVOs.get(0);
        Assert.assertEquals("2029-10-17", showVO.getExpectTerminalDate());
        Assert.assertEquals(null, showVO.getActualTermainalDate());
        Assert.assertEquals("云信信2016-78-DK(ZQ2016042522502)", showVO.getContractNo());
        Assert.assertEquals("0.00", showVO.getCurrentPeriodRepaymentAmount());
        Assert.assertEquals("2400.00", showVO.getCurrentPeriodRepaymentInterest());
        Assert.assertEquals("0/2", showVO.getRepaymentSchedule());
        Assert.assertEquals("测试用户3", showVO.getCustomerName());
        Assert.assertEquals("2016-04-17", showVO.getEffectDate());
        Assert.assertEquals("4500.00", showVO.getLoanAmount());
        Assert.assertEquals("未还", showVO.getRepaymentSituation());
        Assert.assertEquals("2029-09-17", showVO.getRepaymentDate());
    }

    @Test
    @Sql("classpath:test/yunxin/contract/testInvalidateContract_error.sql")
    public void testInvalidateContract_throwError() {
        try {
            String fileName = "";
            Contract contract = contractService.load(Contract.class, 325L);
            contractHandler.invalidateContract(contract, 1L, "autoTest", "贷款合同作废", "127.0.0.1",fileName);
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("贷款合同已开始执行，无法中止!", e.getMessage());
        }
    }

    @Test
    @Sql("classpath:test/yunxin/contract/testInvalidateContract_error2.sql")
    public void testInvalidateContract_throwError2() {
        try {
            String fileName = "";
            Contract contract = contractService.load(Contract.class, 325L);
            contractHandler.invalidateContract(contract, 1L, "autoTest", "贷款合同作废", "127.0.0.1",fileName);
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("贷款合同已开始执行，无法中止!", e.getMessage());
        }
    }

    @Test
    @Sql("classpath:test/yunxin/contract/testInvalidateContract.sql")
    public void testInvalidateContract() {
        try {
            String fileName = "";
            Contract contract = contractService.load(Contract.class, 325L);
            contractHandler.invalidateContract(contract, 1L, "autoTest", "贷款合同作废", "127.0.0.1",fileName);

            List<AssetSet> list = repaymentPlanService.loadAll(AssetSet.class);
            long count = list.stream().filter(assetSet -> assetSet.getActiveStatus() == AssetSetActiveStatus.INVALID).count();
            Assert.assertEquals(2L, count);

            Contract contract2 = contractService.load(Contract.class, 325L);
            Assert.assertEquals(ContractState.INVALIDATE.ordinal(), contract2.getState().ordinal());

        } catch (RuntimeException e) {
            e.printStackTrace();
            //	Assert.fail();
        }
    }

    @Test
    @Ignore
    public void test_calculateRepurchaseAmountDetail() {
        List<AssetSet> assetSets = new ArrayList<AssetSet>();
        FinancialContract financialContract = null;
        Contract contract = null;
        RepurchaseAmountDetail repurchaseAmountDetail = null;
        contractHandler.calculateRepurchaseAmountDetail(assetSets, financialContract, contract, repurchaseAmountDetail);
        // assertEquals...
    }

    @Test
    @Ignore
    public void test_cal_repurchase_detail_algorithm() {
        RepurchaseAmountDetail repurchaseAmountDetail = null;
        FinancialContract financialContract = null;
        Contract contract = null;
        contractHandler.cal_repurchase_detail_algorithm(repurchaseAmountDetail, financialContract);
        // assertEquals...
    }

    @Test
    @Ignore
    public void test_parseRepurchaseEnvironmentVariables() {
        List<AssetSet> assetSets = new ArrayList<AssetSet>();
        RepurchaseAmountDetail repurchaseAmountDetail = null;
        contractHandler.parseRepurchaseEnvironmentVariables(assetSets);
        // assertEquals...
    }

    @Test
    @Sql("classpath:test/yunxin/contract/test_getLoanBathByContract.sql")
    public void test_getLoanBathByContract() {
        Long contractId = 1L;
        LoanBatch loanBatch = contractHandler.getLoanBathByContract(contractId);
        Assert.assertEquals(new Long(1),loanBatch.getId());
    }

}
