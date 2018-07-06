import * as api from 'api/financialContract';
import * as types from '../mutationTypes';

const state = {
    financialContractQueryModels: [],
    companyList: [],
    appList: [],
    financialContractType: [],
    remittanceStrategyMode: [],
    assetPackageFormat: [],
    feeType: [],
    bankNames: [],
    appAccountList: [],
    voucherTypes: [],
    repurchaseStatusList: [],
    repurchaseCycles: [],
    financialType: []
};

const getters = {};

const actions = {
    getFinancialContractQueryModels: function({commit, state}, payload = {}) {
        if (state.financialContractQueryModels.length != 0 && !payload.force) return;

        api.getFinancialContractQueryModels()
            .then(data => {
                commit(types.GET_FINANCIAL_CONTRACT_QUERY_MODELS_SUCCESS, data);
            });
    },
    getCompanyList: function({commit, state}, payload = {}) {
        if (state.companyList.length != 0 && !payload.force) return;

        api.getCompanyList()
            .then(data => {
                commit(types.GET_COMPANY_LIST_SUCCESS, data);
            });
    },
    getAppList: function({commit, state}, payload = {}) {
        if (state.appList.length != 0 && !payload.force) return;

        api.getAppList()
            .then(data => {
                commit(types.GET_APP_LIST_SUCCESS, data);
            });
    },
    getFinancialContractType: function({commit, state}, payload = {}) {
        if (state.financialContractType.length != 0 && !payload.force) return;

        api.getFinancialContractType()
            .then(data => {
                commit(types.GET_FINANCIAL_CONTRACT_TYPE_SUCCESS, data);
            });
    },
    getRemittanceStrategyMode: function({commit, state}, payload = {}) {
        if (state.remittanceStrategyMode.length != 0 && !payload.force) return;

        api.getRemittanceStrategyMode()
            .then(data => {
                commit(types.GET_REMITTANCE_STRATEGY_MODE_SUCCESS, data);
            });
    },
    getAssetPackageFormat: function({commit, state}, payload = {}) {
        if (state.assetPackageFormat.length != 0 && !payload.force) return;

        api.getAssetPackageFormat()
            .then(data => {
                commit(types.GET_REPAYMENT_WAY_SUCCESS, data);
            });
    },
    getFeeType: function({commit, state}, payload = {}) {
        if (state.feeType.length != 0 && !payload.force) return;

        api.getFeeType()
            .then(data => {
                commit(types.GET_FEE_TYPE_SUCCESS, data);
            });
    },
    getBankNames: function({commit, state}, payload = {}) {
        if (state.bankNames.length != 0 && !payload.force) return;

        api.getBankNames()
            .then(data => {
                commit(types.GET_BANK_NAMES_SUCCESS, data);
            });
    },
    getAppAccountList: function({commit, state}, payload = {}) {
        if (state.appAccountList.length != 0 && !payload.force) return;

        api.getAppAccountList(payload.financialContractUuid)
            .then(data => {
                commit(types.GET_APP_ACCOUNT_LIST_SUCCESS, data);
            });
    },
    getVoucherTypes: function({commit, state}, payload = {}) { 
        if (state.voucherTypes.length != 0 && !payload.force) return;

        api.getVoucherTypes()
            .then(data => {
                commit(types.GET_VOUCHER_TYPES_SUCCESS, data);
            });
    },
    getRepurchaseCycles: function({commit, state}, payload = {}) {
        if (state.repurchaseCycles.length != 0 && !payload.force) return;

        api.getRepurchaseCycles()
            .then(data => {
                commit(types.GET_REPURCHASE_CYCLES_SUCCESS, data);
            });
    },
    getFinancialType: function({commit, state}, payload = {}) {
        if (state.financialType.length != 0 && !payload.force) return;

        api.getFinancialType()
            .then(data => {
                commit(types.GET_FINANCIAL_TYPE_SUCCESS, data);
            });
    }
};

const mutations = {
    [types.GET_FINANCIAL_CONTRACT_QUERY_MODELS_SUCCESS]: function(state, data) {
        state.financialContractQueryModels = data;
    },
    [types.GET_COMPANY_LIST_SUCCESS]: function(state, data) {
        state.companyList = data;
    },
    [types.GET_APP_LIST_SUCCESS]: function(state, data) {
        state.appList = data;
    },
    [types.GET_FINANCIAL_CONTRACT_TYPE_SUCCESS]: function(state, data) {
        state.financialContractType = data;
    },
    [types.GET_REMITTANCE_STRATEGY_MODE_SUCCESS]: function(state, data) {
        state.remittanceStrategyMode = data;
    },
    [types.GET_REPAYMENT_WAY_SUCCESS]: function(state, data) {
        state.assetPackageFormat = data;
    },
    [types.GET_FEE_TYPE_SUCCESS]: function(state, data) {
        state.feeType = data;
    },
    [types.GET_BANK_NAMES_SUCCESS]: function(state, data) {
        state.bankNames = data;
    },
    [types.GET_APP_ACCOUNT_LIST_SUCCESS]: function(state, data) {
        state.appAccountList = data;
    },
    [types.GET_VOUCHER_TYPES_SUCCESS]: function(state, data) {
        state.voucherTypes = data;
    },
    [types.GET_REPURCHASE_CYCLES_SUCCESS]: function(state, data) {
        state.repurchaseCycles = data;  
    },
    [types.GET_FINANCIAL_TYPE_SUCCESS]: function(state, data) {
        state.financialType = data;  
    },
};

export default {
    state,
    getters,
    actions,
    mutations
};