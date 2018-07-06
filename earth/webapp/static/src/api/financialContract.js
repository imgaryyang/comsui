import { ajaxPromise } from 'assets/javascripts/util';

// 信托合同列表
export function getFinancialContractQueryModels() {
    return ajaxPromise({
        url: '/financialContract/financialContractList',
        parse: data => data.queryAppModels
    });
}

// 信托公司列表
export function getCompanyList() {
    return ajaxPromise({
        url: '/financialContract/companyList',
        parse: data => data.companyList
    });
}

// 合作商户列表
export function getAppList() {
    return ajaxPromise({
        url: '/financialContract/applist',
        parse: data => data.appList
    });
}

// 信托合同类型列表
export function getFinancialContractType() {
    return ajaxPromise({
        url: '/financialContract/financialContractType',
        parse: data => {
            return data.financialContractType.map(function(item) {
                return {
                    key: item.key + '', // 由于select的v-model对数字0和''匹配为相等，强制转为string
                    value: item.value
                }; 
            });
        }
    });
}

// 放款模式列表
export function getRemittanceStrategyMode() {
    return ajaxPromise({
        url: '/financialContract/remittanceStrategyMode',
        parse: data => data.remittanceStrategyMode
    });
}

// 还款类型列表
export function getAssetPackageFormat() {
    return ajaxPromise({
        url: '/financialContract/assetPackageFormat',
        parse: data => {
            return data.assetPackageFormat.map(function(item) {
                return {
                    key: item.key + '', // 由于select的v-model对数字0和''匹配为相等，强制转为string
                    value: item.value
                };
            });
        }
    });
}

// 费用名目列表
export function getFeeType() {
    return ajaxPromise({
        url: '/financialContract/feeType',
        parse: data => data.feeType
    });
}

// 银行名称列表
export function getBankNames() {
    return ajaxPromise({
        url: '/financialContract/bankNames',
        parse: data => data.bankNames
    });
}

// 商户账户列表
export function getAppAccountList(financialContractUuid) {
    return ajaxPromise({
        url: '/financialContract/appAccountList',
        data: { financialContractUuid },
        parse: data => data.appAccountList
    });
}

// 凭证类型列表
export function getVoucherTypes() {
    return ajaxPromise({
        url: '/voucher/active/voucherTypes',
        parse: data => data.voucherTypes
    });
}

//不定期类型
export function getRepurchaseCycles() {
    return ajaxPromise({
        url: '/financialContract/repurchaseCycle',
        parse: data => data.repurchaseCycle
    });
}

//合同类型
export function getFinancialType() {
    return ajaxPromise({
        url: '/financialContract/financialType',
        parse: data => data.financialType
    });
}
