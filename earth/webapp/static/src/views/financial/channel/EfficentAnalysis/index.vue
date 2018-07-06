<template>
	<div class="content content-passage" id="financialChannelEdit">
	    <div class="scroller">
	        <Breadcrumb :routes="[{ title: '通道管理' }, {title: '效率分析'}]">
	        </Breadcrumb>
	        <div class="wrapper">
	        	<div class="sketch">
	        		<div class="bd">
	        			<div class="cols">
	        				<div class="col">
	        				    <p><span class="color-warning">{{ debitTransferApplicationCount }}</span></p>
	        				    <p>累计收款成功笔数</p>
	        				</div>
	        				<div class="col">
	        				    <p>
	        				        {{ debitTotalTradingVolume | formatMoney }}
	        				    </p>
	        				    <p>累计收款交易额</p>
	        				</div>
	        				<div class="col">
	        				    <p>
	        				        {{ debitTotalFee | formatMoney }}
	        				    </p>
	        				    <p>累计收款通道费用</p>
	        				</div>
	        				<div class="col">
	        				    <p><span class="color-warning">{{ creditTransferApplicationCount }}</span></p>
	        				    <p>累计付款成功笔数</p>
	        				</div>
	        				<div class="col">
	        				    <p>
	        				        {{ creditTotalTradingVolume | formatMoney }}
	        				    </p>
	        				    <p>累计付款交易额</p>
	        				</div>
	        				<div class="col">
	        				    <p>
	        				        {{ creditTotalFee | formatMoney }}
	        				    </p>
	        				    <p>累计付款通道费用</p>
	        				</div>
	        			</div>
	        		</div>
	        	</div>
	        	<div class="graph">
	        	    <Trend ref="turnover" :turnoverData="turnoverData"></Trend>
	        	    <Statistics ref="channel" :channelData="channelData"></Statistics>
	        	</div>
	        </div>
	    </div>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import Highcharts from 'highcharts';

	export default {
		components: {
			Trend: require('./Trend'),
			Statistics: require('./Statistics')
		},
		data: function() {
			return {
				creditTotalFee: 0,
				creditTotalTradingVolume: 0,
				creditTransferApplicationCount: 0,
				debitTotalFee: 0,
				debitTotalTradingVolume: 0,
				debitTransferApplicationCount: 0,

				turnoverData: {},
				channelData: [],
			}
		},
		activated: function(){
			this.getDetailData(this.$route.params.paymentChannelUuid);
			this.getTurnoverData();
			this.getChannelData();
		},
		methods: {
			getDetailData: function(paymentChannelUuid) {
				ajaxPromise({
					url: `/paymentchannel/efficentanalysis/data`
				}).then(data => {
					this.detail = data;
				}).catch(message => {
					MessageBox.open(message);
				});
			},
			getTurnoverData: function() {
				this.$refs.turnover.getTurnoverData().then(data => this.turnoverData = data);
			},
			getChannelData: function() {
				this.$refs.channel.getChannelData().then(data => this.channelData = data.list);
			}
		}
	}
</script>