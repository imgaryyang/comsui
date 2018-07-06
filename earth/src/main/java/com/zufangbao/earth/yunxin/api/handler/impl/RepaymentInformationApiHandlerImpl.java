package com.zufangbao.earth.yunxin.api.handler.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suidifu.coffer.entity.cmb.UnionPayBankCodeMap;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.earth.yunxin.api.handler.RepaymentInformationApiHandler;
import com.zufangbao.earth.yunxin.handler.SystemOperateLogHandler;
import com.zufangbao.earth.yunxin.handler.impl.deduct.notify.v2.DeductBusinessNotifyJobServer;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.IpUtil;
import com.zufangbao.sun.entity.bank.Bank;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.ContractAccount;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.modify.RepaymentInformationModifyModel;
import com.zufangbao.sun.entity.security.Principal;
import com.zufangbao.sun.geography.entity.City;
import com.zufangbao.sun.geography.entity.Province;
import com.zufangbao.sun.geography.service.CityService;
import com.zufangbao.sun.geography.service.ProvinceService;
import com.zufangbao.sun.service.ContractAccountService;
import com.zufangbao.sun.service.CustomerService;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.log.LogFunctionType;
import com.zufangbao.sun.yunxin.log.LogOperateType;
import com.zufangbao.sun.yunxin.log.RecordLogCore;
import com.zufangbao.sun.yunxin.log.SystemOperateLog;
import com.zufangbao.sun.yunxin.service.BankService;
import com.zufangbao.sun.yunxin.service.SystemOperateLogService;
import com.zufangbao.sun.yunxin.service.repayment.UpdateRepaymentInformationLogService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.wellsfargo.yunxin.handler.v2.SignUpHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("RepaymentInformationApiHandler")
@Deprecated
public class RepaymentInformationApiHandlerImpl implements RepaymentInformationApiHandler {

    @Autowired
    private ContractAccountService contractAccountService;

    @Autowired
    private UpdateRepaymentInformationLogService updateRepaymentInformationLogService;

    @Autowired
    private BankService bankService;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FinancialContractService financialContractService;

    @Autowired
    private SystemOperateLogHandler systemOperateLogHandler;
    @Autowired
    private RecordLogCore recordLogCore;
    @Autowired
    private SystemOperateLogService systemOperateLogService;

    @Autowired
    private DeductBusinessNotifyJobServer deductBusinessNotifyJobServer;

    @Autowired
    private SignUpHandler signUpHandler;

    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;

    @Value("#{config['notifyserver.groupCacheJobQueueMap_group2']}")
    private String groupNameForChannelsSignUp;

    @Value("#{config['zhonghang.signTransType']}")
    private String signTransType;

    @Value("#{config['zhonghang.notifyUrlToSignUp']}")
    private String notifyUrlToSignUp;

    @Value("#{config['zhonghang.merId']}")
    private String zhonghangMerId;

    @Value("#{config['zhonghang.secret']}")
    private String zhonghangSecret;

    @Override
    public void modifyRepaymentInformation(RepaymentInformationModifyModel modifyModel, HttpServletRequest request, Contract contract) throws Exception {

        ContractAccount vaildContractAccount = contractAccountService.getTheEfficientContractAccountBy(contract);
        Customer customer = contract.getCustomer();
        String payerName = StringUtils.isNotBlank(modifyModel.getPayerName()) ? modifyModel.getPayerName() : vaildContractAccount.getPayerName();
        modifyRepaymentInfoByRule(payerName, modifyModel.getBankCode(), modifyModel.getBankAccount(), modifyModel.getBankName(), modifyModel.getBankProvince(),
                modifyModel.getBankCity(), vaildContractAccount, IpUtil.getIpAddress(request), null, modifyModel.getIdCardNum(), customer, modifyModel.getMobile());

    }


    @Override
    public void modifyRepaymentInfoByRule(String payerName, String bankCode, String bankAccount, String bankName, String provinceCode, String cityCode,
                                          ContractAccount vaildContractAccount, String ip, Principal principal, String idCardNum, Customer customer, String mobile) throws Exception {

        if (vaildContractAccount.getContract().isNotModifyingRepaymentInfo()) {
            throw new ApiException(ApiResponseCode.FAIL_TO_MODIFY);
        }
        contractAccountService.setContractAccountInValid(vaildContractAccount);//作废

        customer.setMobile(mobile);
        customerService.save(customer);
        String financialContractUuid = vaildContractAccount.getContract().getFinancialContractUuid();

        //变更信息同时进行签约
        generateAndSaveEditContractAccount(payerName, bankCode, bankAccount, bankName, provinceCode,
                cityCode, vaildContractAccount, ip, principal, idCardNum, financialContractUuid);

    }


