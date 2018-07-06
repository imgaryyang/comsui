<template>
	<div class="content">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{title: $utils.locale('financialContract') + '通道详情'}]">
			</Breadcrumb>
			<el-form
				:model="currentModel"
				label-width="120px"
				class="sdf-form">
				<el-form-item label="基础信息" class="form-item-legend">
				</el-form-item>
				<el-form-item :label="$utils.locale('financialContract.name')" prop="contractName" required>
					{{ currentModel.contractName }}
				</el-form-item>
				<el-form-item :label="$utils.locale('financialContract.no')" prop="contractNo" required>
					{{ currentModel.contractNo }}
				</el-form-item>
			</el-form>

			<el-form
				:model="currentModel"
				label-width="120px"
				class="sdf-form">
				<el-form-item label="自有通道服务" class="form-item-legend">
				</el-form-item>
				<el-form-item label="清算账户行(号)" prop="bankNameUnionAccountNo" required>
					{{ currentModel.bankNameUnionAccountNo }}
				</el-form-item>
				<el-form-item label="收款通道策略" required>
					<ChannelTableView
						:paymentChannelMode="currentModel.switchDetailInfo.debitPaymentChannelMode"
						:paymentChannelModeData="currentModel.switchDetailInfo.debitPaymentChannelModeData"
						@priorityPreview="showPriorityPreviewModal($event,1,0)"
						>
					</ChannelTableView>
					<el-button @click.prevent="showChannelStrategyModal(1, 0)">设置策略</el-button>
				</el-form-item>
				<el-form-item label="收款通道轮询" required>
					<PollingTableView :pollings="currentModel.switchDetailInfo.debitPaymentChannelPolling"></PollingTableView>
					<el-button type="primary" @click.prevent="showPollingModal(1,0,currentModel.switchDetailInfo.debitPaymentChannelPolling)">设置</el-button>
				</el-form-item>
				<el-form-item label="付款通道策略" required>
					<CreditChannelTableView
						:paymentChannelMode="currentModel.switchDetailInfo.creditPaymentChannelMode"
						:paymentChannelModeData="currentModel.switchDetailInfo.creditPaymentChannelModeData"
						@priorityPreview="showPriorityPreviewModal($event,0,0)"
						>
					</CreditChannelTableView>
					<el-button @click.prevent="showChannelStrategyModal(0, 0)">设置策略</el-button>
				</el-form-item>
			</el-form>

			<el-form
				:model="currentModel"
				label-width="120px"
				class="sdf-form"
				v-if="false">
				<el-form-item label="委托通道服务" class="form-item-legend">
				</el-form-item>
				<el-form-item label="清算账户行(号)" prop="bankNameUnionAccountNo" required>
					{{ currentModel.bankNameUnionAccountNo }}
				</el-form-item>
				<el-form-item label="收款通道策略" required>
					<ChannelTableView
						:paymentChannelMode="currentModel.switchDetailInfo.acDebitPaymentChannelMode"
						:paymentChannelModeData="currentModel.switchDetailInfo.acDebitPaymentChannelModeData"
						@priorityPreview="showPriorityPreviewModal($event,1,1)"
						>
					</ChannelTableView>
					<el-button @click.prevent="showChannelStrategyModal(1, 1)">设置策略</el-button>
				</el-form-item>
				<el-form-item label="收款通道轮询" required>
					<PollingTableView :pollings="currentModel.switchDetailInfo.acDebitPaymentChannelPolling"></PollingTableView>
					<el-button type="primary" @click.prevent="showPollingModal(1,1,currentModel.switchDetailInfo.acDebitPaymentChannelPolling)">设置</el-button>
				</el-form-item>
				<el-form-item label="付款通道策略" required>
					<CreditChannelTableView
						:paymentChannelMode="currentModel.switchDetailInfo.acCreditPaymentChannelMode"
						:paymentChannelModeData="currentModel.switchDetailInfo.acCreditPaymentChannelModeData"
						@priorityPreview="showPriorityPreviewModal($event,0,1)"
						>
					</CreditChannelTableView>
					<el-button @click.prevent="showChannelStrategyModal(0, 1)">设置策略</el-button>
				</el-form-item>
			</el-form>
		</div>

		<PriorityPreviewModal
			:model="priorityPreviewModal.model"
			:accountSide="accountSideFlag"
			v-model="priorityPreviewModal.show">
		</PriorityPreviewModal>

		<QuotaPreviewModal
			:model="quotaPreviewModal.model"
			:accountSide="accountSideFlag"
			v-model="quotaPreviewModal.show">
		</QuotaPreviewModal>

		<DebitPaymentChannelStrategyModal
			:model="debitChannelStrategyModal.model"
			v-model="debitChannelStrategyModal.show"
			@confirm="fetchDetail($route.params.financialContractUuid)">
		</DebitPaymentChannelStrategyModal>

		<CreditPaymentChannelStrategyModal
			:model="creditChannelStrategyModal.model"
			v-model="creditChannelStrategyModal.show"
			@confirm="fetchDetail($route.params.financialContractUuid)">
		</CreditPaymentChannelStrategyModal>

		<PollingModal
			:model="pollingModal.model"
			v-model="pollingModal.show"
			@confirm="fetchDetail($route.params.financialContractUuid)">
		</PollingModal>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		components: {
			PriorityPreviewModal: require('./include/PriorityPreviewModal'),
			DebitPaymentChannelStrategyModal: require('./include/DebitPaymentChannelStrategyModal'),
			CreditPaymentChannelStrategyModal: require('./include/CreditPaymentChannelStrategyModal'),
			ChannelTableView: require('./include/ChannelTableView'),
			CreditChannelTableView: require('./include/CreditChannelTableView'),
			QuotaPreviewModal: require('./include/QuotaPreviewModal'),
			PollingTableView: require('./include/PollingTableView'),
			PollingModal: require('./include/PollingModal')
		},
		data: function() {
			return {
				fetching: false,
				currentModel: {
					contractName: '',
					contractNo: '',
					bankNameUnionAccountNo: '',
					switchDetailInfo: {
						creditPaymentChannelData: [],
						creditPaymentChannelMode: '',
						debitPaymentChannelModeData: [],
						debitPaymentChannelMode: '',
						acDebitPaymentChannelMode: '',
						acDebitPaymentChannelModeData: [],
						acCreditPaymentChannelMode: '',
						acCreditPaymentChannelModeData: [],
					}
				},
				priorityPreviewModal: {
					show: false,
					model: {

					}
				},
				quotaPreviewModal: {
					show: false,
					model: {

					}
				},
				debitChannelStrategyModal: {
					show: false,
					model: {

					}
				},
				creditChannelStrategyModal: {
					show: false,
					model: {

					}
				},
				accountSideFlag: '',

				pollingModal: {
					show: false,
					model: {}
				}
			}
		},
		activated: function() {
			this.fetchDetail(this.$route.params.financialContractUuid);
		},
		methods: {
			fetchDetail: function(financialContractUuid) {
				this.fetching = true;
				return ajaxPromise({
					url: `/paymentchannel/switch/getDetailData/${financialContractUuid}`
				}).then(data => {
					this.currentModel = data;
				}).catch(message => {
					MessageBox.open(message);
				}).then(() => {
					this.fetching = false;
				})
			},
			showPriorityPreviewModal: function($event,accountSide, businessType) {
// $event == 'ISSUINGBANKFIRST','QUOTAPRIORITY'
				this.accountSideFlag = accountSide;
				if( $event == 'ISSUINGBANKFIRST'){
					var { priorityPreviewModal } = this;
					priorityPreviewModal.show = true;
					priorityPreviewModal.model = {
						financialContractUuid: this.$route.params.financialContractUuid,
						accountSide: accountSide,
						businessType: businessType,
					}
				}else if($event == 'QUOTAPRIORITY') {
					var actionParams = {
						financialContractUuid: this.$route.params.financialContractUuid,
						accountSide: accountSide,
						businessType: businessType,
					}
					ajaxPromise({
						url: `/paymentchannel/switch/strategy/step/2`,
						data: actionParams
					}).then(data => {
						this.quotaPreviewModal.model = data.list;
						this.quotaPreviewModal.show = true;
					}).catch(message => {
						MessageBox.open(message);
					});
				}
			},
			showChannelStrategyModal: function(accountSide, businessType) {
				//收款 1	委托通道服务 1
				//付款 0	自有通道服务 0
				this.accountSideFlag = accountSide;

				if(accountSide == 1) {
					var { debitChannelStrategyModal } = this;
					debitChannelStrategyModal.show = true;
					debitChannelStrategyModal.model = {
						financialContractUuid: this.$route.params.financialContractUuid,
						accountSide: accountSide,
						businessType: businessType,
					}
				}else if(accountSide == 0){
					var { creditChannelStrategyModal } = this;
					creditChannelStrategyModal.show = true;
					creditChannelStrategyModal.model = {
						financialContractUuid: this.$route.params.financialContractUuid,
						accountSide: accountSide,
						businessType: businessType,
					}
				}
			},
			showPollingModal: function(accountSide, businessType, pollings) {
				var { pollingModal }= this;
				pollingModal.show = true;
				pollingModal.model = {
					financialContractUuid: this.$route.params.financialContractUuid,
					businessType: businessType,
					accountSide: accountSide,
					open: pollings && pollings.length ? 1 : 0,
					pollings: pollings
				};
			}
		}
	}
</script>