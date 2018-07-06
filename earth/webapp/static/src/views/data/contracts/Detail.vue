<style lang="sass">
    #contractDetail {
        .select-version {
            display: inline-block;
            border-radius: 0;
            width: 215px;
            height: 28px;
            padding: 0;
            margin: 0 10px;
        }

        .datetimepicker-trigger {
            .calendar {
                line-height: 34px;
            }
        }
    }
    .attachment {
        padding: 0!important;
        margin-top: 8px!important;
        border-radius: 0px!important;
        ul {
            list-style: none;
            margin: 0;
            padding: 0;
            width: 280px;
            max-height: 200px;
            overflow-y: auto;
            li {
                padding: 0 15px;
                line-height: 50px;
                border-bottom: 1px solid #f5f5f5;
                font-size: 12px;
                &:hover {
                    background-color: #f5f5f5;
                }
            }
        }
    }
</style>

<template>
    <div class="content" id="contractDetail">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '贷款合同详情'}]">
                <template>
                    <el-popover
                        popper-class="attachment"
                        trigger="click"
                        placement="bottom">
                        <ul v-if="fileList.length">
                            <li v-for="item in fileList">
                                <span><img src="./include/attachment-pdf.png"></span>
                                <span style="margin-left: 2px;">{{ item.fileName }}</span>
                                <span class="pull-right" @click="deleteAttachedFile(item)" style="margin-left: 10px;cursor: pointer;">
                                    <img src="./include/attachment-delete.png">
                                </span>
                                <span class="pull-right" @click="downloadAttachedFile(item)" style="cursor: pointer;">
                                    <img src="./include/attachment-download.png">
                                </span>
                            </li>
                        </ul>
                        <div v-else style="line-height: 50px; text-align: center">暂无附件</div>
                        <el-button size="small" type="primary" slot="reference">附件</el-button>
                    </el-popover>
                    <el-button size="small" type="primary" v-if="ifElementGranted('pre-repayment') && currentModel.canPrepayment" @click="prepayModal.visible = true">提前还款</el-button>
                    <el-button size="small" type
                    ="primary" v-if="ifElementGranted('invalidate-assets-contract') && currentModel.contract && currentModel.contract.state == 'EFFECTIVE' && currentModel.canInvalidate" @click="terminateModal.visible = true">关闭合同</el-button>
                    <el-button size="small" type="primary" v-if="ifElementGranted('repurchase-assets-contract') && currentModel.contract && currentModel.contract.state == 'EFFECTIVE'" @click="repurchaseModal.visible = true">回购</el-button>
                    <a 
                        class="el-button el-button--primary el-button--small" 
                        v-if="currentModel.contract && currentModel.contract.state == 'EFFECTIVE'"
                        :href="`${ctx}#/capital/voucher/active/create?contractNo=${currentModel.contract.contractNo}`">
                        主动付款
                    </a>
                </template>
            </Breadcrumb>

            <div class="col-layout-detail">
                <div class="top">
                    <div class="block">
                        <h5 class="hd">贷款合同信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>合同编号 : {{ currentModel.contract.contractNo }}</p>
                                <p>贷款方式 : {{ currentModel.contract.repaymentWayMsg }}</p>
                                <p>贷款利率 : 
                                    <span v-if="currentModel.contract.interestRate || currentModel.contract.interestRate === 0">{{ currentModel.contract.interestRate | formatPercent }}</span>
                                </p>
                                <p>利率周期 : {{ currentModel.contract.interestRateCycle | formatInterestRateCycle}}</p>
                                <p>罚息利率 : 
                                    <span v-if="currentModel.contract.penaltyInterest || currentModel.contract.penaltyInterest === 0">{{ currentModel.contract.penaltyInterest | formatPercent}}</span>
                                </p>
                                <p>合同状态 : 
                                    <span :class="{
                                        'color-danger': ['DEFAULT', 'INVALIDATE'].includes(currentModel.contract.state),
                                        'color-warning': ['REPURCHASING'].includes(currentModel.contract.state) }">
                                        {{ currentModel.contract.stateMsg }}
                                    </span>
                                    <template v-if="currentModel.contract.state == 'INVALIDATE'">
                                        <a href="#" @click.prevent="downloadFile(contractId)" v-if="hasFiles">下载附件</a>
                                        <span style="color:#999" v-else>没有上传附件</span>
                                    </template>
                                </p>
                                <p>导入批次号 : <a :href="redirectLoanBatch()">
                                {{ currentModel.loanBatchCode }}</a></p>
                                <p>贷款客户姓名 : {{ customer.name}}</p>
                                <p>贷款客户编号 : {{ customer.source }}</p>
                                <p>贷款客户身份证 : {{ customer.desensitizationAccount }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">资产信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>资产编号 : {{ house.address }}</p>
                                <p>生效日期 : {{ currentModel.contract.beginDate | formatDate('yyyy/MM/dd') }}</p>
                                <p>截止日期 : {{ currentModel.contract.endDate | formatDate('yyyy/MM/dd') }}</p>
                                <p>还款期数 : {{ currentModel.contract.periods }}</p>
                                <p>本金总额 : {{ currentModel.contract.totalAmount | formatMoney }}</p>
                                <p>回购状态 : {{ repurchaseState }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">项目信息</h5>
                        <div class="bd">
                            <div class="col">
                                <p>{{ $utils.locale('financialContract.no') }} : 
                                <a :href="`${ctx}#/financial/contract/${currentModel.contract.financialContractUuid}/detail`">
                                {{ currentModel.financialContract.contractNo }}</p>
                                <p>资产方简称 : {{ app.name }}</p>
                                <p>商户代码 : {{ app.appId }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block">
                        <h5 class="hd">还款账户信息
                            <a  href="#" 
                                v-if="ifElementGranted('modify-client-info') && !['REPURCHASING', 'REPURCHASED', 'DEFAULT'].includes(currentModel.contract.state)"
                                @click.prevent="modifyCustomerInfo()">
                                [编辑]</a>
                        </h5>
                        <div class="bd">
                            <div class="col">
                                <p>还款方账户名 : {{ currentModel.contractAccount.payerName}}
                                    <a href="javascript: void(0)" :loading="posting" v-if="!isYunXin" @click.prevent="signUp">{{isNeedReSignUp ? '[ 重新签约 ]' : '[ 解除签约 ]'}}</a>
                                </p>
                                <p v-if="!isYunXin">手机号 : {{ currentModel.mobile }}</p>
                                <p>银行账户号 : {{ currentModel.contractAccount.payAcNo}}</p>
                                <p>账户开户行 : {{ currentModel.contractAccount.bank }}</p>
                                <p>开户行所在地 : {{ currentModel.contractAccount.province }}&nbsp; {{ currentModel.contractAccount.city }}</p>
                            </div>
                        </div>
                    </div>
              </div>
            </div>

            <div class="table-layout-detail" >
                <div class="block">
                    <h5 class="hd">合同状态：</h5>
                    <div class="bd">
                        <table>
                            <thead>
                                <tr>
                                    <th width="50%">还款总额:
                                        <span>
                                        {{ currentModel.fields.returnedPrincipalAndInterest + currentModel.fields.planningPrincipalAndInterest | formatMoney }}
                                        &nbsp;&nbsp;&nbsp;
                                        共({{ currentModel.fields.returnedPeriod + currentModel.fields.planningPeriod }})期</span>
                                    </th>
                                    <th width="50%">放款总额:
                                        <span>{{ currentModel.raTotalAmount | formatMoney }}</span>
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td height="200px">
                                        <div class="progress-status">
                                            <div>
                                                <span class="color-success">已还：
                                                  {{ currentModel.fields.returnedPrincipalAndInterest | formatMoney }} |
                                                </span>
                                                <span>未还：
                                                   {{ currentModel.fields.planningPrincipalAndInterest | formatMoney }}
                                                </span>
                                            </div>
                                            <div class="progress">
                                                <div class="progress-bar" role="progressbar" v-bind:style="{width: percentPrincipalAndInterest+'%' }">
                                                    <span class="sr-only">{{ percentPrincipalAndInterest }}%</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="progress-status">
                                            <div>
                                                <span class="color-success">已还：{{ currentModel.fields.returnedPeriod }} 期</span> |
                                                <span >未还：{{ currentModel.fields.planningPeriod }} 期</span>
                                            </div>
                                            <div class="progress">
                                                <div class="progress-bar" role="progressbar" v-bind:style="{width: percentPeriod + '%' }">
                                                    <span class="sr-only">{{ percentPeriod }}%</span>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="progress-status">
                                            <div>
                                                <span class="color-success">
                                                    已放：
                                                    {{ currentModel.raPaidAmount | formatMoney}}
                                                </span> |
                                                <span >
                                                    未放：
                                                    {{ currentModel.raNotPaidAmount | formatMoney}}
                                                </span>
                                            </div>
                                            <div class="progress">
                                                <div class="progress-bar" role="progressbar" v-bind:style="{width: percentRaPaidAmount + '%'}">
                                                    <span class="sr-only">
                                                        {{ percentRaPaidAmount }}%
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="progress-status" style="visibility: hidden;">
                                            <div>
                                                <span class="color-success">已放：0</span> |
                                                <span >未放：0</span>
                                            </div>
                                            <div class="progress">
                                              <div class="progress-bar" role="progressbar">
                                                <span class="sr-only">0%</span>
                                              </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <TabMenu v-model="tabSelected">
                        <TabMenuItem id="repay">还款</TabMenuItem>
                        <TabMenuItem id="release">放款</TabMenuItem>
                    </TabMenu>
                    <TabContent v-model="tabSelected">
                        <TabContentItem id="repay">
                            <RepayTabContentItem
                                :selected="tabSelected === 'repay'"
                                :currentModel="currentModel"
                                :defaultVersion="defaultVersion"
                                :contractId="contractId"
                                :repaymentHistorysFetching="repaymentHistorysFetching"
                                :repaymentHistorys="repaymentHistorys"
                                @signUpReference="getSignUpReference">
                            </RepayTabContentItem>
                        </TabContentItem>
                        <TabContentItem id="release">
                            <ReleaseTabContentItem
                                :selected="tabSelected === 'release'"
                                :remittanceApplications="currentModel.remittanceApplications">
                            </ReleaseTabContentItem>
                        </TabContentItem>
                    </TabContent>
                </div>
                <div class="block">
                    <SystemOperateLog 
                        ref="sysLog"
                        :for-object-uuid="currentModel.contract.uuid"></SystemOperateLog>
                </div>
            </div>
        </div>

        <EditCustomerInfoModal
            v-model="customerInfoModal.visible"
            :isYunXin="isYunXin"
            :model="customerInfoModal.model"
            :contractId="contractId"
            :coreBanks="currentModel.coreBanks"
            @submit="fetch">
        </EditCustomerInfoModal>

        <EditRepurchaseModal 
            v-model="repurchaseModal.visible"
            @submit="fetch"
            :contractNo="currentModel.contract.contractNo"
            :contractId="contractId">
        </EditRepurchaseModal>

        <EditPrepayModal
            v-model="prepayModal.visible"
            @submit="fetch"
            :contractId="contractId">
        </EditPrepayModal>

        <EditTerminateModal 
            v-model="terminateModal.visible"
            @submit="fetch"
            :contractId="contractId"
            :customer="customer"
            :contract="currentModel.contract"
            :contractAccount="currentModel.contractAccount">
        </EditTerminateModal>
    </div>
</template>

<script>
    import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
    import format from 'filters/';

    export default{
        components:{
            SystemOperateLog,TabMenu,TabMenuItem, TabContent, TabContentItem,
            EditRepurchaseModal: require('./include/EditRepurchaseModal'),
            EditPrepayModal: require('./include/EditPrepayModal'),
            EditTerminateModal: require('./include/EditTerminateModal'),
            RepayTabContentItem: require('./include/RepayTabContentItem'),
            ReleaseTabContentItem: require('./include/ReleaseTabContentItem'),
            EditCustomerInfoModal: require('./include/EditCustomerInfoModal'),
        },
        data: function() {
            return {
                fetching: false,
                tabSelected: 'repay',
                defaultVersion: '',
                hasFiles: true,

                repaymentHistorys: [],
                currentModel: {
                    loanBatchCode: '',
                    contract: {},
                    contractAccount: {},
                    financialContract: {},
                    repurchaseDoc: [],
                    fields: {},
                    coreBanks: {},
                    remittanceApplications: [],
                    raTotalAmount: '',
                    raPaidAmount: '',
                    raNotPaidAmount: '',
                    total: '',
                    allVersion: [],
                    modifyReason: '',
                    canInvalidate: false,
                    mobile: '',
                },  

                customerInfoModal: {
                    model:{
                        customerSource: '',
                        customer: '',
                        idCardNum: '',
                        bankAccount: '',
                        bankCode: '',
                        provinceCode: '',
                        cityCode: '',
                        mobile: '',
                    },
                    visible: false,
                },
                repurchaseModal: {
                    visible: false,
                },
                prepayModal: {
                    visible: false
                },
                terminateModal: {
                    visible: false
                },

                //重新解约相关
                posting: false,
                isYunXin: true,
                isNeedReSignUp: true,
                opType: '1',

                fileList: [],

                repaymentHistorysFetching: false,
            }
        },
        computed: {
            house: function() {
                var contract = this.currentModel.contract;
                return contract.house ? contract.house : {};
            },
            app: function() {
                var contract = this.currentModel.contract;
                return contract.app ? contract.app : {};
            },
            customer: function() {
                var contract = this.currentModel.contract;
                return contract.customer ? contract.customer : {};
            },
            contractId: function() {
                return this.currentModel.contract.id;  
            },
            repurchaseState: function() {
                var states = ['PRE_PROCESS','INEFFECTIVE','EFFECTIVE','INVALIDATE','COMPLETION'];
                return states.includes(this.currentModel.contract.state) ? '未触发' : this.currentModel.contract.stateMsg;
            },
            percentPrincipalAndInterest: function() {
                var sumPrincipalAndInterest = this.currentModel.fields.returnedPrincipalAndInterest + this.currentModel.fields.planningPrincipalAndInterest;
                var value = 0;
                if (sumPrincipalAndInterest != 0) {
                    value = 100 * this.currentModel.fields.returnedPrincipalAndInterest / (this.currentModel.fields.returnedPrincipalAndInterest + this.currentModel.fields.planningPrincipalAndInterest);
                }
                return value;
            },
            percentPeriod: function() {
                var sumPeriod = this.currentModel.fields.returnedPeriod + this.currentModel.fields.planningPeriod;
                var value = 0;
                if (sumPeriod != 0) {
                    value = 100 * this.currentModel.fields.returnedPeriod / (this.currentModel.fields.returnedPeriod + this.currentModel.fields.planningPeriod);
                }
                return value;
            },
            percentRaPaidAmount: function() {
                var sumRaPaidAmount = this.currentModel.raPaidAmount + this.currentModel.raNotPaidAmount;
                var value = 0;
                if (sumRaPaidAmount != 0) {
                    value = 100 * this.currentModel.raPaidAmount / (this.currentModel.raPaidAmount + this.currentModel.raNotPaidAmount)
                }
                return value;
            },
        },
        filters: {
            formatInterestRateCycle: function(value) {
                var types = {
                    0: '年化',
                    1: '月化',
                    2: '日化'
                };
                return types[value];
            },
        },
        activated: function() {
            this.tabSelected = 'repay';
            this.hasFiles = true;
            this.fetch();
        },
        deactivated: function(){
            this.currentModel = Object.assign({},{
                loanBatchCode: '',
                contract: {},
                contractAccount: {},
                financialContract: {},
                repurchaseDoc: [],
                fields: {},
                coreBanks: {},
                remittanceApplications: [],
                raTotalAmount: '',
                raPaidAmount: '',
                raNotPaidAmount: '',
                total: '',
                allVersion: [],
                modifyReason: '',
                canInvalidate: false
            });
            this.defaultVersion = '';
            this.repaymentHistorys = [];
        },
        methods: {
            fetch: function() {
                this.fetching = true;
                const { id, uid, uuid } = this.$route.query;
                ajaxPromise({
                    url: id ? `/contracts/getDetailData?id=${id}` : uuid ? `/contracts/getDetailData?uuid=${uuid}` : `/contracts/getDetailData?uid=${uid}`
                }).then(data => {

                    this.currentModel = Object.assign({
                        loanBatchCode: '',
                        contract: {},
                        contractAccount: {},
                        financialContract: {},
                        repurchaseDoc: [],
                        fields: {},
                        coreBanks: {},
                        remittanceApplications: [],
                        raTotalAmount: '',
                        raPaidAmount: '',
                        raNotPaidAmount: '',
                        total: '',
                        allVersion: [],
                        modifyReason: '',
                        canInvalidate: false,
                        mobile: ''
                    }, data);
                    var { currentModel, defaultVersion, versionIndex } = this;

                    var length = currentModel.allVersion.length;
                    currentModel.allVersion.forEach(item => {
                        var index =  versionIndex(item.versionNo);
                        var formatLabel = '版本' + (length-index) +' ' + format.formatDate(item.createTime, 'yyyy/MM/dd HH:mm:ss');
                        item.formatLabel = formatLabel;
                    });

                    this.defaultVersion = currentModel.contract.activeVersionNo;
                    this.contractId && this.fetchRepaymentHistorys(this.contractId);

                    if (data.contract != null && data.contract.uniqueId != "") {
                      this.getFileList(data.contract.uniqueId);
                    }
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });
            },
            fetchRepaymentHistorys: function(id) {
                this.repaymentHistorysFetching = true;
                ajaxPromise({
                    url: `/contracts/detail/repaymentRecordDetail/${id}`,
                }).then(data => {
                    this.repaymentHistorys = data.list;
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.repaymentHistorysFetching = false;
                })
            },
            getFileList: function(uniqueId) {
                ajaxPromise({
                    url: `/contracts/appendices?contractUniqueId=${uniqueId}`,
                }).then(data => {
                    this.fileList = data.list;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            downloadAttachedFile(item) {
                downloadFile(`/contracts/appendices/${item.fileKey}`);
            },
            deleteAttachedFile(item) {
                var htm = `<div style="margin-top: 25px;">确定删除附件<span class="color-danger">${item.fileName}</span>吗？</div>`;
                MessageBox.open(htm, '提示', [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        MessageBox.close();

                        ajaxPromise({
                            url: `/contracts/appendices/${item.fileKey}`,
                            type: 'delete'
                        }).then(data => {
                            this.fileList = this.fileList.filter(cur => cur.fileKey != item.fileKey);
                            MessageBox.open("删除成功");
                        }).catch(message => {
                            MessageBox.open(message);
                        })
                    }
                }]);
            },
            versionIndex: function(cur) {
                var index = this.currentModel.allVersion.findIndex(item => item.versionNo == cur);
                return index != -1 ? index : 0;
            },
            modifyCustomerInfo: function() {
                var { currentModel } = this;
                this.customerInfoModal.model = {
                    payerName : currentModel.contractAccount.payerName,
                    bankCode : currentModel.contractAccount.standardBankCode,
                    bankAccount : currentModel.contractAccount.payAcNo,
                    provinceCode : currentModel.contractAccount.provinceCode,
                    cityCode : currentModel.contractAccount.cityCode,
                    idCardNum: currentModel.contractAccount.idCardNum,
                    mobile: currentModel.mobile
                };
                this.customerInfoModal.visible = true;  
            },
            downloadFile: function(contractId) {
                ajaxPromise({
                    url: `/contracts/download`,
                    data: { contractId },
                }).then(data => {
                    this.hasFiles = true;
                    window.location.href = this.api+ '/contracts/download?contractId=' + contractId;
                }).catch(message => {
                    this.hasFiles = false;
                });
            },
            redirectLoanBatch: function() {
                var loanBatchCode = this.currentModel.loanBatchCode;
                var loanBatchCodeStr = encodeURI(loanBatchCode);
                var financialContractUuids = [this.currentModel.financialContract.financialContractUuid];
                var financialContractUuidsStr = encodeURI(JSON.stringify(financialContractUuids))
                var url = `${this.ctx}#/data/loan-batch?loanBatchCode=${loanBatchCodeStr}&financialContractUuids=${financialContractUuidsStr}&isRedirect="true"`;
                return url;
            },
            getSignUpReference: function(ref){
                this.isYunXin = ref.isYunXin;
                this.isNeedReSignUp = ref.isNeedReSignUp;
                this.opType = ref.opType;
            },
            signUp: function(){
                var str = this.isNeedReSignUp ? '重新签约' : '解除签约';
                var opType = this.isNeedReSignUp ? '1' : '2'
                MessageBox.open(`是否确定${str}此银行卡？`, null, [{
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        this.posting = true
                        ajaxPromise({
                            url:`/contracts/signUp`,
                            data: {
                                contractId: this.contractId,
                                opType: opType
                            },
                        }).then(data =>{
                            this.posting = false
                            this.isNeedReSignUp = !this.isNeedReSignUp
                            MessageBox.open(data.message)
                            this.fetch()
                        }).catch(message => {
                            MessageBox.open(message);
                        }).then(() => {
                            this.posting = false;
                        })
                    }
                },{
                    text: '取消',
                    type: 'default',
                    handler: () => MessageBox.close()
                }])
            },
        }
    }
</script>
