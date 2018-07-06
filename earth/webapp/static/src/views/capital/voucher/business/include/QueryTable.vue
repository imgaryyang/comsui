<template>
	<div class="block">
		<h5 class="hd"> {{ title }}&nbsp;
			<el-button size="small" type="primary" v-if="showExport" @click="handleExport">导出</el-button>
		</h5>
		<div class="bd">
			<el-table
                :data="dataSource.list"
                class="td-15-padding th-8-15-padding no-th-border"
                v-loading="dataSource.fetching"
                stripe
                border>
                <slot></slot>
            </el-table>
		</div>
		<div class="ft text-align-right">
			<div class="list-statistics">
	            <el-popover
	            	@show="fetchTotalAmount"
	            	popper-class="text-align-center"
	            	ref="popover"
	            	:width="210"
	            	placement="top"
	            	trigger="click">
	            	<template scoped="totalAmountModel">
	            		<div v-if="totalAmountModel.fetching">统计中...</div>
	            		<div v-else-if="totalAmountModel.error">{{ error }}</div>
						<template v-else-if="secondType == 'enum.voucher-type.repurchase'">
							<div>回购本金金额<span class="pull-right">{{ totalAmountModel.totalAmount.principal | formatMoney }}</span></div>
							<div>回购利息金额<span class="pull-right">{{ totalAmountModel.totalAmount.interest | formatMoney }}</span></div>
							<div>回购罚息金额<span class="pull-right">{{ totalAmountModel.totalAmount.penaltyFee | formatMoney }}</span></div>
							<div>回购其他费用金额<span class="pull-right">{{ totalAmountModel.totalAmount.otherCharge | formatMoney }}</span></div>
						</template>
	            		<template v-else>
							<div>还款本金<span class="pull-right">{{ totalAmountModel.totalAmount.principal | formatMoney }}</span></div>
							<div>还款利息<span class="pull-right">{{ totalAmountModel.totalAmount.interest | formatMoney }}</span></div>
							<div>贷款服务费<span class="pull-right">{{ totalAmountModel.totalAmount.serviceCharge | formatMoney }}</span></div>
							<div>技术维护费<span class="pull-right">{{ totalAmountModel.totalAmount.maintenanceCharge | formatMoney }}</span></div>
							<div>其他费用<span class="pull-right">{{ totalAmountModel.totalAmount.otherCharge | formatMoney }}</span></div>
							<div>逾期罚息<span class="pull-right">{{ totalAmountModel.totalAmount.penaltyFee | formatMoney }}</span></div>
							<div>逾期违约金<span class="pull-right">{{ totalAmountModel.totalAmount.latePenalty | formatMoney }}</span></div>
							<div>逾期服务费<span class="pull-right">{{ totalAmountModel.totalAmount.lateFee | formatMoney }}</span></div>
							<div>逾期其他费用<span class="pull-right">{{ totalAmountModel.totalAmount.lateOtherCost | formatMoney }}</span></div>
							<div>逾期费用合计<span class="pull-right">{{ totalAmountModel.totalAmount.lateTotalAmount | formatMoney }}</span></div>
	            		</template>
	            		<span slot="reference" style="color:#436ba7;cursor:pointer;">统计明细</span>
	            	</template>
	            </el-popover>
	        </div>
            <PageControl 
                v-model="pageConds.pageIndex"
                :size="dataSource.size"
                :per-page-record-number="pageConds.perPageRecordNumber">
            </PageControl>
        </div>
	</div>
</template>

<script>
	import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import Pagination from 'mixins/Pagination';

	export default {
	    mixins: [Pagination],
	    components: {
            ListStatistics: require('views/include/ListStatistics')
	    },
		props: {
			action: {
				type: String,
				default: '',
				required: true
			},
			title: {
				type: String,
				default: ''
			},
			voucherId: [Number, String],
			active: {
				required: true
			},
      		showExport: {
			  	type: Boolean,
				default: false
			},
			voucherUuid: String,
			secondType: String
		},
		watch: {
			active: function(current) {
				if (current) {
					this.fetch();
				}
			},
		},
		computed: {
			conditions: function() {
    			return Object.assign({voucherId: this.voucherId}, this.pageConds);
    		},
		},
		data: function() {
			return {
				pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
                totalAmountModel: {
                	fetching: false,
                	error: '',
                	totalAmount: {}
                },
                secondTypeMap:{
                	'回购': 'enum.voucher-type.repurchase'
                }
			}
		},
		methods: {
			fetch: function() {
				if (this.active && this.voucherId) {
					this.getData({
					    url: this.action,
					    data: this.conditions
					});
				}

			},
			onSuccess: function(data) {
                this.dataSource.list = data.list.map(item => {
                    item.statistics = {
                        planChargesDetail: {},
                    };
                    return item;
                });
                this.dataSource.size = data.size;
            },
            fetchFeeDetail: function(assetSetUuid) {
            	var { list } = this.dataSource;
                var index = list.findIndex(item => item.assetSetUuid === assetSetUuid);

                if (index === -1 || list[index].statistics.success) return;

                ajaxPromise({
                    url: `/assets/${assetSetUuid}/feeDetail`,
                }).catch(error => {
                    this.$set(list[index], 'statistics', { error });
                }).then(data => {
                    this.$set(list[index], 'statistics', Object.assign({
                        success: true,
                    }, data.planChargesDetail));
                });
            },
		    handleExport: function () {
		      downloadFile(`${this.api}/voucher/business/detail/query/export`, {voucherId: this.voucherId});
		    },
		    fetchTotalAmount: function(){
		    	var {voucherUuid, secondType} = this
		    	this.totalAmountModel.fetching = true
		    	this.totalAmountModel.totalAmount = {}
		    	ajaxPromise({
		    		url: '/voucher/statistics/voucherAmount',
		    		data: {
		    			voucherUuid,
		    			secondType
		    		},
		    	}).then(data => {
		    		this.totalAmountModel.totalAmount = data.voucherAmountStatisticsModel
		    	}).catch(message =>{
		    		MessageBox.open(message)
		    	}).then(()=>{
		    		this.totalAmountModel.fetching = false
		    	})
		    }
		}
	}
</script>