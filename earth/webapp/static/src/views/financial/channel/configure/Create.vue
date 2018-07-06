<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller">
			<Breadcrumb :routes="[
		        { title: '通道配置' }, 
		        { title: '新增通道' }
		    ]">
		    </Breadcrumb>

		    <div style="padding: 10px 50px;">
		        <div style="margin-top: 20px;">
		        	<CreateBasis 
		        		ref="createBasis" 
		        		@isSaveBasis="isSaveBasis = arguments[0]"
		        		@updateBasisData="handleUpdateBasisData"
		        		:businessTypeList="businessTypeList"
		        		:financialContractList="financialContractList">
		        	</CreateBasis>
		        </div>
		        <div style="margin-top: 20px;">
		        	<Transaction 
		        		ref="debit" 
		        		:accountSide="1"
		        		:workingStatus="basisData.debitPaymentChannelServiceUuid == '' ? 0 : -1"
		        		:showPreview="showPreview"
		        		:model="_debitChannelConfigure"
		        		:chargeRateMode="chargeRateMode" 
		        		:chargeExcutionMode="chargeExcutionMode" 
		        		:channelWorkingStatus="filterChannelWorkingStatus(channelWorkingStatus, basisData.debitPaymentChannelServiceUuid)" 
		        		:outlierChannelName="basisData.outlierChannelName"
		        		:paymentInstitutionOrdinal="basisData.paymentInstitutionNameOrdinal"
		        		@preview="handlerPreview(basisData.paymentInstitutionNameOrdinal, basisData.outlierChannelName, 1)" 
		        		title="收款交易"></Transaction>
		        </div>
                <div style="margin-top: 20px;">
                	<Transaction 
                		ref="credit" 
                		:accountSide="0"
		        		:workingStatus="basisData.creditPaymentChannelServiceUuid == '' ? 0 : -1"
		        		:showPreview="showPreview"
                		:model="_creditChannelConfigure" 
                		:chargeRateMode="chargeRateMode" 
                		:chargeExcutionMode="chargeExcutionMode" 
                		:channelWorkingStatus="filterChannelWorkingStatus(channelWorkingStatus, basisData.creditPaymentChannelServiceUuid)" 
                		:outlierChannelName="basisData.outlierChannelName"
                		:paymentInstitutionOrdinal="basisData.paymentInstitutionNameOrdinal"
                		@preview="handlerPreview(basisData.paymentInstitutionNameOrdinal, basisData.outlierChannelName, 0)" 
                		title="付款交易"></Transaction>
                </div>
                <div style="margin-left: 12%; margin-top: 30px;">
                	<el-button style="margin-left: 120px" type="primary" @click="submit" :disabled="!isSaveBasis" :loading="submiting">提交</el-button>
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
		    CreateBasis : require('./include/CreateBasis'),
		    PreviewModal: require('./include/PreviewModal'),
		},
		data: function() {
			return {
				submiting: false,
				isSaveBasis: false,
				basisData: {
					debitPaymentChannelServiceUuid: '',
					creditPaymentChannelServiceUuid: '',
				},
				createBasis: {},
				creditChannelConfigure: {},
				debitChannelConfigure: {},
				relatedFinancialContractUuid: '',

				previewModal: {
					model: {},
					show: false
				},

				businessTypeList: [],
				channelWorkingStatus: [],
				chargeRateMode: [],
				chargeExcutionMode: [],
				financialContractList: [],
			}
		},
		computed: {
			showPreview: function() {
				var { paymentInstitutionNameOrdinal, outlierChannelName} = this.basisData;
				return paymentInstitutionNameOrdinal != undefined && outlierChannelName != undefined;
			},
			_debitChannelConfigure: function(){
				if (this.basisData.debitPaymentChannelServiceUuid != '') {
					return Object.assign({}, this.debitChannelConfigure, {channelStatus: 'OFF', chargeExcutionMode: 'FORWARD'});
				}
				return this.debitChannelConfigure;
			},
			_creditChannelConfigure: function(){
				if (this.basisData.creditPaymentChannelServiceUuid != '') {
					return Object.assign({}, this.creditChannelConfigure, {channelStatus: 'OFF', chargeExcutionMode: 'FORWARD'});
				}
				return this.creditChannelConfigure;
			}
		},
		activated: function() {
			this.getOptions();
			this.basisData = {
				debitPaymentChannelServiceUuid: '',
				creditPaymentChannelServiceUuid: '',
			};
			this.submiting = false;
			this.creditChannelConfigure = Object.assign({
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
				bankLimitationFileName: '',
				chargeRateMode: 'SINGLERATA'
			});
			this.debitChannelConfigure = Object.assign({
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
				bankLimitationFileName: '',
				chargeRateMode: 'SINGLERATA'
			});
		},
		deactivated: function() {
			var { createBasis, credit, debit } = this.$refs;
			createBasis.resetFields();
			credit.resetFields();
			debit.resetFields();
		},
		methods: {
			handleUpdateBasisData: function(data) {
				this.basisData = Object.assign({}, this.basisData, data)
			},
			filterChannelWorkingStatus: function(list, uuid) {
				if (uuid != '') {
					return list.filter(item => item.key != 'NOTLINK');
				}
				return list;
			},
			getOptions: function() {
				return ajaxPromise({
					url: `/paymentchannel/optionData`
				}).then(data => {
					this.businessTypeList = data.businessTypeList || [];
					this.channelWorkingStatus = data.channelWorkingStatus || [];
					this.chargeRateMode = data.chargeRateMode || [];
					this.chargeExcutionMode = data.chargeExcutionMode || [];
					this.financialContractList = data.queryAppModels || [];
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			handlerPreview: function(paymentInstitutionOrdinal,outlierChannelName,accountSide) {
				this.previewModal.model = {
					paymentInstitutionOrdinal: paymentInstitutionOrdinal,
                    outlierChannelName: outlierChannelName,
                    accountSide: accountSide
				};	
				this.previewModal.show = true;
			},
			submit: function() {
				var { createBasis, credit, debit } = this.$refs;
				Promise.all([credit.validate(), debit.validate()])
					.then(valids => {
						const result = valids.every(valid => valid);
						if (result) {
							const basisConfigure = createBasis.save();
							const creditChannelConfigure = credit.save();
							const debitChannelConfigure = debit.save();

							if (this.submiting) return;
							this.submiting = true;
							ajaxPromise({
							    url: '/paymentchannel/config/create',
								type: 'POST',
								data: {
									paymentChannelCreateModelJson: JSON.stringify({
										...basisConfigure,
										paymentChannelConfigureString:  JSON.stringify({
								    		creditChannelConfigure: creditChannelConfigure,
								    		debitChannelConfigure: debitChannelConfigure
									    })
									})
								}
							}).then(data => {
								var flag = 0;
								MessageBox.once('closed',()=>{
									if(flag == 0){
										location.assign(`${this.ctx}#/financial/channel/configure/list`);
									}
								});
							    MessageBox.open('提交成功是否前去切换通道？', '提示', [{
								    	text: '否',
								    	handler: () => {
								    		MessageBox.close();
								    		flag = 1;
								    		location.assign(`${this.ctx}#/financial/channel/configure/list`);
								    	}
								    },{
								    	text: '是',
								    	type: 'success',
								    	handler: () => {
								    		MessageBox.close();
								    		flag = 2;
								    		location.assign(`${this.ctx}#/financial/channel/switch/detail/${this.basisData.relatedFinancialContractUuid}`);
								    	}
								}]);
							}).catch(message => {
							    MessageBox.open(message);
							    this.submiting = false;
							});
						}
					}).catch(errors => {
						MessageBox.open(errors);
					});
			}
		}
	}
</script>