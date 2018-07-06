
import MessageBox from 'components/MessageBox';
import { ajaxPromise, patchCancelPromise, searchify, purify } from 'assets/javascripts/util';

var J = JSON;
var O = Object;

export const extract = function(dest, src) {
    var res = {};
    O.keys(dest).forEach(key => {
        var value = src[key] != null ? src[key] : dest[key];
        try {
            res[key] = typeof value === 'object' ? value : J.parse(value);
        } catch (err) {
            res[key] = value;
        }
    });
    return res;
};

/*
    使用了四个属性，其中dataSource为内置声明的data属性
    action: '',
    autoload: 挂载后是否主动查询，默认是
    listenConditionChange: 条件变化是否自动查询，默认是
    conditions: {},
    dataSource: {
        size: 0,
        fetching: false,
        list: [],
        error: ''
    }
*/
export default {
    data: function() {
        return {
            initializing: false,
            dataSource: {
                size: 0,
                fetching: false,
                list: [],
                error: ''
            }
        };
    },
    beforeMount: function() {
        this.$on('ready', () => {
            this.setChangeListener();
            if (this.autoload !== false || this.existRouteQueryExceptTimestamp()) {
                this.fetch();
            }
        });

        this._initialize();
    },
    methods: {
        existRouteQueryExceptTimestamp: function() {
            if (!this.$route) return false;

            var keys = Object.keys(this.$route.query);

            var remains = keys.filter(name => name != 't')

            return remains.length > 0;
        },
        _initialize: function() {
            this.initializing = true;
            this.initialize()
                .then(() => {
                    this.$emit('ready');
                    this.initializing = false;
                });
        },
        initialize: function() {
            return new Promise(resolve => resolve());
        },
        unsetChangeListener: function() {
            typeof this.unwatchConditions === 'function' && this.unwatchConditions();
            this.unwatchConditions = null;
        },
        setChangeListener: function() {
            // setTimeout(() => {
                if (this.listenConditionChange !== false && !this.unwatchConditions) {
                    this.unwatchConditions = this.$watch('conditions', (current, previous) => {
                        this.previousConditions = previous;
                        this.fetch();
                    }, {deep: true});
                }
            // }, 0);
        },
        getData: function(options) {
            this.cancelPromise && this.cancelPromise.cancel();

            if (this.onRequest(options)) return;

            if (options.data && typeof options.data == 'object') {
                const source = options.data;

                // 为了兼容后台
                // 注意：不能直接修改传递进来的options.data
                var complement = {
                    page: source.pageIndex,
                    pageNumber: source.perPageRecordNumber,
                    isAsc: source.isAsc === 'ascending'
                        ? true
                        : source.isAsc === 'descending'
                            ? false
                            : null
                };

                options.data = purify(Object.assign({}, source, complement));
            }

            this.cancelPromise = patchCancelPromise(ajaxPromise(options));

            this.cancelPromise
                .promise
                .then(this.onSuccess)
                .catch(this.onError)
                .then(this.onComplete);
        },
        fetch: function() {
            if (this.fetchTimer) {
                clearTimeout(this.fetchTimer);
            }

            this.fetchTimer = setTimeout(() => {
                if (this.dataSource.fetching
                    && this.equalTo(purify(this.conditions), purify(this.previousConditions))) {
                    return
                }

                this.getData({
                    url: this.action,
                    data: this.conditions
                });
            }, 10);
        },
        equalTo: function(a, b) {
            // 获取对象属性的所有的键
            var aProps = Object.keys(a);
            var bProps = Object.keys(b);

            // 如果键的数量不同，那么两个对象内容也不同
            if (aProps.length != bProps.length) {
                return false;
            }

            for (var i = 0, len = aProps.length; i < len; i++) {
                var propName = aProps[i];

                // 如果对应的值不同，那么对象内容也不同
                if (a[propName] !== b[propName]) {
                    return false;
                }
            }

            return true;
        },
        parse: function(data) {
            return data;
        },
        onRequest: function() {
            this.dataSource.fetching = true;
        },
        onSuccess: function(data) {
            var d = this.parse(data);
            this.dataSource.list = d.list;
            this.dataSource.size = d.size;
            this.dataSource.error = '';
        },
        onError: function(message) {
            this.dataSource.error = message.toString();
            MessageBox.open(message);
        },
        onComplete: function() {
            this.dataSource.fetching = false;
        },
    }
}