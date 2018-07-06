package com.suidifu.morganstanley.handler.repayment;

import com.suidifu.morganstanley.model.request.repayment.ContractDetail;
import com.suidifu.morganstanley.model.request.repayment.ImportAssetPackage;
import com.suidifu.swift.notifyserver.notifyserver.impl.NotifyJobServerImpl;
import com.zufangbao.sun.api.model.repayment.ImportAssetPackageResponseModel;
import com.zufangbao.sun.entity.financial.FinancialContract;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 导入资产包接口
 *
 * @author wangjianhua
 */
public interface ImportAssetPackageHandler {

    ImportAssetPackageResponseModel importAssetPackage(ImportAssetPackage model, boolean not_offline_remittance) throws Exception;

    List<String> checkContractEffectDate(ImportAssetPackage model, boolean not_offline_remittance, boolean notNullRemittance);

    void dataVerification(ImportAssetPackage model, boolean not_offline_remittance, boolean notNullRemittance);

    void checkFirstPeriodAssetRecycleDate(ContractDetail contractDetail, boolean not_offline_remittance, boolean notNullRemittance);

    FinancialContract checkImportAssetPackageRequestModel(ImportAssetPackage model, HttpServletRequest request);

    void executeAsyncImport(NotifyJobServerImpl morganStanleyNotifyServer, HttpServletRequest request, ImportAssetPackage model);

    String asyncImportAssetPackage(String subRequestNo) throws Exception;
}
