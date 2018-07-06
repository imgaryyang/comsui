<style lang="sass" scoped>
    @import '~assets/stylesheets/base.scss';

    @include min-screen(768px) {
        .editor-library-modal .modal-dialog {
            width: 670px;
        }
    }

    .editor-library-modal {
        .new-library{
            background-color: #fff;
            background-image: none;
            border-radius: 4px;
            border: 1px solid #e0e0e0;
            box-sizing: border-box;
            color: #1f2d3d;
            display: block;
            font-size: inherit;
            height: 34px;
            line-height: 1;
            outline: none;
            padding: 3px 10px;
            transition: border-color .2s cubic-bezier(.645,.045,.355,1);
            width: 100%;
            &:focus{
                border-color: #217ac0
            }
        }
        ul{
            list-style: none;
            margin: 0;
            padding: 0;
            li {
                border-bottom: 1px solid #ededed;
                position: relative;
                .view{
                    padding: 15px 15px 5px;
                    &:hover{
                        .destroyLib{
                            display: block;
                        }
                    }
                    .destroyLib{
                        display: none;
                        border: 0;
                        position: absolute;
                        top: 0;
                        right: 20px;
                        color: #cc9a9a;
                        background-color: #fff;
                        font-size: 30px;
                        transition: color 0.2s ease-out;
                        &:after{
                            content: 'x'
                        }
                    }
                }
            }
        }
    }
</style>

<template>
    <Modal class="editor-library-modal" v-model="show">
        <ModalHeader title="类库选择"></ModalHeader>
        <ModalBody align="left">
            <input v-model="newLibrabry"
                placeholder="请输入需要导入的类库,并按回车键确认" 
                @keyup.enter="addLibrary"
                class="new-library"></input>
            <ul>
                <li v-for="lib in innerLibraries">
                    <div class="view">
                        {{lib}}
                        <button class="destroyLib" @click="removeLibrary(lib)"></button>
                    </div>
                </li>
            </ul>
        </ModalBody>
        <ModalFooter>
            <el-button @click="show = false">取消</el-button>
            <el-button @click="submit" type="success">确定</el-button>
        </ModalFooter>
    </Modal>
</template>

<script>
    export default {
        props: {
            value: {
                default: false
            },
            libraries: {
                type: Array,
                default: () => []
            }
        },
        data: function() {
            return {
                show: this.value,
                innerLibraries: [],
            }
        },
        watch: {
            show: function(current) {
                this.$emit('input', current);
            },
            value: function(current) {
                this.show = current;
                if (!current) {
                    this.selected = [];
                    this.activeLibrary = null;
                }
            },
            libraries: function(cur){
                this.innerLibraries = Object.assign([],cur)
            }
        },
        methods: {
            addLibrary: function(){
                if(this.newLibrabry != '')
                    this.innerLibraries.push(this.newLibrabry)
                this.newLibrabry = ''
            },
            removeLibrary: function(lib){
                 this.innerLibraries.splice(this.innerLibraries.indexOf(lib), 1)
            },
            submit: function() {
                this.$emit('modifyLibrary',this.innerLibraries)
                this.show = false
                this.innerLibraries = []
                this.newLibrabry = ''
            },
        }
    }
</script>
