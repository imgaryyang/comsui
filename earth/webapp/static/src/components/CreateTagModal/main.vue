<style lang="sass">
    #create-tag-modal {
        .transitivity-list-move {
            transition: transform 0.5s;
        }
        .default-list-move {
            transition: transform 0s;
        }
        .ghost {
            opacity: .5;
            background: #C8EBFB;
        }

        .tag_amount {
            color: #999999;

            .highlight {
                color: red;
                padding: 0 5px;
            }

            .icon-tag-download {
                background-position: -378px -212px;
                width: 16px;
                height: 16px;
            }
            .download {
                margin-left: 10px;
                color: #3462a7;
                cursor: pointer;
            }
            .checkfile {
                border-radius: 2px;
                border-color: #74b734;
                color: #74b734;
                background: #ffffff;
                &:hover, &:focus {
                    border-color: #74b734!important;
                    color: #74b734!important;
                }
                .progress-bg {
                    background-color: #74b734;
                    opacity: 0.2;
                    position: absolute;
                    top: 0;
                    left: 0;
                    height: 100%;
                }
            }
            .upload {
                max-width: 130px;
                display: inline-block;
                vertical-align: middle;
                float: right;
                height: 36px;
            }
        }
        .search_icon {
            .el-input__icon {
                color: #3462a7!important;
                cursor: pointer;
            }
        }
        .create_tag {
            min-height: 40px;
            border: 1px solid #e0e0e0;
            border-radius: 2px;
            padding: 0 15px;
            box-sizing: border-box;
            max-height: 150px;
            overflow: auto;

            .draggable-title {
                float: left;
                color: rgb(153, 153, 153);
                margin-right: 10px;
            }
            .tag {
                position: relative;
                vertical-align: middle;
                display: inline-block;
                background: #436ba7;
                color: #ffffff;
                padding: 4px 8px;
                line-height: 20px;
                margin-right: 10px;
                max-width: 200px;
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                padding-right: 20px;
                cursor: move;

                &.default {
                    cursor: pointer;
                }

                span {
                    position: absolute;
                    right: 5px;
                    top: 8px;
                    font-size: 10px;
                    margin-left: 5px;
                    transform: scale(0.6);
                    cursor: pointer;
                }
            }
            .create_input {
                display: inline-block;
                border: none!important;
                outline: none!important;
                padding: 0;
                color: #666;
                font-size: 14px;
                margin-left: 5px;
                vertical-align: baseline;
                -webkit-appearance: none;
                -moz-appearance: none;
                appearance: none;
                height: 20px;
                background-color: transparent;
                max-width: 80%;
            }
        }
    }
