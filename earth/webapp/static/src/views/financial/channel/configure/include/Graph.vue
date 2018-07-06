<template>
	<div class="graph">
		<div class="row span2">
    		<div class="item">
    			<div class="hd">
    				<i class="icon icon-circle"></i>
    				<span>交易额趋势&nbsp;&nbsp;&nbsp;</span>
    				<select v-model="turnoverTime" class="form-control filter-turnover">
    					<option value="7d">最近7天</option>
    					<option value="6m">最近6个月</option>
    				</select>
        		</div>
    			<div class="bd">
    				<div id="drawContainer1" style="width: 100%; height: 300px;"></div>
    			</div>
    		</div>
    		<div class="item">
    			<div class="hd">
    				<i class="icon icon-circle"></i>
    				<span>通道费用趋势&nbsp;&nbsp;&nbsp;</span>
    				<select v-model="channelTime" class="form-control filter-channel">
    					<option value="7d">最近7天</option>
    					<option value="6m">最近6个月</option>
    				</select>
        		</div>
    			<div class="bd">
    				<div id="drawContainer2" style="height: 300px;"></div>
    			</div>
    		</div>
		</div>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import Highcharts from 'highcharts';

	export default {
		props:{
			uuid: String
		},
		data: function() {
			return {
				turnoverTime: "7d",
				channelTime: "7d",
				channelData: {},
				turnoverData: {}

			}
		},
		watch: {
			uuid: function(value) {
				if(value){
					this.getTurnoverData();
					this.getChannelData();
				}
			},
			turnoverTime: function(value) {
				this.getTurnoverData();
			},
			channelTime: function(value) {
				this.getChannelData();
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
			},
			channelData: function(data) {
				if (this.channelChart.series == 0) {
				    this.channelChart.addSeries({
				        name: '付款通道费',
				        data: data[0]
				    });
				    this.channelChart.addSeries({
				        name: '付款交易额',
				        data: data[1]
				    });
				} else {
				    this.channelChart.series[0].setData(data[0]);
				    this.channelChart.series[1].setData(data[1]);
				}
			}
		},
		mounted: function() {
			var defaults = {
				colors: ['#69c2e2', '#ed4407'],
				credits: {
				    enabled: false // 禁用版权信息
				},
				exporting: {
					enabled: false
				},
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
				},
				legend: {
				    align: 'right',
				    verticalAlign: 'top',
				    y: -10
				},
				tooltip: {
				    enabled: false
				}
			};

			this.turnoverChart = new Highcharts.Chart(Object.assign({
				chart: {
					marginTop: 50,
				    renderTo: $('#drawContainer1').get(0)
				},
				series: []
			}, defaults));

			this.channelChart = new Highcharts.Chart(Object.assign({
			    chart: {
			    	marginTop: 50,
			        renderTo: $('#drawContainer2').get(0)
			    },
			    series: []
			},defaults));

			/*$(document).on('toggle.aside', function(e) {
			    this.turnoverChart.reflow();
			    this.channelChart.reflow();
			}.bind(this));*/

			this.getChannelData();
			this.getTurnoverData();
		},
		methods: {
			getChannelData: function() {
			    var type = this.channelTime;

			    setTimeout(() => {
			        this.channelData = {};
			    }, 500);
			},
			getTurnoverData: function() {
				ajaxPromise({
					url: `/paymentchannel/config/transactionDetail/searchTradingVolume`,
					data:{
						time: this.turnoverTime,
						paymentChannelUuid: this.uuid
					}
				}).then(data => {
					this.turnoverData = data;
				});

			}
		}
	}
</script>