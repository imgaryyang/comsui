<template>
    <Modal v-model="show">
        <ModalHeader title="修改备注">
        </ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="currentModel"
                label-width="120px"
                :rules="rules"
                class="sdf-form sdf-modal-form">
                <el-form-item style="padding: 0;" label="">是否作废提前还款单？</el-form-item>
                <el-form-item
                    label="备注"
                    prop="comment"
                    required>
                    <el-input
                          class="long"
                          type="textarea"
                          v-model="currentModel.comment"
                          placeholder="请输入原因(50个字以内)">
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
            assetSetId: {
                default: null
            },
  		},
  		data: function() {
  			return {
                show: this.value,
                currentModel: Object.assign({}, this.model),
  				rules: {
  					comment: [{
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
                            type:'post',
                            url: `/assets/invalidate`,
                            data: {
                                ...this.currentModel,
                                assetUuid: this.assetSetId
                            }
                        }).then(data =>{
                            MessageBox.once('close',() => {
                                this.show = false;
                            });
                            MessageBox.once('closed', () => {
                                this.$emit('submit');
                                this.fetchSysLog();
                            });
                            MessageBox.open('修改成功');
                        }).catch(message => {
                            MessageBox.open(message);
                        });
                    }
                });
  			}
  		}
  	}

</script>
