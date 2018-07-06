<style lang="sass">
    #financialContractEdit {
        .step-tip-item{
            width: 22%;
        }
        .muted {
            margin: 0 8px;
        }
        
        .prev {
            margin-left: 10px;
            cursor: pointer;
            margin-left: 10px;
        }

        .subform {
            border: 1px solid #e0e0e0;
            border-radius: 4px;
        }
    }
</style>

<template>
    <div class="content" id="financialContractEdit">
        <div class="scroller">
            <Breadcrumb :routes="[{ title: $utils.locale('financialContract') }, {title: '新增合同'}]"></Breadcrumb>

            <div style="width: 80%; margin: 30px auto;">
                <StepTip v-model="stepIndex" align="center">
                    <StepTipItem :index="1">填写合同基础信息</StepTipItem>
                    <StepTipItem :index="2">填写信托专户信息</StepTipItem>
                    <StepTipItem :index="3">填写放款信息</StepTipItem>
                    <StepTipItem :index="4">填写还款信息</StepTipItem>
                </StepTip>
                <StepContent v-model="stepIndex">
                    <StepContentItem :index="1">
                        <EditBasicInfo @next="doneStep1" :basicInfo="basicInfo"></EditBasicInfo>
                    </StepContentItem>
                    <StepContentItem :index="2">
                        <EditSpecialAccountInfo @next="doneStep2" @prev="backStep" 
                        :specialAccountInfo="specialAccountInfo" 
                        :specialAccountType="specialAccountType"
                        :specialAccountSwitchOn="specialAccountSwitchOn"
                        @specialAccountSwitch="specialAccountSwitch"></EditSpecialAccountInfo>
                    </StepContentItem>
                    <StepContentItem :index="3">
                        <EditRemittanceInfo @next="doneStep3" @prev="backStep"  :remittanceInfo="remittanceInfo"></EditRemittanceInfo>
                    </StepContentItem>
                    <StepContentItem :index="4">
                        <EditRepayInfo @next="doneStep4" @prev="backStep"  :repayInfo="repayInfo"></EditRepayInfo>
                    </StepContentItem>
                </StepContent>
            </div>
        </div>
    </div>
</template>

<script>
    import { StepContent, StepContentItem, StepTip, StepTipItem, StepFooter } from 'components/StepOperation';
    import EditBasicInfo from './include/_EditBasicInfo';
    import EditRepayInfo from './include/_EditRepayInfo';
    import EditRemittanceInfo from './include/_EditRemittanceInfo';
    import EditSpecialAccountInfo from './include/_EditSpecialAccountInfo';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            StepContent, StepContentItem, StepTip, StepTipItem, StepFooter,
            EditBasicInfo,
            EditSpecialAccountInfo,
            EditRepayInfo,
            EditRemittanceInfo
        },
        data: function() {
            return { 
                stepIndex: 1,
                basicInfo: {},
                specialAccountInfo:{},
                repayInfo: {},
                remittanceInfo: {},
                specialAccountType: {},
                specialAccountSwitchOn: true
            };
        },
        methods: {
            backStep: function() {
                --this.stepIndex;
            },
            doneStep1: function(data) {
                this.basicInfo = data;
                ++this.stepIndex;
            },
            doneStep2: function(data) {
                this.specialAccountInfo = data;
                ++this.stepIndex;
            },
            doneStep3: function(data) {
                this.remittanceInfo = data;
                ++this.stepIndex;
            },
            doneStep4: function(data) {
                this.repayInfo = data;
                this.submit()
                    .then(financialContractUuid => {
                        MessageBox.once('closed', () => {
                            this.$router.push({
                                name: 'financialContractdetail',
                                params: { financialContractUuid }
                            });
                        });
                        MessageBox.open('提交成功');
                    })
                    .catch(message => {
                        MessageBox.open(message);
                    });
            },

            submit: function() {
                var isUse = this.specialAccountSwitchOn ? {
                    specialAccountSwitchOn: true,
                    specialAccountInitializationModelJson: this.specialAccountType
                } : {
                    specialAccountSwitchOn: false
                }
                var data = Object.assign({}, this.basicInfo,this.specialAccountInfo, this.remittanceInfo, this.repayInfo,isUse);
                return ajaxPromise({
                    type: 'post',
                    url: '/financialContract/add-new-financialContract',
                    contentType: 'application/json',
                    parse: data => data.financialContractUuid,
                    data: JSON.stringify(data)
                });
            },

            initializeStepModal: function() {
                this.stepIndex = 1
                this.basicInfo = {
                    financialContractNo: '',
                    financialContractName: '',
                    companyUuid: '',
                    appId: '',
                    financialContractType: '',
                    accountName: '',
                    bankName: '',
                    accountNo: '',
                    advaStartDate: '',
                    thruDate: '',
                    financialType: '',
                    financialContractShortName: '',
                    capitalParty: [],
                    otherParty: [],
                    subAccounts: []
                }
                this.specialAccountInfo = {
                    accountName: '',
                    accountNo: '',
                    bankName: ''
                }
                this.remittanceInfo = {
                    transactionLimitPerTranscation:  '',
                    transactionLimitPerDay: '',
                    remittanceStrategyMode: 0,
                    appAccounts:  [],
                    allowModifyRemittanceApplication: 0
                }
                this.repayInfo = {
                    allowRepayment: [], // 存allowOnlineRepayment，allowOfflineRepayment的值
                    assetPackageFormat: '',
                    allowOnlineRepayment: false,
                    allowOfflineRepayment: false,
                    sysNormalDeductFlag: true,
                    sysOverdueDeductFlag: true,
                    allowAdvanceDeductFlag: true,
                    advaRepoTerm: '',
                    advaRepaymentTerm: '',
                    advaMatuterm: '',
                    sysCreatePenaltyFlag: true,
                    penalty: '',
                    overdueDefaultFee: '',
                    overdueServiceFee: '',
                    overdueOtherFee: '',
                    repurchaseCycle: '',
                    daysOfMonth: [],
                    temporaryRepurchases: [],
                    repurchaseRule: '',
                    repurchaseApproach: '-1',
                    repurchasePrincipalAlgorithm: '',
                    repurchaseInterestAlgorithm: '',
                    repurchasePenaltyAlgorithm: '',
                    repurchaseOtherChargesAlgorithm: '',
                    modifyFlag: '',
                    allowNotOverdueAutoConfirm:0,
                    repaymentCheckDays: '',
                    planRepaymentTimeOnline: 0,
                    planRepaymentTimeOffline: 0
                }
            },
            fetchSpecialAccountType(){
                ajaxPromise({
                    url: 'capital/query-special-account-type'
                }).then(data =>{
                    function transfer(tree){
                        if(tree instanceof Array){
                            tree.forEach(item => {
                                transfer(item)
                            })
                        }
                        if(tree.childAccountTypeString){
                            tree.childAccountTypeString = JSON.parse(tree.childAccountTypeString)
                            transfer(tree.childAccountTypeString)
                        }
                    }
                    transfer(data.model)
                    this.specialAccountType = data.model
                }).catch(message =>{
                    MessageBox.open(message)
                })
            },
            specialAccountSwitch: function(checked){
                this.specialAccountSwitchOn = checked
            }
        },
        activated: function(){
            this.initializeStepModal();
            this.fetchSpecialAccountType()
        }
    }

</script>