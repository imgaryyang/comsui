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
                            v-model="queryConds.refundStatus"
                            placeholder="退款状态"
                            size="small"
                            multiple
                            clearable>
                            <el-select-all-option
					 			:options="refundStatusList">
					 		</el-select-all-option>
                            <el-option
                                v-for="item in refundStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <DatePicker
                            v-model="createdTimeRange"
                            size="small"
                            type="datetimerange"
                            placeholder="创建时间">
                        </DatePicker>
                    </el-form-item>
                    <el-form-item>
                        <DatePicker
                            v-model="statusModifyTimeRange"
                            size="small"
                            type="datetimerange"
                            placeholder="状态变更时间">
                        </DatePicker>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="退款资产编号" value="refundAssetNo"></el-option>
                            <el-option label="账户编号" value="accountNo"></el-option>
                            <el-option label="账户名称" value="accountName"></el-option>
                            <el-option label="备注" value="remark"></el-option>
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
                    :data="dataSource.list"
                    v-loading="dataSource.fetching"
                    @sort-change="onSortChange"
                    stripe>
                    <el-table-column label="退款单号" inline-template>
                        <a :href="`${ctx}#/capital/account/refund-bill/${row.uuid}/detail`">{{ row.refundOrderNo }}</a>
                    </el-table-column> 
                    <el-table-column label="账户编号" prop="accountNo">
                    </el-table-column>
                    <el-table-column label="账户名称" prop="accountName">
                    </el-table-column>
                    <el-table-column label="退款资产编号" prop="refundAssetNo">
                    </el-table-column>
                    <el-table-column label="退款金额" inline-template>
                        <div>{{ row.amount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="创建时间" inline-template sortable="custom">
                        <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" inline-template sortable="custom">
                        <div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="退款状态" prop="refundStatusName"></el-table-column>
                    <el-table-column label="备注" prop="remark"></el-table-column>
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
    import DatePicker from 'components/DatePicker'

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
            DatePicker
        },
        data: function() {
            return {
                action: '/refund/query',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    refundStatus: [],
                    startDate: '',
                    endDate: '',
                    modifyStartTime: '',
                    modifyEndTime: '',
                },
                comboConds: {
                    refundAssetNo: '',
                    accountNo: '',
                    accountName: '',
                    remark: '',
                },
                sortConds: {
                    sortField: '',
                    isAsc: ''
                },

                financialContractQueryModels: [],
                refundStatusList: [],

                createdTimeRange: [],
                statusModifyTimeRange: [],
            };
        },
        watch: {
            createdTimeRange: function(current){
                this.queryConds.startDate = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endDate = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            },
            statusModifyTimeRange: function(current){
                this.queryConds.modifyStartTime = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.modifyEndTime = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            }
        },
        activated: function() {
            this.getOptions();
        },
        methods: {
            getOptions: function() {
                ajaxPromise({
                    url: `/refund/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.refundStatusList = data.refundStatus || [];
                }).catch(message => {
                    MessageBox.open(message);
                })
            }
        }
    }
</script>