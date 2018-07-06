<style lang="sass">
    @import '~assets/stylesheets/base.scss';
    @import "~codemirror/lib/codemirror.css";
    @import "~codemirror/addon/hint/show-hint.css";
    @import "~views/include/Editor/theme/suidifu.css";

    .editor-viewport {
        .main {
            height: 100%;
        }

        .CodeMirror {
            height: 100%;
        }
    }

    .CodeMirror-linenumber {
        padding: 0 5px 0 8px;
    }

    .CodeMirror-definition-tooltip {
        border: 1px solid silver;
        border-radius: 3px;
        color: #444;
        padding: 2px 5px;
        font-size: 12px;
        font-family: Tahoma!important;
        background-color: white;
        white-space: pre-wrap;

        max-width: 40em;
        position: absolute;
        z-index: 10;
        box-shadow: 2px 3px 5px rgba(0,0,0,.2);

        @include transition(opacity 1s);
    }
    .errorLight{
        background-color: #a51a1a;
        color: #f00;
    }
</style>

<template>
    <div class="editor-viewport" v-loading="fetching">
        <div class="main" ref="main">
            
        </div>
    </div>
</template>

<script>
    import CodeMirror from 'codemirror';
    import 'codemirror/mode/clike/clike.js';
    import 'codemirror/addon/edit/matchbrackets.js';
    import 'codemirror/addon/edit/closebrackets.js';
    import 'codemirror/addon/hint/show-hint.js';
    import 'codemirror/keymap/sublime.js';
    import 'codemirror/addon/hint/javascript-hint.js';
    import 'codemirror/addon/lint/json-lint.js'

    export default {
        props: {
            model: {
                type: Object,
            },
            errorline: {
                type: Number,
            },
            fetching: {
                type: Boolean,
                default: false
            }
        },
        watch: {
            model: function(current, previous) {
                if (current) {
                    this.mainCodeMirror.setValue(current.content || '');
                } else {
                    this.mainCodeMirror.setValue('');
                }
            },
            errorline: function(cur, pre) {
                if(cur == undefined)
                    return
                //过滤掉run时设置的undefined
                if(pre != undefined){
                    this.removeLineClass(pre)
                }
                this.addLineClass(cur - 1)
                this.mainCodeMirror.doc.cm.scrollIntoView(cur - 1)
            }
        },
        methods: {
            removeLineClass: function(pre){
                this.mainCodeMirror.doc.removeLineClass(pre, 'gutter', 'errorLight')
                this.mainCodeMirror.doc.removeLineClass(pre, 'wrap', 'errorLight')
            },
            addLineClass: function(cur){
                this.mainCodeMirror.doc.addLineClass(cur, 'wrap', 'errorLight')
                this.mainCodeMirror.doc.addLineClass(cur, 'gutter', 'errorLight')
            },
        },
        beforeDestroy: function() {
            if (this.inspectTimer) {
                clearTimeout(this.inspectTimer);
            }
        },
        mounted: function() {
            this.mainCodeMirror = CodeMirror(this.$refs.main, {
                value: (this.model && this.model.content) ? this.model.content : '',
                mode: {name: "javascript", json: true},
                lineNumbers: true,
                matchBrackets: true,
                autoCloseBrackets: true,
                indentUnit: 4,
                tabSize: 4,
                keyMap: 'sublime',
                extraKeys: {
                    'Ctrl-Space': 'autocomplete'
                },
                theme: 'suidifu',
                hintOptions: {
                    hint: CodeMirror.hint.javascript,
                    lint: CodeMirror.lint.json
                },
            });

            this.mainCodeMirror.on('change', (cm, changeObj) => {
                if (this.model) {
                    this.$emit('change', cm, this.model, changeObj);
                }
                if(this.errorline != undefined)
                    this.removeLineClass(this.errorline - 1)
            });
        }
    }
</script>
