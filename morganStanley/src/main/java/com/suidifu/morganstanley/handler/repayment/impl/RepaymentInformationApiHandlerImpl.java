package com.suidifu.morganstanley.handler.repayment.impl;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.entity.cmb.UnionPayBankCodeMap;
import com.suidifu.morganstanley.configuration.bean.zhonghang.ZhongHangProperties;
import com.suidifu.morganstanley.handler.repayment.RepaymentInformationApiHandler;
import com.suidifu.morganstanley.model.request.repayment.ModifyRepaymentInformation;
import com.suidifu.morganstanley.servers.MorganStanleyNotifyServer;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.gluon.api.earth.v3.model.ApiMessage;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.util.CardBinUtil;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.modify.UpdateRepaymentInformationLog;
import com.zufangbao.sun.geography.entity.City;
import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.CityService;
import com.zufangbao.sun.geography.service.ProvinceService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.service.BankService;
import com.zufangbao.sun.yunxin.service.CardBinService;
import com.zufangbao.sun.yunxin.service.repayment.UpdateRepaymentInformationLogService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.wellsfargo.yunxin.handler.v2.SignUpHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("RepaymentInformationApiHandler")
public class RepaymentInformationApiHandlerImpl implements RepaymentInformationApiHandler {
    @Resource
    private UpdateRepaymentInformationLogService updateRepaymentInformationLogService;

    @Resource
    private ContractAccountService contractAccountService;

    @Resource
    private BankService bankService;

    @Resource
    private ProvinceService provinceService;

    @Resource
    private CityService cityService;

    @Resource
    private CustomerService customerService;

    @Resource
    private FinancialContractService financialContractService;

    @Resource
    private MorganStanleyNotifyServer morganStanleyNotifyServer;

    @Resource
    private SignUpHandler signUpHandler;

    @Resource
    private ZhongHangProperties zhongHang;

    @Resource
    private CardBinService cardBinService;

    @Override
    public void modifyRepaymentInformation(ModifyRepaymentInformation modifyModel,
                                           Contract contract) {
        ContractAccount validContractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
        Customer customer = customerService.getCustomer(contract.getCustomerUuid());

        modifyModel.setPayerName(StringUtils.isNotBlank(modifyModel.getPayerName()) ? modifyModel.getPayerName() : validContractAccount.getPayerName());

        modifyRepaymentInfoByRule(modifyModel, validContractAccount, customer);
    }

    private void modifyRepaymentInfoByRule(ModifyRepaymentInformation modifyModel,
                                           ContractAccount validContractAccount,
                                           Customer customer) {
        if (validContractAccount.getContract().isNotModifyingRepaymentInfo()) {
            throw new ApiException(ApiMessage.FAIL_TO_MODIFY);
        }
        //作废
        contractAccountService.setContractAccountInValid(validContractAccount);

        customer.setMobile(modifyModel.getMobile());
        customerService.save(customer);

        //变更信息同时进行签约
        generateAndSaveEditContractAccount(modifyModel, validContractAccount);
    }

