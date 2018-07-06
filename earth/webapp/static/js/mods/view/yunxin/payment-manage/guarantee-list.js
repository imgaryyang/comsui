define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var $ = jQuery;
    var GuaranteeListView = TableContentView.extend({
        initialize: function() {
            GuaranteeListView.__super__.initialize.call(this, arguments);
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
                url: global_config.root + '/guarantee/order/amountStatistics',
                data: data
            };
            opt.success = function(resp) {
                var popover = this.$showPopover.data('bs.popover');
                if (resp.code == 0) {
                    var rentAmount = 0;
                    if (!_.isEmpty(resp.data)) {
                        rentAmount = resp.data.rentAmount.formatMoney(2,'');
                    }

                    var content = '<div><span class="text-muted">担保金额:</span>' + rentAmount + '</div>';
                    popover.options.content = content;

                } else {
                    popover.options.content = resp.message;
                }
                this.$showPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
        }
    });

    module.exports = GuaranteeListView;
});