</style>
<template>
    <Modal v-model="visible" id="create-tag-modal">
        <ModalHeader :title="action === 'add' ? '添加标签' : '删除标签'"></ModalHeader>
        <ModalBody align="left">
            <el-form
                @submit.native.prevent
                ref="form"
                class="sdf-form"
                style="margin-left: 0;"
                label-width="0">
                <el-form-item>
                    <div class="tag_amount">
                        当前为<span class="highlight">{{ uusize }}</span>个<span class="highlight">{{ tagType }}</span>{{action === 'add' ? '添加标签' : '删除标签'}}
                        <span class="icon icon-tag-download download" v-if="batch" @click="downLoadFile"></span>
                        <!-- <el-button size="mini" class="pull-right checkfile" v-if="batch">选择文件</el-button> -->
                        <el-upload
                            class="upload"
                            v-if="batch"
                            :show-upload-list="false"
                            :on-success="onUploadFileSuccess"
                            :on-progress="getUploadPercent"
                            :on-error="onUploadError"
                            :before-upload="onBeforeUpload"
                            :data="{'type': type}"
                            accept=".txt"
                            :action="`${api}/tag/add/up`">
                            <el-button class="button-multimedia pull-right checkfile">
                                <span class="progress-bg" :style="{width: percent + '%'}"></span>
                                <span v-if="!percent">选择文件</span>
                                <span v-else>已校验{{ Number(percent).toFixed(2) }}%<span v-if="percent !== 100">...</span></span>
                            </el-button>
                        </el-upload>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="fliterWords" icon="search" @change="handlefilterOptions" @click="handlefilterOptions" class="search_icon"></el-input>
                </el-form-item>
                <el-form-item>
                    <TagList v-model="currentCheckTag" @delect="delectCheckTag(arguments[0])" :options="filterOptions" :bothTagList="bothTagList"></TagList>
                </el-form-item>
                <template v-if="isAddWith">
                    <el-form-item>
                        <DraggableInput
                            v-model="transitivityTagList"
                            :defaultData="filterTransitivityTagList"
                            :bothTagList="bothTagList"
                            type="transitivity"
                            draggableTitle="联动">
                        </DraggableInput>
                    </el-form-item>
                    <el-form-item>
                        <DraggableInput
                            v-model="defaultTagList"
                            :defaultData="filterDefaultTagList"
                            :bothTagList="bothTagList"
                            type="default"
                            draggableTitle="非联">
                        </DraggableInput>
                        <span style="color: #999999;">输入文字，按回车以生成标签</span>
                    </el-form-item>
                </template>
                <el-form-item v-else>
                    <div class="create_tag" @click="getFocus" ref="reference">
                        <div class="tag default" v-for="tag in defaultTagList">{{ tag }}<span class="el-icon-close" @click="deletTag(tag)"></span></div>
                        <input
                            v-model="tagName"
                            autocomplete="off"
                            class="create_input"
                            :style="{ width: inputLength + 'px' }"
                            @keyup.enter.prevent="createTag"
                            ref="createInput">
                        </input>
                    </div>
                    <span style="color: #999999;">输入文字，按回车以生成标签</span>
                </el-form-item>
            </el-form>
        </ModalBody>
        <ModalFooter>
            <el-button type="default" @click="visible = false">取消</el-button>
            <el-button type="success" @click="submit" :loading="loading">确定</el-button>
        </ModalFooter>
    </Modal>
