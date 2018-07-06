<style lang="sass">
	
</style>

<template>
	<div 
        v-if="visible"
        class="dashboard dashboard-notice-detail item-notice-detail">
        <div class="inner">
            <div class="hd">
                <p class="name">
                    <a href="#" @click.prevent="$parent.open('item-notice')">&lt;返回</a>
                    <strong style="margin-left: 10px">公告栏</strong>
                </p>
            </div>
            <div class="bd">
                <p class="notice-title"><strong>{{ data.title | unescape }}</strong></p>
                <p style="white-space:pre-wrap;">{{ data.content | unescape }}</p>
            </div>
        </div>  
    </div>
</template>

<script>
	export default {
        props: {
            id: String,
            options: Object,
            data: Object,
        },
        computed: {
            visible: function() {
                return this.$parent.selected == this.id;
            }
        },
        filters: {
            unescape: function(value) {
                return unescape(value);
            }
        },
    }
</script>