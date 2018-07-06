<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{title: '清算凭证详情'}]">
                <!-- <el-button
                    size="small"
                    type="primary"
                    @click="invalidVoucher"
                    v-if="detail.clearingVoucherStatus === 'DONE'">
                    作废
                </el-button> -->
            </Breadcrumb>
            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">凭证信息</h5>
                        <div class="bd">
                            <div class="col" style="width: 50%;">
                                <p>凭证编号：{{ clearingVoucher.voucherNo }}</p>
                                <p>对账任务：
                                    <a :href="`/v#/capital/third-party-account/beneficiary/${item.key}/detail`"
                                        v-for="(item, index) in auditJobIdList">
                                        {{item.value}}
                                        <span v-if="index != auditJobIdList.length -1">,</span>
                                    </a>
                                </p>
                                <p>凭证金额：
                                    {{ clearingVoucher.voucherAmount | formatMoney }}
                                </p>
                                <p>凭证状态：{{ clearingVoucher.clearingVoucherStatus | VoucherStatus}}</p>
                            </div>
                            <div class="col">
                                <p>创建时间：{{ clearingVoucher.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>状态变更时间：{{ clearingVoucher.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>资金入账时间：{{ clearingVoucher.cashFlowTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>备注：{{ clearingVoucher.comment || '无'}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        凭证资金流水
                    </h5>
                    <div class="bd">
                        <PagingTable
                            :data="cashFlows"
                            :pagination="true"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="cashFlowUuid" label="流水号"></el-table-column>
                                <el-table-column inline-template label="交易金额">
                                    <div> {{ row.transactionAmount | formatMoney }}</div>
                                </el-table-column>
                                <el-table-column prop="counterAccountName" label="对方户名"></el-table-column>
                                <el-table-column prop="counterAccountNo" label="对方账号"></el-table-column>
                                <el-table-column prop="counterBankInfo" label="对方开户行"></el-table-column>
                                <el-table-column inline-template label="入账时间">
                                    <div>
                                        {{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                    </div>
                                </el-table-column>
                                <el-table-column prop="remark" label="摘要"></el-table-column>
                        </PagingTable>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">资金明细汇总</h5>
                    <div class="bd">
                        <PagingTable
                                :data="capitalDetailList"
                                :pagination="true"
                                class="td-15-padding th-8-15-padding no-th-border"
                                stripe
                                border>
                            <el-table-column prop="contractName" label = "信托项目"></el-table-column>
                            <el-table-column prop="principal" label="还款本金"></el-table-column>
                            <el-table-column prop="interest" label="还款利息"></el-table-column>
                            <el-table-column prop="loanServiceFee" label="贷款服务费"></el-table-column>
                            <el-table-column prop="techFee" label="技术维护费"></el-table-column>
                            <el-table-column prop="otherFee" label="其他费用"></el-table-column>
                            <el-table-column prop="overduePenalty" label="逾期罚息"></el-table-column>
                            <el-table-column prop="overdueDefaultFee" label="逾期违约金"></el-table-column>
                            <el-table-column prop="overdueServiceFee" label="逾期服务费"></el-table-column>
                            <el-table-column prop="overdueOtherFee" label="逾期其他费用"></el-table-column>
                        </PagingTable>
                    </div>
                </div>
            </div>

            <QueryFailDeductPlanTable
                title="清算失败"
                :autoload="false"
                :clearingVoucherUuid="$route.params.voucherUuid"
                :failAuditJobUuids="failAuditJobUuids"
                v-show="failAuditJobUuids.length!=0"
                v-model="refreshTable"
                key="voucher-clearing-failure">
            </QueryFailDeductPlanTable>
            <QueryClearDeductPlanTable
                title="清算成功"
                :autoload="false"
                :clearingVoucherUuid="$route.params.voucherUuid"
                :auditJobIdList="auditJobIdList"
                v-model="refreshTable"
                key="voucher-clearing-success">
            </QueryClearDeductPlanTable>

            <div class="row-layout-detail">
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="clearingVoucher.uuid">
                    </SystemOperateLog>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import SystemOperateLog from 'views/include/SystemOperateLog';

    export default{
        components: {
            PagingTable: require('views/include/PagingTable'),
            QueryFailDeductPlanTable: require('./include/QueryFailDeductPlanTable'),
            QueryClearDeductPlanTable: require('./include/QueryClearDeductPlanTable'),
            SystemOperateLog
        },
        data: function() {
            return {
                fetching: false,
                clearingVoucher: {},
                cashFlows: [],
                capitalDetailList:[],                  // 资金明细汇总数据
                auditJobIdList: [],
                failAuditJobUuids: [],
                refreshTable: false,
            }
        },
        activated: function() {
            this.fetchDetail();
        },
        filters: {
            VoucherStatus(value){
                return {
                    DOING: '处理中',
                    DONE:'已核销',
                    Fail: '存在核销明细失败',
                    INVALID: '已作废'
                }[value]
            }
        },
        methods: {
            fetchDetail: function() {
                this.fetching = true;
                ajaxPromise({
                    url: `clearingVoucher/${this.$route.params.voucherUuid}/detail/basic-info`
                }).then(data => {
                    this.clearingVoucher = data.clearingVoucher || {};
                    this.cashFlows = data.cashFlows || [];
                    this.capitalDetailList= data.capitalDetailList || [];
                    this.auditJobIdList = data.auditJobIdList ||{};
                    this.failAuditJobUuids = data.failAuditJobUuids ||[];
                    this.refreshTable = true;
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                })
            },
        }
    }
</script>