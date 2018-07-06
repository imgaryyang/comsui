<style lang="sass">
    .wrap{
        width: 100%;
        height: 100%;
        z-index: 8;
        .help_content{
            position: fixed;
            padding-top: 60px;
            top: 0;
            left: 0;
            box-sizing: border-box;
            width: 100%;
            height: 100%;
            background-color: #ffffff;
            display: flex;
            flex-flow: row;
            overflow-x: auto;
            overflow-y: hidden;
        }
    }
</style>
<template>
    <div class="item-faq wrap">
        <SHeader></SHeader>
        <div class="help_content">
            <keep-alive>
                <router-view></router-view>
            </keep-alive>
            <ProblemList></ProblemList>
        </div>
    </div>
</template>
<script>
    import ProblemList from './include/Problems';

    export default{
        components: {
            ProblemList,
            SHeader: require('views/include/Header'),
        },
        data: function(){
            return {
                keywords: ''
            }
        },
        computed: {
            active: function(){
                return this.$parent.selected == this.id;
            }
        },
    }
</script>