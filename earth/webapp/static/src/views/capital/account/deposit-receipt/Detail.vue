<style>
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb 
                :routes="[
                    { title: '充值详情'}
                ]">
                <el-button v-if="!model.sourceDocumentOfRevokeNo" size="small" type="primary" @click="onClickRevoke">作废</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">充值信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>充值单号 ：{{ model.sourceDocumentNo }}</p>
                                <p>充值金额 ：{{ model.bookingAmount | formatMoney }}</p>
                                <p>银行流水 ：<a href="#" @click.prevent="redirectCashflow">{{ model.institutions }}</a></p>
                                <p>创建时间 ：{{ model.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>状态 ：{{ model.sourceDocumentStatusName }}</p>
                                <p>发生时间 ：{{ model.lastModifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>备注 ：{{ model.summary }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">贷款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>账户编号 ：<a :href="`${ctx}#/capital/account/virtual-acctount/${model.virtualAccountUuid}/detail`">{{ model.virtualAccountNo }}</a></p>
                                <p>账户名称 ：{{ model.virtualAccountName }}</p>
                                <p>客户类型 ：{{ model.customerTypeName }}</p>
                                <p>{{ $utils.locale('financialContract.name')}} ：{{ model.financialContractName }}</p>
                                <p>{{ $utils.locale('financialContract.no')}} ：<a :href="`${ctx}#/financial/contract/${model.financialContractUuid}/detail`">{{ model.financialContractNo }}</a></p>
                                <p>贷款合同编号 ：<a :href="`${ctx}#/data/contracts/detail?id=${model.contractId}`">{{ model.contractNo }}</a></p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">付款方信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>户名 ：{{ model.payerName }}</p>
                                <p>开户行 ：{{ model.bank }}</p>
                                <p>银行账户 ：{{ model.payAcNo }}</p>
                                <p>开户行所在地 ：{{ model.bankLocation }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">充值撤销单</h5>
                    <div class="bd">
                        <el-table 
                            :data="revokeList"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column 
                                inline-template
                                :context="_self"
                                label="撤销单号">
                                <a :href="`${ctx}#/capital/account/recharge-revoke/${model.sourceDocumentRevokeUuid}/detail`">{{ row.sourceDocumentOfRevokeNo }}</a>
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                :context="_self"
                                label="账户编号">
                                <a :href="`${ctx}#/capital/account/virtual-acctount/${model.virtualAccountUuid}/detail`">{{ row.virtualAccountNo }}</a>
                            </el-table-column>
                            <el-table-column prop="virtualAccountName" label="账户名称">
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                :context="_self"
                                label="撤销金额">
                                <div>{{ row.bookingAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="summaryRevoke" label="备注">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <SystemOperateLog 
                        ref="sysLog"
                        :for-object-uuid="$route.params.id"></SystemOperateLog>
                </div>
            </div>
        </div>

        <EditRevokeModal
            :source-document-uuid="revokeModal.sourceDocumentUuid"
            :model="revokeModal.model"
            v-model="revokeModal.show"
            @submit="submitRevoke">
        </EditRevokeModal>
    </div>
</template>

<script>
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import EditRevokeModal from './EditRevokeModal';
    export default {
        components: {
            SystemOperateLog,EditRevokeModal
        },
        data: function() {
            return {
                fetching: false,
                revoking: false,
                show: false,
                model: {},
                remark: '',
                revokeModal: {
                    sourceDocumentUuid:'',
                    model: {
                        sourceDocumentNo: '',
                        sourceDocumentOfRevokeNo: '',
                        summaryRevoke: '',
                        virtualAccountName: '',
                        virtualAccountNo: '',
                        bookingAmount: ''
                    },
                    show: false
                }
            }
        },
        computed: {
            revokeList: function() {
                var res = [];
                var {
                    sourceDocumentOfRevokeNo,
                    summaryRevoke,
                    virtualAccountName,
                    virtualAccountNo,
                    bookingAmount
                } = this.model;

                if (sourceDocumentOfRevokeNo) {
                    res.push({
                        sourceDocumentOfRevokeNo,
                        summaryRevoke,
                        virtualAccountName,
                        virtualAccountNo,
                        bookingAmount
                    });
                }

                return res;
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            fetch: function(sourceDocumentUuid) {
                this.fetching = true;

                ajaxPromise({
                    url: `/capital/customer-account-manage/deposit-receipt-list/detail-data`,
                    data: {
                        sourceDocumentUuid
                    }
                }).then(data => {
                    this.model = Object.assign({}, this.model, data.accountDepositModel);
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
            redirectCashflow: function() {
                var { accountNo, institutions } = this.model;
                var search = encodeURI(`hostAccountNo=${accountNo}&cashFlowNo=${institutions}`);
                var href = `${this.ctx}#/capital/special-account/cash-flow-audit?${search}`;
                // var href = `${this.ctx_deprecated}/capital/account-manager/cash-flow-audit/show#${search}`;
                location.assign(href);
            },
            submitRevoke: function() {
                this.fetch(this.$route.params.id);
                this.$refs.sysLog.fetch();
            },
            onClickRevoke: function() {
                this.revokeModal.show = true;
                this.revokeModal.model = Object.assign({}, this.model);
                this.revokeModal.sourceDocumentUuid = this.$route.params.id;
            }
        }
    }
</script>