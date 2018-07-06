<style lang="sass">
    .editor-window {
        position: relative;

        .editor-tab {
            line-height: 32px;
        }

        .editor-viewport {
            min-height: 450px;
        }

        .editor-control {
            margin-top: 10px;
        }

        #handle_horizontal{
            z-index: 90;
            width: 100%;
            height: 8px;
            padding: 0 1px;
            cursor: row-resize;
            position: absolute;
            // top: 0;
            left: 0;
            margin-top: -4px;
            background-color: #666;
        }
    }
</style>

<template>
    <div class="editor-window"
        @mousemove="doDrag"
        @mouseup="stopDrag">
        <editor-tab 
            class="clearfix"
            :views="views" 
            :model="currentView" 
            @command="handleMenuCommand"
            @create="handleCreate"
            @select="handleSelect"
            @run="handleRun"
            @save="handleSave"
            @close="handleClose">
        </editor-tab>
        <editor-viewport 
            ref="viewport"
            :style="{height: viewportHeight+'px'}"
            :model="currentView"
            :errorline="errorline"
            :fetching="fetching"
            @change="handleChange">
        </editor-viewport>
        <div id="handle_horizontal" 
            :style="{top: viewportHeight+33+'px'}"
            @mousedown="startDrag"></div>
        <editor-control
            :runResult="runResult"
            :waitingResult="waitingResult">
        </editor-control>
        <editor-library-modal
            v-model="libraryModal.visible"
            :libraries="libraryModal.model"
            @modifyLibrary="handleLibrary">
        </editor-library-modal>
        <editor-create-service-modal
            v-model="createServiceModal.visible"
            :scriptOptions="scriptOptions"
            @submit="handleCreateSubmit">
        </editor-create-service-modal>
    </div>
</template>

