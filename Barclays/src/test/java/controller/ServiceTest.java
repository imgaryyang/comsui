package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.suidifu.coffer.util.CSVFileUtil;
import com.zufangbao.sun.entity.account.InstitutionReconciliationAuditStatus;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyTransactionRecord;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyTransactionRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.sun.entity.account.AuditStatus;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.yunxin.entity.barclays.BusinessProcessStatus;
import com.zufangbao.sun.yunxin.entity.barclays.ThirdPartyAuditBill;
import com.zufangbao.sun.yunxin.service.barclays.ThirdPartyAuditBillService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/context/applicationContext-*.xml",
		"classpath:/local/applicationContext-*.xml", })
@TransactionConfiguration(defaultRollback=false)
public class ServiceTest {

	@Autowired
	ThirdPartyAuditBillService thirdPartyAuditBillService;

	@Autowired
	ThirdPartyTransactionRecordService thirdPartyTransactionRecordService;
	
	@Test
	public void testThirdPartyAuditBill() {
		try {

			//15486->fb5c398f-46c3-4923-a125-5518fa2931d2   31013->6582ca51-659e-4f48-95ee-50675478ce30
			List<ThirdPartyAuditBill> auditBillModelList = new ArrayList<ThirdPartyAuditBill>();
			CSVFileUtil csvFileUtil = new CSVFileUtil("/Users/qinweichao/Downloads/9.1-9.21对账单天百融安.csv");
			String line = null;
			int startLine = 0;
			while ((line = csvFileUtil.readLine()) != null) {
					startLine++;
					if (startLine < 2){
						continue;
					}
					List<String> listData = csvFileUtil.fromCSVLinetoArray(line);
					List<String> auditBillArr = new ArrayList<String>();
					for (String string : listData) {
						auditBillArr.add(string.trim());
					}
					ThirdPartyAuditBill thirdPartyAuditBill = new ThirdPartyAuditBill();
					thirdPartyAuditBill.setFinancialContractUuid("6582ca51-659e-4f48-95ee-50675478ce30");
					thirdPartyAuditBill.setAuditBillUuid(UUID.randomUUID().toString());
					thirdPartyAuditBill.setMerchantNo(auditBillArr.get(1));
					thirdPartyAuditBill.setMerchantOrderNo(auditBillArr.get(3));
					thirdPartyAuditBill.setChannelSequenceNo(auditBillArr.get(4));
					thirdPartyAuditBill.setTransactionAmount(new BigDecimal(auditBillArr.get(7)));
					thirdPartyAuditBill.setAuditStatus(AuditStatus.CREATE);
					thirdPartyAuditBill.setCreateTime(new Date());
					thirdPartyAuditBill.setIssuedAmount(BigDecimal.ZERO);
					thirdPartyAuditBill.setAccountSide(com.zufangbao.sun.ledgerbook.AccountSide.DEBIT);
					thirdPartyAuditBill.setPaymentGateway(PaymentInstitutionName.JIANHANG);
					thirdPartyAuditBill.setSettleDate(DateUtils.parseDate(auditBillArr.get(8), "yyyy-MM-dd HH:mm:ss"));
					thirdPartyAuditBill.setTransactionTime(DateUtils.parseDate(auditBillArr.get(8), "yyyy-MM-dd HH:mm:ss"));
					auditBillModelList.add(thirdPartyAuditBill);

			}
			thirdPartyAuditBillService.judgeAndSaveThirdPartyAuditBills(auditBillModelList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testThirdPartyTransactionRecord(){
		try {
			/**
			 *15486->6be51699-76d7-410a-b8d4-39e84f8f32b5  中通银邦 
			 *31013->073e3f82-8321-4f0f-802f-84bcf201ebd1 天百融安
			 *
			 */
		List<ThirdPartyTransactionRecord> thirdPartyTransactionRecordList = new ArrayList<ThirdPartyTransactionRecord>();
		CSVFileUtil csvFileUtil = new CSVFileUtil("/Users/qinweichao/Downloads/9.1-9.21对账单中通银邦.csv");
		String line = null;
		int startLine = 0;
		while ((line = csvFileUtil.readLine()) != null) {
			startLine++;
			if (startLine < 2){
				continue;
			}
			List<String> listData = csvFileUtil.fromCSVLinetoArray(line);
			List<String> auditBillArr = new ArrayList<String>();
			for (String string : listData) {
				auditBillArr.add(string.trim());
			}
			ThirdPartyTransactionRecord thirdPartyTransactionRecord = new ThirdPartyTransactionRecord();
			thirdPartyTransactionRecord.setFinancialContractUuid("6be51699-76d7-410a-b8d4-39e84f8f32b5");
			thirdPartyTransactionRecord.setTransactionRecordUuid(UUID.randomUUID().toString());
			thirdPartyTransactionRecord.setMerchantNo(auditBillArr.get(1));
			thirdPartyTransactionRecord.setMerchantOrderNo(auditBillArr.get(3));
			thirdPartyTransactionRecord.setChannelSequenceNo(auditBillArr.get(4));
			thirdPartyTransactionRecord.setTransactionAmount(new BigDecimal(auditBillArr.get(7)));
			thirdPartyTransactionRecord.setAuditStatus(InstitutionReconciliationAuditStatus.CREATE);
			thirdPartyTransactionRecord.setCreateTime(new Date());
			thirdPartyTransactionRecord.setIssuedAmount(BigDecimal.ZERO);
			thirdPartyTransactionRecord.setAccountSide(com.zufangbao.sun.ledgerbook.AccountSide.DEBIT);
			thirdPartyTransactionRecord.setPaymentGateway(PaymentInstitutionName.JIANHANG);
			thirdPartyTransactionRecord.setSettleDate(DateUtils.parseDate(auditBillArr.get(8), "yyyy-MM-dd HH:mm:ss"));
			thirdPartyTransactionRecord.setTransactionTime(DateUtils.parseDate(auditBillArr.get(8), "yyyy-MM-dd HH:mm:ss"));
			thirdPartyTransactionRecord.setBusinessProcessStatus(BusinessProcessStatus.SUCCESS);
			thirdPartyTransactionRecordList.add(thirdPartyTransactionRecord);

		}
			thirdPartyTransactionRecordService.judgeAndSaveTransactionRecord(thirdPartyTransactionRecordList);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}

}
