<style lang="sass" scoped>
    .faq{
        width: 370px;
        min-width: 370px;
        box-sizing: border-box;
        border-left: 1px solid #dddfe8;
        height: 100%;
        padding: 30px 40px;
        float: right;
        background-color: #f1f2f5;
        .title{
            color: #333333;
            font-size: 18px;
            line-height: 30px;
        }
        .problem{
            padding: 0;
            li{
                cursor: pointer;
                list-style: none;
                position: relative;
                font-size: 14px;
                color: #999999;
                line-height: 24px;
                margin: 18px 0;
                padding-left: 16px;
                &:hover{
                    color: #8fb9f8;
                }
                &:before{
                    content: '';
                    position: absolute;
                    top: 10px;
                    left: 0;
                    display: block;
                    width: 3px;
                    height: 3px;
                    border-radius: 1.5px;
                    background: #666666;
                }
            }
        }
    }
</style>
<template>
    <div class="faq">
        <div class="title">常见问题</div>
        <ul class="problem">
            <li v-for="(item, index) in questions" @click="search(item)">{{ item.question }}</li>
        </ul>
    </div>
</template>
<script>
    import tableList from '../data.js';

    export default{
        data: function() {
            return {
                questions: tableList.filter(function(item){
                    return item.rate == 1;
                }),
            }
        },
        methods:{
            search: function(item){
                this.$router.push({ name: 'searchRouter', params: { id: item.question }});
            }
        }
    }
</script>