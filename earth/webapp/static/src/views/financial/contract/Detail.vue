<template>
    <div class="content" id="financialContractEdit">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: $utils.locale('financialContract') }, {title: $utils.locale('financialContract') + '详情'}]">
                <a href="javascript: void 0;" @click="redirectContract" class="btn btn-primary">贷款合同列表</a>
            </Breadcrumb>
            <div class="col-layout-detail">
                <div class="top">
                    <div class="block" >
                        <h5 class="hd">
                            合同基础信息
                            <router-link 
                                :to="{name: 'editFinancialBasic', params: {financialContractUuid: $route.params.financialContractUuid}}"
                                v-if="ifElementGranted('modify-contract-basic-info')">
                                [编辑]
                            </router-link>
                        </h5>
                        <div class="bd">
                            <div class="col" style="max-width: 500px">
                                <p>{{ $utils.locale('financialContract.name') }} ：{{ financialContract.financialContractName }}</p>
                                <p>信托合同简称 ：{{ financialContract.financialContractShortName }}</p>
                                <p>{{ $utils.locale('financialContract.no') }} ：{{ financialContract.financialContractNo }}</p>
                                <p>信托类型 ：{{ financialTypeZh }}</p>
                                <p>{{ $utils.locale('financialContract.type') }} ：{{ financialContractTypeZh }}</p>
                                <p>{{ $utils.locale('financialContract.deadline') }} ：{{ financialContract.advaStartDate | formatDate }} / {{ financialContract.thruDate | formatDate }}</p>
                                <p>信托公司 ：{{ financialContract.company.fullName }}</p>
                                <p>资产方 ：{{ financialContract.app.name }}</p>
                                <p>资金方 ：{{ convertMerchant2Name(financialContract.capitalParty) }}</p>
                                <p>其他合作商户 ：{{ convertMerchant2Name(financialContract.otherParty) }}</p>
                                <p>{{ $utils.locale('financialContract.account') }} ：{{ financialContract.capitalAccount.bankName }} {{ financialContract.capitalAccount.accountNo }} ({{ financialContract.capitalAccount.accountName }})</p>
                                <div style="margin: 0 0 10px;"><span class="pull-left">收付子专户 ：</span>
                                    <div style="overflow: hidden;">
                                        <p  v-for="account in subAccounts">
                                            {{account.bankName}} {{account.accountNo}} ({{account.accountName}})<br>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="block" >
                        <h5 class="hd">
                            放款信息
                            <router-link 
                                :to="{name: 'editFinancialRemittance', params: {financialContractUuid: $route.params.financialContractUuid}}"
                                v-if="ifElementGranted('modify-remittance-info')">
                                [编辑]
                            </router-link>
                        </h5>
                        <div class="bd">
                            <div class="col">
                                <p>单笔放款上限 ：{{ financialContract.transactionLimitPerTranscation }}万元</p>
                                <!-- <p>单日放款上限 ：{{ financialContract.transactionLimitPerDay }}万元</p> -->
                                <p>放款总额 ：{{ remittancetotalAmountFormat }} 元</p>
                                <p>头寸余额 ：{{ bankSavingLoanFormat }} 元</p>
                                <p>放款方式 ：{{ financialContract.remittanceStrategyMode | remittanceStrategyMode_Zh_CN }}</p>
                                <div style="margin: 0 0 10px;"><span class="pull-left">受托入款账户 ：</span>
                                    <div style="overflow: hidden;">
                                        <p  v-for="account in appAccounts">
                                            {{account.bankName}} {{account.accountNo}} ({{account.accountName}})<br>
                                        </p>
                                    </div>
                                </div>
                                <br/>
                                <p>二次放款 ：{{ allowModifyRemittanceApplication == 1 ? '开启' : '关闭' }}</p>
                                <p>放款对象 ：{{ financialContract.remittanceObject | remittanceObject_ZH_CN }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block" >
                        <h5 class="hd">
                            还款信息
                            <router-link 
                                :to="{name: 'editFinancialRepay', params: {financialContractUuid: $route.params.financialContractUuid}}"
                                v-if="ifElementGranted('modify-repayment-info')">
                                [编辑]
                            </router-link>
                        </h5>
                        <div class="bd">
                            <div class="col">
                                <p>还款类型 ：{{ assetPackageFormatZh }}</p>
                                <div style="display: flex">
                                    <span>还款模式 ：</span>
                                    <div>
                                        <p style="margin-bottom: 0" v-show="financialContract.allowOnlineRepayment == 1">线上还款：
                                            正常扣款(<span>{{financialContract.sysNormalDeductFlag ? '系统主动' : '接口请求'}}</span>)，
                                            逾期扣款(<span>{{financialContract.sysOverdueDeductFlag ? '系统主动' : '接口请求'}}</span>)
                                            <p style="margin-left: 5em" v-show="financialContract.allowAdvanceDeductFlag == 1">允许接口提前划扣</p>
                                        </p>
                                        <p v-show="financialContract.allowOfflineRepayment == 1">线下还款：</p>
                                    </div>
                                </div>
                                <p>设定还款时间生效 : {{planRepaymentTimeLock}}</p>
                                <p>还款宽限日 ：{{ financialContract.advaRepaymentTerm }}自然日</p>
                                <p>逾期转坏账 ：{{ financialContract.advaRepoTerm }}自然日</p>
                                <p>商户打款宽限日 ：{{ financialContract.advaMatuterm }}工作日</p>
                                <p>逾期费用生成方式 ：
                                    <span>{{ financialContract.sysCreatePenaltyFlag ? '系统生成' : '对手方传递' }}</span>
                                </p>
                                <p v-if="(financialContract.sysCreatePenaltyFlag && financialContract.penalty)
                                    || (!financialContract.sysCreatePenaltyFlag && financialContract.penalty == -1)">罚息 ：{{ financialContract.sysCreatePenaltyFlag ? financialContract.penalty : '对手方传递' }}</p>
                                <p v-if="(financialContract.sysCreatePenaltyFlag && financialContract.overdueDefaultFee)
                                    || (!financialContract.sysCreatePenaltyFlag && financialContract.overdueDefaultFee == -1)">逾期违约金 ：{{ financialContract.sysCreatePenaltyFlag ? financialContract.overdueDefaultFee + '元' : '对手方传递' }}</p>
                                <p v-if="(financialContract.sysCreatePenaltyFlag && financialContract.overdueServiceFee)
                                    || (!financialContract.sysCreatePenaltyFlag && financialContract.overdueServiceFee == -1)">逾期服务费 ：{{ financialContract.sysCreatePenaltyFlag ? financialContract.overdueServiceFee + '元' : '对手方传递' }}</p>
                                <p v-if="(financialContract.sysCreatePenaltyFlag && financialContract.overdueOtherFee)
                                    || (!financialContract.sysCreatePenaltyFlag && financialContract.overdueOtherFee == -1)">逾期其他费用 ：{{ financialContract.sysCreatePenaltyFlag ? financialContract.overdueOtherFee + '元' : '对手方传递' }}</p>

                                <div>逾期状态自动确认 :
                                    <span >
                                        未逾期自动确认 : {{ allowNotOverdueAutoConfirm == 1 ? '开启' : '关闭' }}
                                    </span>
                                    <p style="margin-left: 9em;">
                                        已逾期自动确认 : {{ allowOverdueAutoConfirm == "" ? '关闭' : `计划还款日期+还款宽限日+${allowOverdueAutoConfirm}天`}}
                                    </p>
                                </div>

                                <p>回购受理方式 ：{{ financialContract.repurchaseApproach }}</p>
                                <div v-if= "financialContract.repurchaseApproachOr == '1'" style="display: flex;">
                                    <span style="float:left">回购规则 ：</span> 
                                    <template v-if="financialContract.repurchaseRule == 0">
                                        <p style="float:left;margin-left:-4px">{{ financialContract.repurchaseRule | repurchaseRule_Zh_CN }} {{ financialContract.advaRepoTerm }} 日</p>
                                    </template>
                                    <template v-else>
                                        <p style="float:left">{{ financialContract.repurchaseRule | repurchaseRule_Zh_CN }} {{ repurchaseRuleMsg }}</p>
                                        <HelpPopover style="margin-left: 10px" :popoverWidth="400" :customClass="'el-icon-information'" v-if="repurchaseRuleMsg">
                                            <PagingTable :data="financialContract.temporaryRepurchases">
                                                <el-table-column label="临时回购时间" inline-template :context="_self">
                                                    <div>{{ row.repurchaseDate | formatDate }}</div>
                                                </el-table-column>
                                                <el-table-column label="有效期" inline-template :context="_self">
                                                    <div>{{ row.effectStartDate | formatDate }} ~ {{ row.effectEndDate | formatDate }}</div>
                                                </el-table-column>
                                            </PagingTable>
                                        </HelpPopover>
                                    </template>
                                </div>
                                <div v-if= "financialContract.repurchaseApproachOr != '0'" style="display:flex">
                                    <span>回购算法 ：</span> 
                                    <div>
                                        <p>回购本金：{{ financialContract.repurchasePrincipalAlgorithm ? financialContract.repurchasePrincipalAlgorithm : '空' }}</p>
                                        <p>回购利息：{{ financialContract.repurchaseInterestAlgorithm ? financialContract.repurchaseInterestAlgorithm : '空' }}</p>
                                        <p>回购罚息：{{ financialContract.repurchasePenaltyAlgorithm ? financialContract.repurchasePenaltyAlgorithm : '空' }}</p>
                                        <p>回购其他费用：{{ financialContract.repurchaseOtherChargesAlgorithm ? financialContract.repurchaseOtherChargesAlgorithm : '空' }}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row-layout-detail">
                <div class="block">
                    <SystemOperateLog :for-object-uuid="$route.params.financialContractUuid" ref="syslog"></SystemOperateLog>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import { ajaxPromise,searchify,purify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { mapState } from 'vuex';
    import Enum from 'assets/javascripts/enum';
    import formats from 'filters/format';
    import HelpPopover from 'views/include/HelpPopover';
    import PagingTable from 'views/include/PagingTable';
    const REPURCHASE_RULE = new Enum([
        {0: '坏账回购'},
        {1: '不定期'}
    ]);
    const REMITTANCE_STRATEGY_MODE = new Enum([
        {0: '单向放款'},
        {1: '双向放款'},
        {2: '放扣联动'},
        {3: '线下放款'},
        {4: '无'}
    ]);
    const REMITTANCE_OBJECT = new Enum([
        {0: '贷款人'},
        {1: '供应商'},
        {2: '其他'},
    ]);
    export default {
        components: { SystemOperateLog, HelpPopover,PagingTable },
        data: function() {
            return {
                fetching: false,
                appAccounts: [],
                financialContract: {
                    app: {},
                    capitalAccount: {},
                    company: {},
                    paymentChannel: {}
                },
                allowModifyRemittanceApplication: '',
                allowNotOverdueAutoConfirm: '',
                allowOverdueAutoConfirm: '',

                remittancetotalAmount: 0,
                bankSavingLoan: 0,
                planRepaymentTimeLock: ''
            };
        },
        computed: {
            ...mapState({
                appList: state => state.financialContract.appList,
                assetPackageFormats: state => state.financialContract.assetPackageFormat,
                financialContractTypes: state => state.financialContract.financialContractType,
                financialTypes: state => state.financialContract.financialType
            }),
            assetPackageFormatZh: function() {
                var types = this.assetPackageFormats;
                var index = types.findIndex(item => item.key == this.financialContract.assetPackageFormat);
                return index != -1 ? types[index].value : '';
            },
            financialContractTypeZh: function() {
                var types = this.financialContractTypes;
                var index = types.findIndex(item => item.key == this.financialContract.financialContractType);
                return index != -1 ? types[index].value : '';
            },
            financialTypeZh: function() {
                var types = this.financialTypes;
                var index = types.findIndex(item => item.key == this.financialContract.financialType);
                return index != -1 ? types[index].value : '';
            },
            repurchaseRuleMsg: function() {
                var result = '';
                var { repurchaseCycle, daysOfCycle} = this.financialContract;
                if (daysOfCycle && daysOfCycle.length) {
                    var daysOfCycleList = formats.sortArrayList(daysOfCycle);
                    result += daysOfCycleList.join();
                }
                return repurchaseCycle == 1 ? '每月' + result + '日' : repurchaseCycle == 2 ? '每周星期' + result : '';
            },
            remittancetotalAmountFormat: function() {
                return this.remittancetotalAmount == undefined ? '--' : formats.formatMoney(this.remittancetotalAmount)
            },
            bankSavingLoanFormat: function() {
                return this.bankSavingLoan == undefined ? '--' : formats.formatMoney(this.bankSavingLoan)
            }
        },
        activated: function() {
            const { $store } = this;
            $store.dispatch('getAssetPackageFormat');
            $store.dispatch('getFinancialContractType');
            $store.dispatch('getAppList');
            $store.dispatch('getFinancialType');
            this.fetch(this.$route.params.financialContractUuid);
            this.$refs.syslog.fetch();
        },
        methods: {
            fetch: function(financialContractUuid) {
                this.fetching = true;
                return ajaxPromise({
                    url: `/financialContract/detail/data`,
                    data: { financialContractUuid }
                }).then(data => {
                    this.financialContract = data.financialContract;
                    this.appAccounts = data.appAccounts;
                    this.subAccounts = data.subAccounts;
                    this.allowModifyRemittanceApplication = data.allowModifyRemittanceApplication || 0;
                    this.allowNotOverdueAutoConfirm = data.allowNotOverdueAutoConfirm || 0;
                    this.allowOverdueAutoConfirm = data.allowOverdueAutoConfirm || 0;
                    this.financialContract.temporaryRepurchases.forEach((item, index) => {
                        item.effectEndDate = formats.formatDate(item.effectEndDate);
                        item.effectStartDate = formats.formatDate(item.effectStartDate);
                        item.repurchaseDate = formats.formatDate(item.repurchaseDate);
                    });

                    this.remittancetotalAmount = data.remittancetotalAmount;
                    this.bankSavingLoan = data.bankSavingLoan;
                    this.planRepaymentTimeLock = data.planRepaymentTimeLock;

                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                })
            },
            redirectContract: function() {
                var attr = {
                    financialContractUuids: [this.financialContract.financialContractUuid],
                    isRedirect: true
                };
                var search =  searchify(purify(attr));
                location.assign(encodeURI(`${this.ctx}#/data/contracts?${search}`));
            },
            convertMerchant2Name: function(str) {
                var list = [];
                try {
                    if (str != null) {
                        list = JSON.parse(str)
                    }
                } catch (err) {
                    console.error(err)
                }
                if (list instanceof Array) {
                    var result = list.map(key => {
                        var i = this.appList.findIndex(item => item.key == key)
                        if (i != -1) {
                            return this.appList[i].value
                        }
                        return key
                    })
                    return result.join(', ')
                }
                return list
            }
        },
        filters: {
            remittanceStrategyMode_Zh_CN: Enum.filter(REMITTANCE_STRATEGY_MODE),
            repurchaseRule_Zh_CN: Enum.filter(REPURCHASE_RULE),
            remittanceObject_ZH_CN: Enum.filter(REMITTANCE_OBJECT)
        }
    }
</script>
