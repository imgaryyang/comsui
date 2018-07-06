<template>
    <Modal v-model="show">
        <ModalHeader :title="title">
        </ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="currentModel"
                label-width="120px"
                :rules="rules"
                class="sdf-form sdf-modal-form">
                <el-form-item style="padding: 0;" label="">确定{{ title }}？</el-form-item>
                <el-form-item
                    label="备注"
                    prop="remark"
                    required>
                    <el-input
                        class="long"
                        type="textarea"
                        v-model="currentModel.remark"
                        placeholder="请输入原因备注">
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
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import modalMixin from './modal-mixin';

  	export default {
        mixins: [modalMixin],
  		props: {
            orderUuid: {
                default: null
            },
            title: {
                type: String,
                required: true
            },
            action: {
                type: String,
                required: true
            }
  		},
  		data: function() {
  			return {
                show: this.value,
                currentModel: {
                    remark: ''
                },
  				rules: {
  					remark: [{
                        required: true,
                        message: ' '
                    }]
  				}
  			}
  		},
        watch: {
            show: function(cur) {
                if (!cur) {
                    this.$refs.form.resetFields();
                }
            }
        },
  		methods: {
  			submit: function() {
  				this.$refs.form.validate(valid => {
                    if (valid) {
                        ajaxPromise({
                            url: this.action,
                            data: {
                                orderUuid: this.orderUuid,
                                ...this.currentModel
                            }
                        }).then(data =>{
                            MessageBox.once('close',() => {
                                this.show = false;
                            });
                            MessageBox.once('closed', () => {
                                this.$emit('submit');
                            });
                            MessageBox.open(data.message);
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                });
  			}
  		}
  	}

</script>
