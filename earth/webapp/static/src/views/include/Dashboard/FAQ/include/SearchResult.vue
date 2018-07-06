<style lang="sass">
    .result_center{
        position: relative;
        flex: 1;
        height: 100%;
        min-width: 820px;
        float: left;
        background: url('~assets/images/faq-image/faq-bg.png')no-repeat;
        background-size: 100% 100%;
        overflow-y: auto;
        .result_wrap{
            margin: 0 auto;
            padding-top: 100px;
            width: 85%;
            .back{
                position: absolute;
                top: 30px;
                left: 40px;
                font-size: 18px;
                line-height: 40px;
                color: #666666;
                img{
                    vertical-align: middle;
                    cursor: pointer;
                }
            }
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
                width: 44%;
                min-width: 400px;
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
            .result_list{
                margin-top: 30px;
                margin-bottom: 60px;
                border-top: 1px solid #e6e6e6;
            }
            .text_center{
                text-align: center;
                line-height: 40px;
                color: #999999;
            }
        }
        .totop{
            position: fixed;
            right: 400px;
            bottom: 30px;
            border-radius: 5px;
            overflow: hidden;
        }
    }
</style>
<template>
    <div class="result_center">
        <div class="result_wrap">
            <header class="back"><img @click="$router.push('/faq')" src="~assets/images/faq-image/back.png">返回</header>
            <div class="headline">
                <img src="~assets/images/faq-image/blub.png"><span>搜索结果</span>
            </div>
            <div class="search_content">
                <el-input size="small" v-model="value" @keyup.enter.native="handleSearch" class="search" placeholder="搜索问题" icon="search" @click="handleSearch"></el-input>
            </div>
            <div v-if="searchList.length != 0" class="result_list" v-loading="loading">
                <QAList v-for="(item, index) in searchList" :data="item" :index="index"></QAList>
            </div>
            <div v-else class="result_list text_center" v-loading="loading">未搜索到相关问题</div>
        </div>
        <div class="totop" v-show="topVisible" @click="toTop">
            <img src="~assets/images/faq-image/top.png">
        </div>
    </div>
</template>
<script>
    import QAList from './QAList';
    import tableList from '../data.js';

    export default{
        components: {
            QAList,
        },
        activated: function(){
            this.vLoading();
            var _this = this;
            this.value = this.$route.params.id;
            this.searchList = tableList.filter(function(item){
                return item.question && _this.value && (item.question.indexOf(_this.value) != -1);
            });
        },
        mounted: function(){
            var _this = this;
            document.getElementsByClassName('result_center')[0].onscroll = function(){
                if(this.scrollTop > 0){
                    _this.topVisible = true;
                }else{
                    _this.topVisible = false;
                }
            }
        },
        data: function(){
            return {
                value: '',
                searchList: [],
                loading: false,
                topVisible: false
            }
        },
        watch: {
            '$route.params.id': function(cur){
                this.value = cur;
                this.vLoading();
                this.reSearch();
            }
        },
        methods: {
            reSearch: function(){
                var _this = this;
                this.searchList = tableList.filter(function(item){
                    return item.question && _this.value && (item.question.indexOf(_this.value) != -1);
                });
            },
            handleSearch: function(){
                this.vLoading();
                this.$router.push({ name: 'searchRouter', params: { id: this.value }});
            },
            vLoading: function(){
                var _this = this;
                this.loading = true;
                setTimeout(function(){
                    _this.loading = false;
                }, 300);
            },
            toTop: function(){
                document.getElementsByClassName('result_center')[0].scrollTop = 0;
            }
        }
    }
</script>