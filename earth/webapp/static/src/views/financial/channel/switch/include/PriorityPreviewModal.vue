<template>
	<Modal v-model="show">
		<ModalHeader title="各银行通道顺序预览">
		</ModalHeader>
		<ModalBody>
			<div class="row-layout-detail">
				<div class="block">
					<div class="bd">
						<PagingTable :data="dataSource.list" :pagination="true" :perPageRecordNumber="8">
							<el-table-column prop="bankName" label="银行名称">
							</el-table-column>
							<template v-for="n in paymentChannelSize">
								<el-table-column :label="`优先级第${n}位`" inline-template :context="_self">
									<el-popover
										@show="bankChannelLimitPreview(row.paymentChannelList[n-1].paymentChannelUuid,row.bankCode)"
										trigger="hover"
										placement="top">
										<div>
											<div v-if="row.error">
												{{ row.error }}
											</div>
											<template v-if="row.bankChannelLimitData">
												<div>{{ row.bankChannelLimitData.paymentChannelName }}</div><br/>
												<div>{{ row.bankChannelLimitData.bankName }}</div><br/>
												<div><span class="text-muted">单笔限额:</span> {{ row.bankChannelLimitData.transactionLimitPerTranscation}}</div><br/>
<!-- 												<div><span class="text-muted">单日限额:</span> {{ row.bankChannelLimitData.transcationLimitPerDay}}</div><br/>
												<div><span class="text-muted">单月限额:</span> {{ row.bankChannelLimitData.transactionLimitPerMonth}}</div> -->
											</template>
										</div>
										<span slot="reference">{{ row.paymentChannelList[n-1].paymentChannelName }}</span>
									</el-popover>
								</el-table-column>
							</template>
						</PagingTable>
					</div>
				</div>
			</div>
		</ModalBody>
	</Modal>
</template>
<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import modalMixin from './modal-mixin';
	import Pagination  from 'mixins/Pagination';
//	import ListPage from 'mixins/ListPage';

	export default {
		mixins: [Pagination, modalMixin],
		components: {
			PagingTable: require('views/include/PagingTable'),
		},
		props: {
			model: {
				default : null
			}
		},
		watch: {
			model: function(cur) {
				this.currentModal = Object.assign({
					financialContractUuid: '',
					accountSide: '',
					businessType: '',
				},cur);
			},
		},
		data: function() {
			return {
				show: this.value,
				action: '/paymentchannel/switch/paymentChannelOrder',
				autoload:false,

				pageConds: {
					perPageRecordNumber: 9999,
					pageIndex: 1
				},

				// queryConds: {
				// },

				currentModal: Object.assign({}, this.model),
			};
		},
		computed: {
			paymentChannelSize: function() {
				return this.dataSource.list.length > 0 ? this.dataSource.list[0].paymentChannelList.length : 0;
			},
			conditions: function() {
				return Object.assign({}, this.pageConds, this.currentModal);
			}
		},
		methods: {
			bankChannelLimitPreview: function(paymentChannelUuid, bankCode) {
				var { list } = this.dataSource;
				var index = list.findIndex(item => item.bankCode === bankCode);
				if (index === -1) return;

				ajaxPromise({
					url: `/paymentchannel/switch/strategy/bankChannelLimitPreview`,
					data: {
						paymentChannelUuid: paymentChannelUuid,
						bankCode: bankCode,
						accountSide: this.model.accountSide
					}
				}).then(data => {
					this.$set(list[index], 'bankChannelLimitData', data);
				}).catch(message => {
					this.$set(list[index], 'error', message);
				});
			}
		}
	}
</script>