define(function(require, exports, module) {
    var popupTip = require('component/popupTip');
    var DialogView = require('component/dialogView');
    var FormDialogView = require('baseView/baseFormView').FormDialogView;
    var $ = jQuery;
    var root = global_config.root;

    var ModifyNoteDialog = FormDialogView.extend({
        template: _.template($('#modifyNoteTmpl').html()),
        className: 'modal fade form-modal small-form-modal',
        action: {
            save: root + ''
        },
        initialize: function() {
            ModifyNoteDialog.__super__.initialize.apply(this, arguments);
            this.defineValidator();

        },
        defineValidator: function() {
            this.validator = this.$('form').validate({
                rules: {
                    note: {
                        required: true,
                        maxlength: 50
                    }
                },
                messages: {
                    note: {
                        maxlength: '备注信息不超过50字'
                    }
                }
            });
        },
        validate: function() {
            this.validator.form();
        },
        save: function() {
            var opt = {
                url: this.action.save,
                dataType: 'json',
                data: ''
            };
            opt.success = function(resp) {
                if (resp.code === 1) {

                } else {
                    popupTip.show(resp.message);
                }
            };
            $.ajax(opt);
        },
        submitHandler: function() {
            if (!this.validate()) return;
            // this.save();
        }
    });

    var ResendRemittancePlanDialog = FormDialogView.extend({
        template: _.template($('#resendRemittancePlanTmpl').html()),
        className: 'modal fade form-modal small-form-modal',
        initialize: function() {
            ResendRemittancePlanDialog.__super__.initialize.apply(this, arguments);
            this.defineValidator();
        },
        defineValidator: function() {
            this.validator = this.$('form').validate({
                rules: {
                    comment: {
                        required: true,
                        maxlength: 50
                    }
                },
                messages: {
                    comment: {
                        maxlength: '备注信息不超过50字'
                    }
                }
            });
        },
        validate: function() {
            return this.validator.form();
        },
        submitHandler: function() {
            if (!this.validate()) return;
            this.model.set(this.extractDomData());
            this.model.resendRemittancePlan();
            this.hide();
        }
    });

    var ChannelLoanDialog = FormDialogView.extend({
        template: _.template($('#channelLoanTmpl').html()),
        action: {
            save: root + ''
        },
        initialize: function() {
            ChannelLoanDialog.__super__.initialize.apply(this, arguments);
            this.defineValidator();
        },
        defineValidator: function() {
            this.validator = this.$('form').validate({
                rules: {
                    channelSelect: 'required'
                }
            });
        },
        validate: function() {
            this.validator.form();
        },
        save: function() {
            var opt = {
                url: this.action.save,
                dataType: 'json',
                data: ''
            };
            opt.success = function(resp) {
                if (resp.code == 1) {

                } else {
                    popupTip.show(resp.message);
                }
            };
            $.ajax(opt);
        },
        submitHandler: function() {
            if (!this.validate()) return;
            // this.save();
        }
    });

    var LModel = Backbone.Model.extend({
        actions: {
            resendRemittancePlan: function(remittancePlanUuid) {
                return root + '/remittance/plan/resend?remittancePlanUuid=' + remittancePlanUuid;
            }
        },
        resendRemittancePlan: function() {
            var remittancePlanUuid = this.get('remittancePlanUuid');
            var plannedPaymentDate = this.get('plannedPaymentDate');

            if (plannedPaymentDate && new Date(plannedPaymentDate) <= new Date('2016-10-10')) {
                this.trigger('model:resend', {
                    code: -1,
                    message: '该贷款合同已生效，禁止再放款。请重新核对信息。'
                });
            } else {
                var opt = {
                    url: this.actions.resendRemittancePlan(remittancePlanUuid),
                    dataType: 'json',
                    data: {
                        comment: this.get('comment')
                    }
                };

                opt.success = function(resp) {
                    this.trigger('model:resend', resp);
                }.bind(this);

                $.ajax(opt);
            }
        }
    });

    var LoanDetailView = Backbone.View.extend({
        el: '.content',
        events: {
            'click #modifyNote': 'onClickModifyNote',
            'click #channelLoan': 'onClickChannelLoan',
            'click #resendRemittancePlan': 'onClickResendRemittancePlan',
            'mouseover .showPopover': 'showPopover',
            'mouseout .showPopover': 'showPopover'
        },
        initialize: function() {
            this.initModel();
        },
        initModel: function() {
            var attr = {};
            attr.remittancePlanUuid = this.$('[name=remittancePlanUuid]').val().trim();
            attr.plannedPaymentDate = this.$('#plannedPaymentDate').text().trim();
            this.model = new LModel(attr);

            var successDialog = new DialogView({
                excludeGoahed: true
            });

            successDialog.on('closedialog', function() {
                location.reload();
            });

            this.listenTo(this.model, 'model:resend', function(resp) {
                if (resp.code == 0) {
                    successDialog.show('放款单，重新执行成功。');
                } else {
                    popupTip.show(resp.message);
                }
            });
        },
        onClickResendRemittancePlan: function(e) {
            e.preventDefault();
            // this.model.resendRemittancePlan();

            var resendRemittancePlanDialog = new ResendRemittancePlanDialog({
                model: this.model
            });
            resendRemittancePlanDialog.show();
        },
        onClickModifyNote: function(e) {
            e.preventDefault();
            var modifyNoteDialog = new ModifyNoteDialog();
            modifyNoteDialog.show();
        },
        onClickChannelLoan: function(e) {
            e.preventDefault();
            var channelLoanDialog = new ChannelLoanDialog();
            channelLoanDialog.show();
        },
        showPopover: function(e) {
            var el = $(e.target);
            var popoverContent = el.siblings('.account').html();
            el.popover({
                content: popoverContent
            });
            el.popover('toggle');
        }
    });

    module.exports = LoanDetailView;
});