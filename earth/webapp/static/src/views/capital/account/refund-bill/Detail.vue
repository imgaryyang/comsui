<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '余额退款详情'}]">
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">退款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>退款单号 ：{{ detailModel.refundOrderNo}}</p>
                                <p>退款总金额 ：{{detailModel.amount | formatMoney }}</p>
                                <p>创建时间 ：{{detailModel.createTime}}</p>
                                <p>状态变更时间 ：{{detailModel.lastModifiedTime}}</p>
                                <p>退款状态 ：{{detailModel.refundStatusName}}</p>
                            </div>
                            <div class="col">
                                <p>退款资产编号 ：{{detailModel.refundAssetNo}}</p>
                                <p>备注 ：{{detailModel.remark}}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>账户编号 ：{{detailModel.accountNo}}</p>
                                <p>账户名称 ：{{detailModel.accountName}}</p>
                                <p>贷款人 ：{{detailModel.accountName}}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">退款明细</h5>
                    <el-table
                        stripe
                        border
                        class="td-15-padding th-8-15-padding no-th-border"
                        :data="[detailModel]">
                        <el-table-column label="本金" inline-template>
                            <div> {{ row.principal | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="利息" inline-template>
                            <div> {{ row.interest | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="贷款服务费" inline-template>
                            <div> {{ row.serviceCharge | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="技术维护费" inline-template>
                            <div> {{ row.maintenanceCharge | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="其他费用" inline-template>
                            <div> {{ row.otherCharge | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="逾期罚息" inline-template>
                            <div> {{ row.penaltyFee | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="逾期违约金" inline-template>
                            <div> {{ row.latePenalty | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="逾期服务费" inline-template>
                            <div> {{ row.lateFee | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="逾期其他费用" inline-template>
                            <div> {{ row.lateOtherCost | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="金额合计" inline-template>
                            <div> {{ row.totalFee | formatMoney }}</div>
                        </el-table-column>
                    </el-table>
                </div>
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="$route.params.refundOrderUuid">
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
            SystemOperateLog: require('views/include/SystemOperateLog'),
        },
        activated: function() {
            this.fetchDetail();
        },
        data: function() {
            return {
                detailModel: {},
            }
        },
        methods: {
            fetchDetail: function() {
                ajaxPromise({
                    url: `/refund/${this.$route.params.refundOrderUuid}/detail`
                }).then(data => {
                    this.detailModel = data.detail || {};
                }).catch(message => {
                    MessageBox.open(message);
                })
            }
        }
    }
</script>