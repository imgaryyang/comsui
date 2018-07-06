import { SET_CACHE_QUERY } from 'store/mutationTypes';
import { purify, filterQueryConds } from 'assets/javascripts/util';

const pick = function(dest, src) {
    var res = {};
    Object.keys(src).forEach(key => {
        if (!dest.hasOwnProperty(key)) return;

        var value = src[key];

        if (typeof value === 'string' && value !== '') {
            try {
                /**
                 * value的几种情况：
                 * '1' => 1 这种情况有失会有问题会有问题
                 * '23231123243423423423423432443' => '23231123243423423423423432443'   不能转变为数字，因为会变成科学计数法，所以事数字也直接返回字符串
                 * '22323adsdfasdf2323' => '22323adsdfasdf2323'
                 * '[1, 2, 3]' => [1, 2, 3]
                 */

                if (/^\[[\s\S]*\]$/.test(value)) {
                    value = JSON.parse(value);
                }

                // 是否是科学计数法表示的极大极小的数 
                // var reg = /^(\d+(\.\d+)?)(e)([+-]?\d+)$/;
                // value = isNaN(value) 
                //     ? JSON.parse(value) 
                //     : reg.test((+value).toString())
                //         ? value 
                //         : +value;
            } catch (error) {}
        }

        res[key] = value;
    });
    return res;
};

// disableGlobalQueryCache: 是否在列表页之间记录查询条件，默认true
export default {
    data: function() {
        return {
            listenConditionChange: false
        }
    },
    computed: {
        conditions: function() {
            var currentQueryConds = filterQueryConds(this.queryConds);
            return Object.assign({}, currentQueryConds, this.comboConds, this.sortConds, this.pageConds);
        },
        filterConds: function() {
            return Object.assign({}, this.queryConds, this.comboConds);
        }
    },
    watch: {
        initializing: function(current) {
            if (current) {
                this.initializeLoading = this.$loading({
                    text: '正在初始化',
                    target: this.$el
                });
            } else {
                this.initializeLoading && this.initializeLoading.close();
            }
        }
    },
    activated: function() {
        this.disableGlobalQueryCache = true; // 暂时不使用该特性

        this.setRouteQueryListener();

        if (!this.initializing) {
            this.setChangeListener();
        }

        if (this.initializing) return;

        if (!this.equalTo(purify(this.lastRouteQuery), purify(this.$route.query))) {
            // query不同则认为相当于重新进入页面，重置
            
            // 有这两种情况：
            // 回退时可能需要刷新列表，但要保持pageIndex不变啊！
            // $route.query中queryConds,comboConds发生变化又要设置pageIndex为1
            if (this.disableGlobalQueryCache) {
                this.extractFromQuery(this.mergeDefaultQuery(this.$route.query));
            } else {
                this.extractFromQuery(this.mergeDefaultQuery(this.$store.state.query, this.$route.query));
            }

            this.dataSource.list = [];
            this.dataSource.size = 0;
            this.dataSource.error = '';
            this.dataSource.fetching = false;

            if (this.autoload !== false || this.existRouteQueryExceptTimestamp()) {
                this.fetch();
            }
        }
    },
    deactivated: function() {
        this.unsetRouteQueryListener();
        this.unsetChangeListener();
    },
    beforeRouteLeave: function(to, from, next) {
        if (!this.disableGlobalQueryCache) {
            this.$store.commit(SET_CACHE_QUERY, JSON.parse(JSON.stringify(this.conditions)));
        }
        this.lastRouteQuery = this.$route.query;
        next();
    },
    methods: {
        unsetRouteQueryListener: function() {
            typeof this.unwatchRouteQuery === 'function' && this.unwatchRouteQuery();
            this.unwatchRouteQuery = null;
        },
        setRouteQueryListener: function() {
            if (!this.unwatchRouteQuery) {
                this.unwatchRouteQuery = this.$watch('$route.query', (current, previous) => {
                    this.dataSource.list = [];
                    this.dataSource.size = 0;
                    this.dataSource.error = '';
                    this.dataSource.fetching = false;

                    this.extractFromQuery(this.mergeDefaultQuery(current));
                });
            }
        },
        unsetChangeListener: function() {
            typeof this.unwatchConditions === 'function' && this.unwatchConditions();
            this.unwatchConditions = null;

            typeof this.unwatchFilterConds === 'function' && this.unwatchFilterConds();
            this.unwatchFilterConds = null;

            typeof this.unwatchSortConds === 'function' && this.unwatchSortConds();
            this.unwatchSortConds = null;

            typeof this.unwatchPageConds === 'function' && this.unwatchPageConds();
            this.unwatchPageConds = null;
        },
        setChangeListener: function() {
            setTimeout(() => {
                if (!this.unwatchConditions) {
                    this.unwatchConditions = this.$watch('conditions', (current, previous) => {
                        this.previousConditions = previous;
                    }, {deep: true});
                }

                if (this.listenConditionChange !== false && !this.unwatchFilterConds) {
                    this.unwatchFilterConds = this.$watch('filterConds', () => {
                        if (this.pageConds) {
                            this.pageConds.pageIndex = 1;
                        }
                        this.fetch();
                    }, {deep: true});
                }

                if (this.sortConds && !this.unwatchSortConds) {
                    this.unwatchSortConds = this.$watch('sortConds', (current, previous) => {
                        this.fetch();
                    }, {deep: true});
                }

                if (this.pageConds && !this.unwatchPageConds) {
                    this.unwatchPageConds = this.$watch('pageConds', (current, previous) => {
                        this.fetch();
                    }, {deep: true});
                }
            }, 0);
        },
        _initialize: function() {
            this.initializing = true;
            this.initialize()
                .then(() => {
                    if (this.$refs.lookup) {
                        this.$refs.lookup.$on('click', () => {
                            if (this.pageConds) {
                                this.pageConds.pageIndex = 1;
                            }
                            this.fetch();
                        });
                    }

                    this.defaultConditions = Object.assign({}, this.conditions);

                    if (this.disableGlobalQueryCache) {
                        this.extractFromQuery(this.mergeDefaultQuery(this.$route.query));
                    } else {
                        this.extractFromQuery(this.mergeDefaultQuery(this.$store.state.query, this.$route.query));
                    }

                    this.initializing = false;

                    this.$emit('ready');
                });
        },
        onSortChange: function({column, prop, order}) {
            this.sortConds.sortField = prop;
            this.sortConds.isAsc = order;
        },
        mergeDefaultQuery: function() {
            // 如果不显示指定pageIndex，那么mergeDefaultQuery一定会把pageIndex置为1
            var results = Array.prototype.slice.call(arguments);
            results.unshift(this.defaultConditions);
            results.unshift({});

            return Object.assign.apply(Object, results)
        },
        mergeLastQuery: function() {
            
        },
        extractFromQuery: function(query) {
            // 注意，还有一个问题是：
            // 多选的select查询条件，通过URL查询条件直接改变值会触发两次fetch过程，可能是因为：对数组的处理是先push再splice，触发了两次conditions的watch
            if (this.pageConds) {
                this.pageConds = Object.assign({}, this.pageConds, pick(this.pageConds, query));
            }

            if (this.queryConds) {
                this.queryConds = Object.assign({}, this.queryConds, pick(this.queryConds, query));
            }

            if (this.comboConds) {
                this.comboConds = Object.assign({}, this.comboConds, pick(this.comboConds, query));
            }

            if (this.sortConds) {
                this.sortConds = Object.assign({}, this.sortConds, pick(this.sortConds, query));   
            }
        }
    }
}