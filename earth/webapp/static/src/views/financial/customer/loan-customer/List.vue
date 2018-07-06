<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <el-select
                            size="small"
                            v-model="queryConds.customerStyleOrdinal"
                            @change="queryConds.idTypeOrdinal = ''"
                            placeholder="客户类型">
                            <el-option
                                v-for="item in customerStyle"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            size="small"
                            v-model="queryConds.idTypeOrdinal"
                            clearable 
                            placeholder="证件类型">
                            <el-option
                                v-for="item in idTypes"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select
                            size="small"
                            v-model="queryConds.customerStatus"
                            placeholder="客户状态"
                            clearable>
                            <el-option
                                v-for="item in customerStatus"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option value="customerName" label="贷款客户姓名"></el-option>
                            <el-option value="customerUuid" label="贷款客户编号"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" type="primary" @click="exportModal.show = true">导出</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column width="180" inline-template label="客户编号">
                        <div>
                            <a v-if="row.status" :href="`${ctx}#/financial/customer/loan-customer/${row.customerUuid}/detail?customerStyle=${row.customerStyle}`">{{row.customerUuid}}</a>
                            <span v-else>{{ row.customerUuid }}</span>
                        </div>
                    </el-table-column>
                    <el-table-column label="客户名称" prop="name"></el-table-column>
                    <el-table-column label="客户类型" prop="customerStyle"></el-table-column>
                    <el-table-column label="证件类型" prop="idType"></el-table-column>
                    <el-table-column label="证件号" prop="idNumber"></el-table-column>
                    <el-table-column label="银行账号" prop="accountNo"></el-table-column>
                    <el-table-column label="账户开户行" prop="bankName"></el-table-column>
                    <el-table-column label="开户行所在省" prop="province"></el-table-column>
                    <el-table-column label="开户行所在市" prop="city"></el-table-column>
                    <el-table-column label="状态" inline-template>
                        <div>
                            <span v-if="!row.status" style="color: red;">异常</span>
                            <span v-else>正常</span>
                        </div>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-right">
                <PageControl
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>

        <ExportPreviewModal
            :parameters="conditions"
            :query-action="`/customer/preview-exprot-customer`"
            :download-action="`/customer/export`"
            :handlerExport="true"
            @handlerExport="handlerExport"
            v-model="exportModal.show">
            <el-table-column prop="customerUuid" label="客户编号">
            </el-table-column>
            <el-table-column prop="name" label="客户名称">
            </el-table-column>
            <el-table-column prop="customerStyle" label="客户类型">
            </el-table-column>
            <el-table-column prop="idType" label="证件类型">
            </el-table-column>
            <el-table-column prop="idNumber" label="证件号">
            </el-table-column>
             <el-table-column prop="accountNo" label="银行账号">
            </el-table-column>
            <el-table-column prop="bankName" label="账户开户行">
            </el-table-column>
            <el-table-column prop="province" label="开户行所在省">
            </el-table-column>
            <el-table-column prop="city" label="开户行所在市">
            </el-table-column>
            <el-table-column prop="statusMsg" label="状态">
            </el-table-column>
        </ExportPreviewModal>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise, downloadFile, purify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
            ExportPreviewModal: require('views/include/ExportPreviewModal')
        },
        data: function() {
            return {
                action: `/customer/search`,
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    customerStyleOrdinal: '',
                    idTypeOrdinal: '',
                    customerStatus: true
                },
                comboConds: {
                    customerName: '',
                    customerUuid: '',
                },

                customerStyle: [],
                IDType: [],
                customerStatus: [{
                    label: '正常',
                    value: true,
                },{
                    label: '异常',
                    value: false,
                }],
                exportModal: {
                    show: false
                }
            };
        },
        activated: function () {
            this.initialize();  
        },
        computed: {
            idTypes: function() {
                var { IDType } = this;
                if(this.queryConds.customerStyleOrdinal === 0) {
                    return IDType.slice(0, 5);
                } else if(this.queryConds.customerStyleOrdinal === 1) {
                    return IDType.slice(5);
                } else {
                    return IDType;
                }
            },
        },
        methods: {
            initialize: function() {
                return this.getOptions();
            },
            getOptions: function() {
                return ajaxPromise({
                    url: `/customer/optionData`
                }).then(data => {
                    this.customerStyle = data.customerStyle || [];
                    this.IDType = data.IDType || [];
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            handlerExport: function() {
                let rqData = Object.assign({}, this.queryConds, this.comboConds);
                ajaxPromise({
                    url: `/customer/export/limit`,
                    data: purify(rqData)
                }).then(data => {
                    downloadFile('/customer/export', rqData);
                }).catch(message => {
                    MessageBox.open(message);
                });
            }
        }
    }
</script>