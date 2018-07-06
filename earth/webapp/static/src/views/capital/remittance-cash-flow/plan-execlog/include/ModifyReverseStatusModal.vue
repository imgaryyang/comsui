<template>
    <Modal v-model="show">
        <ModalHeader title="修改冲账状态"></ModalHeader>
        <ModalBody align="left">
        <el-form
            ref="form"
            :model="currentModel"
            label-width="120px"
            :rules="rules"
            class="sdf-form sdf-modal-form">
            <el-form-item prop="reverseStatus" label="修改冲账状态" required>
                <el-select class="middle" v-model="currentModel.reverseStatus">
                    <el-option
                        v-for="item in auditReverseStatusList"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item
                label="备注"
                prop="executionRemark"
                required>
                <el-input
                    class="long"
                    type="textarea"
                    v-model="currentModel.executionRemark"
                    placeholder="请输入备注">
                </el-input>
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
    import { ajaxPromise, searchify, purify } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            value: Boolean,
            model: {
                default: () => ({})
            }
        },
        data: function() {
            return {
                show: this.value,
                currentModel: {
                    reverseStatus:'',
                    executionRemark: ''
                },
                auditReverseStatusList: [],
                rules: {
                    reverseStatus: {
                        required: true,
                        message: ' ',
                        type: 'number'
                    },
                    executionRemark: {
                          required: true,
                          message: ' '
                    }
                }
            }
        },
        watch: {
            value: function(current) {
                this.show = current;
            },
            show: function(current) {
                this.$emit('input', current);
            },
            model: function(cur){
                this.currentModel = Object.assign({
                    execReqNo: '',
                    reverseStatus: '',
                    executionRemark : ''
                },cur);
            },
            'currentModel.reverseStatus': function(cur) {
                if(cur === 'UNOCCUR'){
                    this.auditReverseStatusList= [{label:'已退票',value: 3}]
                }else if(cur === 'NOTREVERSE'){
                    this.auditReverseStatusList= [{label: '已冲账',value: 2}]
                }
            }
        },
        methods: {
            submit:function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        if(this.currentModel.reverseStatus === 3){
                            this.show = false;
                            this.$emit('showAddFlow',this.currentModel);
                            return;
                        }
                        ajaxPromise({
                            url:`/capital/plan/execlog/editReverseStatus`,
                            type: 'post',
                            data: purify(this.currentModel)
                        }).then(data => {
                            MessageBox.once('close', () => {
                              this.$emit('submit',this.currentModel);
                            });
                            MessageBox.open('修改成功');
                        }).catch(message => {
                          MessageBox.open(message);
                        }).then(()=>{
                            this.show = false;
                        });
                    }
                })
            }
        }
    }
</script>