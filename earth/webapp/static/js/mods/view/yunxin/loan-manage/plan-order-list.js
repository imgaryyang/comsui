define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var $ = jQuery;
    var PlanOrderListView = TableContentView.extend({
        initialize: function() {
            PlanOrderListView.__super__.initialize.call(this, arguments);
            this.initDateTimePicker();

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
                url: global_config.root + '/remittance/application/amountStatistics',
                data: data
            };
            opt.success = function(resp) {
                var popover = this.$showPopover.data('bs.popover');
                if (resp.code == 0) {
                    var plannedAmount = 0;
                    var actualAmount = 0;
                    var differenceAmount = 0;
                    if (!_.isEmpty(resp.data)) {
                        plannedAmount = resp.data.plannedAmount.formatMoney(2,'');
                        actualAmount = resp.data.actualAmount.formatMoney(2,'');
                        differenceAmount = resp.data.differenceAmount.formatMoney(2,'');
                    }

                    var content = '<div><span class="text-muted">计划放款金额:</span>' + plannedAmount +
                        '<br/><span class="text-muted">实际放款金额:</span>' + actualAmount +
                        '<br/><span class="text-muted">差值:</span>' + differenceAmount + '</div>';
                    popover.options.content = content;

                } else {
                    popover.options.content = resp.message;
                }
                this.$showPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
        },
        initDateTimePicker: function() {
            $('.datetimepicker-form-control').css('width', '135');
        }
    });

    module.exports = PlanOrderListView;
});