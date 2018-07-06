<style lang="sass">
    
</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '凭证批次列表'}, {title: '凭证批次详情'} ]">
            </Breadcrumb>

            <div class="col-layout-detail">
            	<div class="top">
            		<div class="block">
            			<h5 class="hd">批次信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>批次编号 : {{ batchInfo.batchUuid }}</p>
                                <p>来源编号 : {{ batchInfo.requestNo }}</p>
                                <p>项目名称 : {{ batchInfo.financialContractNo }}</p> 
                                <p>导入时间 : {{ batchInfo.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
            				</div>
            			</div>
            		</div>
            	</div>
            </div>

            <div class="row-layout-detail">
            	<div class="block" key="system" >
            		<h5 class="hd">凭证明细</h5>
            		<div class="bd">
            			<el-table 
                            class="td-15-padding th-8-15-padding no-th-border"
                            border
                            stripe
                            v-loading="dataSource.fetching"
                            :data="dataSource.list">
                            <el-table-column 
                                inline-template
                                :context="_self"
                                label="凭证编号">
                                <a :href="`${ctx}#/capital/voucher/third-pay/history-voucher/${row.voucherUuid}/detail`">{{ row.voucherUuid }}</a>
                            </el-table-column>
                            <el-table-column prop="receivableAccountNo" label="清算账号"></el-table-column>
                            <el-table-column prop="paymentBank" label="来往机构"></el-table-column>
                            <el-table-column prop="paymentName" label="机构账户名"></el-table-column>
                            <el-table-column prop="paymentAccountNo" label="机构账户号"></el-table-column>
                            <el-table-column inline-template label="凭证余额">
                                <div>{{ row.transcationAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column prop="voucherSource" label="凭证来源"></el-table-column>
                            <el-table-column prop="transcationGateway" label="交易网关"></el-table-column>
                            <el-table-column prop="voucherLogStatus" label="校验状态" inline-template>
                                <span :class="{
                                    'color-danger': row.voucherLogStatus === '校验成成功'
                                }">{{ row.voucherLogStatus }}</span>
                            </el-table-column>
                            <el-table-column prop="voucherLogIssueStatus" label="核销状态"></el-table-column>
                        </el-table>
            		</div>
                    <div class="ft">
                        <PageControl 
                            v-model="pageConds.pageIndex"
                            :size="dataSource.size"
                            :per-page-record-number="pageConds.perPageRecordNumber">
                        </PageControl>
                    </div>
            	</div>
            </div>
        </div>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import Pagination from 'mixins/Pagination';

    export default{
        mixins: [Pagination],
        data: function() {
            return {
                fetching: false,
                batchInfo: {},

                // 分页
                autoload: false,
                pageConds: {
                    pageIndex: 1,
                    perPageRecordNumber: 12
                }
            }
        },
        computed: {
            action: function() {
                return `/voucher/thirdPartyPayApi/list-batchs/vouchers/${this.$route.params.id}`;
            },
            conditions: function() {
                return Object.assign({}, this.pageConds);
            }
        },
        activated: function() {
            this.pageConds.pageIndex = 1;
            this.fetchDetail(this.$route.params.id);
            this.fetch(); // page list
        },
        methods: {
            fetchDetail: function(journalVoucherUuid) {
                this.fetching = true;

                ajaxPromise({
                    url: `/voucher/thirdPartyPayApi/detail-batchs/${journalVoucherUuid}`
                }).then(data => {
                   this.batchInfo = data.batchInfo;
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            }
        }
    }
</script>
