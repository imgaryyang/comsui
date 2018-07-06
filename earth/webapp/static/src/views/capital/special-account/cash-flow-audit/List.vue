<style lang="sass">
    .cashflow-audit-financialcontract-select {
        .el-select-dropdown__list{
            .el-select-dropdown__item{
                height: 76px;
                .selectpicker-content {
                    min-height: 50px;

                    .content {
                        overflow: hidden;
                        background: transparent;
                        padding-left: 50px;
                        min-width: 300px;
                    }

                    .content .title {
                        font-size: 14px;
                        white-space: nowrap;
                    }

                    .content .subtitle {
                        color: #999;
                        font-size: 12px;
                        white-space: nowrap;
                    }

                    .identification {
                        position: absolute;
                        top: 10px;
                    }

                    .identification img {
                        height: 36px; 
                        width: 36px;
                    }

                    .link {
                        color: #436ba7;
                        cursor: pointer;
                    }
                }

                &.selected {
                    .selectpicker-content {
                        .link {
                            color: #fff;
                        }   
                    }
                }
            }
        }
    }

    .app-arrive-item.active td {
        background-color: #fffcc7!important;
    }
</style>

<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <list-cascader
                            clearable
                            size="small"
                            multiple="0"
                            :defaultSelected="true"
                            :collection="financialContractQueryModels"
                            v-model="financialContractUuids"
                            :customItem="renderItem"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.accountSide"
                            size="small" 
                            placeholder="借贷标记"
                            clearable>
                            <el-option
                                v-for="item in accountSideList"
                                :value="item.key"
                                :label="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.auditStatus"
                            size="small" 
                            placeholder="对账状态"
                            clearable>
                            <el-option
                                v-for="item in auditStatusList"
                                :value="item.key"
                                :label="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.tradeStartTime"
                                    :end-date="queryConds.tradeEndTime"
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    placeholder="入账时间起始"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.tradeEndTime"
                                    :start-date="queryConds.tradeStartTime"
                                    :pickTime="true"
                                    :formatToMaximum="true"
                                    placeholder="入账时间终止"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="流水号" value="cashFlowNo"></el-option>
                            <el-option label="银行账户号" value="accountNo"></el-option>
                            <el-option label="账户姓名" value="accountName"></el-option>
                            <el-option label="摘要内容" value="transactionRemark"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table
                    class="no-table-bottom-border"
                    v-loading="dataSource.fetching"
                    @sort-change="onSortChange"
                    :data="dataSource.list"
                    stripe>
                    <el-table-column label="流水号" prop="bankSequenceNo" inline-template>
                        <a :href="`${ctx}#/capital/special-account/cash-flow-audit/${row.cashFlowUuid}/detail`">{{ row.bankSequenceNo }}</a>
                    </el-table-column>
                    <el-table-column label="借贷标志" prop="accountSideMsg"></el-table-column>
                    <el-table-column label="交易金额" prop="transactionAmount" sortable="custom" inline-template>
                        <div>{{ row.transactionAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="瞬时余额" prop="balance" sortable="custom" inline-template>
                        <div>{{ row.balance | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="银行账号" prop="counterAccountNo"></el-table-column>
                    <el-table-column label="账户姓名" prop="counterAccountName"></el-table-column>
                    <el-table-column label="入账时间" prop="transactionTime" sortable="custom" inline-template>
                        <div>{{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="摘要内容" prop="remark"></el-table-column>
                    <el-table-column label="对账状态" prop="auditStatus" inline-template>
                        <span :class="{
                            'color-warning': row.auditStatusMsg =='部分平账',
                            'color-danger': row.auditStatusMsg =='存疑'
                        }">{{ row.auditStatusMsg }}</span>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-left">
                <el-button size="small" style="background: #fff;" @click="handleExport">导出银行流水</el-button>
                <HelpPopover>
                    由于业务数据量过大，时间跨度最长为3*24小时整；<br/>若需更多数据，可联系数据维护人员。
                </HelpPopover>
            </div>
            <PageControl 
                v-model="pageConds.pageIndex"
                :size="dataSource.size"
                :per-page-record-number="pageConds.perPageRecordNumber">
            </PageControl>
        </div>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise, searchify } from 'assets/javascripts/util';
    import HelpPopover from 'views/include/HelpPopover';
    import MessageBox from 'components/MessageBox';
    import Vue from 'vue';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            HelpPopover,
            ComboQueryBox: require('views/include/ComboQueryBox'),
        },
        data: function() {
            return {
                action: '/capital/account-manager/cash-flow-audit/query',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                comboConds: {
                    cashFlowNo: '',
                    accountNo: '',
                    accountName: '',
                    transactionRemark: '',
                },
                sortConds: {
                    sortField: '',
                    isAsc: ''
                },
                queryConds: {
                    hostAccountNo: '',
                    accountSide: '',
                    auditStatus: '',
                    tradeStartTime: '',
                    tradeEndTime: ''
                },
                financialContractUuids: [],
                accountSideList: [],
                auditStatusList: [],
                customerTypeList: [],

                financialContractQueryModels: [],

                createBillModal: {
                    visible: false,
                    cashFlowUuid: '',
                    selectedCashFlowRef: null
                },

            };
        },
        watch: {
            financialContractUuids: function(arr){
                if(arr.length == 0){
                    this.queryConds.hostAccountNo = '';
                } else {
                    this.queryConds.hostAccountNo = arr[0].capitalAccount.accountNo;
                }
            }
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function(){
                return ajaxPromise({
                    url: `/capital/account-manager/cash-flow-audit/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.accountSideList = data.accountSideList || [];
                    this.auditStatusList = data.auditStatusList || [];
                    this.customerTypeList = data.customerTypeList || [];
                });
            },
            handleExport: function() {
                var {tradeStartTime, tradeEndTime} = this.queryConds;

                if (tradeStartTime && tradeEndTime) {
                    var detla = new Date(tradeEndTime) - new Date(tradeStartTime);
                    var maxDiddSecond = 3 * 24 * 60 * 60 * 1000;
                    if (detla > maxDiddSecond) {
                        MessageBox.open('时间跨度不允许超过3天！');
                        return;
                    }
                } else if (!tradeStartTime) {
                    MessageBox.open('请选择入账起止时间');
                    return;
                } else if (!tradeEndTime) {
                    MessageBox.open('请选择入账终止时间');
                    return;
                }

                var search = searchify(this.conditions);

                window.open(`${this.api}/report/export?reportId=12&${search}`);
            },
            renderItem: function(h,item){
                return (<div class={['selectpicker-content']}>
                  <div class={['identification']}>
                    <img src={`${this.resource}/images/bank-logo/bank_${ item.capitalAccount && item.capitalAccount.bankCode ? item.capitalAccount.bankCode.toLowerCase() : '' }.png`}></img>
                  </div>
                  <div class={['content']}>
                      <div class={['title']}>{ item.contractName }</div>
                      <div class={['title']}>{ item.capitalAccount && item.capitalAccount.markedAccountNo }</div>
                      <div class={['subtitle']}>
                          <span style="float: left">({ item.capitalAccount && item.capitalAccount.bankName })</span>
                      </div>
                  </div>
                </div>)
            },
        }
    }
</script>