<style lang="sass">

</style>

<template>
    <div class="content">
        <div class="scroller" v-loading="fetching">
            <Breadcrumb :routes="[{ title: '计划订单详情'}]">
            </Breadcrumb>

            <div class="col-layout-detail">
            	<div class="top">
            		<div class="block">
            			<h5 class="hd">基本信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>订单编号：{{ remittanceApplication.remittanceApplicationUuid }}</p>
                                <p>商户订单号：{{ remittanceApplication.remittanceId || remittanceApplication.requestNo }}</p>
            					<p>贷款合同编号：<a :href="`${ctx}#/data/contracts/detail?uid=${remittanceApplication.contractUniqueId}`">{{remittanceApplication.contractNo}}</p>
            					<p>{{ $utils.locale('financialContract.no') }}：{{ remittanceApplication.financialProductCode }}</p>
            				</div>
            			</div>
            		</div>
            		<div class="block">
            			<h5 class="hd">放款信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>计划放款金额：{{remittanceApplication.plannedTotalAmount |formatMoney }}</p>
            					<p>放款策略类型：{{remittanceApplication.remittanceStrategyMsg }}</p>
            					<p>放款起始日期：{{remittanceApplication.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                      <p>创建时间：{{remittanceApplication.createTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
                      <p>状态变更时间：{{remittanceApplication.lastModifiedTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</p>
            					<p>订单状态：{{ remittanceApplication.executionStatusMsg }}</p>
                            </div>
                        </div>
            		</div>
            		<div class="block">
            			<h5 class="hd">审核信息</h5>
            			<div class="bd">
            				<div class="col">
            					<p>审核人：{{ remittanceApplication.auditorName }}</p>
            					<p>审核日期：{{ remittanceApplication.auditTime | formatDate}}</p>
            					<p>受理时间：{{ remittanceApplication.createTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</p>
            					<p>计划回调次数：{{remittanceApplication.planNotifyNumber}}&nbsp;
                                    <el-button
                                        v-if="ifElementGranted('update-notify-number') && remittanceApplication.actualNotifyNumber >= remittanceApplication.planNotifyNumber
                                            && remittanceApplication.planNotifyNumber != 0"
                                        type="primary"
                                        size="small"
                                        @click="reCallbackModal.show= true">
                                        重新回调结果
                                    </el-button>
                                </p>
                                <p>实际回调次数 : {{remittanceApplication.actualNotifyNumber}}</p>
            				</div>
            			</div>
            		</div>
            	</div>
            </div>

            <div class="row-layout-detail">

                <div class="block">
                    <h5 class="hd">订单明细</h5>
                    <div class="bd">
                        <el-table
                            :data="remittancePlans"
                            class="td-15-padding th-8-15-padding no-th-border"
                            stripe
                            border>
                            <el-table-column
                                inline-template
                                prop="repaymentPlanNo"
                                label="放款编号"
                                width="300px">
                                <a :href="`${ctx}#/data/remittance/plan/${row.remittancePlanUuid}/detail`">
                                    {{row.remittancePlanUuid}}
                                </a>
                            </el-table-column>
                            <el-table-column prop="priorityLevel" label="放款优先级"></el-table-column>
                            <el-table-column prop="businessRecordNo" label="商户明细号"></el-table-column>
                            <el-table-column inline-template label="计划执行日期">
                                <div>{{ row.plannedPaymentDate | formatDate }}</div>
                            </el-table-column>
                            <el-table-column inline-template label="执行金额">
                                <div>{{ row.plannedTotalAmount | formatMoney }}</div>
                            </el-table-column>
                            <el-table-column
                                inline-template
                                label="付款方账户名">
                                <el-popover trigger="hover" placement="top">
                                    <div>账户名：{{row.pgAccountInfo.accountName}}</div>
                                    <div>账户号：{{row.pgAccountInfo.accountNo}}</div>
                                    <div>开户行：{{row.pgAccountInfo.bankName}}</div>
                                    <div>所在地：{{row.pgAccountInfo.province}} {{row.pgAccountInfo.city}}</div>
                                    <div>证件号：{{row.pgAccountInfo.desensitizationIdNumber}}</div>
                                    <span slot="reference">
                                      {{ row.pgAccountInfo.accountName }}
                                    </span>
                                </el-popover>
                            </el-table-column>
                            <el-table-column
                                inline-template
                                label="收款方账户名">
                                <el-popover trigger="hover" placement="top">
                                    <div>账户名：{{row.cpAccountInfo.accountName}}</div>
                                    <div>账户号：{{row.cpAccountInfo.accountNo}}</div>
                                    <div>开户行：{{row.cpAccountInfo.bankName}}</div>
                                    <div>所在地：{{row.cpAccountInfo.province}} {{row.cpAccountInfo.city}}</div>
                                    <div>证件号：{{row.cpAccountInfo.desensitizationIdNumber}}</div>
                                    <span slot="reference">
                                      {{ row.cpAccountInfo.accountName }}
                                    </span>
                                </el-popover>
                            </el-table-column>
                            <el-table-column
                                inline-template
                                label="交易类型">
                                <span>{{ row.transactionType == 'CREDIT' ? '代付' : '代收' }}</span>
                            </el-table-column>
                            <el-table-column prop="executionStatusMsg" label="执行状态"></el-table-column>
                            <el-table-column prop="executionRemark" label="备注"></el-table-column>
                        </el-table>
                    </div>
                </div>
                <div class="block">
                    <SystemOperateLog
                        ref="sysLog"
                        :for-object-uuid="$route.params.id"></SystemOperateLog>
                </div>
            </div>
        </div>

        <Modal v-model="reCallbackModal.show">
            <ModalHeader title="重新回调结果"></ModalHeader>
            <ModalBody align="left">
                <el-form
                    ref="form"
                    class="sdf-form"
                    :style="{
                        'margin-right': '30px',
                        'margin-left': '30px'
                    }"
                    label-width="120px">
                    <el-form-item style="padding-bottom: 0;">
                        <div>是否重新回调结果？</div>
                    </el-form-item>
<!--                     <el-form-item label="备注" prop="comment">
                        <el-input class="long" type="textarea" placeholder="请输入原因备注" v-model="reCallbackModal.fields.comment"></el-input>
                    </el-form-item> -->
                </el-form>
            </ModalBody>
            <ModalFooter>
                <el-button @click="reCallbackModal.show = false">取消</el-button>
                <el-button type="success" @click="submit">确定</el-button>
            </ModalFooter>
        </Modal>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default{
        components:{
            SystemOperateLog: require('views/include/SystemOperateLog'),
        },
        data: function() {
            return {
                fetching: false,
                remittanceApplication: {},
                remittancePlans: [],

                reCallbackModal: {
                    show:false
                }
            }
        },
        watch: {
            show: function(current) {
                this.$emit('input', current);
                if (current) {
                    this.$refs.form.resetFields();
                }
            },
        },
        activated: function() {
            this.fetch(this.$route.params.id);
        },
        methods: {
            fetch: function(journalVoucherUuid) {
                this.fetching = true;

                ajaxPromise({
                    url: `/remittance/application/details/${journalVoucherUuid}`
                }).then(data => {
                    this.remittanceApplication = data.remittanceApplication;
                    this.remittancePlans = data.remittancePlans;
                })
                .catch(message => {
                    MessageBox.open(message);
                })
                .then(() => {
                    this.fetching = false;
                });
            },
            submit: function() {
                ajaxPromise({
                    url: '/remittance/application/notifyOutlier',
                    data: {
                        remittanceApplicationUuid: this.$route.params.id
                    }
                }).then(data => {
                    this.reCallbackModal.show = false;
                    MessageBox.open(data.message);
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
