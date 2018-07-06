define(function(require, exports, module) {
    var Backdrop = require('./backdrop');
    var pathHelper = require('scaffold/path');
    var Pagination = require('component/pagination');
    var popupTip = require('component/popupTip');
    
    var SStorage = sessionStorage;
    var loadingImg = global_const.loadingImg;
    var root = global_config.root;
    var dashboardEl = $('.dashboard');

    var DashboardView = Backbone.View.extend({
        initialize: function() {

        },
        hide: function() {
            this.$el.addClass('hide');
        },
        show: function() {
            dashboardEl.addClass('hide');
            this.$el.removeClass('hide');
        }
    });

    var NoticeDetailView = DashboardView.extend({
        events: {
            'click .goback': 'onClickBack'
        },
        initialize: function(data) {
            NoticeDetailView.__super__.initialize.apply(this, arguments);
            if (!data) return;
            this.render(data);

        },
        render: function(data) {
            var detail = '<p class="notice-title"><strong></strong></p><p class="content-notice" style="white-space:pre-wrap;"></p>';
            this.$('.bd').html(detail);
            this.$('.bd').find('strong').text(unescape(data.mtitle));
            this.$('.bd').find('.content-notice').text(unescape(data.content));
        },
        onClickBack: function(e) {
            e.preventDefault();
            this.trigger('showNotice');
            this.hide();
        }
    });

    var NoticeView = DashboardView.extend({
        events: {
            'click .notice-item': 'onClickItem'
        },
        initialize: function() {
            NoticeView.__super__.initialize.apply(this, arguments);

            this.render();
        },
        render: function() {
            this.pagination = new Pagination({
                el: this.$('> .inner'),
                template: _.template($('#noticeTableTmpl').html()),
                pageRecordNum: 7
            });
        },
        onClickItem: function(e) {
            e.preventDefault();

            var data = $(e.target).parents('tr').data();
            data.content = $(e.target).parents('tr').find('.data-content').html();
            this.trigger('showNoticeDetail', data);
        }
    });

    var TODOView = DashboardView.extend({
        events: {
            'click [data-key]': 'onClickTabMenuItem',
            'change .selectpicker': 'onChangeSelectpicker',
            'click a[data-vue-path]': 'onClickVueHref',
            'click a[data-path]': 'onClickHref'
        },
        initialize: function() {
            TODOView.__super__.initialize.apply(this, arguments);
            this.tabContentCache = {};
            this.tabContentEl = this.$('.tab-content');
            this.tabMenuEl = this.$('.nav-tabs');
            this.selectpickerEl = this.$('.selectpicker');

            this.on('request', function() {
                this.tabContentEl.html(loadingImg.clone());
            });

            this.initActiveTab();

            if (this.$el.data('disable-toggle')) {
                this.fetchTabContent();
                return;
            }

            var self = this;

        },
        initActiveTab: function() {
            if (SStorage) {
                var activeTabKey = SStorage.getItem('active_tab_key');
                var $activeTab = this.$el.find('a[data-key=' + activeTabKey + ']').parent('li');
            }

            if (!$activeTab || $activeTab.length == 0) {
                $activeTab = this.$el.find('.nav-tabs li').first();
            }

            $activeTab.addClass('active');
        },
        onClickVueHref: function(e) {
            e.preventDefault();

            var $target = $(e.currentTarget);
            var attr = this.getSeletedProject();

            attr.financialContractIds = JSON.stringify(JSON.parse(attr.financialContractIds).map(function(item) {
                return +item;
            }));

            var appendHash = pathHelper.stringifyQueryObject(attr);

            var path = $target.data('vue-path');
            var hash = $target.data('hash');

            hash = hash ? (hash + '&' + appendHash) : appendHash;

            location.assign(encodeURI(path + '?' + hash));
        },
        onClickHref: function(e) {
            e.preventDefault();

            var $target = $(e.currentTarget);
            var attr = this.getSeletedProject();

            var appendHash = pathHelper.stringifyQueryObject(attr);
            var appendSearch = 't=' + Date.now();

            var path = $target.data('path');
            var hash = $target.data('hash');
            var search = $target.data('search');

            search = search ? (search + '&' + appendSearch) : appendSearch;
            hash = hash ? (hash + '&' + appendHash) : appendHash;

            location.assign(encodeURI(path + '?' + search + '#' + hash));
        },
        onChangeSelectpicker: function() {
            this.fetchTabContent();
        },
        onClickTabMenuItem: function(e) {
            e.preventDefault();

            var $target = $(e.currentTarget);
            var $parent = $target.parent();

            if ($parent.is('.active')) return;

            this.tabMenuEl.children().removeClass('active');
            $parent.addClass('active');

            if (SStorage) {
                SStorage.setItem('active_tab_key', $target.attr('data-key'));
            }

            this.fetchTabContent();
        },
        getActiveTabMenu: function() {
            return this.tabMenuEl.find('.active');
        },
        getActiveTabMenuHref: function() {
            var $active = this.getActiveTabMenu();
            var $target = $active.find('a');
            var href = $target.data('href');
            return href;
        },
        getSeletedProject: function() {
            var attr = {};
            var val = this.selectpickerEl.selectpicker('val');
            var ids = [];
            var uuids = [];
            if (val != null) {
                val.forEach(function(item) {
                    ids.push(item.split('&')[0]);
                    uuids.push(item.split('&')[1]);
                });
            }
            attr.financialContractIds = JSON.stringify(ids);
            attr.financialContractUuids = JSON.stringify(uuids);
            return attr;
        },
        fetchTabContent: function() {
            var self = this;
            var href = this.getActiveTabMenuHref();

            if (!href) return;

            var attr = this.getSeletedProject();

            self.trigger('request');

            $('<div/>').load(href, attr, function(resp, statusTxt, xhr) {
                if (statusTxt == 'error' || xhr.status != 200) {
                    self.tabContentEl.html('系统异常请稍候重试');
                } else if (!$(resp).data('dashboard-item')) {
                    self.tabContentEl.html('系统异常请稍候重试');
                } else {
                    self.tabContentEl.html(resp);

                    var total = 0;
                    self.tabContentEl.find('[data-total]').each(function() {
                        total += +$(this).data('total');
                    });
                    var $active = self.getActiveTabMenu();
                    $active.find('.total').html(total);
                }
                self.trigger('refresh', resp, statusTxt);
            });
        }
    });

    var Manager = Backbone.View.extend({
        initialize: function() {

            if (!dashboardEl.length) return;

            this.views = {};
            this.views.toDo = new TODOView({
                el: dashboardEl.get(0)
            });
            this.views.notice = new NoticeView({
                el: dashboardEl.get(1)
            });
            this.views.noticeDetail = new NoticeDetailView({
                el: dashboardEl.get(2)
            });
            var self = this;
            this.views.noticeDetail.on('showNotice', function() {
                self.views.notice.show();
            });
            this.views.notice.on('showNoticeDetail', function(data) {
                self.views.noticeDetail.render(data);
                self.views.noticeDetail.show();
            });
            this.backdrop = new Backdrop({
                zIndex: 7
            });

            this.listenTo(this.backdrop, 'close:backdrop', function() {
                dashboardEl.addClass('hide');
            });
            this.listenTo(this.backdrop, 'showView', function(view) {
                if (view == 'item-todo') {
                    self.showView('toDo');
                } else if (view == 'item-notice') {
                    self.showView('notice');
                }
            });
            this.listenTo(this.backdrop, 'open:backdrop', function(view) {
                var self = this;
                if (view == 'item-todo') {
                    self.views.toDo.fetchTabContent();
                    self.showView('toDo');
                } else if (view == 'item-notice') {
                    self.showView('notice');
                }
                //this.$el.removeClass('hide');
                //  this.fetchTabContent();
            });
            var self = this;
            var $btnToggleDashboard = $('.web-g-hd .btn-toggle-dashboard');
            $btnToggleDashboard.on('click', function(e) {
                if ($('#modifyPasswordTime').val() == 0) {
                    popupTip.show('首次登陆，请修改密码！', '', [{
                        text: '确定',
                        style: 'success',
                        handler: function() {
                            this.hide();
                        }
                    }]);
                    return;
                }
                if ($(dashboardEl.get(0)).data('disable-toggle')) return;
                var view = $(e.target).parent().attr('class');
                self.backdrop.toggle(view);
            });

            $(window).on('keyup.dashboard', function(e) {
                if (e.keyCode == 27) {
                    self.backdrop.hide();
                }
            });
        },
        showView: function(name, data) {
            if (!this.views[name]) return;
            this.views[name].render(data);
            this.views[name].show();
        }
    });


    module.exports = new Manager();
});
