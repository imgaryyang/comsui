define(function(require, exports, module) {
    var FormDialogView = require('baseView/baseFormView').FormDialogView;
    var popupTip = require('component/popupTip');

    var root = global_config.root;
    var $ = jQuery;

    var ModifyPenaltyView = FormDialogView.extend({
        template: _.template($('#modifyPenaltyTmpl').html(), {
            variable: 'obj'
        }),
        actions: {
            submit: function(orderId) {
                return root + '/payment-manage/order/' + orderId + '/edit';
            }
        },
        initialize: function() {
            ModifyPenaltyView.__super__.initialize.call(this, arguments);
            this.defineValidator();
        },
        defineValidator: function() {
            this.validator = this.$('.form').validate({
                rules: {
                    comment: {
                        required: true,
                        maxlength: 50
                    }
                }
            });
        },
        validate: function() {
            return this.validator.form();
        },
        save: function() {
            var self = this;
            var attr = this.extractDomData();
            attr.orderId = this.model.get('orderId');
            var opt = {
                url: this.actions.submit(this.model.get('orderId')),
                type: 'post',
                dataType: 'json',
                data: attr
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    // self.model.set(attr);
                }
                self.model.trigger('modify:penaltyAmount', attr, resp);
            };

            $.ajax(opt);
        },
        submitHandler: function() {
            if (!this.validate()) return;
            this.save();
            this.hide();
        }
    });

    var OrderDetailView = Backbone.View.extend({
        el: '.content',
        events: {
            'click #modifyPenalty': 'onClickModifyPenalty',
            'mouseover .showPopover': 'onShowPopover',
            'mouseout .showPopover': 'onClosePopover',
            'click .closeOrder': 'onClickCloseOrder'
        },
        actions: {
            chargesDetail: function(orderId) {
                return root + '/payment-manage/order/' + orderId + '/chargesDetail';
            },
            closeNormalOrder: root + '/payment-manage/order/close'
        },
        initialize: function(opts) {
            this.initModel(opts.orderId);
        },
        onShowPopover: function(e) {
            var $el = $(e.target);
            var self = this;
            this.timer = setTimeout(function() {
                var opt = {
                    url: self.actions.chargesDetail(self.model.get('orderId')),
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
                                        totalOverdue = repaymentChargesDetail.totalOverdueFee;

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
        },
        initModel: function(orderId) {
            var attr = {};
            attr.penalty = +this.$('.penalty-amount').data('value');
            attr.comment = this.$('.comment').text().trim();
            attr.orderId = orderId;
            attr.assetRecycleDate = this.$('.asset-recycle-date').text().trim();
            attr.customerName = this.$('.customer-name').text().trim();
            attr.numbersOfOverdueDays = this.$('.numbers-of-overdue-days').text().trim();
            this.model = new Backbone.Model(attr);
        },
        onClickModifyPenalty: function() {
            var view = new ModifyPenaltyView({
                model: this.model
            });

            this.model.once('modify:penaltyAmount', function(attr, resp) {
                if (resp.code == 0) {
                    popupTip.show('操作成功');

                    setTimeout(function() {
                        location.reload();
                    }, 1000);
                } else {
                    popupTip.show(resp.message || '操作失败，请重试');
                }
            });

            view.show();
        },
        onClickCloseOrder: function(e) {
            var self = this;
            popupTip.show('确认关闭该结算单？', null, [{
                text: '确定',
                style: 'success',
                handler: function() {
                    var opt = {
                        url: self.actions.closeNormalOrder,
                        dataType: 'json',
                        type: 'post',
                        data: {
                            'orderId': self.model.get('orderId'),
                            'type': 'normal'
                        }
                    };
                    opt.success = function(resp) {
                        if (resp.code == 0) {
                            location.reload();
                        } else {
                            popupTip.show(resp.message);
                        }
                    };
                    $.ajax(opt);
                    this.hide();
                }
            }, {
                text: '取消',
                style: 'default',
                handler: function() {
                    this.hide();
                }
            }]);
        }
    });


    module.exports = OrderDetailView;

});