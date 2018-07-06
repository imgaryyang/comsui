define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var popupTip = require('component/popupTip');

    var $ = jQuery;
    var $body = $('body');
    var root = global_config.root;
    var tmpl = [
        '{% if ((+size) >= 5) { %}',
        '    <h3 class="title" style="padding: 8px 14px; margin: -9px -14px 9px; font-size: 14px; background-color: #f7f7f7; border-bottom: 1px solid #ebebeb; border-radius: 5px 5px 0 0;">统计数量前5的收款方银行</h3>',
        '{% } %}',
        '{% _.each(dataMap, function(val, key) { %}',
        '    <div className="item clearfix" style="width: 180px">',
        '        <span class="left">{%= key %}</span>',
        '        <span class="right pull-right">{%= val %}条</span>',
        '    </div>',
        '{% }) %}'
    ].join('');

    var OnlineAdvanceList = TableContentView.extend({
        dataStatisticsTemplate: _.template(tmpl),
        events: {
            'click .icon-bankcard': 'onClickBankcard',
            'mouseover .showPopover': 'showPopover',
            'mouseout .showPopover': 'showPopover',
            'click #exportData': 'onClickExportData',
            'click .pagecontrol-show-popover': 'onClickPageControlShowPopover'
        },
        actions: {
            export: root + '/capital/plan/execlog/export',
            getDataStatistics: root + '/capital/plan/execlog/dataStatistics',
            execlogAmountStatistics: root + '/capital/plan/execlog/amountStatistics'
        },
        initialize: function() {
            this.$showPopover = this.$('.pagecontrol-show-popover');
            this.$amountStatisticsPopover = $('.statistical-amount');

            this.listenTo(this, 'show-popover:amountStatistics', this.showAmountStatisticsPopover);
            this.on('fetch:dataStatistics', function(resp) {
                var popover = this.$showPopover.data('bs.popover');

                if (resp.code == 0) {
                    if (resp.data.size >= 5) {
                        popover.options.title = '统计数量前5的银行';
                    }

                    if (resp.data.size === 0) {
                        popover.options.content = '没有统计数据';
                    } else {
                        popover.options.content = this.dataStatisticsTemplate(resp.data);
                    }

                } else {
                    popover.options.content = resp.message;
                }

                this.$showPopover.popover('show');
            });

            OnlineAdvanceList.__super__.initialize.apply(this, arguments);
        },
        getDataStatistics: function() {
            var opt = {
                url: this.actions.getDataStatistics,
                data: this.collectParams(),
                dataType: 'json'
            };

            opt.success = function(resp) {
                this.trigger('fetch:dataStatistics', resp);
            }.bind(this);

            $.ajax(opt);
        },
        showAmountStatisticsPopover: function(queryAmount) {
            if (!queryAmount) return;
            this.queryAmountStatistics();
        },
        queryAmountStatistics: function() {
            var data = this.collectParams();
            var opt = {
                type: 'post',
                dataType: 'json',
                url: this.actions.execlogAmountStatistics,
                data: data
            };
            opt.success = function(resp) {
                if (resp.code == 0) {
                    var remittanceAmount = 0;
                    if (!_.isEmpty(resp.data)) {
                        remittanceAmount = resp.data.remittanceAmount.formatMoney(2,'');
                    }

                    var popover = this.$amountStatisticsPopover.data('bs.popover');
                    var content = '<div><span class="text-muted">放款金额:</span>' + remittanceAmount + '</div>';
                    popover.options.content = content;

                } else {
                    popover.options.content = resp.message;
                }
                this.$amountStatisticsPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
        },
        showPopover: function(e) {
            var el = $(e.target);
            var popoverContent = el.siblings('.account').html();
            el.popover({
                content: popoverContent
            });
            el.popover('toggle');
        },
        onClickPageControlShowPopover: function(e) {
            var el = $(e.target);
            this.destroyOtherPopover();
            el.popover();
            el.data('bs.popover').options.content = '数据统计中...';
            el.popover('show');
            this.getDataStatistics();
        },
        onClickBankcard: function(e) {
            e.preventDefault();
            this.showPopover(e);
            var el = $(e.target);
            el.on('shown.bs.popover', function() {
                $('body').find('.popover').last().addClass('clickShow');
            });
            el.on('hidden.bs.popover', function() {
                el.removeClass('active');
            });
            el.on('hide.bs.popover', function() {
                el.removeClass('active');
            });
            el.toggleClass('active');
            return false;
        },
        checkDate: function() {
            var starttimeEl = $('.starttime-datepicker').last();
            var startDateVal = starttimeEl.find('.datetimepicker-form-control').first().val();
            if (_.isEmpty(startDateVal)) {
                popupTip.show('请选择起始日期');
                return false;
            }
            var endtimeEl = $('.endtime-datepicker').last();
            var endDateVal = endtimeEl.find('.datetimepicker-form-control').first().val();

            var startDate = new Date(startDateVal);
            var endDate = new Date(endDateVal);

            var diff = endDate.getTime() - startDate.getTime();
            if (diff > 7 * 24 * 1000 * 60 * 60) {
                popupTip.show('不允许时间间隔超过7天');
                return false;
            }
            return true;
        },
        onClickExportData: function(e) {
            e.preventDefault();
            if (!this.checkDate()) return;
            var data = $('#myForm').serialize();
            var opt = {
                url: this.actions.export,
                type: 'post',
                dataType: 'json',
                data: data
            };
            opt.success = function(resp) {
                if (resp.code == 0) {
                    window.location.href = root + '/report/export?reportId=9&' + data;
                } else {
                    popupTip.show(resp.message);
                }
            };
            $.ajax(opt);
        }
    });

    module.exports = OnlineAdvanceList;
});