export default {
    props: {
        value: {
            default: false
        }
    },
    watch: {
        show: function(cur) {
            this.$emit('input', cur);
        },
        value: function(cur) {
            this.show = cur;
        }
    },
    methods: {
        fetchSysLog: function() {
            var sysLog = this.$parent.$refs.sysLog;
            if (sysLog) {
                sysLog.fetch();
            }
        }
    }
}
