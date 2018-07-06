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
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.startTime"
                                    :end-date="queryConds.endTime"
                                    size="small"
                                    pickTime="true"
                                    formatToMinimum="true"
                                    placeholder="导入起始时间">
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
                                    placeholder="导入终止时间">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column 
                        inline-template
                        label="批次编号">
                        <a :href="`${ctx}#/capital/voucher/third-pay/voucher-batch/${row.batchUuid}/detail`">{{ row.batchUuid }}</a>
                    </el-table-column>
                    <el-table-column prop="requestNo" label="来源编号"></el-table-column>
                    <el-table-column prop="financialContractNo" label="项目名称"></el-table-column>
                    <el-table-column prop="size" label="凭证总数"></el-table-column>
                    <el-table-column inline-template label="导入时间">
                        <div>{{ row.createTime | formatDate}}</div>
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
        data: function() {
            return {
                action: '/voucher/thirdPartyPayApi/list-batchs',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    startTime: '',
                    endTime: ''
                },

                financialContractQueryModels: [],
            };
        },
        // activated: function() {
        //     this.queryConds.financialContractUuids = [];
        //     this.queryConds.startTime = '';
        //     this.queryConds.endTime = '';
        // },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
                return ajaxPromise({
                    url: '/voucher/thirdPartyPayApi/list-batchs/options'
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                }).catch(msg => {
                    MessageBox.open(message);
                })
            }
        }
    }
</script>