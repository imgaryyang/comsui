<style type="sass">

</style>

<template>
    <Modal v-model="show">
        <ModalHeader :title="currentModel.title">
        </ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="currentModel"
                label-width="120px"
                :rules="rules"
                class="sdf-form sdf-modal-form">
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
        model: {
          default: () => ({})
        }
      },
      data: function() {
        return {
          show: this.value,
          currentModel: Object.assign({}, this.model),
          rules: {
            comment: [{
              required: true,
              message: ' ',
              trigger: 'blur',
            }, {
              max: 50,
              message: '原因不得超过50个字符',
              trigger: 'blur'
            }]
          }
        }
      },
      watch: {
        model: function(cur) {
          this.currentModel = Object.assign({
            comment: ''
          }, cur);
        },
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
                this.$emit('submit', this.currentModel);
              }
          })
        }
      }
    }

</script>
