<template>
  <div>
    <el-table 
        class="td-15-padding th-8-15-padding no-th-border"
        border
        stripe
        v-loading="dataSource.fetching"
        :data="dataSource.list">
        <el-table-column prop="cashFlowAdapter.cashFlowSerialNo" label="通道流水号"></el-table-column>
        <el-table-column 
            inline-template 
            label="对方账户">
            <el-popover 
                v-if="row.cashFlowAdapter.cashFlowCpAccountInfo"
                trigger="hover" 
                placement="top">
                    <div>角色类型：--</div>
                    <div>账户名：{{ row.cashFlowAdapter.cashFlowCpAccountInfo.accountName }}</div>
                    <div>账户号：{{ row.cashFlowAdapter.cashFlowCpAccountInfo.accountNo }}</div>
                    <div>开户行：{{ row.cashFlowAdapter.cashFlowCpAccountInfo.bankName }}</div>
                    <div>所在省：{{ row.cashFlowAdapter.cashFlowCpAccountInfo.province }}</div>
                    <div>所在市：{{ row.cashFlowAdapter.cashFlowCpAccountInfo.city }}</div>
                      <span slot="reference">{{ row.cashFlowAdapter.cashFlowCpAccountInfo.accountName }}</span>
            </el-popover>
        </el-table-column>
        <el-table-column 
            inline-template
            label="流水金额">
            <span>{{ row.cashFlowAdapter.cashFlowAmount | formatMoney }}</span>
        </el-table-column>
        <el-table-column prop="cashFlowAdapter.accountSideName" label="借贷标记"></el-table-column>
        <el-table-column prop="cashFlowAdapter.cashFlowTradeUuid" label="通道请求号"></el-table-column>
        <el-table-column 
            inline-template
            label="入账时间">
            <span>{{ row.cashFlowAdapter.cashFlowTransationTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</span>
        </el-table-column>
        <el-table-column prop="cashFlowAdapter.cashFlowRemark" label="备注"></el-table-column>
        <el-table-column 
            inline-template
            label="操作">
            <a href="javascript: void 0;" @click="onClickCheck(row)" v-if="ifElementGranted('verify-counter-reconciliation')">重新核单</a>
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
    import { Loading } from 'element-ui';
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
            auditJobUuid:String,
        },
        data: function() {
            return {
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
        watch: {
            auditJobUuid: function(current) {
                if(!current) {
                    this.dataSource.list=[];
                }  
            },
            selected: function(current) {
                if (current && !this.dataSource.list.length) {
                    this.fetch();
                }
            }
        },
        methods: {
            onSuccess: function(data) {
                var d = this.parse(data);
                this.dataSource.list = d.list;
                this.dataSource.size = d.size;
                this.dataSource.error = '';
            },
            onClickCheck:function(row) {
                var uuid = row.cashFlowAdapter.cashFlowTradeUuid;
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