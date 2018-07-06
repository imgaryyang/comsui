package com.suidifu.dowjones.dao;

import com.suidifu.dowjones.vo.enumeration.RepurchaseMode;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;

import java.math.BigDecimal;
import java.util.List;

public interface ContractReportDAO {

    BigDecimal getOverdueUnearnedPrincipal(InputParameter inputParameter);

    BigDecimal getOverdueUnearnedPrincipalWithInterest(InputParameter inputParameter);

    BigDecimal getRemainingLoanPrincipal(InputParameter inputParameter);

    BigDecimal getRemainingLoanPrincipalWithInterest(InputParameter inputParameter);

    BigDecimal getRemittanceTotalAmountByMonth(StaticOverdueRateInputParameter staticOverdueRateInputParameter);

    BigDecimal getMonthlyOverdueLoanPrincipal(StaticOverdueRateInputParameter staticOverdueRateInputParameter);

    BigDecimal getMonthlyOverdueLoanPrincipalWithInterest(
            StaticOverdueRateInputParameter staticOverdueRateInputParameter);

    BigDecimal getMonthlyRemittanceRemainingLoanPrincipalWithInterest(
            StaticOverdueRateInputParameter staticOverdueRateInputParameter);

    List<String> getContractUuidsByOverdueAssetSet(InputParameter inputParameter, RepurchaseMode repurchaseMode);

    List<String> getContractUuidsByUnConfirmedAssetSet(InputParameter inputParameter, RepurchaseMode repurchaseMode);

    List<String> getUnclearContractUuidsBy(InputParameter inputParameter, RepurchaseMode repurchaseMode);

    List<String> getOverdueLoanPrincipalByMonthAndOverdueAssetSet(
            StaticOverdueRateInputParameter staticOverdueRateInputParameter, RepurchaseMode repurchaseMode);

    List<String> getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(
            StaticOverdueRateInputParameter staticOverdueRateInputParameter, RepurchaseMode repurchaseMode);

    List<String> getRemittanceUnclearLoansBy(StaticOverdueRateInputParameter staticOverdueRateInputParameter,
                                             RepurchaseMode repurchaseMode);

    BigDecimal getUnEarnedPrincipalFromLedgerBookBy(InputParameter inputParameter, List<String> contractUuids);

    BigDecimal getUnEarnedPrincipalInterestFromLedgerBookBy(InputParameter inputParameter, List<String> contractUuids);

    BigDecimal getUnEarnedPrincipalFromRepurchaseBy(List<String> contractUuids);

    BigDecimal getUnEarnedPrincipalInterestFromRepurchaseBy(List<String> contractUuids);

}
