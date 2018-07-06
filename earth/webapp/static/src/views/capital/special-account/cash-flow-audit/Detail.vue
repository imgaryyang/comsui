<style lang="sass">
	
</style>

<template>
	<div class="content">
		<div class="scroller" v-loading="fetching">
			<Breadcrumb :routes="[{title: '银行对账流水详情'}]">
				<el-button
					size="small"
					type="primary"
					v-if="detail.canBeDeposited && doubtAmount > 0"
					@click="handleDeposit">
					充值
				</el-button>
			</Breadcrumb>
			<div class="col-layout-detail">
				<div class="top">
					<div class="block">
						<h5 class="hd">流水信息</h5>
						<div class="bd">
							<div class="col">
								<p>流水号：{{ detail.bankSequenceNo }} </p>
								<p>交易金额：{{ detail.transactionAmount | formatMoney }} </p>
								<p>入账时间：{{ detail.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }} </p>
								<p>对账状态：{{ detail.auditStatusMsg }} </p>
								<p>借贷标志：{{ detail.accountSideMsg }} </p>
								<p>摘要内容：{{ detail.remark }} </p>
							</div>
						</div>
					</div>
					<div class="block">
						<h5 class="hd">信托合同信息</h5>
						<div class="bd">
							<div class="col">
								<p>信托合同代码：{{ detail.contractNo }} </p>
								<p>信托合同名称：{{ detail.contractName }} </p>
								<p>信托专户：{{ capitalAccountStr }} </p>
								<p>开户行：{{ detail.capitalBankName }} </p>
							</div>
						</div>
					</div>
					<div class="block">
						<h5 class="hd">对方账户信息</h5>
						<div class="bd">
							<div class="col">
								<p>银行账号：{{ detail.accountNo }} </p>
								<p>账户姓名：{{ detail.accountName }} </p>
								<p>开户行：{{ detail.bankName }} </p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row-layout-detail">
				<div class="block">
					<h5 class="hd">
						存疑金额：
						<span class="color-danger">{{ doubtAmount | formatMoney }}</span>
					</h5>
					<div class="bd">
						<el-table
			                :data="dataSource.list"
			                class="td-15-padding th-8-15-padding no-th-border"
			                v-loading="dataSource.fetching"
			                stripe
			                border>
			                <el-table-column label="充值单号" prop="showData.depositNo" inline-template>
			                	<a :href="`${ctx}#/capital/account/deposit-receipt/${row.sourceDocumentUuid}/detail`">{{ row.showData.depositNo }}</a>
			                </el-table-column>
	                        <el-table-column label="账户编号" prop="showData.virtualAccountNo"></el-table-column>
	                        <el-table-column label="客户姓名" prop="showData.customerName"></el-table-column>
	                        <el-table-column label="客户类型" prop="showData.customerTypeMsg"></el-table-column>
	                        <el-table-column label="贷款合同编号" prop="showData.contractNo"></el-table-column>
	                        <el-table-column :label="$utils.locale('financialContract.name')" prop="showData.financialContractName"></el-table-column>
	                        <el-table-column label="账户余额" prop="balance" inline-template>
	                            <div>{{ row.balance | formatMoney }}</div>
	                        </el-table-column>
	                        <el-table-column label="充值金额" inline-template>
                               <div>{{ row.depositAmount | formatMoney }}</div>
	                        </el-table-column>
	                        <el-table-column label="备注" prop="remark">
	                        </el-table-column>
	                        <el-table-column label="状态" prop="sourceDocumentStatusEnum">
	                        </el-table-column>
			            </el-table>
					</div>
					<div class="ft text-align-center">
			            <PageControl 
			                v-model="pageConds.pageIndex"
			                :size="dataSource.size"
			                :per-page-record-number="pageConds.perPageRecordNumber">
			            </PageControl>
			        </div>
				</div>
				<div class="block">
					<h5 class="hd">相关凭证</h5>
					<div class="bd">
						<PagingTable 
							:data="voucher == undefined ? [] : [voucher]">
	                        <el-table-column label="凭证单号" inline-template>
	                        	<a :href="row.voucherSource == '商户付款凭证' ? `${ctx}#/capital/voucher/business/${row.id}/detail` : row.voucherSource == '主动付款凭证' ? `${ctx}#/capital/voucher/active/${row.voucherNo}/detail` : `${ctx}#/capital/voucher/remittance/${row.sourceDocumentUuid}/detail`">{{ row.voucherNo }}</a>
	                        </el-table-column>
	                        <el-table-column label="凭证来源" prop="voucherSource"></el-table-column>
	                        <el-table-column label="凭证类型" prop="voucherType"></el-table-column>
	                        <el-table-column label="往来机构名称" prop="paymentBank"></el-table-column>
	                        <el-table-column label="账户名" prop="paymentName"></el-table-column>
	                        <el-table-column label="机构账户号" prop="paymentAccountNo"></el-table-column>
	                        <el-table-column label="凭证金额" prop="amount" inline-template>
	                            <div>{{ row.amount | formatMoney }}</div>
	                        </el-table-column>
	                        <el-table-column label="创建时间" inline-template>
                               <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
	                        </el-table-column>
	                        <el-table-column label="状态变更时间" inline-template>
                               <div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
	                        </el-table-column>
	                        <el-table-column label="凭证状态" prop="voucherStatusMessage">
	                        </el-table-column>
                    	</PagingTable>
					</div>
				</div>
				<div class="block">
					<SystemOperateLog
						ref="sysLog"
						:for-object-uuid="$route.params.uuid">
					</SystemOperateLog>
				</div>
			</div>
		</div>

		<CreateBillModal 
            :financialContractModels="financialContractModels"
            :customerTypeList="customerTypeList"
            :cashFlowUuid="$route.params.uuid"
            :doubtAmount="doubtAmount"
            v-model="createBillModal.visible"
            @submitFailed="onFailedSubmit" 
            @submitSuccess="onSuccessSubmit">
        </CreateBillModal>
	</div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
	import Pagination from 'mixins/Pagination';
	import Decimal from 'decimal.js'

	export default {
		mixins: [Pagination],
		components: {
			SystemOperateLog: require('views/include/SystemOperateLog'),
			PagingTable: require('views/include/PagingTable'),
			CreateBillModal: require('./include/CreateBillModal'),
		},
		data: function() {
			return {
				fetching: false,
				action: '/capital/account-manager/cash-flow-audit/show-deposit-result',

				pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },

				financialContractModels: [],
                accountSideList: [],
                auditStatusList: [],
                customerTypeList: [],

                detail: {},
                voucher: {},

                createBillModal: {
                    visible: false,
                    model: {}
                },
			}
		},
		computed: {
			doubtAmount: function() {
                var transactionAmount = new Decimal(+this.detail.transactionAmount);
                var issuedAmount = new Decimal(+this.detail.issuedAmount);
                return transactionAmount.sub(issuedAmount).toNumber();
            },
            capitalAccountStr: function() {
            	var str = '';
            	var { capitalAccount } = this.detail;
            	if (!$.isEmptyObject(capitalAccount)) {
            		var formatStr = [
            			capitalAccount.bankName,
            			capitalAccount.accountNo,
            			'(' + capitalAccount.accountName + ')',
            		].join(' ');

            		str = formatStr;
            	}
            	return str;
            },
            conditions: function() {
            	return Object.assign({}, {cashFlowUuid: this.$route.params.uuid});
            }
		},
		activated: function() {
			this.fetchOptions();
			this.fetchDetail(this.$route.params.uuid);
		},
		methods: {
			fetch: function() {
				if (!this.$route.params.uuid || this.dataSource.fetching) return;

	            this.getData({
	                url: this.action,
	                data: this.conditions,
	                type: 'post'
	            });
			},
			onSuccess: function(data) {
				var d = this.parse(data);
	            this.dataSource.list = d.depositResults;
	            this.dataSource.size = d.depositResults.length;
	            this.dataSource.error = '';
			},
			fetchOptions: function() {
				ajaxPromise({
                    url: `/capital/account-manager/cash-flow-audit/options`
                }).then(data => {
                    this.financialContractModels = data.queryAppModels || [];
                    this.accountSideList = data.accountSideList || [];
                    this.auditStatusList = data.auditStatusList || [];
                    this.customerTypeList = data.customerTypeList || [];
                });	
			},
			fetchDetail: function(uuid) {
				if (this.fetching) return;
				this.fetching = true;

				ajaxPromise({
					url: `/capital/account-manager/cash-flow-audit/detail/${uuid}`,
				}).then(data => {
					this.detail = data.detail;
					this.voucher = data.voucher;
				}).catch(message => {
					MessageBox.open(message);
				}).then(() => {
					this.fetching = false;
				});
			},
			handleDeposit: function() {
				var { createBillModal } = this;
				createBillModal.visible = true;
				createBillModal.model = {};
			},
			onSuccessSubmit: function() {
				this.createBillModal.visible = false;
				this.fetch();
				this.fetchDetail(this.$route.params.uuid);
				this.$refs.sysLog.fetch();
			},
			onFailedSubmit: function(requestError) {
				if (requestError.code == -6005) {
                    var htm = [
                        '<div style="margin: 30px;">',
                        '流水银行卡',
                        '<span class="color-danger"> ',
                        this.detail.accountName,
                        ' ',
                        this.detail.bankName,
                        ' ',
                        this.detail.accountNo,
                        ' </span>',
                        '与客户账户中银行卡信息不符，请修正。',
                        '</div>'
                    ].join('');

                    MessageBox.open(htm, null, [{
                        text: '确定',
                        type: 'success',
                        handler: () => {
                            MessageBox.close();
                            location.assign(`${this.ctx}#/capital/account/virtual-acctount/${requestError.response.data.virtualAccountUuid}/detail`);
                        }
                    }]);
                } else {
                    MessageBox.open(requestError);
                }
			}
		}
	}
</script>