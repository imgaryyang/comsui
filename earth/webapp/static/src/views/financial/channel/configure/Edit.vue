<!-- 通道名称唯一性检测
alias => ordinal
file upload
限额预览 -->

<template>
	<div class="content">
		<div class="scroller" v-loading="fetching">
		    <Breadcrumb :routes="[
		        { title: '通道配置' }, 
		        { title: '配置通道' }
		    ]">
		    </Breadcrumb>

		    <div style="padding: 10px 50px;">
		        <div style="margin-top: 20px;">
		        	<Basis 
		        		ref="basis" 
		        		:paymentChannelUuid="$route.params.paymentChannelUuid"
		        		:model="basis"></Basis>
		        </div>
		        <div style="margin-top: 20px;">
		        	<Transaction 
		        		ref="debit" 
		        		:accountSide="1"
                		:showPreview="true"
		        		:model="debitChannelConfigure"
		        		:chargeRateMode="chargeRateMode" 
		        		:chargeExcutionMode="chargeExcutionMode" 
		        		:channelWorkingStatus="channelWorkingStatus" 
		        		:outlierChannelName="basis.outlierChannelName"
		        		:workingStatus="basis.debitChannelWorkingStatus"
		        		:paymentInstitutionOrdinal="basis.paymentInstitutionOrdinal"
		        		@preview="handlerPreview(basis.paymentInstitutionOrdinal, basis.outlierChannelName, 1)" 
		        		title="收款交易"></Transaction>
		        </div>
                <div style="margin-top: 20px;">
                	<Transaction 
                		ref="credit" 
                		:accountSide="0"
                		:showPreview="true"
                		:model="creditChannelConfigure" 
                		:chargeRateMode="chargeRateMode" 
                		:chargeExcutionMode="chargeExcutionMode" 
                		:channelWorkingStatus="channelWorkingStatus" 
                		:workingStatus="basis.creditChannelWorkingStatus"
                		:paymentInstitutionOrdinal="basis.paymentInstitutionOrdinal"
                		:outlierChannelName="basis.outlierChannelName"
                		@preview="handlerPreview(basis.paymentInstitutionOrdinal, basis.outlierChannelName, 0)" 
                		title="付款交易"></Transaction>
                </div>
                <div style="margin-left: 12%; margin-top: 30px;">
                	<el-button style="margin-left: 120px" type="primary" @click="submit($route.params.paymentChannelUuid)">提交</el-button>
                </div>
		    </div>
		</div>

		<PreviewModal
			v-model="previewModal.show"
			:model="previewModal.model">
		</PreviewModal>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		components: { 
		    Transaction : require('./include/Transaction'),
		    Basis : require('./include/Basis'),
		    PreviewModal: require('./include/PreviewModal'),
		},
		data: function() {
			return {
				fetching: false,
				channelWorkingStatus: [],
				chargeRateMode: [],
				chargeExcutionMode: [],
				basis: {},
				creditChannelConfigure: {},
				debitChannelConfigure: {},
				relatedFinancialContractUuid: '',

				previewModal: {
					model: {},
					show: false
				}
			};
		},
		activated: function() {
			this.fetch(this.$route.params.paymentChannelUuid);	
		},
		methods: {
			handlerPreview: function(paymentInstitutionOrdinal,outlierChannelName,accountSide) {
				this.previewModal.model = {
					paymentInstitutionOrdinal: paymentInstitutionOrdinal,
                    outlierChannelName: outlierChannelName,
                    accountSide: accountSide
				};	
				this.previewModal.show = true;
			},
			fetch: function(paymentChannelUuid) {
				this.fetching = true;
				return ajaxPromise({
					url: `/paymentchannel/config/${paymentChannelUuid}`
				}).then(data => {
					const {
						channelWorkingStatus, 
						chargeRateMode, 
						chargeExcutionMode, 
						paymentChannelConfigData,
						relatedFinancialContractUuid,
						...basis
					} = data;

					this.channelWorkingStatus = channelWorkingStatus;
					this.chargeRateMode = chargeRateMode;
					this.chargeExcutionMode = chargeExcutionMode;
					this.relatedFinancialContractUuid = relatedFinancialContractUuid;

					this.basis = basis;
					var _paymentChannelConfigData = JSON.parse(paymentChannelConfigData);
					var def = {
						channelStatus: 'NOTLINK', // 通道状态 停用、开启等
						chargeExcutionMode: '', // 费用模式 向前 向后 等
						chargePerTranscation: '', // 单笔固定时的单笔费用
						clearingInterval: '', // 清算周期
						side: '', // 收款方 付款方（应该用不到）
						trasncationLimitPerTransaction: '', // 通道单笔限额
						chargeRateMode: '', // 通道扣率模式 单笔固定 单笔比例
						chargeRatePerTranscation: '', // 比率
						upperChargeLimitPerTransaction: '', // 最高收取
						lowerestChargeLimitPerTransaction: '', // 最低收取
						trasncationLimitPerTransactionMin: '',//通道单笔限额小值
		                trasncationLimitPerTransactionMax: '',//通道单笔限额大值
						bankLimitationFileKey: '',
						bankLimitationFileName: ''
					}
					this.creditChannelConfigure = Object.assign({}, def, _paymentChannelConfigData.creditChannelConfigure)
					this.debitChannelConfigure = Object.assign({}, def, _paymentChannelConfigData.debitChannelConfigure)

				}).catch(message => {
					MessageBox.open(message);
				}).then(() => {
					this.fetching = false;
				})
			},
			submit: function(paymentChannelUuid) {
				var { basis, credit, debit } = this.$refs;
				Promise.all([basis.validate(), credit.validate(), debit.validate()])
					.then(valids => {
						const result = valids.every(valid => valid);
						if (result) {
							const { paymentChannelName } = basis.save();
							const creditChannelConfigure = credit.save();
							const debitChannelConfigure = debit.save();

							ajaxPromise({
							    url: `/paymentchannel/config/${paymentChannelUuid}/save`,
							    type: 'post',
							    data: {
							    	paymentChannelUuid,
							    	paymentChannelName,
							    	data: JSON.stringify({
							    		creditChannelConfigure,
							    		debitChannelConfigure
							    	})
							    }
							}).then(data => {
							    MessageBox.open('提交成功是否前去切换通道？', '提示', [{
								    	text: '否',
								    	handler: () => {
								    		MessageBox.close();
								    		location.assign(`${this.ctx}#/financial/channel/configure/list`);
								    	}
								    },{
								    	text: '是',
								    	type: 'success',
								    	handler: () => {
								    		MessageBox.close();
								    		location.assign(`${this.ctx}#/financial/channel/switch/detail/${this.relatedFinancialContractUuid}`);
								    	}
								}]);
							}).catch(message => {
							    MessageBox.open(message);
							});
						}
					}).catch(errors => {
						MessageBox.open(errors);
					});
			}
		},
	}
</script>