    private void generateAndSaveEditContractAccount(String payerName, String bankCode,
                                                    String bankAccount, String bankName, String bankProvince,
                                                    String bankCity, ContractAccount vaildContractAccount, String ip,
                                                    Principal principal, String idCardNum, String financialContractUuid) throws Exception {

        ContractAccount editContractAccount = new ContractAccount(vaildContractAccount);

        checkModifyContentAndSetProvinceAndCity(bankCode, bankProvince, bankCity, editContractAccount);

        String unionBankCode = StringUtils.EMPTY;

        if (!signUpHandler.judgeZhongHangByfinancialContractCode(financialContractUuid)) {

            unionBankCode = UnionPayBankCodeMap.BANK_CODE_MAP.get(bankCode);
            editContractAccount.setBankCode(unionBankCode);
            editContractAccount.setStandardBankCode(bankCode);
        } else {
            unionBankCode = ZhonghangResponseMapSpec.ENERGENCY_BANK_NAME_FOR_SHORT.get(bankCode);
            editContractAccount.setBankCode(bankCode);
            editContractAccount.setStandardBankCode(unionBankCode);
        }


        editContractAccount.setPayerName(payerName);
//		editContractAccount.setBankCode(unionBankCode);
//		editContractAccount.setStandardBankCode(bankCode);

        if (StringUtils.isNotEmpty(idCardNum)) {
            editContractAccount.setIdCardNum(idCardNum);
        }
        editContractAccount.setPayAcNo(bankAccount);
        editContractAccount.setBank(bankName);
        editContractAccount.setFromDate(new Date());
        editContractAccount.setThruDate(DateUtils.MAX_DATE);
        //如果请求账户为空,则为接口修改用户账户信息,不添加日志
        if (principal != null) {
            //增加日志
            SystemOperateLog log = recordLogCore.insertNormalRecordLog(
                    principal.getId(), ip,
                    LogFunctionType.EDIT_PAYMENT_BANK_ACCOUNT, LogOperateType.UPDATE,
                    vaildContractAccount);
            log.setKeyContent(vaildContractAccount.getUuid());
            StringBuffer recordContentBuffer = new StringBuffer();
            recordContentBuffer.append("编辑还款账户信息");
            if (!vaildContractAccount.getPayerName().equals(editContractAccount.getPayerName())) {
                recordContentBuffer.append("，还款方账户名由【" + vaildContractAccount.getPayerName() + "】变更为【" + editContractAccount.getPayerName() + "】");
            }
            if (!vaildContractAccount.getPayAcNo().equals(editContractAccount.getPayAcNo())) {
                recordContentBuffer.append("，银行账户号由【" + vaildContractAccount.getPayAcNo() + "】变更为【" + editContractAccount.getPayAcNo() + "】");
            }
            if (!vaildContractAccount.getBank().equals(editContractAccount.getBank())) {
                recordContentBuffer.append("，账户开户行由【" + vaildContractAccount.getBank() + " 】变更为【 " + editContractAccount.getBank() + " 】");
            }
            if (!vaildContractAccount.getProvince().equals(editContractAccount.getProvince()) || !vaildContractAccount.getCity().equals(editContractAccount.getCity())) {
                recordContentBuffer.append("，开户行所在地由【" + vaildContractAccount.getProvince() + " " + vaildContractAccount.getCity() + "】" +
                        "变更为【" + editContractAccount.getProvince() + " " + editContractAccount.getCity() + "】");
            }
            if (!vaildContractAccount.getIdCardNum().equals(editContractAccount.getIdCardNum())) {
                recordContentBuffer.append("，账户身份证信息由【" + vaildContractAccount.getIdCardNum() + " 】变更为【 " + editContractAccount.getIdCardNum() + " 】");
            }
            log.setRecordContent(recordContentBuffer.toString());
            systemOperateLogService.saveOrUpdate(log);
        }
        contractAccountService.save(editContractAccount);

        //签约
        pushJobForSignUp(editContractAccount, deductBusinessNotifyJobServer, groupNameForChannelsSignUp, signTransType, notifyUrlToSignUp, ZhonghangResponseMapSpec.SIGN_UP);

    }

    private void pushJobForSignUp(ContractAccount contractAccount, DeductBusinessNotifyJobServer deductBusinessNotifyJobServer, String groupNameForChannelsSignUp, String signTransType, String notifyUrlToSignUp, String opType) {

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

        List<NotifyApplication> jobList = signUpHandler.pushJobFromImportAssetAndContract(sendToSignUpInterfaceParam, financialContract.getFinancialContractUuid(), financialContract.getContractNo(), notifyUrlToSignUp, zhonghangMerId, zhonghangSecret, signTransType, groupNameForChannelsSignUp, opType);

        deductBusinessNotifyJobServer.pushJobs(jobList);
    }


    public void checkModifyContentAndSetProvinceAndCity(String bankCode, String bankProvinceCode,
                                                        String bankCityCode, ContractAccount editContractAccount) {
        //判断或者yunxin
        if (!signUpHandler.judgeZhongHangByfinancialContractCode(editContractAccount.getContract().getFinancialContractUuid())) {
            Bank bank = bankService.getCachedBanks().get(bankCode);
            if (bank == null) {
                throw new ApiException(ApiResponseCode.NO_MATCH_BANK, "未知银行代码");
            }
        }
        Province province = provinceService.getProvinceByCode(bankProvinceCode);
        if (province == null) {
            throw new ApiException(ApiResponseCode.NO_MATCH_PROVINCE, "未知省份代码");
        }
        City city = cityService.getCityByCityCode(bankCityCode);
        if (city == null) {
            throw new ApiException(ApiResponseCode.NO_MATCH_CITY, "未知城市代码");
        }
        editContractAccount.setProvinceCode(bankProvinceCode);
        editContractAccount.setCityCode(bankCityCode);
        editContractAccount.setProvince(province.getName());
        editContractAccount.setCity(city.getName());
    }


}
