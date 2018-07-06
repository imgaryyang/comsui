<style lang="sass">
	.list-statistics {
		margin-right: 10px;
		line-height: 28px;
		display: inline-block;
	}
</style>

<template>
	<div class="list-statistics">
		<el-popover
			@show="fetch"
			popper-class="text-align-center"
			ref="popover"
			:width="210"
			placement="top"
			trigger="click">
			<template v-if="fillIn">
				<div class="text-align-left">
		  			<slot></slot>
				</div>
			</template>
			<template v-else>
				<div v-if="fetching">统计中...</div>
				<div v-else-if="error">{{ error }}</div>
				<template v-else>
					<div v-if="parameters[identifier].length == 0">还未选择{{$utils.locale('financialContract')}}</div>
					<div v-else-if="parameters[identifier].length > 1">项目数量>1超出统计范围</div>
					<div class="text-align-left" v-else="parameters[identifier].length == 1">
			  			<slot :data="data"></slot>
					</div>
				</template>	
			</template>
		</el-popover>
		<a 
			v-popover:popover 
			href="javascript: void 0;">
			{{ indicator}}
		</a>
	</div>
</template>

<script>
	import { ajaxPromise, purify } from 'assets/javascripts/util';
	import Vue from 'vue';

	export default {
		props: {
			action: {
				type: String,
			},
			identifier: {
				default: 'financialContractUuids'
			},
			parameters: {
				default: () => ({})
			},
			indicator: {
				default: '统计金额'
			},
			fillIn: {//不通过异步请求
				default: false
			},
		},
		data: function() {
			return {
				data: {},
				error: '',
				fetching: false
			}
		},
		watch: {
			parameters: {
				deep: true,
				handler: function() {
					this.error = '';
				}
			}
		},
		methods: {
			fetch: function() {
				if (this.fillIn || this.parameters[this.identifier].length != 1) return;

				this.fetching = true;
				ajaxPromise({
					url: this.action,
					data: purify(this.parameters)
				}).then(data => {
					this.error = '';
					this.data = data;
					setTimeout(() => {
						this.$refs.popover.updatePopper();
					}, 0);
				}).catch(message => {
					this.error = message;
				}).then(() => {
					this.fetching = false;
				});
			}
		}
	}
</script>
