<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '担保补足详情'}]">
                <el-button
                    v-if="ifElementGranted('disable-guarantee-complement') && !assetSet.guaranteeStatusLapsed"
                    type="primary"
                    size="small"
                    @click="onClickRevoke">作废</el-button>
                <el-button
                    v-if="assetSet.guaranteeStatusLapsed"
                    type="primary"
                    size="small"
                    @click="onClickAlive">激活
                    </el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">担保信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>担保补足号：{{ order.orderNo }}</p>
                                <p>担保金额 ：{{ order.totalRent | formatMoney }}</a></p>
                                <p>担保截止日 ：{{ order.dueDate | formatDate }}</span></p>
                                <p>发生时间 ：{{ order.modifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">还款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>还款编号 ：
                                    <a :href="`${ctx}#/finance/assets/${ assetSet.assetUuid }/detail`">{{ assetSet.singleLoanContractNo }}</a>
                                </p>
                                <p>商户还款计划编号 ：{{ assetSet.outerRepaymentPlanNo }}</p>
                                <p>计划还款金额 ：
                                    <span class="asset-recycle-date">{{ assetSet.assetInitialValue | formatMoney }}</span>
                                </p>
                                <p>计划还款日期 ：{{ assetSet.assetRecycleDate | formatDate }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">其他</h5>
                        <div class="bd">
                            <div class="col">
                                <p>商户编号 ：{{ assetSet.contract.app.appId  }}</p>
                                <p>状态 ： {{ assetSet.guaranteeStatus | GUARANTEE_STATUS_ZH_CN }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">线下支付记录</h5>
                    <div class="bd">
                        <el-table
                            :data="payDetails"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column label="支付单号" prop="deductId" inline-template>
                                <a :href="`${ctx}#/capital/payment-cash-flow/offline-payment/${row.offlineBill.id}/detail`">{{row.offlineBill.offlineBillNo}}</a>
                            </el-table-column>
                            <el-table-column label="银行名称" prop="offlineBill.bankShowName"></el-table-column>
                            <el-table-column label="账户姓名" prop="offlineBill.payerAccountName">
                            </el-table-column>
                            <el-table-column label="支付机构流水号" inline-template>
                                <a href="javascript: viod 0;">{{row.offlineBill.serialNo}}</a>
                            </el-table-column>
                            <el-table-column label="支付金额" prop="journalVoucher.bookingAmount"></el-table-column>
                            <el-table-column label="关联状态" prop="offlineBill.offlineBillConnectionState" inline-template>
                                <span>{{ row.offlineBill.offlineBillConnectionState | OFFLINE_BILL_CONNECTION_STATE_ZH_CN }}</span>
                            </el-table-column>
                            <el-table-column label="入账时间" inline-template>
                                <div>{{ row.offlineBill.tradeTime | formatDate("yyyy-MM-dd HH:mm:ss") }}</div>
                            </el-table-column>
                            <el-table-column label="发生时间" inline-template>
                                <div>{{ row.journalVoucher.createdDate | formatDate("yyyy-MM-dd HH:mm:ss") }}</div>
                            </el-table-column>
                            <el-table-column label="状态" prop="offlineBill.offlineBillStatus" inline-template>
                                <span>{{ row.offlineBill.offlineBillStatus | OFFLINE_BILL_STATUS_ZH_CN }}</span>
                            </el-table-column>
                            <el-table-column label="备注" prop="offlineBill.comment"></el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="order.repaymentBillId"></SystemOperateLog>
                </div>
            </div>
        </div>
        <RevokeModal
            v-model="revokeModal.show"
            :id="$route.params.id"
            @submit="submitRevoke">
        </RevokeModal>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import Enum from 'assets/javascripts/enum';


    // 差额补足状态（担保状态）
    const GUARANTEE_STATUS = new Enum([
        { NOT_OCCURRED: '未发生' },
        { WAITING_GUARANTEE: '待补足' },
        { HAS_GUARANTEE: '已补足' },
        { LAPSE_GUARANTEE: '担保作废' }
    ]);

    // 资产的资金状态
    const OFFLINE_BILL_STATUS = new Enum([
        { UNPAID: '失败' },
        { PAID: '成功' }
    ]);

    // 线下支付单关联状态
    const OFFLINE_BILL_CONNECTION_STATE = new Enum([
        { NONE: '未关联'},
        { PART: '部分关联'},
        { ALL: '已关联'},
    ]);


    export default{
        components:{
            SystemOperateLog: require('views/include/SystemOperateLog'),
            RevokeModal: require('./include/RevokeModal'),
        },
        data: function() {
            return {
                fetching: false,
                revokeModal: {
                    show: false
                },
                order:{},
                assetSet: {
                   contract:{
                        app: {
                            appId: ''
                        }
                   }
                },
                payDetails:[],
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        filters: {
            GUARANTEE_STATUS_ZH_CN: Enum.filter(GUARANTEE_STATUS),
            OFFLINE_BILL_STATUS_ZH_CN: Enum.filter(OFFLINE_BILL_STATUS),
            OFFLINE_BILL_CONNECTION_STATE_ZH_CN: Enum.filter(OFFLINE_BILL_CONNECTION_STATE)
        },
        methods: {
            fetch: function(id) {
                this.fetching = true;

                ajaxPromise({
                    url: `/guarantee/order/${id}/guarantee-detail`,
                }).then(data => {
                   this.order = data.order || {};
                   this.assetSet = data.assetSet || {};
                   this.payDetails = data.payDetails || [];
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
            onClickRevoke: function() {
                this.revokeModal.show = true;
            },
            onClickAlive: function() {
                const id = this.$route.params.id;

                MessageBox.open('确认激活？', null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/guarantee/order/${id}/guarantee-active`,
                            type: 'post',
                        }).then(data => {
                            MessageBox.close();
                            this.fetch(id);
                            this.$refs.sysLog.fetch();
                        }).catch(message => MessageBox.open(message))
                    }
                }]);
            },
            submitRevoke: function() {
                this.fetch(this.$route.params.id);
                this.$refs.sysLog.fetch();
            }
        }
    }
</script>
