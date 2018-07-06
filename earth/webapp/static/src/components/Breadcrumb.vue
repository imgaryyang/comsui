<style lang="sass">
    .position-map {

        .back {
            margin-right: 10px;
            vertical-align: bottom;
        }

        .el-button {
            border-radius: 2px;
            min-width: 60px;

            & + .el-button {
                margin-left: 0px;
            }
        }
    }   
</style>

<template>
    <div class="position-map">
        <div class="pull-left">
            <a 
                v-if="enableBack" 
                href="#" 
                @click.prevent="goback" 
                class="back btn btn-default">
                &lt;&lt; 返回
            </a>
            <span>当前位置:</span>
            <span 
                v-for="(route, index) in routes" 
                :class="['item', index === routes.length -1 ? 'current' : '']">
                <router-link v-if="route.path" :to="route.path" v-text="route.title"></router-link>
                <span v-else v-text="route.title"></span>    
                <span v-if="index < routes.length - 1" v-text="' > '"></span>
            </span>
        </div>
        <div class="pull-right">
            <slot></slot>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            routes: [String, Object, Array],
            previous: [String, Object],
            enableBack: {
                type: Boolean,
                default: true
            }
        },
        methods: {
            goback: function() {
                if (this.previous) {
                    typeof this.previous === 'string' ? location.assign(this.previous) : this.$router.push(this.previous);
                } else {
                    history.back();
                }
            }
        }
    }
</script>