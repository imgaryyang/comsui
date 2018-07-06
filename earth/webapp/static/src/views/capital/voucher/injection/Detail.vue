<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '专户注资凭证'}, { title: '专户注资凭证详情'}]">
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">凭证信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>凭证单号 ：{{detailModel.payerName}}</p>
                                <p>{{ $utils.locale('financialContract') }} ：{{detailModel.bankName}}</p>
                                <p>凭证金额 ：{{detailModel.occurAmount | formatMoney }}</p>
                                <p>创建时间 ：{{detailModel.createTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
                            </div>
                            <div class="col">
                                <p>状态变更时间 ：{{ detailModel.deductPlanNo | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
                                <p>状态 ：{{detailModel.lastModifiedTime}}</p>
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
                    <h5 class="hd">凭证业务单据</h5>
                    <el-table :data="voucherModel">
                        <el-table-column label="注资单号" inline-template>
                            <a :href="`${ctx}#/capital/special-account/injection/${row.temporaryDepositDocUuid}/detail`">{{row.temporaryDepositDocNo }}</a>
                        </el-table-column>
                        <el-table-column label="注资金额" prop="remainAmount" inline-template>
                            <div>{{ row.remainAmount | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column prop="stausModifiedTime" label="状态变更时间">
                        </el-table-column>
                        <el-table-column prop="transcationStatus" label="交易状态">
                        </el-table-column>
                        <el-table-column label="备注" prop="remark">
                        </el-table-column>
                    </el-table>
                </div>

                <div class="block">
                    <h5 class="hd">凭证银行流水</h5>
                    <el-table :data="voucherModel">
                        <el-table-column label="流水号" inline-template>
                            <a :href="`${ctx}#/capital/special-account/injection/${row.temporaryDepositDocUuid}/detail`">{{row.temporaryDepositDocNo }}</a>
                        </el-table-column>
                        <el-table-column label="交易金额" prop="remainAmount" inline-template>
                            <div>{{ row.remainAmount | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column prop="paymentName" label="对方户名">
                        </el-table-column>
                        <el-table-column prop="paymentAccountNo" label="对方账号">
                        </el-table-column>
                        <el-table-column prop="paymentAccountNo" label="对方开户行">
                        </el-table-column>
                        <el-table-column prop="stausModifiedTime" label="流水入账时间">
                        </el-table-column>
                        <el-table-column label="备注" prop="remark">
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