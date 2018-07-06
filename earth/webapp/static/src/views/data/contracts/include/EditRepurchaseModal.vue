<style lang="sass">
    @import '~assets/stylesheets/base';
    .editRepurchaseModal {
        @include min-screen(768px) {
            .modal-dialog {
                width: 60%;
                margin: 30px auto;
            }
        }

        .dash {
            border-top:dashed 1px #999;
            height:1px;
            margin: 20px 0px;
        }

        .money {
            font-size: 18px;
            margin: 0 5px
        }
    }

</style>
<template>
    <div>
        <Modal v-model="show" class="editRepurchaseModal">
            <div v-loading="fetching">
                <ModalHeader title="回购">
                </ModalHeader>
                <ModalBody align="left">
                    <div v-if="error" style="text-align: center; margin-top: 35px;">
                        {{ error }}
                    </div>
                    <template v-else>
                        <p>是否对贷款合同({{ contractNo }})进行回购？</p>
                        <div>
                            <span style="color:#999;">
                                回购金额:
                                <span class="color-danger money">
                                    {{ currentModel.repurchaseDetail.amount | formatMoney }}
                                </span>
                                元
                            </span>
                        </div>
                        <div>
                            <span style="color:#999;">
                                本金金额:
                                <el-popover
                                    v-if="currentModel.repurchaseDetail.repurchasePrincipalExpression"
                                    trigger="hover"
                                    placement="top">
                                    {{ currentModel.repurchaseDetail.repurchasePrincipalExpression }}
                                    <span class="color-danger money" slot="reference">
                                        {{ currentModel.repurchaseDetail.repurchasePrincipal | formatMoney }}
                                    </span>
                                </el-popover>
                                <span class="color-danger money" v-else>
                                    {{ currentModel.repurchaseDetail.repurchasePrincipal | formatMoney }}
                                </span>
                                元&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;算法：{{ repurchasePrincipalAlgorithm }}
                            </span>
                            <a href="#" @click.prevent="modifyAlgorithm('本金', currentModel.repurchaseDetail.repurchasePrincipalAlgorithm)" style="margin-left:10px">修改</a>
                        </div>
                        <div>
                            <span style="color:#999;">
                                利息金额:
                                <el-popover
                                    v-if="currentModel.repurchaseDetail.repurchaseInterestExpression"
                                    trigger="hover"
                                    placement="top">
                                    {{ currentModel.repurchaseDetail.repurchaseInterestExpression }}
                                    <span class="color-danger money" slot="reference">
                                        {{ currentModel.repurchaseDetail.repurchaseInterest | formatMoney }}
                                    </span>
                                </el-popover>
                                <span class="color-danger money" v-else>
                                    {{ currentModel.repurchaseDetail.repurchaseInterest | formatMoney }}
                                </span>
                                元&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;算法：{{ repurchaseInterestAlgorithm }}
                            </span>
                            <a href="#" @click.prevent="modifyAlgorithm('利息', currentModel.repurchaseDetail.repurchaseInterestAlgorithm)" style="margin-left:10px">修改</a>
                        </div>
                        <div>
                            <span style="color:#999;">
                                罚息金额:
                                <el-popover
                                    v-if="currentModel.repurchaseDetail.repurchasePenaltyExpression"
                                    trigger="hover"
                                    placement="top">
                                    {{ currentModel.repurchaseDetail.repurchasePenaltyExpression }}
                                    <span class="color-danger money" slot="reference">
                                        {{ currentModel.repurchaseDetail.repurchasePenalty | formatMoney }}
                                    </span>
                                </el-popover>
                                <span class="color-danger money" v-else>
                                    {{ currentModel.repurchaseDetail.repurchasePenalty | formatMoney }}
                                </span>
                                元&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;算法：{{ repurchasePenaltyAlgorithm }}
                            </span>
                            <a href="#" @click.prevent="modifyAlgorithm('罚息', currentModel.repurchaseDetail.repurchasePenaltyAlgorithm)" style="margin-left:10px">修改</a>
                        </div>
                        <div>
                            <span style="color:#999;">
                                其他费用金额:
                                <el-popover
                                    v-if="currentModel.repurchaseDetail.repurchaseOtherChargesExpression"
                                    trigger="hover"
                                    placement="top">
                                    {{ currentModel.repurchaseDetail.repurchaseOtherChargesExpression }}
                                    <span class="color-danger money" slot="reference">
                                        {{ currentModel.repurchaseDetail.repurchaseOtherCharges | formatMoney }}
                                    </span>
                                </el-popover>
                                <span class="color-danger money" v-else>
                                    {{ currentModel.repurchaseDetail.repurchaseOtherCharges | formatMoney }}
                                </span>
                                元&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;算法：{{ repurchaseOtherChargesAlgorithm }}
                            </span>
                            <a href="#" @click.prevent="modifyAlgorithm('其他费用', currentModel.repurchaseDetail.repurchaseOtherChargesAlgorithm)" style="margin-left:10px">修改</a>
                        </div>
                        <div class="dash"></div>
                        <div class="block">
                            <h5 class="hd">还款列表</h5>
                            <div class="bd">
                                <PagingTable
                                    :data="currentModel.assetSetBillingDetails">
                                    <el-table-column label="还款编号" inline-template>
                                        <a :href="`${ctx}#/finance/assets/${row.assetUuid}/detail`">{{ row.repaymentPlanNo }}</a>
                                    </el-table-column>
                                    <el-table-column label="计划还款日期" inline-template>
                                        <div>{{ row.occurDate | formatDate }}</div>
                                    </el-table-column>
                                    <el-table-column label="计划还款本金" inline-template>
                                        <div>{{ row.repayPrincipal | formatMoney }}</div>
                                    </el-table-column>
                                    <el-table-column label="计划还款利息" inline-template>
                                        <div>{{ row.repayProfit | formatMoney }}</div>
                                    </el-table-column>
                                    <el-table-column label="贷款服务费" inline-template>
                                        <div>{{ row.loanServiceFee | formatMoney }}</div>
                                    </el-table-column>
                                    <el-table-column label="技术维护费" inline-template>
                                        <div>{{ row.techMaintenanceFee | formatMoney }}</div>
                                    </el-table-column>
                                    <el-table-column label="其他费用" inline-template>
                                        <div>{{ row.otherFee | formatMoney }}</div>
                                    </el-table-column>
                                    <el-table-column label="计划还款金额" inline-template>
                                        <div>{{ row.assetInitialValue | formatMoney }}</div>
                                    </el-table-column>
                                    <el-table-column label="计划状态" prop="planStatus">
                                    </el-table-column>
                                    <el-table-column label="还款状态" prop="paymentStatus">
                                    </el-table-column>
                                </PagingTable>
                            </div>
                        </div>
                    </template>
                </ModalBody>
                <ModalFooter>
                    <el-button @click="show = false">取消</el-button>
                    <el-button v-if="!error" @click="submit" type="success" :loading="submitting">确定回购</el-button>
                </ModalFooter>
            </div>
        </Modal>

        <RepurchaseAlgorithmModal
            v-model="modal.show"
            @confirm="onConfirmModify"
            :keyword="modal.keyword"
            :model="modal.model">
        </RepurchaseAlgorithmModal>
    </div>
