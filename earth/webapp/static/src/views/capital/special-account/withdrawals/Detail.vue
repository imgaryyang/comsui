<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '提现管理'}, { title: '提现单详情'}]">
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">提现信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>提现单号 ：{{detailModel.payerName}}</p>
                                <p>{{ $utils.locale('financialContract') }} ：{{detailModel.bankName}}</p>
                                <p>创建时间 ：{{detailModel.createTime}}</p>
                                <p>状态 ：{{detailModel.lastModifiedTime}}</p>
                                <p>发生时间 ：{{detailModel.occurAmount | formatMoney }}</p>
                                <p>提现金额 ：{{ detailModel.deductPlanNo}}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">对方账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>银行账号 ：{{ detailModel.paymentInstitution}}</p>
                                <p>账户姓名 ：{{detailModel.paymentInstitutionFlowNo}}</p>
                                <p>账户银行 ：{{detailModel.paymentWay}}</p>
                                <p>开户行所在地 ：{{detailModel.bindId}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">相关凭证</h5>
                    <el-table :data="voucherModel">
                        <el-table-column label="凭证编号" inline-template>
                            <a :href="`${ctx}#/capital/voucher//${row.voucherUuid}/detail`">
                                {{ row.voucherUuid }}
                            </a>
                        </el-table-column>
                        <el-table-column prop="paymentName" label="来往机构">
                        </el-table-column>
                        <el-table-column prop="paymentBank" :label="$utils.locale('financialContract')">
                        </el-table-column>
                        <el-table-column prop="paymentName" label="对方户名">
                        </el-table-column>
                        <el-table-column prop="paymentAccountNo" label="对方账号">
                        </el-table-column>
                        <el-table-column prop="paymentAccountNo" label="对方开户行">
                        </el-table-column>
                        <el-table-column label="凭证金额" inline-template>
                            <div> {{ row.transcationAmount | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column prop="createTime" label="交易通道">
                        </el-table-column>
                        <el-table-column prop="createTime" label="创建时间">
                        </el-table-column>
                        <el-table-column prop="stausModifiedTime" label="状态变更时间">
                        </el-table-column>
                        <el-table-column prop="voucherLogIssueStatus" label="凭证状态">
                        </el-table-column>
                    </el-table>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="detailModel.uuid">
                    </SystemOperateLog>
                </div>
            </div>

        </div>
    </div>

</template>

<script>
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default{
        components:{
            SystemOperateLog
        },
        activated: function() {

        },
        data: function() {
            return {
                fetching: false,
                detailModel: {},
                voucherModel: []
            }
        },
        methods: {

        }
    }
</script>