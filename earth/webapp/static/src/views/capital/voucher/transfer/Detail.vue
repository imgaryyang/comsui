<style>
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '专户转账凭证详情'}]">
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">凭证信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>项目名称 ：{{ basicInfo.financialContractCode }}</p>
                                <p>凭证编号 ：{{ basicInfo.voucherNo }}</p>
                                <p>凭证金额 ：{{ basicInfo.amount | formatMoney}}</p>
                                <p>备注 ：{{ basicInfo.remark }}</p>
                            </div>
                            <div class="col">
                                <p>交易通道 ：{{ basicInfo.paymentChannelName }}</p>
                                <p>交易请求号 ：{{ basicInfo.execReqNo }}</p>
                                <p>借贷标记 ：{{ basicInfo.accountSide }}</p>
                            </div>
                            <div class="col">
                                <p>创建时间 ：{{ basicInfo.createTime | formatDate("yyyy-MM-dd HH:mm:ss")}}</p>
                                <p>状态变更时间 ：{{ basicInfo.modifiedTime | formatDate("yyyy-MM-dd HH:mm:ss")}}</p>
                                <p>凭证状态 ：{{ basicInfo.sourceDocumentStatus }}</p>
                            </div>
                            <div class="col">
                                <p>专户账号 ：{{ basicInfo.outlierCounterPartyAccount }}</p>
                                <p>来往机构 ：{{ basicInfo.bankName }}</p>
                                <p>机构账户名 ：{{ basicInfo.outlierAccountName }}</p>
                                <p>机构账户号 ：{{ basicInfo.outlierAccount }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        凭证业务明细
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="transferVoucherDetailShowModel"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="transferBillNo" label="五维转账单号">
                            </el-table-column>
                            <el-table-column prop="" label="付款方信息" inline-template>
                                <el-popover trigger="hover">
                                    <div>
                                        <div>账户名: {{ row.paymentAccountInfoModel.accountName }}</div>
                                        <div>账户号: {{ row.paymentAccountInfoModel.accountNo }}</div>
                                        <div>开户行: {{ row.paymentAccountInfoModel.bankName }}</div>
                                        <div>所在地: {{ row.paymentAccountInfoModel.province }}&nbsp;{{row.paymentAccountInfoModel.city}}</div>
                                        <div>证件号: {{ row.paymentAccountInfoModel.desensitizationIdCardNo }}</div>
                                        <div>恒生银行编码: {{ row.paymentAccountInfoModel.bankCode }}</div>
                                    </div>
                                    <span slot="reference">{{ row.paymentAccountInfoModel.accountName }}</span>
                                </el-popover>
                            </el-table-column>
                            <el-table-column prop="" label="收款方信息" inline-template>
                                <el-popover trigger="hover">
                                    <div>
                                        <div>账户名: {{ row.receivableAccountInfoModel.accountName }}</div>
                                        <div>账户号: {{ row.receivableAccountInfoModel.accountNo }}</div>
                                        <div>开户行: {{ row.receivableAccountInfoModel.bankName }}</div>
                                        <div>所在地: {{ row.receivableAccountInfoModel.province }}&nbsp;{{row.receivableAccountInfoModel.city}}</div>
                                        <div>证件号: {{ row.receivableAccountInfoModel.desensitizationIdCardNo }}</div>
                                        <div>恒生银行编码: {{ row.receivableAccountInfoModel.bankCode }}</div>
                                    </div>
                                    <span slot="reference">{{ row.receivableAccountInfoModel.accountName }}</span>
                                </el-popover>
                            </el-table-column>
                            <el-table-column prop="transferAmount" label="转账金额" inline-template>
                                <div>{{ row.transferAmount | formatMoney}}</div>
                            </el-table-column>
                            <el-table-column prop="createTime" label="创建时间" inline-template>
                                <div>{{ row.createTime| formatDate("yyyy-MM-dd HH:mm:ss")}}</div>
                            </el-table-column>
                            <el-table-column prop="modifiedTime" label="状态变更时间" inline-template>
                                <div>{{ row.modifiedTime | formatDate("yyyy-MM-dd HH:mm:ss")}}</div>
                            </el-table-column>
                            <el-table-column prop="status" label="执行状态">
                            </el-table-column>
                            <el-table-column prop="remark" label="备注">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block">
                    <h5 class="hd">
                        交易流水明细
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="transferVoucherCashFlowShowModel"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="bankSequenceNo" label="通道流水号">
                            </el-table-column>
                            <el-table-column prop="amount" label="流水金额" inline-template>
                                <div>{{ row.amount | formatMoney}}</div>
                            </el-table-column>
                            <el-table-column prop="accountSide" label="借贷标记">
                            </el-table-column>
                            <el-table-column prop="paymentChannelName" label="通道请求号">
                            </el-table-column>
                            <el-table-column prop="transactionTime" label="流水入账时间" inline-template>
                                <div>{{ row.transactionTime | formatDate("yyyy-MM-dd HH:mm:ss")}}</div>
                            </el-table-column>
                            <el-table-column prop="remark" label="备注">
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
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';

    export default {
        components: {
            SystemOperateLog
        },
        data: function() {
            return {
                fetching: false,
                basicInfo: {},
                transferVoucherDetailShowModel: [],
                transferVoucherCashFlowShowModel: []
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            fetch: function(uuid) {
                this.fetching = true;
                ajaxPromise({
                    url: `/audit/remittance/${uuid}/queryTransferVoucherDetail`
                })
                .then(data => {
                    this.basicInfo = Object.assign(this.basicInfo, data.transferVoucherBasicShowModel);
                    this.transferVoucherDetailShowModel = JSON.parse(data.transferVoucherDetailShowModel);
                    this.transferVoucherCashFlowShowModel = 
                        data.transferVoucherCashFlowShowModel 
                    ? [ data.transferVoucherCashFlowShowModel ] 
                    : [];
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