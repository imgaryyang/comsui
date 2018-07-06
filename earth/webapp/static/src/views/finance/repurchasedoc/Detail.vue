<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '回购详情'}]">
                <el-button 
                    v-if="canNullify" 
                    type="primary" 
                    size="small" 
                    @click="handleNullify">作废</el-button>
                <el-button 
                    v-else-if="ifElementGranted('activate-repurchasedoc') && repurchaseDoc.repurchaseStatus == 3" 
                    :loading="activating"
                    type="primary" 
                    size="small" 
                    @click="handleActivate">激活</el-button>
                <el-button
                    v-if="ifElementGranted('mark-break-contract') && repurchaseDoc.isDefault && repurchaseDoc.repurchaseStatus == 0"
                    type="primary"
                    size="small"
                    @click="handleBreakContract">违约</el-button>
                </el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
            	<div class="top">
            		<div class="block">
            			<h5 class="hd">回购信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>回购单号：{{repurchaseDoc.repurchaseDocUuid }}</p>
                                <p>批次号：{{repurchaseDoc.batchNo }}</p>
                                <p>回购受理方式: {{ repurchaseDoc.repurchaseApproach }}</p>
                                <p>回购规则: {{ repurchaseDoc.repurchaseRuleDetail }} </p>
                                <p>回购金额：{{ repurchaseDoc.amount | formatMoney }}</p>
                                <p>回购本金金额：
                                    <el-popover
                                    v-if="repurchaseDoc.repurchasePrincipalExpression"
                                    trigger="hover"
                                    placement="top">
                                        {{ repurchaseDoc.repurchasePrincipalExpression }}
                                        <span slot="reference">
                                            {{ repurchaseDoc.repurchasePrincipal | formatMoney }}
                                        </span>
                                    </el-popover>
                                    <template v-else>{{ repurchaseDoc.repurchasePrincipal | formatMoney }}</template>
                                    {{ repurchaseDoc.repurchasePrincipalAlgorithm ? `(${repurchaseDoc.repurchasePrincipalAlgorithm})` : `` }}</p>
                                <p>回购利息金额：
                                    <el-popover
                                    v-if="repurchaseDoc.repurchaseInterestExpression"
                                    trigger="hover"
                                    placement="top">
                                        {{ repurchaseDoc.repurchaseInterestExpression }}
                                        <span slot="reference">
                                            {{ repurchaseDoc.repurchaseInterest | formatMoney }}
                                        </span>
                                    </el-popover>
                                    <template v-else>{{ repurchaseDoc.repurchaseInterest | formatMoney }}</template>
                                    {{ repurchaseDoc.repurchaseInterestAlgorithm ? `(${repurchaseDoc.repurchaseInterestAlgorithm})` : `` }}</p>
                                <p>回购罚息金额：
                                    <el-popover
                                    v-if="repurchaseDoc.repurchasePenaltyExpression"
                                    trigger="hover"
                                    placement="top">
                                        {{ repurchaseDoc.repurchasePenaltyExpression }}
                                        <span slot="reference">
                                            {{ repurchaseDoc.repurchasePenalty | formatMoney }}
                                        </span>
                                    </el-popover>
                                    <template v-else>{{ repurchaseDoc.repurchasePenalty | formatMoney }}</template>
                                    {{ repurchaseDoc.repurchasePenaltyAlgorithm ? `(${repurchaseDoc.repurchasePenaltyAlgorithm})` : `` }}</p>
                                <p>回购其他费用金额：
                                    <el-popover
                                    v-if="repurchaseDoc.repurchaseOtherChargesExpression"
                                    trigger="hover"
                                    placement="top">
                                        {{ repurchaseDoc.repurchaseOtherChargesExpression }}
                                        <span slot="reference">
                                            {{ repurchaseDoc.repurchaseOtherCharges | formatMoney }}
                                        </span>
                                    </el-popover>
                                    <template v-else>{{ repurchaseDoc.repurchaseOtherCharges | formatMoney }}</template>
                                    {{ repurchaseDoc.repurchaseOtherChargesAlgorithm ? `(${repurchaseDoc.repurchaseOtherChargesAlgorithm})` : `` }}</p>
                                <p>回购起始日: {{ repurchaseDoc.repoStartDate | formatDate }} </p>
                                <p>回购截止日：{{ repurchaseDoc.repoEndDate | formatDate }}</p>
                                <p>创建时间：{{ repurchaseDoc.creatTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>状态变更时间: {{ repurchaseDoc.modifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
            				</div>
            			</div>
            		</div>
            		<div class="block">
            			<h5 class="hd">贷款合同信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>贷款合同编号：<a :href="!repurchaseDoc.contractId ? 'javascript: void 0;' : `${ctx}#/data/contracts/detail?id=${repurchaseDoc.contractId }`">{{ repurchaseDoc.contractNo }}</a></p>
                                <p>商户编号: {{ repurchaseDoc.appName }}</p>
                                <p>客户名称: {{ repurchaseDoc.customerName }}</p>
                                <p>还款期数：{{ repurchaseDoc.assetSetPeriods }}</p>
                                <p>生效日期：{{ repurchaseDoc.contractBeginTime | formatDate }}</p>
                                <p>回购天数：{{ repurchaseDoc.repoDays }}</p>
                                <p>回购期数：{{ repurchaseDoc.repurchasePeriods }}</p>
                                <p v-if="repurchaseDoc.repurchaseStatus != null">回购状态：{{ repurchaseDoc.repurchaseStatusName }}</p>
                            </div>
                        </div>
            		</div>

            	</div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">回购凭证</h5>
                    <div class="bd">
                        <el-table
                            :data="dataSource.list"
                            class="td-15-padding th-8-15-padding no-th-border"
                            v-loading="dataSource.fetching"
                            stripe
                            border>
                            <el-table-column label="凭证编号" prop="voucherNo" inline-template>
                                <a :href="`${ctx}#/capital/voucher/business/${ row.id}/detail`">{{ row.voucherNo }}</a>
                            </el-table-column>
                            <el-table-column label="专户账号" prop="receivableAccountNo"></el-table-column>
                            <el-table-column label="账户姓名" prop="paymentName"></el-table-column>
                            <el-table-column label="机构账户号" prop="paymentAccountNo"></el-table-column>
                            <el-table-column label="凭证金额" prop="amount" inline-template>
                                <div>{{ row.amount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column label="凭证类型" prop="voucherType"></el-table-column>
                            <el-table-column label="凭证内容" prop=""></el-table-column>
                            <el-table-column label="凭证来源" prop="voucherSource"></el-table-column>
                            <el-table-column label="凭证状态" prop="voucherStatus" inline-template>
                                <span :class="{
                                    'color-warning': row.voucherStatus == '未核销',
                                    'color-danger': row.voucherStatus == '作废'
                                    }">{{ row.voucherStatus }}</span>
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
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="repurchaseDoc.repurchaseDocUuid"></SystemOperateLog>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import format from 'filters/format';
    import Pagination from 'mixins/Pagination';

    export default{
        mixins: [Pagination],
        components:{
            SystemOperateLog: require('views/include/SystemOperateLog'),
        },
        data: function() {
            return {
                action: '/repurchasedoc/voucher',
                fetching: false,
                activating: false,
                canNullify: false,
                repurchaseDoc: {},
                voucherList: [],

                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
            }
        },
        computed: {
            conditions: function() {
                return Object.assign({rduid: this.$route.params.rduid}, this.pageConds);
            }
        },
        activated: function() {
            this.fetchDetail();
            this.isCanNullify();
        },
        methods: {
            isCanNullify: function() {
                ajaxPromise({
                    url: `/repurchasedoc/can-nullify`,
                    data: {
                        rduid: this.$route.params.rduid
                    }
                }).then(data => {
                    this.canNullify = data.canNullify;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            fetchDetail: function() {
                this.fetching = true;

                ajaxPromise({
                    url: `/repurchasedoc/detail`,
                    data: {
                        rduid: this.$route.params.rduid
                    }
                }).then(data => {
                    this.repurchaseDoc = data.repurchaseDoc || {};
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
            fetch: function() {
                if(!this.$route.params.rduid) return;

                this.getData({
                    url: this.action,
                    data: this.conditions
                });
            },
            onSuccess: function(data) {
                var d = this.parse(data);
                this.dataSource.list = d.voucherList || [];
                this.dataSource.size = d.size;
                this.dataSource.error = '';
            },
            handleNullify: function() {
                var { repurchaseDoc } = this;
                var htmlContent = `<span>回购单：<span style="color:red">${repurchaseDoc.repurchaseDocUuid}</span></span><br/>
                        <span>回购截止日：${ format.formatDate(repurchaseDoc.repoEndDate)}，回购金额：${ format.formatMoney(repurchaseDoc.amount)}</span><br/>
                        <span>你确认将此回购单置为作废吗</span>`;

                MessageBox.open(htmlContent, '提示', [{
                    text: '取消',
                    handler: () => MessageBox.close(),
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: '/repurchasedoc/nullify',
                            data: {
                                rduid: this.$route.params.rduid
                            }
                        }).then(data => {
                            MessageBox.close();
                            this.freshDetail();
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }]);
            },
            handleActivate: function() {
                this.activating = true;
                ajaxPromise({
                    url: `/repurchasedoc/activate`,
                    data: {
                        rduid: this.$route.params.rduid
                    }
                }).then(data => {
                    this.freshDetail();
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.activating = false;
                });
            },
            handleBreakContract: function() {
                var { repurchaseDoc } = this;
                var htmlContent = `<span>回购单：<span style="color:red">${repurchaseDoc.repurchaseDocUuid}</span></span><br/>
                        <span>回购截止日：${ format.formatDate(repurchaseDoc.repoEndDate) }，回购金额：${ format.formatMoney(repurchaseDoc.amount)}</span><br/>
                        <span>你确认将此回购单置为违约吗</span>`;

                MessageBox.open(htmlContent, '提示', [{
                    text: '取消',
                    handler: () => MessageBox.close(),
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: '/repurchasedoc/default',
                            type: 'post',
                            data: {
                                rduid: this.$route.params.rduid
                            }
                        }).then(data => {
                            MessageBox.close();
                            this.freshDetail();
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }]);
            },
            freshDetail: function() {
                this.fetch();
                this.fetchDetail();
                this.isCanNullify();
                this.$refs.sysLog.fetch();
            }
        }
    }
</script>
