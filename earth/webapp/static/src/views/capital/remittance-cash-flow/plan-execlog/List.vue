<template>
    <div class="content">
        <div class="scroller">
            <div class="query-area">
                <el-form class="sdf-form sdf-query-form" :inline="true">
                    <el-form-item>
                        <list-cascader
                            clearable
                            size="small"
                            :collection="financialContractQueryModels"
                            v-model="queryConds.financialContractUuids"
                            :placeholder="$utils.locale('financialContract')">
                        </list-cascader>
					</el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.remittanceChannel"
							placeholder="放款通道"
							size="small"
							clearable>
							<el-option
								v-for="item in remittanceChannel"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
                    <el-form-item>
                        <DatePicker
                            v-model="createdTimeRange"
                            size="small"
                            type="datetimerange"
                            placeholder="创建时间">
                        </DatePicker>
                    </el-form-item>
                    <el-form-item>
                        <DatePicker
                            v-model="statusModifyTimeRange"
                            size="small"
                            type="datetimerange"
                            placeholder="状态变更时间">
                        </DatePicker>
                    </el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.executionStatusAndTransactionRecipients"
                            placeholder="代付状态"
							size="small"
                            multiple
							clearable>
                            <el-select-all-option
                                :options="executionStatus">
                            </el-select-all-option>
							<el-option
								v-for="(label, value) in executionStatus"
								:label="label"
								:value="value">
							</el-option>
						</el-select>
					</el-form-item>
                    <el-form-item>
                        <el-select
                            v-model="queryConds.reverseStatusOrdinals"
                            placeholder="冲账状态"
                            size="small"
                            multiple
                            clearable>
                            <el-select-all-option
                                :options="reverseStatus">
                            </el-select-all-option>
                            <el-option
                                v-for="item in reverseStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="代付单号" value="execLogUuid"></el-option>
							<el-option label="放款编号" value="planUuid"></el-option>
							<el-option label="付款方账户名" value="payerAccountHolder"></el-option>
                            <el-option label="收款方账户名" value="cpBankAccountHolder"></el-option>
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
                    v-loading="dataSource.fetching"
                    @sort-change="onSortChange"
                    :data="dataSource.list"
                    stripe>
                    <el-table-column label="代付单号" inline-template>
                        <a :href="`${ctx}#/capital/remittance-cash-flow/plan-execlog/${ row.execReqNo }/detail`">{{ row.execReqNo }}</a>
                    </el-table-column>
                    <el-table-column :label="execlogTypeText+ '编号'" inline-template>
                        <div>
                            <a v-if="remittanceMode" :href="`${ctx}#/data/remittance/plan/${ row.remittancePlanUuid }/detail`">{{ row.remittancePlanUuid }}</a>
                            <a v-else-if="transferMode" :href="`${ctx}#/capital/special-account/transfer/${ row.transferNo }/detail`">{{ row.transferNo }}</a>
                            <span v-else></span>
                        </div>
                    </el-table-column>
                    <el-table-column label="付款方账户名" inline-template>
                        <div>
                            <span>{{ row.pgAccountInfo.accountName }}</span>
                            <el-popover
                                v-if="row.pgAccountInfo.accountName"
                                trigger="click"
                                placement="top">
                                <div>
                                    <template>
                                        <div>账户名：{{ row.pgAccountInfo.accountName }}</div>
                                        <div>账户号：{{ row.pgAccountInfo.accountNo }}</div>
                                        <div>开户行：{{ row.pgAccountInfo.bankName }}</div>
                                        <div>所在地：{{ row.pgAccountInfo.province }}&nbsp;{{ row.pgAccountInfo.city }}</div>
                                        <div>证件号：{{ row.pgAccountInfo.desensitizationIdNumber }}</div>
                                    </template>
                                </div>
                                <i slot="reference" class="icon icon-bankcard"></i>
                            </el-popover>
                        </div>
                    </el-table-column>
                    <el-table-column label="收款方账户名" inline-template>
                        <div>
                            <span>{{ row.cpAccountInfo.accountName }}</span>
                            <el-popover
                                v-if="row.cpAccountInfo.accountName"
                                trigger="click"
                                placement="top">
                                <div>
                                    <template>
                                        <div>账户名：{{ row.cpAccountInfo.accountName }}</div>
                                        <div>账户号：{{ row.cpAccountInfo.accountNo }}</div>
                                        <div>开户行：{{ row.cpAccountInfo.bankName }}</div>
                                        <div>所在地：{{ row.cpAccountInfo.province }}&nbsp;{{ row.cpAccountInfo.city }}</div>
                                        <div>证件号：{{ row.cpAccountInfo.desensitizationIdNumber }}</div>
                                    </template>
                                </div>
                                <i slot="reference" class="icon icon-bankcard"></i>
                            </el-popover>
                        </div>
                    </el-table-column>
                    <el-table-column :label="execlogTypeText+ '金额'" prop="plannedAmount" inline-template sortable="custom">
                        <div>{{ row.plannedAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column :label="remittanceMode? '放款通道': transferMode? '交易网关': ''" prop="remittanceChannel">
                    </el-table-column>
                    <el-table-column label="创建时间" inline-template sortable="custom">
                        <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" inline-template sortable="custom">
                        <div>{{ row.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="执行时差" prop="executedTimeOff"></el-table-column>
                    <el-table-column label="代付状态" inline-template>
                        <div :class="{
                            'color-warning': row.executionStatus == '异常',
                            'color-danger': row.executionStatus == '失败'
                        }">{{ row.executionStatus }}</div>
                    </el-table-column>
                    <el-table-column label="冲账状态" inline-template>
                        <div :class="{
                        'color-danger': row.reverseStatus == '未冲账'
                        }">{{ row.reverseStatus }}</div>
                    </el-table-column>
                    <el-table-column label="备注" prop="executionRemark"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <div class="pull-left">
                <el-button @click="$router.push({path: '/capital/remittance-cash-flow/plan-execlog/merge', query: {t: new Date().getTime()}})" v-if="remittanceMode">合并退票</el-button>
                <el-button @click="changeExeclogType">{{ execlogTypeWillChangeText }}</el-button>
            </div>
            <div class="pull-right">
                <ListStatistics 
                    action="/capital/plan/execlog/amountStatistics"
                    :parameters="conditions">
                    <template scope="statistics">
                        <div>
                            放款金额<span class="pull-right">{{ statistics.data.remittanceAmount | formatMoney }}</span>
                        </div>
                    </template>
                </ListStatistics>
                <PageControl 
                    ref="pageControl"
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                    <template scope="pageStatistics" slot="statistics">
                        共
                        <el-popover
                            @show="fetchDataStatistics"
                            popper-class="text-align-center"
                            :width="210"
                            placement="top"
                            trigger="click">
                            <div v-if="dataStatistics.error">
                                {{ dataStatistics.error }}
                            </div>
                            <div v-else-if="dataStatistics.size == 0">
                                没有统计数据
                            </div>
                            <div v-else>
                                <div class="title" v-if="dataStatistics.size >= 5">统计数量前5的银行</div>
                                <div class="content text-align-left">
                                    <div v-for="(value, key) in dataStatistics.dataMap">
                                        <span>{{key}}</span>
                                        <span class="pull-right">{{value}}条</span>
                                    </div>
                                </div>
                            </div>
                            <a href="javascript: void 0;" slot="reference">{{ pageStatistics.data.size }}</a>
                        </el-popover>
                        条
                    </template>
                </PageControl>
            </div>
        </div>
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise, searchify, purify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import format from 'filters/format';
    import DatePicker from 'components/DatePicker'

    export default {
        mixins: [Pagination, ListPage],
        components: { 
            HelpPopover: require('views/include/HelpPopover'),
            ComboQueryBox: require('views/include/ComboQueryBox'),
            ListStatistics: require('views/include/ListStatistics'),
            DatePicker
        },
        data: function() {
        	return {
        		action: '/capital/plan/execlog/query',
                exportQuery: {
                    startDate: '',
                    endDate: '',
                    financialContractUuids: []
                },
        		pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
        		queryConds: {
                    financialContractUuids: [],
                    remittanceChannel: '',
                    createTimeStart: '',
                    createTimeEnd: '',
                    statusModifyTimeStart: '',
                    statusModifyTimeEnd: '',
                    executionStatusAndTransactionRecipients: [],
                    reverseStatusOrdinals: [],
                    execlogType: 0//放款
                },
        		comboConds: {
                    execLogUuid: '',
                    planUuid: '',
                    payerAccountHolder: '',
                    cpBankAccountHolder: '',
                },
        		sortConds: {
                    sortField: '',
                    isAsc: ''
                },
                dataStatistics: {
                    error: '',
                    dataMap: {},
                    size: 0
                },

        		financialContractQueryModels: [],
        		remittanceChannel: [],
        		executionStatus: [],
                reverseStatus: [],

                createdTimeRange: [],
                statusModifyTimeRange: [],
                execlogType: ['放款', '转账']
        	};
        },
        computed: {
            execlogTypeText: function(){
                return this.execlogType[this.queryConds.execlogType]
            },
            execlogTypeWillChangeText: function(){
                return this.execlogType[(this.queryConds.execlogType + 1) % 2]
            },
            remittanceMode: function(){
                return this.queryConds.execlogType == 0
            },
            transferMode: function(){
                return this.queryConds.execlogType == 1
            }
        },
        watch: {
            createdTimeRange: function(current){
                this.queryConds.createTimeStart = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.createTimeEnd = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            },
            statusModifyTimeRange: function(current){
                this.queryConds.statusModifyTimeStart = format.formatDate(current[0], 'yyyy-MM-dd HH:mm:ss');
                this.queryConds.statusModifyTimeEnd = format.formatDate(current[1], 'yyyy-MM-dd HH:mm:ss');
            }
        },
        methods: {
            fetchDataStatistics: function() {
                ajaxPromise({
                    url: '/capital/plan/execlog/dataStatistics',
                    data: purify(this.conditions)
                }).then(data => {
                    this.dataStatistics.size = data.size;
                    this.dataStatistics.dataMap = data.dataMap;
                    this.dataStatistics.error = '';
                }).catch(message => {
                    this.dataStatistics.error = message;
                });
            },
            initialize: function() {
                return ajaxPromise({
                    url: `/capital/plan/execlog/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.remittanceChannel = data.remittanceChannel || [];
                    this.executionStatus = data.executionStatus || [];
                    this.reverseStatus = data.reverseStatus || [];

                    this.queryConds.reverseStatusOrdinals = data.reverseStatus.map(item => item.key);
                    this.queryConds.executionStatusAndTransactionRecipients = Object.keys(data.executionStatus);

                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            changeExeclogType: function(){
                // changeExeclogType: [0:放款，1：转账]
                this.queryConds.execlogType = (this.queryConds.execlogType + 1) % 2
                this.dataSource = Object.assign(this.dataSource,{
                    size: 0,
                    fetching: false,
                    list: [],
                    error: ''
                })
                this.dataStatistics = Object.assign(this.dataStatistics,{
                    error: '',
                    dataMap: {},
                    size: 0
                })
            }
        }
    }
</script>