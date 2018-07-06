<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <DatePicker
                            v-model="CreateTime"
                            size="small"
                            type="datetimerange"
                            placeholder="消息接收时间">
                        </DatePicker>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.source" 
                            placeholder="来源" 
                            clearable
                            size="small">
                            <el-option
                                v-for="item in source"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.status" 
                            placeholder="状态" 
                            clearable
                            size="small">
                            <el-option
                                v-for="item in status"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <ComboQueryBox v-model="comboConds">
                            <el-option label="消息编号" value="messageUuid"></el-option>
                            <el-option label="公司" value="companyName"></el-option>
                            <el-option label="名称" value="name"></el-option>
                        </ComboQueryBox>
                    </el-form-item>
                    <el-form-item>
                        <el-button size="small" ref="lookup" type="primary">查询</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <div class="table-area">
                <el-table 
                    class="no-table-bottom-border"
                    stripe
                    v-loading="dataSource.fetching"
                    :data="dataSource.list">
                    <el-table-column label="消息编号" prop="messageUuid" inline-template>
                        <a href="#" @click.prevent="showInformationModal(row)">{{ row.messageUuid }}</a>
                    </el-table-column>
                    <el-table-column label="消息接收时间" prop="createTime" inline-template>
                        <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
                    </el-table-column>
                    <el-table-column label="来源" prop="sourceStr"></el-table-column>
                    <el-table-column label="公司" prop="companyName"></el-table-column>
                    <el-table-column label="部门" prop="deptName"></el-table-column>
                    <el-table-column label="名称" prop="name"></el-table-column>
                    <el-table-column label="处理状态" prop="statusStr"></el-table-column>
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

        <InforMationModal
            v-model="inforMationModal.show"
            :processed="inforMationModal.processed"
            @refresh="fetch"
            :messageUuid="inforMationModal.messageUuid">
        </InforMationModal>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';
    import format from 'filters/format';
    import DatePicker from 'components/DatePicker';

    export default {
        mixins: [Pagination, ListPage],
        components: { 
            ComboQueryBox: require('views/include/ComboQueryBox'),
            InforMationModal: require('./include/InformationModal'),
            DatePicker
        },
        data: function() {
            return {
                action: `/messages`,
                CreateTime: [],
                source: [],
                status: [],

                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                comboConds: {
                    companyName: '',
                    messageUuid: '',
                    name: '',
                },
                queryConds: {
                    startCreateTime: '',
                    endCreateTime: '',
                    source: '',
                    status: ''
                },
                inforMationModal: {
                    show: false,
                    processed: false,
                    messageUuid: ''
                }
            };
        },
        watch: {
            CreateTime: function(current) {
                this.queryConds.startCreateTime = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.endCreateTime = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            }
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: `/messages/data`,
                }).then(data => {
                    this.status = data.status || [];
                    this.source = data.source || [];
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            showInformationModal: function(row) {
                var { inforMationModal } = this;
                inforMationModal.show = true;
                inforMationModal.processed = row.status == 1;
                inforMationModal.messageUuid = row.messageUuid;
            }
        }
    }
</script>
