<style lang="sass" scoped>
    .operations{
        .warning{
            border-color: #e8415f;
        }
    }
</style>

<template>
    <div class="content" id="repayment-order">
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
                            v-model="queryConds.customerTypeList"
                            placeholder="客户类型"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in a"
                                :label="item.key"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.tmpDepositUseStatus"
                            placeholder="使用状态"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in []"
                                :label="item.key"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    v-model="queryConds.startDateTimeString"
                                    :end-date="queryConds.endDateTimeString"
                                    placeholder="创建起始时间"
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
                                    :pickTime="true"
                                    :formatToMaximum="true"
                                    v-model="queryConds.endDateTimeString"
                                    :start-date="queryConds.startDateTimeString"
                                    placeholder="创建终止时间"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="客户姓名" value="customerName"></el-option>
                            <el-option label="预付单号" value="tmpDepositNo"></el-option>、
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
                    stripe
                    @selection-change="handleSelectionChange"
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column
                        type="selection"
                        :reserve-selection="true"
                        width="55">
                    </el-table-column>
                    <el-table-column label="滞留单号" prop="temporaryDepositDocNo" inline-template>
                        <a href="${ctx}#/capital/special-account/retention-voucher/${row.temporaryDepositDocNo}/detail">{{row.temporaryDepositDocNo }}</a>
                    </el-table-column>
                    <el-table-column label="客户姓名" prop="customerName">
                    </el-table-column>
                    <el-table-column label="客户类型" prop="customerType">
                    </el-table-column>
                    <el-table-column label="滞留金额(排序)" prop="totalAmount">
                    </el-table-column>
                    <el-table-column label="剩余金额(排序)" prop="remainAmount" inline-template>
                        <div>{{ row.remainAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="创建时间" prop="createdTime" inline-template>
                        <div>{{ row.createdTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="来源凭证" prop="voucherNo">
                        <a href="#">{{row.voucherNo }}</a>
                    </el-table-column>
                    <el-table-column label="来源类型" prop="voucherSource">
                    </el-table-column>
                    <el-table-column label="使用状态" prop="tmpDepositStatus">
                    </el-table-column>
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
                            <div v-for="(item, index ) in selectedMergeOrdersReflect" class="item">
                                <div class="pull-left">
                                    <p class="color-dim">滞留单号: {{item.temporaryDepositDocNo}}</p>
                                    <p class="color-danger">提现金额: <el-input v-model="item.totalAmount" :class="[item.totalAmount <= selectedMergeOrders[index].totalAmount ? '' : 'warning']"></el-input></p>
                                </div>
                                <i class="delete pull-right" @click="deleteItem(index)"></i>
                            </div>
                            <div v-if="!selectedMergeOrders.length" class="no-data color-dim">暂无单据</div>
                        </div>
                        <div class="operation-area">
                            <div class="pull-left">
                                <div>
                                    <span class="color-dim">提现单据：</span><span class="color-danger">{{ selectedMergeOrders.length }}</span>
                                </div>
                                <div>
                                    <span class="color-dim">提现金额：</span><span class="color-danger">{{ mergeTotalAmount | formatMoney }}</span>
                                </div>
                            </div>
                            <div class="pull-right">
                                <el-button type="primary" :disabled="disabled">申请提现</el-button>
                            </div>
                        </div>
                    </slot>
                </el-popover>
                <el-badge :value="selectedMergeOrders.length">
                    <el-button key="merge" v-popover:mergePopover>待合并订单</el-button>
                </el-badge>
                <el-button @click="$router.go(-1)">取消</el-button>
            </div>
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
    import { ajaxPromise, downloadFile, purify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import format from 'filters/format';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox')
        },
        data: function() {
            return {
                action: '/temporary-deposit-doc/query-temporary-deposit-doc',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    customerTypeList: '',
                    tmpDepositUseStatus: '',
                    startDateTimeString: '',
                    endDateTimeString: ''
                },
                comboConds: {
                    customerName: '',
                    tmpDepositNo: '',
                },

                financialContractQueryModels: [],
                selectedMergeOrders: [],
                disabled: false,
            }
        },
        computed: {
            selectedMergeOrdersReflect: function(){
                return this.selectedMergeOrders.slice()
            },
            mergeTotalAmount: function() {
                var totalAmount = 0;
                this.selectedMergeOrdersReflect.forEach(item => {
                    var amount = parseFloat(item.totalAmount ? item.totalAmount : '0');
                    totalAmount += amount;
                })
                return totalAmount;
            },
            disabled: function(){
                return this.selectedMergeOrdersReflect.some((item , index, arr) => {
                    return item.totalAmount > this.selectedMergeOrders[index].totalAmount
                })
            }
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: '/temporary-deposit-doc/gain-drop-down-parameters'
                }).then(data => {
                    // this.financialContractQueryModels = data.queryAppModels || [];
                    this.customerParameters = data.customerParameters || [];
                    this.tmpDepositUseStatusParameters = data.tmpDepositUseStatusParameters || [];
                    this.feildParameters = data.feildParameters || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleSelectionChange: function(selectedItems) {
                this.selectedMergeOrders = selectedItems;
            },
            deleteItem: function(index) {
                this.selectedMergeOrders.splice(index, 1);
            }
        }
    }
</script>