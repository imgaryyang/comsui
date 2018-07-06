package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repurchase.FileRepurchaseDetail;
import com.zufangbao.sun.entity.repurchase.RepurchaseAmountDetail;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.yunxin.entity.excel.RepurchaseDetailExcelVO;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseQueryModel;
import com.zufangbao.sun.yunxin.entity.model.repurchase.RepurchaseShowModel;

import java.util.List;

/**
 * 回购单
 * @author jx
 */
public interface RepurchaseHandler {
	
	/**
	 * 系统自动生成回购单
	 * @param contractId
	 */
    RepurchaseDoc conductRepurchaseViaAutomatic(Long contractId);

    /**
	 * 人工触发回购单
     * @param contractId
     * @param repurchaseAmountDetail
     */
    RepurchaseDoc conductRepurchaseViaManual(Long contractId, RepurchaseAmountDetail repurchaseAmountDetail, String batchNo);

    RepurchaseDoc conductRepurchaseViaInterface(Contract contract, FinancialContract fc, RepurchaseAmountDetail repurchaseAmountDetail, String batchNo);

    /**
	 * 设置贷款合同和还款计划的状态为回购中
	 * @param contract
	 */
    void updateAssetsetAndContractWithRepurchasingStatus(Contract contract);

	/**
	 * 去掉贷款合同和还款计划的回购中状态
	 * @param contract
	 */
    void updateAssetsetAndContractRemoveRepurchasingStatus(Contract contract);

    /**
	 * 设置贷款合同和还款计划的状态为违约
	 * @param contract
	 */
    void updateAssetsetAndContractWithDefaultStatus(Contract contract);

    /**
	 * 回购违约操作
	 * @param repurchaseDocUuid
	 * @param request TODO
	 * @param principal TODO
	 * @return 错误信息
	 */
    String repurchaseDefault(String repurchaseDocUuid, Long principalId, String ipAddr) throws Exception;

    /**
	 * 激活回购单操作
	 * @param repurchaseDocUuid
	 * @param principalId
	 * @param ipAddr
	 * @return 错误信息
	 * @throws Exception
	 */
    String activateRepurchaseDoc(String repurchaseDocUuid, Long principalId, String ipAddr) throws Exception;

    /**
	 * 作废回购单操作
	 * @param repurchaseDoc
	 * @param contract TODO
	 * @param financialContract TODO
	 * @param principalId
	 * @param ipAddr
	 * @throws Exception
	 */
    void nullifyRepurchaseDoc(RepurchaseDoc repurchaseDoc, Contract contract, FinancialContract financialContract, Long principalId, String ipAddr) throws Exception;

    /**
	 * 查询
	 */
    List<RepurchaseShowModel> query(RepurchaseQueryModel queryModel, Page page);

	/**
	 * 能否作废
	 */
	boolean canNullifyRepurchaseDoc(String repurchaseUuid);

	 List<RepurchaseDetailExcelVO> previewRepurchaseDetail(RepurchaseQueryModel queryModel, Page page);
	/**
	 * 人工批量回购
	 * @param fc
	 * @param details
	 * @param batchNo
	 * @return
	 */
	List<FileRepurchaseDetail> batchRepurchase(FinancialContract fc, List<FileRepurchaseDetail> details,
			String batchNo);
}
