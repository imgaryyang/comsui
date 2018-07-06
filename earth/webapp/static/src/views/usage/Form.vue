<style>
    
</style>

<template>
    <div class="content">
        <div class="scroller">
            <div class="form">
                
                <el-form label-position="top">
                    <el-form-item label="内容">
                        <el-input placeholder="请输入内容">
                        </el-input>
                    </el-form-item>

                    <el-form-item label="爱好">
                        <el-select v-model="value" placeholder="请选择" clearable>
                            <el-option
                                v-for="item in options"
                                :label="item.label"
                                :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item>
                        <el-button>默认按钮</el-button>
                        <el-button type="primary">默认按钮</el-button>
                    </el-form-item>
                </el-form>

                <ElFieldset>
                    <div class="fieldset-header">
                        <h5 class="title">基本使用</h5>
                    </div>
                    <div class="fieldset-body">
                        <Field>
                            <ElInput placeholder="无label"></ElInput>
                        </Field>
                        <Field label="有label">
                            <ElInput placeholder="关联信托合同"></ElInput>
                        </Field>
                        <Field label="静态内容">
                            <div class="form-text">我是静态内容</div>
                        </Field>
                        <Field label="多个FormControl">
                            <ElInput placeholder="关联信托合同"></ElInput>
                            <ElInput placeholder="关联信托合同"></ElInput>
                        </Field>
                        <Field label="选项框">
                            <ElSelect :options="[{name: 'ych', value: 1}]"></ElSelect>
                        </Field>
                        <Field label="单选时间">
                            <DateTimePicker></DateTimePicker>
                        </Field>
                        <Field label="时间组">
                            <DateTimePicker></DateTimePicker>
                            <span class="hyphen">至</span>
                            <DateTimePicker></DateTimePicker>
                        </Field>
                        <Field label="有后缀">
                            <ParcelInput placeholder="关联信托合同">
                                <span slot="suffix">层</span>
                            </ParcelInput>
                        </Field>
                        <Field label="有后缀有前缀">
                            <ParcelInput placeholder="关联信托合同">
                                <span slot="prefix">第</span>
                                <span slot="suffix">层</span>
                            </ParcelInput>
                        </Field>
                        <Field label="上传文件">
                            <FileUpload 
                                @add="add"
                                @error="error"
                                :filters="['jpg']"
                                action="/vue/upload" 
                                :autopost="true"></FileUpload>
                        </Field>
                    </div>
                    <div class="fieldset-footer">
                        <Field>
                            <button type="submit" class="btn btn-primary">确定</button>
                            <button class="btn btn-success">预览</button>
                        </Field>
                    </div>
                </ElFieldset>
                <ElFieldset>
                    <div class="fieldset-header">
                        <h5 class="title">缩略块内容</h5>
                    </div>
                    <div class="fieldset-body">
                        <Field label="自动">
                            <SketchItem v-for="item in sketchs">
                                <img v-if="item.stamp" :src="item.stamp" class="stamp">
                                <div class="text">
                                  <p>{{item.title}}</p>
                                  <p>{{item.subtitle}}</p>
                                </div>
                                <span class="operate">
                                  <a class="edit"></a>
                                  <a class="delete"></a>
                                </span>
                            </SketchItem>
                            <div>
                                <button class="btn btn-default">添加</button>
                                <span>可添加多个业主</span>
                            </div>
                        </Field>
                    </div>
                </ElFieldset>
                <ElFieldset>
                    <div class="fieldset-header">
                        <h5 class="title">控制宽度</h5>
                    </div>
                    <div class="fieldset-body">
                        <Field label="自动">
                            <ElInput></ElInput>
                        </Field>
                        <Field label="小号" size="long">
                            <ParcelInput>
                                <span slot="prefix">第</span>
                                <span slot="suffix">层</span>
                            </ParcelInput>
                        </Field>
                        <Field label="小号" size="long">
                            <ElInput></ElInput>
                        </Field>
                        <Field label="小号" size="long">
                            <DateTimePicker></DateTimePicker>
                        </Field>
                        <Field label="大号" size="short">
                            <ParcelInput>
                                <span slot="prefix">第</span>
                                <span slot="suffix">层</span>
                            </ParcelInput>
                        </Field>
                        <Field label="大号" size="short">
                            <ElInput></ElInput>
                        </Field>
                        <Field label="大号" size="short">
                            <DateTimePicker></DateTimePicker>
                        </Field>
                    </div>
                </ElFieldset>
                <ElFieldset layout="block">
                    <div class="fieldset-header">
                        <h5 class="title">块级布局</h5>
                    </div>
                    <div class="fieldset-body">
                        <Field label="Label">
                            <ElInput></ElInput>
                        </Field>
                        <Field label="Label">
                            <ElInput></ElInput>
                        </Field>
                    </div>
                </ElFieldset>
                <ElFieldset>
                    <div class="fieldset-header">
                        <h5 class="title">表单校验</h5>
                    </div>
                    <div class="fieldset-body">
                        <Field label="必填">
                            <ElInput @input.native="handleValidate"></ElInput>
                        </Field>
                    </div>
                </ElFieldset>
            </div>
        </div>
    </div>
</template>

<script>
    import ParcelInput from 'components/Form/ParcelInput';
    import SketchItem from 'components/SketchItem';
    import MessageBox from 'components/MessageBox';

    export default {
        components: {
            ParcelInput, SketchItem
        },
        data: function() {
            return { 
                selected: 'home',
                stepIndex: 1,
                sketchs: [{
                    title: '夜车怒火'
                }, {
                    title: '又是奋斗',
                    subtitle: '是付水电费的',
                    stamp: 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3447995495,1491391978&fm=111&gp=0.jpg'
                }],

                options: [{
                  value: '选项1',
                  label: '黄金糕'
                }, {
                  value: '选项2',
                  label: '双皮奶'
                }, {
                  value: '选项3',
                  label: '蚵仔煎'
                }, {
                  value: '选项4',
                  label: '龙须面'
                }, {
                  value: '选项5',
                  label: '北京烤鸭'
                }],

                value: '',

                result: {}
            };
        },
        methods: {
            add: function(file) {
                console.log(file);
            },
            error: function() {
                MessageBox.open('不支持的格式'); 
            },
            progress: function(file, component) {
                console.log('progress ' + file.error);
            },
            after: function(file, component) {
                console.log('after ' + file.error);
            },
            before: function(file, component) {
                console.log('before ' + file.error);
            },

            handleValidate: function(e) {
                var $validity = e.target.$validity;
                var vm = this;
                $validity.validate(function () {
                    vm.result = $validity.result
                });
            }
        }
    }
</script>