<template>
    <Modal v-model="show">
        <ModalHeader title="添加账户"></ModalHeader>
        <ModalBody align="left">
            <el-form
                :model="currentModel" 
                :rules="rules" 
                ref="form"
                class="sdf-form sdf-modal-form"
                label-width="120px">
                <el-form-item prop="accountName" label="开户名称" required>
                    <el-input class="middle" v-model.trim="currentModel.accountName"></el-input>
                </el-form-item>
                <el-form-item prop="accountNo" label="开户号" required>
                    <el-input class="middle" v-model.trim="currentModel.accountNo"></el-input>
                </el-form-item>
                <el-form-item prop="bankName" label="开户行名" required>
                    <el-select class="middle" v-model="currentModel.bankName">
                        <el-option 
                            v-for="item in bankNames" 
                            :label="item.value"
                            :value="item.value">
                        </el-option>
                    </el-select>
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
            },
            identification: {
                type: String,
                default: ''
            }
        },
        computed: {
            ...mapState({
                bankNames: state => {
                    var arr = [];
                    var bankNames = state.financialContract.bankNames;
                    Object.keys(bankNames).forEach(bankCode => {
                        arr.push({
                            key: bankNames[bankCode],
                            value: bankNames[bankCode]
                        });
                    });
                    return arr;
                }
            })
        },
        data: function() {
            return {
                show: this.value,
                currentModel: Object.assign({}, this.model),
                rules: {
                    accountNo: { required: true, message: ' ', trigger: 'blur', transform: value => value.trim() },
                    accountName: { required: true, message: ' ', trigger: 'blur', transform: value => value.trim() },
                    bankName: { required: true, message: ' ', trigger: 'change' }
                }
            };
        },
        watch: {
            model: function(cur) {
                this.currentModel = Object.assign({
                    accountNo: '',
                    accountName: '',
                    bankName: '',
                    identification: this.identification
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

        beforeMount: function() {
            this.$store.dispatch('getBankNames');
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