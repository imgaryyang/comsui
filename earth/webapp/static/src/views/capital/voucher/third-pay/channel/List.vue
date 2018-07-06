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
                            v-model="queryConds.paymentGateway"
                            size="small" 
                            clearable
                            placeholder="交易网关">
                            <el-option
                                v-for="item in paymentInstitutionNameList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.startTime"
                                    :end-date="queryConds.endTime"
                                    :pickTime="true"
                                    :formatToMinimum="true"
                                    size="small"
                                    placeholder="交易起始时间">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <div class="text-align-center color-dim">至</div>
                        </el-col>
                        <el-col :span="11">
                            <el-form-item>
                                <DateTimePicker
                                    v-model="queryConds.endTime"
                                    :start-date="queryConds.startTime"
                                    :pickTime="true"
                                    :formatToMaximum="true"
                                    size="small"
                                    placeholder="交易终止时间">
                                </DateTimePicker>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.businessProcessStatus"
                            size="small" 
                            clearable
                            placeholder="交易状态">
                            <el-option
                                v-for="item in businessProcessStatus"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select 
                            v-model="queryConds.matchStatus"
                            size="small" 
                            clearable
                            placeholder="匹配状态">
                            <el-option
                                v-for="item in matchStatusList"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
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
                    <el-table-column prop="cashFlowMerchantNo" label="通道请求号"></el-table-column>
                    <el-table-column prop="transcationGatewayName" label="交易网关"></el-table-column>
                    <el-table-column prop="transCmdAmount" label="交易金额" inline-template>
                        <div>{{ row.transCmdAmount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column prop="transactionTime" label="交易时间" inline-template>
                        <div>{{ row.transactionTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
                    </el-table-column>
                    <el-table-column prop="transExcutionStatusName" label="交易状态"></el-table-column>
                    <el-table-column prop="transcationVoucherUuid" label="凭证编号" inline-template>
                        <a :href="`${ctx}#/capital/voucher/third-pay/history-voucher/${row.transcationVoucherUuid}/detail`">{{ row.transcationVoucherUuid }}</a>
                    </el-table-column>
                    <el-table-column prop="matchStatusName" label="匹配状态"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="operations">
            <!-- <div class="pull-left">
                <el-button size="small" @click="updateModal.show = true">
                    更新交易记录
                </el-button>
            </div> -->
            <div class="pull-right">
                <PageControl 
                    v-model="pageConds.pageIndex"
                    :size="dataSource.size"
                    :per-page-record-number="pageConds.perPageRecordNumber">
                </PageControl>
            </div>
        </div>

        <!-- <Modal v-model="updateModal.show">
            <ModalHeader title="更新交易记录"></ModalHeader>
            <ModalBody align="left">
                <el-form
                    :model="updateModal.model"
                    ref="form"
                    class="sdf-form"
                    :style="{'margin-left': '20px'}"
                    label-width="145px">
                    <el-form-item prop="" label="请选择项目">
                        <el-select 
                            class="middle" 
                            v-model="updateModal.model">
                            <el-option 
                                v-for="item in []" 
                                :label="item.contractName + '(' + item.contractNo + ')'"
                                :value="item.financialContractUuid">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="capitalAccountNo" label="请选择网关">
                        <el-select 
                            v-model="updateModal.model"
                            class="middle" 
                            :dropdown-class="'richtext-dropdown'">
                            <el-option 
                                v-for="item in []" 
                                :label="item.accountName"
                                :value="item.accountNo">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </ModalBody>
            <ModalFooter>
                <el-button @click="updateModal.show = false">取消</el-button>
                <el-button type="success">确定</el-button>
            </ModalFooter>
        </Modal> -->
    </div>
</template>

<script>
    import Pagination from 'mixins/Pagination';
    import ListPage from 'mixins/ListPage';
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import formats from 'filters/format';

    export default {
        mixins: [Pagination, ListPage],
        data: function() {
            return {
                action: '/voucher/thirdPartyPayApi/list-transaction-record',
                pageConds: {
                    perPageRecordNumber: 12,
                    pageIndex: 1
                },
                queryConds: {
                    financialContractUuids: [],
                    paymentGateway: '',
                    startTime: '',
                    endTime: '',
                    businessProcessStatus: '',
                    matchStatus: '',
                },

                financialContractQueryModels: [],
                paymentInstitutionNameList: [],
                businessProcessStatus: [],
                matchStatusList: [],

                updateModal: {
                    show: false,
                    model: {}
                }
            };
        },
        methods: {
            initialize: function() {
                return ajaxPromise({
                    url: '/voucher/thirdPartyPayApi/list-transaction-record/options'
                }).then(data => {
                    this.financialContractQueryModels = data.queryAppModels || [];
                    this.paymentInstitutionNameList = data.paymentInstitutionNameList || [];
                    this.businessProcessStatus = data.businessProcessStatus || [];
                    this.matchStatusList = data.matchStatusList || [];

                    this.queryConds.businessProcessStatus = 1;
                    this.queryConds.matchStatus = 0;
                    var d = new Date();
                    d = new Date(d.getFullYear(), d.getMonth(), d.getDate());
                    this.queryConds.startTime = formats.formatDate(d, 'yyyy-MM-dd HH:mm:ss');
                }).catch(message => {
                    MessageBox.open(message);
                });
            }
        }
    }
</script>