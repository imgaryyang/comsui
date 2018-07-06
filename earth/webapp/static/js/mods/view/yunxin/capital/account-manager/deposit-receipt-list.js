define(function(require, exports, module) {
    var popupTip = require('component/popupTip');
    var TableContentView = require('baseView/tableContent');
    var $ = jQuery;
    var root = global_config.root;

    var DModel = Backbone.Model.extend({
        echarge: function(remark) {
            var self = this;

            var data = {
                remark: remark,
                sourceDocumentUuid: this.get('sourceDocumentUuid')
            };

            var opt = {
                dataType: 'json',
                url: root + '/capital/customer-account-manage/deposit-receipt-list/recharge-cancel',
                data: data
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    self.set({
                        revocationSourceDocumentNo: data.sourceDocumentNo
                    });
                }
                self.trigger('echarge:success', resp);
            };

            opt.complete = function() {
                self.trigger('echarge:complete');
            };

            this.trigger('echarge:start');

            $.ajax(opt);
        }
    });

    var DepositReceoptListView = TableContentView.extend({
        events: {
            'click .echarge-cancel': 'onClickCancelEcharge'
        },
        packParams: function($target) {
            var data = {};

            data.sourceDocumentUuid = $target.data('uuid');
            data.rechargeSourceDocumentNo = $target.data('sourcedocumentno');
            data.bookingAmount = $target.data('bookingamount');
            data.virtualAccountName = $target.data('virtualaccountname');
            data.virtualAccountNo = $target.data('virtualaccountno');

            data.revocationSourceDocumentNo = '';

            return data;
        },
        onClickCancelEcharge: function(e) {
            e.preventDefault();
            var $target = $(e.target);
            if ($target.is('.disabled')) return;

            var self = this;
            var attr = this.packParams($target.parents('tr'));
            var model = new DModel(attr);

            model.on('echarge:start', function() {
                $target.addClass('disabled');
            });

            model.on('echarge:success', function(resp) {
                if (resp.code == 0) {
                    popupTip.show('<div class="cancel-success" ><p class="tip" style="margin-top:10px;">撤销单<span class="important">' + resp.data.sourceDocumentNo + '</span>已创建</p><p class="tip">请前往查询撤销结果状态</p></div>');
                    self.query();
                } else {
                    if (resp.message === '余额不足') {
                        popupTip.show('<div class="cancel-failure"><p class="tip" style="margin-top:10px;"><span class="important">余额不足</span></p><p class="tip">请先将余额支付退款至余额</p></div>', null, [{
                            text: '确定',
                            style: 'submit',
                            handler: function() {
                                location.href = root + '/capital/customer-account-manage/payment-order-list/show#status=-1&pageIndex=1&key=' + model.get('virtualAccountNo');
                            }
                        }]);
                    } else {
                        popupTip.show(resp.message);
                    }
                }
            });

            model.on('echarge:complete', function() {
                $target.removeClass('disabled');
            });

            var cancelDialogTmpl = _.template($('#EchargeCancelDialogTmpl').html());
            popupTip.show(cancelDialogTmpl(model.toJSON()), '作废', [{
                text: '关闭',
                style: 'default',
                handler: function() {
                    this.hide();
                }
            }, {
                text: '确定',
                style: 'success',
                handler: function() {
                    var remark = $('#remark').val();
                    model.echarge(remark);
                }
            }]);
        }
    });

    module.exports = DepositReceoptListView;
});