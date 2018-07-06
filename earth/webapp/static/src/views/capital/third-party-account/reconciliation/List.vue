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
                            size="small" 
                            v-model="queryConds.paymentInstitution"
                            multiple
                            placeholder="交易通道">
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
                            size="small" 
                            v-model="queryConds.auditResult"
                            multiple
                            placeholder="对账状态">
                            <el-select-all-option 
                                :options="auditResults">
                            </el-select-all-option>
                            <el-option
                                v-for="item in auditResults"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.startTime"
                                    :end-date="queryConds.endTime"
                                    size="small"
                                    pickTime="true"
                                    formatToMinimum="true"
                                    placeholder="入账起始时间">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.endTime"
                                    :start-date="queryConds.startTime"
                                    size="small"
                                    pickTime="true"
                                    formatToMaximum="true"
                                    placeholder="入账终止时间">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" @click="reconciliationModal.visible = true" v-if="ifElementGranted('create-reconciliation-audit-job')">新建对账任务</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    @sort-change="onSortChange"
                    :data="dataSource.list">
                    <el-table-column 
                        inline-template
                        label="任务编号">
                        <div>
                            <a v-if="row.auditResult != 'UNKNOWN'" 
                                :href="`${ctx}#/capital/third-party-account/reconciliation/${row.uuid}/detail`">
                                {{ row.auditJobNo }}
                            </a>
                            <span v-else>{{ row.auditJobNo }}</span>
                        </div>
                    </el-table-column>
                    <el-table-column prop="financialContractName" :label="$utils.locale('financialContract')"></el-table-column>
                    <el-table-column prop="paymentInstitutionName" label="交易通道"></el-table-column>
                    <el-table-column prop="pgClearingAccount" label="清算号"></el-table-column>
                    <el-table-column prop="accountSideName" label="收付类型"></el-table-column>
                    <el-table-column 
                        inline-template 
                        sortable="custom"
                        prop="startTime"
                        width="140px" 
                        label="入账起始时间">
                            <div>{{ row.startTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column 
                        inline-template 
                        sortable="custom"
                        prop="endTime"
                        width="140px" 
                        label="入账终止时间">
                            <div>{{ row.endTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column prop="auditResultName" label="对账状态"></el-table-column>
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
        <EditReconciliationModal 
            @submit="fetch"
            :financialContractQueryModels="financialContractQueryModels"
            v-model="reconciliationModal.visible">
        </EditReconciliationModal>
    </div>
</template>

<script>
    import Pagination, { extract } from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise } from 'assets/javascripts/util';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            EditReconciliationModal: require('./include/EditReconciliationModal')
        },
        data: function() {
            return {
                action: '/audit/remittance/query',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    paymentInstitution: '',
                    auditResult: '',
                    startTime: '',
                    endTime: '',
                },
                sortConds: {
                    sortField: '',
                    isAsc: ''
                },
                financialContractQueryModels: [],
                paymentInstitutionNames: [],
                auditResults: [],

                reconciliationModal: {
                    visible: false
                }
            };
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
                return ajaxPromise({
                    url: `/audit/remittance/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.paymentInstitutionNames = data.paymentInstitutionNames || [];
                    this.auditResults = data.auditResults || [];
                });
            }
        }
    }
</script>