<style lang="sass">
    .tag_content {
        border: 1px solid #e0e0e0;
        height: 300px;
        overflow: auto;
        ul {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            list-style: none;
        }

        .tag_list {
            .tag_group {
                line-height: 52px;
                .undertint {
                    line-height: 12px;
                    display: inline-block;
                    text-indent: 0;
                    color: #999999;
                    max-width: 300px;
                    overflow: hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                }
                .checkbox {
                    position: absolute;
                    width: 100%;
                    height: 52px;
                    left: 0;

                    .el-checkbox__label {
                        display: none;
                    }
                    .el-checkbox__input {
                        position: absolute;
                        right: 20px;
                        margin-top: -18px;
                        top: 50%;
                    }
                }

                .tag-group_title {
                    background: #f1f1f1;
                    text-indent: 10px;
                    font-weight: bold;
                }
                .tag-group_items {
                    text-indent: 20px;
                    &:last-child {
                        .tag-group_item {
                            &:last-child {
                                border: none;
                            }
                        }
                    }
                    .tag-group_item {
                        text-indent: 0;
                        position: relative;
                        margin-left: 20px;
                        border-bottom: 1px solid #e0e0e0;
                    }
                }
            }

        }

    }
</style>
<template>
    <div class="tag_content">
        <ul class="tag_list">
            <li v-if="!options.length">暂无数据</li>
            <li v-for="item in groupOptions" class="tag_group">
                <div class="tag-group_title" v-if="item.title != ''">{{ item.title }}</div>
                <ul class="tag-group_items">
                    <li class="tag-group_item" v-for="subitem in item.options" :title="subitem.description">
                        {{ subitem.tagName }}
                        <span v-if="subitem.description !== ''">(<span class="undertint">{{ subitem.description }}</span>)</span>
                        <el-checkbox :value="bothTagList.includes(subitem.tagName)" @input="handleInput(subitem.tagName, arguments[0])" class="checkbox pull-right"></el-checkbox>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</template>
<script>
    import tinyPinyin from 'tiny-pinyin'

    export default {
        props: {
            options: Array,
            bothTagList: Array
        },
        computed: {
            groupOptions: function() {
                const arr = this.options.slice(0);
                if(tinyPinyin.isSupported()){
                    // 后台排序过了
                    // const collator = new Intl.Collator(['zh-Hans-CN', 'zh-CN']);
                    // arr.sort(collator.compare)
                    var group, filterArr;
                    var result = [];
                    result.push(Object.assign({},{
                        title: '',
                        options: arr.filter(item => {
                            var s = tinyPinyin.convertToPinyin(item.tagName, '', true).substr(0, 1);
                            return  s.charCodeAt(0) < 97 || s.charCodeAt(0) > 122
                        })
                    }));
                    for(let i=0;i<26;i++){
                        group = String.fromCharCode(97 + i);
                        filterArr = arr.filter(item => tinyPinyin.convertToPinyin(item.tagName, '', true).substr(0, 1) === group);
                        if(filterArr.length){
                            result.push(
                                Object.assign({},{
                                    title: group.toUpperCase(),
                                    options: filterArr
                                })
                            );
                        }
                    }
                    return result;
                } else {
                    return arr;
                }
            }
        },
        methods: {
            handleInput: function(tagName, check) {
                const eventType = check ? 'input':'delect';
                this.$emit(eventType, tagName)
            }
        }
    }
</script>