<template>
	    <div class="content">
	        <div class="scroller" v-loading="fetching">
	            <Breadcrumb :routes="[{ title: '转账详情'}]">
<!-- 	                <el-button size="small" type="primary">审核</el-button>
	                <el-button size="small" type="primary">重新执行</el-button>
	                <el-button size="small" type="primary">重新回调</el-button>
	                <el-button size="small" type="primary">作废</el-button> -->
	            </Breadcrumb>

	            <div class="col-layout-detail">
	                <div class="top">
	                    <div class="block">
	                        <h5 class="hd">订单信息</h5>
	                        <div class="bd">
	                            <div class="col">
	                                <p>商户转账单号 ：{{ detailModel.orderNo }}</p>
	                                <p>五维转账单号 ：{{ detailModel.orderUuid }}</p>
	                                <p>商户提现单号 ：{{ detailModel.a }}</p>
	                                <p>五维提现单号 ：{{ detailModel.a }}</p>
	                                <p>产品代码 ：{{ detailModel.financialContractNo }}</p>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="block">
	                        <h5 class="hd">付款方信息</h5>
	                        <div class="bd">
	                            <div class="col">
	                                <p>付款方账号 ：{{ detailModel.accountNo }}</p>
	                                <p>付款方户名 ：{{ detailModel.a }}</p>
	                                <p>付款方开户行 ：{{ detailModel.a }}</p>
	                                <p>开户行所在地 ：{{ detailModel.a }}</p>
	                                <p>付款方证件号 ：{{ detailModel.a }}</p>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="block">
	                        <h5 class="hd">收款方信息</h5>
	                        <div class="bd">
	                            <div class="col">
	                                <p>收款方账号 ：{{ detailModel.cpBankCardNo }}</p>
	                                <p>收款方户名 ：{{ detailModel.cpBankAccountHolder }}</p>
	                                <p>收款方开户行 ：{{ detailModel.bankName }}</p>
	                                <p>开户行所在地 ：{{ detailModel.cpBankProvinceName }}&nbsp;{{ detailModel.cpBankCityName }}</p>
	                                <p>收款方证件号 ：{{ detailModel.cpIdNumber }}</p>
	                            </div>
	                        </div>
	                    </div>
	                    <div class="block">
	                        <h5 class="hd">执行信息</h5>
	                        <div class="bd">
	                            <div class="col">
	                                <p>转账金额 ：{{ detailModel.plannedTotalAmount }}</p>
	                                <p>审核人 ：{{ detailModel.auditorName }}</p>
	                                <p>审核时间 ：{{ detailModel.auditTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
	                                <p>备注 ：{{ detailModel.remark }}</p>
	                                <p>交易附言 ：{{ detailModel.executionRemark }}</p>
	                            </div>
	                            <div class="col">
	                                <p>状态 ：{{ detailModel.executionStatusName }}</p>
	                                <p>创建时间 ：{{ detailModel.createTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
	                                <p>状态变更时间 ：{{ detailModel.lastModifyTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
	                                <p>计划回调次数 ：{{ detailModel.planNotifyNumber }}</p>
	                                <p>实际回调次数 ：{{ detailModel.actualNotifyNumber }}</p>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	            </div>

	            <div class="row-layout-detail">
	                <div class="block">
	                    <h5 class="hd">
	                        线上代付单列表
	                    </h5>
	                    <div class="bd">
	                        <el-table 
	                            :data="remittancePlanExecLog"
	                            class="td-15-padding th-8-15-padding no-th-border"
	                            stripe
	                            border>
	                            <el-table-column prop="execReqNo" label="代付单号" inline-template>
	                                <a :href="`${ctx}#/capital/remittance-cash-flow/plan-execlog/${ row.execReqNo }/detail`">{{row.execReqNo}}</a>
	                            </el-table-column>
	                            <el-table-column prop="pgAccountName" label="付款方账户名">
	                            </el-table-column>
	                            <el-table-column prop="cpBankAccountHolder" label="收款方账户名">
	                            </el-table-column>
	                            <el-table-column prop="plannedAmount" label="交易金额" inline-template>
	                            	<div>{{ row.plannedAmount | formatMoney }}</div>
	                            </el-table-column>
	                            <el-table-column prop="paymentGatewayMessage" label="交易网关">
	                            </el-table-column>
	                            <el-table-column prop="lastModifiedTime" label="状态变更时间" inline-template>
	                                <div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
	                            </el-table-column>
	                            <el-table-column prop="executionStatusMessage" label="状态">
	                            </el-table-column>
	                            <el-table-column prop="executionRemark" label="备注">
	                            </el-table-column>
	                        </el-table>
	                    </div>
	                </div>
	                <div class="block">
	                    <h5 class="hd">
	                        代付撤销单
	                    </h5>
	                    <div class="bd">
	                        <el-table 
	                            :data="remittanceRefundBills"
	                            class="td-15-padding th-8-15-padding no-th-border"
	                            stripe
	                            border>
	                            <el-table-column prop="channelCashFlowNo" label="通道流水号">
	                            </el-table-column>
	                            <el-table-column prop="paymentChannelName" label="通道名称">
	                            </el-table-column>
	                            <el-table-column prop="createTime" label="发生时间" inline-template>
	                                <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
	                            </el-table-column>
	                            <el-table-column prop="hostAccountNo" label="退回账户">
	                            </el-table-column>
	                            <el-table-column prop="transactionType" label="交易类型">
	                            </el-table-column>
	                            <el-table-column prop="amount" label="金额" inline-template>
	                                <div>{{ row.amount | formatMoney }}</div>
	                            </el-table-column>
	                        </el-table>
	                    </div>
	                </div>
	                <div class="block">
	                    <SystemOperateLog :for-object-uuid="$route.params.orderUuid"></SystemOperateLog>
	                </div>
	            </div>
	        </div>
	    </div>
</template>

<script>
	import SystemOperateLog from 'views/include/SystemOperateLog';
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';

	export default{
		 components: {
            SystemOperateLog,
        },
		data(){
			return{
				fetching: false,
				detailModel: {},
				remittancePlanExeclog: [],
				remittanceRefundBills: []
			}
		},
		activated(){
			this.fetchDetail(this.$route.params.orderUuid)
		},
		methods:{
			fetchDetail(orderUuid){
				this.fetching = true;
				ajaxPromise({
					url: `/transfer/${orderUuid}/detail`
				}).then(data => {
					// 转帐单
					this.detailModel = data.detailModel || {}
					// 线上待付单
					this.remittancePlanExecLog = data.remittancePlanExecLog ? [data.remittancePlanExecLog] : []
					// 代付撤销单列表
					this.remittanceRefundBills = data.remittanceRefundBills || []
					//暂时不存在的属性都用a代替
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                	this.fetching = false;
                })
			}
		}
	}
</script>