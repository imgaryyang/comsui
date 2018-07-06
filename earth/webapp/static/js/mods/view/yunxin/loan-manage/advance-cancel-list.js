define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var $ = jQuery;
    var AdvanceCancelListView = TableContentView.extend({
        initialize: function() {
            AdvanceCancelListView.__super__.initialize.call(this, arguments);
            this.$showPopover = $('.statistical-amount');
            this.listenTo(this, 'show-popover:amountStatistics', this.showAmountStatisticsPopover);
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
                url: global_config.root + '/capital/refundbill/amountStatistics',
                data: data
            };
            opt.success = function(resp) {
                var popover = this.$showPopover.data('bs.popover');
                if (resp.code == 0) {
                    var refundAmount = 0;
                    if (!_.isEmpty(resp.data)) {
                        refundAmount = resp.data.refundAmount.formatMoney(2,'');
                    }

                    var content = '<div><span class="text-muted">计划放款金额:</span>' + refundAmount + '</div>';
                    popover.options.content = content;

                } else {
                    popover.options.content = resp.message;
                }
                this.$showPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
        }
    });

    module.exports = AdvanceCancelListView;
});