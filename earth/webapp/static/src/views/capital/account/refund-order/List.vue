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
									v-model="queryConds.startDate"
									name="startDate"
									pickTime="true"
									formatToMinimum="true"
									:end-date="queryConds.endDate"
									placeholder="创建起始日期"
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
									v-model="queryConds.endDate"
									name="endDate"
									pickTime="true"
									formatToMaximum="true"
									:start-date="queryConds.startDate"
									placeholder="创建终止日期"
									size="small">
								</DateTimePicker>
							</el-form-item>
						</el-col>
					</el-form-item>
					<el-form-item>
						<span class="item vertical-line"></span>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="退款单号" value="journalVoucherRefundNo"></el-option>
							<el-option label="账户名称" value="virtualAccountNameRefund"></el-option>
							<el-option label="账户编号" value="virtualAccountNoRefund"></el-option>
							<el-option label="退款金额" value="amountRefund"></el-option>
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
					stripe
					@sort-change="onSortChange"
					v-loading="dataSource.fetching"
					:data="dataSource.list">
					<el-table-column
						inline-template
						label="退款单号">
						<a :href="`${ctx}#/capital/account/refund-order/${ row.journalVoucherRefundUuid }/detail`">{{ row.journalVoucherRefundNo }}</a>
					</el-table-column>
					<el-table-column
						inline-template
						label="账户编号">
						<a :href="`${ctx}#/capital/account/virtual-acctount/${ row.virtualAccountUuid }/detail`">{{ row.virtualAccountNo }}</a>
					</el-table-column>
					<el-table-column prop="virtualAccountName" label="账户名称">
					</el-table-column>
					<el-table-column
						inline-template
						label="还款编号">
						<a :href="`${ctx}#/finance/assets/${ row.assetSetUuid }/detail`">{{ row.assetSetNo }}</a>
					</el-table-column>
					<el-table-column prop="outerRepaymentPlanNo" label="商户还款计划编号">
					</el-table-column>
					<el-table-column
						inline-template
						label="订单编号">
							<div>
								<a v-if="row.orderTypeName == '结算单'" :href="`${ctx}#/finance/payment-order/${row.orderId}/detail`">{{ row.orderNo }}</a>
								<a v-else :href="`${ctx}#/finance/guarantee/complement/${row.orderId}/detail`">{{ row.orderNo }}</a>
							</div>
					</el-table-column>
					<el-table-column
						inline-template
						label="支付单号">
						<a :href="`${ctx}#/capital/account/payment-order/${ row.journalVoucherUuid }/detail`">{{ row.journalVoucherNo }}</a>
					</el-table-column>
					<el-table-column
						inline-template
						:label="$utils.locale('financialContract')">
						<a :href="`${ctx}#/financial/contract/${ row.financialContractUuid }/detail`">{{ row.financialContractNo }}</a>
					</el-table-column>
					<el-table-column
						inline-template
						label="创建时间"
						prop="createTime"
						sortable="custom">
						<div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
					</el-table-column>
					<el-table-column
						inline-template
						label="退款金额"
						prop="bookingAmount"
						sortable="custom">
						<div>{{ row.amount | formatMoney}}</div>
					</el-table-column>
					<el-table-column prop="summary" label="备注">
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
				action: '/capital/customer-account-manage/refund-order-list/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					endDate: '',
					startDate: ''
				},
				comboConds: {
					journalVoucherRefundNo: '',
					virtualAccountNameRefund: '',
					virtualAccountNoRefund: '',
					amountRefund: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},
				financialContractQueryModels: [],
				exportModal: {
					show: false
				},
			};
		},
		// watch: {
		// 	queryConds: {
  //               deep: true,
  //               handler: function() {
  //                   this.pageConds.pageIndex = 1;
  //               }
  //           },
  //           comboConds: {
  //           	deep: true,
  //           	handler: function() {
  //           		this.pageConds.pageIndex = 1;
  //           	}
  //           }
		// },
		// computed: {
		// 	conditions: function() {
  //               return Object.assign({}, this.queryConds, this.comboConds, this.sortConds, this.pageConds);
  //           }
		// },
		methods: {
			initialize: function() {
				return ajaxPromise({
					url: `/capital/customer-account-manage/refund-order-list/show/options`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
					// var queryConds = this.queryConds;

				}).catch(message => {
					MessageBox.open(message);
				});
			},
		}
	}
</script>
