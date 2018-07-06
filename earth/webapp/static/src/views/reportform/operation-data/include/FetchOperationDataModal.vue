<template>
    <Modal v-model="show">
        <ModalHeader title="更新运营数据"></ModalHeader>
        <ModalBody align="left">
            <el-form
                :model="model"
                :rules="rules"
                ref="form"
                class="sdf-form sdf-modal-form"
                label-width="130px">
                <el-form-item label="更新日期" prop="updateDate" required>
                    <DateTimePicker
                        v-model="model.updateDate"
                        placeholder="请选择日期">
                    </DateTimePicker>
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
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    export default {
        props: {
            value: {
                type: Boolean,
                default: false
            },
        },
        data: function() {
            return {
                show: this.value,
                model: {
                    updateDate: '',
                },
                rules: {
                    updateDate: {required: true, message: ' ', trigger: 'change'}
                },
            }
        },
        watch: {
            show: function(current) {
                this.$emit('input', current);
            },
            value: function(current) {
                this.show = current;
            },
        },
        methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if(valid) {
                        
                    }
                })
            }
        }
    }
</script>