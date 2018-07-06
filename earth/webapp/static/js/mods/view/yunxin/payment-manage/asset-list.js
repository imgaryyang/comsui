define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var popupTip = require('component/popupTip');

    var $ = jQuery;
    var root = global_config.root;

    var AssetListView = TableContentView.extend({
        action: {
            getRepaymentInfo: function(assetSetId) {
                return root + '/repaymentPlan/query/' + assetSetId + '/repaymentInfo';
            }
        },
        events: {
            'click .text-overflow': function(e) {
                if (window.getSelection) {
                    var range = document.createRange();
                    range.selectNode($(e.target)[0]);
                    window.getSelection().addRange(range); // 选择对象
                    document.execCommand('Copy'); // 执行浏览器复制命令
                }
            },
            'mouseover .showPopover': 'showPopover',
            'mouseout .showPopover': 'closePopover'
        },
        initialize: function() {
            AssetListView.__super__.initialize.apply(this, arguments);
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
                url: global_config.root + '/assets/amountStatistics',
                data: data
            };
            opt.success = function(resp) {
                var popover = this.$showPopover.data('bs.popover');
                if (resp.code == 0) {
                    var plannedAmount = 0;
                    var actualAmount = 0;
                    var differenceAmount = 0;

                    if (!_.isEmpty(resp.data)) {
                        plannedAmount = resp.data.plannedAmount.formatMoney(2, '');
                        actualAmount = resp.data.actualAmount.formatMoney(2, '');
                        differenceAmount = resp.data.differenceAmount.formatMoney(2, '');
                    }

                    var content = '<div><span class="text-muted">应还款金额:</span>' + plannedAmount +
                        '<br/><span class="text-muted">实际还款金额:</span>' + actualAmount +
                        '<br/><span class="text-muted">差值:</span>' + differenceAmount + '</div>';
                    popover.options.content = content;

                } else {
                    popover.options.content = resp.message;
                }
                this.$showPopover.popover('show');
            }.bind(this);

            $.ajax(opt);
        },
        showPopover: function(e) {
            e.preventDefault();
            var self = this;
            var $el = $(e.target);
            var assetsetid = $el.parent('td').data('asset-set-id');
            this.timer = setTimeout(function() {
                var opt = {
                    dataType: 'json',
                    url: self.action.getRepaymentInfo(assetsetid)
                };
                opt.success = function(resp) {
                    $el.popover({
                        content: function() {
                            if (resp.code == 0) {
                                return '<div><span class="text-muted">计划还款本金:</span>' + (+resp.data.loanAssetPrincipal).formatMoney(2, '') +
                                    '<br/><span class="text-muted">计划还款利息:</span>' + (+resp.data.loanAssetInterest).formatMoney(2, '') +
                                    ' <br/><span class="text-muted">贷款服务费:</span>' + (+resp.data.loanServiceFee).formatMoney(2, '') +
                                    '<br/><span class="text-muted">技术维护费:</span>' + (+resp.data.loanTechFee).formatMoney(2, '') +
                                    '<br/><span class="text-muted">其他费用:</span>' + (+resp.data.loanOtherFee).formatMoney(2, '') +
                                    '<br/><span class="text-muted">逾期费用合计:</span>' + (+resp.data.totalOverDueFee).formatMoney(2, '') +
                                    '<br/></div>';
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
        closePopover: function(e) {
            e.preventDefault();
            clearTimeout(this.timer);
            $('body').find('.popover').popover('hide');
        }
    });
    module.exports = AssetListView;

});