<template>
    <div>
        <Modal v-model="show">
            <ModalHeader title="添加逾期费用"></ModalHeader>
            <ModalBody align="left">
                <el-form
                    :model="currentModel" 
                    :rules="rules" 
                    ref="form"
                    class="sdf-form sdf-modal-form"
                    label-width="120px">
                    <template v-if="sysCreatePenaltyFlag">
                        <el-form-item key="a" label="费用名目" prop="feeTypeKey" required>
                            <el-select class="middle" v-model="currentModel.feeTypeKey">
                                <el-option 
                                    v-for="item in optionalFeeType" 
                                    :label="item.value"
                                    :value="item.key">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="details" :label="currentModel.feeTypeKey == 0 ? '罚息算法' : '金额'" required>
                            <PenaltyInterestDropdown
                                v-if="currentModel.feeTypeKey == 0"
                                v-model="currentModel.details"
                                :penaltyInterestList="penaltyInterestList"
                                @deleteDropItem="deleteDropItem"
                                @editDropItem="editDropItem"
                                @createDropItem="createDropItem">
                            </PenaltyInterestDropdown>
                            <el-input 
                                v-else
                                class="middle" 
                                v-model.trim="currentModel.details">
                            </el-input>
                        </el-form-item>
                    </template>
                    <template v-else>
                        <div class="color-danger bottom-margin-10">费用计算基数周期及算法由对手方传递</div>
                        <el-form-item key="b" label="费用名目" prop="feeTypeKey" required>
                            <el-select class="middle" v-model="currentModel.feeTypeKey">
                                <el-option 
                                    v-for="item in optionalFeeType" 
                                    :label="item.value"
                                    :value="item.key">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </template>
                </el-form>
            </ModalBody>
            <ModalFooter>
                <el-button @click="show = false">取消</el-button>
                <el-button @click="submit" type="success">确定</el-button>
            </ModalFooter>
        </Modal>

        <PenaltyInterestAlgorithmModal
            v-model="algorithmModal.show"
            :model="algorithmModal.model"
            @confirm="fetchPenaltyInterestList()">
        </PenaltyInterestAlgorithmModal>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import { mapState } from 'vuex';
    import { nonNegativeNumber } from 'src/validators';
    import PenaltyInterestDropdown from './PenaltyInterestDropdown';
    import PenaltyInterestAlgorithmModal from './PenaltyInterestAlgorithmModal';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            PenaltyInterestDropdown,
            PenaltyInterestAlgorithmModal,
        },
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
            optionalFeeType: {
                default: []
            },
            sysCreatePenaltyFlag: Boolean
        },
        data: function() {
            var validateDetails = (rule, value, callback) => {
                if (this.currentModel.feeTypeKey == 0) {
                    this.currentModel.details ? callback() : callback(new Error(' '));
                } else {
                    nonNegativeNumber(this.currentModel.details) ? callback() : callback(new Error('请输入合法金额'));
                }
            };

            return {
                show: this.value,
                currentModel: Object.assign({}, this.model),
                rules: {
                    feeTypeKey: { type: 'number', required: true, message: ' ', trigger: 'blur' },
                    details: { validator: validateDetails, trigger: 'blur'}
                },

                algorithmModal: {
                    show: false,
                    model: {},
                },

                penaltyInterestList: []
            };
        },
        watch: {
            model: function(cur) {
                this.currentModel = Object.assign({
                    feeTypeKey: '',
                    details: ''
                }, cur); 
                this.currentModel.feeTypeKey = + this.currentModel.feeTypeKey;
            },
            show: function(cur) {
                this.$emit('input', cur);
                if (!cur) {
                    this.$refs.form.resetFields();
                } else {
                    this.fetchPenaltyInterestList();
                }
            },
            value: function(cur) {
                this.show = cur;
            },
            'currentModel.feeTypeKey': function(current) {
                if (this.currentModel.details) {
                    this.$refs.form.validateField('details', () => {});
                }
            }
        },
        methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        var d = JSON.parse(JSON.stringify(this.currentModel));
                        if (typeof d.details === 'string') {
                            d.details = d.details.trim();
                        }
                        this.$emit('submit', d);
                    }
                });
            },
            fetchPenaltyInterestList: function() {
                ajaxPromise({
                    url: `/inputHistory`,
                    data: {
                        whatFor: '0',
                    },
                }).then(data => {
                    this.penaltyInterestList = data.list;
                }).catch(message => {
                    MessageBox.open(message);
                });
            },
            deleteDropItem: function(model) {
                ajaxPromise({
                    url: `/inputHistory/del`,
                    data: {
                        uuid: model.uuid
                    },
                    type: 'post',
                }).then(data => {
                    this.fetchPenaltyInterestList();
                });
            },
            editDropItem: function(model) {
                this.algorithmModal.show = true;
                this.algorithmModal.model = model;
            },
            createDropItem: function(model) {
                this.algorithmModal.show = true;
                this.algorithmModal.model = model;
            }
        }
    }
</script>