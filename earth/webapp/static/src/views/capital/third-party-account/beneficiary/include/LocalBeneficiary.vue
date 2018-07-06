<template>
    <div>
        <div class="bd">
            <el-table 
                class="td-15-padding th-8-15-padding no-th-border"
                border
                stripe
                v-loading="dataSource.fetching"
                :data="dataSource.list">
                </el-table-column>
                <el-table-column label="来源单号" inline-template>
                    <a :href="`${ctx}#/capital/payment-cash-flow/online-payment/${row.deductPlanDetailShowModel.deductPlanId}/detail`">{{ row.deductPlanDetailShowModel.deductPlanNo }}</a>
                </el-table-column>
                <el-table-column label="单据类型" inline-template>
                    <div>{{ row.beneficiaryAuditResult.systemBillTypeName }}</div>
                </el-table-column>
                <el-table-column inline-template label="对方账户">
                    <el-popover
                        trigger="hover"
                        placement="top">
                        <div>角色类型：--</div>
                        <div>账户名：{{ row.deductPlanDetailShowModel.payerName }}</div>
                        <div>账户号：{{ row.deductPlanDetailShowModel.bankAccountNo }}</div>
                        <div>开户行：{{ row.deductPlanDetailShowModel.bankName }}</div>
                        <div>所在省：{{ row.deductPlanDetailShowModel.bankProvince }}</div>
                        <div>所在市：{{ row.deductPlanDetailShowModel.bankCity }}</div>
                        <span slot="reference">{{ row.beneficiaryAuditResult.counterPartyAccountName }}</span>
                    </el-popover>
                </el-table-column>
                <el-table-column inline-template label="交易金额">
                    <div>{{ row.beneficiaryAuditResult.systemBillPlanAmount | formatMoney }}</div>
                </el-table-column>
                <el-table-column label="借贷标记" inline-template>
                    <div>{{ row.deductPlanDetailShowModel.orderType }}</div>
                </el-table-column>
                <el-table-column label="通道请求号" inline-template>
                    <div>{{ row.beneficiaryAuditResult.tradeUuid }}</div>
                </el-table-column>
                <el-table-column inline-template label="状态变更时间">
                    <div>{{ row.beneficiaryAuditResult.systemBillOccurDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                </el-table-column>
                <el-table-column label="执行状态" inline-template>
                    <div>{{ row.beneficiaryAuditResult.transExcutionStatusName }}</div>
                </el-table-column>
                <el-table-column
                    inline-template
                    label="操作">
                    <a href="#" @click.prevent="onClickCheck(row)" :loading="checking" v-if="ifElementGranted('verify-local-beneficiary')">重新核单</a>
                </el-table-column>
            </el-table>
        </div>
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
                pageConds:{
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
            };
        },
        computed: {
            conditions: function() {
                return Object.assign({}, this.pageConds);
            }
        },
        watch: {
        	selected: function(current) {
                if (current && !this.dataSource.list.length) {
                    this.fetch();
                }
            },
            auditJobUuid: function(current) {
                if(!current){
                    this.dataSource.list=[];
                } else {
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
        	onClickCheck:function(row) {
                MessageBox.open('确认核单?', '提示', [{
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
                		    url: `/audit/beneficiary/${this.auditJobUuid}/vouching/local`,
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
            onSuccess: function(data) {
                var d = this.parse(data);
                this.dataSource.list = d.list || [];
                this.dataSource.size = d.size || 0;
                this.dataSource.error = '';
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