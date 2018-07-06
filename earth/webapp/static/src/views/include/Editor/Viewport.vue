<style lang="sass">
    @import '~assets/stylesheets/base.scss';
    @import "~codemirror/lib/codemirror.css";
    @import "~codemirror/addon/hint/show-hint.css";
    @import "./theme/suidifu.css";

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
    import './hint/java-hint';
    import { library as libraryStore } from './hint/store';

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
                    this.mainCodeMirror.setOption('readOnly', false);
                } else {
                    this.mainCodeMirror.setValue('');
                    this.mainCodeMirror.setOption('readOnly', 'nocursor');
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
            parseLibrary: function() {
                var editor = this.mainCodeMirror;
                var libraries = [];
                var reg = /^\s*import\s*([\w\.]+)\s*;$/;
                var row = editor.firstLine();

                do {
                    var content = editor.getLine(row);
                    var matched;
                    if (/^\s*$/.test(content)) {
                        row += 1;
                    } else if (matched = reg.exec(content)) {
                        libraries.push(matched[1]);
                        row += 1;
                    } else {
                        break;
                    }
                } while (true);

                CodeMirror.hint.clearLibrary(editor);

                return libraryStore.fetch(libraries).then((succPackageFullNames) => {
                    succPackageFullNames.forEach(packageFullName => {
                        CodeMirror.hint.addLibrary(editor, packageFullName);
                    });
                });
            },
            inspectLibrary: function() {
                if (this.inspectTimer) {
                    clearTimeout(this.inspectTimer);
                }

                var start = () => {
                    this.parseLibrary()
                        .catch(error => {
                            console.error(error);
                        })
                        .then(() => {
                            this.inspectTimer = setTimeout(start, 1000);
                        });
                };

                start();
            },
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
                readOnly: this.model ? false : 'nocursor',
                mode: 'text/x-java',
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
                    hint: CodeMirror.hint.java,
                }
            });

            this.mainCodeMirror.on('change', (cm, changeObj) => {
                if (this.model) {
                    this.model.successCompiled = false
                    this.$emit('change', cm, this.model, changeObj);
                }
                if(this.errorline != undefined)
                    this.removeLineClass(this.errorline - 1)
            });
            this.inspectLibrary();
        }
    }
</script>
