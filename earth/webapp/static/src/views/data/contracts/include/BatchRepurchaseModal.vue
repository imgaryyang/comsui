<style lang="sass">
    @import '~assets/stylesheets/base';
	#batchRepurchaseModal {
		@include min-screen(768px) {
            .modal-dialog {
                width: 60%;
                margin: 30px auto;
            }
        }
    }
</style>
<template>
    <Modal v-model="show" id="batchRepurchaseModal">
        <ModalHeader title="批量回购"></ModalHeader>
        <ModalBody align="left">
			<div v-if="hasError" class="block">
				<h5 class="hd">
					回购申请处理结果
					<el-button class="pull-right" type="primary" size="small" @click="handleDownloadResultFile">导出回购结果</el-button>
				</h5>
				<PagingTable :data="submitResult.list" :pagination="true">
					<el-table-column 
                        prop="uniqueId"
                        label="贷款合同唯一编号">
                    </el-table-column>
                    <el-table-column 
                        prop="contractNo"
                        label="贷款合同编号">
                    </el-table-column>
                    <el-table-column 
                        prop="principalString"
                        label="回购本金"
                        inline-template>
                        <div>{{ row.principalString}}</div>
                    </el-table-column>
                    <el-table-column 
                        prop="interestString"
                        label="回购利息"
                        inline-template>
                        <div>{{ row.interestString}}</div>
                    </el-table-column>
                    <el-table-column 
                        prop="penaltyInterestString"
                        label="回购罚息"
                        inline-template>
                        <div>{{ row.penaltyInterestString}}</div>
                    </el-table-column>
                    <el-table-column 
                        prop="repurchaseOtherFeeString"
                        label="回购其它费用"
                        inline-template>
                        <div>{{ row.repurchaseOtherFeeString}}</div>
                    </el-table-column>
                    <el-table-column 
                        prop="amountString"
                        label="回购金额"
                        inline-template>
                        <div>{{ row.amountString}}</div>
                    </el-table-column>
                    <el-table-column 
                        prop="checkFailedMsg"
                        label="错误信息">
                    </el-table-column>
				</PagingTable>
			</div>
            <el-form
                v-else
                ref="form"
                :model="currentModel" 
                :rules="rules" 
                class="sdf-form sdf-modal-form"
                label-width="120px">
                <el-form-item prop="appId" :label="$utils.locale('financialContract.appAccount.name')" required>
                    <el-select class="middle"
                        placeholder="请选择"
                        v-model="currentModel.appId">
                        <el-option
                            v-for="item in financialContractQueryModels" 
                            :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item :label="$utils.locale('financialContract')" prop="financialContractUuid" required>
                    <el-select 
                        v-model="currentModel.financialContractUuid"
                        class="middle"
                        :placeholder="$utils.locale('financialContract')">
                        <el-option
                            v-for="item in currentFinancialContracts"
                            :label="item.label"
                            :value="item.financialContractUuid">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="回购业务明细表" prop="file" required>
                    <el-upload
                        action="/contracts/batchRepurchase/import"
                        :on-success="onUploadFileSuccess"
                        :on-error="onUploadError"
                        :show-upload-list="false"
                        style="width: auto; display: inline-block;"
                        >
                        <el-button type="primary" size="small">导入</el-button>
                    </el-upload>
                    <el-button type="primary" size="small" @click="handleDownloadFile">下载模板</el-button>
                </el-form-item>

                <div class="bd" style="margin: 10px 5% 10px 53px;" v-if="repurchaseDetails.dataList.length">
                    <el-table
                        :data="showList"
                        class="td-15-padding th-8-15-padding no-th-border"
                        stripe
                        border>
                        <template>
                            <el-table-column 
                                v-for="(label, key) in repurchaseDetails.headers" 
                                :prop="key"
                                :label="label">
                            </el-table-column>
                        </template>
                    </el-table>
                </div>
                <div class="ft text-align-center" style="margin-bottom:30px;margin-right:60px">
                    <div class="pull-right">
                       <ListStatistics :fillIn="true">
                            <template scope="statistics">
                                <div>回购本金：{{ repurchaseDetails.principal | formatMoney }}</div>
                                <div>回购利息：{{ repurchaseDetails.interest | formatMoney }}</div>
                                <div>回购罚息：{{ repurchaseDetails.penaltyInterest | formatMoney }}</div>
                                <div>回购其他费用：{{ repurchaseDetails.repurchaseOtherFee | formatMoney }}</div>
                                <div>回购总金额：{{ repurchaseDetails.amount | formatMoney }}</div>
                            </template>
                        </ListStatistics>
                        <PageControl 
                            v-model="pageIndex"
                            :size="repurchaseDetails.dataList.length"
                            :per-page-record-number="perPageRecordNumber">
                        </PageControl>
                    </div>
                </div>
            </el-form>
		</ModalBody>
		<ModalFooter>
			<el-button @click="show = false">{{ hasError ? '关闭' : '取消'}}</el-button>
            <el-button @click="submit" type="success" :loading="submitting" v-if="!hasError">提交</el-button>
		</ModalFooter>
	</Modal>
