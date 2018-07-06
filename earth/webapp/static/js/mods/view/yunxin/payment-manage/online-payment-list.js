define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');

    var $ = jQuery;
    var $body = $('body');
    var root = global_config.root;
    var tmpl = [
        '{% if ((+size) >= 5) { %}',
        '    <h3 class="title" style="padding: 8px 14px; margin: -9px -14px 9px; font-size: 14px; background-color: #f7f7f7; border-bottom: 1px solid #ebebeb; border-radius: 5px 5px 0 0;">统计数量前5的银行</h3>',
        '{% } %}',
        '{% _.each(dataMap, function(val, key) { %}',
        '    <div className="item clearfix" style="width: 180px">',
        '        <span class="left">{%= key %}</span>',
        '        <span class="right pull-right">{%= val %}条</span>',
        '    </div>',
        '{% }) %}'
    ].join('');

    module.exports = TableContentView.extend({
        dataStatisticsTemplate: _.template(tmpl),
        events: {
            'click .pagecontrol-show-popover': 'onClickShowPopover'
        },
        actions: {
            getDataStatistics: root + '/payment-manage/payment/dataStatistics'
        },
        initialize: function() {
            this.constructor.__super__.initialize.apply(this, arguments);
            this.$showPopover = this.$('.pagecontrol-show-popover');
            this.$showStatisticalAmountPopover = $('.statistical-amount');

            this.listenTo(this, 'show-popover:amountStatistics', this.showAmountStatisticsPopover);
            this.on('fetch:dataStatistics', function(resp) {
                var popover = this.$showPopover.data('bs.popover');

                if (resp.code == 0) {
                    if (resp.data.size >= 5) {
                        popover.options.content = '统计数量前5的银行';
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
        },
        showAmountStatisticsPopover: function(queryAmount) {
            if (!queryAmount) return;
            this.queryAmountStatistics();
        },
        queryAmountStatistics: function() {
            var data = this.collectParams();
            var opt = {
                dataType: 'json',
                url: global_config.root + '/payment-manage/payment/amountStatistics',
                data: data
            };

            opt.success = function(resp) {
                var popover = this.$showStatisticalAmountPopover.data('bs.popover');
                if (resp.code == 0) {

                    var transferAmount = 0;
                    if (!_.isEmpty(resp.data)) {
                        transferAmount = resp.data.transferAmount.formatMoney(2,'');
                    }

                    var content = '<div><span class="text-muted">代扣金额:</span>' + transferAmount + '</div>';
                    popover.options.content = content;

                } else {
                    popover.options.content = resp.message;
                }
                
                this.$showStatisticalAmountPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
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
        onClickShowPopover: function(e) {
            this.destroyOtherPopover();
            var el = $(e.target);
            el.popover();
            el.data('bs.popover').options.content = '数据统计中...';
            el.popover('show');
            this.getDataStatistics();
        }
    });

});