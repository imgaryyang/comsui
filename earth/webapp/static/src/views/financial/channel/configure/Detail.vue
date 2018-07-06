<template>
	<div class="content content-passage" id="financialChannelEdit">
	    <div class="scroller" v-loading="fetching">
	        <Breadcrumb :routes="[{ title: '通道管理' }, {title: '通道详情'}]">
	        </Breadcrumb>
	        <div class="wrapper">
	        	<div class="sketch">
	        		<div class="hd">
	        			<span class="title">{{detail.paymentChannelName}}</span>
	        			<span>（24小时内交易成功率<span class="color-warning">{{detail.tradingSuccessRateIn24Hours}}</span>）</span>
	        			<span class="pull-right">通道开通时间：{{detail.createTime | formatDate}}</span>
	        		</div>
	        		<div class="bd">
	        			<div class="cols">
	        				<div class="col">
	        					<p>
	        						{{ detail.totalDebitAmount | formatMoney }}
	        					</p>
	        					<p>累计收款交易额</p>
	        				</div>
	        				<div class="col">
	        					<p>
	        						{{ 0 | formatMoney}}
	        					</p>
	        					<p>累计收款通道费用</p>
	        				</div>
	        				<div class="col">
	        					<p>
	        						{{ detail.totalCreditAmount | formatMoney }}
	        					</p>
	        					<p>累计付款交易额</p>
	        				</div>
	        				<div class="col">
	        					<p>
	        						{{ 0 | formatMoney }}
	        					</p>
	        					<p>累计付款通道费用</p>
	        				</div>
	        				<div class="col">
	        					<p>
	        						{{ 0 | formatMoney }}
	        					</p>
	        					<p>累计未结费用</p>
	        				</div>
	        			</div>
	        		</div>
	        	</div>
	        	<Graph :uuid="$route.params.paymentChannelUuid"></Graph>
	        	<div class="row-layout-detail" style="padding:0">
		        	<div class="block">
		        	    <h5 class="hd">交易记录</h5>
		    	        <div class="query-area" style="margin: 10px 0;border: 1px solid #d9d9d9;">
		    	        	<el-form class="sdf-form sdf-query-form" :inline="true">
		    	        		<el-form-item>
		    	        			<DateTimePicker
										v-model="queryConds.startDateString"
										:end-date="queryConds.endDateString"
										placeholder="交易时间起始"
										:format-to-minimum="true"
										size="small">
									</DateTimePicker>
		    	        		</el-form-item>
		    	        		<el-form-item>
		    	        			<DateTimePicker
										v-model="queryConds.endDateString"
										:start-date="queryConds.startDateString"
										placeholder="交易时间终止"
										:format-to-minimum="true"
										size="small">
									</DateTimePicker>
		    	        		</el-form-item>
		    	        		<el-form-item>
		    	        			<el-select 
		    	        			    v-model="queryConds.transactionType"
		    	        			    size="small" 
		    	        			    placeholder="选择交易类型">
		    	        			    <el-option
		    	        			    	v-for="item in transactionTypes"
		    	        					:label="item.label"
		    	        					:value="item.value">
		    	        			    </el-option>
		    	        			</el-select>
		    	        		</el-form-item>
		    	        		<el-form-item>
		    	        			<el-input size="small" placeholder="系统流水号" v-model="queryConds.transferApplicationNo"></el-input>
		    	        		</el-form-item>
		    	        		<el-form-item>
		    	        			<el-button ref="lookup" size="small" type="primary">查询</el-button>
		    	        		</el-form-item>
		    	        	</el-form>
		    	        </div>
		        	    <div class="bd">
	        	        	<el-table
	        	        		class="td-15-padding th-8-15-padding no-th-border"
	        	        		stripe
	        	        		v-loading="dataSource.fetching"
	        	        		:data="dataSource.list"
	        	        		border>
	        	        		<el-table-column prop="creatTime" label="交易时间">
	        	        		</el-table-column>
	        	        		<el-table-column prop="amount" label="交易金额">
	        	        		</el-table-column>
	        	        		<el-table-column prop="fee" label="通道费用">
	        	        		</el-table-column>
	        	        		<el-table-column prop="transactionType" label="交易类型">
	        	        		</el-table-column>
	        	        		<el-table-column prop="transferApplicationNo" label="系统流水号">
	        	        		</el-table-column>
	        	        	</el-table>
		        	    </div>
	        	    	<div class="ft clearfix">
	        	            <PageControl 
	        	                v-model="pageConds.pageIndex"
	        	                :size="dataSource.size"
	        	                :per-page-record-number="pageConds.perPageRecordNumber">
	        	            </PageControl>
	        	    	</div>
		        	</div>
	        	</div>
	        </div>
	    </div>
	</div>
</template>

<script>
	import Pagination from 'mixins/Pagination';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import Highcharts from 'highcharts';

	export default {
		mixins: [Pagination],
		components: { 
			Graph: require('./include/Graph')
		},
		data: function() {
			var transactionTypes=[
				{
					label: '付款',
					value: '1'
				},{
					label: '收款',
					value: '0'
				}];

			return {
				action: '/paymentchannel/config/transactionDetail/search',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					startDateString: '',
					endDateString: '',
					transactionType: '',
					transferApplicationNo: '',
					paymentChannelUuid: this.$route.params.paymentChannelUuid
				},

				transactionTypes,
				detail: {},
				fetching: false
			};
		},
		watch: {
			queryConds: {
                deep: true,
                handler: function() {
                    this.pageConds.pageIndex = 1;
                }
            }
		},
		computed: {
			conditions: function() {
                return Object.assign({}, this.queryConds, this.pageConds);
            }
		},
		activated: function(){
			this.getDetailData(this.$route.params.paymentChannelUuid);
		},
		methods: {
			getDetailData: function(paymentChannelUuid) {
				this.fetching = true;
				ajaxPromise({
					url: `/paymentchannel/config/transactiondetail/${paymentChannelUuid}`
				}).then(data => {
					this.detail = data;
				}).catch(message => {
					MessageBox.open(message);
				}).then(() => {
					this.fetching = false;
				})
			}
		}
	}
</script>