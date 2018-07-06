<template>
	<div class="row">
	    <div class="item turnover">
	        <div class="hd">
	            <i class="icon icon-circle"></i>
	            <span>交易额趋势&nbsp;&nbsp;&nbsp;</span>
	            <select v-model="time" class="form-control filter-turnover">
	                <option value="7d">最近7天</option>
	                <option value="6m">最近6个月</option>
	            </select>
	        </div>
	        <div class="bd">
	            <div ref="draw" class="draw" style="width: 100%; height: 300px; margin: auto; max-width: 1160px;"></div>
	        </div>
	    </div>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import Highcharts from 'highcharts';

	export default {
		props:{
			turnoverData: Object,
		},
		data: function() {
			return {
				time: '7d'
			}
		},
		watch: {
			time: function() {
				this.$parent.getTurnoverData();
			},
			turnoverData: function(data) {
				if (this.turnoverChart.series == 0) {
				    this.turnoverChart.addSeries({
				        name: '付款交易额',
				        data: data.creditAmountList
				    });
				    this.turnoverChart.addSeries({
				        name: '收款交易额',
				        data: data.debitAmountList
				    });
				} else {
				    this.turnoverChart.series[0].setData(data.creditAmountList);
				    this.turnoverChart.series[1].setData(data.debitAmountList);
				}
			}
		},
		mounted: function() {
			this.turnoverChart = new Highcharts.Chart({
				credits: {
				    enabled: false // 禁用版权信息
				},
				exporting: {
					enabled: false
				},
				legend: {
				    align: 'right',
				    verticalAlign: 'top',
				    y: -10
				},
				tooltip: {
				    enabled: false
				},

				chart: {
				    renderTo: this.$refs.draw,
				    marginTop: 50
				},
				colors: ['#69c2e2', '#ed4407'],
				title: {
				    text: null
				},
				plotOptions: {
				    line: {
				        marker: {
				            symbol: 'circle'
				        }
				    }
				},
				xAxis: {
				    lineColor: '#dedede',
				    tickLength: 0,
				    labels: {
				        enabled: false
				    }
				},
				yAxis: {
				    lineWidth: 1,
				    tickInterval: 10,
				    lineColor: '#dedede',
				    gridLineColor: '#cdcdcd',
				    gridLineDashStyle: 'Dash',
				    title: {
				        text: null // 隐藏坐标轴标题
				    }
				}
			});
		},
		methods: {
			getTurnoverData: function() {
				return ajaxPromise({
	                url: `/paymentchannel/efficentanalysis/tradingVolumeTrend`,
	                data: {
	                    time: this.time
	                }
	            });
			}
		}
	}
</script>