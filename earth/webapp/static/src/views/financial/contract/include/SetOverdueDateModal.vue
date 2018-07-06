<template>
    <Modal v-model="show">
        <ModalHeader title="设置逾期起始日"></ModalHeader>
        <ModalBody align="left">
            <el-form
                :model="currentModel" 
                :rules="rules" 
                ref="form"
                class="sdf-form sdf-modal-form"
                label-width="120px">
                <el-form-item prop="allowOverdueAutoConfirm" label="逾期起始日" required>
                    <span>计划还款日+</span>
                    <span>还款宽限日+</span>
                    <el-input class="short" v-model.trim="currentModel.allowOverdueAutoConfirm"></el-input>
                    <span>天</span>
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
    import { mapState } from 'vuex';
    import { REGEXPS } from 'src/validators';

    export default {
        props: {
            value: {
                default: false
            },
            model: {
                default: _ => ({})
            }
        },
        computed: {},
        data: function() {
            var validator = (rule, value, callback) => {
                var v = value + ''
                if (v.trim() == "") {
                    callback(new Error(' '))
                    return
                }
                if (isNaN(v) || v.includes('.') || value < 1) {
                    callback(new Error('请输入大于等于1的整数'))
                } else {
                    callback()
                }
            };

            return {
                show: this.value,
                rules: {
                    allowOverdueAutoConfirm: { required: true, trigger: 'change', validator: validator},
                },
                currentModel: Object.assign({}, this.model),
            };
        },
        watch: {
            model: function(cur) {
                this.currentModel = Object.assign({
                    allowOverdueAutoConfirm: '',
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