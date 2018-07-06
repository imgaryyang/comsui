<style lang="sass">
    .merge-table {
        th  {
            .el-checkbox {
                display: none;
            }
        }
    }
	.mergePopover {
        padding: 0px;

        .merge-data {
            background-color: #F5F5F5;
            overflow:scroll;
            height: 150px;
            overflow-x: hidden;

            .no-data {
                text-align: center;
                line-height: 150px;
            }


            .item {
                height: 60px;
                padding: 5px 10px;
                border-bottom: 1px solid #e0e0e0;
                position: relative;

                .color-dim {
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    width: 290px;
                    overflow: hidden;

                }

                p {
                    margin: 5px 0px;
                }

                .delete {
                    height: 16px;
                    width: 16px;
                    background-image: url(~assets/images/icons.png);
                    display: inline-block;
                    background-repeat: no-repeat;
                    vertical-align: middle;
                    background-position: -117px -4px;
                    cursor: pointer;
                    position: absolute;
                    top: 25px;
                    right: 10px;
                }
            }

        }

        .operation-area {
            padding: 10px;
            height: 60px;
        }
    }
</style>

<template>
	<div class="content" id="repayment-order">
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
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    v-model="queryConds.startDateString"
                                    :end-date="queryConds.endDateString"
                                    placeholder="创建起始时间"
                                    size="small">
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
                                    :formatToMaximum="true"
                                    v-model="queryConds.endDateString"
                                    :start-date="queryConds.startDateString"
                                    placeholder="创建终止时间"
                                    size="small">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="订单编号" value="orderUuid"></el-option>
                            <el-option label="订单总金额" value="orderAmount"></el-option>
                            <el-option label="商户订单号" value="orderUniqueId"></el-option>
                            <el-option label="贷款人" value="firstCustomerName"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
			</div>
			<div class="table-area">
				<el-table
					class="no-table-bottom-border merge-table"
					stripe
                    ref="table"
                    row-key="orderUuid"
                    @selection-change="handleSelectionChange"
					v-loading="dataSource.fetching"
					:data="dataSource.list">
                    <el-table-column
                        type="selection"
                        :reserve-selection="true"
                        width="55">
                    </el-table-column>
					<el-table-column label="订单编号" prop="orderUuid" inline-template>
                        <a :href="`${ctx}#/finance/repayment-order/${row.orderUuid}/detail`">{{row.orderUuid }}</a>
                    </el-table-column>
                    <el-table-column label="商户订单号" prop="orderUniqueId">
                    </el-table-column>
                    <el-table-column label="信托产品代码" prop="financialContractNo">
                    </el-table-column>
                    <el-table-column label="贷款人" prop="firstCustomerName">
                    </el-table-column>
                    <el-table-column label="订单总金额" prop="orderAmount" inline-template>
                        <div>{{ row.orderAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="订单状态" prop="chineseRepaymentStatus">
                    </el-table-column>
                    <el-table-column label="创建时间" prop="orderCreateTime" inline-template>
                        <div>{{ row.orderCreateTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" prop="orderLastModifiedTime" inline-template>
                        <div>{{ row.orderLastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="订单来源" prop="orderSourceStatusCh">
                    </el-table-column>
                    <el-table-column label="备注" prop="remark">
                    </el-table-column>
				</el-table>
			</div>
		</div>
		<div class="operations">
            <div class="pull-left">
                <el-popover
                    ref="mergePopover"
                    placement="top"
                    trigger="click"
                    popper-class="mergePopover"
                    width="320">
                    <slot>
                        <div class="merge-data">
                            <div v-for="(item, index ) in selectedMergeOrders" class="item">
                                <div class="pull-left">
                                    <p class="color-dim">订单编号: {{ item.orderUuid }}</p>
                                    <p class="color-danger">金额: {{ item.orderAmount | formatMoney }}</p>
                                </div>
                                <i class="delete pull-right" @click="deleteItem(index)"></i>
                            </div>
                            <div v-if="!selectedMergeOrders.length" class="no-data color-dim">暂无订单</div>
                        </div>
                        <div class="operation-area">
                            <div class="pull-left">
                                <div>
                                    <span class="color-dim">合并订单：</span><span class="color-danger">{{ selectedMergeOrders.length }}</span>
                                </div>
                                <div>
                                    <span class="color-dim">合并金额：</span><span class="color-danger">{{ mergeTotalAmount | formatMoney }}</span>
                                </div>
                            </div>
                            <div class="pull-right">
                                <el-button type="primary" @click="doMerge" v-if="selectedMergeOrders.length > 1">去合并</el-button>
                            </div>
                        </div>
                    </slot>
                </el-popover>
                <el-badge :value="selectedMergeOrders.length">
                    <el-button key="merge" v-popover:mergePopover>待合并订单</el-button>
                </el-badge>
                <el-button @click="$router.go(-1)">取消</el-button>
            </div>
			<div class="pull-right">
				<PageControl 
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
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
    import format from 'filters/format';

	export default {
		mixins: [Pagination, ListPage],
		components: {
			ComboQueryBox: require('views/include/ComboQueryBox')
		},
		data: function() {
			return {
				action: '/repayment-order/mergeRepayment/query',
				pageConds: {
					perPageRecordNumber: 12,
					pageIndex: 1
				},
				queryConds: {
					financialContractUuids: [],
					startDateString: '',
					endDateString: ''
				},
				comboConds: {
                    orderUuid: '',
                    orderAmount: '',
                    orderUniqueId: '',
                    firstCustomerName: ''
				},

				financialContractQueryModels: [],
                selectedMergeOrders: []
			}
		},
		computed: {
            mergeTotalAmount: function() {
                var totalAmount = 0;
                this.selectedMergeOrders.forEach(item => {
                    var amount = parseFloat(item.orderAmount ? item.orderAmount : '0');
                    totalAmount += amount;
                })
                return totalAmount;
            },
            mergeRepaymentOrderUuids: function() {
                var uuids = [];
                uuids = this.selectedMergeOrders.map(item => item.orderUuid);
                return JSON.stringify(uuids);
            }
		},
        watch: {
            'queryConds.financialContractUuids': function(current) {
                // console.log(current);
            },
        },
        mounted: function() {
            this.checked();
        },
        deactivated: function() {
            this.$refs.table.clearSelection();
        },
		methods: {
			initialize: function() {
				return ajaxPromise({
					url: '/repayment-order/repayment/optionsData'
				}).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];

                    // let uuids = [];
                    // let value = data.queryAppModels[0].value;
                    // data.queryAppModels[0].children.forEach(item => {
                    //     uuids.push(item.value);
                    // });
                    // this.queryConds.financialContractUuids = [value,uuids];
                    // console.log(this.queryConds.financialContractUuids);
				}).catch(message => {
					MessageBox.open(message);
				});
			},
            handleSelectionChange: function(selectedItems) {
                this.selectedMergeOrders = selectedItems;
            },
            checked: function() {
                var selected = false;
                this.dataSource.list.forEach(row => {
                    this.selectedMergeOrders.forEach(orders => {
                        if (row.orderUuid == orders.orderUuid) {
                            selected = true;
                            return;
                        }
                    })
                    selected && this.$refs.table.toggleRowSelection(row,true);
                });
            },
            doMerge: function() {
                MessageBox.open(`确认合并订单数：${this.selectedMergeOrders.length}, 总金额：${ format.formatMoney(this.mergeTotalAmount)}`, '确认合并订单', [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: '/repayment-order/mergeRepaymentOrder',
                            data: {
                                mergeRepaymentOrderUuids: this.mergeRepaymentOrderUuids
                            }
                        }).then(data => {
                            MessageBox.once('closed', () => {
                                this.selectedMergeOrders = [];
                                this.$router.push({path: '/finance/repayment-order'});
                            });
                            MessageBox.open(data.message);
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                }]);
            },
            deleteItem: function(index) {
                this.selectedMergeOrders.splice(index, 1);
            }
		}
	}
</script>