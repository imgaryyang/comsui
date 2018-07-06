<style lang="sass">
    
</style>
<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '回盘详情'}]">
                <el-button
                    v-if="obsoleteJudge"
                    type="primary" 
                    size="small" 
                    :loading="obsoleting"
                    @click="obsolete">作废</el-button>
                <el-button 
                    v-if="arrivalStateJudge"
                    type="primary" 
                    size="small" 
                    :loading="settling"
                    @click="settle">结清</el-button>
            </Breadcrumb>

            <div class="col-layout-detail">
            	<div class="top">
            		<div class="block">
            			<h5 class="hd">回盘详情</h5>
            			<div class="bd">
            				<div class="col">
            					<p>回盘编号：{{ batchInfo.externalTradeBatchUuid }}</p>
                                <p>{{$utils.locale('financialContract')}}：{{ batchInfo.financialContractName }}</p>
            					<p>批次号：{{ batchInfo.externalBatchNo }}</p>
            				</div>
            			</div>
            		</div>
            		<div class="block">
                        <h5 class="hd"></h5>
            			<div class="bd">
            				<div class="col">
            					<p>交易日期：{{ batchInfo.tradeDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
            					<p>导入时间：{{ batchInfo.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
            					<p>状态变更时间：{{ batchInfo.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
            					<p>校验状态：{{ batchInfo.tradeBatchState }}</p>
                                <p>清算状态：{{ batchInfo.arrivalState }}</p>
                            </div>
                        </div>
            		</div>
            		<div class="block">
                        <h5 class="hd"></h5>
            			<div class="bd">
            				<div class="col">
            					<p>总金额：{{ batchInfo.totalAmount | formatMoney }}</p>
            					<p>三方代扣总额：{{ batchInfo.deductAmount | formatMoney}}</p>
            					<p>转账总额：{{ batchInfo.onlineTransferAmount | formatMoney}}</p>
                                <p>现金总额 : {{ batchInfo.offlineCashAmount | formatMoney}}</p>
            				</div>
            			</div>
            		</div>
            	</div>
            </div>

            <div class="row-layout-detail">
                <div class="block">
                    <h5 class="hd">银行流水
                    <el-button
                        v-if="arrivalStateJudge"
                        type="primary" 
                        size="small"
                        class="pull-right"
                        @click=" showCashFlow = true">添加流水</el-button>
                    </h5>
                    <div class="bd">
                        <el-table 
                            :data="batchCashFlows"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column 
                                inline-template
                                label="流水号">
                            <a :href="`${ctx}#/capital/special-account/cash-flow-audit?cashFlowNo=${row.bankSequenceNo}&hostAccountNo=${hostAccountNo}`">{{ row.bankSequenceNo }}</a>
                            </el-table-column>
                            <el-table-column prop="accountSideMsg" label="借贷标志"></el-table-column>
                            <el-table-column 
                                inline-template
                                label="交易金额">
                                <div>{{ row.tradeAmount | formatMoney}}</div>    
                            </el-table-column>
                            <el-table-column prop="counterAccountNo" label="银行账号"></el-table-column>
                            <el-table-column prop="counterAccountName" label="银行账户名"></el-table-column>
                            <el-table-column prop="counterBankName" label="开户行"></el-table-column>
                            <el-table-column 
                                inline-template
                                prop="tradeTime" 
                                label="入账时间">
                                <div>{{ row.tradeTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>    
                            </el-table-column>
                            <el-table-column prop="tradeRemark" label="摘要"></el-table-column>
                            <el-table-column
                                label="对应类型"
                                prop="relatedTradeTypeMsg">
                            </el-table-column>
                            <el-table-column 
                                inline-template
                                label="操作">
                                <a v-if="arrivalStateJudge" 
                                    href="javascript:void(0)" 
                                    @click="deleteCashFlow(row)">
                                    删除
                                </a>    
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <el-form
                        ref="form"
                        v-model="queryConds"
                        class="sdf-form sdf-query-form" 
                        :inline="true">
                        <el-form-item>
                            <label class="hd">回盘数据</label>
                        </el-form-item>
                        <el-form-item>
                            <el-select 
                                v-model="queryConds.checkState"
                                size="small" 
                                clearable 
                                placeholder="验真状态">
                                <el-option
                                    v-for="(value, label) in tradeAuditResults"
                                    :label="value"
                                    :value="label">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-select 
                                v-model="queryConds.cashFlowAuditResult"
                                size="small" 
                                clearable 
                                placeholder="资金流水">
                                <el-option
                                    v-for="(value, label) in cashFlowAuditResults"
                                    :label="value"
                                    :value="label">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-form>
                    
                    <div class="bd">
                        <el-table 
                            :data="dataSource.list"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column prop="externalRecordSn" label="序号" width="60px">
                            </el-table-column>
                            <el-table-column prop="externalRecordUniqueId" label="订单号"></el-table-column>
                            <el-table-column 
                                inline-template
                                label="订单金额">
                                <div>   
                                    {{ row.amount | formatMoney }}    
                                </div>
                            </el-table-column>
                            <el-table-column prop="currency" label="交易币种"></el-table-column>
                            <el-table-column prop="cpName" label="姓名"></el-table-column>
                            <el-table-column prop="cpBankCardNo" label="银行账号"></el-table-column>
                            <el-table-column prop="cpBankCode" label="开户行联行号"></el-table-column>
                            <el-table-column prop="financialContractName" label="渠道来源"></el-table-column>
                            <el-table-column prop="contractNo" label="借据号"></el-table-column>
                            <el-table-column 
                                inline-template
                                label="交易时间">
                                <div>
                                    {{ row.tradeTime | formatDate('yyyy-MM-dd HH:mm:ss') }}        
                                </div>
                            </el-table-column>
                            <el-table-column prop="tradeStateChinese" label="交易状态"></el-table-column>
                            <el-table-column prop="externalBusinessOrderNo" label="清算平台订单号"></el-table-column>
                            <el-table-column prop="externalTradeSerialNo" label="清算平台流水号"></el-table-column>
                            <el-table-column prop="tradeRemark" label="交易摘要"></el-table-column>
                            <el-table-column prop="checkState" label="校验状态"></el-table-column>
                            <el-table-column prop="arriveRecode" label="资金流水"></el-table-column>
                            <el-table-column prop="detailVerifyResultRemark" label="备注"></el-table-column>
                        </el-table>
                    </div>
                        <div class="ft clearfix">
                            <PageControl 
                                v-model="pageConds.pageIndex"
                                :size="dataSource.size"
                                :per-page-record-number="pageConds.perPageRecordNumber">
                            </PageControl>
                        </div>
                </div>
            </div>
        </div>

    <AddCashFlow 
        v-model="showCashFlow"
        :externalTradeBatchUuid="this.$route.params.uuid"
        @submit="onSubmitAddStatement">
    </AddCashFlow>

    </div>
</template>

<script>
    import Pagination, { extract } from 'mixins/Pagination';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import formats from 'filters/format';

    export default{
        mixins: [Pagination],
        components:{
            SystemOperateLog: require('views/include/SystemOperateLog'),
            AddCashFlow: require('./include/AddCashFlow')
        },                                               
        data: function() {
            return {
                action: '/trade/external-batch/detail/query',

                fetching: false,
                obsoleting: false,
                settling: false,

                queryConds: {
                    checkState: '',
                    cashFlowAuditResult: '',
                },
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                dataList:[],
                remittanceApplication: {},
                remittancePlans: [],
                cashFlowAuditResults:[],
                tradeAuditResults:[],
                batchInfo:{},
                batchCashFlows:[],
                totalCashFlow: '',
                hostAccountNo: '',
                flag: '',

                showCashFlow:false
            }
        },
        computed: {
            conditions: function() {
                return Object.assign({ externalTradeBatchUuid: this.$route.params.uuid }, this.queryConds, this.pageConds);
            },
            obsoleteJudge: function() {
                if (this.flag == 0 || this.flag == 2){
                    return true;
                }

                return false;
               /* const { batchInfo } = this;
                if (batchInfo.externalTradeActiveStatus == 1) {
                    return false;
                }
                return ([2].includes(batchInfo.externalTradeVerifyResult) && ['未结清'].includes(batchInfo.arrivalState)) 
                    || ([0].includes(batchInfo.externalTradeAuditProgress) && ['未结清'].includes(batchInfo.arrivalState))*/
            },
            arrivalStateJudge: function() {
                if (this.flag == 1 || this.flag == 2){
                    return true;
                }

                return false;
                /*const { batchInfo } = this;
                if (batchInfo.externalTradeActiveStatus == 1) {
                    return false;
                }
                if (batchInfo.externalTradeVerifyResult == 2) {
                    return false;
                }
                return ['未结清'].includes(batchInfo.arrivalState) && [0, 2].includes(batchInfo.externalTradeAuditProgress);*/
            }
        },
        watch: {
            show: function(current) {
                this.$emit('input', current);
                if (current) {
                    this.$refs.form.resetFields();
                }
            },
            externalTradeBatchUuid: function(current) {
                if(!current){
                    this.$refs.form.resetFields();
                }
            },
            queryConds: {
                deep: true,
                handler: function() {
                    this.pageConds.pageIndex = 1;
                }
            },
        },
        activated: function() {
            this.externalTradeBatchUuid = this.$route.params.uuid;
            this.getDate(this.$route.params.uuid);
            this.getOption(this.$route.params.uuid);
        },
        deactivated: function() {
            this.queryConds= {
                    checkState: '',
                    cashFlowAuditResult: '',
                };
        },
        methods: {
            fetch: function() {
                if(this.$route.params.uuid){
                    this.getData({
                        url: this.action,
                        data: this.conditions
                    });
                }
            },
            getDate: function(uuid) {
                ajaxPromise({
                    url: `/trade/external-batch/detail`,
                    data:{
                        externalTradeBatchUuid: uuid 
                    }
                }).then(data => {
                    this.batchInfo = data.batchInfo || {};
                    this.flag = data.flag || '';
                    this.batchCashFlows = data.batchCashFlows || [];
                    this.totalCashFlow = data.totalCashFlow || '';
                    this.hostAccountNo = data.hostAccountNo || '';
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            getOption: function(uuid) {
                ajaxPromise({
                    url: `/trade/external-batch/detail/optionData`,
                    data:{
                        externalTradeBatchUuid: uuid 
                    }
                }).then(data => {
                    this.tradeAuditResults = data.tradeAuditResult || [];
                    this.cashFlowAuditResults = data.cashFlowAuditResult || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (!valid) return;
                    ajaxPromise({
                        url: `/remittance/application/details/updateplannotifynumber/${this.$route.params.id}`,
                        data: this.reCallbackModal.fields,
                        type: 'POST'
                    }).then(data => {
                        this.reCallbackModal.show = false;
                        setTimeout(()=> {
                            MessageBox.once('close', () => {
                                this.fetch(this.$route.params.id);
                                this.$refs.sysLog.fetch();
                            });
                            MessageBox.open('计划回调次数已增加，稍后系统会自动回调结果');
                        }, 500);
                    })
                    .catch(message => {
                        MessageBox.open(message);
                    })
                    .then(() => {
                        this.fetching = false;
                    });
                });
            },
            obsolete: function() {
                MessageBox.open('确认作废该条回盘文件嘛？', '提示', [{
                    text: '取消',
                    handler: () => {
                        MessageBox.close();
                    }
                },{
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        if (this.obsoleting) return;

                        this.obsoleting = true;

                        ajaxPromise({
                            url: `/trade/external-batch/invalid`,
                            data:{
                                externalTradeBatchUuid: this.$route.params.uuid 
                            }
                        }).then(data => {
                            MessageBox.open("作废成功");
                            this.getDate(this.$route.params.uuid);
                            this.fetch();
                            setTimeout(()=>{
                                MessageBox.close();
                            },500)
                        }).catch(message => {
                            MessageBox.open(message);
                        }).then(() => {
                            this.obsoleting = false;
                        })
                    }
                }]);
            },
            settle: function() {
                var htm = `<div style="margin: 30px 0px;">回盘总金额：`+ formats.formatMoney(this.batchInfo.totalAmount)    +`<br>流水总金额：`+ formats.formatMoney(this.totalCashFlow) + `<br>确认结清回盘文件嘛？</div>`;

                MessageBox.open(htm, '提示', [{
                    text: '取消',
                    handler: () => {
                        MessageBox.close();
                    }
                },{
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        if (this.settling) return;

                        this.settling = true;

                        ajaxPromise({
                            url: `/trade/external-batch/settle`,
                            data:{
                                externalTradeBatchUuid: this.$route.params.uuid 
                            }
                        }).then(data => {
                            MessageBox.open("结清成功");
                            this.getDate(this.$route.params.uuid);
                            this.fetch();
                            setTimeout(()=>{
                                MessageBox.close();
                            },500)
                        }).catch(message => {
                            MessageBox.open(message);
                        }).then(() => {
                            this.settling = false;
                        })
                    }
                }]);
            },
            onSubmitAddStatement: function() {
                this.getDate(this.$route.params.uuid);
                this.getOption(this.$route.params.uuid);
                this.fetch(this.$route.params.uuid);
                this.showCashFlow = false;
            },
            deleteCashFlow: function(row) {
                MessageBox.open('确认删除该条记录？', '提示', [{
                    text: '取消',
                    handler: () => {
                        MessageBox.close();
                    }
                },{
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/trade/external-batch/cash/unbind`,
                            type:'POST',
                            data:{
                                externalTradeBatchUuid: this.$route.params.uuid,
                                cashFlowUuid: row.cashFlowUuid
                            }
                        }).then(data => {
                            MessageBox.open("作废成功");
                            this.getDate(this.$route.params.uuid);
                            this.fetch();
                            setTimeout(()=>{
                                MessageBox.close();
                            },500)
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }]);
            },
        }
    }
</script>
