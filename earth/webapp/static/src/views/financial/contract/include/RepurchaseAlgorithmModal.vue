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
        
        .div-input {
            border: 1px #e0e0e0 solid;
            border-radius: 4px;
            padding: 5px 10px;
            text-align: end;

            &.gray-color {
                color: #999;
            }
        }

    }
</style>
<template>
    <Modal v-model="show" class="addAlgorithmModal">
        <ModalHeader :title="isModify ? `修改${this.keyword}算法` : `添加${this.keyword}算法`"></ModalHeader>
        <ModalBody align="center">
            <div class="div-input" :class="{'gray-color' : currentModel.content == ''}">{{ showContent }}</div>
            <div style="margin: 10px -50px 0px 1px">
                <el-row :gutter="10">
                    <el-col :span="6"><el-button :class="{'active' : currentSelected == '未偿本金'}" @click="handleSelect('未偿本金')">未偿本金</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '1'}" @click="handleSelect('1')">1</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '2'}" @click="handleSelect('2')">2</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '3'}" @click="handleSelect('3')">3</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '+'}" @click="handleSelect('+')">+</el-button></el-col>
                </el-row>
                <el-row :gutter="10">
                    <el-col :span="6"><el-button :class="{'active' : currentSelected == '未偿利息'}" @click="handleSelect('未偿利息')">未偿利息</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '4'}" @click="handleSelect('4')">4</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '5'}" @click="handleSelect('5')">5</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '6'}" @click="handleSelect('6')">6</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '-'}" @click="handleSelect('-')">-</el-button></el-col>
                </el-row>
                <el-row :gutter="10">
                    <el-col :span="6"><el-button :class="{'active' : currentSelected == '未偿罚息'}" @click="handleSelect('未偿罚息')">未偿罚息</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '7'}" @click="handleSelect('7')">7</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '8'}" @click="handleSelect('8')">8</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '9'}" @click="handleSelect('9')">9</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '*'}" @click="handleSelect('*')">*</el-button></el-col>
                </el-row>
                <el-row :gutter="10">
                    <el-col :span="6"><el-button :class="{'active' : currentSelected == '未偿逾期费用合计'}" @click="handleSelect('未偿逾期费用合计')">未偿逾期费用合计</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '0'}" @click="handleSelect('0')">0</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '('}" @click="handleSelect('(')">(</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == ')'}" @click="handleSelect(')')">)</el-button></el-col>
                    <el-col :span="4"><el-button :class="{'active' : currentSelected == '/'}" @click="handleSelect('/')">/</el-button></el-col>
                </el-row>
                <el-row :gutter="10">
                    <el-col :span="7" v-if="false"><el-button :class="{'active' : currentSelected == '%'}" @click="handleSelect('%')">%</el-button></el-col>
                    <el-col :span="11"><el-button :class="{'active' : currentSelected == '.'}" @click="handleSelect('.')">.</el-button></el-col>
                    <el-col :span="11"><el-button :class="{'active' : currentSelected == 'delete'}" @click="handleSelect('delete')" ><i class="icon icon-delete" :class="{'active': currentSelected == 'delete'}"></i></el-button></el-col>
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
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';

    export default {
        props: {
            value: {
                default: false
            },
            model: {
                default: null
            },
            ifFromContract: {
                default: true
            },
            keyword: {
                default: '回购'
            }
        },
        computed: {
            isModify: function() {
                return this.model.content != '';
            },
            showContent: function() {
                return this.currentModel.content == '' ? this.keyword + '算法' : this.currentModel.content;
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
            model: function(cur) {
                if (!cur) return;
                this.currentModel = Object.assign({
                    uuid: '',
                    content: '',
                }, cur); 
            },
            show: function(cur) {
                if (!cur) {
                    this.currentSelected = '';
                    this.currentModel = {};
                }
                this.$emit('input', cur);
            },
            value: function(cur) {
                this.show = cur;
            },
        },
        methods: {
           confirm: function() {
                if (this.ifFromContract) {
                    this.$emit('confirm', this.currentModel.content, this.keyword);
                    return;
                }
                
                if (this.currentModel.content == '') {
                    MessageBox.open('请输入内容！');
                    return;
                }

                if (this.confirming) return;
                this.confirming = true;

                ajaxPromise({
                    url: this.isModify ? `/inputHistory/edit` : `/inputHistory/add`,
                    type: 'post',
                    data: {
                        whatFor: '1',
                        ...this.currentModel
                    }
                }).then(data => {
                    this.show = false;
                    this.$emit('confirm');
                }).catch(message => {
                    MessageBox.open(message);
                }).then(() => {
                    this.confirming = false;
                })
           },
           handleSelect: function(currentSelected) {
                this.currentSelected = currentSelected;
                var content = this.currentModel.content;
                if (currentSelected == 'delete') {
                    this.currentModel.content = content.substring(0, content.length - 1);
                    return;
                }
                this.currentModel.content += currentSelected;
           }
        }
    }
</script>