    private void generateAndSaveEditContractAccount(ModifyRepaymentInformation modifyModel,
                                                    ContractAccount validContractAccount) {
        String financialContractUuid = validContractAccount.getContract().getFinancialContractUuid();
        String bankCode = modifyModel.getBankCode();
        String idCardNum = modifyModel.getIdCardNum();
        ContractAccount editContractAccount = new ContractAccount(validContractAccount);

        // 如果bankCode 是空的话 尝试自己进行补充全
        if (StringUtils.isEmpty(bankCode)) {
            Map<String, String> cardBinBankCodeMap = cardBinService.getCachedCardBins();
            String account = modifyModel.getBankAccount();
            bankCode = CardBinUtil.getBankCodeViaCardNo(cardBinBankCodeMap, account);
        }

        //判断zhonghang还是yunxin
        if (!signUpHandler.judgeZhongHangByfinancialContractCode(editContractAccount.getContract().getFinancialContractUuid())) {
            Bank bank = bankService.getCachedBanks().get(bankCode);

            if (bank == null) {
                throw new ApiException(ApiMessage.NO_MATCH_BANK);
            }
        }
        Province province = provinceService.getProvinceByCode(modifyModel.getBankProvince());
        City city = cityService.getCityByCityCode(modifyModel.getBankCity());
        if (province == null) {
            throw new ApiException(ApiMessage.NO_MATCH_PROVINCE);
        }
        if (city == null) {
            throw new ApiException(ApiMessage.NO_MATCH_CITY);
        }

        editContractAccount.setProvinceCode(modifyModel.getBankProvince());
        editContractAccount.setCityCode(modifyModel.getBankCity());
        editContractAccount.setProvince(province.getName());
        editContractAccount.setCity(city.getName());

        String unionBankCode;

        if (!signUpHandler.judgeZhongHangByfinancialContractCode(financialContractUuid)) {
            unionBankCode = UnionPayBankCodeMap.BANK_CODE_MAP.get(bankCode);
            editContractAccount.setBankCode(unionBankCode);
            editContractAccount.setStandardBankCode(bankCode);
        } else {
            unionBankCode = ZhonghangResponseMapSpec.ENERGENCY_BANK_NAME_FOR_SHORT.get(bankCode);
            editContractAccount.setBankCode(bankCode);
            editContractAccount.setStandardBankCode(unionBankCode);
        }

        editContractAccount.setPayerName(modifyModel.getPayerName());

        if (StringUtils.isNotEmpty(idCardNum)) {
            editContractAccount.setIdCardNum(idCardNum);
        }
        editContractAccount.setPayAcNo(modifyModel.getBankAccount());
        editContractAccount.setBank(modifyModel.getBankName());
        editContractAccount.setFromDate(new Date());
        editContractAccount.setThruDate(DateUtils.MAX_DATE);

        contractAccountService.save(editContractAccount);

        //签约
        pushJobForSignUp(editContractAccount, zhongHang, ZhonghangResponseMapSpec.SIGN_UP);
    }

    private void pushJobForSignUp(ContractAccount contractAccount, ZhongHangProperties zhongHang, String
            opType) {
        if (!signUpHandler.judgeZhongHangByfinancialContractCode(contractAccount.getContract().getFinancialContractUuid())) {
			return;
        }

        Contract contract = contractAccount.getContract();
        Customer customer = contract.getCustomer();
        FinancialContract financialContract = financialContractService.getFinancialContractBy(contract.getFinancialContractUuid());

        if (financialContract == null || customer == null) {
            return;
        }

        Map<String, String> sendToSignUpInterfaceParam = new HashMap<>();
        sendToSignUpInterfaceParam.put("accName", contractAccount.getPayerName());
        sendToSignUpInterfaceParam.put("accNo", contractAccount.getPayAcNo());
        sendToSignUpInterfaceParam.put("certId", contractAccount.getIdCardNum());
        sendToSignUpInterfaceParam.put("phoneNo", customer.getMobile() == null ? StringUtils.EMPTY : customer.getMobile());
        sendToSignUpInterfaceParam.put("bankAliasName", contractAccount.getBankCode());
        sendToSignUpInterfaceParam.put("bankFullName", StringUtils.EMPTY);

        List<NotifyApplication> jobList = signUpHandler.pushJobFromImportAssetAndContract(sendToSignUpInterfaceParam, financialContract.getFinancialContractUuid(),
                financialContract.getContractNo(), zhongHang.getNotifyUrlToSignUp(), zhongHang.getMerId(), zhongHang.getSecret(), zhongHang.getSignTransType(),
                zhongHang.getSignUpGroupName(), opType);

        morganStanleyNotifyServer.pushJobs(jobList);
    }

    @Override
    public void saveLog(ModifyRepaymentInformation information, Contract contract, String ip) {
        UpdateRepaymentInformationLog log = new UpdateRepaymentInformationLog();
        BeanUtils.copyProperties(information, log);
        log.setContract(contract);
        log.setRequestData(JsonUtils.toJsonString(information));
        log.setCreateTime(new Date());
        log.setIp(ip);

        updateRepaymentInformationLogService.save(log);
    }

    @Override
    public void checkRequestNo(String requestNo) {
        updateRepaymentInformationLogService.checkRequestNo(requestNo);
    }
}