<style lang="sass">
	
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
                            v-model="queryConds.repaymentStatus"
                            placeholder="订单状态"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in repaymentStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.sourceStatus"
                            placeholder="订单来源"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in orderSourceStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    v-model="queryConds.startDateString"
                                    :end-date="queryConds.endDateString"
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
                                    v-model="queryConds.endDateString"
                                    :start-date="queryConds.startDateString"
                                    placeholder="创建终止时间"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="订单编号" value="orderUuid"></el-option>
                            <el-option label="订单总金额" value="orderAmount"></el-option>
                            <el-option label="商户订单号" value="orderUniqueId"></el-option>
                            <el-option label="贷款人" value="firstCustomerName"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" :disabled="dataSource.size == 0 || dataSource.size == undefined" @click="handleExport">导出</el-button>
                        <el-button size="small" type="success" @click="$router.push({path: '/finance/repayment-order/create', query: {t: new Date().getTime()}})">新增</el-button>
                    </el-form-item>
                </el-form>
			</div>
			<div class="table-area">
				<el-table
					stripe
                    class="no-table-bottom-border"
                    @sort-change="onSortChange"
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column label="订单编号" prop="orderUuid" inline-template>
                        <a :href="`${ctx}#/finance/repayment-order/${row.orderUuid}/detail`">{{row.orderUuid }}</a>
                    </el-table-column>
                    <el-table-column label="商户订单号" prop="orderUniqueId">
                    </el-table-column>
                    <el-table-column :label="$utils.locale('financialContract.no')" prop="financialContractNo" inline-template>
                        <a :href="`${ctx}#/financial/contract/${row.financialContractUuid}/detail`">{{row.financialContractNo}}</a>
                    </el-table-column>
                    <el-table-column label="贷款人" prop="firstCustomerName">
                    </el-table-column>
                    <el-table-column label="订单总金额" prop="orderAmount" inline-template sortable="custom">
                        <div>{{ row.orderAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="订单状态" prop="chineseRepaymentStatus">
                    </el-table-column>
                    <el-table-column label="创建时间" prop="orderCreateTime" inline-template sortable="custom">
                        <div>{{ row.orderCreateTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" prop="orderLastModifiedTime" inline-template sortable="custom">
                        <div>{{ row.orderLastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="订单来源" prop="orderSourceStatusCh">
                    </el-table-column>
                    <el-table-column label="备注" prop="remark">
                    </el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
            <div class="pull-left">
                <el-button @click="$router.push({path: '/finance/repayment-order/merge', query: {t: new Date().getTime()}})">合并订单</el-button>
            </div>
			<div class="pull-right">
				<PageControl 
	                v-model="pageConds.pageIndex"
	                :size="dataSource.size"
	                :per-page-record-number="pageConds.perPageRecordNumber">
	            </PageControl>
			</div>
		</div>

        <ExportOrderModal
            v-model="exportOrderModal.visible"
            :model="exportOrderModal.model">
        </ExportOrderModal>
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
			ComboQueryBox: require('views/include/ComboQueryBox'),
            ExportOrderModal: require('./include/ExportOrderModal')
		},
		data: function() {
			return {
				action: '/repayment-order/repayment/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					repaymentStatus: '',
                    sourceStatus: '',
					startDateString: '',
					endDateString: ''
				},
				comboConds: {
                    orderUuid: '',
                    orderAmount: '',
                    orderUniqueId: '',
                    firstCustomerName: ''
				},

                sortConds: {
                    sortField: '',
                    isAsc: ''
                },

				financialContractQueryModels: [],
				repaymentStatus: [],
                orderSourceStatus: [],
                normalOrders: true,
                selectedMergeOrders: [],

                exportOrderModal: {
                    visible: false,
                    model: {
                        conditions: {},
                        detailNumberSum: ''
                    }
                }
			}
		},
		methods: {
			initialize: function() {
                this.normalOrders = true;
				return ajaxPromise({
					url: '/repayment-order/repayment/optionsData'
				}).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.repaymentStatus = data.repaymentStatus || [];
                    this.orderSourceStatus = data.orderSourceStatus || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
            handleExport: function() {
                // if (this.queryConds.financialContractUuids.length > 1) {
                //     MessageBox.open('信托合同只能选一个');
                //     return
                // }
                ajaxPromise({
                    url: '/repayment-order/queryExportOrderFileNumber',
                    data: purify(Object.assign({}, this.conditions))
                }).then(data => {
                    this.exportOrderModal.model.detailNumberSum = data.detailNumberSum;
                    this.exportOrderModal.model.conditions = this.conditions;
                    this.exportOrderModal.visible = true;
                    // downloadFile(`${this.api}/repayment-order/exportRepaymentOrderFile`,this.conditions);
                }).catch(message => {
                    MessageBox.open(message);
                });

            }
		}
	}
</script>