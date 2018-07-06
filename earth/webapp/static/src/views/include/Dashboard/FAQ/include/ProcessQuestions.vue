<template>
    <div class="base_center">
        <header class="back"><img @click="$router.go(-1)" src="~assets/images/faq-image/back.png">流程问题</header>
        <div class="base_wrap">
            <TabMenu class="tab-menu-base" v-model="selected">
                <TabMenuItem id="FIRST">整体流程图</TabMenuItem>
                <TabMenuItem id="SECOND">放款</TabMenuItem>
                <TabMenuItem id="THIRD">导入资产包</TabMenuItem>
                <TabMenuItem id="FOURTH">还款</TabMenuItem>
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
                <TabContentItem id="FOURTH">
                    <QAList v-for="(item, index) in qaLists4" :data="item" :index="index"></QAList>
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
                    return item.minorType == 'p1';
                })
            },
            qaLists2: function(){
                return tableList.filter(function(item){
                    return item.minorType == 'p2';
                })
            },
            qaLists3: function(){
                return tableList.filter(function(item){
                    return item.minorType == 'p3';
                })
            },
            qaLists4: function(){
                return tableList.filter(function(item){
                    return item.minorType == 'p4';
                })
            }
        },
        activated: function() {
            this.selected = 'FIRST';
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
        methods: {
            toTop: function(){
                document.getElementsByClassName('base_center')[0].scrollTop = 0;
            }
        }
    }
</script>