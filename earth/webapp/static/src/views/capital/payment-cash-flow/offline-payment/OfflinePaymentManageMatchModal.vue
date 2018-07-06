<style lang="sass">
    @import '~assets/stylesheets/base.scss';
    #matchOfflinePaymentModal {
        @include min-screen(768px) {
            .modal-dialog {
                width: 85%;
            }
        }
    }
</style>

<template>
    <Modal v-model="visible" id="matchOfflinePaymentModal">
        <ModalHeader title="关联线下支付单"></ModalHeader>
        <ModalBody align="left" v-loading="fetching">
            <div class="query-area" style="margin: 0px 0 10px; border: 1px solid #d1d1d1;">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <el-input size="small" placeholder="来源单号" :value="queryConds.orderNo" @change.native="queryConds.orderNo = $event.target.value"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-select size="small" placeholder="来源类型" v-model="queryConds.orderType">
                            <el-option 
                                v-for="item in orderTypes"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-input size="small" placeholder="客户姓名" :value="queryConds.customerName" @change.native="queryConds.customerName = $event.target.value"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.assetRecycleStartDateString"
                                    :end-date="queryConds.assetRecycleEndDateString"
                                    size="small"
                                    placeholder="请输入计划还款起始日期">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.assetRecycleEndDateString"
                                    :start-date="queryConds.assetRecycleStartDateString"
                                    size="small"
                                    placeholder="请输入计划还款终止日期">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" @click="fetch">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" :disabled="matchDisabled" @click="handleMatch">关联</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    v-loading="dataSource.fetching"
                    @selection-change="onSelectionChange"
                    :data="dataSource.list"
                    stripe>
                    <el-table-column label="" type="selection" :selectable="selectableCheck"></el-table-column>
                    <el-table-column label="来源单号" prop="orderNo"></el-table-column>
                    <el-table-column label="还款期号" prop="singleLoanContractNo"></el-table-column>
                    <el-table-column label="应还日期" prop="assetRecycleDate" inline-template>
                        <div>{{ row.assetRecycleDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="结算日期" prop="settlementDate" inline-template>
                        <div>{{ row.settlementDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="客户姓名" prop="customerName"></el-table-column>
                    <el-table-column label="应还本金" prop="assetPrincipalValue"></el-table-column>
                    <el-table-column label="应还利息" prop="assetInterestAmount"></el-table-column>
                    <el-table-column label="差异罚息" prop="penaltyInterestAmount"></el-table-column>
                    <el-table-column label="差异天数" prop="overDueDays"></el-table-column>
                    <el-table-column label="发生时间" prop="modifyTime" inline-template>
                        <div>{{ row.settlementDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="应还金额" prop="amount"></el-table-column>
                    <el-table-column label="已付金额" prop="paidAmount"></el-table-column>
                    <el-table-column label="关联金额" inline-template :width="110">
                        <el-input v-model="dataSource.list[$index].matchAmount"></el-input>
                    </el-table-column>
                    <el-table-column label="状态" prop="statusMsg"></el-table-column>
                    <el-table-column label="备注" prop="comment"></el-table-column>
                </el-table>
            </div>
        </ModalBody>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import Pagination from 'mixins/Pagination';
    import MessageBox from 'components/MessageBox';

    export default {
        mixins: [Pagination],
        props: {
            value: Boolean,
            offlineBillUuid: String,
            orderTypes: Array
        },
        data: function() {
        	return {
                action: '/offline-payment-manage/search',
                autoload: false,
                visible: false,
                selections: [],

                queryConds: {
                    orderNo: '',
                    orderType: '',
                    customerName: '',
                    assetRecycleStartDateString: '',
                    assetRecycleEndDateString: '',
                },

                matchDisabled: false,
        	};
        },
        computed: {
            conditions: function() {
                return Object.assign({}, this.queryConds);
            }
        },
        watch: {
            value: function(current) {
                this.visible = current;
                if (!current) {
                    this.dataSource.list = [];
                    this.dataSource.size = [];
                }
            },
            visible: function(current, previous) {
                this.$emit('input', current);
            }
        },
        methods: {
            handleMatch: function() {
                var result = {};

                this.matchDisabled = true

                if (!this.selections.length) {
                    MessageBox.open('至少勾选一项');
                    return;
                }

                this.selections.forEach(item => {
                    result[item.orderNo] = item.matchAmount;
                });

                ajaxPromise({
                    url: '/offline-payment-manage/connection',
                    type: 'post',
                    data: {
                        orderNoAndValues: JSON.stringify(result),
                        offlineBillUuid: this.offlineBillUuid,
                    }
                }).then(data => {
                    this.matchDisabled = false;
                    MessageBox.once('close', () => {
                        this.visible = false;
                    });
                    MessageBox.once('closed', () => {
                        this.$emit('submit');
                    });
                    MessageBox.open('关联成功！');
                }).catch(message => {
                    this.matchDisabled = false;
                    MessageBox.open(message);
                })
            },
            onSuccess: function(data) {
                var d = this.parse(data);
                this.dataSource.list = d.orderMatchModelList.map(item => {
                    item.matchAmount = '';
                    return item
                });
                this.dataSource.size = d.size;
                this.dataSource.error = '';
            },
            selectableCheck: function(row) {
                return row.status != 'ALL';
            },
            onSelectionChange: function(selections) {
                this.selections = selections;
            },
        }
    }
</script>