<style lang="sass" scoped>
    .qa_content{
        position: relative;
        border: 1px solid #e6e6e6;
        width: 100%;
        margin-top: 28px;
        overflow: hidden;
        .question{
            height: 45px;
            line-height: 45px;
            background-color: #f5f5f5;
            padding: 0 10px;
            color: #666666;
        }
        .answer{
            width: 100%;
            color: #666666;
            padding: 10px 20px;
            background-color: #ffffff;
            .answer_content{
                position: relative;
                line-height: 2;
                width: 100%;
                span{
                    position: absolute;
                }
                p{
                    text-indent: 2em;
                    margin: 0;
                }
            }
            .img_content{
                margin-top: 10px;
                padding-left: 28px;
                width: 100%;
                overflow: hidden;
                background: linear-gradient(to bottom, #fff 0%, transparent 100%);
                img{
                    vertical-align: top;
                    max-width: calc(100% - 50px);
                }
            }
            .mask{
                position: absolute;
                left: 0;
                bottom: 0;
                z-index: 7;
                width: 100%;
                height: 50%;
                background: linear-gradient(to bottom, transparent 0%, #fff 100%);
            }
        }
        .shouqi{
            color: #8fb9f8;
            width: 100%;
            height: 40px;
            line-height: 40px;
            background: #fff;
            text-align: right;
            padding-right: 5px;
            border-top: 1px solid #e6e6e6;
            span{
                cursor: pointer;
            }
            i{
                margin: 0 10px;
                transform: scaleX(0.7);
            }
        }
    }
    .max_height{
        max-height: 260px;
    }
</style>
<template>
    <div>
        <div class="qa_content" :class="{ max_height: !visible && ifVisible }">
            <div class="question">{{ index + 1 }}. {{ data.question }}</div>
            <div class="answer" @click="visible = true" ref="answer">
                <div class="answer_content">
                    <span v-if="data.mainType != 'base' && data.minorType != 'p1'">答：</span>
                    <p v-if="data.answer.length != 0" v-for="item in data.answer">{{ item }}</p>
                </div>
                <div class="img_content" v-if="data.imgSrc.length != 0" v-for="(item, index2) in data.imgSrc">
                    <span>图{{ index2 | wordMap }}： </span><img :src="`${resource}/images/faq-image/answer_img/${data.imgSrc[index2]}.png`">
                </div>
                <div class="mask" v-if="!visible && ifVisible"></div>
            </div>
            <div class="shouqi" v-if="visible && ifVisible">
                <span @click="visible = false">收起<i class="el-icon-arrow-up"></i></span>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        props: {
            data: {
                type: Object,
                default: function() {
                    return {};
                }
            },
            index: {
                type: Number,
                default: 0
            },

        },
        data: function(){
            return {
                visible: false,
            }
        },
        computed: {
            ifVisible: function(){
                return this.data.imgSrc.length || this.data.answer.length > 7;
            }
        },
        filters: {
            wordMap: function(val){
                var arr = ["一", "二", "三", "四"];
                return arr[val];
            }
        }
    }
</script>