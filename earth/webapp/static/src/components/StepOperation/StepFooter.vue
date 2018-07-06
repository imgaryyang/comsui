<style lang="sass">
    .step-footer {
        padding: 10px 0;
        text-align: center;

        .btn {
            min-width: 80px;
        }

        .btn+.btn {
            margin-left: 10px;
        }
    }
</style>

<template>
    <div class="step-footer">
        <template v-if="priority == 'next'">
            <button 
                :disabled="disabled"
                type="button" 
                @click="next"
                class="btn btn-primary next">
                {{ nextBtnText }}
            </button>
            <button 
                v-if="visible"
                type="button" 
                @click="prev"
                class="btn btn-default prev">
                {{ prevBtnText }}
            </button>
        </template>
        <template v-else>
            <button 
                v-if="visible"
                type="button" 
                @click="prev"
                class="btn btn-default prev">
                {{ prevBtnText }}
            </button>
            <button 
                :disabled="disabled"
                type="button" 
                @click="next"
                class="btn btn-primary next">
                {{ nextBtnText }}
            </button>
        </template>
    </div>
</template>

<script>
    export default {
        props: {
            value: Number,
            stepNumber: Number,
            visible: {
                type: Boolean,
                default: true
            },
            disabled: {
                type: Boolean,
                default: false
            },
            cancelText: {
                default: '取消'
            },
            completeText: {
                default: '确认'
            },
            priority: {
                default: 'prev'
            }
        },
        computed: {
            prevBtnText: function() {
                if (this.value === 1) {
                    return this.cancelText;
                } else {
                    return '上一步';
                }
            },
            nextBtnText: function() {
                if (this.value === this.stepNumber) {
                    return this.completeText;
                } else {
                    return '下一步';
                }
            }
        },
        methods: {
            prev: function() {
                if (this.value <= 1) {
                    this.$emit('cancel');
                } else {
                    this.$emit('input', this.value - 1, this.value);
                }
            },
            next: function() {
                if (this.value >= this.stepNumber) {
                    this.$emit('complete');
                } else {
                    this.$emit('input', this.value + 1, this.value);
                }
            }
        }
    }
</script>