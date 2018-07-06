<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <list-cascader
                            clearable
                            size="small"
                            :collection="financialContractQueryModels"
                            v-model="queryConds.financialContractUuids"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.paymentGateway"
                            placeholder="支付通道"
                            size="small"
                            multiple>
                            <el-option
                                v-for="item in paymentGateway"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.paymentOrderStatus"
                            placeholder="支付状态"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in paymentOrderStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.payWay"
                            placeholder="支付方式"
                            clearable
                            size="small">
                            <el-option
                                v-for="item in payWays"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    v-model="queryConds.startDateString"
                                    :end-date="queryConds.endDateString"
                                    size="small"
                                    placeholder="创建起始时间">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    v-model="queryConds.endDateString"
                                    :start-date="queryConds.startDateString"
                                    size="small"
                                    placeholder="创建终止时间">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="订单编号" value="uuid"></el-option>
                            <el-option label="还款订单号" value="orderUuid"></el-option>
                            <el-option label="通道交易号" value="outlierDocumentIdentity"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    v-loading="dataSource.fetching"
                    :data="dataSource.list"
                    stripe>
                    <el-table-column label="订单编号" inline-template>
                        <a :href="`${ctx}#/capital/payment-cash-flow/payment-order/${ row.uuid }/detail`">{{ row.uuid }}</a>
                    </el-table-column>
                    <el-table-column label="还款订单号" inline-template>
                        <a :href="`${ctx}#/finance/repayment-order/${ row.orderUuid }/detail`">{{ row.orderUuid }}</a>
                    </el-table-column>
                    <el-table-column label="产品代码" prop="financialContractNo"></el-table-column>
                    <el-table-column label="支付方式" prop="payWayChinese"></el-table-column>
                    <el-table-column label="支付通道" prop="paymentGateWay"></el-table-column>
                    <el-table-column label="通道交易号" prop="tradeUuid"></el-table-column>
                     <el-table-column label="交易金额" inline-template>
                        <div>{{ row.amount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="创建时间" inline-template>
                        <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" inline-template>
                        <div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="支付状态" prop="paymentOrderStatusChinese"></el-table-column>
                    <el-table-column label="备注" prop="remark"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-right">
                <PageControl 
                    ref="pageControl"
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise, searchify, purify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        mixins: [Pagination, ListPage],
        components: { 
            ComboQueryBox: require('views/include/ComboQueryBox'),
        },
        data: function() {
            return {
                action: '/repayment-order/payment/query',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    paymentGateway: '',
                    paymentOrderStatus: '',
                    payWay: '',
                    startDateString: '',
                    endDateString: '',
                },
                comboConds: {
                    uuid: '',
                    orderUuid: '',
                    outlierDocumentIdentity: '',
                },

                financialContractQueryModels: [],
                paymentGateway: [],
                paymentOrderStatus: [],
                payWays: []
            };
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: `/repayment-order/payment/optionsData`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.paymentGateway = data.paymentGateway || [];
                    this.paymentOrderStatus = data.paymentOrderStatus || [];
                    this.payWays = data.payWays || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            }
        }
    }
</script>