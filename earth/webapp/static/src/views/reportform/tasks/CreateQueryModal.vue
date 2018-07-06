<template>
    <Modal v-model="show">
        <ModalHeader title="新建查询任务"></ModalHeader>
        <ModalBody align="left">
            <el-form
                :model="model" 
                :rules="rules" 
                ref="form"
                class="sdf-form sdf-modal-form"
                label-width="130px">
                <el-form-item prop="reportId" label="请选择报表类型" required>
                    <el-select class="middle"
                        v-model="model.reportId">
                        <el-option
                            v-for="(key,value) in requestIds" 
                            :label="key"
                            :value="value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="appId" :label="$utils.locale('financialContract.appAccount.name')" required>
                    <el-select class="middle"
                        placeholder="请选择"
                        v-model="model.appId">
                        <el-option
                            v-for="item in financialContractQueryModels" 
                            :label="item.label"
                            :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="financialContractUuids" :label="$utils.locale('financialContract')" required>
                    <el-select 
                        ref="multipleSelect"
                        v-model="model.financialContractUuids"
                        placeholder="请选择"
                        class="middle"
                        multiple>
                        <el-select-all-option
                            :options="currentFinancialContracts">
                        </el-select-all-option>
                        <el-option 
                            v-for="item in currentFinancialContracts" 
                            :label="item.contractName"
                            :value="item.financialContractUuid">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="请选择查询时间" required>
                    <el-col :span="9">
                        <el-form-item prop="queryStartDate">
                            <DateTimePicker
                                v-model="model.queryStartDate"
                                :end-date="model.queryEndDate"
                                name="loanEffectStartDate"
                                placeholder="起始日期">
                            </DateTimePicker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="2">
                        <div class="text-align-center color-dim">至</div>
                    </el-col>
                    <el-col :span="9">
                        <el-form-item prop="queryEndDate">
                            <DateTimePicker
                                v-model="model.queryEndDate"
                                :start-date="model.queryStartDate"
                                name="loanEffectEndDate"
                                placeholder="截止日期">
                            </DateTimePicker>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <!-- <el-form-item prop="m" label="下载完成是否自动发送邮件">
                    <el-radio :label="true">是</el-radio>
                    <el-radio :label="false">否</el-radio>
                    <template v-if="true">
                        <br>
                        <el-input class="middle" v-model="" placeholder="邮箱地址"></el-input>
                    </template>
                </el-form-item> -->
            </el-form>
        </ModalBody> 
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise,purify, searchify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    export default {
        props: {
            value: {
                default: false
            },
            financialContractQueryModels: {
                type: Array,
                default: () => {[]}
            },
            requestIds: {
                type: Object,
                default: () => {[]}
            },
        },
        data: function() {
            return {
                show: this.value,
                model: {
                    reportId: '',
                    appId: '',
                    financialContractUuids: [],
                    queryStartDate: '',
                    queryEndDate: ''
                },
                
                rules: {
                    reportId: {required: true, message: ' '},
                    appId: {required: true, message: ' '},
                    financialContractUuids: {required: true, message: ' ',trigger: 'change',type: 'array'},
                    queryStartDate: {required: true, message: ' '},
                    queryEndDate: {required: true, message:' '},
                },

            }
        },
        computed: {
            currentFinancialContracts: function() {
                var result = [];
                this.financialContractQueryModels.forEach(item => {
                    if (item.value == this.model.appId) {
                        result = item.children;
                    }
                });
                return result;
            }
        },
        watch: {
            show: function(current) {
                this.$emit('input', current);
                if (!current) {
                    this.$refs.form.resetFields();
                }
            },
            value: function(current) {
                this.show = current;
                if(current){
                    var popper = this.$refs.multipleSelect.$refs.popper;
                    popper.doDestroy();
                }
            },
            'model.appId': function(current) {
                this.model.financialContractUuids = [];
            }
        },
        methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        var data = JSON.parse(JSON.stringify(this.model));
                        data.financialContractUuids = JSON.stringify(data.financialContractUuids);
                        ajaxPromise({
                            url: `/report/job/add`,
                            data: data,
                            type: 'GET'
                        }).then(data => {
                            this.show = false;
                            MessageBox.open('提交成功');
                            MessageBox.once('close',()=>{
                                this.$emit('submit', data);
                            });
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                });
            }
        }
    }
</script>