<script>
    import { ajaxPromise } from 'assets/javascripts/util';
    import MessageBox from 'components/MessageBox';
    import format from 'filters/format';

    export default {
        props: {
            scriptOptions: {
                type: Object,
                default: {}
            },
            scriptData: {
                type: Object,
                default: {}
            },
            fetching: {
                type: Boolean,
                default: false
            }
        },
        components: {
            'editor-viewport': require('./Viewport'),
            'editor-tab': require('./Tab'),
            'editor-control': require('./Control'),
            'editor-library-modal': require('./LibraryModal'),
            'editor-create-service-modal': require('./CreateServiceModal'),
        },
        data: function() {
            return {
                libraryModal: {
                    visible: false,
                    model: []
                },
                createServiceModal: {
                    visible: false
                },
                views: [],
                errorline: undefined,
                runResult: {
                    result: '',
                    error: '',
                    log: ''
                },
                scrollInfo:{},
                cacheView:{},
                waitingResult: false,

                //resize reference
                viewportHeight: 450,
                onDrag: false
            }
        },
        computed: {
            currentView: function() {
                var selected = this.views.filter(view => view.selected === true);
                return selected ? selected[0] : null;
            }
        },
        watch: {
            scriptOptions: function(){
                this.currentView = null;
                this.scrollInfo = {};
                this.cacheView = {};
                this.views.forEach(item => {
                    item.selected = false
                })
                this.views.splice(0, this.views.length);
                this.errorline = undefined;
                this.runResult = {
                    result: '',
                    error: '',
                    log: ''
                };
            },
            scriptData: function(cur){
                var existed = this.views.some((item, index, array) =>{
                    return item.urlDefiner == cur.urlDefiner
                })
                this.views.forEach(item=> item.selected = false)
                if(!existed){
                    if(!cur.intercept){
                        this.cacheView[cur.urlDefiner] = cur
                    }
                    this.views.push(Object.assign({},this.cacheView[cur.urlDefiner],{selected: true}))
                }else{
                    this.views.forEach(item => {
                        if(item.urlDefiner == cur.urlDefiner){
                            item.selected = true
                        }
                    })
                }
            },
        },
        methods: {
            handleSelect: function(targetView) {
                // this.currentView.script = this.currentView.content
                var codeDOC = this.$refs.viewport.mainCodeMirror.doc
                this.scrollInfo ?  this.scrollInfo[this.currentView.urlDefiner] = {
                    left : codeDOC.scrollLeft,
                    top : codeDOC.scrollTop
                }: ''
                this.views.forEach(item => item.selected = false)
                targetView.selected = true;
                if(targetView.urlDefiner in this.scrollInfo){
                    this.$nextTick(() => {
                        codeDOC.cm.scrollTo(this.scrollInfo[targetView.urlDefiner].left,this.scrollInfo[targetView.urlDefiner].top)
                    })
                }
            },
            handleClose: function(targetView) {
                var close = () => {
                    var index = this.views.indexOf(targetView);
                    if (index !== -1) {
                        var closedView = this.views.splice(index, 1)[0];
                        if (closedView.selected) {
                            var nextSelectViewIndex = index - 1 < 0 ? 0 : index - 1;
                            var nextSelectView = this.views[nextSelectViewIndex];
                            nextSelectView ? (nextSelectView.selected = true) : void 0;
                        }
                    }
                };

                if (targetView.content === targetView.script) {
                    this.cacheView[targetView.urlDefiner] = targetView
                    close()
                    return;
                }

                MessageBox.open('内容已改动，是否确认关闭?', null, [{
                    text: '否',
                    handler: () => {
                        MessageBox.close();
                    }
                }, {
                    text: '是',
                    type: 'success',
                    handler: () => {
                        MessageBox.close();
                        close();
                    }
                }]);
            },
            handleCreateSubmit: function(obj) {
                this.views.forEach(view => view.selected = false);
                this.views.push({ 
                    title: obj.serviceName,
                    content: '',
                    script: '',//即originalContent,与服务器传过来的script保持一致
                    selected: true,
                    urlDefiner: obj.urlDefiner,
                    productLv1Name: obj.merchantName,
                    productLv2Name: obj.sysytemServiceName,
                    productLv3Name: obj.serviceName,
                    successCompiled: false
                });
                this.$emit('addCustomizeScript',{
                    productLv1Name: obj.merchantName,
                    productLv2Name: obj.sysytemServiceName,
                    productLv3Name: obj.serviceName,
                    urlDefiner: obj.urlDefiner,
                    postfix: ''
                })
                this.cacheView[obj.urlDefiner] = { 
                    title: obj.serviceName,
                    content: '',
                    script: '',//即originalContent,与服务器传过来的script保持一致
                    urlDefiner: obj.urlDefiner,
                }
            },
            handleCreate: function() {
                this.createServiceModal.visible = true;
            },
            handleChange: function(cm, model, changeObj) {
                if (!this.views.includes(model)) {
                    model.selected = true; // ?
                    this.views.push(model);
                }
                model.content = cm.getValue();
            },
            handleMenuCommand: function(command) {
                if (command === 'library') {
                    this.libraryModal.visible = true;
                    this.libraryModal.model = this.currentView && this.currentView.compileImport ? this.currentView.compileImport.split(','): []
                } else if (command === 'usage') {

                }
            },
            handleRun: function(){
                if(this.currentView){
                    this.errorline = undefined
                    this.runResult = {
                        result: '',
                        error: '',
                        log: ''
                    }
                    this.waitingResult = true
                    ajaxPromise({
                        url: `/customize-script/runSource`,
                        type: 'post',
                        data: {
                            preScript: this.currentView.content,
                            compileImport: this.currentView.compileImport
                        }
                    }).then(data=>{
                        // this.currentView.script = this.currentView.content
                        this.waitingResult = false
                        this.runResult = {
                            result: data.message
                        }
                        this.currentView.successCompiled = true
                    }).catch(err => {
                        this.waitingResult = false
                        this.runResult = {
                            error: err.message
                        }
                        this.errorline = Number(err.message.match(/^Line (\d+)/)[1])
                    })
                }
            },
            handleLibrary: function(lib){
                if(this.currentView != undefined){
                    this.currentView.compileImport = lib.join(',')
                }
            },
            handleSave: function(){
                var {compileImport, urlDefiner, title, productLv1Name, productLv2Name, productLv3Name, script, content} = Object.assign({
                    productLv1Name: '',
                    productLv2Name: '',
                    productLv3Name: '',
                    compileImport: ''
                },this.currentView);
                var productCode = urlDefiner.split('/')
                ajaxPromise({
                    url: `/customize-script/pre-script/submit`,
                    type: 'post',
                    data: {
                        preProcessInterfaceUrl: urlDefiner,
                        compileImport: compileImport,
                        productLv1Name: productLv1Name,
                        productLv2Name: productLv2Name,
                        productLv3Name: productLv3Name,
                        productLv1Code: productCode[0],
                        productLv2Code: productCode[1],
                        productLv3Code: productCode[2],
                        preProcessInterfaceUrl: urlDefiner,
                        script : content
                    }
                }).then(data=>{
                    this.currentView.script = this.currentView.content
                    this.currentView.successCompiled = false
                    this.$emit('addCustomizeScript',{
                        productLv1Name: productLv1Name,
                        productLv2Name: productLv2Name,
                        productLv3Name: productLv3Name,
                        urlDefiner: urlDefiner,
                        postfix: '(' + data.author + format.formatDate(data.modifyDate, 'yyyy-MM-dd HH:mm:ss') + ')'
                    })
                    MessageBox.open(data.message)
                }).catch(message=>{
                    MessageBox.open(message)
                })
            },

            //resize reference
            startDrag: function(e){
                this.startY = e.clientY;
                this.startHeight = parseInt(this.viewportHeight, 10);
                this.onDrag = true;
            },
            doDrag: function(e){
                e.preventDefault()
                if(this.onDrag && this.viewportHeight >= 450 && this.viewportHeight <= 750){
                    this.viewportHeight = Math.min(Math.max(this.startHeight + e.clientY - this.startY, 450),750)
                }else{
                    this.onDrag = false
                }
            },
            stopDrag: function(e){
                this.onDrag = false;
            }
        }
    }
</script>
