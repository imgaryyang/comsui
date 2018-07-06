<style lang="sass">
    @import '~assets/stylesheets/base';

    .split-order-modal {

        @include min-screen(768px) {
            .modal-dialog {
                width: 85%;
                margin: 30px auto;
            }
        }

        .col-layout-detail {
            background: transparent;

            .top {
                border: none;
            }
        }

    }
</style>

<template>
  <Modal v-model="show" class="split-order-modal">
    <ModalHeader title="拆分结算单">
    </ModalHeader>
    <ModalBody align="left" v-loading="fetching">
        <div class="col-layout-detail">
            <div class="top">
                <div class="block">
                    <h5 class="hd">还款计划信息</h5>
                    <div class="bd">
                        <div class="col">
                            <p>还款编号：{{ model.repaymentPlanNo }}</p>
                            <p>应还金额总计：{{ model.planChargesDetail.totalFee | formatMoney }}</p>
                            <p>实际还款金额：-- </p>
                        </div>
                        <div class="col">
                            <p>计划还款本金：{{ model.planChargesDetail.loanAssetPrincipal | formatMoney }}</p>
                            <p>计划划款利息：{{ model.planChargesDetail.loanAssetInterest | formatMoney }}</p>
                            <p>贷款服务费：{{ model.planChargesDetail.loanServiceFee | formatMoney }}</p>
                            <p>技术维护费：{{ model.planChargesDetail.loanTechFee | formatMoney }}</p>
                            <p>其他费用：{{ model.planChargesDetail.loanOtherFee | formatMoney }}</p>
                        </div>
                        <div class="col">
                            <p>逾期罚息：{{ model.planChargesDetail.overdueFeePenalty | formatMoney }}</p>
                            <p>逾期违约金：{{ model.planChargesDetail.overdueFeeObligation | formatMoney }}</p>
                            <p>逾期服务费：{{ model.planChargesDetail.overdueFeeService | formatMoney }}</p>
                            <p>逾期其他费用：{{ model.planChargesDetail.overdueFeeOther | formatMoney }}</p>
                        </div>
                    </div>
                </div>
                <div class="block">
                    <h5 class="hd">账户信息</h5>
                    <div class="bd">
                        <div class="col">
                            <p>客户姓名：{{ model.accountInfo.payerName }} </p>
                            <p>客户账号：{{ model.accountInfo.payAcNo }}</p>
                            <p>账户开户行：{{ model.accountInfo.bank }}</p>
                            <p>开户行所在地：{{ model.accountInfo.province }} {{ model.accountInfo.city }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row-layout-detail">
            <div class="block">
                <div class="bd">
                    <el-table
                        :data="showOrders"
                        :context="_self" 
                        class="td-15-padding th-8-15-padding no-th-border"
                        stripe
                        border>
                        <el-table-column prop="normalOrderNo" label="结算单编号"></el-table-column>
                        <el-table-column 
                            :min-width="110"
                            inline-template 
                            label="还款本金">
                                <div>
                                    <div v-if="row.editting">
                                        <el-input 
                                            size="small" 
                                            v-model="insertPendingOrders[0].normalOrderChargesDetail.loanAssetPrincipal"></el-input>
                                    </div>
                                    <div v-else>{{ row.normalOrderChargesDetail.loanAssetPrincipal | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column 
                            :min-width="110"
                            inline-template 
                            label="还款利息">
                                <div>
                                    <div v-if="row.editting"><el-input size="small" v-model="insertPendingOrders[0].normalOrderChargesDetail.loanAssetInterest"></el-input></div>
                                    <div v-else>{{ row.normalOrderChargesDetail.loanAssetInterest | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column
                            :min-width="110" 
                            inline-template 
                            label="贷款服务费">
                                <div>
                                    <div v-if="row.editting"><el-input size="small" v-model="insertPendingOrders[0].normalOrderChargesDetail.loanServiceFee"></el-input></div>
                                    <div v-else>{{ row.normalOrderChargesDetail.loanServiceFee | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column 
                            :min-width="110"
                            inline-template 
                            label="技术维护费">
                                <div>
                                    <div v-if="row.editting"><el-input size="small" v-model="insertPendingOrders[0].normalOrderChargesDetail.loanTechFee"></el-input></div>
                                    <div v-else>{{ row.normalOrderChargesDetail.loanTechFee | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column 
                            :min-width="110"
                            inline-template 
                            label="其他费用">
                                <div>
                                    <div v-if="row.editting"><el-input size="small" v-model="insertPendingOrders[0].normalOrderChargesDetail.loanOtherFee"></el-input></div>
                                    <div v-else>{{ row.normalOrderChargesDetail.loanOtherFee | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column 
                            :min-width="110"
                            inline-template 
                            label="逾期罚息">
                                <div>
                                    <div v-if="row.editting"><el-input size="small" v-model="insertPendingOrders[0].normalOrderChargesDetail.overdueFeePenalty"></el-input></div>
                                    <div v-else>{{ row.normalOrderChargesDetail.overdueFeePenalty | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column 
                            :min-width="110"
                            inline-template 
                            label="逾期违约金">
                                <div>
                                    <div v-if="row.editting"><el-input size="small" v-model="insertPendingOrders[0].normalOrderChargesDetail.overdueFeeObligation"></el-input></div>
                                    <div v-else>{{ row.normalOrderChargesDetail.overdueFeeObligation | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column 
                            :min-width="110"
                            inline-template 
                            label="逾期服务费">
                                <div>
                                    <div v-if="row.editting"><el-input size="small" v-model="insertPendingOrders[0].normalOrderChargesDetail.overdueFeeService"></el-input></div>
                                    <div v-else>{{ row.normalOrderChargesDetail.overdueFeeService | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column 
                            :min-width="110"
                            inline-template 
                            label="逾期其他费用">
                                <div>
                                    <div v-if="row.editting"><el-input size="small" v-model="insertPendingOrders[0].normalOrderChargesDetail.overdueFeeOther"></el-input></div>
                                    <div v-else>{{ row.normalOrderChargesDetail.overdueFeeOther | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column 
                            inline-template 
                            label="结算金额">
                                <div>
                                    <div v-if="row.editting"></div>
                                    <div v-else>{{ row.normalOrderChargesDetail.totalFee | formatMoney }}</div>
                                </div>
                            </el-table-column>
                        <el-table-column 
                            width="95px" 
                            prop="lastModifyTime" 
                            label="状态变更时间"></el-table-column>
                        <el-table-column prop="normalOrderStatus" label="状态"></el-table-column>
                        <el-table-column 
                            inline-template 
                            width="85px"
                            label="操作">
                                <div>
                                    <div v-if="row.editting">
                                        <a href="#" @click.prevent="onSaveOrder">保存</a>
                                        <a href="#" @click.prevent="insertPendingOrders.splice(0, 1)">取消</a>
                                    </div>
                                    <div v-else>
                                        <a v-if="!['已关闭', '成功'].includes(row.normalOrderStatus)" href="#" @click.prevent="onCloseOrder(row.normalOrderId)">关闭</a>
                                        <!-- <a href="#" @click.prevent="onActivateOrder">激活</a> -->
                                    </div>
                                </div>
                            </el-table-column>
                    </el-table>
                </div>
                <div class="ft text-align-center" v-if="insertPendingOrders.length === 0">
                    <a href="#" @click.prevent="onCreateOrder">＋ 新增</a>
                </div>
            </div>
        </div>

        <split-legend 
            :show="show"
            :can-split-charges-detail="model.canSplitChargesDetail"
            :receivable-charges-detail="model.receivableChargesDetail"></split-legend>
    </ModalBody>
    <ModalFooter>
      <el-button 
        @click="submit" 
        type="success">完成</el-button>
    </ModalFooter>
  </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import SplitLegend from './SplitLegend';
    import modalMixin from './modal-mixin';

    export default {
        mixins: [modalMixin],
        components: {
            'split-legend': SplitLegend
        },
        props: {
            repaymentPlanUuid: {
                required: true
            }
        },
        watch: {
            show: function(cur) {
                if (cur) {
                    this.fetch();
                    this.insertPendingOrders = [];
                }
            }
        },
        data: function() {
            return {
                fetching: false,
                closing: false,
                saving: false,
                show: this.value,
                insertPendingOrders: [],
                model: {
                    canSplitChargesDetail: {},
                    accountInfo: {},
                    effectiveNormalOrders: [],
                    planChargesDetail: {},
                    receivableChargesDetail: {},
                    repaymentPlanNo: ''
                }
            }
        },
        computed: {
            showOrders: function() {
                return [].concat(this.model.effectiveNormalOrders, this.insertPendingOrders);
            }
        },
        methods: {
            onCloseOrder: function(orderId) {
                if (this.closing) return;

                this.closing = true;

                ajaxPromise({
                    url: `/payment-manage/order/close`,
                    data: {
                        type: 'normal',
                        orderId
                    },
                    type: 'POST'
                }).then(data => {
                    var model = this.model;
                    var index = model.effectiveNormalOrders.findIndex(item => item.normalOrderId === orderId);
                    Object.assign(model.effectiveNormalOrders[index], data.closedNormalOrder);
                    model.canSplitChargesDetail = data.canSplitChargesDetail;
                    this.fetchSysLog();
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.closing = false;
                });
            },
            onActivateOrder: function() {
                // not yet
            },
            onSaveOrder: function() {
                if (this.saving) return;

                this.saving = true;

                var orderInfo = Object.assign({}, this.insertPendingOrders[0].normalOrderChargesDetail);

                Object.keys(orderInfo).forEach(key => {
                    if (!orderInfo[key]) {
                        orderInfo[key] = 0;
                    }
                });

                ajaxPromise({
                    url: `/payment-manage/order/add`,
                    type: 'POST',
                    data: {
                        type: 'normal',
                        repaymentPlanUuid: this.repaymentPlanUuid,
                        orderInfo: JSON.stringify(orderInfo)
                    }
                }).then(data => {
                    this.model.effectiveNormalOrders.push(data.newNormalOrder);
                    this.model.canSplitChargesDetail = data.canSplitChargesDetail;
                    this.insertPendingOrders.splice(0, 1);
                    this.fetchSysLog();
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.saving = false;
                });
            },
            onCreateOrder: function(e) {
                if (this.insertPendingOrders.length) return;
                this.insertPendingOrders.push({
                    editting: true,
                    createTime: "",
                    lastModifyTime: "",
                    normalOrderAmount:  0,
                    normalOrderChargesDetail: {
                        loanAssetInterest: '',
                        loanAssetPrincipal: '',
                        loanOtherFee: '',
                        loanServiceFee: '',
                        loanTechFee: '',
                        overdueFeeObligation: '',
                        overdueFeeOther: '',
                        overdueFeePenalty: '',
                        overdueFeeService: '',
                        totalFee: '',
                        totalOverdueFee: '',
                    },
                    normalOrderId: '',
                    normalOrderNo: '',
                    normalOrderStatus: ''
                });
            },
            fetch: function() {
                if (this.fetching) return;

                this.fetching = true;

                ajaxPromise({
                    url: `/payment-manage/order/split`,
                    data: {
                        repaymentPlanUuid: this.repaymentPlanUuid
                    }
                }).then(data => {
                    this.model = Object.assign({
                        canSplitChargesDetail: {},
                        accountInfo: {},
                        effectiveNormalOrders: [],
                        planChargesDetail: {},
                        receivableChargesDetail: {},
                        repaymentPlanNo: ''
                    }, data);
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            submit: function() {
                this.show = false;
                this.$emit('submit');
            }
        }
    }
</script>
