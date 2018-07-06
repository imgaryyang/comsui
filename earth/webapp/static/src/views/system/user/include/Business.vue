<style lang="sass">

</style>

<template>
    <div class="block">
        <div class="bd">
            <el-table 
                class="td-15-padding th-8-15-padding no-th-border"
                stripe
                border
                @selection-change="handleSelectionChange"
                v-loading="dataSource.fetching"
                :data="dataSource.list">
                <el-table-column label="全选" type="selection"  width="50"></el-table-column>
                <el-table-column :label="$utils.locale('financialContract.no')" prop="contractNo"></el-table-column>
                <el-table-column :label="$utils.locale('financialContract.name')" prop="contractName"></el-table-column>
                <el-table-column :label="$utils.locale('financialContract.appAccount')" prop="appName"></el-table-column>
                <el-table-column :label="$utils.locale('financialContract.type')" prop="financialContractType"></el-table-column>
                <el-table-column label="状态" inline-template>
                    <div>
                        <span class="color-danger" v-if="row.bindState == 0">未绑定</span>
                        <span v-else>已绑定</span>
                    </div>
                </el-table-column>
                <el-table-column label="操作" inline-template>
                    <a href="javascript: void 0;" @click="handleUnBind(row.id)">解除绑定</a>
                </el-table-column>
            </el-table>
        </div>
        <div class="ft clearfix" style="line-height: 28px;">
            <el-button size="small" type="text" @click="handleUnBindBatch">批量解除绑定</el-button>
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
    import MessageBox from 'components/MessageBox';
    import Pagination from 'mixins/Pagination';

    export default {
        mixins: [Pagination],
        props: {
            principalId: [String, Number]
        },
        data: function() {
            return {
                action: '/bind-financial-contract/query',
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
                selecteds: []
            }
        },
        computed: {
            conditions: function() {
                return Object.assign({ 
                    principalId: this.principalId,
                    bindState: 1
                }, this.pageConds);
            }
        },
        methods: {
            onRequest: function(options) {
                if (!options.data || !options.data.principalId) {
                    return true;
                }
                this.dataSource.fetching = true;
            },
            handleSelectionChange: function(rows) {
                this.selecteds = rows.map(row => row.id);
            },
            handleUnBind: function(id) {
                MessageBox.open('确认解除绑定？', null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        this.unBind([id])
                            .then(() => {
                                MessageBox.once('closed', () => this.fetch());
                                MessageBox.close();
                            })
                            .catch(message => {
                                MessageBox.open(message);
                            });
                    }
                }]);
            },
            handleUnBindBatch: function() {
                if (!this.selecteds.length) return;

                MessageBox.open('确认解除绑定？', null, [{
                    text: '取消',
                    handler: () => MessageBox.close()
                }, {
                    text: '确定',
                    type: 'success',
                    handler: () => {
                        this.unBind(this.selecteds)
                            .then(() => {
                                MessageBox.once('closed', () => this.fetch());
                                MessageBox.close();
                            })
                            .catch(message => {
                                MessageBox.open(message);
                            });    
                    }
                }]);
            },
            unBind: function(ids) {
                return ajaxPromise({
                    url: `/bind-financial-contract/unbind`,
                    type: 'post',
                    data: {
                        principalId: this.$route.params.id,
                        financialContractIds: JSON.stringify(ids)
                    }
                });
            }
        }

    }
</script>