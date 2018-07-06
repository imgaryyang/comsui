<template>
  <div>
    <el-table 
        class="td-15-padding th-8-15-padding no-th-border"
        border
        stripe
        v-loading="dataSource.fetching"
        :data="dataSource.list">
        <el-table-column prop="merchantNo" label="商户号"></el-table-column>
        <el-table-column prop="sumCount" label="总笔数"></el-table-column>
        <el-table-column 
            inline-template
            label="总金额">
            <span>{{ row.sumAmount | formatMoney }}</span>
        </el-table-column>
        <el-table-column 
            inline-template
            label="总手续费">
            <span>{{ row.sumServiceFee | formatMoney }}</span>
        </el-table-column>
        <el-table-column 
            inline-template
            label="清算时间">
            <span>{{ row.settleDate | formatDate('yyyy-MM-dd HH:mm:ss') }}</span>
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
            onComplete: function() {
                if (this.value) {
                    this.$emit('input', false);
                }
                this.dataSource.fetching = false;
            },
        }
    }
</script>