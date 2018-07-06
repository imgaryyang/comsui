<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{title: '还款订单详情'}]">
				<template v-if="currentModel.repaymentOrderStatusOrdinal == 3 || currentModel.repaymentOrderStatusOrdinal == 5">
					<el-button
						type="primary"
						size="small"
						v-if="currentModel.sourceStatusOrdinal == 0 || currentModel.sourceStatusOrdinal == 2"
						@click="cancelOrder">
						撤销订单
					</el-button>
					<el-button
						v-if="currentModel.sourceStatusOrdinal == 1 && currentModel.orderAliveStatus == 0 "
						type="primary"
						size="small"
						@click="splitOrder">
						拆分订单
					</el-button>
				</template>
				<el-button
					type="primary"
					size="small"
					v-if="currentModel.repaymentOrderStatusOrdinal >= 3 && currentModel.repaymentOrderStatusOrdinal <= 5 && currentModel.showPay"
					@click="payOrder">
					订单支付
				</el-button>
				<el-button
					type="primary"
					size="small"
					v-if="false"
					@click="reCallback">
					重新回调
				</el-button>
				<el-button
					type="primary"
					size="small"
					v-if="currentModel.canRedoRecover"
					@click="redoReconciliation">
					重新核销
				</el-button>
			</Breadcrumb>
			<div class="col-layout-detail">
				<div class="top">
					<div class="block">
						<h5 class="hd">订单信息</h5>
						<div class="bd">
							<div class="col">
								<p>商户订单号：{{ currentModel.orderUniqueId }}</p>
								<p>五维订单号：{{ currentModel.orderUuid }}</p>
								<p>{{$utils.locale('financialContract.no')}}：<a :href="`${ctx}#/financial/contract/${ currentModel.financialContractUuid }/detail`">{{ currentModel.financialContractNo }}</a></p>
								<p>{{$utils.locale('financialContract.name')}}：{{ currentModel.financialContractName }}</p>
								<p>订单状态：{{ currentModel.chineseRepaymentStatus }} </p>
							</div>
							<div class="col">
								<p>订单总金额：{{ currentModel.orderAmount | formatMoney }}</p>
								<p>已支付金额：{{ currentModel.paidAmount | formatMoney }} </p>
								<p>创建时间：{{ currentModel.orderCreateTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
								<p>状态变更时间：{{ currentModel.orderLastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
							</div>
							<div class="col">
								<p>计划回调次数：-- </p>
								<p>实际回调次数：{{ currentModel.actualNotifyNums }}</p>
								<p>订单来源：{{ currentModel.sourceStatusCh }} </p>
								<p>备注：{{ currentModel.remark }}</p>
							</div>
						</div>
					</div>
				</div>
			</div>

			<RepaymentOrderItemsTable
				title="订单明细"
				:autoload="true"
				:action="`/repayment-order/get-repayment-order-items`"
				:orderUuid="orderUuid"
				:repaymentOrderStatusOrdinal="currentModel.repaymentOrderStatusOrdinal"
				v-model="refreshTable"
				key="repayment-order-items">
			</RepaymentOrderItemsTable>

			<div class="row-layout-detail">
				<QueryTable
					title="来源订单"
					key="order-source"
					v-model="refreshTable"
					:orderUuid="orderUuid"
					:action="`/repayment-order/obtainOrderSource`"
					v-if="currentModel.sourceStatusOrdinal == 1">
					<el-table-column label="订单编号" prop="orderUuid" inline-template>
						<a :href="`${ctx}#/finance/repayment-order/${row.orderUuid}/detail`">{{ row.orderUuid }}</a>
					</el-table-column>
					<el-table-column label="商户订单号" prop="orderUniqueId" >
					</el-table-column>
					<el-table-column label="订单总金额" prop="orderAmount" inline-template>
						<div>{{ row.orderAmount | formatMoney }}</div>
					</el-table-column>
					<el-table-column label="订单状态" prop="orderStatus" >
					</el-table-column>
					<el-table-column label="状态变更时间" prop="statusModifyTime" inline-template>
						<div>{{ row.statusModifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
					</el-table-column>
				</QueryTable>

				<QueryTable
					title="支付明细"
					:action="`/repayment-order/repayment/get-payment-orders`"
					:orderUuid="orderUuid"
					v-model="refreshTable"
					key="payment-orders">
					<el-table-column label="支付编号" prop="" inline-template>
						<a :href="`${ctx}#/capital/payment-cash-flow/payment-order/${ row.uuid }/detail`">{{ row.uuid }}</a>
					</el-table-column>
					<el-table-column label="支付方式" prop="payWayChinese">
					</el-table-column>
					<el-table-column label="支付通道" prop="paymentGateWay">
					</el-table-column>
					<el-table-column label="通道交易号" prop="tradeUuid">
					</el-table-column>
					<el-table-column label="交易金额" prop="amount" inline-template>
						<div>{{ row.amount | formatMoney }}</div>
					</el-table-column>
					<el-table-column label="交易时间" prop="transactionTime" inline-template>
						<div>{{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
					</el-table-column>
					<el-table-column label="支付状态" prop="paymentOrderStatusChinese" >
					</el-table-column>
				</QueryTable>

				<div class="block">
					<h5 class="hd">相关凭证</h5>
					<PagingTable
						:loading="repaymentOrderLoading"
						:data="repaymentOrderOfVouchers">
						<el-table-column label="凭证编号" prop="voucherNo" inline-template>
							<a :href="`${ctx}#/capital/voucher/third-pay/history-voucher/${row.voucherNo}/detail`">{{row.voucherNo}}</a>
						</el-table-column>
						<el-table-column label="专户账号" prop="receivableAccountNo" >
						</el-table-column>
						<el-table-column label="往来机构名称" prop="paymentBank" >
						</el-table-column>
						<el-table-column label="账户名" prop="paymentName" >
						</el-table-column>
						<el-table-column label="机构账户号" prop="paymentAccountNo" >
						</el-table-column>
						<el-table-column label="凭证金额" prop="transcationAmount" inline-template>
							<div>{{ row.transcationAmount | formatMoney }}</div>
						</el-table-column>
						<el-table-column label="凭证类型" prop="voucherType" >
						</el-table-column>
						<el-table-column label="凭证内容" prop="remark" >
						</el-table-column>
						<el-table-column label="凭证来源" prop="voucherSource" >
						</el-table-column>
						<el-table-column label="凭证状态" prop="voucherLogIssueStatus" >
						</el-table-column>
					</PagingTable>
				</div>

				<div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="orderUuid">
                    </SystemOperateLog>
                </div>
			</div>
		</div>

		<ReCallBackModal
			v-model="reCallbackModal.show"
			:id="orderUuid"
			@submit="refreshDetail(orderUuid)">
		</ReCallBackModal>

		<HandleModal
			v-model="handleModal.show"
			:orderUuid="orderUuid"
			:title="handleModal.title"
			:action="handleModal.action"
			@submit="refreshDetail(orderUuid)">
		</HandleModal>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		components: {
			SystemOperateLog: require('views/include/SystemOperateLog'),
			PagingTable: require('views/include/PagingTable'),
			ReCallBackModal: require('./include/ReCallBackModal'),
			HandleModal: require('./include/HandleModal'),
			QueryTable: require('./include/QueryTable'),
			RepaymentOrderItemsTable: require('./include/RepaymentOrderItemsTable')
		},
		data: function() {
			return {
				fetching: false,
				orderUuid: '',
				refreshTable: false,

				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},

				reCallbackModal: {
					show: false,
				},

				handleModal: {
					show: false,
					title: '',
					action: ''
				},
				currentModel: { 
					orderUniqueId: '',
					orderUuid: '',
					financialContractNo: '',
					orderAmount: '',
					orderCreateTime: '',
					orderLastModifiedTime: '',
					remark: '',
					repaymentOrderStatusOrdinal: '',
					chineseRepaymentStatus: '',
					canRedoRecover: false
				},
				repaymentOrderOfVouchers: [],
				repaymentOrderLoading: false,
			}
		},
		watch: {
			'$route': function(to, from) {
				if (from.name == 'repaymentOrder' && to.name == 'repaymentOrder' && to.params.uuid != undefined) {
					this.orderUuid = to.params.uuid;
				}
			},
			orderUuid: function(current) {
				this.fetchDetail(current);
				this.fetchRepaymentOrderOfVoucher(current);
			}
		},
		activated: function() {
			this.orderUuid = this.$route.params.uuid;
		},
		methods: {
			refreshDetail: function(orderUuid) {
				if (!orderUuid) return;
				this.fetchDetail(orderUuid);
				this.fetchRepaymentOrderOfVoucher(orderUuid)
				this.refreshTable = true;
                this.$refs.sysLog.fetch();
			},
			fetchDetail: function(uuid) {
				if (this.fetching) return;
				this.fetching = true;

				ajaxPromise({
					url: '/repayment-order/repayment/detail-data',
					data: {
						orderUUid: uuid
					}
				}).then(data => {
					this.currentModel = Object.assign({}, data.model);
				}).catch(message => {
					MessageBox.open(message);
				}).then(() => {
					this.fetching = false;
				});
			},
			cancelOrder: function() {
				var modal = this.handleModal;
				modal.show = true;
				modal.title = '撤销订单';
				modal.action = '/repayment-order/cancelRepaymentOrder';
			},
			splitOrder: function() {
				var modal = this.handleModal;
				modal.show = true;
				modal.title = '拆分订单';
				modal.action = '/repayment-order/cancelMergeRepaymentOrder';
			},
			payOrder: function() {
				this.$router.push({path: '/finance/repayment-order/create-payment-order', query: {orderUuid: this.orderUuid }})
			},
			reCallback: function() {
				var modal = this.reCallbackModal;
				modal.show = true;
			},
			redoReconciliation: function() {
				var modal = this.handleModal;
				modal.show = true;
				modal.title = '重新核销';
				modal.action = '/repayment-order/redoReconciliation';
			},
			fetchRepaymentOrderOfVoucher: function(orderUuid) {
				if(this.repaymentOrderLoading) return;
				this.repaymentOrderLoading = true;
				ajaxPromise({
					url: '/repayment-order/repayment/get-payment-vouchers',
					data: {
						orderUuid: orderUuid
					}
				}).then(data => {
					this.repaymentOrderOfVouchers = [].concat(data.list || []);
				}).catch(message => {
					MessageBox.open(message);
				}).then(() => {
					this.repaymentOrderLoading = false;
				});
			}
		}
	}
</script>