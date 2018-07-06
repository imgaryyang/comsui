<style lang="sass">
    .select-special_acount {
        position: relative;
        height: 50px;
        background-color: #eee;
        border-bottom: 1px solid #dedede;
        padding: 0;
        white-space: nowrap;
        z-index: 1;
    }
    .at-container {
        overflow-x: auto;
        position: absolute;
        top: 0;
        padding-top: 50px;
        box-sizing: border-box;
        width: 100%;
        height: 100%;
    }
    .at-content {
        min-width: 1480px;
        position: relative;
        background-color: #fff;
        float: left;
        width: 100%;
        height: 100%;
        padding: 20px 600px 20px 20px;
        .left-adapt {
            position: relative;
            border: 1px solid #e7e8e9;
            height: 100%;
            background-color: #eee;
            padding: 30px;
            .left-skip, .right-skip {
                position: absolute;
                top: 50%;
                display: block;
                background-color: #fff;
                color: #999;
                font-size: 12px;
                width: 10px;
                height: 40px;
                line-height: 40px;
                cursor: pointer;
                border: 1px solid #e7e8e9;
            }
            .left-skip {
                left: -1px;
                border-left: none;
                border-bottom-right-radius: 20px;
                border-top-right-radius: 20px;
                &:before {
                    margin-left: -3px;
                }
            }
            .right-skip {
                right: -1px;
                border-right: none;
                border-bottom-left-radius: 20px;
                border-top-left-radius: 20px;
            }
            .left-scroll {
                position: relative;
                width: 100%;
                height: 100%;
                overflow-x: hidden;
                overflow-y: auto;
            }
            .left-chart {
                position: relative;
                width: 100%;
                height: 100%;
                overflow-y: auto;
                overflow-x: hidden;
            }
            .chart-title {
                position: absolute;
                background-color: #eee;
                line-height: 50px;
                height: 50px;
                font-size: 24px;
                z-index: 1;
                width: 100%;
                margin: 0;
                text-align: center;
                padding-bottom: 20px;
            }
            .second-select {
                width: 100%;
                line-height: 40px;
                background-color: #fff;
                margin-bottom: 2px;
                border-width: 1px 1px 1px 0;
                border-style: solid;
                border-color: #e7e8e9;
                .el-select {
                    width: 25%;
                    float: left;
                    .el-input__inner {
                        height: 40px;
                        line-height: 40px;
                        border: none;
                        border-left: 1px solid #e7e8e9;
                        border-radius: 0;
                    }
                }
            }
            .list-query {
                padding: 0;
                background-image: none;
                box-sizing: border-box;
                border: 1px solid #e7e8e9;
                border-left: none;
            }
            .list-table {
                margin-bottom: 0;
                border-left: 1px solid #e7e8e9;
                .list-hd {
                    padding: 10px;
                    background-color: #fff;
                    position: relative;
                    border: 1px solid #e7e8e9;
                    border-bottom: none;
                    .el-button--small {
                        position: relative;
                        top: 50%;
                        margin-top: -12px;
                        border-radius: 1px;
                        padding: 5px 10px;
                    }
                }
            }
            .list-operation {
                border: 1px solid #e7e8e9;
                box-sizing: border-box;
                background-color: #fff;
                position: relative;
            }
            .list-systemoperatelog {
                margin-top: 2px;
            }
        }
        .at-aside {
            background-color: #fff;
            position: absolute;
            top: 0;
            right: 0;
            width: 600px;
            height: 100%;
            padding: 20px 15px;
        }
    }
