<style type="sass">
	.margin-bottom {
		margin-bottom: 15px;
	}
	.field-span {
		padding-right: 20px;
	}
</style>
<template>
	<div class="margin-bottom" v-if="paymentChannelMode">
		<span>
			{{ paymentChannelModeTitle }}
			<HelpPopover v-show="paymentChannelMode == 'ISSUINGBANKFIRST' || paymentChannelMode == 'QUOTAPRIORITYNOTSIGN'" content="默认优先级：列表从上往下为序排优先级，上方优先级最高">
			</HelpPopover>
		</span>
		<template>
			<div class="block">
				<div class="bd field-span">
					<el-table
						:data="paymentChannelModeData"
						class="td-15-padding th-8-15-padding"
						stripe
						border>
						<el-table-column prop="paymentChannelName" label="通道名称">
						</el-table-column>
						<el-table-column prop="fee" label="通道费用">
						</el-table-column>
						<el-table-column inline-template label="状态">
							<span :class="{'color-danger': row.channelStatus == 'OFF' }">{{ row.channelStatusMsg }}</span>
						</el-table-column>
					</el-table>
				</div>
				<a href="#" @click.prevent="$emit('priorityPreview',paymentChannelMode)" v-if="paymentChannelMode == 'ISSUINGBANKFIRST' || paymentChannelMode == 'QUOTAPRIORITY'">{{paymentChannelModePreviewMSG}}</a>
			</div>
		</template>
	</div>
</template>
<script>
	import HelpPopover from 'views/include/HelpPopover';

	export default {
		components: {
			HelpPopover
		},
		props: {
			paymentChannelMode: {
				default : ''
			},
			paymentChannelModeData: {
				default : () => ({})
			}
		},
		computed: {
			paymentChannelModeTitle: function (){
				if(this.paymentChannelMode == 'SINGLECHANNELMODE') {
					return '单一通道模式';
				}else if(this.paymentChannelMode == 'ISSUINGBANKFIRST') {
					return '发卡行优先模式';
				}else if( this.paymentChannelMode == 'QUOTAPRIORITY') {
					return '限额优先-签约模式';
				}else if( this.paymentChannelMode == 'QUOTAPRIORITYNOTSIGN') {
					return '限额优先-非签约模式';
				}else{
					return ' ';
				}
			},
			paymentChannelModePreviewMSG: function() {
				if(this.paymentChannelMode == 'ISSUINGBANKFIRST') {
					return '各银行通道顺序预览';
				}else if( this.paymentChannelMode == 'QUOTAPRIORITY') {
					return  '通道限额预览';
				}else {
					return ' ';
				}
			}
		},
		data() {
			return {
				paymentChannelModeTitle: '',
			}
		},
		watch: {
			
		},
		methods: {
			
		}
	}
</script>