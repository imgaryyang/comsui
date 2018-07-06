define(function(require, exports, module) {
    var PageControl = require('./pageControl');
    var util = require('scaffold/util');

    var $ = jQuery;
    var loadingImg = global_const.loadingImg;
    var extractParamFromDifferentDom = util.extractValueFromDifferentDom;

    var Pagination = Backbone.View.extend({
        events: {
            'click .redirect,.prev,.next,.first-page,.last-page': 'onClickPageNavigateBtn',
            'click #lookup': 'query',
            'click .check-box': 'onClickCheckbox',
            'change .lookup-params': function(e) {
                // 如果是时间选择框的话会触发两次，事件冒泡
                if ($(e.target).parents('.operations').length) return;
                if ($(e.target).is('[autoquery=false]')) return;
                this.query();
            }
        },
        initialize: function(options) {
            this.tableListEl = this.$('table[data-action]');
            this.lookupParamsEl = this.$('.lookup-params');
            this.rowNum = this.tableListEl.find('thead th').length;

            options.template && (this.template = options.template);
            options.queryAction && (this.queryAction = options.queryAction);

            if (typeof this.template !== 'function') {
                var tmplStr = this.$('.template').html() || '';
                this.template = _.template(tmplStr);
            }

            if (!this.queryAction) {
                this.queryAction = this.tableListEl.data('action');
            }

            if (!this.template || !this.queryAction) return;

            this.pageControl = new PageControl($.extend({
                el: this.$('.page-control').get(0),
                url: this.queryAction
            }, _.omit(options, 'el', 'template')));

            this.listenTo(this.pageControl, 'next:pagecontrol prev:pagecontrol redirect:pagecontrol', this.refreshTableDataList);
            this.listenToOnce(this.pageControl, 'request', function() {
                var htm = '<tr><td style="text-align: center;" colspan="' + this.rowNum + '"></td></tr>';
                var el = $(htm).find('td').html(loadingImg.clone());
                this.tableListEl.find('tbody').html(el);
            });
            this.initCheckOperate();

            this.getFirstData();
        },
        initCheckOperate: function() {
            var self = this;
            var execCheck = function(els, isChecked) {
                for (var i = 0; i < els.length; i++) {
                    var one = els.eq(i);
                    if (one.is(':disabled') || one.is('.disabled')) continue;
                    one[0].checked = isChecked;
                }
            };

            this.on('checkall.tablelist', function(isChecked, target) {
                var checkboxs = self.$('.check-box');
                execCheck(checkboxs, isChecked);

                self.trigger('checkbox.all', isChecked, target);
            });

            this.on('checkitem.tablelist', function(isChecked, target) {
                if (isChecked === false) {
                    var checkboxalls = self.$('.check-box-all');
                    execCheck(checkboxalls, isChecked);

                }
                self.trigger('checkbox.item', isChecked, target);
            });

            // 不能写死呀
            // this.on('refreshdata.tablelist', function() {
            //     self.checkAll(false);
            // });
        },
        refreshTableDataList: function(data, opepration, response, query) {
            if (!this.template) return;
            var htm;
            if (data.length < 1) {
                htm = '<tr class="nomore"><td style="text-align: center;" colspan="' + this.rowNum + '">没有更多数据</td></tr>';
            } else {
                htm = this.template({
                    list: this.polish(data)
                });
            }
            this.tableListEl.find('tbody').html(htm);
            this.trigger('refresh', data, opepration, response, query);
        },
        collectQueryParams: function(resultParams) {
            var paramsItems = this.lookupParamsEl.find('.real-value');

            for (var i = 0, len = paramsItems.length; i < len; i++) {
                $.extend(resultParams, extractParamFromDifferentDom(paramsItems.eq(i)));
            }

            return resultParams;
        },
        collectSortParams: function(resultParams) {
            var order = this.tableListEl.find('thead .sort');
            var res = {};
            $.each(order, function(index, item) {
                var tar = $(item);
                var name = tar.data('paramname');
                var value = tar.hasClass('desc') ? 'DESC' : (tar.hasClass('asc') ? 'ASC' : '');
                if (name && value) {
                    res[name] = value;
                }
            });

            $.extend(resultParams, res);

            return resultParams;
        },
        collectParams: function() {
            var resultParams = {};

            this.collectQueryParams(resultParams);
            this.collectSortParams(resultParams);

            return resultParams;
        },
        getFirstData: function() {
            var params = this.collectParams();
            this.pageControl.redirect(1, params);
        },
        query: function() {
            var params = this.collectParams();
            this.pageControl.redirect(1, params);
            this.trigger('page:change');
        },
        queryCurrentPage: function() {
            var params = this.collectParams();
            this.pageControl.redirect(this.pageControl.getCurrentPageIndex(), params);
            this.trigger('page:change');
        },
        queryPrevPage: function() {
            var params = this.collectParams();
            this.pageControl.prev(params);
            this.trigger('page:change');
        },
        polish: function(list) {
            return list;
        },
        onClickPageNavigateBtn: function(e) {
            e.preventDefault();

            var tar = $(e.target);
            var params = this.collectParams();

            if (tar.hasClass('prev')) {
                this.pageControl.prev(params);
            } else if (tar.hasClass('next')) {
                this.pageControl.next(params);
            } else if (tar.hasClass('redirect')) {
                this.pageControl.importPageIndexRedirect(params);
            } else if (tar.hasClass('first-page')) {
                this.pageControl.first(params);
            } else if (tar.hasClass('last-page')) {
                this.pageControl.last(params);
            }
            this.trigger('page:change');
        },
        checkAll: function(flag, target) {
            this.allChecked = flag;
            this.trigger('checkall.tablelist', flag, target);
        },
        checkItem: function(flag, target) {
            this.itemChecked = flag;
            this.trigger('checkitem.tablelist', flag, target);
        },

        onClickCheckbox: function(e) {
            var flag = e.target.checked;
            var tar = $(e.target);

            tar.is('.check-box-all') ? this.checkAll(flag, tar) : this.checkItem(flag, tar);
        }
    });

    Pagination.find = function(selector, ctx) {
        var Constructor = this;
        var els = $(ctx || document).find(selector);
        for (var i = 0; i < els.length; i++) {
            var $el = els.eq(i);
            var obj = $el.data('pagination');
            if (!obj) {
                $el.data('pagination', new Constructor({
                    el: $el[0]
                }));
            }
        }
    };

    module.exports = Pagination;


});