</template>

<script>
	import modalMixin from './modal-mixin';
	import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

	export default {
		mixins: [modalMixin],
		components: {
            ListStatistics: require('views/include/ListStatistics'),
            PagingTable: require('views/include/PagingTable')
		},
		props: {
			financialContractQueryModels: Array,
		},
		data: function() {
			return {
				show: this.value,
                submitting: false,
                hasError: false,
                currentModel: {
                	appId: '',
                	financialContractUuid: '',
                	file: null
                },
                rules: {
                	appId: {required: true, message: ' '},
                	financialContractUuid: {required: true, message: ' '},
				    file: { type: 'object', required: true, message: '请导入文件', trigger: 'change'}
                },

                repurchaseDetails: {
                    dataList: [],
                    headers: [],
                    principal: '',
                    interest: '',
                    penaltyInterest: '',
                    repurchaseOtherFee: '',
                    amount: ''
                },
                submitResult: {
                    batchNo: '',
                    list: []
                },

                pageIndex: 1,
                perPageRecordNumber: 5,
			}
		},
		watch: {
			'currentModel.appId': function(current) {
				this.currentModel.financialContractUuid = '';
			},
            show: function(current) {
                if (current) {
                    this.hasError = false;
                    this.repurchaseDetails = {
                        dataList: [],
                        headers: [],
                        principal: '',
                        interest: '',
                        penaltyInterest: '',
                        repurchaseOtherFee: '',
                        amount: ''
                    };
                    this.currentModel.appId = '';
                    this.currentModel.file = null;
                }
                this.$emit('input', current);
            },
            'repurchaseDetails.dataList': function(current) {
                this.pageIndex = 1;
            }
		},
		computed: {
			currentFinancialContracts: function() {
                var result = [];
                this.financialContractQueryModels.forEach(item => {
                    if (item.value == this.currentModel.appId) {
                        result = item.children;
                    }
                });
                return result;
            },
            showList: function() {
                var { pageIndex, perPageRecordNumber } = this;
                var start = (pageIndex - 1) * perPageRecordNumber;
                var end = start + perPageRecordNumber;
                return this.repurchaseDetails.dataList ? this.repurchaseDetails.dataList.slice(start, end) : this.repurchaseDetails.dataList;
            }
		},
		methods: {
            onUploadFileSuccess: function (response, file, fileList) {
                if (response.code == 0) {
                    this.currentModel.file = file;
                    this.repurchaseDetails = Object.assign(this.repurchaseDetails, response.data);
                } else {
                    this.repurchaseDetails = {
                        dataList: [],
                        headers: [],
                        principal: '',
                        interest: '',
                        penaltyInterest: '',
                        repurchaseOtherFee: '',
                        amount: ''
                    };
                    MessageBox.open(response.message);
                }
            },
            onUploadError: function(err, response, file) {
                this.currentModel.file = null;
                MessageBox.open(response.message);
            },
			handleDownloadFile: function() {
				downloadFile('/contracts/batchRepurchase/export')
			},
            handleDownloadResultFile: function() {
                downloadFile('/contracts/batchRepurchase/download', {batchNo: this.submitResult.batchNo})
            },
			submit: function() {
                var self = this;
                this.$refs.form.validate(vaild => {
                    if (vaild) {
        				MessageBox.open('确定批量回购贷款合同?', '提示', [{
        					text: '取消',
        					handler: () => MessageBox.close()
        				}, {
        					text: '确定',
        					type: 'success',
        					handler: () => {
        						if (this.submitting) return;
        						this.submitting = true;
        						ajaxPromise({
        							url: '/contracts/batchRepurchase/submit',
                                    type: 'post',
                                    data: {
                                        repurchaseDetail: JSON.stringify(this.repurchaseDetails.dataList),
                                        financialContractUuid: this.currentModel.financialContractUuid
                                    }
        						}).then(data => {
                                    MessageBox.close();
                                    if (data.list.length == 0) {
                                        this.show = false;
                                        MessageBox.open('批量回购成功')
                                        return;
                                    }
                                    self.hasError = data.list.length != 0;
                                    self.submitResult.batchNo = data.batchNo || '';
                                    self.submitResult.list = data.list || [];
        						}).catch(message => {
        							MessageBox.open(message);
        						}).then(() => {
        							this.submitting = false;
        						});
        					}
        				}]);

                    }
                });
			}
		}
	}
</script>