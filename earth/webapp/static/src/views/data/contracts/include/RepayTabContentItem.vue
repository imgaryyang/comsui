<style type="sass">

</style>

<template>
	<div>
        <div class="block">
            <h5
            tyle="display: inline-flex">
                <span style="line-height: 34px">还款计划</span>
                <el-select class="small select-version" v-model="currentVersion" :disabled="repayPlanModel.edit == true">
                    <el-option
                        v-for="(item, index) in currentModel.allVersion"
                        :label="item.formatLabel"
                        :value="item.versionNo">
                        <span class="pull-left">{{ item.formatLabel }}</span>
                        <i class="el-icon-star-on" v-if="item.versionNo == defaultVersion" style="margin-left: 10px"></i>
                    </el-option>
                </el-select>
                <template v-if="repayPlanModel.edit == false">
                    <el-button
                        type="primary"
                        size="small"
                        v-if="currentModel.contract.id && lastVersion && ifElementGranted('update-repayment-plan')"
                        @click="handleEditRepayPlan">
                        编辑
                    </el-button>
                    <span style="margin: 0 15px">变更类型：{{ modifyReason }}</span>
                    <span>备注：{{ comment }}</span>
                </template>
            </h5>
            <div class="bd">
                <el-table
                    :data="repayPlanModel.edit ? repayPlanModel.todoRepayPlanList : repayPlanModel.repayPlanList"
                    v-loading="repayPlanModel.fetching"
                    class="td-15-padding th-8-15-padding no-th-border"
                    stripe
                    border>
                    <el-table-column
                        inline-template
                        label="还款编号"
                        v-if="!repayPlanModel.edit">
                        <a :href="`${ctx}#/finance/assets/${row.assetUuid}/detail`">
                            {{ row.repaymentPlanNo }}
                        </a>
                    </el-table-column>
                    <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo" inline-template>
                        <div>
                            <div v-if="repayPlanModel.edit && row.canBeModifed">
                                <el-input size="small" v-model="repayPlanModel.todoRepayPlanList[$index].outerRepaymentPlanNo"></el-input>
                            </div>
                            <div v-else>{{ row.outerRepaymentPlanNo }}</div>
                        </div>
                    </el-table-column>
                    <el-table-column inline-template label="计划还款日期">
                       <div>
                            <div v-if="repayPlanModel.edit && row.canBeModifed">
                               <DateTimePicker v-model="repayPlanModel.todoRepayPlanList[$index].occurDate"></DateTimePicker>
                            </div>
                            <div v-else>{{ row.occurDate | formatDate }}</div>
                        </div>
                    </el-table-column>
                    <el-table-column inline-template label="计划还款本金">
                        <div>
                            <div v-if="repayPlanModel.edit && row.canBeModifed">
                                <el-input size="small" v-model="repayPlanModel.todoRepayPlanList[$index].repayPrincipal"></el-input>
                            </div>
                           <div v-else>{{ row.repayPrincipal | formatMoney }}</div>
                        </div>
                    </el-table-column>
                    <el-table-column inline-template label="计划还款利息">
                        <div>
                            <div v-if="repayPlanModel.edit && row.canBeModifed">
                                <el-input size="small" v-model="repayPlanModel.todoRepayPlanList[$index].repayProfit"></el-input>
                            </div>
                           <div v-else>{{ row.repayProfit | formatMoney }}</div>
                        </div>
                    </el-table-column>
                    <el-table-column inline-template label="贷款服务费">
                        <div>
                            <div v-if="repayPlanModel.edit && row.canBeModifed">
                                <el-input size="small" v-model="repayPlanModel.todoRepayPlanList[$index].loanServiceFee"></el-input>
                            </div>
                           <div v-else>{{ row.loanServiceFee | formatMoney }}</div>
                        </div>
                    </el-table-column>
                    <el-table-column inline-template label="技术维护费">
                        <div>
                            <div v-if="repayPlanModel.edit && row.canBeModifed">
                                <el-input size="small" v-model="repayPlanModel.todoRepayPlanList[$index].techMaintenanceFee"></el-input>
                            </div>
                           <div v-else>{{ row.techMaintenanceFee | formatMoney }}</div>
                        </div>
                    </el-table-column>
                    <el-table-column inline-template label="其他费用">
                        <div>
                            <div v-if="repayPlanModel.edit && row.canBeModifed">
                                <el-input size="small" v-model="repayPlanModel.todoRepayPlanList[$index].otherFee"></el-input>
                            </div>
                           <div v-else>{{ row.otherFee | formatMoney }}</div>
                        </div>
                    </el-table-column>
                    <el-table-column inline-template label="计划还款金额">
                        <div>
                            <div v-if="repayPlanModel.edit">{{ row.assetInitialValue }}</div>
                            <div v-else>{{ row.assetInitialValue | formatMoney }}</div>
                        </div>
                    </el-table-column>
                    <el-table-column label="计划状态" prop="planStatus" ></el-table-column>
                    <el-table-column label="还款状态" prop="paymentStatus"></el-table-column>
                    <el-table-column
                        inline-template
                        label="操作"
                        v-if="repayPlanModel.edit">
                        <div v-if="row.canBeModifed || !row.id">
                            <a class="color-danger" href="#" @click.prevent="handleDeleteChanges($index,'')">删除</a>
                        </div>
                    </el-table-column>
                </el-table>
            </div>
            <div class="ft text-align-center" style="background: #fff;line-height: 28px" v-if="repayPlanModel.edit">
                <a href="#" @click.prevent="handleCreate">+  新增</a>
            </div>
            <div class="ft clearfix">
                <div class="pull-left">
                    <span style="display: inline-flex" v-if="repayPlanModel.edit">
                        <span style="line-height: 34px">修改原因:</span>
                        <el-select class="small select-version" v-model="modifyCode">
                            <el-option
                                v-for=" item in currentModel.modifyReason"
                                :label="item.value"
                                :value="item.key">
                            </el-option>
                        </el-select>
                    </span>
                </div>
                <div class="pull-right">
                    <span class="summary" v-if="!repayPlanModel.edit">
                        合计:
                        <span>{{ summaryModel.principalSum | formatMoney }}</span>（本金）+
                        <span>{{ summaryModel.profitSum | formatMoney }}</span>（利息）+
                        <span>{{ summaryModel.loanServiceFeeSum | formatMoney }}</span>（贷款服务费）+
                        <span>{{ summaryModel.techMaintenanceFeeSum | formatMoney }}</span>（技术维护费）+
                        <span>{{ summaryModel.otherFeeSum | formatMoney }}</span> （其他费用）=
                        <span>{{ summaryModel.totalAmount | formatMoney }}</span>
                    </span>
                    <div v-if="repayPlanModel.edit" style="line-height: 34px">
                        <span class="summary" style="margin-right:10px">
                            剩余本金:
                            <span v-bind:class="{'color-danger' : residualPrincipal < 0}">{{ residualPrincipal | formatMoney }}</span>
                        </span>
                        <el-button size="small" type="success" :loading="submiting" @click.prevent="handleSubmitChanges">提交</el-button>
                        <el-button size="small" type="default" :disabled="submiting" @click.prevent="handleCancelChanges">取消</el-button>
                    </div>
                </div>
            </div>
        </div>

        <div class="block">
            <h5 class="hd">还款记录</h5>
            <div class="bd">
                <PagingTable :data="repaymentHistorys" :loading="repaymentHistorysFetching">
                    <el-table-column label="记录编号" prop="" inline-template>
                        <div>{{ $index + 1 }}</div>
                    </el-table-column>
                    <el-table-column label="业务编号" prop="repaymentPlanNo" inline-template>
                        <a :href="row.capitalType != '回购' ? `${ctx}#/finance/assets/${row.assetSetUuid}/detail` : `${ctx}#/finance/repurchasedoc/${row.uuid}/detail`">{{ row.repaymentPlanNo }}</a>
                    </el-table-column>
                    <el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo" inline-template>
                        <div>{{ row.outerRepaymentPlanNo }}</div>
                    </el-table-column>
                    <el-table-column label="计划还款日期" prop="planDate" inline-template>
                        <div>
                            {{ row.planDate | formatDate('yyyy-MM-dd') }}
                        </div>
                    </el-table-column>
                    <el-table-column label="实际还款时间" prop="actualRecycleDate" inline-template>
                        <div>
                            {{ row.actualRecycleDate | formatDate('yyyy-MM-dd HH:mm:ss') }}
                        </div>
                    </el-table-column>
                    <el-table-column label="发生时间" prop="happenDate" inline-template>
                        <div>
                            {{ row.happenDate | formatDate('yyyy-MM-dd HH:mm:ss') }}
                        </div>
                    </el-table-column>
                    <el-table-column label="资金入账时间" prop="accountedDate" inline-template>
                        <div>
                            {{ row.accountedDate | formatDate('yyyy-MM-dd HH:mm:ss') }}
                        </div>
                    </el-table-column>
                    <el-table-column label="本次实收金额" prop="totalFee" inline-template>
                        <el-popover
                            placement="top"
                            trigger="hover">
                            <template v-if="row.capitalType != '回购'">
                                <div>实收本金：{{ row.loanAssetPrincipal | formatMoney }}</div>
                                <div>实收利息：{{ row.loanAssetInterest | formatMoney }}</div>
                                <div>实收贷款服务费：{{ row.loanServiceFee | formatMoney }}</div>
                                <div>实收技术维护费：{{ row.loanTechFee | formatMoney }}</div>
                                <div>实收其他费用：{{ row.loanOtherFee | formatMoney }}</div>
                                <div>实收逾期罚息：{{ row.overdueFeePenalty | formatMoney }}</div>
                                <div>实收逾期违约金：{{ row.overdueFeeObligation | formatMoney }}</div>
                                <div>实收逾期服务费：{{ row.overdueFeeService | formatMoney }}</div>
                                <div>实收逾期其他费用：{{ row.overdueFeeOther | formatMoney }}</div>
                                <div>实收逾期费用合计：{{ (row.overdueFeePenalty + row.overdueFeeObligation + row.overdueFeeService + row.overdueFeeOther) | formatMoney }}</div>
                            </template>
                            <template v-else>
                                <div>实收回购本金：{{ row.repurchasePrincipal | formatMoney }}</div>
                                <div>实收回购利息：{{ row.repurchaseInterest | formatMoney }}</div>
                                <div>实收回购罚息：{{ row.repurchasePenalty | formatMoney }}</div>
                                <div>实收回购其他费用：{{ row.repurchaseOtherCharges | formatMoney }}</div>
                            </template>
                            <span slot="reference">{{ row.totalFee | formatMoney }}</span>
                        </el-popover>
                    </el-table-column>
                    <el-table-column label="还款方式" prop="capitalType">
                    </el-table-column>
                    <el-table-column label="支付通道" prop="paymentGateway">
                    </el-table-column>
                    <el-table-column label="相关凭证" prop="voucherNo">
                    </el-table-column>
                </PagingTable>
            </div>
        </div>

        <div class="block">
            <h5 class="hd">回购记录</h5>
            <div class="bd">
                <el-table
                    :data="repurchaseDocs"
                    class="td-15-padding th-8-15-padding no-th-border"
                    stripe
                    border>
                    <el-table-column inline-template label="回购单号">
                        <a :href="`${ctx}#/finance/repurchasedoc/${row.repurchaseDocUuid}/detail`">
                            {{ row.repurchaseDocUuid }}
                        </a>
                    </el-table-column>
                    <el-table-column inline-template label="回购起始日">
                       <div>{{ row.repoStartDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column inline-template label="回购截止日">
                       <div>{{ row.repoEndDate | formatDate }}</div>
                    </el-table-column>
                    <el-table-column label="商户名称" prop="appName">
                    </el-table-column>
                    <el-table-column inline-template label="回购金额">
                       <div>{{ row.amount | formatMoney }}</div>
                    </el-table-column>
                    <el-table-column label="回购天数" prop="repoDays">
                    </el-table-column>
                    <el-table-column inline-template label="发生时间">
                       <div>{{ row.creatTime | formatDate('yyyy-MM-dd HH:mm:ss') }}</div>
                    </el-table-column>
                    <el-table-column inline-template label="回购状态" prop="repurchaseStatus">
                        <div>
                            {{ row.repurchaseStatus | formatRepurchaseStatus }}
                        </div>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import format from 'filters/';
    import Decimal from 'decimal.js'

    export default {
        components: {
            PagingTable: require('views/include/PagingTable')
        },
    	props: {
            selected: Boolean,
    		currentModel: {
    		    default: () => ({})
    		},
			defaultVersion: [String, Number],
			repaymentHistorys: {
                default: () => ({})
            },
			contractId: [String, Number],
            repurchaseStatusList: {
                type: Array,
                default: () => ([]),
            },
            repaymentHistorysFetching: Boolean
    	},
    	data: function() {
    		return {
                submiting: false,
    			currentVersion: this.defaultVersion,
    			modifyCode: -1,
                totalPrincipal: 0,
                modifyReason: '',
                comment: '',
				repayPlanModel: {
                    fetching: false,
				    repayPlanList: [],
                    todoRepayPlanList: [],
                    edit: false,
				},
                repaymentHistoryList: {}
    		}
    	},
        filters: {
            formatRepurchaseStatus: function(value) {
                var types = {
                    REPURCHASING: '回购中',
                    REPURCHASED: '已回购',
                    DEFAULT: '违约',
                    INVALID: '作废',
                }
                return types[value];
            }
        },
    	computed: {
    		lastVersion: function() {
                return this.currentModel.allVersion.length != 0 && this.defaultVersion == this.currentVersion;
            },
            summaryModel: function() {
                var model = {};
                var principalSum= new Decimal(0);
                var profitSum= new Decimal(0);
                var loanServiceFeeSum= new Decimal(0);
                var techMaintenanceFeeSum= new Decimal(0);
                var otherFeeSum= new Decimal(0);
                var totalAmount = new Decimal(0);

                this.repayPlanModel.repayPlanList.forEach(item => {
                    principalSum = principalSum.add(item.repayPrincipal);
                    profitSum = profitSum.add(item.repayProfit);
                    loanServiceFeeSum = loanServiceFeeSum.add(item.loanServiceFee);
                    techMaintenanceFeeSum = techMaintenanceFeeSum.add(item.techMaintenanceFee);
                    otherFeeSum = otherFeeSum.add(item.otherFee);
                });

                if (!this.repayPlanModel.edit) {
                    this.totalPrincipal = principalSum.toNumber();
                }

                model.principalSum = principalSum.toNumber();
                model.profitSum = profitSum.toNumber();
                model.loanServiceFeeSum = loanServiceFeeSum.toNumber();
                model.techMaintenanceFeeSum = techMaintenanceFeeSum.toNumber();
                model.otherFeeSum = otherFeeSum.toNumber();
                model.totalAmount = principalSum.add(profitSum).add(loanServiceFeeSum).add( techMaintenanceFeeSum).add(otherFeeSum).toNumber();
                return model;
            },
            residualPrincipal: function() {
                var { repayPlanList, todoRepayPlanList } = this.repayPlanModel;
                var total = new Decimal(0);
                todoRepayPlanList.forEach(item => {
                    var value = parseFloat(item.repayPrincipal);
                    if (!isNaN(value)) total = total.add(value);
                });
                return new Decimal(this.totalPrincipal).minus(total).toNumber();
            },
            repurchaseDocs: function() {
                var arry = [];
                if (!$.isEmptyObject(this.currentModel.repurchaseDoc)) {
                    arry = [this.currentModel.repurchaseDoc];
                }
                return arry;
            }
        },
        watch: {
            defaultVersion: function(cur) {
                this.currentVersion = cur;
            },
            contractId: function(cur) {
                this.modifyCode = -1;
                if (this.selected && !this.repayPlanModel.fetching) {
                    this.fetchCurrentVersion(this.currentVersion);
                }
            },
            currentVersion: function(cur) {
                if (this.selected && !this.repayPlanModel.fetching) {
                    this.fetchCurrentVersion(cur);
                }
            },
            'currentModel.allVersion': function(cur){
                if(!(0 in cur)){
                    this.repayPlanModel = Object.assign({},{
                        fetching: false,
                        repayPlanList: [],
                        todoRepayPlanList: [],
                        edit: false,
                    })
                }
            }
    	},
    	methods: {
    		fetchCurrentVersion: function(versionNo) {
    		    if (!this.contractId) return;

                this.repayPlanModel.fetching = true;

    			ajaxPromise({
                    url: `/contracts/detail/${this.contractId}`,
                    data: { versionNo }
                }).then(data => {
                    this.$emit('signUpReference', {
                        isYunXin: data.isYunXin,
                        isNeedReSignUp: data.isNeedReSignUp || '',
                        opType: data.opType || ''
                    });
                    this.modifyReason = data.modifyReason || '';
                    this.comment = data.comment || '';
                    this.repayPlanModel.repayPlanList = data.list || [];
                    this.repayPlanModel.edit = false;

                    this.repayPlanModel.repayPlanList.forEach(item =>{
                        item.occurDate = format.formatDate(item.occurDate);
                    });
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.repayPlanModel.fetching = false;
                });
    		},
            updateRepaymentPlan: function(modelList) {
                if (this.submiting) return;

                this.submiting = true;

                ajaxPromise({
                    url: `/contracts/detail/updateRepaymentPlan`,
                    type: 'post',
                    data: {
                        data: JSON.stringify(modelList),
                        contractId: this.contractId,
                        modifyCode: this.modifyCode
                    },
                }).then(data => {
                    MessageBox.open('操作成功');
                    setTimeout(() => {
                        location.reload();
                    }, 500);
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.submiting = false;
                });
            },
    		handleEditRepayPlan: function() {
                if(this.repayPlanModel.repayPlanList.size == 0) return;
                this.repayPlanModel.edit = true;
                this.repayPlanModel.todoRepayPlanList = this.repayPlanModel.repayPlanList.map(item => Object.assign({}, item));
            },
            handleCreate: function() {
                this.repayPlanModel.todoRepayPlanList.push({
                    canBeModifed: true,
                    repayPrincipal: '',
                    repayProfit: '',
                    loanServiceFee: '',
                    techMaintenanceFee: '',
                    otherFee: '',
                    occurDate: '',
                    outerRepaymentPlanNo: ''
                });
            },
            handleDeleteChanges: function(index, type) {
                var { repayPlanList, todoRepayPlanList } = this.repayPlanModel;

                MessageBox.open('即将删除该还款计划是否继续？', '', [{
                    text: '取消',
                    type: 'default',
                    handler: () => MessageBox.close()
                }, {
                    text: '继续',
                    type: 'success',
                    handler: () => {
                        if (todoRepayPlanList.length <= 1) {
                            MessageBox.open('还款计划不能删光哦！');
                            return;
                        }

                        todoRepayPlanList.splice(index, 1);

                        MessageBox.close();
                    }
                }]);
            },
            handleSubmitChanges: function() {
                var formatValue = function(value) {
                    return value == '' || value.length == 0 ? '0': value + '';
                };

                var { repayPlanList, todoRepayPlanList } = this.repayPlanModel;

                if (this.residualPrincipal < 0 ) {
                    MessageBox.open('计划还款本金与原版本不相等！');
                    return;
                }
                if(this.modifyCode == -1) {
                    MessageBox.open('请选择修改原因！');
                    return;
                }

                var modelList = todoRepayPlanList.filter(item => item.canBeModifed).map(item => {
                    var model = {};
                    model.assetRecycleDate = item.occurDate;
                    model.assetPrincipal = formatValue(item.repayPrincipal);
                    model.assetInterest = formatValue(item.repayProfit);
                    model.serviceCharge = formatValue(item.loanServiceFee);
                    model.maintenanceCharge = formatValue(item.techMaintenanceFee);
                    model.otherCharge = formatValue(item.otherFee);
                    model.repayScheduleNo = item.outerRepaymentPlanNo;
                    return model;
                });

                this.updateRepaymentPlan(modelList);
            },
            handleCancelChanges: function() {
                this.repayPlanModel.edit = false;
                this.repayPlanModel.todoRepayPlanList = [];
            }
    	}
    }
</script>
