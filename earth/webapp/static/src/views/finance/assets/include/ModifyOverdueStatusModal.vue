<style>
</style>

<template>
    <Modal v-model="show">
        <ModalHeader title="修改逾期状态">
        </ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="currentModel"
                label-width="120px"
                :rules="rules"
                class="sdf-form sdf-modal-form">
                <el-form-item prop="overdueStatus" label="修改逾期状态" required>
                    <el-select class="middle" v-model="currentModel.overdueStatus">
                        <el-option
                            v-for="(label,value) in auditOverdueStatusList"
                            :label="label"
                            :value="value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item
                        prop="overdueDate"
                        label="逾期起始日期"
                        v-if="currentModel.overdueStatus == 2"
                        required
                        >
                    <DateTimePicker
                        v-model="currentModel.overdueDate"
                        :end-date="actualRecycleDateStr"
                        class="middle"
                    ></DateTimePicker>
                </el-form-item>
                <el-form-item
                    label="修改原因"
                    prop="reason"
                    required>
                    <el-form-item
                        prop="selectReason"
                        style="margin-bottom:20px"
                        v-if="currentModel.overdueStatus == 2"
                        required>
                        <el-select class="middle" v-model="currentModel.selectReason">
                            <el-option v-for="item in reasonType"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-input
                        v-if="visibleTextarea"
                        class="long"
                        type="textarea"
                        v-model="currentModel.reason"
                        placeholder="请输入原因(30个字以内)">
                    </el-input>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="submit" type="success">确定</el-button>
            <el-button @click="show = false">取消</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';
    import formats from 'filters/format';

    export default {
        mixins: [modalMixin],
        props: {
            model: {
              default: () =>({})
            },
            auditOverdueStatusList: {
                default: {}
            },
            assetSetId: {
                default: null
            },
            actualRecycleDate: {
                default: null
            }
        },
        watch: {
            model: function(cur) {
                this.currentModel = Object.assign({
                    overdueStatus: '',
                    reason: '',
                    overdueDate: '',
                    selectReason: '余额不足'
                },cur)
            },
            show: function(cur){
                if (!cur) {
                    this.$refs.form.resetFields();
                }
            }
        },
        data: function() {
            var validateTextAreaReason = (rule, value, callback) => {
                if (this.visibleTextarea) {
                    value ? callback() : callback(new Error(' '));
                } else {
                    callback();
                }
            };
            return {
                show: this.value,
                currentModel: Object.assign({},this.model),
                rules: {
                    overdueStatus: {
                        required: true,
                        message: ' ',
                        trigger: 'change'
                    },
                    reason: [
                        {
                          validator: validateTextAreaReason,
                          required: true,
                          message: ' ',
                          trigger: 'blur'
                        },
                        {
                          max: 30,
                          message: '备注不得大于30个字符串',
                          trigger: 'blur'
                        }
                    ],
                    overdueDate: {
                        required: true,
                        trigger: 'blur, change',
                        message: ' '
                    },
                    selectReason: {
                        required: true,
                        trigger: 'change',
                        message: ' '
                    }
                }
            }
        },
        computed: {
            actualRecycleDateStr: function() {
                return formats.formatDate(this.actualRecycleDate);
            },
            visibleTextarea: function() {
                return this.currentModel.selectReason == '其他' || this.currentModel.overdueStatus != 2;
            },
            reasonType: function() {
                return [{
                        label: '余额不足',
                        value: '余额不足'
                    }, {
                        label: '其他',
                        value: '其他'
                    }]
            }
        },
        methods: {
            getAttrFormCurrentModel: function() {
                var attr = {};
                var { currentModel } = this;

                attr.status = currentModel.overdueStatus;

                if (attr.status == 2) {
                    attr.overdueDate = currentModel.overdueDate;
                    if (currentModel.selectReason == '余额不足') {
                        attr.reason = currentModel.selectReason;
                    } else {
                        attr.reason = currentModel.reason;
                    }
                }else {
                    attr.reason = currentModel.reason;
                }

                return attr;
            },
            submit:function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        ajaxPromise({
                            url:`/assets/${this.assetSetId}/confirm-overdue`,
                            type: 'post',
                            data: this.getAttrFormCurrentModel()
                        }).then(data => {
                            MessageBox.once('close', () => {
                              this.$emit('submit',this.currentModel);
                            });
                            MessageBox.open('修改成功');
                            this.fetchSysLog();
                        }).catch(message => {
                          MessageBox.open(message);
                        })
                    }
                })
            }
        }
    }
</script>
