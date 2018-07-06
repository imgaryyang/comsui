
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
                            v-model="queryConds.financialContractUuids"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.voucherType"
                            placeholder="凭证类型"
                            size="small"
                            clearable>
                            <el-option
                                v-for="item in voucherTypeList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.voucherStatus"
                            placeholder="凭证状态"
                            size="small"
                            clearable>
                            <el-option
                                v-for="item in voucherStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.startDateString"
                                    :end-date="queryConds.endDateString"
                                    size="small"
                                    pickTime="true"
                                    formatToMinimum="true"
                                    placeholder="发生起始日期">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.endDateString"
                                    :start-date="queryConds.startDateString"
                                    size="small"
                                    pickTime="true"
                                    formatToMaximum="true"
                                    placeholder="发生终止日期">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="凭证编号" value="voucherNo"></el-option>
                            <el-option label="专户账号" value="hostAccount"></el-option>
                            <el-option label="账户姓名" value="counterName"></el-option>
                            <el-option label="机构账户号" value="counterNo"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item v-if="ifElementGranted('create-bussiness-voucher')">
                        <el-button size="small" type="success" @click="location.assign(`${ctx}#/capital/voucher/business/create`)">新增</el-button>
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
                        <a :href="`${ctx}#/capital/voucher/business/${ row.id }/detail`">{{ row.voucherNo }}</a>
                    </el-table-column>
                    <el-table-column prop="receivableAccountNo" label="专户账号">
                    </el-table-column>
                    <el-table-column prop="paymentName" label="账户姓名">
                    </el-table-column>
                    <el-table-column prop="paymentAccountNo" label="机构账户号">
                    </el-table-column>
                    <el-table-column 
                        prop="amount"
                        inline-template 
                        label="凭证金额">
                        <el-popover
                            @show="fetchAssetSetStatistics($index)"
                            trigger="hover"
                            placement="top">
                            <div>
                                <div v-if="row.statistics.error">{{ row.statistics.error }}</div>
                                <template v-else-if="row.voucherType == '回购'">
                                    <div>回购本金金额：{{ row.statistics.principal | formatMoney }}</div>
                                    <div>回购利息金额：{{ row.statistics.interest | formatMoney }}</div>
                                    <div>回购罚息金额：{{ row.statistics.penaltyFee | formatMoney }}</div>
                                    <div>回购其他费用金额：{{ row.statistics.otherCharge | formatMoney }}</div>
                                </template>
                                <template v-else>
                                    <div>还款本金:{{ row.statistics.principal | formatMoney }}</div>
                                    <div>还款利息:{{ row.statistics.interest | formatMoney }}</div>
                                    <div>贷款服务费:{{ row.statistics.serviceCharge | formatMoney }}</div>
                                    <div>技术维护费:{{ row.statistics.maintenanceCharge | formatMoney }}</div>
                                    <div>其他费用:{{ row.statistics.otherCharge | formatMoney }}</div>
                                    <div>逾期罚息:{{ row.statistics.penaltyFee | formatMoney }}</div>
                                    <div>逾期违约金:{{ row.statistics.latePenalty | formatMoney }}</div>
                                    <div>逾期服务费:{{ row.statistics.lateFee | formatMoney }}</div>
                                    <div>逾期其他费用:{{ row.statistics.lateOtherCost | formatMoney }}</div>
                                </template>
                            </div>
                            <span slot="reference">{{ row.amount | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column prop="voucherType" label="凭证类型">
                    </el-table-column>
                    <el-table-column prop="voucherSource" label="凭证来源">
                    </el-table-column>
                    <el-table-column 
                        prop="createTime" 
                        inline-template 
                        label="发生时间">
                        <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column 
                        prop="sourceDocumentTradeTime" 
                        inline-template 
                        label="流水入账时间">
                        <div>{{ row.sourceDocumentTradeTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column prop="voucherStatus" label="凭证状态">
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

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
        },
        data: function() {
            return {
                action: '/voucher/business/query',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    voucherType: '',
                    voucherStatus: '',
                    startDateString: '',
                    endDateString: ''
                },
                comboConds: {
                    hostAccount: '',
                    counterName: '',
                    counterNo: ''
                },
                
                financialContractQueryModels: [],
                voucherTypeList: [],
                voucherStatusList: []
            };
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
                return ajaxPromise({
                    url: `/voucher/business/optionData`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.voucherTypeList = data.voucherTypeList || [];
                    this.voucherStatusList = data.voucherStatusList || [];

                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            onSuccess: function(data) {
                this.dataSource.list = data.list.map(item => {
                    item.statistics = {
                        planChargesDetail: {},
                        paidUpChargesDetail: {},
                        unPaidDetail: {}
                    };
                    return item;
                });
                this.dataSource.size = data.size;
            },
            fetchAssetSetStatistics: function(index) {
                var { list } = this.dataSource;
                if(list[index] && list[index].statistics && list[index].statistics.success){
                    return
                }
                ajaxPromise({
                    url: `/voucher/statistics/voucherAmount`,
                    data: {
                        voucherUuid: list[index].uuid,
                        secondType: list[index].secondType
                    }
                }).catch(error => {
                    this.$set(list[index], 'statistics', { error });
                }).then(data => {
                    this.$set(list[index], 'statistics', Object.assign({
                        success: true
                    }, data.voucherAmountStatisticsModel));
                });
            },
        }
    }
</script>