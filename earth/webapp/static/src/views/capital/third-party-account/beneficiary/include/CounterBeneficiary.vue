<template>
  <div>
    <el-table
        class="td-15-padding th-8-15-padding no-th-border"
        border
        stripe
        v-loading="dataSource.fetching"
        :data="dataSource.list">
        <el-table-column inline-template label="通道流水号">
            <div>{{ row.beneficiaryAuditResult.cashFlowSequenceNo }}</div>
        </el-table-column>
        <el-table-column inline-template label="对方账户">
            <span>{{ row.counterAccountName}}</span>
        </el-table-column>
        <el-table-column
            inline-template
            label="交易金额">
            <span>{{ row.beneficiaryAuditResult.cashFlowTransactionAmount | formatMoney }}</span>
        </el-table-column>
        <el-table-column label="借贷标记" inline-template>
            <div>{{ row.beneficiaryAuditResult.cashFlowAccountSide | CashFlowAccountSideFilter }}</div>
        </el-table-column>
        <el-table-column label="通道请求号" inline-template>
            <div>{{ row.beneficiaryAuditResult.tradeUuid }}</div>
        </el-table-column>
        <el-table-column
            inline-template
            label="入账时间">
            <span>{{ row.beneficiaryAuditResult.cashFlowTransactionTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</span>
        </el-table-column>
        <el-table-column label="备注" inline-template>
            <div>{{ row.beneficiaryAuditResult.cashFlowRemark }}</div>
        </el-table-column>
        <el-table-column
            inline-template
            label="操作">
            <a href="" @click.prevent="onClickCheck(row)" :loading="checking" v-if="ifElementGranted('verify-counter-beneficiary')">重新核单</a>
        </el-table-column>
    </el-table>
    <div class="ft">
        <PageControl
            v-model="pageConds.pageIndex"
            :size="dataSource.size"
            :per-page-record-number="pageConds.perPageRecordNumber">
        </PageControl>
    </div>
  </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import Pagination from 'mixins/Pagination';
    import MessageBox from 'components/MessageBox';

    export default {
        mixins: [Pagination],
        props: {
            selected: Boolean,
            action: {
                type: String,
                required: true
            },
            autoload:{
                type: Boolean,
                default: false
            },
            auditJobUuid: String,
            value: {
                type: Boolean,
                default: false
            }
        },
        data: function() {
            return {
                checking: false,
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
            };
        },
        computed: {
            conditions: function() {
                return Object.assign({}, this.pageConds);
            }
        },
        filters: {
            CashFlowAccountSideFilter: function(val) {
                return {
                    0: '贷',
                    1: '借',
                    '-1': '无对应流水'
                }[val];
            }
        },
        watch: {
            auditJobUuid: function(current) {
                if(!current) {
                    this.dataSource.list = [];
                }
            },
            selected: function(current) {
                if (current && !this.dataSource.list.length) {
                    this.fetch();
                }
            },
            value: function(current) {
                if (this.selected && current) {
                    this.fetch();
                }
            },
        },
        methods: {
            onSuccess: function(data) {
                var d = this.parse(data);
                this.dataSource.list = d.list || [];
                this.dataSource.size = d.size || 0;
                this.dataSource.error = '';
            },
            onClickCheck: function(row) {
                MessageBox.open('确认核单', '提示', [{
                    text: '关闭',
                    type: 'default',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        if (this.checking) return;
                        this.checking = true;
                        ajaxPromise({
                            url: `/audit/beneficiary/${this.auditJobUuid}/vouching/counter`,
                            data: {
                                'auditResultUuid': row.beneficiaryAuditResult.uuid
                            },
                            type: 'post'
                        }).then(data => {
                            MessageBox.once('close',() => {
                                this.$emit('refresh');
                            });
                            MessageBox.open('核单成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        }).then(() => {
                            this.checking = false;
                        });
                    }
                }]);
            },
            onComplete: function() {
                if (this.value) {
                    this.$emit('input', false);
                }
                this.dataSource.fetching = false;
            },
        }
    }
</script>