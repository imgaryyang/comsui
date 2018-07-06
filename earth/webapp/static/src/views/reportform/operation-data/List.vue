<style lang="sass">
</style>

<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <list-cascader
                            clearable
                            size="small"
                            multiple="0"
                            :collection="financialContractQueryModels"
                            v-model="queryConds.financialContractUuids"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
                    </el-form-item>
                    <el-form-item>
                        <DatePicker
                            v-model="queryDateRange"
                            size="small"
                            type="daterange"
                            placeholder="请选择查询日期">
                        </DatePicker>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="fetchTotalAmount" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" v-if="" @click.prevent="changeType">导出</el-button>
                    </el-form-item>
                    <el-form-item class="pull-right">
                        <el-button size="small" type="primary" v-if="false"
                             @click="fetchOperationDataModal.visible = true">更新
                        </el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area" v-if="showBoxAndTable">
                <div class="amount-card-content clearfix">
                    <AmountCard
                        :url="['/operation-data/query/remittance']"
                        :action="action"
                        @click="fetchOperationData('/remittance')">
                        <div slot="left">
                            <div class="hd">放款</div>
                            <p>计划订单总额 ：<span>{{ totalAmount.applicationAmount | formatMoney }}</span> / 笔数 {{ totalAmount.applicationCount }}</p>
                            <p>放款单总额 ：<span>{{ totalAmount.planAmount | formatMoney }}</span> / 笔数 {{ totalAmount.planCount }}</p>
                            <p>导入资产包本金总额 ：<span>{{ totalAmount.assetPrincipal | formatMoney }}</span></p>
                        </div>
                    </AmountCard>
                    <AmountCard
                        v-model="cardInit"
                        ref="planRepayment"
                        hasRight
                        :url="['/operation-data/query/planRepayment']"
                        :action="action"
                        @click="fetchOperationData('/planRepayment', 'planRepayment')">
                        <div slot="left">
                            <div class="hd">计划还款</div>
                            <p>应还日 ：<span>{{ totalAmount.planRepaymentAmount | formatMoney }}</span>(本金{{ totalAmount.planRepaymentPrincipal | formatMoney }}) / 笔数 {{ totalAmount.planRepaymentCount }}</p>
                            <p>宽限期内 ：<span>{{ totalAmount.notOverdueRepaymentAmount | formatMoney }}</span>(本金{{ totalAmount.notOverdueRepaymentPrincipal | formatMoney }}) / 笔数 {{ totalAmount.notOverdueRepaymentCount }}</p>
                            <p>待确认 ：<span>{{ totalAmount.unConfirmedRepaymentAmount | formatMoney }}</span>(本金{{ totalAmount.unConfirmedRepaymentPrincipal | formatMoney }}) / 笔数 {{ totalAmount.unConfirmedRepaymentCount }}</p>
                            <p>已逾期 ：<span>{{ totalAmount.overdueRepaymentAmount | formatMoney }}</span>(本金{{ totalAmount.overdueRepaymentPrincipal | formatMoney }}) / 笔数 {{ totalAmount.overdueRepaymentCount }}</p>
                        </div>
                        <div slot="right">
                            <div
                                v-for="(item, index) in ['应还日', '宽限日', '待确认', '已逾期']"
                                @click="fetchOperationData2('/planRepayment', $event, item, index)"
                                class="box-card-item">
                                {{ item }}计划还款
                            </div>
                        </div>
                    </AmountCard>
                    <AmountCard
                        v-model="cardInit1"
                        ref="actualRepayment"
                        hasRight
                        :url="['/operation-data/query/actualRepayment', '/operation-data/query/preRepayment', '/operation-data/query/partRepayment']"
                        :action="action"
                        @click="fetchOperationData('/actualRepayment', 'actualRepayment')">
                        <div slot="left">
                            <div class="hd">实际还款<br><span>{{ totalAmount.actualRepaymentAmount | formatMoney }}</span></div>
                            <p>本金 ：{{ totalAmount.actualRepaymentPrincipal | formatMoney }}</p>
                            <p>笔数 ：{{ totalAmount.actualRepaymentCount }}</p>
                        </div>
                        <div slot="right">
                            <div class="box-card-item" @click="fetchOperationData2('/actualRepayment', $event, '实际还款', '')">实际还款总额</div>
                            <div
                                v-for="(item, index) in ['线上还款', '线下还款', '线下支付单']"
                                @click="fetchOperationData2('/actualRepayment', $event, item, index)"
                                class="box-card-item">
                                {{ item }}
                            </div>
                            <div class="box-card-item" @click="fetchOperationData2('/preRepayment', $event, '提前还款')">提前还款</div>
                            <div class="box-card-item" @click="fetchOperationData2('/partRepayment', $event, '部分还款')">部分还款</div>
                        </div>
                    </AmountCard>
                    <AmountCard
                        :url="['/operation-data/query/guarantee']"
                        :action="action"
                        @click="fetchOperationData('/guarantee')">
                        <div slot="left">
                            <div class="hd">担保<br><span>{{ totalAmount.guaranteeAmount | formatMoney }}</span></div>
                            <p>笔数 ：{{ totalAmount.guaranteeCount }}</p>
                        </div>
                    </AmountCard>
                    <AmountCard
                        :url="['/operation-data/query/repurchase']"
                        :action="action"
                        @click="fetchOperationData('/repurchase')">
                        <div slot="left">
                            <div class="hd mb">回购<br><span>{{ totalAmount.repurchaseAmount | formatMoney }}</span></div>
                            <p>本金 ：{{ totalAmount.repurchasePrincipal | formatMoney }}</p>
                            <p>笔数 ：{{ totalAmount.repurchaseCount }}</p>
                        </div>
                    </AmountCard>
                </div>
                <div>
                    <RemittanceTable
                        v-if="action == '/operation-data/query/remittance'"
                        :dataSource="dataSource">
                    </RemittanceTable>
                    <PlanRepaymentTable
                        v-if="action == '/operation-data/query/planRepayment'"
                        :tableHeaderTitle="tableHeaderTitle"
                        :dataSource="dataSource">
                    </PlanRepaymentTable>
                    <ActualRepaymentTable
                        v-if="['/operation-data/query/actualRepayment', '/operation-data/query/preRepayment', '/operation-data/query/partRepayment'].includes(action)"
                        :financialContractUuid="queryConds.financialContractUuid"
                        :cashFlowChannelTypeList="cashFlowChannelType"
                        :journalVoucherTypeList="journalVoucherType"
                        :tableHeaderTitle="tableHeaderTitle"
                        :dataSource="dataSource">
                    </ActualRepaymentTable>
                    <GuaranteeTable
                        v-if="action == '/operation-data/query/guarantee'"
                        :dataSource="dataSource">
                    </GuaranteeTable>
                    <RepurchaseTable
                        v-if="action == '/operation-data/query/repurchase'"
                        :dataSource="dataSource">
                    </RepurchaseTable>
                </div>
            </div>
        </div>
        <div class="operations">
            <div class="pull-right">
                <ListStatistics
                    ref="listStatistics"
                    :action="listStatisticsAction"
                    :parameters="conditions">
                    <template scope="statistics">
                        <div v-if="action == '/operation-data/query/remittance'">
                            <div>计划订单笔数：{{ statistics.data.applicationCount ? statistics.data.applicationCount : 0 }}</div>
                            <div>计划订单总额：{{ statistics.data.applicationAmount | formatMoney }}</div>
                            <div>放款单笔数：{{ statistics.data.planCount }}</div>
                            <div>放款单总额：{{ statistics.data.planAmount | formatMoney }}</div>
                            <div>实际放款笔数：{{ statistics.data.actualCount }}</div>
                            <div>实际放款总额：{{ statistics.data.actualAmount | formatMoney }}</div>
                            <div>导入资产包总额：{{ statistics.data.assetAmount | formatMoney }}</div>
                            <div>导入资产包本金总额：{{ statistics.data.assetPrincipal | formatMoney }}</div>
                            <div>导入资产包利息总额：{{ statistics.data.assetInterest | formatMoney }}</div>
                            <div>导入资产包贷款服务费总额：{{ statistics.data.assetLoanServiceFee | formatMoney }}</div>
                        </div>
                        <div v-if="action == '/operation-data/query/planRepayment'">
                            <div>{{tableHeaderTitle}}计划还款笔数：{{ statistics.data.count ? statistics.data.count : 0 }}</div>
                            <div>{{tableHeaderTitle}}计划还款总额：{{ statistics.data.amount | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}计划还款本金：{{ statistics.data.assetPrincipalValue | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}计划还款利息：{{ statistics.data.assetInterestValue | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}计划还款贷款服务费：{{ statistics.data.loanServiceFee | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}计划还款技术维护费：{{ statistics.data.loanTechFee | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}计划还款其他费用：{{ statistics.data.loanOtherFee | formatMoney }}</div>
                        </div>
                        <div v-if="['/operation-data/query/actualRepayment', '/operation-data/query/preRepayment', '/operation-data/query/partRepayment'].includes(action) && tableHeaderTitle !='线下支付单'">
                            <div>{{tableHeaderTitle}}笔数：{{ statistics.data.count ? statistics.data.count : 0 }}</div>
                            <div>{{tableHeaderTitle}}总额：{{ statistics.data.amount | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}本金：{{ statistics.data.loanAssetPrincipal | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}利息：{{ statistics.data.loanAssetInterest | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}贷款服务费：{{ statistics.data.loanServiceFee | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}技术维护费：{{ statistics.data.loanTechFee | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}其他费用：{{ statistics.data.loanOtherFee | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}逾期罚息：{{ statistics.data.overdueFeePenalty | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}逾期违约金：{{ statistics.data.overdueFeeObligation | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}逾期服务费：{{ statistics.data.overdueFeeService | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}逾期其他费用：{{ statistics.data.overdueFeeOther | formatMoney }}</div>
                        </div>
                        <div v-if="['/operation-data/query/actualRepayment', '/operation-data/query/preRepayment', '/operation-data/query/partRepayment'].includes(action) && tableHeaderTitle =='线下支付单'">
                            <div>{{tableHeaderTitle}}笔数：{{ statistics.data.count ? statistics.data.count : 0 }}</div>
                            <div>{{tableHeaderTitle}}还款总额：{{ statistics.data.amount | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}还款本金：{{ statistics.data.loanAssetPrincipal | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}还款利息：{{ statistics.data.loanAssetInterest | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}还款贷款服务费：{{ statistics.data.loanServiceFee | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}还款技术维护费：{{ statistics.data.loanTechFee | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}其他费用：{{ statistics.data.loanOtherFee | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}逾期罚息：{{ statistics.data.overdueFeePenalty | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}逾期违约金：{{ statistics.data.overdueFeeObligation | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}逾期服务费：{{ statistics.data.overdueFeeService | formatMoney }}</div>
                            <div>{{tableHeaderTitle}}逾期其他费用：{{ statistics.data.overdueFeeOther | formatMoney }}</div>
                        </div>
                        <div v-if="action == '/operation-data/query/guarantee'">
                            <div>担保笔数：{{ statistics.data.count ? statistics.data.count : 0 }}</div>
                            <div>担保总额：{{ statistics.data.amount | formatMoney }}</div>
                        </div>
                        <div v-if="action == '/operation-data/query/repurchase'">
                            <div>回购笔数：{{ statistics.data.count ? statistics.data.count : 0 }}</div>
                            <div>回购总额：{{ statistics.data.amount | formatMoney }}</div>
                            <div>回购本金：{{ statistics.data.repurchasePrincipal | formatMoney }}</div>
                            <div>回购利息：{{ statistics.data.repurchaseInterest | formatMoney }}</div>
                            <div>回购罚息：{{ statistics.data.repurchasePenalty | formatMoney }}</div>
                            <div>回购其他费用：{{ statistics.data.repurchaseOtherCharges | formatMoney }}</div>
                        </div>
                    </template>
                </ListStatistics>
                <PageControl
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>
        <FetchOperationDataModal
            v-model="fetchOperationDataModal.visible"
            :model="fetchOperationDataModal.model">
        </FetchOperationDataModal>

        <ExportOperationDataModal
                :model="exportOperationData.model"
                v-model="exportOperationData.visible">
        </ExportOperationDataModal>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise, purify, searchify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import DatePicker from 'components/DatePicker'
    import format from 'filters/index';

    export default {
        mixins: [ListPage, Pagination],
        components: {
            DatePicker,
            ListStatistics: require('views/include/ListStatistics'),
            AmountCard: require('./include/AmountCard'),
            FetchOperationDataModal: require('./include/FetchOperationDataModal'),
            ExportOperationDataModal: require('./include/ExportOperationDataModal'),
            RemittanceTable: require('./include/RemittanceTable'),
            PlanRepaymentTable: require('./include/PlanRepaymentTable'),
            ActualRepaymentTable: require('./include/ActualRepaymentTable'),
            GuaranteeTable: require('./include/GuaranteeTable'),
            RepurchaseTable: require('./include/RepurchaseTable'),
        },
        data: function() {
            var endDate = format.formatDate(new Date() - 1000* 60*60*24);
            return {
                action: '/operation-data/query/remittance',
                listStatisticsAction: '/operation-data/statistics/remittance',
                autoload: false,

                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    financialContractUuid: '',
                    queryStartDate: '',
                    queryEndDate: '',
                    planStyleOrdinal: '',
                    businessTypeOrdinal: '',
                },

                financialContractQueryModels: [],
                totalAmount: {},
                queryDateRange: [],

                cashFlowChannelType: [],
                journalVoucherType: [],

                tableHeaderTitle: '',

                fetchOperationDataModal: {
                    visible: false,
                    model: {}
                },
                exportOperationData: {
                    visible: false,
                    model: {
                        financialContractUuid: '',
                        queryStartDate: '',
                        queryEndDate:''
                    }
                },
                showBoxAndTable: false,
                cardInit: false,
                cardInit1: false,
            };
        },
        activated: function() {
            this.getOptions();
            this.getCashFlowChannelType();
            this.getJournalVoucherType();
        },
        watch: {
            '$route.query.t': function() {
                this.queryConds = {
                    financialContractUuids: [],
                    financialContractUuid: '',
                    queryStartDate: '',
                    queryEndDate: '',
                    planStyleOrdinal: '',
                    businessTypeOrdinal: '',
                };
                this.queryDateRange = [];
                this.totalAmount = {};
                this.tableHeaderTitle = '';
                this.showBoxAndTable = false;
            },
            queryDateRange: function(current){
                this.queryConds.queryStartDate = format.formatDate(current[0], 'yyyy-MM-dd');
                this.queryConds.queryEndDate = format.formatDate(current[1], 'yyyy-MM-dd');
            },
            'queryConds.financialContractUuids': function(current) {
                if(current.length == 0){
                    this.queryConds.financialContractUuid = '';
                } else {
                    this.queryConds.financialContractUuid = current[0].financialContractUuid;
                }
            },
            'pageConds.pageIndex': function(){
                if(this.queryConds.financialContractUuid && this.queryConds.queryStartDate) {
                    this.fetch();
                }
            },
        },
        methods: {
            getOptions: function() {
                ajaxPromise({
                    url: `/operation-data/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            changeType: function () {
                if (!this.queryConds.financialContractUuid || !this.queryConds.queryStartDate || !this.queryConds.queryEndDate) {
                    MessageBox.open('请选择信托合同和查询日期！');
                    return;
                }
                this.exportOperationData.visible = true;
                this.exportOperationData.model={
                    financialContractUuid:this.queryConds.financialContractUuid ,
                    queryStartDate:this.queryConds.queryStartDate,
                    queryEndDate:this.queryConds.queryEndDate
                }
            },
            getCashFlowChannelType: function() {
                ajaxPromise({
                    url: `/operation-data/cashFlowChannelType`
                }).then(data => {
                    this.cashFlowChannelType = data.cashFlowChannelType || [];
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            getJournalVoucherType: function() {
                ajaxPromise({
                    url: `/operation-data/journalVoucherType`
                }).then(data => {
                    this.journalVoucherType = data.journalVoucherType || [];
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            onSuccess: function(data) {
                this.dataSource.list = data.list.map(item => {
                    item.statistics = {
                        list: []
                    };
                    return item;
                });
                this.dataSource.size = data.size;
            },
            fetchTotalAmount: function() {
                if(this.queryConds.financialContractUuid == '' || !this.queryConds.queryStartDate) {
                    if(this.queryConds.financialContractUuid != '') {
                        MessageBox.open('请选择查询日期');
                    } else if(this.queryConds.queryStartDate){
                        MessageBox.open('请选择信托合同');
                    } else {
                        MessageBox.open('请选择信托合同和查询日期');
                    }
                } else {
                    ajaxPromise({
                        url: `/operation-data/query/totalAmount`,
                        data: {
                            financialContractUuid: this.queryConds.financialContractUuid,
                            queryStartDate: this.queryConds.queryStartDate,
                            queryEndDate: this.queryConds.queryEndDate
                        }
                    }).then(data => {
                        this.totalAmount = data || {};
                        this.showBoxAndTable = true;
                    }).catch(message => {
                        MessageBox.open(message);
                    }).then(() => {
                        this.cardInit = true;
                        this.cardInit1 = true;
                        this.action = `/operation-data/query/remittance`;
                        this.listStatisticsAction = '/operation-data/statistics/remittance';
                        $('.box-card-item').removeClass('active');
                        this.fetch();
                    })
                }
            },
            fetchOperationData: function(url, ref) {
                $('.box-card-item').removeClass('active');
                if(ref == 'planRepayment' || ref == 'actualRepayment') {
                    $(this.$refs[ref].$el).find('.box-card-item:first').addClass('active');
                    if(ref == 'planRepayment') {
                        this.tableHeaderTitle = '应还日';
                        this.queryConds.planStyleOrdinal = 0;
                        this.queryConds.businessTypeOrdinal = '';
                    }
                    if(ref == 'actualRepayment') {
                        this.tableHeaderTitle = '实际还款';
                        this.queryConds.businessTypeOrdinal = '';
                        this.queryConds.planStyleOrdinal = '';
                    }
                }
                this.action = '/operation-data/query' + url;
                this.pageConds.pageIndex = 1;
                this.listStatisticsAction = '/operation-data/statistics' + url;
                this.fetch();
            },
            fetchOperationData2: function(url, $event, item, index) {
                $('.box-card-item').removeClass('active');
                $($event.target).addClass('active');
                if(item == '线上还款') {
                    this.tableHeaderTitle = '线上实收';
                }
                if(item == '线下还款') {
                    this.tableHeaderTitle = '线下实收';
                }
                if(!['线上还款', '线下还款'].includes(item)){
                    this.tableHeaderTitle = item;
                }
                if(url == '/planRepayment') {
                    this.queryConds.planStyleOrdinal = index;
                    this.queryConds.businessTypeOrdinal = '';
                }
                if(url == '/actualRepayment') {
                    this.queryConds.businessTypeOrdinal = index;
                    this.queryConds.planStyleOrdinal = '';
                }
                this.action = '/operation-data/query' + url;
                this.pageConds.pageIndex = 1;
                this.listStatisticsAction = '/operation-data/statistics' + url;
                this.dataSource = Object.assign(this.dataSource,{
                    list : [],
                    size : 0
                })
                this.fetch();
            }
        }
    }
</script>