<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '接口线上支付单详情'}]">
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">支付信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>支付单号 ：{{ detailModel.deductPlanNo}}</p>
                                <p>订单编号 ：
                                    <a :href="`${ctx}#/finance/application/${ detailModel.deudctNo }/detail`">{{ detailModel.deudctNo }}</a>
                                </p>
                                <p>支付机构 ：{{ detailModel.paymentInstitution}}</p>
                                <p>交易金额 ：{{detailModel.occurAmount | formatMoney }}</p>
                                <p>支付状态 ：{{detailModel.paymentStatus}}</p>
                                <p>备注 ：{{detailModel.remark}}</p>
                            </div>
                            <div class="col">
                                <p>创建时间 ：{{detailModel.createTime}}</p>
                                <p>状态变更时间 ：{{detailModel.lastModifiedTime}}</p>
                                <p>交易时间 ：{{detailModel.transactionTime}}</p>
                                <p>清算时间 ：{{detailModel.settleTime}}</p>
                                <p>资金入账时间 ：{{detailModel.clearTime}}</p>
                                <p>通道请求号 ：{{detailModel.paymentInstitutionTradeNo}}</p>
                                <p>通道流水号 ：{{detailModel.paymentInstitutionFlowNo}}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">付款方账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>付款方账户名 ：{{detailModel.payerName}}</p>
                                <p>付款方账户号 ：{{detailModel.bankAccountNo}}</p>
                                <p>账户开户行 ：{{detailModel.bankName}}</p>
                                <p>开户行所在地 ：{{detailModel.bankProvince}} {{detailModel.bankCity}}</p>
                                <p>银行编号 ：{{detailModel.bankNo}}</p>
                                <p>证件号 ：{{detailModel.identityNo}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">第三方支付凭证</h5>
                    <PagingTable
                        :data="voucherModel"
                        :pagination="true">
                        <el-table-column label="凭证编号" prop="voucherUuid">

                        </el-table-column>

                        <el-table-column prop="paymentBank" label="来往机构">
                        </el-table-column>

                        <el-table-column prop="paymentName" label="机构账户名">
                        </el-table-column>

                        <el-table-column prop="paymentAccountNo" label="机构账户号">
                        </el-table-column>

                        <el-table-column label="凭证金额" inline-template>
                            <div> {{ row.transcationAmount | formatMoney }}</div>
                        </el-table-column>

                        <el-table-column prop="voucherSource" label="凭证来源">
                        </el-table-column>

                        <el-table-column prop="transcationGateway" label="交易网关">
                        </el-table-column>

                        <el-table-column prop="createTime" label="创建时间">
                        </el-table-column>

                        <el-table-column prop="voucherLogStatus" label="校验状态">
                        </el-table-column>

                        <el-table-column prop="voucherLogIssueStatus" label="凭证状态">
                        </el-table-column>
                    </PagingTable>
                </div>
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="detailModel.deductPlanNo">
                    </SystemOperateLog>
                </div>
            </div>

        </div>
    </div>

</template>

<script>
    import { ctx } from 'src/config';
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default{
        components:{
            SystemOperateLog: require('views/include/SystemOperateLog'),
            PagingTable: require('views/include/PagingTable')
        },
        activated: function() {
            this.fetch();
        },
        data: function() {
            return {
                fetching: false,
                detailModel: {},
                voucherModel: []
            }
        },
        methods: {
            fetch: function() {
                this.fetching = true;

                ajaxPromise({
                    url: `/interfacePayment/list/${this.$route.params.id}/detail`
                }).then(data => {
                    this.detailModel = data.detailModel || {};
                    this.voucherModel = data.voucherModel ? [data.voucherModel] : [];
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            }
        }
    }
</script>