<style lang="sass">
	@import '~assets/stylesheets/base';
</style>
<template>
	<Modal v-model="visible">
		<ModalHeader title="查看余额"></ModalHeader>
		<ModalBody align="left">
			<div> 
	    		<div v-if="!content.checked" style="text-align: center;margin-top: 40px;">{{ content.notice }}</div>
				<el-form v-else
				    ref="form"
				    :model="balanceQueryResult"
				    label-width="220px"
				    class="sdf-form sdf-modal-form">
					    <el-form-item style="padding: 0;" label="账户号：">
					    {{ balanceQueryResult.financeAccountNo }}
		                </el-form-item>
		                <el-form-item style="padding: 0;" label="账户名称：">
		                {{ balanceQueryResult.financeAccountName}}
		                </el-form-item>
		                <el-form-item style="padding: 0;" label="开户行：">
		                {{ balanceQueryResult.financeAccountBankName}}
		                </el-form-item>
		                <el-form-item style="padding: 0;" :label="$utils.locale('financialContract.name').concat('：')">
		                {{ balanceQueryResult.financeContractName }}
		                </el-form-item>
		                <el-form-item style="padding: 0;" :label="$utils.locale('financialContract.no').concat('：')">
		                {{ balanceQueryResult.financeContractNo }}
		                </el-form-item>
		                <el-form-item style="padding: 0;" label="查询时间：">
		                {{ balanceQueryResult.queryTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
		                </el-form-item>
		                <el-form-item style="padding: 0;" label="账户余额：">
		                {{ balanceQueryResult.accountBalance | formatMoney }}
		                </el-form-item>
				</el-form>
			</div>
		</ModalBody>
		<ModalFooter>
			<el-button type="success" @click="visible=false">确定</el-button>
		</ModalFooter>
	</Modal>
</template>

<script>
	import Pagination, { extract } from 'mixins/Pagination';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		props: {
			value: Boolean,
			financeContractId: [String, Number],
			accountId: [String, Number]
		},
		data: function() {
			return {
				visible: this.value,
				content: {
					notice: '余额查询中...',
					checked: false,
				},
				balanceQueryResult: {}
			};
		},
		watch: {
			value: function(current) {
				this.visible = current;
				if(current) {
					this.getData();
				}
			},
			visible: function(current) {
	        	this.$emit('input', current);
	        },
		},
		methods: {
			getData: function() {
				this.content.checked = false;
				this.content.notice = '余额查询中...';
				
				ajaxPromise({
					url: `/capital/balance`,
					data: {
						financeContractId: this.financeContractId,
						accountId: this.accountId
					}
				}).then(data => {
					this.content.checked = true;
					this.balanceQueryResult = data.balanceQueryResult;
				}).catch(message => {
					this.content.checked = false;
					this.content.notice = message.toString();
				})
			},
		}
	}
</script>