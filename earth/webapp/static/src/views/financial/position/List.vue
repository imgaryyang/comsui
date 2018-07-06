<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <el-select
                            v-model="queryConds.appId"
                            size="small"
                            clearable
                            placeholder="资产方">
                            <el-option
                                v-for="item in appList"
                                :label="item.name"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <span class="item vertical-line"></span>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option value="financialContractNo" :label="$utils.locale('financialContract.no')"></el-option>
                            <el-option value="financialContractName" :label="$utils.locale('financialContract.name')"></el-option>
                            <el-option value="positionName" label="项目职务"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button ref="lookup" size="small" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column :label="$utils.locale('financialContract.no')" prop="financialContractNo"></el-table-column>
                    <el-table-column :label="$utils.locale('financialContract.name')" prop="financialContractName"></el-table-column>
                    <el-table-column label="资产方" prop="appName"></el-table-column>
                    <el-table-column label="五维负责人" prop="positionNameOne"></el-table-column>
                    <el-table-column label="云信投运对接人" prop="positionNameTwo"></el-table-column>
                    <el-table-column label="贷前运营" prop="positionNameThree"></el-table-column>
                    <el-table-column label="贷前技术" prop="positionNameFour"></el-table-column>
                    <el-table-column label="云信信托经理" prop="positionNameFive"></el-table-column>
                    <el-table-column label="云信财务" prop="positionNameSix"></el-table-column>
                    <el-table-column label="操作" inline-template>
                        <span style="color:#436ba7;cursor:pointer;" @click.prevent="getDetailData(row.financialContractUuid)">详情</span>
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
        <DetailModal
            v-model="detailModal.visible"
            :principalShowList="principalShowList"
            :model="detailModal.model"
            @submit="handleSubmit">
        </DetailModal>
    </div>
</template>
<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
            DetailModal: require('./DetailModal'),
        },
        data: function() {
            return {
                action: '/position/query',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    appId: ''
                },
                comboConds: {
                    financialContractNo: '',
                    financialContractName: '',
                    positionName: ''
                },

                appList: [],
                principalShowList: [],

                detailModal: {
                    visible: false,
                    fetching: false,
                    model: {
                        financialContractNo: '',
                        financialContractName: '',
                        financialContractUuid: '',
                        list: []
                    }
                },
            }
        },
        activated: function() {
            this.getOptions();
            this.getPrincipalList();
        },
        methods: {
            getOptions: function() {
                return ajaxPromise({
                    url: `/financialContract/optionData`
                }).then(data => {
                    this.financialContractTypeList = data.financialContractTypeList || [];
                    this.appList = data.appList || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            getPrincipalList: function() {
                ajaxPromise({
                    url: `/position/get-principal`
                }).then(data => {
                    this.principalShowList = data.principalShowList || [];
                }).catch(message => {
                    MessageBox.open(message);
                })
            },
            getDetailData: function(financialContractUuid) {
                ajaxPromise({
                    url: `/position/detail/data`,
                    data: {
                        financialContractUuid
                    }
                }).then(data => {
                    this.detailModal.model.financialContractNo = data.financialContractNo;
                    this.detailModal.model.financialContractName = data.financialContractName;
                    this.detailModal.model.financialContractUuid = financialContractUuid;
                    this.detailModal.model.list = data.list || [];
                    this.detailModal.visible = true;
                }).catch(message => {
                    MessageBox.open(message)
                })
            },
            handleSubmit: function(financialContractUuid) {
                this.getDetailData(financialContractUuid);
            }
        }
    }
</script>