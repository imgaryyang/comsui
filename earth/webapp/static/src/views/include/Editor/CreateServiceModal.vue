<style lang="sass">
    
</style>

<template>
    <Modal v-model="visible">
        <ModalHeader title="新增服务"></ModalHeader>
        <ModalBody align="left">
            <el-form
                ref="form"
                :model="fields" 
                :rules="rules" 
                class="sdf-form sdf-modal-form"
                label-width="120px">
                <el-form-item label="商户名称" prop="merchantName" required>
                    <el-select class="middle" v-model="fields.merchantName">
                        <el-option
                          v-for="item in Object.keys(innerScriptOptions)"
                          :label="item"
                          :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="系统服务" prop="sysytemServiceName" required >
                    <el-select class="middle" v-model="fields.sysytemServiceName">
                        <el-option
                          v-for="item in Object.keys(Object(innerScriptOptions[fields.merchantName]))"
                          :label="item"
                          :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="服务号" prop="serviceNum" required >
                    <el-input class="middle" v-model="fields.serviceNum"></el-input>
                </el-form-item>
                <el-form-item label="服务名称" prop="serviceName" required >
                    <el-input class="middle" v-model="fields.serviceName"></el-input>
                </el-form-item>
<!--                 <el-form-item label="信托产品" prop="" required>
                    <el-select class="middle">
                        <el-option
                          v-for="item in []"
                          :label="item.value"
                          :value="item.key">
                        </el-option>
                    </el-select>
                </el-form-item> -->
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button @click="visible = false">取消</el-button>
            <el-button type="success" @click="submit">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise } from 'assets/javascripts/util';
    import { idCard } from 'src/validators';

    export default {
        props: {
            value: Boolean,
            scriptOptions: {
                type: Object,
                default: {}
            },
        },
        data: function() {
            return {
                visible: this.value,
                fields: {
                    merchantName: '',
                    sysytemServiceName: '',
                    serviceNum: '',
                    serviceName: ''
                },
                rules: {
                    merchantName: [{ required: true, message: '请选择商户名称', trigger: 'change' }],
                    sysytemServiceName: [{ required: true, message: '请选择系统服务', trigger: 'change' }],
                    serviceNum: [{trigger: 'change', validator: this.validateServiceNum}],
                    serviceName: [{ required: true, message: '请输入服务名称', trigger: 'blur'}],
                },
                innerScriptOptions:{}
            }
        },
        computed:{
            existedServiceNum:function(){
                if(this.fields.merchantName && this.fields.sysytemServiceName){
                    return Object.values(this.innerScriptOptions[this.fields.merchantName][this.fields.sysytemServiceName]).map(url=>{
                        return ''+url.match(/[\w-]+$/g)
                    })
                }
            }
        },
        watch: {
            visible: function(current) {
                this.$emit('input', current);
                if (!current) {
                    this.$refs.form.resetFields();
                }
            },
            value: function(current) {
                this.visible = current;
            },
            'fields.merchantName': function(cur, pre){
                this.fields.sysytemServiceName = ''
            },
            scriptOptions: function(cur){
                this.innerScriptOptions = Object.assign({} ,cur)
            }
        },
        methods: {
            submit: function() {
                this.$refs.form.validate(valid => {
                    if(valid){
                        this.getUrlDefiner()
                        this.$emit('submit', this.fields)
                        this.visible = false
                    }
                })
            },
            getUrlDefiner: function(){
                var url = Object.values(this.innerScriptOptions[this.fields.merchantName][this.fields.sysytemServiceName])[0]
                this.fields.urlDefiner = url.replace(/[\w-]+$/g, this.fields.serviceNum)
                this.innerScriptOptions[this.fields.merchantName][this.fields.sysytemServiceName][this.fields.serviceName] = this.fields.serviceNum
            },
            validateServiceNum: function(rule, value, callback){
                if(!value){
                    callback(new Error('请输入服务号'))
                }else if(/[\!\*\'\(\)\;\:\@\&\=\+\$\,\\\/\?\#\[\]]/g.test(value)){
                    callback(new Error('服务号不能含有URI相关保留字'));
                }else if(this.existedServiceNum && this.existedServiceNum.includes(value)){
                    callback(new Error('已经存在相同的服务号'))
                }else{              
                    callback()
                }
            }
        }
    }
</script>