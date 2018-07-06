<style lang="sass">
    .modal {
        display: block;
        overflow-x: hidden;
        overflow-y: auto;

        .modal-content {
            border-radius: 2px;
        }
    }
</style>

<template>
    <transition name="slide"
        @after-enter="afterEnter"
        @after-leave="afterLeave">
        <div 
            :class="['effect', 'modal', 'form-modal', classes]" 
            v-show="value" 
            @click.self="closeOnClickModal && close()">
            <div class="modal-dialog">
                <div class="modal-content">
                    <slot></slot>
                </div>
            </div>
        </div>
    </transition>
</template>

<script>
    // 手动调用close(), open()时会重复执行willClose, willOpen
    import Popup from 'vue-popup';
    require('vue-popup/lib/popup.css');
    
    export default {
        mixins: [Popup],
        props: {
            classes: {
                default: null
            },
            modal: {
                default: true
            },
            closeOnPressEscape: {
                default: true
            },
            closeOnClickModal: {
                default: true
            }
        },
        watch: {
            value: function(val) {
                val ? this.$emit('open') : this.$emit('close');
            }
        },
        methods: {
            afterEnter: function() {
                this.$emit('opened')
            },
            afterLeave: function() {
                this.$emit('closed');
            }
        }
    }
</script>