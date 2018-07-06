<style lang="sass">
    .addAlgorithmModal {
        .el-row {
            margin: 0 20px 10px;

            &:last-child {
              margin-bottom: 0;
            }
        }
        .el-col {
            border-radius: 2px;

            button {
                width: 100%;
                background: white;

                &.active {
                    background: #75b734;
                    color: white;
                }
            }
        }
        .icon-delete {
            background-position: -327px -217px;
            width: 30px;
            height: 10px;

            &.active {
                background-position: -325px -227px;
                width: 30px;
                height: 10px;
            }
        }
    }
</style>
<template>
    <Modal v-model="show" class="addAlgorithmModal">
        <ModalHeader :title="isModify ? '修改罚息算法' : '添加罚息算法'"></ModalHeader>
        <ModalBody align="center">
            <div class="div-input" :class="{'gray-color' : currentModel.content == ''}">{{ showContent }}</div>
            <div style="margin: 10px -50px 0px 1px">
                <el-row :gutter="10">
                    <el-col :span="6"><el-button :class="{'active' : currentSelected == '本金'}" @click="handleSelect('本金')">本金</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '1'}" @click="handleSelect('1')">1</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '2'}" @click="handleSelect('2')">2</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '3'}" @click="handleSelect('3')">3</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '+'}" @click="handleSelect('+')">+</el-button></el-col>
                </el-row>
                <el-row :gutter="10">
                    <el-col :span="6"><el-button :class="{'active' : currentSelected == '利息'}" @click="handleSelect('利息')">利息</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '4'}" @click="handleSelect('4')">4</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '5'}" @click="handleSelect('5')">5</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '6'}" @click="handleSelect('6')">6</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '-'}" @click="handleSelect('-')">-</el-button></el-col>
                </el-row>
                <el-row :gutter="10">
                    <el-col :span="6"><el-button :class="{'active' : currentSelected == '未偿本金'}" @click="handleSelect('未偿本金')">未偿本金</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '7'}" @click="handleSelect('7')">7</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '8'}" @click="handleSelect('8')">8</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '9'}" @click="handleSelect('9')">9</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '*'}" @click="handleSelect('*')">*</el-button></el-col>
                </el-row>
                <el-row :gutter="10">
                    <el-col :span="6"><el-button :class="{'active' : currentSelected == '未偿利息'}" @click="handleSelect('未偿利息')">未偿利息</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '0'}" @click="handleSelect('0')">0</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '('}" @click="handleSelect('(')">(</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == ')'}" @click="handleSelect(')')">)</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '/'}" @click="handleSelect('/')">/</el-button></el-col>
                </el-row>
                <el-row :gutter="10">
                    <el-col :span="8"><el-button :class="{'active' : currentSelected == '逾期天数'}" @click="handleSelect('逾期天数')">逾期天数</el-button></el-col>
                    <el-col :span="5"><el-button :class="{'active' : currentSelected == '.'}" @click="handleSelect('.')">.</el-button></el-col>
                    <el-col :span="9"><el-button :class="{'active' : currentSelected == 'delete'}" @click="handleSelect('delete')" ><i <i class="icon icon-delete" :class="{'active': currentSelected == 'delete'}"></i></el-button></el-col>
                    <el-col :span="4" v-if="false"><el-button :class="{'active' : currentSelected == '%'}" @click="handleSelect('%')">%</el-button></el-col>
                </el-row>
            </div>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="confirm" type="success" :loading="confirming">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            value: {
                default: false
            },
            model: {
                default: null
            }
        },
        computed: {
            isModify: function() {
                return this.model.content != '';
            },
            showContent: function() {
                return this.currentModel.content == '' ? '罚息算法' : this.currentModel.content;
            }
        },
        data: function() {
            return {
                confirming: false,
                show: this.value,
                currentSelected: '',

                currentModel: Object.assign({}, this.model),
            };
        },
        watch: {
            show: function(cur) {
                this.$emit('input', cur);
                if (!cur) {
                    this.currentSelected = '';
                    this.currentModel = {};
                }
            },
            value: function(cur) {
                this.show = cur;
            },
            model: function(cur) {
               if (!cur) return;
               this.currentModel = Object.assign({
                    uuid: '',
                    content: '',
               }, cur);
            }
        },
        methods: {
            confirm: function() {
                if (this.currentModel.content == '') {
                    MessageBox.open('请输入内容！');
                    return;
                }

                if (this.confirming) return;
                this.confirming = true;
                ajaxPromise({
                    url: this.isModify ? `/inputHistory/edit` : `/inputHistory/add`,
                    data: {
                        whatFor: '0',
                        ...this.currentModel,
                    },
                    type: 'post',
                }).then(data => {
                    this.show = false;
                    this.$emit('confirm');
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.confirming = false;
                });
            },
            handleSelect: function(currentSelected) {
                this.currentSelected = currentSelected;
                var content = this.currentModel.content;
                if (currentSelected == 'delete') {
                    this.currentModel.content = content.substring(0, content.length - 1);
                    return;
                }
                this.currentModel.content += currentSelected;
           },
        }
    }
</script>