<style lang="sass">
    .help_center{
        flex: 1;
        height: 100%;
        float: left;
        background: url('~assets/images/faq-image/faq-bg.png')no-repeat;
        background-size: 100% 100%;
        overflow-x: auto;
        .help_wrap{
            margin: 0 auto;
            padding: 150px 0;
            width: 820px;
            height: 100%;
            .headline{
                position: relative;
                height: 60px;
                text-align: center;
                margin-bottom: 30px;
                img{
                    vertical-align: middle;
                }
                span{
                    font-size: 30px;
                    line-height: 60px;
                    color: #666666;
                    vertical-align: middle;
                }
            }
            .search_content{
                position: relative;
                width: 510px;
                margin: 0 auto;
                box-sizing: border-box;
                padding: 8px 10px;
                background-color: #f5f5f5;
                .search{
                    .el-input__inner{
                        border-radius: 0;
                        border-color: #e6e6e6;
                        color: #999999;
                        font-size: 12px;
                    }
                    .el-input__icon{
                        cursor: pointer;
                        color: #999999;
                        font-size: 14px;
                    }
                }
            }
            .question{
                width: 820px;
                margin-top: 30px;
                border-top: 1px solid #e6e6e6;
                .entry{
                    cursor: pointer;
                    text-align: center;
                    line-height: 100px;
                    text-decoration: none;
                    float: left;
                    width: 360px;
                    height: 100px;
                    margin: 50px 25px 0 25px;
                    box-sizing: border-box;
                    border-radius: 50px;
                    background: url('~assets/images/faq-image/border1.png')no-repeat;
                    transition: all 0.32s ease;
                    &:hover{
                        background: url('~assets/images/faq-image/border2.png')no-repeat;
                        .base{
                            background-image: url('~assets/images/faq-image/base2.png');
                        }
                        .process{
                            background-image: url('~assets/images/faq-image/process2.png');
                        }
                        .operation{
                            background-image: url('~assets/images/faq-image/operation2.png');
                        }
                        .err{
                            background-image: url('~assets/images/faq-image/err2.png');
                        }
                        i{
                            background: url('~assets/images/faq-image/go2.png')no-repeat;
                            background-position: 0 -5px;
                        }
                    }
                    .lefticon{
                        vertical-align: middle;
                        display: inline-block;
                        width: 60px;
                        height: 60px;
                        background-position: 0 -5px;
                        background-repeat: no-repeat;
                    }
                    .base{
                        background-image: url('~assets/images/faq-image/base1.png');
                    }
                    .process{
                        background-image: url('~assets/images/faq-image/process1.png');
                    }
                    .operation{
                        background-image: url('~assets/images/faq-image/operation1.png');
                    }
                    .err{
                        background-image: url('~assets/images/faq-image/err1.png');
                    }
                    span{
                        color: #666666;
                        font-size: 20px;
                        margin: 0 65px;
                    }
                    i{
                        display: inline-block;
                        vertical-align: middle;
                        width: 30px;
                        height: 30px;
                        background: url('~assets/images/faq-image/go1.png')no-repeat;
                        background-position: 0 -5px;
                    }
                }
            }
        }
    }
</style>
<template>
    <div class="help_center">
        <div class="help_wrap">
            <div class="headline">
                <img src="~assets/images/faq-image/blub.png"><span>帮助中心</span>
            </div>
            <div class="search_content">
                <el-input size="small" v-model="keywords" @keyup.enter.native="handleSearch" class="search" placeholder="搜索问题" icon="search" @click="handleSearch"></el-input>
            </div>
            <div class="question">
                <a class="entry" :href="`${ctx}#/faq/base`">
                    <div class="lefticon base"></div>
                    <span>基础问题</span>
                    <i></i>
                </a>
                <a class="entry" :href="`${ctx}#/faq/process`">
                    <div class="lefticon process"></div>
                    <span>流程问题</span>
                    <i></i>
                </a>
                <a class="entry" :href="`${ctx}#/faq/operation`">
                    <div class="lefticon operation"></div>
                    <span>操作问题</span>
                    <i></i>
                </a>
                <a class="entry" :href="`${ctx}#/faq/error`">
                    <div class="lefticon err"></div>
                    <span>异常问题</span>
                    <i></i>
                </a>
            </div>
        </div>
    </div>
</template>
<script>
    export default{
        data: function(){
            return {
                keywords: ''
            }
        },
        activated: function(){
            this.keywords = '';
        },
        methods: {
            handleSearch: function(){
                if(this.keywords){
                    this.$router.push({ name: 'searchRouter', params: { id: this.keywords }});
                }
            }
        }
    }
</script>