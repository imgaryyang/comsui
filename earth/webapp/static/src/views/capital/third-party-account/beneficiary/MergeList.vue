<style lang="sass">
	.checkedboxs {
		.el-checkbox__label {
			display: none;
		}
	}
</style>
<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form
                    class="sdf-form sdf-query-form"
                    :inline="true">
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
                            placeholder="交易网关">
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
                </el-form>
            </div>
            <div class="table-area">
                <el-table
                    ref="table"
                    row-key="uuid"
                    class="no-table-bottom-border merge-table"
                    stripe
                    @sort-change="onSortChange"
                    @selection-change="handleSelectionChange"
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column
                        type="selection"
                        :reserve-selection="true"
                        width="55">
                    </el-table-column>
                    <el-table-column
                        inline-template
                        label="任务编号">
                        <div>
                            <a v-if="row.auditResult != 'UNKNOWN'"
                                :href="`${ctx}#/capital/third-party-account/beneficiary/${row.uuid}/detail`">
                                {{ row.auditJobNo }}
                            </a>
                            <span v-else>{{ row.auditJobNo }}</span>
                        </div>
                    </el-table-column>
					<el-table-column prop="paymentChannelName" label="通道名称">
                    </el-table-column>
                    <el-table-column prop="financialContractName" label="关联信托">
                    </el-table-column>
                    <el-table-column prop="accountSideName" label="收付类型">
                    </el-table-column>
                    <el-table-column inline-template label="待清算金额">
                        <div>{{ row.totalReceivableAmount | formatMoney }}</div>
                    </el-table-column>
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
                    <el-table-column prop="auditResultName" label="对账状态">
                    </el-table-column>
                    <el-table-column prop="clearingStatusName" label="清算状态">
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
                            <div v-for="(item, index ) in selectedMergeTasks" class="item">
                                <div class="pull-left">
                                    <p class="color-dim">任务编号: {{ item.auditJobNo }}</p>
                                    <p class="color-danger" style="font-size: 12px;">待清算金额: {{ item.totalReceivableAmount | formatMoney }}</p>
                                </div>
                                <i class="delete pull-right" @click="deleteItem(index)"></i>
                            </div>
                            <div v-if="!selectedMergeTasks.length" class="no-data color-dim">暂无清算任务</div>
                        </div>
                        <div class="operation-area">
                            <div class="pull-left">
                                <div>
                                    <span class="color-dim">合并单数：</span><span class="color-danger">{{ selectedMergeTasks.length }}</span>
                                </div>
                                <div>
                                    <span class="color-dim">待清算金额：</span><span class="color-danger">{{ mergeTotalAmount | formatMoney }}</span>
                                </div>
                            </div>
                            <div class="pull-right">
                                <el-button type="primary" @click="showClearingModal" v-if="selectedMergeTasks.length > 0">去清算</el-button>
                            </div>
                        </div>
                    </slot>
                </el-popover>
                <el-badge :value="selectedMergeTasks.length">
                    <el-button key="merge" v-popover:mergePopover>待清算任务</el-button>
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

        <ClearingCashFlowModal
            @submit="fetch"
            v-model="clearingCashFlowModal.visible"
            :model="clearingCashFlowModal.model">
        </ClearingCashFlowModal>
    </div>
</template>

<script>
    import Pagination, { extract } from 'mixins/Pagination';
    import MessageBox from 'components/MessageBox';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise } from 'assets/javascripts/util';
    import { uniqBy } from 'lodash'

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
            ClearingCashFlowModal: require('./include/ClearingCashFlowModal'),
        },
        data: function() {
            return {
                action: '/audit/beneficiary/query?clearFlag=1',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    paymentInstitution: '',
                    auditResult: '',
                    clearingStatus: [0],
                    startTime: '',
                    endTime: '',
                },
                activeKey: '',
                // comboConds: {
                //     merchantNo: '',
                //     pgClearingAccount: '',
                //     textField: ''
                // },
                sortConds: {
                    sortField: '',
                    isAsc: '',
                },

                financialContractQueryModels: [],
                paymentInstitutionNames: [],
                auditResults: [{ key: 1, value: '未平账' },{ key: 2,  value: '已平账' }],
                clearingStatus: [],

                auditResultCode: [],

                clearingCashFlowModal: {
                    visible: false,
                    model: {}
                },

                selectedMergeTasks: [],
            }
        },
        watch: {
            activeKey: function(current) {
                console.log(current)
                if(current == 'merchantNo') {
                    this.comboConds.merchantNo = 1;
                    this.comboConds.pgClearingAccount = '';
                } else if(current == 'pgClearingAccount') {
                    this.comboConds.merchantNo = '';
                    this.comboConds.pgClearingAccount = 1;
                }
            },
        },
        computed: {
            mergeTotalAmount: function() {
                var totalAmount = 0;
                this.selectedMergeTasks.forEach(item => {
                    var amount = parseFloat(item.totalReceivableAmount ? item.totalReceivableAmount : '0');
                    totalAmount += amount;
                })
                return totalAmount;
            },
            mergeAuditJobuuids: function() {
                var uuids = [];
                uuids = this.selectedMergeTasks.map(item => item.uuid);
                return uuids;
            },
        },
        mounted: function() {
            this.checked();
        },
        deactivated: function() {
            this.$refs.table.clearSelection();
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
                return ajaxPromise({
                    url: `/audit/beneficiary/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.paymentInstitutionNames = data.paymentInstitutionNames || [];
                    // this.auditResults = data.auditResults || [];
                    this.clearingStatus = data.clearingStatus || [];
                    this.auditResultCode = data.auditResultCode || [];

                    this.queryConds.paymentInstitution = data.paymentInstitutionNames.map(item => item.key);
                })
            },
            handleSelectionChange: function(rows) {
                this.selectedMergeTasks = rows;
            },
            checked: function() {
                var selected = false;
                this.dataSource.list.forEach(row => {
                    this.selectedMergeTasks.forEach(item => {
                        if (row.uuid == item.uuid) {
                            selected = true;
                            return;
                        }
                    })
                    selected && this.$refs.table.toggleRowSelection(row,true);
                });
            },
            deleteItem: function(index) {
                this.selectedMergeTasks.splice(index, 1);
            },
            showClearingModal: function() {
                var list = this.selectedMergeTasks;
                var a = uniqBy(list, 'pgClearingAccount').length;
                var b = uniqBy(list, 'paymentInstitution').length;
                var c = uniqBy(list, 'merchantNo').length;

                if(a == 1 && b == 1 && c == 1) {
                    this.clearingCashFlowModal.model.totalAmount = this.mergeTotalAmount;
                    this.clearingCashFlowModal.model.auditJobUuidList = this.mergeAuditJobuuids;
                    this.clearingCashFlowModal.visible = true;
                } else {
                    MessageBox.open('仅支持对同一通道进行批量清算')
                }
            },
        }
    }
</script>