<template>
    <Modal v-model="show">
        <ModalHeader title="合作信息">
        </ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="currentModel"
                :rules="rules"
                label-width="120px"
                class="sdf-form sdf-modal-form">
                <el-form-item label="时间">{{currentModel.createTime | formatDate('yyyy-MM-dd HH:mmss')}}</el-form-item>
                <el-form-item label="来源">{{currentModel.sourceStr}}</el-form-item>
                <el-form-item label="名称">{{currentModel.name}}</el-form-item>
                <el-form-item label="公司">{{currentModel.companyName}}</el-form-item>
                <el-form-item label="部门">{{currentModel.deptName}}</el-form-item>
                <el-form-item label="工号">{{currentModel.jobNumber}}</el-form-item>
                <el-form-item label="身份证">{{currentModel.idNumber}}</el-form-item>
                <el-form-item label="手机号">{{currentModel.phone}}</el-form-item>
                <el-form-item label="邮箱">{{currentModel.email}}</el-form-item>
                <el-form-item label="附言">{{currentModel.postscript}}</el-form-item>
                <template v-if="processed">
                    <el-form-item label="反馈">{{currentModel.feedback}}</el-form-item>
                    <el-form-item label="结果">
                        <span :class="[currentModel.result ? 'danger-color' : 'success-color']">{{currentModel.result ? '拒绝' : '同意'}}</span>
                    </el-form-item>
                </template>
                <el-form-item
                    v-else
                    required
                    label="反馈"
                    prop="feedback">
                    <el-input
                        class="long"
                        type="textarea"
                        v-model="currentModel.feedback">
                    </el-input>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false" v-if="processed">关闭</el-button>
            <template v-else>
                <el-button @click="handleDisAgree">拒绝</el-button>
                <el-button @click="handleAgree" type="success">同意</el-button>
            </template>
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
            messageUuid: {
                default: null
            },
            processed: {
                type: Boolean,
                default: false
            }
  		},
  		data: function() {
  			return {
                show: this.value,
                currentModel: {
                    feedback: ''
                },
                rules: {
                    feedback: {required: true, message: ' '}
                }
  			}
  		},
        watch: {
            show: function(cur) {
                if (cur) {
                    this.currentModel = Object.assign({}, {feedback: ''});
                    this.fetchDetail();
                } else {
                    this.$refs.form.resetFields();
                }
            }
        },
  		methods: {
            fetchDetail: function() {
                ajaxPromise({
                    url: `/messages/${this.messageUuid}`,
                }).then(data =>{
                    this.currentModel = Object.assign({feedback: ''}, data.detailModel);
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
  			handleAgree: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
          				const postData = {
                            result: 0,
                            feedback: this.currentModel.feedback,
                            messageUuid: this.messageUuid
                        };
                        this.handlePost(postData);
                    }
                })
  			},
            handleDisAgree: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        const postData = {
                            result: 1,
                            feedback: this.currentModel.feedback,
                            messageUuid: this.messageUuid
                        };
                        this.handlePost(postData);
                    }
                })
            },
            handlePost: function(postData) {
                ajaxPromise({
                    type:'post',
                    url: `/messages/${this.messageUuid}`,
                    data: postData
                }).then(data =>{
                    this.show = false;
                    if (postData.result == 0) {
                        location.assign(this.ctx + '#/system/user/' + data.principalId);
                    } else {
                        this.$emit('refresh');
                    }
                }).catch(message => {
                    MessageBox.open(message);
                });
            }
  		}
  	}

</script>

