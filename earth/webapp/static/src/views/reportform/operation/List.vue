<style lang="sass" scoped>
    .operation-data-list.active td {
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
                            :collection="financialContractQueryModels"
                            v-model="queryConds.financialContractUuids"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
                    </el-form-item>
                    <el-form-item>
                        <DateTimePicker
                            v-model="queryConds.queryDate"
                            :pickTime="false"
                            :endDate="customEndDate"
                            placeholder="请选择查询日期"
                            size="small">
                        </DateTimePicker>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item v-if="ifElementGranted('export-operation-data')">
                        <ExportDropdown @command="exportModal.show = true">
                            <el-dropdown-item command="">运营数据</el-dropdown-item>
                        </ExportDropdown>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table
                    class="no-table-bottom-border"
                    v-loading="dataSource.fetching"
                    @sort-change="onSortChange"
                    :data="dataSource.list"
                    :row-class-name="rowClassName"
                    stripe
                    >
                    <el-table-column :label="$utils.locale('financialContract')" prop="financialContractName"></el-table-column>
                    <el-table-column label="放款笔数" prop="remittanceNumber"></el-table-column>
                    <el-table-column label="放款总额" prop="remittanceAmount" inline-template>
                        <div>{{row.remittanceAmount | formatMoney}}</div>
                    </el-table-column>
                    <el-table-column label="计划还款笔数" prop="planAssetNumber"></el-table-column>
                    <el-table-column label="计划还款本金" prop="planAssetPrincipal" inline-template>
                        <div>{{row.planAssetPrincipal | formatMoney}}</div>
                    </el-table-column>
                    <el-table-column label="计划还款利息" prop="planAssetInterest" inline-template>
                        <div>{{row.planAssetInterest | formatMoney}}</div>
                    </el-table-column>
                    <el-table-column label="计划还款总额" prop="planAssetAmount" inline-template>
                        <div>{{row.planAssetAmount | formatMoney}}</div>
                    </el-table-column>
                    <el-table-column label="实际还款笔数" prop="actualRepaymentNumber"></el-table-column>
                    <el-table-column label="实收金额" prop="actualReceiveAmount" inline-template>
                        <div>{{row.actualReceiveAmount | formatMoney}}</div>
                    </el-table-column>
                    <el-table-column label="实际还款总额" prop="actualTotalAmount" inline-template>
                        <div>{{row.actualTotalAmount | formatMoney}}</div>
                    </el-table-column>
                    <el-table-column label="差额" prop="diffAmount" inline-template>
                        <div>{{row.diffAmount | formatMoney}}</div>
                    </el-table-column>
                    <el-table-column label="操作" inline-template>
                        <div>
                            <a
                                class="expand-bill"
                                @click="hanldeToggleExpand(row, $event)">
                                <i class="icon icon-expand"></i>
                            </a>
                        </div>
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
        <ExportPreviewModal
            :parameters="exportQueryConds"
            :query-action="`/operation-data/query`"
            :download-action="`/operation-data/export`"
            v-model="exportModal.show">
            <el-table-column label="每日数据清单">
                <el-table-column prop="financialContractName" label="项目">
                </el-table-column>
                <el-table-column prop="date" label="日期">
                </el-table-column>
                <el-table-column prop="remittanceNumber" label="放款笔数">
                </el-table-column>
                <el-table-column prop="remittanceAmount" label="放款总额" inline-template>
                        <div>{{row.remittanceAmount | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="paymentPrincipal" label="应还本金" inline-template>
                        <div>{{row.paymentPrincipal | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="paymentInterest" label="应还利息" inline-template>
                        <div>{{row.paymentInterest | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="paymentLoanServiceFee" label="应还贷款服务费" inline-template>
                        <div>{{row.paymentLoanServiceFee | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="paymentAmount" label="应还总额" inline-template>
                        <div>{{row.paymentAmount | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="planAssetNumber" label="计划还款笔数">
                </el-table-column>
                <el-table-column prop="planAssetPrincipal" label="计划还款本金" inline-template>
                        <div>{{row.planAssetPrincipal | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="planAssetInterest" label="计划还款利息" inline-template>
                        <div>{{row.planAssetInterest | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="planAssetAmount" label="计划还款总金额" inline-template>
                        <div>{{row.planAssetAmount | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualRepaymentNumber" label="实际还款笔数">
                </el-table-column>
                <el-table-column prop="actualReceiveAmount" label="实收金额" inline-template>
                        <div>{{row.actualReceiveAmount | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualPrincipal" label="实收本金" inline-template>
                        <div>{{row.actualPrincipal | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualInterest" label="实收利息" inline-template>
                        <div>{{row.actualInterest | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualLoanServiceFee" label="实收贷款服务费" inline-template>
                        <div>{{row.actualLoanServiceFee | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualTechFee" label="实收技术维护费" inline-template>
                        <div>{{row.actualTechFee | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualOtherFee" label="实收其他费用" inline-template>
                        <div>{{row.actualOtherFee | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualOverduePenalty" label="实收罚息" inline-template>
                        <div>{{row.actualOverduePenalty | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualOverdueDefaultFee" label="实收逾期违约金" inline-template>
                        <div>{{row.actualOverdueDefaultFee | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualOverdueServiceFee" label="实收逾期服务费" inline-template>
                        <div>{{row.actualOverdueServiceFee | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualOverdueOtherFee" label="实收逾期其他费用" inline-template>
                        <div>{{row.actualOverdueOtherFee | formatMoney}}</div>
                </el-table-column>
                <el-table-column prop="actualTotalAmount" label="实际还款总额" inline-template>
                        <div>{{row.actualTotalAmount | formatMoney}}</div>
                </el-table-column>
            </el-table-column>
        </ExportPreviewModal>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise, filterQueryConds } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import Vue from 'vue';
    import OperationDataListConf from './OperationDataList';
    import format from 'filters/format';

    const OperationDataList = Vue.extend(OperationDataListConf);

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ExportDropdown: require('views/include/ExportDropdown'),
            ExportPreviewModal: require('views/include/ExportPreviewModal')
        },
        data: function() {
            var endDate = format.formatDate(new Date() - 1000* 60*60*24);
            return {
                action: '/operation-data/query',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    queryDate: endDate
                },
                financialContractQueryModels: [],
                customEndDate: endDate,

                operationDataRefs: {},
                exportModal: {
                    show: false
                },
            };
        },
        watch: {
            'dataSource.list': function() {
                Object.values(this.operationDataRefs).forEach(ref => {
                    $(ref.$el).remove();
                    ref.$destroy();
                });
                this.operationDataRefs = {};
            }
        },
        computed: {
            exportQueryConds: function() {
                return filterQueryConds(this.queryConds);
            }
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: `/operation-data/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            getOperationDataRef: function(financialContractUuid) {
                var ref = this.operationDataRefs[financialContractUuid];
                if (ref) {
                    return ref;
                }

                ref = this.operationDataRefs[financialContractUuid] =  new OperationDataList({
                    el: document.createElement('tr'),
                });
                return ref;
            },
            rowClassName: function(row) {
                return row.active ? 'operation-data-list active' : 'operation-data-list'
            },
            hanldeToggleExpand: function(row, e) {
                var ref = this.getOperationDataRef(row.financialContractUuid);

                if (ref.active) {
                    row.active = false;

                    ref.$el.parentNode.removeChild(ref.$el);

                    ref.appArrivaList = [];
                    ref.active = false;
                } else {
                    row.active = true;

                    var $tr = $(e.currentTarget).closest('tr');
                    $tr.after(ref.$el);

                    ref.appArrivaList = [row];
                    this.$nextTick(() => {
                        ref.active = true;
                    });
                }

            }
        }
    }
</script>