<template>
	<Modal v-model="show">
	    <ModalHeader title="作废"></ModalHeader>
	    <ModalBody>
	        <el-form
	            ref="form"
	            class="sdf-form"
	            style="margin-left: 0;"
	            label-width="0">
	            <div style="margin: 15px 0;">
	                <div>
	                    你确定作废<span class='color-danger'>{{ currentModel.sourceDocumentNo }}</span>充值账单吗？
	                </div>
	                <div>
	                    此操作会将<span class="color-danger">{{ currentModel.bookingAmount | formatMoney }}</span>元从<span class="color">{{ currentModel.virtualAccountName }}</span>账户中转至云信专户
	                </div>
	            </div>
	            <el-form-item>
	                <el-input class="middle" v-model="remark" placeholder="原因备注（选填）"></el-input>
	            </el-form-item>
	            <br>
	        </el-form>
	    </ModalBody>
	    <ModalFooter>
	        <el-button @click="show = false">取消</el-button>
	        <el-button type="success" :loading="revoking" @click="revoke">确定</el-button>
	    </ModalFooter>
	</Modal>
</template>

<script>
	import { ajaxPromise } from 'assets/javascripts/util';
	import MessageBox from 'components/MessageBox';

	export default {
		props: {
			value: Boolean,
			sourceDocumentUuid: String,
			model: Object
		},
		data: function() {
			return {
				fetching: false,
				revoking: false,
				show: this.value,
				currentModel:  Object.assign({
					sourceDocumentNo: '',
		        	sourceDocumentOfRevokeNo: '',
		            summaryRevoke: '',
		            virtualAccountName: '',
		            virtualAccountNo: '',
		            bookingAmount: ''
	        	}, this.model),
				remark: ''
			}
		},
		watch: {
		    value: function(current) {
		        this.show = current;
		        if(current){
		        	this.currentModel = Object.assign({
		        		sourceDocumentNo: '',
			        	sourceDocumentOfRevokeNo: '',
			            summaryRevoke: '',
			            virtualAccountName: '',
			            virtualAccountNo: '',
			            bookingAmount: ''
		        	}, this.model);
		        }
		    },
		    show: function(current) {
		        this.$emit('input', current);
		        if (!current) {
		            this.remark = '';
		            this.currentModel = {};
		        }
		    },
		},
		computed: {
		    revokeList: function() {
		        var res = [];
		        var {
		            sourceDocumentOfRevokeNo,
		            summaryRevoke,
		            virtualAccountName,
		            virtualAccountNo,
		            bookingAmount
		        } = this.currentModel;

		        if (sourceDocumentOfRevokeNo) {
		            res.push({
		                sourceDocumentOfRevokeNo,
		                summaryRevoke,
		                virtualAccountName,
		                virtualAccountNo,
		                bookingAmount
		            });
		        }

		        return res;
		    }
		},
		methods: {
			revoke: function(e) {
                this.revoking = true;

                ajaxPromise({
                    url: '/capital/customer-account-manage/deposit-receipt-list/recharge-cancel',
                    data: {
                        sourceDocumentUuid: this.sourceDocumentUuid,
                        remark: this.remark
                    }
                }).then(data => {
                    var { sourceDocumentNo } = data;
                    var htm = `
                        <div style="margin-top: 25px;">撤销单<span class="color-danger">${sourceDocumentNo}</span>已创建！</div>
                        <div>请前往查询撤销结果状态</div>
                    `;
                    MessageBox.once('close', () => {
                    	this.$emit('submit');
                    });
                    MessageBox.open(htm);
                }).catch(message => {
                    if (message === '余额不足') {
                        MessageBox.open('余额不足！<br>请先将余额支付退款至余额', '提示', [{
                            text: '确定',
                            type: 'success',
                            handler: () => {
                                var href = `${this.ctx}#/capital/account/payment-order?status=-1&key=${this.currentModel.virtualAccountNo}`;
                                location.assign(href);
                            }
                        }]);
                    } else {
                        MessageBox.open(message);
                    }
                }).then(() => {
                    this.revoking = false;
                    this.show = false;
                });
            },
		}
	}
</script>