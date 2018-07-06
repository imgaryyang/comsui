<style>
    .tab-menu-beneficiary {
        margin-bottom: 20px;
    }
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb
                :routes="[{ title: '代收-第三方机构' }, {title: '详情'}]">
                <el-button v-if="auditJob.clearingStatusName === '未清算'&& totalReceivableBills.totalReceivableAmount > 0 && (auditJob.commandAuditResult== 'LOCAL' || auditJob.commandAuditResult=='ISSUED')" size="small" type="primary" @click="singleClearing">清算</el-button>
                <el-button size="small" type="primary" :loading="reBeneficiaring" @click="onClickBeneficiary" v-if="ifElementGranted('redo-beneficiary-audit')">重新对账</el-button>
                <el-button size="small" type="primary" @click="exportTask">导出结果</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block" >
                        <h5 class="hd">
                            对账任务信息
                        </h5>
                        <div class="bd">
                            <div class="col">
                                <p>任务编号 ：{{ auditJob.auditJobNo }}</p>
                                <p>创建时间 ：{{ auditJob.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>对账状态 ：{{ auditJob.auditResultName }}</p>
                                <p v-if="auditJob.clearingStatusName === '未清算'">待清算金额 ：{{ totalReceivableBills.totalReceivableAmount | formatMoney}}
                                    <!--<el-button :disabled="clearingBtnFetching" size="mini" @click="fetchClearingAmount" style="margin-left: 10px;">-->
                                        <!--<span v-if="!clearingBtnFetching">更新</span>-->
                                        <!--<span v-else>更新中...</span>-->
                                    <!--</el-button>-->
                                </p>
                                <p v-else>待清算金额 ：{{ 0 | formatMoney}}
                                    <!--<el-button :disabled="clearingBtnFetching" size="mini" @click="fetchClearingAmount" style="margin-left: 10px;">-->
                                        <!--<span v-if="!clearingBtnFetching">更新</span>-->
                                        <!--<span v-else>更新中...</span>-->
                                    <!--</el-button>-->
                                </p>
                                <p>已清算金额 ：{{ totalReceivableBills.totalClearedAmount | formatMoney }}</p>
                                <p>清算状态 ：{{ auditJob.clearingStatusName }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block" >
                        <h5 class="hd">
                            通道信息
                        </h5>
                        <div class="bd">
                            <div class="col">
                                <p>交易通道 ：{{ paymentChannel.paymentChannelName }}</p>
                                <p>收付类型 ：代收</p>
                                <p>关联信托 ：{{ paymentChannel.relatedFinancialContractName }}</p>
                                <p>入账起始时间 ：{{ auditJob.startTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>入账终止时间 ：{{ auditJob.endTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <div class="table clearfix">
                        <table class="pull-left" style="width: 45%">
                            <tbody>
                                <tr>
                                    <td colspan="3">本端流水
                                        <!--<el-button :disabled="localCashFlowBtnFetching" size="mini" @click="refreshLocalCashFlow" style="margin-left: 10px;">-->
                                            <!--<span v-if="!localCashFlowBtnFetching">更新</span>-->
                                            <!--<span v-else>更新中...</span>-->
                                        <!--</el-button>-->
                                    </td>
                                </tr>
                                <tr>
                                    <td>还款金额：{{ totalReceivableBills.totalAmount | formatMoney}}</td>
                                    <td>退款金额：0</td>
                                    <td>合计：{{ totalReceivableBills.totalAmount - 0 | formatMoney }}</td>
                                </tr>
                                <tr>
                                    <td>还款笔数：{{ totalReceivableBills.totalNum }} 笔</td>
                                    <td>退款笔数：0 笔</td>
                                    <td>合计：{{ totalReceivableBills.totalNum }}笔</td>
                                </tr>
                            </tbody>
                        </table>
                        <table class="pull-right" style="width: 45%">
                            <tbody>
                                <tr>
                                    <td colspan="3">对端流水
                                        <!--<el-button :disabled="counterCashFlowBtnFetching" size="mini" @click="refreshCounterCashFlow" style="margin-left: 10px;">-->
                                            <!--<span v-if="!counterCashFlowBtnFetching">更新</span>-->
                                            <!--<span v-else>更新中...</span>-->
                                        <!--</el-button>-->
                                    </td>
                                </tr>
                                <tr>
                                    <td>还款金额：{{ detailAmount | formatMoney}}</td>
                                    <td>退款金额：0</td>
                                    <td>合计：{{ detailAmount - 0 | formatMoney }}</td>
                                </tr>
                                <tr>
                                    <td>还款笔数：{{ detailNum }} 笔</td>
                                    <td>退款笔数：0 笔</td>
                                    <td>合计：{{ detailNum }}笔</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="row-layout-detail">
                <div class="block">
                    <TabMenu class="tab-menu-beneficiary" v-model="selected">
                        <TabMenuItem id="LOCAL">本端多账</TabMenuItem>
                        <TabMenuItem id="COUNTER">对端多账</TabMenuItem>
                        <TabMenuItem id="ISSUED">已平账</TabMenuItem>
                    </TabMenu>
                    <TabContent v-model="selected">
                        <TabContentItem id="LOCAL">
                            <LocalBeneficiary
                                :action="`/audit/beneficiary/${$route.params.uuid}/detail/audit-result?resultCode=1&auditJobUuid=${auditJob.uuid}`"
                                :selected="selected == 'LOCAL'"
                                :autoload="selected == 'LOCAL'"
                                @refresh="fetchDetail"
                                v-model="refreshTable"
                                :auditJobUuid="$route.params.uuid">
                            </LocalBeneficiary>
                        </TabContentItem>
                        <TabContentItem id="COUNTER">
                            <CounterBeneficiary
                                :action="`/audit/beneficiary/${$route.params.uuid}/detail/audit-result?resultCode=3&auditJobUuid=${auditJob.uuid}`"
                                @refresh="fetchDetail"
                                :selected="selected == 'COUNTER'"
                                :autoload="selected == 'COUNTER'"
                                v-model="refreshTable"
                                :auditJobUuid="$route.params.uuid">
                            </CounterBeneficiary>
                        </TabContentItem>
                        <TabContentItem id="ISSUED">
                            <IssuedBeneficiary
                                :action="`/audit/beneficiary/${$route.params.uuid}/detail/audit-result?resultCode=2&auditJobUuid=${auditJob.uuid}`"
                                v-model="refreshTable"
                                :selected="selected == 'ISSUED'"
                                :autoload="selected == 'ISSUED'"
                                :auditJobUuid="$route.params.uuid">
                            </IssuedBeneficiary>
                        </TabContentItem>
                    </TabContent>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        清算凭证
                    </h5>
                    <div class="bd">
                        <el-table
                            :data="clearingVoucher"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="voucherNo" label="凭证编号" inline-template>
                            	<div>
                                	<a v-if="row.clearingVoucherStatus !== 'DOING'" :href="`${ctx}#/capital/voucher/clearing/${ row.voucherUuid }/detail`">{{ row.voucherNo }}</a>
                                	<span v-else>{{row.voucherNo}}</span>
                                </div>
                            </el-table-column>
                            <el-table-column prop="bankInfo" label="来往机构"></el-table-column>
                            <el-table-column prop="counterAccountName" label="机构账户名"></el-table-column>
                            <el-table-column prop="counterAccountNo" label="机构账户号"></el-table-column>
                            <el-table-column inline-template label="凭证金额">
                                <div>{{ row.voucherAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="paymentInstitutionName" label="交易网关"></el-table-column>
                            <el-table-column inline-template label="资金入账时间">
                                <div>
                                    {{ row.cashFlowTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                </div>
                            </el-table-column>
                            <el-table-column inline-template label="状态变更时间">
                                <div>
                                    {{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}
                                </div>
                            </el-table-column>
                            <el-table-column prop="clearingVoucherStatusName" label="凭证状态"></el-table-column>
                        </el-table>
                    </div>
                </div>
            </div>
            <div class="row-layout-detail">
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="$route.params.uuid">
                    </SystemOperateLog>
                </div>
            </div>
        </div>
        <ClearingCashFlowModal
            @submit="fetchDetail"
            v-model="clearingCashFlowModal.visible"
            :model="clearingCashFlowModal.model">
        </ClearingCashFlowModal>
    </div>
</template>

<script>
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';

    export default{
        components:{
            SystemOperateLog,
            TabMenu, TabMenuItem, TabContent, TabContentItem,
            LocalBeneficiary: require('./include/LocalBeneficiary'),
            CounterBeneficiary: require('./include/CounterBeneficiary'),
            IssuedBeneficiary: require('./include/IssuedBeneficiary'),
            ClearingCashFlowModal: require('./include/ClearingCashFlowModal'),
        },
        data: function() {
            return {
                selected: 'LOCAL',
                selectedSecond: 'TOTAL',
                reBeneficiaring: false,
                fetching: false,
                refreshTable: false,

                auditJob: {},
                paymentChannel: {},
                totalReceivableBills: {},
                detailNum: '',
                detailAmount: '',
                clearingVoucher: [],

                clearingCashFlowModal: {
                    visible: false,
                    model: {}
                },

                clearingBtnFetching: false,
                localCashFlowBtnFetching: false,
                counterCashFlowBtnFetching: false,
            }
        },
        activated: function() {
            this.selected = 'LOCAL';
            this.selectedSecond = 'TOTAL';
            this.fetch(this.$route.params.uuid);
        },
        methods: {
            fetchDetail: function() {
                this.fetch(this.$route.params.uuid);
                this.$refs.sysLog.fetch();
                this.refreshTable = true;
            },
            fetch: function(auditJobUuid) {
                this.fetching = true;

                ajaxPromise({
                    url: `/audit/beneficiary/${auditJobUuid}/detail/basic-info`
                }).then(data => {
                    this.auditJob = Object.assign({}, data.auditJob);
                    this.paymentChannel = Object.assign({}, data.paymentChannel);
                    this.totalReceivableBills = Object.assign({}, data.totalReceivableBills);
                    this.detailNum = data.detailNum;
                    this.detailAmount = data.detailAmount;
                    this.clearingVoucher = data.clearingVoucher ? [data.clearingVoucher] : [];
                }).catch(message => {
                   MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            onClickBeneficiary: function() {
                MessageBox.open(`<div style="margin: 25px 0;">交易繁忙时期，重新对账操作会占用较多性能<br>是否继续执行？</div>`, null, [{
                    text: '关闭',
                    handler: () => {
                        MessageBox.close();
                    }
                }, {
                    text: '确认',
                    type: 'success',
                    handler: () => {
                        MessageBox.close();
                        if (this.reBeneficiaring) return;
                        this.reBeneficiaring = true;
                        ajaxPromise({
                            url: `/audit/beneficiary/${this.$route.params.uuid}/redo`,
                            type: 'post'
                        }).then(data => {
                           this.fetchDetail();
                           MessageBox.open('对账成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        }).then(() => {
                            this.reBeneficiaring = false;
                        });
                    }
                }]);
            },
            exportTask: function() {
                var params = Object.assign({}, {auditJobUuid: this.$route.params.uuid});
                downloadFile(`${this.api}/report/export?reportId=19`, params);
            },
            fetchClearingAmount: function() {
                this.clearingBtnFetching = true;
                ajaxPromise({
                    url: `/audit/beneficiary/${this.$route.params.uuid}/update/auditJob-amount`,
                }).then(data => {
                    this.totalReceivableBills = data.totalReceivableBills || {};
                    MessageBox.open('更新成功');
                }).catch(message => {
                    MessageBox.open(message)
                }).then(() => {
                    this.clearingBtnFetching = false;
                })
            },
            refreshLocalCashFlow: function() {
                this.localCashFlowBtnFetching = true;
                ajaxPromise({
                    url: `/audit/beneficiary/${this.$route.params.uuid}/update/auditJob-amount`,
                }).then(data => {
                    this.totalReceivableBills = data.totalReceivableBills || {};
                    MessageBox.open('更新成功');
                }).catch(message => {
                    MessageBox.open(message)
                }).then(() => {
                    this.localCashFlowBtnFetching = false;
                })
            },
            refreshCounterCashFlow: function() {
                this.counterCashFlowBtnFetching = true;
                ajaxPromise({
                    url: `/audit/beneficiary/${this.$route.params.uuid}/flow/reFresh`,
                }).then(data => {
                    this.detailNum = data.detailNum;
                    this.detailAmount = data.detailAmount;
                    MessageBox.open('更新成功');
                }).catch(message => {
                    MessageBox.open(message)
                }).then(() => {
                    this.counterCashFlowBtnFetching = false;
                })
            },
            singleClearing: function() {
                this.clearingCashFlowModal.model.totalAmount = this.totalReceivableBills.totalReceivableAmount;
                this.clearingCashFlowModal.model.auditJobUuidList = [this.$route.params.uuid];
                this.clearingCashFlowModal.visible = true;
            }

        }
    }
</script>