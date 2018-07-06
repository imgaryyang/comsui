<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <el-select
                            v-model="queryConds.paymentInstitution"
                            placeholder="交易网关"
                            size="small"
                            multiple>
                            <el-select-all-option
                                :options="paymentInstitutionNames">
                            </el-select-all-option>
                            <el-option
                                v-for="item in paymentInstitutionNames"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.status"
                            placeholder="凭证状态"
                            size="small"
                            clearable>
                            <el-option
                                v-for="item in clearingVoucherStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <DatePicker
                            v-model="cashFlowTimeRange"
                            size="small"
                            type="datetimerange"
                            placeholder="资金入账时间">
                        </DatePicker>
                    </el-form-item>
                    <el-form-item>
                        <DatePicker
                            v-model="updateStateTimeRange"
                            size="small"
                            type="datetimerange"
                            placeholder="状态变更时间">
                        </DatePicker>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="机构账户名" value="counterName"></el-option>
                            <el-option label="机构账户号" value="counterNo"></el-option>
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
                    <el-table-column inline-template label="凭证编号">
                        <div>
                            <div v-if="row.clearingVoucherStatus !== 'DOING'">
                                <a :href="`${ctx}#/capital/voucher/clearing/${ row.voucherUuid }/detail`">{{ row.voucherNo }}</a>
                            </div>
                            <div v-else>
                                <span>{{ row.voucherNo }}</span>
                            </div>
                        </div>
                    </el-table-column>
                    <el-table-column prop="bankInfo" label="来往机构">
                    </el-table-column>
                    <el-table-column prop="counterAccountName" label="机构账户名">
                    </el-table-column>
                    <el-table-column prop="counterAccountNo" label="机构账户号">
                    </el-table-column>
                    <el-table-column 
                        prop="amount"
                        inline-template
                        label="凭证金额">
                        <div>{{ row.voucherAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column prop="paymentInstitutionName" label="交易网关">
                    </el-table-column>
                    <el-table-column
                        prop="sourceDocumentTradeTime"
                        inline-template
                        label="资金入账时间">
                        <div>{{ row.cashFlowTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column
                        prop="createTime"
                        inline-template
                        label="状态变更时间">
                        <div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column prop="clearingVoucherStatusName" label="凭证状态">
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-right">
                <PageControl
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
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import format from 'filters/format';
    import DatePicker from 'components/DatePicker';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
            DatePicker
        },
        data: function() {
            return {
                action: '/clearingVoucher/voucherList',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    paymentInstitution: '',
                    status: '',
                    startCashFlowTime: '',
                    endCashFlowTime: '',
                    startUpdateStateTime: '',
                    endUpdateStateTime: ''
                },
                comboConds: {
                    counterAccountNo: '',
                    counterAccountName: '',
                },

                paymentInstitutionNames: [],
                clearingVoucherStatus: [],

                cashFlowTimeRange: [],
                updateStateTimeRange: [],
            };
        },
        watch: {
            cashFlowTimeRange: function(current){
                this.queryConds.startCashFlowTime = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endCashFlowTime = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            },
            updateStateTimeRange: function(current){
                this.queryConds.startUpdateStateTime = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endUpdateStateTime = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            }
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
                return ajaxPromise({
                    url: `/clearingVoucher/options`
                }).then(data => {
                    this.paymentInstitutionNames = data.paymentInstitutionNames || [];
                    this.clearingVoucherStatus = data.clearingVoucherStatus || [];

                    this.queryConds.paymentInstitution = data.paymentInstitutionNames.map(item => item.key);
                }).catch(message => {
                    MessageBox.open(message);
                })
            }
        }
    }
</script>