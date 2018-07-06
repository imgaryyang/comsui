<style lang="sass" scoped>
	.modal-body.align-center {
		text-align: left;
	}
	.panel {
		margin: 15px;
		display: inline-block;
		width: 140px;
		padding: 10px;
		border: 1px solid #ddd;

		.paymentChannelName {
			height: 50px;
			color: #38e;
			overflow: hidden;
			margin-bottom: 3px;
		}

		.transactionLimitPer {
			height: 42px;
		}

		&:hover {
			border: 1px solid #38e;
		}

	}
</style>

<template>
	<Modal v-model="show">
		<ModalHeader title="通道限额预览">
		</ModalHeader>
		<ModalBody>
			<div><span class="el-icon-information"></span> <em>默认优先级：列表从左到右、从上到下为序排优先级，左上优先级最高</em></div>
			<div class="row-layout-detail">
				<div class="block">
					<div 
             			class="panel" 
             			v-for="(item, key, index) in currentModal" >
             			<div class="paymentChannelName">{{ item.paymentChannelName }}</div>
             			<div class="transactionLimitPer">
             				<div v-if="accountSide == 0">单笔限额 ： {{item.transactionLimitPerMin}}~{{item.transactionLimitPerMax}}万/笔</div>
             				<div v-else-if="accountSide == 1">
             					单笔限额 ： {{item.transactionLimitPer}}万/笔</div>
							<div v-else></div>
             			</div>
             			<div>
             				<span class="color-dim">{{ '('+item.fee+')' }}</span>
            				<span class="pull-right">{{ item.channelStatusMsg }}</span>
             			</div>
					</div>
				</div>
			</div>
		</ModalBody>
	</Modal>
</template>

<script>
	import modalMixin from './modal-mixin';
	import MessageBox from 'components/MessageBox';
	export default {
		mixins: [modalMixin],
		props: {
			model: {
				default : null
			},
			accountSide: '',
		},
		data(){
			return {
				show: this.value,
			}
		},
		computed: {
			currentModal: function(){
				return Object.assign({}, this.model)
			}
		}
	}
</script>