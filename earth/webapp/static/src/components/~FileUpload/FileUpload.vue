<style lang="sass">
    .file-upload {
        position: relative;

        .title {
            position: relative;
            top: -2px;
            font-family: 'Glyphicons Halflings';

            &:before {
                content: "\e167";
                margin-right: 8px;
                vertical-align: middle;
            }
        }

        input[type=file] {
            position: absolute;
            top: 0;
            left: 0;
            opacity: 0;
            height: 100%;
            width: 100%;
        }

    }
</style>

<template>
    <span class="btn btn-default file-upload">
        <span class="title">{{title}}</span>
        <input type="file"
            @change="onChange"
            :name="name"
            :disabled="disabled"
            :multiple="multiple">
    </span>
</template>

<script>
    import InputFile from './InputFile';

    const ERROR_CODE = {
        TYPE: 1,
        SIZE: 2,
        NETWORK: 3
    };

    const support = (function() {
            var input = document.createElement('input');
            input.type = 'file';
            if (window.FormData && input.files)  {
                return true;
            } else {
                return false
            }
    })();

    export default {
        props: {
            name: String,
            disabled: Boolean,
            multiple: Boolean,
            action: String,
            autopost: {
                default: false,
            },
            maxsize: {
                default: Infinity,
                type: Number
            },
            filters: {
                default: () => []
            },
            title: {
                default: '选择文件'
            }
        },
        data: function() {
            return {
                files: []
            };
        },
        methods: {
            extname: function(filename) {
                var reg = /\.(\w+)$/;
                var matchs = filename.match(reg);
                return matchs ? matchs[1] : '';
            },
            isProperExt: function(filename) {
                var extname = this.extname(filename);
                var filters = this.filters.map(function(item){
                    return item.toLowerCase();
                });

                if (filters.length > 0 && ~filters.indexOf(extname.toLowerCase()) === 0) {
                    this.$emit('error', ERROR_CODE.TYPE, extname, filters);
                } else {
                    return true;
                }
            },
            isProperSize: function(filesize) {
                if (filesize > this.maxsize) {
                    this.$emit('error', ERROR_CODE.SIZE, filesize, maxsize);
                } else {
                    return true
                }
            },
            post: function() {
                if (!support || !this.files.length || !this.action) return;

                var formdata = new FormData();

                if (this.multiple) {

                } else {
                    formdata.append(this.name, this.files[0]);
                }


                var opt = {
                    type: 'post',
                    url: this.action,
                    data: formdata,
                    dataType: 'json',
                    contentType: false, // 默认为表单提交方式，改为multipart/form-data
                    processData: false // 阻止jquery编码为字符串
                };

                opt.success = (resp) => {
                    this.$emit('success', resp);
                };

                opt.error = (xhr) => {
                    this.$emit('error', ERROR_CODE.NETWORK);
                };

                opt.complete = (xhr) => {
                    this.$emit('complete');
                    this.clear();
                };

                this.$emit('begin');
                
                $.ajax(opt);
            },
            clear: function() {
                this.files = [];
            },
            onChange: function(e) {
                var files = Array.from(e.target.files);
                var flag = files.every((file) => {
                    return this.isProperExt(file.name) && this.isProperSize(file.size);
                });

                if (!files.length || !flag) return;

                this.files = files;

                this.$emit('add', files);

                if (this.autopost) {
                    this.post();
                }
            }
        }
    }
</script>
