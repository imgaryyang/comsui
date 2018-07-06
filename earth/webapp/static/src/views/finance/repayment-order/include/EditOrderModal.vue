<style lang="sass">
	@import '~assets/stylesheets/base';

	#editOrderModal {
		@include min-screen(768px) {
            .modal-dialog {
                width: 90%;
                margin: 30px auto;
            }
        }

        .error-input {
        	.el-input__inner {
	        	border: 1px solid #ff4949
        	}
        }

        .datetimepicker-trigger {
        	.glyphicon {
	        	margin-top: 10px
	        }
        }

	}
</style>

<template>
	<Modal v-model="show" id="editOrderModal">
		<ModalHeader title="编辑订单">
		</ModalHeader>
		<ModalBody>
			<div>
				<StepTip v-model="stepIndex" style="display: flex">
					<StepTipItem :index="1">确认作废明细</StepTipItem>
					<StepTipItem :index="2">新增业务明细</StepTipItem>
				</StepTip>
				<StepContent v-model="stepIndex">
					<StepContentItem :index="1">
						<div>
							<el-table
				                :data="cancelItem"
				                class="td-15-padding th-8-15-padding no-th-border"
				                stripe
				                border>
				                <el-table-column label="业务编号" prop="repaymentBusinessNo">
								</el-table-column>
								<el-table-column label="商户还款计划编号" prop="outerRepaymentPlanNo">
								</el-table-column>
								<el-table-column label="贷款合同编号" prop="contractNo">
								</el-table-column>
								<el-table-column label="设定还款时间" prop="repaymentPlanTime" inline-template>
									<div>{{ row.repaymentPlanTime | formatDate('yyyy-MM-dd HH:mm:ss')}}</div>
								</el-table-column>
								<el-table-column label="还款方式" prop="repaymentWayCh" >
								</el-table-column>
								<el-table-column label="还款金额" prop="amount" inline-template>
									<el-popover trigger="hover">
										<template v-if="row.repaymentChargesDetail">
											<div v-if="!row.repurchaseWay">
												<div>还款本金: {{ row.repaymentChargesDetail.loanAssetPrincipal | formatMoney  }}</div>
												<div>还款利息: {{ row.repaymentChargesDetail.loanAssetInterest | formatMoney  }}</div>
												<div>贷款服务费: {{ row.repaymentChargesDetail.loanServiceFee | formatMoney  }}</div>
												<div>技术服务费: {{ row.repaymentChargesDetail.loanTechFee | formatMoney  }}</div>
												<div>其他费用: {{ row.repaymentChargesDetail.loanOtherFee | formatMoney  }}</div>
												<div>逾期罚息: {{ row.repaymentChargesDetail.overdueFeePenalty | formatMoney  }}</div>
												<div>逾期违约金: {{ row.repaymentChargesDetail.overdueFeeObligation | formatMoney  }}</div>
												<div>逾期服务费: {{ row.repaymentChargesDetail.overdueFeeService | formatMoney  }}</div>
												<div>逾期其他费用: {{ row.repaymentChargesDetail.overdueFeeOther | formatMoney  }}</div>
											</div>
											<div v-else>
												<div>回购本金: {{ row.repaymentChargesDetail.repurchasePrincipal | formatMoney  }}</div>
												<div>回购利息: {{ row.repaymentChargesDetail.repurchaseInterest | formatMoney  }}</div>
												<div>回购罚息: {{ row.repaymentChargesDetail.repurchasePenalty | formatMoney  }}</div>
												<div>回购其他费用: {{ row.repaymentChargesDetail.repurchaseOtherFee | formatMoney   }}</div>
											</div>
										</template>
										<span slot="reference">{{ row.amount | formatMoney }}</span>
									</el-popover>
								</el-table-column>
								<el-table-column label="校验状态" prop="checkStatus">
								</el-table-column>
								<el-table-column label="核销状态" prop="detailPayStatusCh" >
								</el-table-column>
				            </el-table>
						</div>
					</StepContentItem>
					<StepContentItem :index="2">
						<div>
							<div class="bd">
				                <el-table
				                    :data="createItems"
				                    class="td-15-padding th-8-15-padding no-th-border"
				                    style="width: 100%"
				                    stripe
				                    border>
				                    <el-table-column label="贷款合同编号" prop="contractNo">
				                    </el-table-column>
				                    <el-table-column inline-template label="业务编号" prop="repaymentBusinessNo" width="180px">
				                        <div>
				                            <el-select class="small"  :value="createItems[$index].repaymentBusinessNo" @input="repaymentBusinessNoSelect(arguments[0], $index)" :class="{'error-input': !createItems[$index].repaymentBusinessNo && !createItems[$index].repayScheduleNo && isCheck}">
							                    <el-option 
							                        v-for="item in repaymentBusinessNoList" 
							                        :label="item"
							                        :value="item">
							                    </el-option>
							                </el-select>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="商户还款计划编号" prop="repayScheduleNo" width="130px">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].repayScheduleNo" :class="{'error-input': !createItems[$index].repaymentBusinessNo && !createItems[$index].repayScheduleNo && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="设定还款时间" prop="repaymentPlanDate" width="175px">
				                        <div>
				                            <DateTimePicker v-model="createItems[$index].repaymentPlanDate" :pickTime="true"></DateTimePicker>
				                        </div>
				                    </el-table-column>
				                    <el-table-column label="还款方式" prop="repaymentWayCh">
				                    </el-table-column>
				                    <el-table-column inline-template label="还款本金" prop="principal">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].principal" :class="{'error-input': createItems[$index].principal === '' && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="还款利息" prop="interest">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].interest" :class="{'error-input': createItems[$index].interest === '' && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="贷款服务费" prop="serviceFee">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].serviceFee" :class="{'error-input': createItems[$index].serviceFee === '' && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="技术维护费" prop="techFee">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].techFee" :class="{'error-input': createItems[$index].techFee === '' && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="其他费用" prop="otherFee">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].otherFee" :class="{'error-input': createItems[$index].otherFee === '' && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="逾期罚息" prop="overdueFeePenalty">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].overdueFeePenalty" :class="{'error-input': createItems[$index].overdueFeePenalty === '' && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="逾期违约金" prop="overdueFeeObligation">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].overdueFeeObligation" :class="{'error-input': createItems[$index].overdueFeeObligation === '' && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="逾期服务费" prop="overdueFeeService">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].overdueFeeService" :class="{'error-input': createItems[$index].overdueFeeService === '' && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column inline-template label="逾期其他费用" prop="overdueFeeOther">
				                        <div>
				                            <el-input size="small" v-model="createItems[$index].overdueFeeOther" :class="{'error-input': createItems[$index].overdueFeeOther === '' && isCheck}"></el-input>
				                        </div>
				                    </el-table-column>
				                    <el-table-column  
				                        inline-template 
				                        label="操作">
				                        <div>
				                            <a class="color-danger" href="#" @click.prevent="handleDeleteChanges($index)">删除</a>
				                        </div>
				                    </el-table-column>
				                </el-table>
				            </div>
				            <div class="ft text-align-center" style="background: #fff;line-height: 28px">
				                <a href="#" @click.prevent="handleCreate">+  新增</a>
				            </div>
						</div>
					</StepContentItem>
				</StepContent>
				<StepFooter
					v-model="stepIndex" 
	            	:step-number="stepNumber" 
	            	:disabled="disabledNextBtn"
	            	@complete="complete"
	            	@cancel="show = false">
				</StepFooter>
			</div>
		</ModalBody>
	</Modal>
</template>

<script>
	import modalMixin from './modal-mixin';
	import { StepContent, StepContentItem, StepTip, StepTipItem, StepFooter } from 'components/StepOperation';
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';
    import format from 'filters/';

	export default {
		mixins: [modalMixin],
		components: {
			StepContent, StepContentItem, StepTip, StepTipItem, StepFooter
		},
		props: {
			model: {
				default: () =>({})
			},
		},
		data: function() {
			return {
				stepIndex: 1,
				stepNumber: 2,
				show: this.value,
				isCheck: false,
				submiting: false,
				createItems: []
			}
		},
		watch: {
			model: function(current) {
				this.stepIndex = 1;
				this.isCheck = false;
				this.createItems = [];
				this.createItems.push(Object.assign({}, {repaymentWayCh: current.cancelItem.repaymentWayCh}, current.itemModel[0]));
			}
		},
		computed: {
			disabledNextBtn: function() {
				return  this.submiting ;
			},
			repaymentBusinessNoList: function() {
				return  this.model.itemModel && this.model.itemModel.map(item => item.repaymentBusinessNo);
			},
			cancelItem: function() {
				return $.isEmptyObject(this.model) ? [] : [this.model.cancelItem]
			}
		},
		methods: {
			complete: function() {
				setTimeout(() => {
					this.isCheck = true;
				}, 0);

				const isAnyEmpty = (res) => {
					let result = false;
					Object.keys(res).forEach(key => {
				        var value = res[key];
				        if (key == 'repaymentBusinessNo' || key == 'repayScheduleNo' || key == 'repaymentPlanDate') return;
						if (value === '') result = true;
					});

					if (res.repayScheduleNo == '' && res.repaymentBusinessNo == '') {
						result = true
					}
					return result;
				}

				var error = this.createItems.some(item => isAnyEmpty(item));
				if (error) return;

				if (this.submiting) return;
				this.submiting = true;
				ajaxPromise({
					url: `/repayment-order/${this.model.orderUuid}/lapseAndCreateItems`,
					type: 'post',
					data: {
						lapsedItemUuid: this.model.cancelItem.orderDetailUuid,
						items: JSON.stringify(this.createItems)
					}
				}).then(data => {
					MessageBox.open('系统任务已增加！');
                    setTimeout(() => {
						this.show = false;
                        location.reload();
                    }, 500);
				}).catch(message => {
					MessageBox.open(message);
				}).then(() => {
					this.submiting = false;
				})
			},
			handleCreate: function() {
				this.isCheck = false;
				this.createItems.push(Object.assign({}, {
					repaymentWayCh: this.model.cancelItem.repaymentWayCh,
					contractNo: this.model.itemModel[0].contractNo,
					interest: '',
					otherFee: '',
					overdueFeeObligation: '',
					overdueFeeOther: '',
					overdueFeePenalty: '',
					overdueFeeService: '',
					principal: '',
					repayScheduleNo: '',
					repaymentBusinessNo: '',
					repaymentPlanDate: '',
					serviceFee: '',
					techFee: '',
					totalAmount: ''
				}));
			},
			handleDeleteChanges: function(index) {
				this.createItems.splice(index, 1);
			},
			repaymentBusinessNoSelect: function(repaymentBusinessNo, index) {
				var rbnIndex = this.repaymentBusinessNoList.findIndex(item => item === repaymentBusinessNo);
				var newSource = Object.assign({}, this.model.itemModel[rbnIndex]);
				var self = this;
				setTimeout(() => {
					self.createItems[index] = Object.assign(self.createItems[index], newSource);
				}, 0);
			}
		}
	}
</script>