</template>
<script>
    import TagList from './TagList';
    import DraggableInput from './DraggableInput';
    import MessageBox from 'components/MessageBox';
    import { ajaxPromise, downloadFile } from 'assets/javascripts/util';
    import draggable from 'vuedraggable';

    export default {
        components: {
            TagList, draggable, DraggableInput
        },
        props: {
            value: {
                type: Boolean,
                default: false
            },
            options: Array,
            tags: Array,
            batch: Boolean,
            size: [Number,String],
            type: Number,
            action: {
                type: String,
                default: 'add'
            },
            isAddWith: { //是否显示联动
                type: Boolean,
                default: false
            },
            maxSize: [Number, String]
        },
        data: function() {
            return {
                visible: this.value,
                fliterWords: '', //搜索标签过滤词
                filterOptions: this.options, //筛选过的标签列表
                checkedTags: this.tags.map(item => item.tagName), //选中的tag
                tagName: '',    //输入新增的标签
                inputLength: 20,
                tagList: this.tags.map(item => item.tagName),  //当前所有的tags
                loading: false,

                fileKey: '',
                uusize: 0,
                percent: 0, //文件上传进度

                transitivityTagList: [],//联动数组
                defaultTagList: [],//非联动数组
                currentCheckTag: '',//当前选择的tag
            }
        },
        computed: {
            tagType: function() {
                return { 0: '还款计划', 1: '贷款合同'}[this.type]
            },
            filterTransitivityTagList: function() {
                return this.tags.filter(tag => tag.transitivity).map(item => item.tagName);
            },
            filterDefaultTagList: function() {
                return this.tags.filter(tag => !tag.transitivity).map(item => item.tagName);
            },
            bothTagList: function() {
                return this.transitivityTagList.concat(this.defaultTagList);
            }
        },
        watch: {
            options: function(current) {
                this.filterOptions = current;
            },
            value: function(current) {
                if(!current){
                    this.loading = false;
                    this.percent = 0;
                    this.fileKey = '';
                }
                this.visible = current;
                this.uusize = this.size;
            },
            visible: function(current) {
                if(current) {
                    this.tagList = this.tags.map(item => item.tagName);
                    this.checkedTags = this.tags.map(item => item.tagName);
                    this.fliterWords = '';
                    this.tagName = '';
                    this.transitivityTagList = [];
                    this.defaultTagList = [];
                    this.handlefilterOptions();
                }
                if (!this.isAddWith) {
                    this.defaultTagList = [].concat(this.filterDefaultTagList);
                }
                this.$emit('input', current);
            },
            tagName: function (current) {
                if(current != ''){
                    this.inputLength = this.$refs.input.value.trim().length * 15 + 40;
                }else {
                    this.inputLength = 40;
                }
            },
            currentCheckTag: function(current) {
                if (current == '') return;
                if (this.isAddWith) {
                    this.transitivityTagList.push(current);
                } else {
                    this.defaultTagList.push(current);
                }
                this.currentCheckTag = '';
            }
        },
        methods: {
            handlefilterOptions: function() {
                this.filterOptions = this.options.filter(item => item.tagName.indexOf(this.fliterWords.trim()) != -1);
            },
            getFocus: function() {
                this.$refs.createInput.focus();
            },
            createTag: function (name) {
                if(this.tagName.trim() != '' && this.defaultTagList.findIndex(item => item == this.tagName) == -1){
                    this.defaultTagList.push(this.tagName);
                }
                this.tagName = '';
                this.inputLength = 40;
            },
            deletTag: function(tag) {
                this.defaultTagList = this.defaultTagList.filter(item => item != tag);
            },
            submit: function() {
                this.loading = true;
                if(!this.batch){
                    if (this.isAddWith) {
                        this.$emit('submit-add-with', this.transitivityTagList, this.defaultTagList);
                    } else {
                        this.$emit('submit', this.defaultTagList);
                    }
                }else {
                    if(this.uusize > this.maxSize){
                        MessageBox.open(`批量${this.action === 'add' ? '添加' : '删除'}上限为${this.maxSize}条`);
                        this.loading = false;
                    } else {
                        this.$emit('batch-submit', this.isAddWith ? {
                            fileKey: this.fileKey,
                            type: this.type,
                            actionType: this.action,
                            transitivityTagList: JSON.stringify(this.transitivityTagList),
                            defaultTagList: JSON.stringify(this.defaultTagList),
                        } : {
                            fileKey: this.fileKey,
                            type: this.type,
                            actionType: this.action,
                            defaultTagList: JSON.stringify(this.defaultTagList),
                        });
                    }
                }
            },
            downLoadFile: function() {
                if(this.fileKey !== ''){
                    downloadFile(`${this.api}/tag/add/download`, {fileKey: this.fileKey});
                }
            },
            onUploadFileSuccess: function(response, file, fileList) {
                if (response.code == 0){
                  this.fileKey = response.data.fileKey;
                  this.uusize = response.data.size;
                  return;
                }
                this.percent = 0;
                this.fileKey = '';
                this.uusize = this.size;
                MessageBox.open(response.message);
            },
            getUploadPercent: function (e) {
                this.percent = e.percent;
            },
            onBeforeUpload: function(file) {
                if (file.type != 'text/plain') {
                    MessageBox.open('仅支持txt格式,以换行符隔开数据');
                    return false;
                }
                this.percent = Math.random().toFixed(2);
            },
            onUploadError: function(err, response, file) {
                this.fileKey = '';
                this.uusize = this.size;
            },
            delectCheckTag: function(tagName) {
                var index = this.transitivityTagList.findIndex(tag => tagName == tag);
                if (index != -1) {
                    this.transitivityTagList.splice(index,1);
                    return
                }

                var index1 = this.defaultTagList.findIndex(tag => tagName == tag);
                if (index1 != -1) {
                    this.defaultTagList.splice(index1,1);
                }
            }
        }

    }
</script>
