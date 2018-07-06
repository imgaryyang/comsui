<style lang="sass">
    @import '~assets/stylesheets/base';

    #payment-order-cashflow {
        @include min-screen(768px) {
            .modal-dialog {
                width: 50%;
                margin: 30px auto;
            }
        }
    }
</style>

<template>
    <Modal v-model="show" id="payment-order-cashflow">
        <ModalHeader title="流水匹配">
        </ModalHeader>
        <ModalBody v-loading="dataSource.fetching">
            <div v-if="dataSource.error.length != 0">
                {{ dataSource.error }}
            </div>
            <div style="margin-bottom: 15px;">
                <el-table
                    :data="showList"
                    class="td-15-padding th-8-15-padding no-th-border"
                    v-loading="dataSource.fetching"
                    stripe
                    border>
                    <el-table-column :width="50" label="" inline-template :context="_self">
                        <el-checkbox @input="handleInput(row.cashFlowUuid, arguments[0])" :value="row.cashFlowUuid == selectCashFlowUuid"></el-checkbox >
                    </el-table-column>
                    <el-table-column label="流水号" prop="bankSequenceNo">
                    </el-table-column>
                    <el-table-column label="交易金额" prop="cashFlowAmount" inline-template>
                        <div>{{ row.cashFlowAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="银行账号" prop="countAccountNo">
                    </el-table-column>
                    <el-table-column label="账户姓名" prop="countAccountName">
                    </el-table-column>
                    <el-table-column label="入账时间" prop="transactionTime" inline-template>
                        <div>{{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="摘要内容" prop="remark">
                    </el-table-column>
                </el-table>
                <div>
                    <PageControl 
                        v-model="pageConds.pageIndex"
                        :size="dataSource.size"
                        :per-page-record-number="pageConds.perPageRecordNumber">
                    </PageControl>
                </div>
            </div>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" :disabled="selectCashFlowUuid === ''" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';
    import Pagination from 'mixins/Pagination';

  	export default {
        mixins: [modalMixin, Pagination],
        props: {
            uuid: {
              default: null
            }
        },
        data: function() {
            return {
                selectCashFlowUuid: '',
                show: this.value,

                action: '/repayment-order/selectCashFlow',
                autoload: false,
                
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 4
                },
            }
        },
        watch: {
            show: function(current) {
                if (current) {
                    this.selectCashFlowUuid = ''
                    this.fetch()
                }
            }
        },
        computed: {
            conditions: function() {
                return Object.assign({paymentOrderUuid: this.uuid});
            },
            showList: function() {
                var { pageIndex, perPageRecordNumber } = this.pageConds;
                var start = (pageIndex - 1) * perPageRecordNumber;
                var end = start + perPageRecordNumber;
                return this.dataSource.list ? this.dataSource.list.slice(start, end) : this.dataSource.list;
            }
        },
        methods: {
            fetch: function() {
                if (!this.uuid || !this.show) return;
                if (this.fetchTimer) {
                    clearTimeout(this.fetchTimer);
                }

                this.fetchTimer = setTimeout(() => {
                    if (this.dataSource.fetching
                        && this.equalTo(purify(this.conditions), purify(this.previousConditions))) {
                        return
                    }

                    this.getData({
                        url: this.action,
                        data: this.conditions
                    });
                }, 10);
            },
            submit: function() {
                var { uuid, selectCashFlowUuid } = this;

                ajaxPromise({
                    url: `/repayment-order/selectCashFlowAfter`,
                    data: {
                        cashFlowUuid: selectCashFlowUuid,
                        paymentOrderUuid: this.uuid
                    },
                }).then(data => {
                    MessageBox.open('流水匹配成功!正在跳转...');
                    setTimeout(() => {
                        this.show = false;
                        location.reload();
                    }, 1000);
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            handleInput: function(cashFlowUuid, checked) {
                if (checked) {
                    this.selectCashFlowUuid = cashFlowUuid
                } else {
                    this.selectCashFlowUuid = ''
                }
            }
        }
    }

</script>
