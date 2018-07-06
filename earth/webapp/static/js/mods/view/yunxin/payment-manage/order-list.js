define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var $ = jQuery;
    var OrderListView = TableContentView.extend({
        initialize: function() {
            OrderListView.__super__.initialize.call(this, arguments);
            this.$showPopover = $('.statistical-amount');
            this.listenTo(this, 'show-popover:amountStatistics', this.showAmountStatisticsPopover);
        },
        events: {
            'mouseover .showPopover': 'onShowPopover',
            'mouseout .showPopover': 'onClosePopover'
        },
        showAmountStatisticsPopover: function(queryAmount) {
            if (!queryAmount) return;
            this.queryAmountStatistics();
        },
        queryAmountStatistics: function() {
            var data = this.collectParams();
            var opt = {
                dataType: 'json',
                url: global_config.root + '/payment-manage/order/paymentAmountStatistics',
                data: data
            };
            opt.success = function(resp) {
                var popover = this.$showPopover.data('bs.popover');
                if (resp.code == 0) {

                    var plannedAmount = 0;
                    var penaltyAmount = 0;
                    var paymentAmount = 0;
                    var totalAmount = 0;

                    if (!_.isEmpty(resp.data)) {
                        plannedAmount = resp.data.plannedAmount;
                        penaltyAmount = resp.data.penaltyAmount;
                        paymentAmount = resp.data.paymentAmount;
                        totalAmount = resp.data.totalOverdueFee;

                        plannedAmount = plannedAmount.formatMoney(2, '');
                        penaltyAmount = penaltyAmount.formatMoney(2, '');
                        paymentAmount = paymentAmount.formatMoney(2, '');
                        totalAmount = totalAmount.formatMoney(2, '');
                    }

                    var content = '<div><span class="text-muted">计划还款本金:</span>' + plannedAmount +
                        '<br/><span class="text-muted">计划还款利息:</span>' + penaltyAmount +
                        '<br/><span class="text-muted">差异罚息:</span>' + paymentAmount +
                        '<br/><span class="text-muted">结算金额:</span>' + totalAmount + '</div>';
                    popover.options.content = content;

                } else {
                    popover.options.content = resp.message;
                }
                this.$showPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
        },
        onShowPopover: function(e) {
            var $el = $(e.target);
            var self = this;
            var orderId = $el.parent().data('order-id');
            this.timer = setTimeout(function() {
                var opt = {
                    url: global_config.root + '/payment-manage/order/' + orderId + '/chargesDetail',
                    dataType: 'json'
                };
                opt.success = function(resp) {
                    $el.popover({
                        content: function() {
                            if (resp.code == 0) {
                            	if($.isEmptyObject(resp.data)){ 
                            		return '<div><span class="text-muted">还款本金: -</span>' +
                                    '<br/><span class="text-muted">还款利息: -</span>'  +
                                    '<br/><span class="text-muted">贷款服务费: -</span>'  +
                                    '<br/><span class="text-muted">技术维护费: -</span>' +
                                    '<br/><span class="text-muted">其他费用: -</span>' +
                                    '<br/><span class="text-muted">逾期罚息: -</span>' +
                                    '<br/><span class="text-muted">逾期违约金: -</span>'  +
                                    '<br/><span class="text-muted">逾期服务费: -</span>' +
                                    '<br/><span class="text-muted">逾期其他费用: -</span>' +
                                    '<br/><span class="text-muted">逾期费用合计: -</span>' +
                                    '<br/></div>';
                            	} else {
                            		var loanAssetPrincipal = 0;
                            		var loanAssetInterest = 0;
	                                var loanServiceFee = 0;
	                                var loanTechFee = 0;
	                                var loanOtherFee = 0;
	                                var overdueFeePenalty = 0;
	                                var overdueFeeObligation = 0;
	                                var overdueFeeService = 0;
	                                var overdueFeeOther = 0;
	                                var totalOverdue = 0;

	                                if (!_.isEmpty(resp.data.data)) {
	                                    var repaymentChargesDetail = resp.data.data;
	                                    loanAssetPrincipal = repaymentChargesDetail.loanAssetPrincipal;
	                                    loanAssetInterest = repaymentChargesDetail.loanAssetInterest;
	                                    loanServiceFee = repaymentChargesDetail.loanServiceFee;
	                                    loanTechFee = repaymentChargesDetail.loanTechFee;
	                                    loanOtherFee = repaymentChargesDetail.loanOtherFee;
	                                    overdueFeePenalty = repaymentChargesDetail.overdueFeePenalty;
	                                    overdueFeeObligation = repaymentChargesDetail.overdueFeeObligation;
	                                    overdueFeeService = repaymentChargesDetail.overdueFeeService;
	                                    overdueFeeOther = repaymentChargesDetail.overdueFeeOther;
	
	                                    totalOverdue = overdueFeePenalty + overdueFeeObligation + overdueFeeService + overdueFeeOther;

		                                }
		                                return '<div><span class="text-muted">还款本金:</span>' + (+loanAssetPrincipal).formatMoney(2, '') +
		                                    '<br/><span class="text-muted">还款利息:</span>' + (+loanAssetInterest).formatMoney(2, '') +
		                                    '<br/><span class="text-muted">贷款服务费:</span>' + (+loanServiceFee).formatMoney(2, '') +
		                                    '<br/><span class="text-muted">技术维护费:</span>' + (+loanTechFee).formatMoney(2, '') +
		                                    '<br/><span class="text-muted">其他费用:</span>' + (+loanOtherFee).formatMoney(2, '') +
		                                    '<br/><span class="text-muted">逾期罚息:</span>' + (+overdueFeePenalty).formatMoney(2, '') +
		                                    '<br/><span class="text-muted">逾期违约金:</span>' + (+overdueFeeObligation).formatMoney(2, '') +
		                                    '<br/><span class="text-muted">逾期服务费:</span>' + (+overdueFeeService).formatMoney(2, '') +
		                                    '<br/><span class="text-muted">逾期其他费用:</span>' + (+overdueFeeOther).formatMoney(2, '') +
		                                    '<br/><span class="text-muted">逾期费用合计:</span>' + (+totalOverdue).formatMoney(2, '') +
		                                    '<br/></div>';
                            			}
	                            } else {
	                                return '<div>' + resp.message + '</div>';
	                            }
                        }
                    });
                    $el.popover('toggle');
                };
                $.ajax(opt);

            }, 300);
        },
        onClosePopover: function(e) {
            e.preventDefault();
            clearTimeout(this.timer);
            $('body').find('.popover').popover('hide');
        }
    });

    module.exports = OrderListView;
});