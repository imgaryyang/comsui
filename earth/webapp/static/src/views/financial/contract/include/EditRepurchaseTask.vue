<style lang="sass">
  
</style>
<template>
    <Modal v-model="show" id="repurchaseTaskModal">
        <ModalHeader :title="isUpdate ? '修改回购任务' : '添加回购任务'"></ModalHeader>
        <ModalBody align="left">
            <el-form
                :model="currentModel" 
                :rules="rules" 
                ref="form"
                class="sdf-form"
                label-width="100px">
                <el-form-item prop="repurchaseDate" label="回购时间" required>
                    <DateTimePicker
                        v-model="currentModel.repurchaseDate"
                        placeholder="回购时间"
                        size="middle">
                    </DateTimePicker>
                </el-form-item>
                <el-form-item label="生效时间" required>
                    <el-col :span="8">
                        <el-form-item prop="effectStartDate">
                            <DateTimePicker
                                v-model="currentModel.effectStartDate"
                                :end-date="currentModel.repurchaseDate"
                                placeholder="生效起始日">
                            </DateTimePicker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="2">
                        <div class="text-align-center color-dim">至</div>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item prop="effectEndDate">
                            <DateTimePicker
                                v-model="currentModel.effectEndDate"
                                :start-date="currentModel.repurchaseDate"
                                placeholder="生效截止日">
                            </DateTimePicker>
                        </el-form-item>
                    </el-col>
                </el-form-item>
                <div style="margin-left: 30px;"><span class="color-danger">说明：</span>临时回购任务生效期间，原不定期回购规则冻结</div>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>

    export default {
        props: {
            isUpdate: {
                default: false
            },
            value: {
                default: false
            },
            model: {
                default: null
            }
        },
        data: function() {
            return {
                show: this.value,
                currentModel: Object.assign({}, this.model),
                rules: {
                    repurchaseDate: { required: true, message: ' '},
                    effectStartDate: { required: true, message: ' '},
                    effectEndDate: { required: true, message: ' '},
                }
            };
        },
        watch: {
            model: function(cur) {
                this.currentModel = Object.assign({
                    repurchaseDate: '',
                    effectStartDate: '',
                    effectEndDate: '',
                }, cur); 
            },
            show: function(cur) {
                this.$emit('input', cur);
                if (!cur) {
                    this.$refs.form.resetFields();
                }
            },
            value: function(cur) {
                this.show = cur;
            }
        },
        methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        var d = JSON.parse(JSON.stringify(this.currentModel));
                        this.$emit('submit', d);
                    }
                });
            }
        }
    }
</script>