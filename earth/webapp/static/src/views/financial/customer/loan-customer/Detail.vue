<style lang="sass">
    .hd{
        color: #333;
        margin-bottom: 18px;
        font-weight: bold;
    }
    .border-block {
        .bd{
            border: 1px solid #e0e0e0;
            padding: 20px;
            margin-bottom: 40px;
        }
    }
    .bankCards {
        &:before, &:after {
            content: '';
            display: table;
            clear: both;
        }
        .sketch-item {
            width: 300px;
            height: 120px;
            float: left;
            margin-right: 20px;
            padding: 20px;
            .bankNum {
                display: inline-block;
                max-width: 70%;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
            }
            p:last-child {
                line-height:  50px;
            }
        }
    }
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{title: '贷款客户管理'}, {title: '贷款客户信息详情'} ]">
                <el-button v-if="!isEdit" size="small" type="primary" @click="handleEdit">编辑</el-button>
                <template v-else>
                    <el-button size="small" @click="handleCancelEdit">取消</el-button>
                    <el-button size="small" type="primary" @click="handleSubmitEdit">保存</el-button>
                </template>
            </Breadcrumb>
            <div class="row-layout-detail">
                <div class="border-block">
                    <h5 class="hd" v-if="isPersonal">个人客户信息</h5>
                    <PersonalInfo
                        v-if="isPersonal"
                        ref="personInfo"
                        :isEdit="isEdit"
                        :options="editOptions"
                        :customer="customer"
                        :customerPerson="customerPerson"
                        @submit="submit">
                    </PersonalInfo>
                    <h5 class="hd" v-if="!isPersonal">企业客户信息</h5>
                    <CompanyInfo
                        v-if="!isPersonal"
                        ref="enterpriseInfo"
                        :isEdit="isEdit"
                        :options="editOptions"
                        :customer="customer"
                        :customerEnterprise="customerEnterprise"
                        @submit="submit">
                    </CompanyInfo>
                </div>
                <div class="border-block">
                    <h5 class="hd">银行信息</h5>
                    <div class="bd bankCards">
                        <SketchItem v-for="item in bankCards">
                            <div class="text">
                                <p>{{item.bankName}}</p>
                                <p><span class="bankNum">{{item.accountNo}}</span> <span class="pull-right" style="color:#333">{{ item.city }}</span></p>
                            </div>
                            <span class="operate">
                                <a class="edit" @click="onEditBankCard(item)"></a>
                                <a class="delete" @click="onDeleteBankCard(item)"></a>
                            </span>
                        </SketchItem>

                        <SketchItem @click.native="onAddBankCard">
                            <div style="text-align: center;line-height: 70px;cursor: pointer;">+ 添加银行卡</div>
                        </SketchItem>
                    </div>
                </div>

                <div class="block border-block">
                    <h5 class="hd" v-if="isPersonal">客户贷款信息</h5>
                    <h5 class="hd" v-else>企业贷款信息</h5>
                    <CustomerLoan :loandetail="customerLoanDetail" v-loading="relatedContractFetching"></CustomerLoan>
                </div>

                <div class="block">
                    <h5 class="hd" v-if="isPersonal">贷款合同相关信息</h5>
                    <h5 class="hd" v-else>相关贷款合同列表</h5>
                    <PagingTable
                        :data="loanBatchDetail"
                        :loading="relatedContractFetching"
                        :pagination="true">
                        <el-table-column label="贷款合同编号" prop="contractNo" inline-template>
                            <a :href="`${ctx}#/data/contracts/detail?id=${row.id}`">{{ row.contractNo }}</a>
                        </el-table-column>
                        <el-table-column label="信托合同名称" prop="financialContractName"></el-table-column>
                        <el-table-column label="贷款利率" inline-template><div>{{ row.interestRate | formatPercent }}</div></el-table-column>
                        <el-table-column label="生效日期" inline-template><div>{{ row.beginDate | formatDate }}</div></el-table-column>
                        <el-table-column label="截止日期" inline-template><div>{{ row.endDate | formatDate }}</div></el-table-column>
                        <el-table-column label="贷款方式" prop="repaymentWay"></el-table-column>
                        <el-table-column label="期数" prop="periods"></el-table-column>
                        <el-table-column label="还款周期" prop="paymentFrequency"></el-table-column>
                        <el-table-column label="客户姓名" prop="customerName"></el-table-column>
                        <el-table-column label="贷款总额" inline-template><div>{{ row.totalAmount | formatMoney }}</div></el-table-column>
                        <el-table-column label="合同状态" prop="stateStr"></el-table-column>
                        <el-table-column label="账户编号" prop="virtualAccountNo" inline-template>
                            <a :href="`${ctx}#/capital/account/virtual-acctount/${row.virtualAccountUuid}/detail`">{{ row.virtualAccountNo }}</a>
                        </el-table-column>
                        <el-table-column v-if="isPersonal" label="对手方编号" prop="customerNo"></el-table-column>
                        <el-table-column v-if="!isPersonal" label="资产方客户编号" prop="customerNo"></el-table-column>
                    </PagingTable>
                </div>

                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="customer.customerUuid"></SystemOperateLog>
                </div>
            </div>

            <EditBankCardModal
                :model="editBankCardModal.model"
                :openType="editBankCardModal.openType"
                v-model="editBankCardModal.show"
                @submit="onSubmitTrustAccount">
            </EditBankCardModal>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise, purify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            PersonalInfo: require('./include/PersonalInfo'),
            CompanyInfo: require('./include/CompanyInfo'),
            CustomerLoan: require('./include/CustomerLoan'),
            EditBankCardModal: require('./include/EditBankCardModal'),
            PagingTable: require('views/include/PagingTable'),
            SystemOperateLog: require('views/include/SystemOperateLog'),
            SketchItem: require('components/SketchItem')
        },
        data: function() {
            return {
                isPersonal: true,
                customerUuid: '',
                fetching: false,
                isEdit: false,
                relatedContractFetching: false,

                customer: {},
                customerPerson: {},
                customerEnterprise: {},
                bankCards: [],
                editOptions: {},

                customerLoanDetail: {},
                loanBatchDetail: [],

                editBankCardModal: {
                    show: false,
                    openType: '',
                    model: {},
                }
            }
        },
        activated: function() {
            this.isPersonal = this.$route.query.customerStyle == '个人';
            this.customerUuid = this.$route.params.customerUuid;
            this.isEdit = false;
            this.fetchOptions();
            this.fetchDetail();
            this.fetchRelatedContract();
        },
        computed: {
            customerStyle: function() {
                return this.isPersonal ? 'person' : 'enterprise';
            }
        },
        watch: {
            // isEdit: function(current) {
            //     if (!current) {
            //         this.isPersonal ? this.$refs.personInfo && this.$refs.personInfo.resetFields() : this.$refs.enterpriseInfo && this.$refs.enterpriseInfo.resetFields();
            //     }

            // }
        },
        methods: {
            fetchDetail: function() {
                this.fetching = true;
                var customerUuid = this.$route.params.customerUuid;
                //获取详情页信息
                ajaxPromise({
                    url: `/customer/${this.customerStyle}/detail?customerUuid=${customerUuid}`
                }).then(data => {
                    this.customer = data.customer || {};
                    this.customerPerson = data.customerPerson || {};
                    this.customerEnterprise = data.customerEnterprise || {};
                    this.bankCards = data.bankCards || [];
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            fetchOptions: function() {
                //获取编辑下拉框选项
                ajaxPromise({
                    url: `/customer/optionData/${this.customerStyle}?customerUuid=${this.customerUuid}`
                }).then(data => {
                    this.editOptions = data || {};
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            fetchRelatedContract: function() {
                 //相关贷款合同列表
                this.relatedContractFetching = true;
                ajaxPromise({
                    url: `/customer/relatedContract?customerUuid=${this.customerUuid}`
                }).then(data => {
                    this.loanBatchDetail = data.list || [];
                    this.customerLoanDetail = Object.assign({}, data);
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.relatedContractFetching = false;
                })
            },
            onAddBankCard: function() {
                this.editBankCardModal.show = true;
                this.editBankCardModal.openType = 'add';
                this.editBankCardModal.model = {
                    accountName: this.customer.name
                };
            },
            onEditBankCard: function(item) {
                this.editBankCardModal.show = true;
                this.editBankCardModal.openType = 'edit';
                this.editBankCardModal.model = Object.assign({}, item);
            },
            onDeleteBankCard: function(elem) {
                if (this.bankCards.length == 1) {
                    MessageBox.open('银行卡只剩一张，不可删除');
                    return
                }

                MessageBox.open('确认删除该银行信息', '提示', [{
                    text: '确认',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/bankCard/delete`,
                            data: {
                                uuid: elem.uuid
                            }
                        }).then(data => {
                            MessageBox.once('closed', () => this.refreshDetail());
                            MessageBox.open('操作成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }, {
                    text: '关闭',
                    handler: () => MessageBox.close()
                }])
            },
            onSubmitTrustAccount: function(model) {
                ajaxPromise({
                    url: this.editBankCardModal.openType == 'add' ? `/bankCard/add` : `/bankCard/modify`,
                    data: Object.assign({
                            outerIdentifier: this.customerUuid,
                            identityOrdinal: 1
                        }, model)
                }).then(data => {
                    MessageBox.once('closed', () => {
                        this.editBankCardModal.show = false;
                        this.refreshDetail();
                    });
                    MessageBox.open("操作成功");
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            handleEdit: function() {
                this.isEdit = !this.isEdit;
            },
            handleCancelEdit: function() {
                this.isEdit = false;
            },
            handleSubmitEdit: function() {
                var customerInfo = this.$refs[this.customerStyle + 'Info'];
                customerInfo.validate();
            },
            submit: function(model) {
                ajaxPromise({
                    url: `/customer/${this.customerStyle}/modify`,
                    type: 'post',
                    data: purify(model)
                }).then(data => {
                    MessageBox.once('closed', () => {
                        this.isEdit = false;
                        this.refreshDetail();
                    });
                    MessageBox.open("保存成功");
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            refreshDetail: function() {
                this.fetchDetail();
                this.fetchRelatedContract();
                this.$refs.sysLog.fetch();
            }
        }
    }
</script>