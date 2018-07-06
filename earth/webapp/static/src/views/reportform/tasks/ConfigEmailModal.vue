<template>
    <Modal v-model="show">
        <ModalHeader title="新建查询任务"></ModalHeader>
        <ModalBody align="left">
            <el-form
                :model="model" 
                :rules="rules" 
                ref="form"
                class="sdf-form sdf-modal-form"
                label-width="120px">
                <el-form-item prop="bankName" label="邮箱" required>
                    <el-input class="middle"></el-input>
                </el-form-item>
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
            value: {
                default: false
            },
        },
        data: function() {
            return {
                show: this.value,
                model: {},
                rules: {}
            };
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
            }
        },
        methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.$emit('submit');
                    }
                });
            }
        }
    }
</script>