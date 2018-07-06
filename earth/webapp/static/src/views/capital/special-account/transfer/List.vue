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
                        <el-date-picker
                            type="datetime"
                            size="small"
                            v-model="createTime"
                            placeholder="创建时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item>
                        <el-date-picker
                            type="datetime"
                            size="small"
                            v-model="lastModifyTime"
                            placeholder="状态变更时间">
                        </el-date-picker>
                    </el-form-item>
					<el-form-item>
						<el-select
							v-model="queryConds.executionStatus"
                            placeholder="执行状态"
							size="small"
                            multiple
							clearable>
                            <el-select-all-option
                                :options="executionStatus">
                            </el-select-all-option>
							<el-option
								v-for="item in executionStatus"
								:label="item.value"
								:value="item.key">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<ComboQueryBox v-model="comboConds">
							<el-option label="五维转账单号" value="orderUuid"></el-option>
							<el-option label="商户转账单号" value="orderNo"></el-option>
							<el-option label="收款方名称" value="cpAccountName"></el-option>
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
                    :data="dataSource.list"
                    stripe>
                    <el-table-column label="产品代码" inline-template>
                        <a >{{ row.financialContractNo }}</a>
                    </el-table-column>
                    <el-table-column label="商户转账单号" prop="orderNo">
                    </el-table-column>
                    <el-table-column label="五维转账单号" inline-template>
                        <a :href="`${ctx}#/capital/special-account/transfer/${row.orderUuid}/detail`">{{ row.orderUuid }}</a>
                    </el-table-column>
                    <el-table-column label="付款方信息" inline-template>
                        <div>
                            <span>{{ row.accountNo }}</span>
<!--                             <el-popover
                                v-if="row.accountNo"
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
                            </el-popover> -->
                        </div>
                    </el-table-column>
                    <el-table-column label="收款方信息" inline-template>
                        <div>
                            <span>{{ row.cpBankAccountHolder }}</span>
<!--                             <el-popover
                                v-if="row.cpBankAccountHolder"
                                trigger="click"
                                placement="top">
                                <div>
                                    <template>
                                        <div>账户名：{{ row.cpBankAccountHolder }}</div>
                                        <div>账户号：{{ row.cpBankAccountHolder }}</div>
                                        <div>开户行：{{ row.cpBankAccountHolder }}</div>
                                        <div>所在地：{{ row.cpBankAccountHolder }}&nbsp;{{ row.cpBankAccountHolder }}</div>
                                        <div>证件号：{{ row.cpBankAccountHolder }}</div>
                                    </template>
                                </div>
                                <i slot="reference" class="icon icon-bankcard"></i>
                            </el-popover> -->
                        </div>
                    </el-table-column>
                    <el-table-column label="转账金额" inline-template>
                        <div>{{ row.plannedTotalAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="创建时间" inline-template>
                        <div>{{ row.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="状态变更时间" inline-template>
                        <div>{{ row.lastModifyTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column label="执行状态" inline-template>
                        <div class="{
                            'color-warning': false,
                            'color-danger': false
                        }">{{ row.executionStatusName }}</div>
                    </el-table-column>
                    <el-table-column label="备注" prop="remark"></el-table-column>
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
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise, searchify, purify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import format from 'filters/format';

    export default {
        mixins: [Pagination, ListPage],
        components: {
            ComboQueryBox: require('views/include/ComboQueryBox'),
        },
        data: function() {
        	return {
        		action: '/transfer/query',
        		pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
        		queryConds: {
                    financialContractUuids: [],
                    createTime: '',
                    lastModifyTime: '',
                    executionStatus: [],
                },
                createTime: '',
                lastModifyTime: '',
        		comboConds: {
                },
        		financialContractQueryModels: [],
                executionStatus: [],
        	};
        },
        watch : {
        	createTime: function(v){
        		this.queryConds.createTime = format.formatDate(v, 'yyyy-MM-dd HH:mm:ss')
        	},
        	lastModifyTime: function(v){
        		this.queryConds.lastModifyTime = format.formatDate(v, 'yyyy-MM-dd HH:mm:ss')
        	}
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: `/transfer/options`
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || []
                    this.executionStatus = data.executionStatus || []
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
        }
    }
</script>