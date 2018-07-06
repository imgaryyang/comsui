<style lang="sass">
    .merge-table {
        th  {
            .el-checkbox {
                display: none;
            }
        }
    }
    .mergePopover {
        padding: 0px;

        .merge-data {
            background-color: #F5F5F5;
            overflow:scroll;
            height: 150px;
            overflow-x: hidden;

            .no-data {
                text-align: center;
                line-height: 150px;
            }


            .item {
                height: 60px;
                padding: 5px 10px;
                border-bottom: 1px solid #e0e0e0;
                position: relative;

                .color-dim {
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    width: 290px;
                    overflow: hidden;

                }

                p {
                    margin: 5px 0px;
                }

                .delete {
                    height: 16px;
                    width: 16px;
                    background-image: url(~assets/images/icons.png);
                    display: inline-block;
                    background-repeat: no-repeat;
                    vertical-align: middle;
                    background-position: -117px -4px;
                    cursor: pointer;
                    position: absolute;
                    top: 25px;
                    right: 10px;
                }
            }

        }

        .operation-area {
            padding: 10px;
            height: 60px;
        }
    }
</style>

<template>
    <div class="content" id="plan-execlog-order">
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
                            v-model="queryConds.remittanceChannel"
                            placeholder="放款通道"
                            size="small"
                            clearable>
                            <el-option
                                v-for="item in remittanceChannel"
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
                            <el-option label="代付单号" value="execLogUuid"></el-option>
                            <el-option label="放款编号" value="planUuid"></el-option>
                            <el-option label="付款方账户名" value="payerAccountHolder"></el-option>
                            <el-option label="收款方账户名" value="cpBankAccountHolder"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border merge-table"
                    ref="table"
                    row-key="execReqNo"
                    v-loading="dataSource.fetching"
                    @selection-change="handleSelectionChange"
                    @sort-change="onSortChange"
                    :data="dataSource.list"
                    stripe>
                    <el-table-column
                        type="selection"
                        :reserve-selection="true"
                        width="55">
                    </el-table-column>
                    <el-table-column label="代付单号" inline-template>
                        <a :href="`${ctx}#/capital/remittance-cash-flow/plan-execlog/${ row.execReqNo }/detail`">{{ row.execReqNo }}</a>
                    </el-table-column>
                    <el-table-column label="放款编号" inline-template>
                        <a :href="`${ctx}#/data/remittance/plan/${ row.remittancePlanUuid }/detail`">{{ row.remittancePlanUuid }}</a>
                    </el-table-column>
                    <el-table-column label="付款方账户名" inline-template>
                        <div>
                            <span>{{ row.pgAccountInfo.accountName }}</span>
                            <el-popover
                                v-if="row.pgAccountInfo.accountName"
                                trigger="click"
                                placement="top">
                                <div>
                                    <template>
                                        <div>账户名：{{ row.pgAccountInfo.accountName }}</div>
                                        <div>账户号：{{ row.pgAccountInfo.accountNo }}</div>
                                        <div>开户行：{{ row.pgAccountInfo.bankName }}</div>
                                        <div>所在地：{{ row.pgAccountInfo.province }}&nbsp;{{ row.pgAccountInfo.city }}</div>
                                        <div>证件号：{{ row.pgAccountInfo.desensitizationIdNumber }}</div>
                                    </template>
                                </div>
                                <i slot="reference" class="icon icon-bankcard"></i>
                            </el-popover>
                        </div>
                    </el-table-column>
                    <el-table-column label="收款方账户名" inline-template>
                        <div>
                            <span>{{ row.cpAccountInfo.accountName }}</span>
                            <el-popover
                                v-if="row.cpAccountInfo.accountName"
                                trigger="click"
                                placement="top">
                                <div>
                                    <template>
                                        <div>账户名：{{ row.cpAccountInfo.accountName }}</div>
                                        <div>账户号：{{ row.cpAccountInfo.accountNo }}</div>
                                        <div>开户行：{{ row.cpAccountInfo.bankName }}</div>
                                        <div>所在地：{{ row.cpAccountInfo.province }}&nbsp;{{ row.cpAccountInfo.city }}</div>
                                        <div>证件号：{{ row.cpAccountInfo.desensitizationIdNumber }}</div>
                                    </template>
                                </div>
                                <i slot="reference" class="icon icon-bankcard"></i>
                            </el-popover>
                        </div>
                    </el-table-column>
                    <el-table-column label="放款金额" prop="plannedAmount" inline-template sortable="custom">
                        <div>{{ row.plannedAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="放款通道" prop="remittanceChannel">
                    </el-table-column>
                    <el-table-column label="创建时间" inline-template sortable="custom">
                        <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" inline-template sortable="custom">
                        <div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="执行时差" prop="executedTimeOff"></el-table-column>
                    <el-table-column label="代付状态" inline-template>
                        <div :class="{
                            'color-warning': row.executionStatus == '异常',
                            'color-danger': row.executionStatus == '失败'
                        }">{{ row.executionStatus }}</div>
                    </el-table-column>
                    <el-table-column label="冲账状态" inline-template>
                        <div :class="{
                        'color-danger': row.reverseStatus == '未冲账'
                        }">{{ row.reverseStatus }}</div>
                    </el-table-column>
                    <el-table-column label="备注" prop="executionRemark"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-left">
                <el-popover
                    ref="mergePopover"
                    placement="top"
                    trigger="click"
                    popper-class="mergePopover"
                    width="320">
                    <slot>
                        <div class="merge-data">
                            <div v-for="(item, index ) in selectedMergeOrders" class="item">
                                <div class="pull-left">
                                    <p class="color-dim">代付单号: {{ item.execReqNo }}</p>
                                    <p class="color-danger">金额: {{ item.plannedAmount | formatMoney }}</p>
                                </div>
                                <i class="delete pull-right" @click="deleteItem(index)"></i>
                            </div>
                            <div v-if="!selectedMergeOrders.length" class="no-data color-dim">暂无代付单</div>
                        </div>
                        <div class="operation-area">
                            <div class="pull-left">
                                <div>
                                    <span class="color-dim">合并单数：</span><span class="color-danger">{{ selectedMergeOrders.length }}</span>
                                </div>
                                <div>
                                    <span class="color-dim">合并金额：</span><span class="color-danger">{{ mergeTotalAmount | formatMoney }}</span>
                                </div>
                            </div>
                            <div class="pull-right">
                                <el-button type="primary" @click="showCashFlowModal" v-if="selectedMergeOrders.length > 1">去合并</el-button>
                            </div>
                        </div>
                    </slot>
                </el-popover>
                <el-badge :value="selectedMergeOrders.length">
                    <el-button key="merge" v-popover:mergePopover>待合并代付单</el-button>
                </el-badge>
                <el-button @click="$router.go(-1)">取消</el-button>
            </div>
            <div class="pull-right">
                <ListStatistics
                    action="/capital/plan/execlog/amountStatistics"
                    :parameters="conditions">
                    <template scope="statistics">
                        <div>
                            放款金额<span class="pull-right">{{ statistics.data.remittanceAmount | formatMoney }}</span>
                        </div>
                    </template>
                </ListStatistics>
                <PageControl
                    ref="pageControl"
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                    <template scope="pageStatistics" slot="statistics">
                        共
                        <el-popover
                            @show="fetchDataStatistics"
                            popper-class="text-align-center"
                            :width="210"
                            placement="top"
                            trigger="click">
                            <div v-if="dataStatistics.error">
                                {{ dataStatistics.error }}
                            </div>
                            <div v-else-if="dataStatistics.size == 0">
                                没有统计数据
                            </div>
                            <div v-else>
                                <div class="title" v-if="dataStatistics.size >= 5">统计数量前5的银行</div>
                                <div class="content text-align-left">
                                    <div v-for="(value, key) in dataStatistics.dataMap">
                                        <span>{{key}}</span>
                                        <span class="pull-right">{{value}}条</span>
                                    </div>
                                </div>
                            </div>
                            <a href="javascript: void 0;" slot="reference">{{ pageStatistics.data.size }}</a>
                        </el-popover>
                        条
                    </template>
                </PageControl>
            </div>
        </div>
        <MergeCashFlowModal
            v-model="mergeCashFlowModal.show"
            :model="mergeCashFlowModal.model">
        </MergeCashFlowModal>
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
            ListStatistics: require('views/include/ListStatistics'),
            MergeCashFlowModal: require('./include/MergeCashFlowModal'),
            DatePicker
        },
        data: function() {
            return {
                action: '/capital/plan/execlog/query',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    remittanceChannel: '',
                    createTimeStart: '',
                    createTimeEnd: '',
                    statusModifyTimeStart: '',
                    statusModifyTimeEnd: '',
                    executionStatusAndTransactionRecipients: ['3'],
                    reverseStatusOrdinals: []
                },
                comboConds: {
                    execLogUuid: '',
                    planUuid: '',
                    payerAccountHolder: '',
                    cpBankAccountHolder: '',
                },
                sortConds: {
                    sortField: '',
                    isAsc: ''
                },
                dataStatistics: {
                    error: '',
                    dataMap: {},
                    size: 0
                },
                mergeCashFlowModal: {
                    show: false,
                    model: {
                        plannedAmount: ''
                    }
                },

                financialContractQueryModels: [],
                remittanceChannel: [],
                selectedMergeOrders: [],

                createdTimeRange: [],
                statusModifyTimeRange: [],
            }
        },
        watch: {
            createdTimeRange: function(current){
                this.queryConds.createTimeStart = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.createTimeEnd = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            },
            statusModifyTimeRange: function(current){
                this.queryConds.statusModifyTimeStart = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.statusModifyTimeEnd = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            }
        },
        computed: {
            mergeTotalAmount: function() {
                var totalAmount = 0;
                this.selectedMergeOrders.forEach(item => {
                    var amount = parseFloat(item.plannedAmount ? item.plannedAmount : '0');
                    totalAmount += amount;
                })
                return totalAmount;
            },
            mergeRepaymentOrderUuids: function() {
                var uuids = [];
                uuids = this.selectedMergeOrders.map(item => item.execReqNo);
                return JSON.stringify(uuids);
            }
        },
        mounted: function() {
            this.checked();
        },
        deactivated: function() {
            this.$refs.table.clearSelection();
        },
        methods: {
            fetchDataStatistics: function() {
                ajaxPromise({
                    url: '/capital/plan/execlog/dataStatistics',
                    data: purify(this.conditions)
                }).then(data => {
                    this.dataStatistics.size = data.size;
                    this.dataStatistics.dataMap = data.dataMap;
                    this.dataStatistics.error = '';
                }).catch(message => {
                    this.dataStatistics.error = message;
                });
            },
            initialize: function() {
                return ajaxPromise({
                    url: `/capital/plan/execlog/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.remittanceChannel = data.remittanceChannel || [];

                    this.queryConds.reverseStatusOrdinals = data.reverseStatus.map(item => item.value === "未发生" ? item.key : '');
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleSelectionChange: function(selectedItems) {
                this.selectedMergeOrders = selectedItems;
            },
            checked: function() {
                var selected = false;
                this.dataSource.list.forEach(row => {
                    this.selectedMergeOrders.forEach(orders => {
                        if (row.execReqNo == orders.execReqNo) {
                            selected = true;
                            return;
                        }
                    })
                    selected && this.$refs.table.toggleRowSelection(row,true);
                });
            },
            showCashFlowModal: function() {
                this.mergeCashFlowModal.model.plannedAmount = this.mergeTotalAmount;
                this.mergeCashFlowModal.model.execReqNos = this.mergeRepaymentOrderUuids;
                this.mergeCashFlowModal.show = true;
            },
            deleteItem: function(index) {
                this.selectedMergeOrders.splice(index, 1);
            }
        }
    }
</script>