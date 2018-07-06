<style lang="sass" scoped>
    .tip{
        font-size: 10px;
        color: #888;
        line-height: 14px;
        margin-top: 2px;
    }
</style>

<template>
    <Modal v-model="visible">
        <ModalHeader title="提现"></ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                class="sdf-form sdf-modal-form"
                label-width="120px">
                <el-form-item label="提现卡号">
                    <el-autocomplete
                        class="middle"
                        :fetch-suggestions="querySearch"
                        placeholder="请输入内容"
                        @select="handleSelect"
                    ></el-autocomplete>
                </el-form-item>
                <el-form-item label="持卡人姓名">
                    <el-input class="middle"></el-input>
                </el-form-item>
                <el-form-item label="提现方式">
                    <el-select class="middle"></el-select>
                </el-form-item> 
                <el-form-item label="提现金额">
                    <el-input class="middle" placeholder="默认为全部金额"></el-input>
                    <div class="tip">可提现金额： 100.00</div>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input class="middle" placeholder="选填"></el-input>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="visible=false">取消</el-button>
            <el-button type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import { mapState } from 'vuex';

    export default {
        props: {
            value: Boolean,
            model: {}
        },
        data: function() {
            return {
                visible: this.value,
                fields: {},
            }
        },
        watch: {
            visible: function(current) {
                this.$emit('input', current);
            },
            value: function(current) {
                this.visible = current;
            },
        },
        methods: {
            submit: function() {
                
            },
            querySearch: function() {

            },
            handleSelect: function(){

            }
        }
    }
</script>