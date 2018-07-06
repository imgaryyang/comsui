<template>
	<div ref="echartsBox" style="min-height: 600px;"></div>
</template>

<script>
	import * as echarts from 'echarts/lib/echarts';
	import 'echarts/lib/chart/pie';
	import 'echarts/lib/component/tooltip';
	import 'echarts/lib/component/title';
	import 'echarts/lib/component/legend';
	import {throttle} from 'lodash';

	export default{
		component:{
			echarts
		},
		props: {
		  	options: {
		    	type: Object,
		    	required: true
		  	},
		  	styles: {
		    	type: Object,
		    	default: function(){
		      		return{
		        		width: 800,
		        		height: 400
		      		}
		    	}
		  	},
		  	loading: {
		  		type: Boolean,
		  		default: false
		  	}
		},
		data() {
		  	return {
		    	chart: null,
		    	resizefn: null
		  	}
		},
		watch: {
		  	options: {
		    	deep: true,
		    	handler(){
		      		this.initChart()
		    	}
		  	},
		  	loading: function(current) {
		  		if(current && this.chart) {
		  			this.chart.showLoading();
		  		} else {
		  			this.chart.hideLoading();
		  		}
		  	}
		},
		mounted() {
		  	this.initChart();
		  	window.addEventListener('resize',this.resizefn)
		},
		destroyed() {
		 	window.removeEventListener('resize',this.resizefn)
		},
		methods: {
		  	initChart() {
			    this.$el.style.width =
			    	typeof this.styles.width == 'string'
			    	? this.styles.width
			    	: (this.styles.width || 800) + 'px';
			    this.$el.style.height =
			    	typeof this.styles.height == 'string'
			    	? this.styles.height
			    	: (this.styles.height || 400) + 'px';
			    this.chart = echarts.init(this.$refs.echartsBox);
			    this.chart.setOption(this.options, true)
			    this.resizefn = throttle(this.chart.resize,30)
		  	},
		}
	}
</script>
