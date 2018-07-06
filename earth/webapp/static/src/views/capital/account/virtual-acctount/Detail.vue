<style lang="sass">
    #virtualAccountDetail {
        .el-col-7, .el-col-xs-7 {
            width: auto;
            margin-right: 10px;
        }
    }
</style>

<template>
    <div class="content" id="virtualAccountDetail">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb 
                :routes="[
                    { title: '账户详情'}
                ]">
                <el-button size="small" type="primary" @click="redirectCashflow">查看账户流水</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">账户信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>账户编号 ：{{ model.virtualAccountNo }}</p>
                                <p>账户名称 ：{{ model.virtualAccountName }}</p>
                                <p>客户类型 ：{{ model.customerTypeName }}</p>
                                <p style="display: inline-block">账户余额 ：
                                    <span class="color-danger">{{ model.balance | formatMoney }}</span>
                                    <!-- <el-button size="small" type="primary" class="hide">提现</el-button> -->
                                    <a href="javascript: void 0" style="cursor: pointer; color: #436ba7;" @click="withdrawDeposit">提现</a>
                                </p>
                                <p>账户状态 ：{{ model.virtualAccountStatusName }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">贷款信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>{{ $utils.locale('financialContract.name') }} ：{{ model.financialContractName }}</p>
                                <p>{{ $utils.locale('financialContract.no') }} ：<a :href="`${ctx}#/financial/contract/${model.financialContractUuid}/detail`">{{ model.financialContractNo }}</a></p>
                                <p>贷款合同编号 ：<a :href="`${ctx}#/data/contracts/detail?id=${model.contractId}`">{{ model.contractNo }}</a></p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">客户信息</h5>
                        <div class="bd">
                            <div class="col" v-if="model.customerType == 'PERSON'">
                                <p>姓名 ：{{ model.accountName  }}</p>
                                <p>编号 ：{{ model.number }}</p>
                                <p>身份证号 ：{{ model.desensitizationIdCardNo  }}</p>
                                <p>开户行 ：{{ model.bankName  }}</p>
                                <p>银行账户 ：{{ model.accountNo  }}</p>
                                <p>开户行所在地 ：{{ model.location  }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">
                        银行卡
                        <el-button type="text" style="padding: 0 10px; font-weight: normal;" @click="createBank" v-if="ifElementGranted('create-bank-account')">新增银行卡</el-button>
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="model.bankAccountAdapterList"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="accountNo" label="银行账户号">
                            </el-table-column>
                            <el-table-column prop="accountName" label="账户姓名">
                            </el-table-column>
                            <el-table-column prop="bankName" label="开户行">
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="开户行所在地">
                                <div>{{ row.province }} {{ row.city }}</div>
                            </el-table-column>
                            <el-table-column prop="desensitizationIdCardNo" label="身份证号码">
                            </el-table-column>
                            <el-table-column prop="bankCardStatusName" label="状态">
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="操作">
                                <div>
                                    <template v-if="!row.default">
                                        <a href="#" v-if="ifElementGranted('unbind-bank-account') && row.bankCardStatus === 'BINDING'" @click.prevent="unbindBank(row)">解绑</a>
                                        <a href="#" v-if="ifElementGranted('bind-bank-account') && row.bankCardStatus !== 'BINDING'" @click.prevent="bindBank(row)">绑定</a>
                                        <a href="#" v-if="ifElementGranted('modify-bank-account')" @click.prevent="editBank(row)">编辑</a>
                                    </template>
                                </div>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block">
                    <h5 class="hd">
                        提现记录
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="recordmodel"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="" label="提现单号">
                            </el-table-column>
                            <el-table-column prop="" label="银行账户号">
                            </el-table-column>
                            <el-table-column prop="" label="账户姓名">
                            </el-table-column>
                            <el-table-column prop="" label="开户行">
                            </el-table-column>
                            <el-table-column prop="" label="提现方式">
                            </el-table-column>
                            <el-table-column prop="" label="创建时间">
                            </el-table-column>
                            <el-table-column prop="" label="提现金额">
                            </el-table-column>
                            <el-table-column prop="" label="备注">
                            </el-table-column>
                            <el-table-column prop="" label="状态">
                            </el-table-column>
                        </el-table>
                    </div>
                </div>

                <div class="block">
                    <SystemOperateLog 
                        ref="sysLog"
                        :for-object-uuid="$route.params.id"></SystemOperateLog>
                </div>
            </div>
        </div>
        
        <EditBankModal 
            :virtual-account-uuid="$route.params.id"
            :contract-id="model.contractId"
            :financial-contract-uuid="model.financialContractUuid"
            :bank-account-adapter-list="model.bankAccountAdapterList"
            v-model="bankModal.visible" 
            @submit="submitBank"
            :model="bankModal.model"></EditBankModal>
    </div>
</template>

<script>
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import { mapState } from 'vuex';
    import EditBankModal from './EditBankModal';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            SystemOperateLog,
            EditBankModal,
        },
        data: function() {
            return {
                fetching: false,

                bankModal: {
                    visible: false,
                    isUpdate: false,
                    model: {}
                },

                model: {
                    bankAccountAdapterList: []
                },

                recordmodel: [],
            }
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            bindBank: function(bank) {
                var htm = `
                    <div style="margin-top: 25px;">你确定绑定<span class="color-danger">${ bank.accountName }，${ bank.accountNo }</span></div>
                    <div><span class="color-danger">${ bank.accountNo }</span>的卡吗？</div>
                `;

                MessageBox.open(htm, '提示', [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        MessageBox.close();
                        
                        ajaxPromise({
                            url: '/capital/customer-account-manage/virtual-account-list/bank_card_binding',
                            data: {
                                uuid: bank.uuid,
                                virtualAccountUuid: this.$route.params.id
                            }
                        }).then(data => {
                            MessageBox.open('绑定成功');
                            var index = this.model.bankAccountAdapterList.findIndex(item => item.uuid === bank.uuid);
                            Object.assign(this.model.bankAccountAdapterList[index], data.data);
                            this.$refs.sysLog.fetch();
                        }).catch(message => {
                            MessageBox.open(message);
                        });    
                    }
                }]);
            },
            unbindBank: function(bank) {
                var htm = `
                    <div class="color-danger" style="margin-top: 25px;">${ bank.accountName } ${ bank.bankName } ${ bank.accountNo }</div>
                    <div>你确定解绑此卡？</div>
                `;

                MessageBox.open(htm, '提示', [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        MessageBox.close();

                        ajaxPromise({
                            url: '/capital/customer-account-manage/virtual-account-list/bank_card_tiedUp',
                            data: {
                                uuid: bank.uuid,
                                virtualAccountUuid: this.$route.params.id
                            }
                        }).then(data => {
                            MessageBox.open('解绑成功');
                            var index = this.model.bankAccountAdapterList.findIndex(item => item.uuid === bank.uuid);
                            Object.assign(this.model.bankAccountAdapterList[index], data.data);
                            this.$refs.sysLog.fetch();
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }]);
            },
            fetch: function(virtualAccountUuid) {

                this.fetching = true;

                ajaxPromise({
                    url: `/capital/customer-account-manage/virtual-account-list/detail-data`,
                    data: {
                        virtualAccountUuid
                    }
                }).then(data => {
                    this.model = Object.assign({}, this.model, data.accountDepositModel);
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
            createBank: function() {
                var { bankModal } = this;
                bankModal.model = {};
                bankModal.isUpdate = false;
                bankModal.visible = true;
            },
            editBank: function(bank) {
                var { bankModal } = this;
                bankModal.model = Object.assign({}, bank);
                bankModal.isUpdate = true;
                bankModal.visible = true;
            },
            submitBank: function(data) {
                if (this.bankModal.isUpdate) {
                    var index = this.model.bankAccountAdapterList.findIndex(item => item.uuid === data.uuid);
                    Object.assign(this.model.bankAccountAdapterList[index], data);
                } else {
                    this.model.bankAccountAdapterList.push(data);
                }
                this.$refs.sysLog.fetch();
                this.fetch(this.$route.params.id);
            },
            redirectCashflow: function() {
                var { financialContractUuid, virtualAccountNo } = this.model;
                var search = encodeURI(`financialContractUuids=["${financialContractUuid}"]&key=${virtualAccountNo}`);
                var href = `${this.ctx}#/capital/account/account-flow?${search}&isRedirect=true`;
                location.assign(href);
            },
            withdrawDeposit: function(){

            }
        }
    }
</script>