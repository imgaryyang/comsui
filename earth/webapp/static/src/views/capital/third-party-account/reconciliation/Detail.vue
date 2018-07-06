<style lang="sass" scoped>
    $boldSize: 20px;
    .tab-menu-reconciliation {
        margin-bottom: 20px;
    }
    .tableList{
        font-size: 12px;
        background-color: #fff;
        table{
            table-layout: fixed;
            .help-popover{
                position: absolute;
            }
            caption{
                color: #333;
                font-size: 14px;
                font-weight: bold;
            }
            td{
                color: #888;
            }
            .tr1{
                height: 37px;
            }
            .font-color-nomal{
                color: #000;
            }
            .text-large{
                font-size: $boldSize;
            }
            .tr3{
                height: 111px;
                td{
                    vertical-align: middle;
                    div{
                        margin-left: 5px;
                    }
                }
            }
        }
    }
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb 
                :routes="[{ title: '对账机构' }, {title: '对账详情'}]">
                <el-button size="small" type="primary" :loading="reReconciliating" @click="onClickReconciliation" v-if="ifElementGranted('redo-reconciliation-audit')">重新对账</el-button>
                <el-button size="small" type="primary" :loading="exporting" @click="onClickExport" v-if="ifElementGranted('export-reconciliation')">导出结果</el-button>
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
                                <p>状态 ：{{ auditJob.auditResultName }}</p>
                            </div>
                        </div>
                    </div>
                    <div class="block" >
                        <h5 class="hd">
                            {{ $utils.locale('financialContract') }}信息
                        </h5>
                        <div class="bd">
                            <div class="col">
                                <p>{{ $utils.locale('financialContract.no') }} ：{{ financialContract.contractNo }}</p>
                                <p>{{ $utils.locale('financialContract.name') }} ：{{ financialContract.contractName }}</p>
                                <p>银行账号 ：{{ capitalAccount.accountNo }}</p>
                                <p>开户行 ：{{ capitalAccount.bankName }}</p>
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
                                <p>清算号 ：{{ paymentChannel.clearingNo }}</p>
                                <p>收付类型 ：{{ auditJob.accountSideName }}</p>
                                <p>入账起始时间 ：{{ auditJob.startTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                                <p>入账终止时间 ：{{ auditJob.endTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                            </div>
                        </div>
                    </div>
                </div> 
            </div>
            <div class="col-layout-detail tableList">
                <div class="top">
                    <div class="block">
                        <table class="table">
                            <caption>本端流水</caption>
                            <thead>
                                <td></td>
                                <td>交易成功</td>
                                <td>冲账</td>
                                <td>合计</td>
                            </thead>
                            <tbody>
                                <tr class="tr1">
                                    <td>放款</td>
                                    <td><span class="font-color-nomal">{{statResult.remittanceExecLogSucAmount | formatMoney}}</span>/ {{statResult.execLogSucNum}} 笔</td>
                                    <td><span class="font-color-nomal">{{statResult.remittanceExecLogFailAndReversedAmount | formatMoney}}</span>/ {{statResult.remittanceExecLogFailAndReversedNum}} 笔</td>
                                    <td><span class="font-color-nomal">{{statResult.remittanceExecLogTotalAmount | formatMoney}}</span></td>
                                </tr>
                                <tr class="tr1">
                                    <td>转账</td>
                                    <td><span class="font-color-nomal">{{statResult.transferExecLogSucAmount | formatMoney}}</span>/ {{statResult.transferExecLogSucNum}} 笔</td>
                                    <td><span class="font-color-nomal">{{statResult.transferExecLogFailAndReversedAmount | formatMoney}}</span>/ {{statResult.transferExecLogFailAndReversedNum}} 笔</td>
                                    <td><span class="font-color-nomal">{{statResult.transferExecLogTotalAmount | formatMoney}}</span></td>
                                </tr>
                                <tr class="tr1">
                                    <td>合计</td>
                                    <td><span class="font-color-nomal">{{statResult.ExecLogSucTotalAmount | formatMoney}}</span>/ {{statResult.execLogSucTotalNum}} 笔</td>
                                    <td><span class="font-color-nomal">{{statResult.ExecLogFailAndReversedTotalAmount | formatMoney}}</span>/ {{statResult.execLogFailAndReversedTotalNum}} 笔</td>
                                    <td><span class="font-color-nomal">{{statResult.ExecLogTotalAmount | formatMoney}}</span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="block">
                        <table class="table">
                            <caption>对端流水</caption>
                            <thead>
                                <td>贷记</td>
                                <td>借记</td>
                                <td>合计   <HelpPopover content="合计=贷记-借记"/></td>
                            </thead>
                            <tbody>
                                <tr class="tr3">
                                    <td>
                                        <div>
                                            <p><span :class="[
                                            'text-large',
                                            statResult.creditCashAmount == statResult.ExecLogTotalAmount ? 'color-success' : 'color-danger'
                                            ]">{{ statResult.creditCashAmount | formatMoney }}/ {{ statResult.creditCashNum }}</span> 条</p>
                                            <p>本端: {{statResult.ExecLogTotalAmount | formatMoney }}</p>
                                        </div>
                                    </td>
                                    <td>
                                        <div>
                                            <p><span :class="[
                                            'text-large',
                                            statResult.debitCashAmount == statResult.ExecLogFailAndReversedTotalAmount ? 'color-success' : 'color-danger'
                                            ]">{{ statResult.debitCashAmount | formatMoney }}/ {{ statResult.debitCashNum }}</span> 条</p>
                                            <p>本端: {{statResult.ExecLogFailAndReversedTotalAmount | formatMoney}}/ {{statResult.execLogFailAndReversedTotalNum}} 笔</p>
                                        </div>
                                    </td>
                                    <td>
                                        <div>
                                            <p><span :class="[
                                            'text-large',
                                            statResult.indebtednessTotalAmount == statResult.ExecLogSucTotalAmount ? 'color-success' : 'color-danger'
                                            ]">{{statResult.indebtednessTotalAmount | formatMoney}}</span></p>
                                            <p>本端: {{statResult.ExecLogSucTotalAmount | formatMoney}}/ {{statResult.execLogSucTotalNum}} 笔</p>
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
                    <TabMenu class="tab-menu-reconciliation" v-model="selected">
                        <TabMenuItem id="LOCAL">本端多账</TabMenuItem>
                        <TabMenuItem id="COUNTER">对端多账</TabMenuItem>
                        <TabMenuItem id="ISSUED">已平账</TabMenuItem>
                    </TabMenu>
                    <TabContent v-model="selected">
                        <TabContentItem id="LOCAL">
                            <LocalReconciliation 
                                :action="`/audit/remittance/${$route.params.id}/detail/audit-result?resultCode=1`"
                                :selected="selected == 'LOCAL'"
                                :autoload="selected == 'LOCAL'"
                                :auditJobUuid="$route.params.id">
                            </LocalReconciliation>
                        </TabContentItem>
                        <TabContentItem id="COUNTER">
                            <CounterReconciliation
                                :action="`/audit/remittance/${$route.params.id}/detail/audit-result?resultCode=3`"
                                :selected="selected == 'COUNTER'"
                                :autoload="selected == 'COUNTER'"
                                :auditJobUuid="$route.params.id">
                            </CounterReconciliation>
                        </TabContentItem>
                        <TabContentItem id="ISSUED">
                            <IssuedReconciliation
                                :action="`/audit/remittance/${$route.params.id}/detail/audit-result?resultCode=2`"
                                :selected="selected == 'ISSUED'"
                                :autoload="selected == 'ISSUED'"
                                :auditJobUuid="$route.params.id">
                            </IssuedReconciliation>
                        </TabContentItem>
                    </TabContent>
                </div>
            </div>
            <div class="row-layout-detail">
                <div class="block">
                    <SystemOperateLog :for-object-uuid="$route.params.id"></SystemOperateLog>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
    import { ctx } from 'src/config';
    import SystemOperateLog from 'views/include/SystemOperateLog';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
    import Decimal from 'decimal.js';

    export default{
        components:{
            SystemOperateLog,
            TabMenu, TabMenuItem, TabContent, TabContentItem,
            LocalReconciliation: require('./include/LocalReconciliation'),
            CounterReconciliation: require('./include/CounterReconciliation'),
            IssuedReconciliation: require('./include/IssuedReconciliation'),
            HelpPopover: require('views/include/HelpPopover'),
        },
        data: function() {
            return {
                selected: '',
                reReconciliating: false,
                exporting: false,
                fetching: false,
                auditJob: {},
                financialContract: {},
                paymentChannel: {},
                statResult: {},
                capitalAccount: {}
            }
        },
        activated: function() {
            this.selected = 'LOCAL';
            this.fetch(this.$route.params.id);
        },
//本端流水表格
// remittanceExecLogSucAmount/execLogSucNum           remittanceExecLogFailAndReversedAmount/remittanceExecLogSucNum            remittanceExecLogTotalAmount
// transferExecLogSucAmount  /transferExecLogSucNum   transferExecLogFailAndReversedAmount  /transferExecLogFailAndReversedNum  transferExecLogTotalAmount
// ExecLogSucTotalAmount     /execLogSucTotalNum      ExecLogFailAndReversedTotalAmount     /execLogFailAndReversedTotalNum     ExecLogTotalAmount

        methods: {
            fetch: function(auditJobUuid) {
                this.fetching = true;

                ajaxPromise({
                    url: `/audit/remittance/${auditJobUuid}/detail/basic-info`
                }).then(data => {
                    this.auditJob = Object.assign({}, data.auditJob);
                    this.financialContract = Object.assign({}, data.financialContract);
                    this.capitalAccount = Object.assign({}, data.financialContract.capitalAccount);
                    this.paymentChannel = Object.assign({}, data.paymentChannel);
                    this.statResult = Object.assign({
                        remittanceExecLogTotalAmount: '',
                        transferExecLogTotalAmount: '',
                        ExecLogSucTotalAmount: '',
                        execLogSucTotalNum: '',
                        ExecLogFailAndReversedTotalAmount: '',
                        execLogFailAndReversedTotalNum: '',
                        ExecLogTotalAmount: '',
                        indebtednessTotalAmount: ''
                    }, data.statResult)
                    this.setTotalAmount()
                })
                .catch(message => {
                   MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
            onClickReconciliation: function(e) {
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
                        var id = this.$route.params.id;
                        this.$router.push({
                            name: 'reconciliationList'
                        });
                        this.reReconciliating = true;
                        ajaxPromise({
                            url: `/audit/remittance/${id}/redo`,
                            type: 'post'
                        }).then(data => {
                            this.fetch(id);
                        }).catch(message => {
                            MessageBox.open(message);
                        }).then(() => {
                            this.reReconciliating = false;
                        });
                    }
                }]);
            },
            onClickExport: function(e) {
                var auditJobUuid = this.$route.params.id;
                window.open(this.api + `/audit/remittance/${auditJobUuid}/detail/audit-result-export`);
            },
            total: function(a,b){
                try{
                    let zero = new Decimal(0)
                    var r = zero.add(a)
                                .add(b)
                                .toNumber()
                }catch(e){
                    return 0
                }
                return r
            },
            getIndebtednessTotalAmount: function(){
                try{
                    let zero = new Decimal(0)
                    var r = zero.add(this.statResult.creditCashAmount)
                                .minus(this.statResult.debitCashAmount)
                                .toNumber()
                }catch(e){
                    return 0
                }
                return r
            },
            setTotalAmount: function(){
                this.statResult.remittanceExecLogTotalAmount = this.total(this.statResult.remittanceExecLogSucAmount, this.statResult.remittanceExecLogFailAndReversedAmount)
                this.statResult.transferExecLogTotalAmount = this.total(this.statResult.transferExecLogSucAmount, this.statResult.transferExecLogFailAndReversedAmount)
                this.statResult.ExecLogSucTotalAmount = this.total(this.statResult.remittanceExecLogSucAmount, this.statResult.transferExecLogSucAmount)
                this.statResult.execLogSucTotalNum = this.total(this.statResult.execLogSucNum, this.statResult.transferExecLogSucNum)
                this.statResult.ExecLogFailAndReversedTotalAmount = this.total(this.statResult.remittanceExecLogFailAndReversedAmount, this.statResult.transferExecLogFailAndReversedAmount)
                this.statResult.execLogFailAndReversedTotalNum = this.total(this.statResult.remittanceExecLogFailAndReversedNum, this.statResult.transferExecLogFailAndReversedNum)
                this.statResult.ExecLogTotalAmount = this.total(this.statResult.remittanceExecLogTotalAmount, this.statResult.transferExecLogTotalAmount)
                this.statResult.indebtednessTotalAmount = this.getIndebtednessTotalAmount()
            }
        }
    }
</script>