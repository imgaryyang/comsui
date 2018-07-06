<style lang="sass">
    @import '~assets/stylesheets/base';

    #repurchaseDateModal {

        @include min-screen(768px) {
            .modal-dialog {
                width: 20%;
                margin: 30px auto;
            }
        }
        .el-radio {
            margin-right: 10px;
        }
        .dateList {
            margin-top: 15px;

            table {
                border: 1px solid #d9d9d9;
                font-size: 12px;

                tr {
                    background: #f5f5f5;

                    .active {
                        background: #75b734;
                        color: white;
                    }
                }
            }
        }
    }
</style>

<template>
    <Modal v-model="show" id="repurchaseDateModal">
        <ModalHeader title="选择回购日期"></ModalHeader>
        <ModalBody align="center">
            <div>
                <el-radio-group v-model="selectedRepurchaseCycle" v-for="item in repurchaseCycles" @change="changeRepurchaseCycle">
                    <el-radio :label="item.key">{{ item.value }}</el-radio>
                </el-radio-group>
            </div>
            <div class="table dateList" v-if="[1, 2].includes(selectedRepurchaseCycle)">
                <table>
                    <tbody>
                        <tr>
                            <td :class="{'active': selectedNumbers.includes(1)}" @click="handlerSelected(1)">1</td>
                            <td :class="{'active': selectedNumbers.includes(2)}" @click="handlerSelected(2)">2</td>
                            <td :class="{'active': selectedNumbers.includes(3)}" @click="handlerSelected(3)">3</td>
                            <td :class="{'active': selectedNumbers.includes(4)}" @click="handlerSelected(4)">4</td>
                            <td :class="{'active': selectedNumbers.includes(5)}" @click="handlerSelected(5)">5</td>
                            <td :class="{'active': selectedNumbers.includes(6)}" @click="handlerSelected(6)">6</td>
                            <td :class="{'active': selectedNumbers.includes(7)}" @click="handlerSelected(7)">7</td>
                        </tr>
                        <template v-if="selectedRepurchaseCycle == 1">
                            <tr>
                                 <td :class="{'active': selectedNumbers.includes(8)}" @click="handlerSelected(8)">8</td>
                                 <td :class="{'active': selectedNumbers.includes(9)}" @click="handlerSelected(9)">9</td>
                                 <td :class="{'active': selectedNumbers.includes(10)}" @click="handlerSelected(10)">10</td>
                                 <td :class="{'active': selectedNumbers.includes(11)}" @click="handlerSelected(11)">11</td>
                                 <td :class="{'active': selectedNumbers.includes(12)}" @click="handlerSelected(12)">12</td>
                                 <td :class="{'active': selectedNumbers.includes(13)}" @click="handlerSelected(13)">13</td>
                                 <td :class="{'active': selectedNumbers.includes(14)}" @click="handlerSelected(14)">14</td>
                            </tr>
                            <tr>
                                 <td :class="{'active': selectedNumbers.includes(15)}" @click="handlerSelected(15)">15</td>
                                 <td :class="{'active': selectedNumbers.includes(16)}" @click="handlerSelected(16)">16</td>
                                 <td :class="{'active': selectedNumbers.includes(17)}" @click="handlerSelected(17)">17</td>
                                 <td :class="{'active': selectedNumbers.includes(18)}" @click="handlerSelected(18)">18</td>
                                 <td :class="{'active': selectedNumbers.includes(19)}" @click="handlerSelected(19)">19</td>
                                 <td :class="{'active': selectedNumbers.includes(20)}" @click="handlerSelected(20)">20</td>
                                 <td :class="{'active': selectedNumbers.includes(21)}" @click="handlerSelected(21)">21</td>
                            </tr>
                            <tr>
                                 <td :class="{'active': selectedNumbers.includes(22)}" @click="handlerSelected(22)">22</td>
                                 <td :class="{'active': selectedNumbers.includes(23)}" @click="handlerSelected(23)">23</td>
                                 <td :class="{'active': selectedNumbers.includes(24)}" @click="handlerSelected(24)">24</td>
                                 <td :class="{'active': selectedNumbers.includes(25)}" @click="handlerSelected(25)">25</td>
                                 <td :class="{'active': selectedNumbers.includes(26)}" @click="handlerSelected(26)">26</td>
                                 <td :class="{'active': selectedNumbers.includes(27)}" @click="handlerSelected(27)">27</td>
                                 <td :class="{'active': selectedNumbers.includes(28)}" @click="handlerSelected(28)">28</td>
                            </tr>
                            <tr>
                                 <td :class="{'active': selectedNumbers.includes(29)}" @click="handlerSelected(29)">29</td>
                                 <td :class="{'active': selectedNumbers.includes(30)}" @click="handlerSelected(30)">30</td>
                                 <td :class="{'active': selectedNumbers.includes(31)}" @click="handlerSelected(31)">31</td>
                            </tr>
                        </template>
                    </tbody>
                </table>
                <span class="color-danger pull-left" style="margin-top: 10px">*可多选</span>
            </div>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import MessageBox from 'components/MessageBox';
   
    export default {
        props: {
            value: {
                default: false
            },
            model: {
                default: null
            },
            repurchaseCycles: {
                default: () => {[]}
            }
        },
        data: function() {
            return {
                show: this.value,
                selectedNumbers: [],
                selectedRepurchaseCycle: '',
                currentModel: {},
            };
        },
        watch: {
            model: function(current) {
                if (!this.value) return;
                this.selectedRepurchaseCycle = current.repurchaseCycle;
                this.currentModel.repurchaseCycle = current.repurchaseCycle;
                this.selectedNumbers = [].concat(current.daysOfCycle);
            },
            value: function(current) {
                this.show = current;
            },
            show: function(current) {
                if (!current) {
                    this.currentModel = {};
                }
                this.$emit('input', current);
            },
        },
        methods: {
            changeRepurchaseCycle: function(value) {
                if (value == -1 || value == this.currentModel.repurchaseCycle) return;
                this.currentModel.repurchaseCycle = value;
                this.selectedNumbers = [];
            },
            submit: function() {
                var selectedNumbers = this.selectedNumbers;
                if ([1, 2].includes(this.currentModel.repurchaseCycle) && selectedNumbers.length == 0) {
                    MessageBox.open('请选择回购时间');
                    return;
                }
                this.currentModel.daysOfCycle = selectedNumbers;
                this.$emit('submit', this.currentModel);
            },
            handlerSelected: function(selectedNumber) {
                var index = this.selectedNumbers.findIndex(item => item === selectedNumber);
                if (index != -1) {
                    this.selectedNumbers.splice(index, 1);
                } else {
                    this.selectedNumbers.push(selectedNumber);
                }
            }
        }
    }
</script>