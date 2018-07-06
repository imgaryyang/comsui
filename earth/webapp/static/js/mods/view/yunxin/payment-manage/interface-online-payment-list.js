define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var $ = jQuery;
    var InterfaceOnlinePayment = TableContentView.extend({
        initialize: function() {
            InterfaceOnlinePayment.__super__.initialize.call(this, arguments);
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
                url: global_config.root + '/interfacePayment/list/amountStatistics',
                data: data
            };
            opt.success = function(resp) {
                var popover = this.$showPopover.data('bs.popover');
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
                this.$showPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
        }
    });

    module.exports = InterfaceOnlinePayment;
});