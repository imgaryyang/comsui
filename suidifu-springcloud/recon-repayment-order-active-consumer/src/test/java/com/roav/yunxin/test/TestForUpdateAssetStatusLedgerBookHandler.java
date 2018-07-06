package com.roav.yunxin.test;


import com.zufangbao.sun.api.model.deduct.RepaymentType;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.yunxin.ContractState;
import com.zufangbao.sun.yunxin.entity.ContractCompletionStatus;
import com.zufangbao.sun.yunxin.entity.ExecutingStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by MieLongJun on 18-3-7.
 */
public class TestForUpdateAssetStatusLedgerBookHandler extends TestBase {
    /**
     * 还款成功
     */
    @Test
    @Sql("classpath:test/yunxin/updateAssetsFromLedgerBook.sql")
    public void test_updateAssetsFromLedgerBook() {
        String ledgerBookNo = "3f501aca-ac49-4889-882a-575d1d09d391";
        List<String> assetSetUuids = new ArrayList<String>();
        assetSetUuids.add("c80b06c4-6187-4bcf-abad-463b66a7ab02");
        Date actualRecycleTime = new Date();
        RepaymentType repaymentType = RepaymentType.NORMAL;
        ExecutingStatus executingStatus = ExecutingStatus.PROCESSING;
        updateAssetStatusLedgerBookHandler.updateAssetsFromLedgerBook(ledgerBookNo, assetSetUuids, actualRecycleTime, repaymentType, executingStatus);
        Contract contract = contractService.getContract(39759L);
        Assert.assertNull(contract.getCompletionStatus());
        Assert.assertEquals(ContractState.EFFECTIVE, contract.getState());
        Assert.assertEquals(1, contract.getRepaymentedPeriods());

    }

    /**
     * 部分核销
     */
    @Test
    @Sql("classpath:test/yunxin/updateAssetsFromLedgerBook2.sql")
    public void test_updateAssetsFromLedgerBook2() {
        String ledgerBookNo = "3f501aca-ac49-4889-882a-575d1d09d391";
        List<String> assetSetUuids = new ArrayList<String>();
        assetSetUuids.add("1b85028a-769f-426f-a332-b4e274e34772");
        Date actualRecycleTime = new Date();
        RepaymentType repaymentType = RepaymentType.NORMAL;
        ExecutingStatus executingStatus = ExecutingStatus.PROCESSING;
        updateAssetStatusLedgerBookHandler.updateAssetsFromLedgerBook(ledgerBookNo, assetSetUuids, actualRecycleTime, repaymentType, executingStatus);
        Contract contract = contractService.getContract(39759L);
        Assert.assertNull(contract.getCompletionStatus());
        Assert.assertEquals(ContractState.EFFECTIVE, contract.getState());
        Assert.assertEquals(1, contract.getRepaymentedPeriods());

    }

    /**
     * 全部还款
     */
    @Test
    @Sql("classpath:test/yunxin/updateAssetsFromLedgerBook3.sql")
    public void test_updateAssetsFromLedgerBook3() {
        String ledgerBookNo = "3f501aca-ac49-4889-882a-575d1d09d391";
        List<String> assetSetUuids = new ArrayList<String>();
        assetSetUuids.add("52ebebd3-4554-49c5-b36b-9bbb7611b316");
        Date actualRecycleTime = new Date();
        RepaymentType repaymentType = RepaymentType.NORMAL;
        ExecutingStatus executingStatus = ExecutingStatus.PROCESSING;
        updateAssetStatusLedgerBookHandler.updateAssetsFromLedgerBook(ledgerBookNo, assetSetUuids, actualRecycleTime, repaymentType, executingStatus);
        Contract contract = contractService.getContract(39759L);
        Assert.assertEquals(ContractCompletionStatus.NORMAL, contract.getCompletionStatus());
        Assert.assertEquals(ContractState.COMPLETION, contract.getState());
        Assert.assertEquals(3, contract.getRepaymentedPeriods());

    }

    /**
     * 退款
     */
    @Test
    @Sql("classpath:test/yunxin/updateAssetsFromLedgerBook4.sql")
    public void test_updateAssetsFromLedgerBook4() {
        String ledgerBookNo = "3f501aca-ac49-4889-882a-575d1d09d391";
        List<String> assetSetUuids = new ArrayList<String>();
        assetSetUuids.add("1b85028a-769f-426f-a332-b4e274e34772");
        Date actualRecycleTime = new Date();
        RepaymentType repaymentType = RepaymentType.NORMAL;
        ExecutingStatus executingStatus = ExecutingStatus.PROCESSING;
        updateAssetStatusLedgerBookHandler.updateAssetsFromLedgerBook(ledgerBookNo, assetSetUuids, actualRecycleTime, repaymentType, executingStatus);
        Contract contract = contractService.getContract(39759L);
        Assert.assertNull(contract.getCompletionStatus());
        Assert.assertEquals(ContractState.EFFECTIVE, contract.getState());
        Assert.assertEquals(2, contract.getRepaymentedPeriods());

    }
}
