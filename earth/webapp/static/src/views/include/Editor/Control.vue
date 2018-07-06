<style lang="sass">
    @import '~assets/stylesheets/base.scss';

    .editor-control {
        font-size: 12px;

        .tab-menu.nav-menu {
            border-bottom: 2px solid #8fb9f8;
        }

        ul{
            margin-bottom: 0;
        }
        .tab-content-item {
            border-width: 0 2px 2px;
            border-style: solid;
            border-color: #ddd;
            line-height: 36px;
            text-indent: 2em;
        }

        .tab-menu.nav-menu .tab-menu-item.active a {
            background-color: #8fb9f8;
            border-radius: 4px 4px 0 0;
        }
    }
</style>

<template>
    <div class="editor-control">
        <TabMenu v-model="selected">
            <TabMenuItem id="result">运行结果</TabMenuItem>
            <!-- <TabMenuItem id="log">运行日志</TabMenuItem> -->
        </TabMenu>
        <TabContent v-model="selected" v-loading="waitingResult">
            <TabContentItem id="result">
                <div v-if="runResult.error" style="color: #a51a1a">
                    {{ runResult.error }}
                </div>
                <div v-else>
                    {{runResult.result}}
                </div>
            </TabContentItem>
            <!-- <TabContentItem id="log">
                {{runResult.log}}
            </TabContentItem> -->
        </TabContent>
    </div>
</template>

<script>
    import { TabMenu, TabMenuItem, TabContent, TabContentItem } from 'components/Tab';

    export default {
        components: {
            TabMenu,TabMenuItem, TabContent, TabContentItem,
        },
        props: {
            runResult: {
                type: Object,
                default:function(){
                    return{
                        result: '',
                        error: '',
                        // log: ''
                    }
                }
            },
            waitingResult: {
                type: Boolean,
                default: false
            }
        },
        data: function() {
            return {
                selected: 'result'
            }
        },
        watch: {},
        methods: {}
    }
</script>
