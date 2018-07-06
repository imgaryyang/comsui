<style lang="sass">
	.customer-query-graph {
		border-style: solid;
		border-width: 1px;
		border-color: #CACACA;
		width: 45%;
		float: left;
		margin: 33px 20px 0px;

		.tab-menu.nav-menu {
			border-bottom: 1px solid #8FB9F8;
			overflow: initial;

			.tab-menu-item {
				float: right;
				position: relative;

				&.active {
					border-style: solid;
					border-color: #8FB9F8;
					border-width: 2px 1px 0px;
					border-radius: 0px;

					&:before {
						content: '';
						display: block;
						position: absolute;
						left: 0;
						bottom: -1px;
						right: 0;
						background-color: #fff;
						height: 2px;
					}

					a {
						color: #333;
						background-color: #FFF;
						border-radius: 0px;
					}
				}
			}
		}
	}
</style>

<template>
	<div class="customer-query-graph row-layout-detail">
		<TabMenu v-model="tabSelected" class="clearfix">
			<span style="color:#8FB9F8;font-size:18px;font-weight:bold">{{title}}</span>
			<TabMenuItem id="1">剩余本息</TabMenuItem>
			<TabMenuItem id="0">本金</TabMenuItem>
		</TabMenu>
		<!-- <TabContent v-model="tabSelected"> -->
			<!-- <TabContentItem id="0"> -->
				<HighchartsBox
					v-loading="waitingResult"
					:title="title"
					:options="options"
					:styles="styles">
				</HighchartsBox>
				<TableWithComboQueryBox
					:dataSource="tableData"
					:waitingResult="waitingResult">
				</TableWithComboQueryBox>
			<!-- </TabContentItem> -->
			<!-- <TabContentItem id="1"> -->
				<!-- bbbbb -->
			<!-- </TabContentItem> -->
		<!-- </TabContent> -->
	</div>
</template>

<script>
	import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
	import format from 'filters/format.js';
	export default {
		components: {
			TableWithComboQueryBox: require('./TableWithComboQueryBox'),
			TabMenu,TabMenuItem, TabContent, TabContentItem,
			HighchartsBox: require('./HighchartsComponent'),
		},
		props: {
			value: {
				type: [Number, String],
				default: '0'
			},
			dataSource: Array,
            waitingResult: {
                type: Boolean,
                default: false
            },
            title: {
            	type: String,
            	default: ''
            }
		},
		computed: {
			tabSelected: {
				get: function(){
					return this.value
				},
				set: function(v){
					this.$emit('input', v)
				}
			},
			tableData: function() {
				return {
					list: this.dataSource,
					size: this.dataSource.length
				}
			}
		},
		watch: {
			dataSource: function(d){
				d = d.map(item => {
					item.x = new Date(item.statisticsDate).getTime()
					item.y = parseFloat(item.quotient)
					return item
				})
				// debugger
				if (d.length > 0){
					var beginDayMillseconds = d[0].x
					var endDayMillseconds = d[d.length - 1].x
					var tickInterval = this.caculate(beginDayMillseconds, endDayMillseconds)

					// const timeRange = Math.ceil(timerange/dayMillseconds/7)*dayMillseconds
					// var timeInterval = Math.max(timeRange, 24* 3600* 1000)
					this.options.xAxis.tickInterval = tickInterval
					this.options.series[0].data = d

					this.tableData.list = d
					this.tableData.size = d.length
				} else {
					this.options.series[0].data = []
				}
			}
		},
		data: function() {
			return {
				options: {
					chart: {
						backgroundColor: '#f3f3f3',
					},
					credits: {
					    enabled: false // 禁用版权信息
					},
					exporting: {
						enabled: false
					},
					legend: {
						enabled: false
					},
					title: {
						text: null,
						align: 'left',
						style: {
							fontSize: '12px'
						}
					},
					xAxis: {
						// startOnTick: true,
						type: "linear",
						title: {
				            text: null
				        },
				        labels: {
				        	formatter: function(){
				        		return format.formatDate(this.value)
				        	}
				        },
					},
					yAxis: {
						title: {
							text: null
						},
						// tickInterval: 0.2,
						tickAmount: 8,
						labels: {
							formatter: function(){
								return `${this.value}%`
							}
						}
					},
					tooltip: {
						formatter: function(){
							return `<b>${format.formatDate(this.x)}</b><br>
								逾期率:${this.y}%<br>
								${this.point.denominatorDesc}:${this.point.denominator}<br>
								${this.point.numeratorDesc}:${this.point.numerator}<br>`
						},
					},
					series: [{
						data: [],
						name: '逾期率',
						showInLegend: false
					}]
				},
			}
		},
		methods: {
			caculate(firstMillseconds, lastMillseconds) {
	            var limitTagLength = 7 // 近似最大tag数
	            var dayMillseconds = 1000 * 60 * 60 * 24
	            var deltaMillseconds = lastMillseconds - firstMillseconds
	            var deltaDays = deltaMillseconds / dayMillseconds
	            deltaDays = Math.ceil(deltaDays / limitTagLength)
	            deltaMillseconds = deltaDays * dayMillseconds
	            return deltaMillseconds
	        }
		}
	}
</script>