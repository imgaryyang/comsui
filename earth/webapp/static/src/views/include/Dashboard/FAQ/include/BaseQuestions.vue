<style lang="sass">
    .tab-menu-base.tab-menu.nav-menu{
        border-color: #8fb9f8;
    }
    .tab-menu-base.tab-menu.nav-menu .tab-menu-item.active a{
        background-color: #8fb9f8;
    }
    .base_center{
        position: relative;
        flex: 1;
        height: 100%;
        min-width: 820px;
        float: left;
        background: url('~assets/images/faq-image/faq-bg.png')no-repeat;
        background-size: 100% 100%;
        overflow-y: auto;
        .back{
            position: absolute;
            top: 30px;
            left: 20px;
            font-size: 18px;
            line-height: 40px;
            color: #666666;
            img{
                vertical-align: middle;
                cursor: pointer;
            }
        }
        .base_wrap{
            margin: 0 auto;
            padding-top: 100px;
            width: 85%;
        }
        .mbt{
            margin-bottom: 60px;
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
    <div class="base_center">
        <header class="back"><img @click="$router.go(-1)" src="~assets/images/faq-image/back.png">基础问题</header>
        <div class="base_wrap">
            <TabMenu class="tab-menu-base" v-model="selected">
                <TabMenuItem id="FIRST">单据关系</TabMenuItem>
                <TabMenuItem id="SECOND">名词解析</TabMenuItem>
                <TabMenuItem id="THIRD">状态位解析</TabMenuItem>
            </TabMenu>
            <TabContent v-model="selected" class="mbt">
                <TabContentItem id="FIRST">
                    <QAList v-for="(item, index) in qaLists1" :data="item" :index="index"></QAList>
                </TabContentItem>
                <TabContentItem id="SECOND">
                    <QAList v-for="(item, index) in qaLists2" :data="item" :index="index"></QAList>
                </TabContentItem>
                <TabContentItem id="THIRD">
                    <QAList v-for="(item, index) in qaLists3" :data="item" :index="index"></QAList>
                </TabContentItem>
            </TabContent>
        </div>
        <div class="totop" v-show="topVisible" @click="toTop">
            <img src="~assets/images/faq-image/top.png">
        </div>
    </div>
</template>
<script>
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
    import QAList from './QAList';
    import tableList from '../data.js'

    export default {
        components: {
            TabMenu,TabMenuItem, TabContent, TabContentItem, QAList,
        },
        data: function(){
            return {
                selected: '',
                topVisible: false
            }
        },
        computed: {
            qaLists1: function(){
                return tableList.filter(function(item){
                    return item.minorType == 'b1';
                })
            },
            qaLists2: function(){
                return tableList.filter(function(item){
                    return item.minorType == 'b2';
                })
            },
            qaLists3: function(){
                return tableList.filter(function(item){
                    return item.minorType == 'b3';
                })
            }
        },
        activated: function() {
            this.selected = 'FIRST';
            // console.log(this.$router);
            // console.log(this.$route);
        },
        mounted: function(){
            var _this = this;
            document.getElementsByClassName('base_center')[0].onscroll = function(){
                if(this.scrollTop > 0){
                    _this.topVisible = true;
                }else{
                    _this.topVisible = false;
                }
            }
        },
        destroyed: function(){
            console.log(1)
        },
        methods: {
            toTop: function(){
                document.getElementsByClassName('base_center')[0].scrollTop = 0;
            }
        }
    }
</script>