<template>
	<div class="row">
	    <div class="item channel">
	        <span class="legend">
	            <span @click="handleChangeLegendType('成功', $event)"><i style="background: #90ed7d" class="rect"></i>成功</span>
	            <span @click="handleChangeLegendType('失败', $event)"><i style="background: #f04f76" class="rect"></i>失败</span>
	            <span @click="handleChangeLegendType('异常', $event)"><i style="background: #f7a35c" class="rect"></i>异常</span>
	            <span @click="handleChangeLegendType('处理中', $event)"><i style="background: #7cb5ec" class="rect"></i>处理中</span>
	            <span @click="handleChangeLegendType('等待处理', $event)"><i style="background: #434348" class="rect"></i>待处理</span>
	        </span>
	        <div class="hd">
	            <i class="icon icon-circle"></i>
	            <span>24小时通道交易统计&nbsp;&nbsp;&nbsp;</span>
	            <select v-model="type" class="form-control filter-channel">
	                <option value="0">收付款</option>
	                <option value="1">仅收款</option>
	                <option value="2">仅付款</option>
	            </select>
	        </div>
	        <div ref="container" class="bd clearfix">
	        </div>
	    </div>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import Highcharts from 'highcharts';

	export default {
		props:{
			channelData: Array,
		},
		data: function() {
			return {
				type: "0"
			}
		},
		watch: {
			type: function() {
				this.$parent.getChannelData();
			},
			channelData: function(data) {
				this.clearCharts();

				for (var i = 0; i < data.length; i++) {
					var chart = this.createChart();
					this.setChartData(chart, data[i]);
					this.channelCharts.push(chart);
				}
			}
		},
		methods: {
			clearCharts: function() {
				(this.channelCharts || []).forEach(function(chart) {
					$(chart.container).parent().remove();
					chart.destroy();
				});
				this.channelCharts = [];
			},
			setChartData: function(chart, item) {
				var toParse = function(obj) {
				    var arr = [];

				    if ($.isEmptyObject(obj)) {
				        obj['SUCCESS'] = 0;
				        obj['FAIL'] = 0;
				        obj['TIME_OUT'] = 0;
				        obj['DOING'] = 0;
				        obj['CREATE'] = 1;
				    }

				    arr.push(['成功', obj['SUCCESS']]);
				    arr.push(['失败', obj['FAIL']]);
				    arr.push(['异常', obj['TIME_OUT']]);
				    arr.push(['处理中', obj['DOING']]);
				    arr.push(['等待处理', obj['CREATE']]);
				    return arr;
				};

				var arrData = toParse(item.data);
				
				if (chart.series.length == 0) {
				    chart.addSeries({data: arrData});
				} else {
				    chart.series[0].setData(arrData);
				}

				chart.setTitle({text: item.paymentChannelName});
			},
			createChart: function() {
				var dom = $('<div class="draw" />');
				var $container = $(this.$refs.container);
				var opt = {
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
					    type: 'pie',
					    renderTo: dom.get(0)
					},
					colors: ['#90ed7d', '#f04f76', '#f7a35c', '#7cb5ec', '#434348'],
					title: {
					    text: '',
					    verticalAlign: 'bottom',
					    y: -20,
					    style: {
					        fontSize: '12px'
					    }
					},
					plotOptions: {
					    pie: {
					        allowPointSelect: true,
					        cursor: 'pointer',
					        size: 150,
					        dataLabels: {
					            enabled: false
					        }
					    }
					},
					series: []
				};

				$container.append(dom);

				return new Highcharts.Chart(opt);
			},
			handleChangeLegendType: function(legendType, e) {
				var $currentTarget = $(e.currentTarget);
				var current = $currentTarget.hasClass('invisible') ? true : false;

				$(this.channelCharts || []).each(function(j, f) {
				    $(this.series[0].data).each(function(k, z) {
				        if (z.name == legendType) {
				            z.setVisible(current);
				        }
				    });
				});

				current ? $currentTarget.removeClass('invisible') : $currentTarget.addClass('invisible');
			},
			getChannelData: function() {
			    return ajaxPromise({
			    	url: `/paymentchannel/efficentanalysis/statistics`,
			    	data: {
			    		type: this.type
			    	}
			    });
			}
		}
	}
</script>