<template>
    <div class="base_center">
        <header class="back"><img @click="$router.go(-1)" src="~assets/images/faq-image/back.png">异常问题</header>
        <div class="base_wrap mbt">
            <QAList v-for="(item, index) in qaLists1" :data="item" :index="index"></QAList>
        </div>
        <div class="totop" v-show="topVisible" @click="toTop">
            <img src="~assets/images/faq-image/top.png">
        </div>
    </div>
</template>
<script>
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';
    import QAList from './QAList';
    import tableList from '../data.js';

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
                    return item.mainType == 'error';
                })
            }
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