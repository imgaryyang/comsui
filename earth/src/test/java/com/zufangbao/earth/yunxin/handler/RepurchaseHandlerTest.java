package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountDetail;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.entity.repurchase.RepurchaseStatus;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseQueryModel;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * @author juxer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class RepurchaseHandlerTest extends AbstractTransactionalJUnit4SpringContextTests  {
	
	@Autowired
	private RepurchaseHandler repurchaseHandler;
	
	@Autowired
	private RepurchaseService repurchaseService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private FinancialContractService financialContractService;

	@Test
	@Sql("classpath:test/yunxin/repurchaseHandler/test4repurchaseDefault.sql")
	public void test4RepurchaseDefault_R(){
		String repurchaseDocUuid = "repurchase_doc_uuid_1";
		Long principalId = 2333L;
		String ipAddr = "111.111.111.111";
		try {
			repurchaseHandler.repurchaseDefault(repurchaseDocUuid, principalId, ipAddr);
			RepurchaseDoc rDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseDocUuid);
			assertEquals(RepurchaseStatus.DEFAULT, rDoc.getRepurchaseStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Sql("classpath:test/yunxin/repurchaseHandler/test4ActivateRepurchaseDoc.sql")
	public void test4ActivateRepurchaseDoc_R(){
		String repurchaseDocUuid = "repurchase_doc_uuid_1";
		Long principalId = 2333L;
		String ipAddr = "111.111.111.111";
		try {
			repurchaseHandler.activateRepurchaseDoc(repurchaseDocUuid, principalId, ipAddr);
			RepurchaseDoc rDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseDocUuid);
			assertEquals(RepurchaseStatus.REPURCHASING, rDoc.getRepurchaseStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	@Sql("classpath:test/yunxin/repurchaseHandler/test4NullifyRepurchaseDoc.sql")
	public void test4nullifyRepurchaseDoc_R(){
		String repurchaseDocUuid = "repurchase_doc_uuid_1";
		Long principalId = 2333L;
		String ipAddr = "111.111.111.111";
		try {
			RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocByUuid(repurchaseDocUuid);
	        Contract contract = contractService.getContract(repurchaseDoc.getContractId());
	        FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());
			repurchaseHandler.nullifyRepurchaseDoc(repurchaseDoc, contract, financialContract, principalId, ipAddr);
			assertEquals(RepurchaseStatus.INVALID, repurchaseDoc.getRepurchaseStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Test
    @Ignore
    public void test_conductRepurchaseViaAutomatic() {
        Long contractId = null;
        repurchaseHandler.conductRepurchaseViaAutomatic(contractId);
        // assertEquals...
    }

    @Test
    @Ignore
    public void test_conductRepurchaseViaManual() {
        Long contractId = null;
        RepurchaseAmountDetail amountDetail = null;
//        repurchaseHandler.conductRepurchaseViaManual(contractId, amountDetail);
        // assertEquals...
    }

    @Test
    @Ignore
    public void test_updateAssetsetAndContractWithRepurchasingStatus() {
        Contract contract = null;
        repurchaseHandler.updateAssetsetAndContractWithRepurchasingStatus(contract);
        // assertEquals...
    }

    @Test
    @Ignore
    public void test_updateAssetsetAndContractRemoveRepurchasingStatus() {
        Contract contract = null;
        repurchaseHandler.updateAssetsetAndContractRemoveRepurchasingStatus(contract);
        // assertEquals...
    }

    @Test
	@Sql("classpath:test/yunxin/repurchaseHandler/test_updateAssetsetAndContractWithDefaultStatus.sql")
    public void test_updateAssetsetAndContractWithDefaultStatus() {
        try
        {Contract contract = contractService.load(Contract.class, 39679L);
        repurchaseHandler.updateAssetsetAndContractWithDefaultStatus(contract);}
        catch (RuntimeException e) {
            Assert.assertEquals("设置还款计划状态为违约的操作失败", e.getMessage());
        }
    }
    
    @Test
	@Sql("classpath:test/yunxin/repurchaseHandler/test_updateAssetsetAndContractWithDefaultStatus.sql")
    public void test_updateAssetsetAndContractWithDefaultStatus_() {
        Contract contract = contractService.load(Contract.class, 39679L);
        repurchaseHandler.updateAssetsetAndContractWithDefaultStatus(contract);
        
    }

    @Test
    @Ignore
    public void test_query() {
        RepurchaseQueryModel queryModel = null;
        Page page = null;
        repurchaseHandler.query(queryModel, page);
        // assertEquals...
    }


}
