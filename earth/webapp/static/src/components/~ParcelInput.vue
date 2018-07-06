<style lang="sass">
    .parcel-input {
        position: relative;
        display: inline-block;

        &.suffix .form-control {
            text-align: left;
            padding-right: 30px;
        }

        &.prefix .form-control {
            text-align: right;
            padding-left: 30px;
        }

        &.both .form-control {
            padding-left: 30px;
            padding-right: 30px;
            text-align: center;
        }

        .form-control {
            padding-left: 30px;
            padding-right: 30px;
        }

        .prefix,
        .suffix {
            position: absolute;
            top: 50%;
            margin-top: -10px;
            color: #999;
        }

        .prefix {
            left: 10px;
        }

        .suffix {
            right: 10px;
        }
    }
</style>

<template>
    <span class="parcel-input" :class="classes">
        <span class="prefix"><slot name="prefix"></slot></span>
        <ElInput v-model="internalValue" 
            :readonly="readonly"
            :disabled="disabled"
            :name="name" 
            :type="type"
            :placeholder="placeholder"></ElInput>
        <span class="suffix"><slot name="suffix"></slot></span>
    </span>
</template>

<script>
    export default {
        computed: {
            classes: function() {
                if (this.$slots.prefix && this.$slots.suffix) {
                    return 'both'
                } else if (this.$slots.prefix) {
                    return 'prefix';
                } else if (this.$slots.suffix) {
                    return 'suffix';
                }
            }
        },
        props: {
            name: String,
            placeholder: String,
            value: String,
            type: {
                default: 'text'
            },
            readonly: {
                default: false
            },
            disabled: {
                default: false
            }
        },
        data: function() {
            return {
                internalValue: this.value
            };
        },
        watch: {
            internalValue: function(cur) {
                this.$emit('input', cur);
            },
            value: function(cur, old) {
                this.internalValue = cur;
            }
        }
    }
</script>