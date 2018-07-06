<style lang="sass">
    .column-block {
        display: inline-block;
        margin: 0 10px;
        border: 1px solid #999;
        padding: 5px;
        &:hover {
            i {
                visibility: visible;
            }
        }
        i {
            display: inline-block;
            font-size: 10px;
            visibility: hidden;
            cursor: pointer;
            margin-left: 5px;
        }
    }
</style>
<template>
    <Modal v-model="show">
        <ModalHeader title="提示">
        </ModalHeader>
        <ModalBody align="left">
            <div style="margin: 50px auto;text-align: center;" v-if="stepFirst">导出明细总数大约为:{{model.detailNumberSum}}条</div>
            <div v-else>
                <el-form
                    ref="form"
                    label-width="80px"
                    class="sdf-form sdf-modal-form">
                    <el-form-item label="自定义列">
                        <el-select
                            style="width: 200px;"
                            v-model="selectColumn"
                            multiple
                            size="small">
                            <el-select-all-option :options="columnList">
                            </el-select-all-option>
                            <el-option
                                v-for="item in columnList"
                                :label="item"
                                :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <div style="border: 1px solid #bcbcbc;margin-left: 10px;padding: 10px;">
                        选择列: <span class="column-block" v-for="item in selectColumn">{{item}}<i class="el-icon-close" @click="delColumn(item)"></i></span>
                    </div>
                </el-form>
            </div>
        </ModalBody>
        <ModalFooter>
            <el-button @click="stepFirst = false" type="success" v-if="stepFirst">下一步</el-button>
            <el-button @click="submit" type="success" v-else>导出</el-button>
            <el-button @click="show = false">取消</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';

    export default {
        props: {
            value: Boolean,
            model: {
                type: Object,
                default: () => {}
            }
        },
        data: function() {
            return {
                show: this.value,
                stepFirst: true,
                columnList: ['标签', '证件号', '生效日期'],
                selectColumn: ['标签', '证件号', '生效日期'],
            }
        },
        watch: {
            value: function(cur) {
                this.show = cur;
            },
            show: function(cur) {
                if(cur) {
                    this.stepFirst = true;
                    this.columnList = ['标签', '证件号', '生效日期'];
                    this.selectColumn = ['标签', '证件号', '生效日期'];
                }
                this.$emit('input', cur)
            }
        },
        methods: {
            delColumn(item) {
                this.selectColumn = this.selectColumn.filter(row => item !== row );
            },
            submit: function() {
                var params = Object.assign({}, this.model.conditions, {
                    tagFlag: this.selectColumn.includes('标签') ? 1 : 0,
                    contractBeginDateFlag: this.selectColumn.includes('证件号') ? 1 : 0,
                    identificationCardFlag: this.selectColumn.includes('生效日期') ? 1 : 0,
                })
                downloadFile(`${this.api}/repayment-order/exportRepaymentOrderFile`, params);
            }
        }
    }

</script>
