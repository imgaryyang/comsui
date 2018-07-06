export default {
    props: {
        value: {
            default: false
        }
    },
    watch: {
        show: function(current) {
            this.$emit('input', current);
            if (!current && this.$refs.form) {
                this.$refs.form.resetFields();
            }
        },
        value: function(current) {
            this.show = current;
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
