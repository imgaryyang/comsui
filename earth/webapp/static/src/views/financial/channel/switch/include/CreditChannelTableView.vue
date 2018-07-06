<style type="sass" scoped>
	.margin-bottom {
		margin-bottom: 15px;
	}
	.field-span {
		padding-right: 20px;
	}
	.item {
		height: 40px;
	}
</style>
<template>
	<div class="margin-bottom" v-if="paymentChannelMode === 0 || paymentChannelMode === 1">
		<span>
			{{ paymentChannelModeTitle }}
		</span>
		<template v-if="paymentChannelModeData.length > 0">
			<div class="block">
				<div class="bd field-span">
					<el-table
						:data="paymentChannelModeData"
						class="td-15-padding th-8-15-padding"
						stripe
						border>
						<el-table-column label="策略模式" inline-template>
							<div>
								{{row.strategyMsg}}&nbsp;&nbsp;&nbsp;
								<span v-if="row.strategyMsg.indexOf('限额优先模式')!==-1" class="el-tag" style="background-color: rgb(32, 160, 255); cursor: pointer;" @click.prevent="$emit('priorityPreview','QUOTAPRIORITY')">通道限额预览</span>
								<span v-if="row.strategyMsg == '发卡行优先模式'" class="el-tag" style="background-color: rgb(32, 160, 255); cursor: pointer;" @click.prevent="$emit('priorityPreview','ISSUINGBANKFIRST')">各银行通道顺序预览</span>

							</div>
						</el-table-column>
						<el-table-column label="通道名称" inline-template>
							<div>
								<div v-for="item in row.channelData" :class="[row.channelData.length > 1 ? 'item' : '']">
									{{item.paymentChannelName}}
								</div>
							</div>
						</el-table-column>
						<el-table-column label="通道费用" inline-template>
							<div>
								<div v-for="item in row.channelData" :class="[row.channelData.length > 1 ? 'item' : '']">
									{{item.fee}}
								</div>
							</div>
						</el-table-column>
						<el-table-column prop="time" label="时间"></el-table-column>
						<el-table-column inline-template label="状态">
							<div>
								<div v-for="item in row.channelData" :class="[row.channelData.length > 1 ? 'item' : '']">
									<span :class="{'color-danger': row.channelStatus == 'OFF' }">{{ item.channelStatusMsg }}</span>
								</div>
							</div>
						</el-table-column>
					</el-table>
				</div>
			</div>
		</template>
	</div>
</template>
<script>

	export default {
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
				if(this.paymentChannelMode == '0'){
					return '无时间切换模式'
				}else if(this.paymentChannelMode == '1'){
					return '定时切换模式'
				}else{
					return ' ';
				}
			}
		},
		data() {
			return {
				paymentChannelModeTitle: '',
			}
		}
	}
</script>