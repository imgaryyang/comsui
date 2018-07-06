<style lang="sass" scoped>
	.el-select{
		margin-bottom: 50px;
	}
	.block{
		margin-top: 30px;
		.hd{
			color: #333;
		    margin-bottom: 18px;
		    font-weight: 700;
		}
		.bd{
		    font-size: 13px;
			word-break: break-all;
			.col{
			    display: table-cell;
			    padding-left: 15px;
			    min-width: 170px;
			    border-right: 100px solid transparent;
				:first-child {
				    padding-left: 0;
				}
			}
		}
	}
</style>

<template>
	<Modal v-model="show" class="export-modal">
	    <ModalHeader title="历史版本"></ModalHeader>
	    <ModalBody align="left" v-loading="versionFetching">
			<el-select size="small" v-model="version" :placeholder="historyData.versionNameList[0]">
				<el-option
					v-for="item in versionNameList"
					:label="item"
					:value="item">
				</el-option>
			</el-select>
			<div class="block">
				<h5 class="hd">凭证信息</h5>
				<div class="bd">
					<div class="col">
						<p>来源编号 : {{ voucher.voucherNo }}</p>
						<p>凭证金额 : {{ voucher.transcationAmount | formatMoney }}</p>
						<p>备注 : {{ voucher.errorMessge }}</p>
	                </div>
	                <div class="col">
						<p>交易网关 : {{ voucher.transcationGateway }}</p>
						<p>创建时间 : {{ voucher.createTime | formatDate }}</p>
						<p>状态变更时间 : {{ voucher.statusModifyTime | formatDate}}</p>
	                    <p>核销状态 : {{ voucher.voucherLogIssueStatus | formatIssueStatus}}</p>
	                </div>
	                <div class="col">
						<p>清算账号 : {{ voucher.receivableAccountNo }}</p>
						<p>来往机构 : {{ voucher.paymentBank }}</p>
	                    <p>机构账户名 : {{ voucher.paymentName }}</p>
	                    <p>机构账户号 : {{ voucher.paymentAccountNo }}</p>
	                </div>
	            </div>
			</div>
			<div class="block">
	            <h5 class="hd">凭证业务明细</h5>
	            <div class="bd">
	                <el-table 
	                    :data="historyDetail" 
	                    class="td-15-padding th-8-15-padding no-th-border"
	                    stripe
	                    border>
	                    <el-table-column prop="repaymentPlanNo" label="还款计划编号" inline-template>
	                        <a >{{ row.repaymentPlanNo  }}</a>
	                    </el-table-column>
						<el-table-column prop="outerRepaymentPlanNo" label="商户还款计划编号"> 
						</el-table-column>
	                    <el-table-column prop="assetRecycleDate" label="计划还款日期" inline-template>
	                        <div>{{ row.assetRecycleDate | formatDate }}</div>
	                    </el-table-column>

	                    <el-table-column prop="principal" label="还款本金" inline-template>
	                        <div>{{ row.principal | formatMoney }}</div>
	                    </el-table-column>

	                    <el-table-column prop="interest" label="还款利息" inline-template>
	                        <div>{{ row.interest | formatMoney }}</div>
	                    </el-table-column>

	                    <el-table-column prop="serviceCharge" label="贷款服务费" inline-template>
	                        <div>{{ row.serviceCharge | formatMoney }}</div>
	                    </el-table-column>

	                    <el-table-column prop="penaltyFee" label="逾期罚息" inline-template>
	                        <div>{{ row.penaltyFee | formatMoney }}</div>
	                    </el-table-column>

	                    <el-table-column prop="lateFee" label="逾期服务费" inline-template>
	                        <div>{{ row.lateFee | formatMoney }}</div>
	                    </el-table-column>

	                    <el-table-column prop="otherCharge" label="杂项" :render-header="renderOtherCharge" inline-template>
	                            <div>{{ row.sundryAmount | formatMoney }}</div>
	                    </el-table-column>

	                    <el-table-column prop="amount" label="本次还款总额"inline-template>
	                        <div>{{ row.amount | formatMoney }}</div>
	                    </el-table-column>
	                    <el-table-column prop="" label="校验状态">
	                    	<div></div>
	                    </el-table-column>
	                    <el-table-column prop="" label="核销状态">
	                    </el-table-column>
	                </el-table>
	            </div>
	        </div>
	    </ModalBody>
	</Modal>
</template>

<script>
	import format from 'filters/format';
	import MessageBox from 'components/MessageBox';
	import { ajaxPromise } from 'assets/javascripts/util';

	export default{
		props: {
			value: Boolean,
			historyData: {
				type: Object,
				default: () => ({
					historyDetail: {},
					versionNameList: [],
					voucher: {}
				})
			},
			tradeUuid: String
		},
		data: function(){
			return {
				show: this.value,
				version: this.historyData.versionNameList[0],
				historyDetail: [this.historyData.historyDetail],
				versionNameList: this.historyData.versionNameList.slice(),
				voucher: Object.assign({}, this.historyData.voucher),
				versionFetching: false,
			}
		},
		filters:{
			formatDate(value){
				return format.formatDate(value,'yyyy-MM-dd HH:mm:ss')
			},
			formatIssueStatus(value){
				return {'NOT_ISSUED':'未核销','HAS_ISSUED':'已核销'}[value];
			}
		},
		watch: {
		    historyData(cur){
		        this.historyDetail = [cur.historyDetail];
		        this.versionNameList = cur.versionNameList.slice();
		        this.voucher = Object.assign({},cur.voucher);
		    },
			version: function(cur){
				this.versionFetching = true;
				ajaxPromise({
					url: '/voucher/thirdPartyPayApi/getRepaymentDetailHistory',
					data: {
						tradeUuid: this.tradeUuid,
						versionName: cur
					}
				}).then(data => {
					this.historyDetail = [data.historydetail];
					this.voucher = data.voucher;
				}).catch(message => {
	                MessageBox.open(message);
	            }).then(() => {
	            	this.versionFetching = false;
	            })
			},
			value: function(cur){
				this.show = cur;
			},
			show: function(cur) {
		        this.$emit('input', cur);
		    }
		},
		methods: {
			renderOtherCharge: function(h, { column, $index }) {
				var formatMoney = format.formatMoney;
                return h('el-popover', {
                    props: {
                        trigger: "hover",
                        placement: "top"
                    }
                }, [
                    h('div', { slot: 'reference' }, column.label),
                    h('div', {}, '技术维护费: '+formatMoney(this.historyDetail.maintenanceCharge)),
                    h('div', {}, '其他费用: '+formatMoney(this.historyDetail.otherCharge)),
                    h('div', {}, '逾期违约金: '+formatMoney(this.historyDetail.latePenalty)),
                    h('div', {}, '逾期其他费用: '+formatMoney(this.historyDetail.lateOtherCost))
                ]);
            }
		}
	}
</script>