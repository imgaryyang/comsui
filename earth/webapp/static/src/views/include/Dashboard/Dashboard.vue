<style lang="sass">
    
</style>

<template>
    <div v-show="visible">
        <div 
            class="v-modal" 
            style="z-index: 7;"
            @click="close">
        </div>
        <Todo 
            :visible="visible"
            :data="options" 
            id="item-todo">
        </Todo>
        <Notice 
            :visible="visible"
            :data="options" 
            id="item-notice">
        </Notice>
        <NoticeDetail 
            :visible="visible"
            :data="options" 
            id="item-notice-detail">
        </NoticeDetail>
    </div>
</template>

<script>
    export default {
        components: {
            Todo: require('./Todo'),
            Notice: require('./Notice'),
            NoticeDetail: require('./NoticeDetail'),
        },
        data: function() {
            return {
                visible: false,
                selected: '',
                options: {}
            }
        },
        beforeMount: function() {
            $(window).on('keyup.dashboard', (e) => {
                if (e.keyCode == 27) {
                    this.close();
                }
            });
        },
        beforeDestory: function() {
            $(window).off('keyup.dashboard');
        },
        methods: {
            open: function(name, options) {
                options && (this.options = options);
                name && (this.selected = name);
                this.visible = true;
            },
            close: function() {
                this.visible = false;
            }
        }
    }
</script>