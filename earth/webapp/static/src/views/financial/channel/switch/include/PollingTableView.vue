<style lang="sass"></style>
<template>
	<div>
		<span>
			{{isOpenedTitle}}
			<HelpPopover popoverWidth="350" content="设置轮循后若返回不成功会根据失败原因进行两种不同的处理：1.若为余额不足，则等到下一个时间点对下一个通道再发起扣款； 2.若为其他原因，则瞬时对下一个通道发起扣款，以此往复直到成功或轮循次数用完。单一通道同通道最多轮循五次，多通道则按通道顺序轮循（例如：通道数量为2，则按1-2-1-2-1顺序轮循，第一个通道最多轮循3次，第二个通道最多2次，以此类推）；若选无轮循则实时发起扣款，并只会执行一次。"></HelpPopover>
			<div class="table">
				<table>
					<thead>
						<tr>
							<th v-for="n in 5">{{ n-1 | pollingsTableLabelFilter}}</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td v-for="n in 5">{{ pollings[n-1] == undefined ? '--': pollings[n-1] }}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</span>
	</div>
</template>

<script>
	export default {
		components: {
			HelpPopover: require('views/include/HelpPopover'),
		},
		props: {
			pollings: {
				type: Array,
				default: () => []
			}
		},
		data: function() {
			return {

			}
		},
		computed: {
			isOpened: function() {
				return this.pollings.length > 0
			},
			isOpenedTitle: function() {
				return this.isOpened ? '有轮循' : '无轮循'
			}
		},
		filters: {
			pollingsTableLabelFilter: function(index) {
				return ['第一次轮循时间', '第二次轮循时间', '第三次轮循时间', '第四次轮循时间', '第五次轮循时间'][index];
			}
		}
	}
</script>