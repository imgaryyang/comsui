<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <list-cascader
                            clearable
                            size="small"
                            :collection="financialContractQueryModels"
                            v-model="queryConds.financialContractIds"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.paymentGateway"
                            placeholder="支付通道"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="paymentGateway">
                            </el-select-all-option>
                            <el-option
                                v-for="item in paymentGateway"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.executionStatus"
                            placeholder="支付单状态"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="orderStatus">
                            </el-select-all-option>
                            <el-option
                                v-for="item in orderStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.receiveStartDate"
                                    :end-date="queryConds.receiveEndDate"
                                    size="small"
                                    placeholder="请输入变更起始日期">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.receiveEndDate"
                                    :start-date="queryConds.receiveStartDate"
                                    size="small"
                                    placeholder="请输入变更终止日期">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="支付单号" value="deductPlanNo"></el-option>
                            <el-option label="扣款单号" value="deductApplicationNo"></el-option>
                            <el-option label="账户姓名" value="customerName"></el-option>
                            <el-option label="银行账户号" value="bankAccountNo"></el-option>
                            <el-option label="银行名称" value="bankName"></el-option>
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
                    <el-table-column label="支付单号" inline-template>
                        <a :href="`${ctx}#/capital/payment-cash-flow/online-payment/${ row.deductPlanId }/detail`">{{ row.deductPlanNo }}</a>
                    </el-table-column>
                    <el-table-column label="扣款单号" prop="deductApplicationNo"></el-table-column>
                    <el-table-column label="银行名称" prop="bankName"></el-table-column>
                    <el-table-column label="账户姓名" prop="accountCustomerName"></el-table-column>
                    <el-table-column label="银行账户号" prop="bankAccountNo"></el-table-column>
                    <el-table-column label="代扣金额" prop="deductAmount" inline-template>
                        <div>{{ row.deductAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="创建时间" width="165px" prop="createTime" inline-template>
                        <div>{{ row.createTime }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" width="165px" prop="lastModifiedTime" inline-template>
                        <div>{{ row.lastModifiedTime }}</div>
                    </el-table-column>
                    <el-table-column label="支付通道" prop="paymentGateway"></el-table-column>
                    <el-table-column label="状态" prop="status"></el-table-column>
                    <el-table-column label="备注" prop="remark"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-left sdf-form sdf-query-form">
                <div class="pull-left">
                    <DatePicker
                        v-model="exportQueryDateRange"
                        size="small"
                        type="daterange"
                        placeholder="发生时间">
                    </DatePicker>
                </div>
                <div class="pull-left" style="margin: 0 7px;">
                    <list-cascader
                        clearable
                        size="small"
                        multiple="0"
                        :defaultSelected="true"
                        :collection="financialContractQueryModels"
                        v-model="exportQuery.financialContractUuids"
                        :placeholder="$utils.locale('financialContract')">
                    </list-cascader>
                </div>
                <div class="pull-left" style="margin: 0 7px 0 0;">
                    <el-select
                        placeholder="通道选择"
                        v-model="exportQuery.paymentGateways"
                        size="small"
                        multiple
                        class="short">
                        <el-select-all-option
                            :options="selectedPaymentGateways">
                        </el-select-all-option>
                        <el-option
                            v-for="item in selectedPaymentGateways"
                            :label="item.value"
                            :value="item.key">
                        </el-option>
                    </el-select>
                </div>
                <div class="pull-left">
                    <el-button size="small" type="primary" @click="handleExport" v-if="ifElementGranted('export-repayment-cashflow')">导出对账单</el-button>
                    <el-button size="small" type="primary" style="margin-left: 4px;" @click="handleExportDailyRepaymentList" v-if="ifElementGranted('export-today-repayment-list')">导出还款清单</el-button>
                </div>
            </div>
            <div class="pull-right">
                <ListStatistics
                    identifier="financialContractIds" 
                    action="/interfacePayment/list/amountStatistics"
                    :parameters="conditions">
                    <template scope="statistics">
                        <div>
                            代扣金额<span class="pull-right">{{ statistics.data.transferAmount | formatMoney }}</span>
                        </div>
                    </template>
                </ListStatistics>
                <PageControl 
                    ref="pageControl"
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise, searchify, purify, downloadFile, filterExportQuery } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import DatePicker from 'components/DatePicker';
    import format from 'filters/format';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            HelpPopover: require('views/include/HelpPopover'),
            ComboQueryBox: require('views/include/ComboQueryBox'),
            ListStatistics: require('views/include/ListStatistics'),
            DatePicker,
        },
        data: function() {
            return {
                action: '/interfacePayment/list/query',
                exportQuery: {
                    financialContractIds: [],
                    financialContractUuids: [],
                    startDateString: format.formatDate(new Date()),
                    endDateString: format.formatDate(new Date()),
                    paymentGateways: [],
                },
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractIds: [],
                    paymentGateway: [],
                    executionStatus: [],
                    receiveStartDate: '',
                    receiveEndDate: '',
                },
                comboConds: {
                    deductPlanNo: '',
                    deductApplicationNo: '',
                    customerName: '',
                    bankAccountNo: '',
                    bankName: '',
                },

                financialContractQueryModels: [],
                paymentGateway: [],
                orderStatus: [],
                selectedPaymentGateways: [],

                exportQueryDateRange: [new Date(), new Date()],
            };
        },
        watch: {
            'exportQuery.financialContractUuids': function(current) {
                if(current.length){
                    var item = current[0];
                    var uuid = item.value;
                    var { financialContractQueryModels, exportQuery, paymentGateways } = this;

                    this.selectedPaymentGateways = paymentGateways[uuid] || [];
                    exportQuery.financialContractIds = [item.financialContractId];
                    exportQuery.paymentGateways = [];
                    this.selectedPaymentGateways.forEach(item => {
                        exportQuery.paymentGateways.push(item.key);
                    });
                } else {
                    this.selectedPaymentGateways = [];
                    this.exportQuery.paymentGateways = [];
                }
            },
            exportQueryDateRange: function(current){
                this.exportQuery.startDateString = format.formatDate(current[0], 'yyyy-MM-dd');
                this.exportQuery.endDateString = format.formatDate(current[1], 'yyyy-MM-dd');
            },
        },
        computed: {
            conditions: function() {
                var currentQueryConds = this.filterQueryConds(this.queryConds);
                return Object.assign({}, currentQueryConds, this.comboConds, this.sortConds, this.pageConds);
            },
        },
        activated: function() {
            this.exportQuery.startDateString = format.formatDate(new Date());  
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: `/interfacePayment/list/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.paymentGateway = data.paymentGateway || [];
                    this.orderStatus = data.orderStatus || [];
                    this.paymentGateways = data.paymentGateways || [];

                    this.queryConds.paymentGateway = data.paymentGateway.map(item => item.key);
                    this.queryConds.executionStatus = data.orderStatus.map(item => item.key);

                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleExport: function() {
                downloadFile(`${this.api}/interfacePayment/exportAccountChecking`, this.formatExportQuery());
            },
            handleExportDailyRepaymentList: function() {
                downloadFile(`${this.api}/report/export?reportId=11`, this.formatExportQuery());
            },
            formatExportQuery: function() {
                let exportQuery = Object.assign({}, this.exportQuery);
                delete exportQuery.financialContractUuids;
                return filterExportQuery(exportQuery);
            },
            filterQueryConds: function(queryConds) {
                var list;
                var currentQueryConds = Object.assign({}, queryConds);
                if (currentQueryConds.financialContractIds && currentQueryConds.financialContractIds.length) {
                    list = currentQueryConds.financialContractIds || [];
                    currentQueryConds.financialContractIds = list.map(item => item.value);
                }
                return currentQueryConds;
            }
        }
    }
</script>