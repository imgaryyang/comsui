<template>
	<Modal v-model="visible">
		<ModalHeader title="限额详情">
		</ModalHeader>
		<ModalBody align="left">
			<TabMenu type="segment" v-model="selected">
			    <TabMenuItem id="LIMITSHEET">修改限额</TabMenuItem>
			    <!-- <TabMenuItem id="LOG">操作日志</TabMenuItem> -->
			</TabMenu>
			<TabContent v-model="selected">
			    <TabContentItem id="LIMITSHEET">
        			<el-form
                        ref="form"
                        :model="internalModel"
                        :rules="rules"
                        style="margin-left: 30px;"
                        class="sdf-form"
                        label-width="120px">
                        <el-form-item style="padding: 0;" label="网关">
                            {{ internalModel.paymentInstitutionName }}
                        </el-form-item>
                        <el-form-item style="padding: 0;" label="商户号">
                            {{ internalModel.outlierChannelName }}
                        </el-form-item>
                        <el-form-item style="padding: 0;" label="银行">
                            {{ internalModel.bankName }}
                        </el-form-item>
                        <el-form-item style="padding: 0;" label="收付类型">
                            {{ internalModel.accountSide }}
                        </el-form-item>
                        <el-form-item label="单笔限额" prop="transactionLimitPerTranscation">
                            <el-input class="middle" v-model="internalModel.transactionLimitPerTranscation"></el-input>
                        </el-form-item>
                        <el-form-item label="单日限额" prop="transcationLimitPerDay">
                            <el-input class="middle" v-model="internalModel.transcationLimitPerDay"></el-input>
                        </el-form-item>
                        <el-form-item label="单月限额" prop="transactionLimitPerMonth">
                            <el-input class="middle" v-model="internalModel.transactionLimitPerMonth"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button @click="visible = false">关闭</el-button>
                            <el-button type="success" :loading="submitting" @click="submit">提交</el-button>
                        </el-form-item>
                    </el-form>
			    </TabContentItem>
			    <!-- <TabContentItem id="LOG">
			    </TabContentItem> -->
			</TabContent>
		</ModalBody>
	</Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import { REGEXPS } from 'src/validators';
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';

	export default {
    		components: {
    			TabMenu, TabMenuItem, TabContent, TabContentItem 
    		},
        props: {
            value: Boolean,
            model: Object
        },
        data: function() {
            var validateTransaction = (rule, value, callback) => {
                var currentModel = this.internalModel;
                var isEmpty = function(value) {
                    return value == '' || value == null;
                };

                if (isEmpty(currentModel.transcationLimitPerDay)) {
                    callback();
                    return;
                }

                if (!isEmpty(currentModel.transcationLimitPerDay) && isEmpty(currentModel.transactionLimitPerTranscation)) {
                    callback(new Error('单日限额存在时必须输入单笔限额'));
                    return;
                }

                var transcationLimitPerDay = + currentModel.transcationLimitPerDay;
                var transactionLimitPerTranscation = + currentModel.transactionLimitPerTranscation;
                var transactionLimitPerMonth = + currentModel.transactionLimitPerMonth;

                if (transactionLimitPerTranscation > transcationLimitPerDay) {
                    callback(new Error('单日限额必须大于单笔限额'));
                } else {
                    if (!isEmpty(currentModel.transactionLimitPerMonth) && transcationLimitPerDay > transactionLimitPerMonth) {
                        callback(new Error('单月限额必须大于单日限额'));
                    } else {
                        callback();
                    }
                }
            };
            return {
            	selected: 'LIMITSHEET',
                visible: this.value,
                submitting: false,
                internalModel: {
                	paymentInstitutionName: '',
                	outlierChannelName: '',
                	bankName: '',
                	accountSide: '',
                    transactionLimitPerTranscation: '',
                    transcationLimitPerDay: '',
                    transactionLimitPerMonth: ''
                },
                rules: {
                	transactionLimitPerTranscation: [
                		{ pattern: REGEXPS.MONEY, message: '请输入正确的金额' },
                        { validator: validateTransaction }
                	],
                	transcationLimitPerDay: [
                		{ pattern: REGEXPS.MONEY, message: '请输入正确的金额' },
                        { validator: validateTransaction }
                	],
                	transactionLimitPerMonth: [
                		{ pattern: REGEXPS.MONEY, message: '请输入正确的金额' },
                        { validator: validateTransaction }
                	],
                }
            }
        },
        watch: {
            value: function(current) {
                this.visible = current;
                if (!current) {
                    this.$refs.form.resetFields();
                }
            },
            visible: function(current) {
                this.$emit('input', current);
            },
            model: function(current) {
            	this.internalModel = Object.assign({}, current);
            }
        },
        methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.submitting = true;

                        ajaxPromise({
                            url: `/paymentchannel/limitSheet/update`,
                            data: this.internalModel,
                            type: 'post'
                        }).then(data => {
                            this.visible = false;
                            this.$emit('submit');
                        }).catch(message => {
                            MessageBox.open(message)
                        }).then(() => {
                            this.submitting = false;
                        });
                    }
                });
            }
        }
    }
</script>