</template>
<script>
	import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';
    import PagingTable from 'views/include/PagingTable';
    import RepurchaseAlgorithmModal from 'views/financial/contract/include/RepurchaseAlgorithmModal';

    export default {
    	mixins: [modalMixin],
        components: {
            PagingTable, RepurchaseAlgorithmModal,
        },
    	props: {
            contractId: {
                required: true
            },
            contractNo: {
                required: true
            }
    	},
    	data: function() {
    		return {
    			show: this.value,
                submitting: false,
                fetching: false,
                error: '',

                currentModel: {
                    repurchaseDetail: {
                        amount: '',
                        repurchasePrincipal: '',
                        repurchasePrincipalAlgorithm: '',
                        repurchasePrincipalExpression: '',
                        repurchaseInterest: '',
                        repurchaseInterestAlgorithm: '',
                        repurchaseInterestExpression: '',
                        repurchasePenalty: '',
                        repurchasePenaltyAlgorithm: '',
                        repurchasePenaltyExpression: '',
                        repurchaseOtherCharges: '',
                        repurchaseOtherChargesAlgorithm: '',
                        repurchaseOtherChargesExpression: '',
                    },
                    assetSetBillingDetails: [],
                },
                modal: {
                    show: false,
                    keyword: '',
                    model: {},
                },
    		}
    	},
        watch: {
            show: function(current) {
                if (current) {
                    this.fetchDetail();
                }else {
                    this.currentModel = {
                        repurchaseDetail: {},
                        assetSetBillingDetails: [],
                    };
                }
            },
        },
        computed: {
            repurchasePrincipalAlgorithm: function() {
                var algorithm = this.currentModel.repurchaseDetail.repurchasePrincipalAlgorithm;
                return algorithm ? algorithm : '空';
            },
            repurchaseInterestAlgorithm: function() {
                var algorithm = this.currentModel.repurchaseDetail.repurchaseInterestAlgorithm;
                return algorithm ? algorithm : '空';
            },
            repurchasePenaltyAlgorithm: function() {
                var algorithm = this.currentModel.repurchaseDetail.repurchasePenaltyAlgorithm;
                return algorithm ? algorithm : '空';
            },
            repurchaseOtherChargesAlgorithm: function() {
                var algorithm = this.currentModel.repurchaseDetail.repurchaseOtherChargesAlgorithm;
                return algorithm ? algorithm : '空';
            },
        },
    	methods: {
            fetchDetail: function() {
                if (this.fetching) return;
                this.fetching = true;

                ajaxPromise({
                    url: `/contracts/detail/repurchaseDetail`,
                    data: {
                        id: this.contractId,
                    },
                }).then(data => {
                    this.currentModel = Object.assign({}, this.currentModel, data);
                    this.error = '';

                }).catch(message => {
                    this.error = message.message;
                }).then(() => {
                    this.fetching = false;
                });
            },
            modifyAlgorithm: function(word, content) {
                this.modal.show = true;
                this.modal.keyword = word;
                this.modal.model = {
                    content: content
                }
            },
            submit: function() {
                if (this.submitting) return;
                this.submitting = true;

                var repurchaseAmountDetail = Object.assign({}, this.currentModel.repurchaseDetail);
                ajaxPromise({
                    url: `/contracts/detail/repurchase`,
                    data: {
                        id: this.contractId,
                        ...repurchaseAmountDetail
                    },
                }).then(data => {
                    MessageBox.open('提交成功！');
                    MessageBox.once('close', () => {
                        this.show = false;
                        setTimeout(() => {
                            this.$emit('submit');
                        }, 500);
                    });
                }).catch(message => {
                    MessageBox.open(message)
                }).then(() => {
                    this.submitting = false;
                })
            },
            onConfirmModify: function(currentContent, keyword) {
                if (this.fetching) return;
                this.fetching = true;
                this.modal.show = false;

                var repurchaseAmountDetail = this.getRepurchaseAmountDetail(currentContent, keyword);
                var oldAlgorithm = this.getOldAlgorithm(keyword);

                ajaxPromise({
                    url: `/contracts/detail/repurchaseDetail`,
                    data: {
                        id: this.contractId,
                        init: 1,
                        ...repurchaseAmountDetail
                    },
                }).then(data => {
                    const amount = this.getNowAlgorithmAmount(keyword, data.repurchaseDetail);
                    MessageBox.open(`
                        <div id="confirmModal">
                            <div>
                                <span class="title">原${ keyword }算法：</span><span class="content">${ oldAlgorithm }</span>
                            </div>
                            <div>
                                <span class="title">新${ keyword }算法：</span><span class="content">${ currentContent }</span>
                            </div>
                            <div>
                                <span class="title">${ keyword }金额： </span><span class="content">${ amount }</span>
                            </div>
                        </div>
                    `, null, [{
                        text: '取消',
                        handler: () => MessageBox.close()
                    }, {
                        text: '确定',
                        type: 'success',
                        handler: () => {
                            this.currentModel = Object.assign({}, this.currentModel, data);
                            MessageBox.close();
                        }
                    }]);
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.fetching = false;
                });

            },
            getRepurchaseAmountDetail: function(currentContent, keyword) {
                var repurchaseDetail  = this.currentModel.repurchaseDetail;
                var _result = {
                    repurchasePrincipalAlgorithm: repurchaseDetail.repurchasePrincipalAlgorithm,
                    repurchaseInterestAlgorithm: repurchaseDetail.repurchaseInterestAlgorithm,
                    repurchasePenaltyAlgorithm: repurchaseDetail.repurchasePenaltyAlgorithm,
                    repurchaseOtherChargesAlgorithm: repurchaseDetail.repurchaseOtherChargesAlgorithm,
                };

                if (keyword == '本金') {
                    _result.repurchasePrincipalAlgorithm = currentContent;
                } else if(keyword == '利息') {
                    _result.repurchaseInterestAlgorithm = currentContent;
                } else if(keyword == '罚息') {
                    _result.repurchasePenaltyAlgorithm = currentContent;
                } else {
                    _result.repurchaseOtherChargesAlgorithm = currentContent;
                }

                return _result;
            },
            getOldAlgorithm: function(keyword) {
                var repurchaseDetail  = this.currentModel.repurchaseDetail;
                var content = '';

                if (keyword == '本金') {
                   content = repurchaseDetail.repurchasePrincipalAlgorithm;
                } else if(keyword == '利息') {
                   content = repurchaseDetail.repurchaseInterestAlgorithm;
                } else if(keyword == '罚息') {
                   content = repurchaseDetail.repurchasePenaltyAlgorithm;
                } else {
                   content = repurchaseDetail.repurchaseOtherChargesAlgorithm;
                }

                return content;
            },
            getNowAlgorithmAmount: function(keyword, repurchaseDetail) {
                var amount = '';

                if (keyword == '本金') {
                   amount = repurchaseDetail.repurchasePrincipal;
                } else if(keyword == '利息') {
                   amount = repurchaseDetail.repurchaseInterest;
                } else if(keyword == '罚息') {
                   amount = repurchaseDetail.repurchasePenalty;
                } else {
                   amount = repurchaseDetail.repurchaseOtherCharges;
                }

                return amount;
            }
    	}
    }
</script>
