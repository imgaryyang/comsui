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
						<ComboQueryBox v-model="comboConds">
							<el-option label="账户姓名" value="accountName"></el-option>
							<el-option label="银行账户号" value="payAcNo"></el-option>
						</ComboQueryBox>
					</el-form-item>
					<el-form-item>
						<el-button size="small" type="primary" ref="lookup">查询</el-button>
					</el-form-item>
					<el-form-item>
						<el-button	
							size="small" 
							type="success"
							@click="createModal.visible = true"
							v-if="ifElementGranted('create-offline-payment')">
							新增
						</el-button>
					</el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    v-loading="dataSource.fetching"
                    @sort-change="onSortChange"
                    :data="dataSource.list"
                    stripe>
                    <el-table-column label="支付单号" prop="" inline-template>
                        <a :href="`${ctx}#/capital/payment-cash-flow/offline-payment/${row.offlineBill.id}/detail`">{{ row.offlineBill.offlineBillNo }}</a>
                    </el-table-column>
                    <el-table-column label="银行名称" prop="offlineBill.bankShowName"></el-table-column>
                    <el-table-column label="账户姓名" prop="offlineBill.payerAccountName"></el-table-column>
                    <el-table-column label="银行账户号" prop="offlineBill.payerAccountNo"></el-table-column>
                    <el-table-column label="支付机构流水号" prop="offlineBill.serialNo"></el-table-column>
                    <el-table-column label="支付金额" prop="" sortable="custom" inline-template>
                        <div>{{ row.offlineBill.amount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="关联金额" prop="" sortable="custom" inline-template>
                        <div>{{ row.sourceDocument.bookingAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="关联状态" prop="offlineBillConnectionState"></el-table-column>
                    <el-table-column label="入账时间" prop="offlineBill.tradeTime" sortable="custom"></el-table-column>
                    <el-table-column label="发生时间" prop="offlineBill.statusModifiedTime" sortable="custom"></el-table-column>
                    <el-table-column label="支付状态" prop="" inline-template>
                        <div :class="{
                            'color-danger': row.offlineBill.offlineBillStatus == 'UNPAID'
                        }">
                            {{ row.offlineBill.offlineBillStatus | offlineBillStatusOrd2Msg }}
                        </div>
                    </el-table-column>
                    <el-table-column label="备注" prop="offlineBill.comment"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-right">
                <ListStatistics 
                    action="/offline-payment-manage/payment/amountStatistics"
                    :parameters="conditions">
                    <template scope="statistics">
                        <div>支付金额<span class="pull-right">{{ statistics.data.paymentAmount | formatMoney }}</span></div>
                    </template>
                </ListStatistics>
                <PageControl 
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>

        <CreateOfflinePaymentModal 
            @submit="fetch()"
            preCreateAction="/offline-payment-manage/payment/pre-create-offline-bill"
            createAction="/offline-payment-manage/payment/create-offline-bill"
            :financialContractQueryModels="financialContractQueryModels"
            v-model="createModal.visible">
        </CreateOfflinePaymentModal>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ListStatistics: require('views/include/ListStatistics'),
        	ComboQueryBox: require('views/include/ComboQueryBox'),
            CreateOfflinePaymentModal: require('./CreateOfflinePaymentModal')
        },
        data: function() {
        	return {
        		action: '/offline-payment-manage/payment/query',
        		pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
        		queryConds: {
                    financialContractUuids: [],
                },
        		comboConds: {
                    accountName: '',
                    payAcNo: '',
                },
        		sortConds: {
                    sortField: '',
                    isAsc: ''
                },

        		financialContractQueryModels: [],

                createModal: {
                    visible: false,
                }
        	};
        },
        filters: {
            offlineBillStatusOrd2Msg: function(value) {
                return { UNPAID:'失败',PAID:'成功' }[value];
            }
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: `/offline-payment-manage/payment/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            }
        }
    }
</script>