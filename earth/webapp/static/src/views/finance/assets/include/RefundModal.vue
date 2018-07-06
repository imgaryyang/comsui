<style lang="sass">
    #refundModals {
        .el-table.td-15-padding td > .cell {
            padding: 10px;
        }
        .error_input, .isNaN_input {
            .el-input__inner {
                border-color: red;
            }
        }
        .tips {
            font-size: 12px;
            text-align: right;
            line-height: 20px;
            background-color: #f5f5f5;
        }
    }
</style>

<template>
    <Modal v-model="show">
        <ModalHeader title="资产退款">
        </ModalHeader>
        <ModalBody align="left">
            <div>
                <div class="bd" id="refundModals">
                    <el-table
                        :data="showData"
                        class="td-15-padding th-8-15-padding no-th-border"
                        border>
                        <el-table-column prop="name" label="明细金额"></el-table-column>
                        <el-table-column label="入账资金" inline-template>
                            <div>{{ row.recordAmount | formatMoney }}</div>
                        </el-table-column>
                        <el-table-column label="退款金额" inline-template>
                            <div>
                                <el-input
                                    v-model="row.refundAmount"
                                    v-if="row.name !=='金额合计'"
                                    :class="{
                                        'error_input': !isNaN(row.refundAmount) && (row.refundAmount > row.recordAmount) && row.blur,
                                        'isNaN_input': isNaN(row.refundAmount) && row.blur
                                    }"
                                    @blur="row.blur = true;getTotalAmount()"
                                    @focus="row.blur = false"
                                    size="small"></el-input>
                                <div v-else>{{ totalAmount | formatMoney }}</div>
                            </div>
                        </el-table-column>
                    </el-table>
                    <div class="tips">资产退款仅允许对入账资金进行退款</div>
                    <el-form label-width="55px" label-position="left">
                        <el-form-item label="备注：">
                            <el-input v-model="queryModel.remark"></el-input>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" type="primary">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            value: Boolean,
            model: {
                type: Object,
                default: () => {}
            },
            actualChargesDetail: {
                type: Object,
                default: () => {}
            }
        },
        data: function() {
            return {
                show: this.value,
                queryModel: {
                    assetUuid: '',
                    repaymentPlanNo: '',
                    remark: '',
                    contractId: '',
                    principal: 0,
                    interest: 0,
                    serviceCharge: 0,
                    maintenanceCharge: 0,
                    otherCharge: 0,
                    penaltyFee: 0,
                    latePenalty: 0,
                    lateFee: 0,
                    lateOtherCost: 0,
                    totalAmount: 0,
                },
                showData: [],
                totalAmount: 0,
            }
        },
        watch: {
            value: function(cur) {
                this.show = cur;
                this.initShowData();
                Object.assign(this.queryModel, this.model);
            },
            show: function(cur) {
                if(!cur) {
                    this.showData = [];
                    this.totalAmount = 0;
                    this.queryModel = {
                        assetUuid: '',
                        repaymentPlanNo: '',
                        remark: '',
                        contractId: '',
                        principal: 0,
                        interest: 0,
                        serviceCharge: 0,
                        maintenanceCharge: 0,
                        otherCharge: 0,
                        penaltyFee: 0,
                        latePenalty: 0,
                        lateFee: 0,
                        lateOtherCost: 0,
                        totalAmount: 0
                    };
                }
                this.$emit('input', cur);
            },
            model: function(cur) {
                Object.assign(this.queryModel, cur);
            },
            actualChargesDetail: {
                deep: true,
                handler: function() {
                    this.initShowData();
                }
            },
        },
        methods: {
            isError: function() {
                const err = function(item) {
                    return isNaN(item.refundAmount) || (item.refundAmount > item.recordAmount);
                }
                return this.showData.some(err);
            },
            initShowData: function() {
                this.showData = [].concat([
                    { name: '本金', recordAmount: this.actualChargesDetail.loanAssetPrincipal || 0, refundAmount: '', blur: false},
                    { name: '利息', recordAmount: this.actualChargesDetail.loanAssetInterest || 0, refundAmount: '', blur: false},
                    { name: '贷款服务费', recordAmount: this.actualChargesDetail.loanServiceFee || 0, refundAmount: '', blur: false},
                    { name: '技术维护费', recordAmount: this.actualChargesDetail.loanTechFee || 0, refundAmount: '', blur: false},
                    { name: '其他费用', recordAmount: this.actualChargesDetail.loanOtherFee || 0, refundAmount: '', blur: false},
                    { name: '逾期罚息', recordAmount: this.actualChargesDetail.overdueFeePenalty || 0, refundAmount: '', blur: false},
                    { name: '逾期违约金', recordAmount: this.actualChargesDetail.overdueFeeObligation || 0, refundAmount: '', blur: false},
                    { name: '逾期服务费', recordAmount: this.actualChargesDetail.overdueFeeService || 0, refundAmount: '', blur: false},
                    { name: '逾期其他费用', recordAmount: this.actualChargesDetail.overdueFeeOther || 0, refundAmount: '', blur: false},
                    { name: '金额合计', recordAmount: this.actualChargesDetail.totalFee || 0, refundAmount: '', blur: false},

                ]);
            },
            getTotalAmount: function() {
                if(this.isError()) return;

                this.totalAmount = this.showData.map(item => item.refundAmount).reduce((prev, curr) => (+prev) + (+curr));
            },
            submit: function() {
                if(this.isError()) return;
                const names = ['principal', 'interest', 'serviceCharge', 'maintenanceCharge', 'otherCharge', 'penaltyFee', 'latePenalty', 'lateFee', 'lateOtherCost'];
                names.forEach((item, index) => {
                    this.queryModel[item] = this.showData[index].refundAmount || this.queryModel[item];
                })
                this.queryModel.totalAmount = this.totalAmount;

                ajaxPromise({
                    url: `/assets/asset/refund`,
                    type: 'post',
                    data: Object.assign({}, this.queryModel)
                }).then(data => {
                    MessageBox.once('close', () => {
                        this.$emit('submit');
                        this.show = false;
                    });
                    MessageBox.open('退款成功');
                }).catch(()=>{});
            }
        }
    }
</script>
