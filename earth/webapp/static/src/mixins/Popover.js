import Vue from 'vue';

export default {
    props: {
        trigger: {
            default: 'hover', // click, hover
        },
        title: {
            default: ''
        },
        content: {
            default: ''
        },
        placement: {
            default: 'top' // bottom, top
        }
    },
    data: function() {
        return {
            visible: false
        };
    },
    mounted: function() {
        var vm = this;
        var events = {hover: 'mouseenter mouseleave', click: 'click'};
        var trigger = this.$refs.trigger;

        if (!trigger) {
            console.error('需要trigger元素');
            return;
        }

        $(trigger).on(events[this.trigger], this.toggle);
        this._trigger = trigger;
    },
    beforeDestroy: function() {
        if (this._trigger) {
            // 这个trigger就只是用来show和hide不应该在它上面绑定其他事件
            // 所以照理就应该off全部
            $(this._trigger).off();
        }
    },
    methods: {
        toggle: function(e) {
            this.visible = !this.visible;
            if (!this.visible) return;
            Vue.nextTick(() => {
                var popover = this.$refs.popover;
                if (!popover) {
                    console.error('需要popover元素');
                    return;
                }

                var trigger = this.$refs.trigger.children[0];
                var offset = $(trigger).offset();
                var left;
                var top;

                switch (this.placement) {
                    case 'top':
                        left = offset.left - popover.offsetWidth / 2 + trigger.offsetWidth / 2;
                        top = offset.top - popover.offsetHeight - 15;
                        break
                    case 'bottom':
                        left = offset.left - popover.offsetWidth / 2 + trigger.offsetWidth / 2;
                        top = offset.top + trigger.offsetHeight + 15;
                        break
                }

                popover.style.left = left + 'px';
                popover.style.top = top + 'px';
            });
        }
    }
}