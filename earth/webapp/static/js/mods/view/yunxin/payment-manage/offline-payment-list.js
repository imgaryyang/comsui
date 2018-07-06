define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var $ = jQuery;
    var OfflinePaymentListView = TableContentView.extend({
        initialize: function() {
            OfflinePaymentListView.__super__.initialize.call(this, arguments);
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
                dataType: 'json',
                url: global_config.root + '/offline-payment-manage/payment/amountStatistics',
                data: data
            };
            opt.success = function(resp) {
                var popover = this.$showPopover.data('bs.popover');
                if (resp.code == 0) {

                    var paymentAmount = 0;
                    if (!_.isEmpty(resp.data)) {
                        paymentAmount = resp.data.paymentAmount.formatMoney(2,'');
                    }

                    var content = '<div><span class="text-muted">支付金额:</span>' + paymentAmount + '</div>';
                    popover.options.content = content;

                } else {
                    popover.options.content = resp.message;
                }
                this.$showPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
        }
    });

    module.exports = OfflinePaymentListView;
});