</style>
<template>
    <div class="content">
        <div class="select-special_acount query-area">
            <el-form class="sdf-form sdf-query-form" :inline="true">
                <el-form-item>
                    <el-select
                        v-model="queryConds.fstLevelContractUuid"
                        placeholder="信托专户选择"
                        size="small">
                        <el-option
                            v-for="item in fstSpecialAccounts"
                            :label="item.financialContractName"
                            :value="item.fstLevelContractUuid">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button :disabled="!queryConds.fstLevelContractUuid" @click="getAccountList" size="small" type="primary">查询</el-button>
                </el-form-item>
                <el-form-item class="pull-right">
                    <el-button :disabled="!queryConds.fstLevelContractUuid || noData" @click="edit" size="small" type="primary">编辑</el-button>
                </el-form-item>
            </el-form>
        </div>
        <div class="at-container" v-if="!noData">
            <div class="at-content">
                <div class="left-adapt">
                    <span class="el-icon-caret-right left-skip" v-if="!isChart" @click="isChart=!isChart"></span>
                    <div class="left-chart" v-if="isChart" v-loading="echartsLoading">
                        <h5 class="chart-title"><span v-if="queryConds.fstLevelContractUuid">{{showSpecialfinancialContractName}}饼状统计图</span></h5>
                        <EchartsPieBox
                            v-if="showChart"
                            :options="options"
                            :styles="styles">
                        </EchartsPieBox>
                        <div v-else style="padding-top: 70px; background: #fff; width: 100%; height: 100%;">
                            <div style="line-height: 50px; color: #666; text-align: center">暂无数据</div>
                        </div>
                    </div>
                    <div class="left-scroll" v-if="!isChart">
                        <div class="second-select clearfix">
                            <el-select
                                v-model="queryConds.uuid"
                                placeholder="暂存户"
                                size="small">
                                <el-option
                                    v-for="item in specialAccountForPendingList"
                                    :label="item.accountName"
                                    :value="item.uuid">
                                </el-option>
                            </el-select>
                            <el-select
                                v-model="queryConds.uuid"
                                placeholder="放款户"
                                size="small">
                                <el-option
                                    v-for="item in specialAccountForRemittanceList"
                                    :label="item.accountName"
                                    :value="item.uuid">
                                </el-option>
                            </el-select>
                            <el-select
                                v-model="queryConds.uuid"
                                placeholder="还款户"
                                size="small">
                                <el-option
                                    v-for="item in specialAccountForRepaymentList"
                                    :label="item.accountName"
                                    :value="item.uuid">
                                </el-option>
                            </el-select>
                            <el-select
                                v-model="queryConds.uuid"
                                placeholder="计提户"
                                size="small">
                                <el-option
                                    v-for="item in specialAccountForAccrualList"
                                    :label="item.accountName"
                                    :value="item.uuid">
                                </el-option>
                            </el-select>
                        </div>
                        <div class="list-query query-area">
                            <el-form class="sdf-form sdf-query-form" :inline="true">
                                <el-form-item>
                                    <el-select
                                        v-model="queryConds.accountSide"
                                        placeholder="借贷标记"
                                        size="small"
                                        multiple>
                                        <el-select-all-option
                                            :options="accountSideList">
                                        </el-select-all-option>
                                        <el-option
                                            v-for="item in accountSideList"
                                            :label="item.value"
                                            :value="item.key">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item>
                                    <el-select
                                        v-model="queryConds.accountTransType"
                                        placeholder="交易类型"
                                        size="small"
                                        multiple>
                                        <el-select-all-option
                                            :options="accountTransTypeList">
                                        </el-select-all-option>
                                        <el-option
                                            v-for="item in accountTransTypeList"
                                            :label="item.value"
                                            :value="item.key">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                                <el-form-item>
                                    <DatePicker
                                        v-model="happenDateRange"
                                        size="small"
                                        type="datetimerange"
                                        placeholder="发生时间">
                                    </DatePicker>
                                </el-form-item>
                                <el-form-item>
                                    <el-input size="small" placeholder="对方账户名称" v-model="queryConds.counterAccountName"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <el-button @click="queryfetch" size="small" :disabled="!queryConds.uuid" type="primary">查询</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                        <div class="list-table clearfix table-area">
                            <div class="list-hd clearfix">
                                <b>{{ thirdAccountName }}流水</b>
                                <div class="pull-right">
                                    <el-button size="small" v-if="false" type="primary">注资</el-button>
                                    <el-button
                                        size="small"
                                        @click="showTransferModal"
                                        v-if="isRepayment"
                                        :disabled="disabledTransfer"
                                        type="primary">转账</el-button>
                                    <el-button size="small" v-if="false" type="primary">提现</el-button>
                                </div>
                            </div>
                            <el-table
                                stripe
                                class="no-table-bottom-border"
                                v-loading="dataSource.fetching"
                                :data="dataSource.list">
                                <el-table-column label="账户流水号" prop="accountFlowNo">
                                </el-table-column>
                                <el-table-column label="发生时间" inline-template>
                                    <div>{{ row.happenDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                                </el-table-column>
                                <el-table-column label="借贷标记" prop="accountSide">
                                </el-table-column>
                                <el-table-column label="对方账户名称" prop="counterAccountName">
                                </el-table-column>
                                <el-table-column label="交易类型" prop="accountTransType">
                                </el-table-column>
                                <el-table-column label="发生金额" inline-template>
                                    <div>{{ row.transactionAmount | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column label="瞬时金额" inline-template>
                                    <div>{{ row.balance | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column label="银行流水" prop="bankSequenceNo" inline-template>
                                    <a :href="`${ctx}#/capital/special-account/cash-flow-audit/${row.cashFlowUuid}/detail`">{{ row.bankSequenceNo }}</a>
                                </el-table-column>
                                <el-table-column label="备注" prop="remark">
                                </el-table-column>
                            </el-table>
                            <div class="list-operation operations clearfix">
                                <div class="pull-right">
                                    <PageControl
                                        v-model="pageConds.pageIndex"
                                        :size="dataSource.size"
                                        :per-page-record-number="pageConds.perPageRecordNumber">
                                    </PageControl>
                                </div>
                            </div>
                        </div>
                        <div class="list-systemoperatelog">
                            <SystemOperateLog
                                :title="`${thirdAccountName}操作日志`"
                                ref="sysLog"
                                :fetching="sysLogfetching"
                                :for-object-uuid="queryConds.uuid">
                            </SystemOperateLog>
                        </div>
                    </div>
                    <span class="el-icon-caret-left right-skip" v-if="isChart" @click="isChart=!isChart"></span>
                </div>
                <div class="at-aside">
                    <AccountAside :data="customerData" @query-amount="handlerAmountClick"></AccountAside>
                </div>
            </div>
        </div>
        <div class="at-container" v-else><div style="line-height: 50px; color: #666; text-align: center">暂无数据</div></div>
        <TransferModal
            v-model="transfermodal.visible"
            :model="transfermodal.model">
        </TransferModal>
    </div>
</template>
<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import EchartsPieBox from './include/echartsPieBox';
    import DatePicker from 'components/DatePicker';
    import format from 'filters/format';

    export default {
        mixins: [ListPage, Pagination],
        components: {
            AccountAside: require('./include/AccountAside'),
            SystemOperateLog: require('./include/CustomSystemOperateLog'),
            TransferModal: require('./include/TransferModal'),
            EchartsPieBox,
            DatePicker,
        },
        data() {
            return {
                action: `/capital/account-management-list/query`,
                autoload: false,

                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 6,
                },
                queryConds: {
                    fstLevelContractUuid: '',
                    uuid: '',
                    accountSide: [],
                    accountTransType: [],
                    startDate: '',
                    endDate: '',
                    counterAccountName: '',
                },

                isChart: true,
                showChart: false,
                echartsLoading: false,
                noData: true,

                sysLogfetching: false,

                fstSpecialAccounts: [],
                accountSideList: [],
                accountTransTypeList: [],

                showSpecialfinancialContractName: '',

                specialAccountForPending: {},
                specialAccountForPendingList: [],
                specialAccountForRemittance: {},
                specialAccountForRemittanceList: [],
                specialAccountForRepayment: {},
                specialAccountForRepaymentList: [],
                specialAccountForAccrual: {},
                specialAccountForAccrualList: [],
                specialAccountForAccount: {},
                specialAccountForAccountList: [],

                happenDateRange: [],

                transfermodal: {
                    visible: false,
                    model: {
                        hostAccountUuid: '',
                        secondAccountName: '',
                        thirdAccountName: ''
                    }
                },

                styles: {
                    width: '100%',
                    height: '100%',
                },
                options: {
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        right : '10%',
                        top : '20%',
                        icon: 'circle',
                        data:[]
                    },
                    series : [
                        {
                            name:'xx专户账户(xx项目)饼状统计图',
                            type:'pie',
                            radius : ['20%', '70%'],
                            center : ['45%', '55%'],
                            roseType : 'radius',
                            label: {
                              normal: {
                                  formatter: function({name, value}){
                                    return name+'\n'+format.formatMoney(value);
                                  },
                              }
                            },
                            data:[]
                        }
                    ],
                    color: ['#95b2e7','#e7cb95','#c7e795','#c795e7','#e79595','#c23531','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3'],
                    backgroundColor: '#fff',
                }
            }
        },
        activated: function() {
            this.getOptions();
        },
        watch: {
            happenDateRange: function(current){
                this.queryConds.startDate = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endDate = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            },
            'pageConds.pageIndex': function(){
                if(this.queryConds.uuid) {
                    this.fetch();
                }
            },
            'queryConds.fstLevelContractUuid': function(cur) {
                if(!cur) {
                    this.noData = true;
                }
            },
            fstSpecialAccounts: function(cur, pre) {
                if(cur.length !== pre.length) {
                    this.queryConds.fstLevelContractUuid = '';
                }
            }
        },
        computed: {
            customerData: function() {
                return Object.assign({}, {
                    specialAccountForPending: this.specialAccountForPending,
                    specialAccountForPendingList: this.specialAccountForPendingList,
                    specialAccountForRemittance: this.specialAccountForRemittance,
                    specialAccountForRemittanceList: this.specialAccountForRemittanceList,
                    specialAccountForRepayment: this.specialAccountForRepayment,
                    specialAccountForRepaymentList: this.specialAccountForRepaymentList,
                    specialAccountForAccrual: this.specialAccountForAccrual,
                    specialAccountForAccrualList: this.specialAccountForAccrualList,
                    specialAccountForAccount: this.specialAccountForAccount,
                    specialAccountForAccountList: this.specialAccountForAccountList,
                })
            },
            specialAccountName: function() {
                if(this.fstSpecialAccounts.length && this.queryConds.fstLevelContractUuid) {
                      return this.fstSpecialAccounts.filter(item => item.fstLevelContractUuid == this.queryConds.fstLevelContractUuid)[0].accountName;
                } else {
                    return 'XX专户账户'
                }
            },
            specialfinancialContractName: function() {
                if(this.fstSpecialAccounts.length && this.queryConds.fstLevelContractUuid) {
                      return this.fstSpecialAccounts.filter(item => item.fstLevelContractUuid == this.queryConds.fstLevelContractUuid)[0].financialContractName;
                } else {
                    return 'XX项目'
                }
            },
            allList: function() {
                return this.specialAccountForPendingList.concat(this.specialAccountForRemittanceList, this.specialAccountForRepaymentList, this.specialAccountForAccrualList)
            },
            thirdAccountName: function() {
                var list = this.allList.slice();
                if(list.length && this.queryConds.uuid) {
                    return list.filter(item => item.uuid == this.queryConds.uuid)[0].accountName;
                } else {
                    return '';
                }
            },
            isRepayment: function() {
                var uuidList = this.specialAccountForRepaymentList.map(item => item.uuid);
                return uuidList.includes(this.queryConds.uuid);
            },
            disabledTransfer: function() {
                return this.queryConds.uuid && this.allList.filter(item => item.uuid == this.queryConds.uuid)[0].balance ? false : true;
            }
        },
        methods: {
            queryfetch: function() {
                if (this.pageConds) {
                    this.pageConds.pageIndex = 1;
                }
                this.fetch();
                this.sysLogfetching = true;
            },
            getOptions: function() {
                ajaxPromise({
                    url: `/capital/account-management-list/show/options`
                }).then(data => {
                    this.fstSpecialAccounts = data.fstSpecialAccounts || [];
                    this.accountSideList = data.accountSideList || [];
                    this.accountTransTypeList = data.accountTransTypeList || [];

                    this.queryConds.accountSide = this.accountSideList.map(item => item.key);
                    this.queryConds.accountTransType = this.accountTransTypeList.map(item => item.key);
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            getAccountList: function(item) {
                this.echartsLoading = true;
                this.queryConds.uuid = '';
                ajaxPromise({
                    url: `/capital/account-management-list/account/name`,
                    data: {
                        fstLevelContractUuid: this.queryConds.fstLevelContractUuid
                    }
                }).then(data => {
                    this.noData = false;
                    this.specialAccountForPending = data.specialAccountForPending || {};
                    this.specialAccountForPendingList = data.specialAccountForPendingList || [];
                    this.specialAccountForRemittance = data.specialAccountForRemittance || {};
                    this.specialAccountForRemittanceList = data.specialAccountForRemittanceList || [];
                    this.specialAccountForRepayment = data.specialAccountForRepayment || {};
                    this.specialAccountForRepaymentList = data.specialAccountForRepaymentList || [];
                    this.specialAccountForAccrual = data.specialAccountForAccrual || {};
                    this.specialAccountForAccrualList = data.specialAccountForAccrualList || [];
                    this.specialAccountForAccount = data.specialAccountForAccount || {};
                    this.specialAccountForAccountList = data.specialAccountForAccountList || [];

                    this.dataSource.list = [];
                    this.dataSource.size = 0;
                    this.pageConds.pageIndex = 1;
                    this.showSpecialfinancialContractName = this.specialfinancialContractName;
                    this.fetchCharts();
                }).catch(message => {
                    this.noData = true;
                    MessageBox.open(message);
                }).then(() => {
                    this.echartsLoading = false;
                })
            },
            fetchCharts: function() {
                var data = [
                    {
                        value: this.specialAccountForPending.balance,
                        name: this.specialAccountForPending.accountName
                    },
                    {
                        value: this.specialAccountForRemittance.balance,
                        name: this.specialAccountForRemittance.accountName
                    },
                    {
                        value: this.specialAccountForRepayment.balance,
                        name: this.specialAccountForRepayment.accountName
                    },
                    {
                        value: this.specialAccountForAccrual.balance,
                        name: this.specialAccountForAccrual.accountName
                    },
                    {
                        value: this.specialAccountForAccount.balance,
                        name: this.specialAccountForAccount.accountName
                    }
                ];
                this.options.series[0].name = `${this.showSpecialfinancialContractName}饼状统计图`;
                this.options.series[0].data = data.filter(item => item.value != 0);
                if(this.options.series[0].data.length != 0) {
                    this.showChart = true;
                } else {
                    this.showChart = false;
                }
                this.options.legend.data = data.map(item => item.name);
            },
            showTransferModal: function() {
                var list = this.allList.slice();
                this.transfermodal.visible = true;
                this.transfermodal.model = Object.assign({}, {
                    hostAccountUuid: this.queryConds.uuid,
                    secondAccountName: this.specialAccountForRepayment.accountName,
                    thirdAccountName: this.thirdAccountName,
                    balance: list.filter(item => item.uuid == this.queryConds.uuid)[0].balance
                })
            },
            edit: function(){
                var id = this.fstSpecialAccounts.filter(item => item.financialContractName === this.showSpecialfinancialContractName)[0].fstLevelContractUuid;
                this.$router.push({
                    path: `/capital/special-account/account-manager/${id}/edit`,
                    query: {
                        title: this.showSpecialfinancialContractName
                    }
                })
            },
            handlerAmountClick: function(uuid) {
                this.isChart = false;
                this.queryConds.uuid = uuid;
                this.queryfetch();
            },
        },
    }
</script>