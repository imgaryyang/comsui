<style lang="sass">
    #table-arrow {
        .icon-box {
            width:12px;
            transition: transform .2s ease-in-out;
        }
        .icon-box.icon-expand{
            transform: rotate(90deg);

        }
    }
</style>
<template>
    <div>
        <div class="row-layout-detail">
            <QueryTable
                :isEleTable="true"
                :title="title"
                :autoload="autoload"
                :querybtn="false"
                key="query-table"
                ref="queryTable"
                :queryConditions="queryConditions"
                :action="`clearingVoucher/${queryConds.auditJobUuid}/queryFairDeductPlan?clearingVoucherUuid=${clearingVoucherUuid}`">
                <template slot="query-form">
                    <el-form-item label="对账任务：">
                        <el-select
                            @option-select="fetch"
                            v-model="queryConds.auditJobUuid"
                            placeholder="对账任务"
                            size="small">
                            <el-option
                                v-for="item in failAuditJobUuids"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </template>
                <template slot="custom-table" scope="props">
                    <thead>
                        <tr><th :key="index" v-for="(title, index) in failTableTitle" class="is-leaf"><div class="cell">{{title}}</div></th></tr>
                    </thead>
                    <tbody class=" el-table--border" id="table-arrow">
                        <tr v-if="props.data.length == 0" class="el-table__empty-block"><td colspan="11">暂无数据</td></tr>
                        <template v-else v-for="(item, index) in props.data">
                            <tr>
                                <td><div class="cell"><a :href="`${ctx}#/capital/payment-cash-flow/online-payment/${ item.deductPlanId }/detail`">{{ item.deductPlanNo }}</a></div></td>
                                <td><div class="cell">线上支付单</div></td>
                                <td><div class="cell">{{item.payerName}}</div></td>
                                <td><div class="cell">{{item.occurAmount}}</div></td>
                                <td><div class="cell">{{item.orderType}}</div></td>
                                <td><div class="cell">{{item.paymentInstitutionTradeNo}}</div></td>
                                <td><div class="cell">{{item.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div></td>
                                <td><div class="cell">{{item.paymentStatus}}</div></td>
                                <td><div class="cell">{{item.clearingStatus}}</div></td>
                                <td><div class="cell"><a href="javascript: void 0;">重新清算</a></div></td>
                                <td><div class="cell">
                                    <div :class="{'icon-box':true,'icon-expand': item.expand}" @click.prevet="expandDetail(index,item)" >
                                        <i style="cursor: pointer"  class="el-icon-arrow-down"></i>
                                    </div>
                                </div></td>
                            </tr>
                            <tr v-if="item.expand">
                                <td colspan="11" style="padding: 20px">
                                    <div v-loading="detailList[index] && detailList[index].fetching">
                                        <h6 class ="hd">还款记录</h6>
                                        <el-table :data="detailList[index] && detailList[index].list">
                                            <el-table-column type="index" label="记录编号" width="100px"></el-table-column>
                                            <el-table-column inline-template label="扣款订单编号">
                                                <div>{{row.deductApplicationUuid}}</div>
                                            </el-table-column>
                                            <el-table-column inline-template label="还款计划编号">
                                                <div>{{row.repaymentRecordDetail.repaymentPlanNo}}</div>
                                            </el-table-column>
                                            <el-table-column inline-template label="计划还款日期">
                                                <div>{{row.repaymentRecordDetail.planDate | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
                                            </el-table-column>
                                            <el-table-column inline-template label="实际还款日期">
                                                <div>{{row.repaymentRecordDetail.actualRecycleDate | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
                                            </el-table-column>
                                            <el-table-column inline-template label="资金入账时间">
                                                <div>{{row.repaymentRecordDetail.accountedDate | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
                                            </el-table-column>
                                            <el-table-column inline-template label="本次实收金额">
                                                <el-popover
                                                        trigger="hover"
                                                        placement="top">
                                                    <template>
                                                        <div>应扣本金：{{ row.repaymentRecordDetail.loanAssetPrincipal | formatMoney }}</div>
                                                        <div>应扣利息：{{ row.repaymentRecordDetail.loanAssetInterest | formatMoney }}</div>
                                                        <div>贷款服务费：{{ row.repaymentRecordDetail.loanServiceFee | formatMoney }}</div>
                                                        <div>技术维护费：{{ row.repaymentRecordDetail.loanTechFee | formatMoney }}</div>
                                                        <div>其他费用：{{ row.repaymentRecordDetail.loanOtherFee | formatMoney }}</div>
                                                        <div>逾期罚息：{{ row.repaymentRecordDetail.overdueFeePenalty | formatMoney }}</div>
                                                        <div>逾期违约金：{{ row.repaymentRecordDetail.overdueFeeObligation | formatMoney }}</div>
                                                        <div>逾期服务费：{{ row.repaymentRecordDetail.overdueFeeService | formatMoney }}</div>
                                                        <div>逾期其他费用：{{ row.repaymentRecordDetail.overdueFeeOther | formatMoney }}</div>
                                                    </template>
                                                    <span slot="reference">{{ row.repaymentRecordDetail.totalFee | formatMoney }}</span>
                                                </el-popover>
                                            </el-table-column>

                                            <el-table-column inline-template label="交付通道">
                                                <div>{{row.repaymentRecordDetail.paymentGateway}}</div>
                                            </el-table-column>
                                            <el-table-column inline-template label="相关凭证">
                                                <div><a :href="`${ctx}#/capital/voucher/third-party/${row.repaymentRecordDetail.voucherNo}/detail`">{{row.repaymentRecordDetail.voucherNo}}</a></div>

                                            </el-table-column>
                                        </el-table>
                                    </div>

                                </td>
                            </tr>
                        </template>
                    </tbody>
                </template>
            </QueryTable>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    export default {
        components: {
            QueryTable: require('views/include/QueryTable')
        },
        props: {
            autoload: {
                type: Boolean,
                default: false
            },
            title: {
                type: String,
                default: ''
            },
            value: {
                type: Boolean,
                default: false
            },
            failAuditJobUuids: {
                type: Array,
                default: () => []
            },
            clearingVoucherUuid: String
        },
        data: function() {
            return {
                queryConds: {
                    auditJobUuid: '',
                },
                failTableTitle: ['来源订单号', '单据类型', '对方账户', '交易金额', '借贷标记', '通道请求号', '状态变更时间', '执行状态', '清算状态', '操作', ''],
                detailList: {}
            }
        },
        computed:{
            tableValue: {
                get(){
                    return this.value
                },
                set(value){
                    this.$emit('input', value)
                }
            },
        },
        watch: {
            failAuditJobUuids: function(current) {
                this.queryConds.auditJobUuid = current.length ? current[0].key : '';
            },
            'queryConds.auditJobUuid': function (current) {
                if(current) {
                    this.$nextTick(() => {
                        this.fetch();
                    })
                } else {
                    this.$refs.queryTable.clearList();
                }
            }
        },
        methods: {
            fetch() {
                this.$refs.queryTable.fetch();
            },
            expandDetail: function (index, item) {
                if(item.expand == undefined){
                    this.$set(item, 'expand',false)
                }
                item.expand = !item.expand;
                if (!item.expand) return;
                this.detailList = Object.assign({}, this.detailList, {
                    [index]: {
                        fetching: true,
                        list: []
                    }
                });
                ajaxPromise({
                    url: `/clearingVoucher/${item.deductPlanNo}/detail/repayment-record`
                }).then(newData => {
                    this.detailList[index].list = newData.list
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.detailList[index].fetching = false;
                })
            }
        }
    }
</script>