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
							v-model="queryConds.status"
							name="status"
							size="small"
							clearable
							placeholder="全部状态">
							<el-option
								v-for="item in journalVoucherStatus"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
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
							<el-option label="支付单号" value="journalVoucherNo"></el-option>
							<el-option label="账户名称" value="virtualAccountName"></el-option>
							<el-option label="账户编号" value="virtualAccountNo"></el-option>
							<el-option label="支付金额" value="amount"></el-option>
							<el-option label="还款编号" value="assetSetNo"></el-option>
							<el-option label="订单编号" value="orderNo"></el-option>
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
						label="支付单号">
						<a class="journal-voucher-no" :href="`${ctx}#/capital/account/payment-order/${ row.journalVoucherUuid }/detail`">
		            		{{ row.journalVoucherNo }}
		            	</a>
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
						label="贷款合同编号">
						<a :href="`${ctx}#/data/contracts/detail?id=${ row.contractId }`">{{ row.contractNo }}</a>
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
						:label="$utils.locale('financialContract.no')">
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
						label="支付金额"
						prop="bookingAmount"
						sortable="custom">
						<div>{{ row.amount | formatMoney }}</div>
					</el-table-column>
					<el-table-column
						inline-template
						label="凭证">
						<a v-if="row.sourceDocumentNo" @click="redirectVoucher(row)" href="javascript: void 0">{{ row.sourceDocumentNo }}</a>
					</el-table-column>
					<el-table-column
						inline-template
						label="状态">
						<div>
							<span v-if="row.statusName == '已退款'" class="color-danger">{{ row.statusName }}</span>
							<span v-else>{{ row.statusName }}</span>
						</div>
					</el-table-column>
					<el-table-column
						inline-template
						label="操作">
							<a v-if="row.canRefund" href="javascript: void 0;" class="refund" @click="handleRefund(row)">退款</a>
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

		<RefundModal
			v-model="refundModal.visible"
			:journalVoucherUuid="refundModal.model.journalVoucherUuid"
		    @submit="fetch"
		    :model="refundModal.model">
		</RefundModal>
	</div>

</template>

<script>
	import Pagination from 'mixins/Pagination';
	import ListPage from 'mixins/ListPage';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import RefundModal from './RefundModal';

	export default {
		mixins: [Pagination, ListPage],
		components: {
			RefundModal,
			ComboQueryBox: require('views/include/ComboQueryBox'),
		},
		data: function() {
			console.log('ddd')
			return {
				action: '/capital/customer-account-manage/payment-order-list/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					status: '',
					endDate: '',
					startDate: ''
				},
				comboConds: {
					journalVoucherNo: '',
					virtualAccountName: '',
					virtualAccountNo: '',
					amount: '',
					assetSetNo: '',
					orderNo: '',
				},
				sortConds: {
					sortField: '',
					isAsc: ''
				},
				financialContractQueryModels: [],
				journalVoucherStatus:[],

				refundModal: {
					visible: false,
					model: {}
				}
			};
		},
		methods: {
			initialize: function() {
				return ajaxPromise({
					url: `/capital/customer-account-manage/payment-order-list/show/options`
				}).then(data => {
					this.financialContractQueryModels = data.queryAppModels || [];
					this.journalVoucherStatus = data.journalVoucherStatus || [];

					// var queryConds = this.queryConds;

				}).catch(message => {
					MessageBox.open(message);
				});
			},
			redirectVoucher: function(data) {
			    ajaxPromise({
			        url: `/capital/customer-account-manage/payment-order-list/query-voucher-detail`,
			        data: {
			            sourceDocumentUuid: data.sourceDocumentUuid
			        },
			        parse: data => data.voucher
			    }).then(voucher => {
			        var href = '';

			        if (!voucher) return;

			        if (voucher.voucherSource === '商户付款凭证') {
			            href = `${this.ctx}#/capital/voucher/business/${voucher.id}/detail`;
			        } else if (voucher.voucherSource === '主动付款凭证') {
			            href = `${this.ctx}#/capital/voucher/active/${voucher.voucherNo}/detail`;
			        } else if (voucher.voucherSource === '第三方扣款凭证') {
			            href = `${this.ctx}#/capital/voucher/third-party/${voucher.uuid}/detail`;
			        }

			        href && location.assign(href);
			    });
			},
			handleRefund: function(data) {
				this.refundModal.visible = true;
				this.refundModal.model = Object.assign({}, data);
			}
		}
	}
</script>
