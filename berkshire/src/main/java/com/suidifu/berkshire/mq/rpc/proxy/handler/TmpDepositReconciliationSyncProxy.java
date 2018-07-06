package com.suidifu.berkshire.mq.rpc.proxy.handler;

import com.suidifu.hathaway.mq.annotations.v2.MicroService;
import com.suidifu.owlman.microservice.handler.TmpDepositReconciliationHandler;
import com.suidifu.owlman.microservice.model.TmpDepositReconciliationParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobTmpDepositReconciliation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author louguanyang at 2018/2/28 16:36
 * @mail louguanyang@hzsuidifu.com
 */
@Component("tmpDepositReconciliationSyncProxy")
public class TmpDepositReconciliationSyncProxy implements TmpDepositReconciliationHandler {

  @Resource
  private DstJobTmpDepositReconciliation dstJobTmpDepositReconciliation;

  @Override
  @MicroService(beanName="tmpDepositReconciliationHandler",
      methodName="criticalMarker",
      sync=true,
      vhostName="/business",
      exchangeName="exchange-business",
      routingKey = "tmp-deposit-reconciliation")
  public Map<String, String> criticalMarker(List<String> list) {
    return dstJobTmpDepositReconciliation.criticalMarker(list);
  }

  @Override
  @MicroService(beanName="tmpDepositReconciliationHandler",
      methodName="validateDetailList",
      sync=true,
      vhostName="/business",
      exchangeName="exchange-business",
      routingKey = "tmp-deposit-reconciliation")
  public boolean validateDetailList(List<TmpDepositReconciliationParameters> list) {
    List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters> oldParametersList = copyParametersList(list);
    return dstJobTmpDepositReconciliation.validateDetailList(oldParametersList);
  }

  @NotNull
  private List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters> copyParametersList(List<TmpDepositReconciliationParameters> list) {
    List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters> oldParametersList = new ArrayList<>();
    for (TmpDepositReconciliationParameters parameter : list) {
      com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters oldParamter = new com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters();
      BeanUtils.copyProperties(parameter, oldParamter);
      oldParametersList.add(oldParamter);
    }
    return oldParametersList;
  }

  @Override
  @MicroService(beanName="tmpDepositReconciliationHandler",
      methodName="virtualAccountTransfer",
      sync=true,
      vhostName="/business",
      exchangeName="exchange-business",
      routingKey = "tmp-deposit-reconciliation")
  public boolean virtualAccountTransfer(List<TmpDepositReconciliationParameters> list) {
    List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters> oldParametersList = copyParametersList(list);
    return dstJobTmpDepositReconciliation.virtual_account_transfer(oldParametersList);
  }

  @Override
  @MicroService(beanName="tmpDepositReconciliationHandler",
      methodName="recoverDetails",
      sync=true,
      vhostName="/business",
      exchangeName="exchange-business",
      routingKey = "tmp-deposit-reconciliation")
  public boolean recoverDetails(List<TmpDepositReconciliationParameters> list) {
    List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters> oldParametersList = copyParametersList(list);
    return dstJobTmpDepositReconciliation.recover_details(oldParametersList);
  }

  @Override
  @MicroService(beanName="tmpDepositReconciliationHandler",
      methodName="unfreezeCapital",
      sync=true,
      vhostName="/business",
      exchangeName="exchange-business",
      routingKey = "tmp-deposit-reconciliation")
  public boolean unfreezeCapital(List<TmpDepositReconciliationParameters> list) {
    List<com.zufangbao.wellsfargo.silverpool.cashauditing.parameters.TmpDepositReconciliationParameters> oldParametersList = copyParametersList(list);
    return dstJobTmpDepositReconciliation.unfreeze_capical(oldParametersList);
  }
}
