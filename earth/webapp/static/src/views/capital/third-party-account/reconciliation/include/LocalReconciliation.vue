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
                <el-table-column inline-template label="来源单号">
					<div>
						<a 
                            :href="`${ctx}#/capital/remittance-cash-flow/plan-execlog/${row.systemBill.systemBillNo}/detail`"
                            v-if="row.systemBill.systemBillType == 'T_REMITTANCE_PLAN_EXEC_LOG'">
                            {{ row.systemBill.systemBillNo }}
                        </a>
						<span v-else>{{ row.systemBill.systemBillNo }}</span>
                	</div>
                </el-table-column>
                <el-table-column prop="systemBill.systemBillTypeName" label="单据类型"></el-table-column>
                <el-table-column prop="systemBill.transactionTypeName" label="业务类别"></el-table-column>
                <el-table-column 
                	inline-template 
                	label="对方账户">
                    <el-popover 
                        v-if="row.systemBill.systemBillCpAccountInfo"
                    	trigger="hover" 
                    	placement="top">
                            <div>角色类型：--</div>
                            <div>账户名：{{ row.systemBill.systemBillCpAccountInfo.accountName }}</div>
                            <div>账户号：{{ row.systemBill.systemBillCpAccountInfo.accountNo }}</div>
                            <div>开户行：{{ row.systemBill.systemBillCpAccountInfo.bankName }}</div>
                            <div>所在省：{{ row.systemBill.systemBillCpAccountInfo.province }}</div>
                            <div>所在市：{{ row.systemBill.systemBillCpAccountInfo.city }}</div>
                            <span slot="reference">{{ row.systemBill.systemBillCpAccountInfo.accountName }}</span>
                    </el-popover>
                </el-table-column>
           
                <el-table-column inline-template label="交易金额">
                    <div>{{ row.systemBill.systemBillAmount | formatMoney }}</div>
                </el-table-column>
                <el-table-column prop="systemBill.systemBillAccountSideName" label="借贷标记" ></el-table-column>
                <el-table-column prop="systemBill.systemBillTradeUuid" label="通道请求号"></el-table-column>
                <el-table-column 
                	inline-template
                	label="状态变更时间">
                	<span>{{ row.systemBill.systemBillLastModifedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</span>
                </el-table-column>
                <el-table-column prop="systemBill.executionStatusName" label="执行状态"></el-table-column>
                <el-table-column 
                    inline-template
                    label="操作">
                    <a href="javascript: void 0;" @click="onClickCheck(row)" v-if="ifElementGranted('verify-local-reconciliation')">重新核单</a>
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
            	type:Boolean,
            	default: false
            },
            auditJobUuid:String
        },
        data: function() {
            return {
                pageConds:{
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                fetching: false,
                systemBill: [],
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
                //pagination.js对condition进行监听，在详情页相互跳转间，$route.params属性相同时，会触发其他页面的请求
                if(current && window.location.hash.includes('capital/third-party-account/reconciliation')){
                    this.fetch();
                }
            }
        },
        methods: {
        	onClickCheck:function(row) {
                var uuid = row.systemBill.systemBillTradeUuid;
                MessageBox.open('确认重新核单？', '提示', [{
                    text: '取消',
                    handler: () => {
                        MessageBox.close();
                    }
                },{
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        ajaxPromise({
                            url: `/audit/remittance/${uuid}/detail/audit-result`,
                            type: 'post'
                        }).then(data => {
                            MessageBox.open('核单成功');
                            setTimeout(()=>{
                                MessageBox.close();
                            },500)
                        }).catch(message => {
                          MessageBox.open(message);
                        });
                    }
                }]);
        	}
        }
    }
</script>