<style>
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb 
                :routes="[
                    { title: '撤销详情'}
                ]">
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block" style="width: 25%">
                        <h5 class="hd">撤销信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>撤销单号 ：{{ model.sourceDocumentRevokeNo }}</p>
                                <p>撤销金额 ：{{ model.bookingAmount | formatMoney }}</p>
                                <p>创建时间 ：{{ model.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>状态 ：{{ model.sourceDocumentStatusName }}</p>
                                <p>发生时间 ：{{ model.lastModifyTime }}</p>
                                <p>备注 ：{{ model.summaryRevoke }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">贷款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>账户编号 ：<a :href="`${ctx}#/capital/account/virtual-acctount/${model.virtualAccountUuid}/detail`">{{model.virtualAccountNo}}</a></p>
                                <p>账户名称 ：{{model.virtualAccountName}}</p>
                                <p>客户类型 ：{{model.customerTypeName }}</p>
                                <p>{{ $utils.locale('financialContract.name') }} ：{{model.financialContractName}}</p>
                                <p>{{ $utils.locale('financialContract.no') }} ：<a :href="`${ctx}#/financial/contract/${model.financialContractUuid}/detail`">{{ model.financialContractNo}}</a></p>
                                <p>贷款合同编号 ：<a :href="`${ctx}#/data/contracts/detail?id=${model.contractId}`">{{ model.contractNo}}</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">充值单</h5>
                    <div class="bd">
                        <el-table 
                            :data="depositList"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column 
                                inline-template 
                                label="充值单号">
                                <a :href="`${ctx}#/capital/account/deposit-receipt/${model.sourceDocumentUuid}/detail`">{{row.sourceDocumentNo}}</a>
                            </el-table-column>
                            <el-table-column 
                                inline-template 
                                label="账户编号">
                                <a :href="`${ctx}#/capital/account/virtual-acctount/${model.virtualAccountUuid}/detail`">{{row.virtualAccountNo}}</a>
                            </el-table-column>
                            <el-table-column prop="virtualAccountName" label="账户名称">
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="充值金额">
                                <div>{{row.bookingAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="summary" label="备注">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <SystemOperateLog :for-object-uuid="$route.params.id"></SystemOperateLog>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            SystemOperateLog
        },
        data: function() {
            return {
                fetching: false,
                model: {}
            }
        },
        computed: {
            depositList: function() {
                var res = [];
                var {
                    sourceDocumentNo,
                    virtualAccountNo,
                    virtualAccountName,
                    bookingAmount,
                    summary,
                } = this.model;

                if (sourceDocumentNo) {
                    res.push({
                        sourceDocumentNo,
                        virtualAccountNo,
                        virtualAccountName,
                        bookingAmount,
                        summary,
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
                    url: `/capital/customer-account-manage/recharge-revoke-list/detail-data`,
                    data: {
                        sourceDocumentUuid
                    }
                }).then(data => {
                    this.model = Object.assign({}, this.model, data.rechargeRevokeDetailModel);
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
        }
    }
</script>