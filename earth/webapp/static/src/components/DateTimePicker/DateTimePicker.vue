<style lang="sass">
    .bootstrap-datetimepicker-widget {
        table {
            font-size: 14px;
        }

        table,
        .picker-switch a {
            line-height: 20px;
            height: 20px;
        }
    }

    .datetimepicker-trigger {
        display: block;
        
        .datetimepicker-trigger-input {
            width: 100%;
            padding: 3px 25px 3px 10px;
        }

        .calendar {
            border: none;
            position: absolute;
            right: 0;
            top: 0;
            bottom: 0;
            background: transparent;
            width: 25px;
            vertical-align: middle;
            padding: 0 5px;

            &:hover .glyphicon:before {
                content: "\e014";
            }

            .glyphicon:before {
                content: "\e109";
            }
        }

        .add-on {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 2;
            cursor: pointer;
            opacity: 0;
            right: 25px;
            height: 100%;
        }

        &.small {
            .el-input__inner {
                height: 28px;
            }
        }

        &.pick-time {
            .el-input__inner {
                min-width: 150px;
            }
        }
    }

    // .datetimepicker-group {
    //     & > * {
    //         display: inline-block
    //     }
    // }
   
</style>

<template>
    <span 
        class="datetimepicker-trigger input-group" 
        :class="[size, pickTime ? 'pick-time' : '']"
    >
        <input 
            class="datetimepicker-trigger-input el-input__inner" 
            v-model="internalValue" 
            :placeholder="placeholder"
            :name="name"
            type="text" 
            readonly
        >
        <span class="calendar"><i class="glyphicon"></i></span>
        <span class="add-on"><i></i></span>
    </span>
</template>

<script>
    require('./bootstrap-datetimepicker.css');
    require('./bootstrap-datetimepicker.js');

    // 精确到秒时有错时间开始时间和结束时间有错
    export default {
        props: {
            value: String,
            startDate: String,
            endDate: String,
            name: String,
            size: String,
            placeholder: {
                default: '请选择时间'
            },
            disabled: {
                default: false
            },
            pickTime: {
                default: false
            },
            // 00:00:00
            formatToMinimum: {
                default: false
            },
            // 23:59:59
            formatToMaximum: {
                default: false
            },
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
            },
            startDate: function(val) {
                if (this.disabled) return;
                $(this.$el).data('datetimepicker').setStartDate(val);
            },
            endDate: function(val) {
                if (this.disabled) return;
                $(this.$el).data('datetimepicker').setEndDate(val);  
            }
        },
        beforeDestory: function() {
            $(this.$el).off('.datetimepicker');
        },
        mounted: function() {
            if (this.disabled) return;

            var $el = $(this.$el);
            var vm = this;

            var options = {
                language: 'zh-CN',
                pickTime: this.pickTime,
                formatToMinimum: this.formatToMinimum,
                formatToMaximum: this.formatToMaximum
            };

            if (this.startDate) {
                options.startDate = this.startDate;
            }

            if (this.endDate) {
                options.endDate = this.endDate;
            }

            if (options.pickTime) {
                options.format = 'yyyy-MM-dd hh:mm:ss';
                options.autoclose = false;
            } else {
                options.format = 'yyyy-MM-dd';
                options.autoclose = true;
            }

            $el.datetimepicker(options)
                .on('click.datetimepicker', '.calendar', function() {
                    vm.$emit('input', ''); 
                })
                .on('changeDate.datetimepicker', function(e) {
                    vm.$emit('input', e.formattedDate);
                });
        },
        methods: {
            toUTCDate: function(y, m, d, h, M, s) {
              return new Date(y, m, d, h, M, s);
            }
        }
    }
</script>
