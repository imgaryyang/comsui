<style lang="sass">
    @import '~assets/stylesheets/base.scss';

    #editBusinessModal {
        @include min-screen(768px) {
            .modal-dialog {
                width: 750px;
            }
        }
    }
</style>

<template>
    <Modal 
        v-model="visible" 
        id="editBusinessModal"
        @closed="$emit('closed')">
        <ModalHeader title="业务绑定"></ModalHeader>
        <ModalBody align="left">
            <div class="row-layout-detail" style="padding: 0;">
                <div class="query-area" style="border-bottom: none; background: none; padding-left: 0;">
                    <el-form class="sdf-form sdf-query-form" :inline="true">
                        <el-form-item>
                            <el-input 
                                :value="queryConds.financialContractNo"
                                @change.native="queryConds.financialContractNo = $event.target.value.trim()"
                                :placeholder="$utils.locale('financialContract.no')"
                                size="small"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-input 
                                :value="queryConds.financialContractName"
                                @change.native="queryConds.financialContractName = $event.target.value.trim()"
                                :placeholder="$utils.locale('financialContract.name')"
                                size="small"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-select 
                                v-model="queryConds.appId" 
                                :placeholder="$utils.locale('financialContract.appAccount')" 
                                clearable
                                size="small">
                                <el-option
                                    v-for="item in appList"
                                    :label="item.name"
                                    :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-select 
                                v-model="queryConds.financialContractType" 
                                :placeholder="$utils.locale('financialContract.type')" 
                                clearable
                                size="small">
                                <el-option
                                    v-for="item in financialContractTypeList"
                                    :label="item.value"
                                    :value="item.key">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button size="small" ref="lookup" type="primary">查询</el-button>
                        </el-form-item>
                    </el-form>
                </div>
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
                                <a href="javascript: void 0;" @click="handleBind(row.id)">绑定</a>
                            </el-table-column>
                        </el-table>
                    </div>
                    <div class="ft clearfix">
                        <el-button size="small" type="text" @click="handleBindBatch">批量绑定</el-button>
                        <PageControl 
                            v-model="pageConds.pageIndex"
                            float="right"
                            :size="dataSource.size"
                            :per-page-record-number="pageConds.perPageRecordNumber">
                        </PageControl>
                    </div>
                </div>
            </div>
        </ModalBody>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import Pagination from 'mixins/Pagination';

    export default {
        mixins: [Pagination],
        props: {
            value: Boolean,
            principalId: String,
            appList: Array,
            financialContractTypeList: Array
        },
        data: function() {
            return {
                visible: this.value,
                autoload: false,
                action: '/bind-financial-contract/query',
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                },
                queryConds: {
                    financialContractNo: '',
                    financialContractName: '',
                    financialContractType: '',
                    appId: ''
                },
                selecteds: []
            }
        },
        computed: {
            conditions: function() {
                return Object.assign({
                    principalId: this.principalId,
                    bindState: 0
                }, this.queryConds, this.pageConds);
            }
        },
        watch: {
            value: function(current) {
                this.visible = current;
            },
            visible: function(current) {
                if (current) {
                    this.fetch();
                }
                this.$emit('input', current);
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
            handleBind: function(id) {
                this.bind([id])
                    .then(() => {
                        this.fetch();
                        this.$emit('bind');
                    })
                    .catch(message => {
                        MessageBox.open(message);
                    });
            },
            handleBindBatch: function() {
                if (!this.selecteds.length) return;

                this.bind(this.selecteds)
                    .then(() => {
                        this.fetch();
                        this.$emit('bind-batch');
                    })
                    .catch(message => {
                        MessageBox.open(message);
                    });
            },
            bind: function(ids) {
                return ajaxPromise({
                    url: `/bind-financial-contract/bind`,
                    type: 'post',
                    data: {
                        principalId: this.principalId,
                        financialContractIds: JSON.stringify(ids)
                    }
                });
            }
        }
    }
</script>
