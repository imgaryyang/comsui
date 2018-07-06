<template>
	<Modal v-model="visible">
		<ModalHeader title="上传限额表">
		</ModalHeader>
		<ModalBody align="left">
			<el-form
                ref="form"
                :model="model"
                :rules="rules"
                class="sdf-form"
                label-width="0">
                <el-form-item prop="paymentInstitutionName" required>
                    <el-select clearable placeholder="选择网关" class="middle" v-model="model.paymentInstitutionName">
                        <el-option v-for="item in gatewayList" :label="item.value" :value="item.key"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="outlierChannelName" required>
                    <el-select clearable placeholder="选择商户号" class="middle" v-model="model.outlierChannelName">
                        <el-option v-for="item in outlierChannelNames" :label="item" :value="item"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="accountSide" required>
                    <el-select clearable placeholder="选择收付类型" class="middle" v-model="model.accountSide">
                        <el-option v-for="item in accountSides" :label="item.key == 0 ? '代付' : '代收'" :value="item.key"></el-option>
                    </el-select>
                    <HelpPopover content="新的通道需配置后再上传限额表"/>
                </el-form-item>
                <el-form-item prop="file" required>
                    <el-upload
                        action="/"
                        accept=".xlsx,.xls"
                        :before-upload="handleBeforeUpload">
                        <el-button class="button-multimedia" style="width: 230px;">
                            <div :style="{
                                'text-align': 'center',
                                'overflow': 'hidden',
                                'text-overflow': 'ellipsis',
                                }">
                                {{ model.file ? model.file.name : '点击上传' }}
                            </div>
                        </el-button>
                    </el-upload>
                    <div>
                        <a target="_blank" style="margin-right:20px" :href="`${api}/paymentchannel/file/download?fileKey=1`">下载限额表模版</a>
                        <a target="_blank" :href="`${api}/paymentchannel/file/download?fileKey=2`">下载银行编号列表</a>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button style="width: 90px; margin-bottom: 15px;" type="primary" :loading="submitting" @click="submit">提交</el-button>
                </el-form-item>
            </el-form>
		</ModalBody>
	</Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import HelpPopover from 'views/include/HelpPopover';

	export default {
        components: {
            HelpPopover
        },
        props: {
            value: Boolean,
            gatewayList: [Object, Array],
            outlierChannelNames: [Object, Array],
            accountSides: [Object, Array]
        },
        data: function() {
            return {
                visible: this.value,
                submitting: false,
                model: {
                    paymentInstitutionName: '',
                    outlierChannelName: '',
                    accountSide: '',
                    file: null
                },
                rules: {
                    paymentInstitutionName: { required: true, message: ' ' },
                    outlierChannelName: { required: true, message: ' ' },
                    accountSide: { required: true, message: ' ' },
                    file: { type: 'object', required: true, message: ' ' }
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
            }
        },
        methods: {
            handleBeforeUpload: function(file) {
                this.model.file = file;
                return false
            },
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        var formData = new FormData();
                        formData.append('file', this.model.file);
                        formData.append('outlierChannelName', this.model.outlierChannelName);
                        formData.append('paymentInstitutionName', this.model.paymentInstitutionName);
                        formData.append('accountSide', this.model.accountSide);

                        this.submitting = true;

                        ajaxPromise({
                            url: `/paymentchannel/file/upload`,
                            data: formData,
                            processData: false,
                            contentType: false,
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