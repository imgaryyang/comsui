<style lang="sass" scoped>
    .hd {
        margin: 0;
        line-height: 40px;
        padding: 0 10px;
        background-color: #fff;
        border: 1px solid #e7e8e9;
        border-bottom: none;
    }
    .ft {
        border: 1px solid #e7e8e9;
        box-sizing: border-box;
        background-color: #fff;
        border-top: none;
        position: relative;
    }
</style>
<template>
    <div class="system-operate-log">
        <div class="hd">{{ title }}</div>
        <div class="bd">
            <el-table
                class="td-15-padding th-8-15-padding no-th-border"
                v-loading="dataSource.fetching"
                :data="dataSource.list"
                stripe
                border>
                <el-table-column width="60px" type="index" label="序号">
                </el-table-column>

                <el-table-column width="150px" prop="occurTime" label="操作发生时间">
                </el-table-column>

                <el-table-column prop="operateName" label="操作员登录名">
                </el-table-column>

                <el-table-column prop="recordContent" label="操作内容">
                </el-table-column>
            </el-table>
        </div>
        <div class="ft clearfix operations">
            <PageControl
                v-model="pageConds.pageIndex"
                :size="dataSource.size"
                :per-page-record-number="pageConds.perPageRecordNumber">
            </PageControl>
        </div>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';

    export default {
        mixins: [Pagination],
        props: {
            forObjectUuid: [String, Number],
            title: {
                type: String,
                default: '操作日志'
            },
            fetching: {
                type: Boolean,
                default: false
            }
        },
        data: function() {
            return {
                action: `/system-operate-log/query`,
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 5
                }
            }
        },
        watch: {
            fetching: function(current) {
                if(current) {
                    this.fetch();
                    this.$emit('input', false);
                }
            }
        },
        computed: {
            queryConds: function() {
                return {
                    objectUuid: this.forObjectUuid
                };
            },
            conditions: function() {
                return Object.assign({}, this.queryConds, this.pageConds);
            }
        },
        methods: {
            fetch: function() {
                if (!this.conditions.objectUuid) return;
                this.getData({
                    url: this.action,
                    data: this.conditions
                });
            },
        }
    }
</script>