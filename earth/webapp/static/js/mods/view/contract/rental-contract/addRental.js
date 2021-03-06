define(function(require, exports, module) {
    var validateUtil = require('scaffold/util').validateUtil;
    var DialogView = require('component/dialogView');
    var popupTip = require('component/popupTip');
    var RentalContractModel = require('entity/contract/rentalContractModel').RentalContractModel;
    var BaseFormView = require('baseView/baseFormView').BaseFormView;
    // attachment view
    var AttachmentView = require('./attachmentInfo');
    // renter view
    var LesseeView = require('./lesseeInfo');
    // payment view
    var RentView = require('./rentInfo');
    // contractDeadline view
    var ContractDeadlineView = require('./contractDeadline');

    // const
    var $ = jQuery;
    $.validator.addMethod('varyCertificates', function(value, element) {
        var type = $(element).prev('[name=certificateType]').val();
        if (type == 0) { // 身份证验证
            return this.optional(element) || validateUtil.isIDCardValid(value);
        } else {
            return true;
        }
    }, '请输入正确的证件号');

    var AddRentalView = BaseFormView.extend({
        el: '.rental-form-wrapper',
        initSubView: function() {
            var model = this.model;
            var self = this;

            this.contractDeadlineView = new ContractDeadlineView({
                el: this.$('.contract-deadline-info'),
                model: model.contractBasicInfo,
                sourceModel: model
            });
            this.contractDeadlineView.on('payperiod:clear', function() {
                self.rentView.clearWritedStage();
            });

            this.lesseeView = new LesseeView({
                el: this.$('.lessee-info'),
                model: model.renterInfo,
                sourceModel: model
            });
            this.rentView = new RentView({
                el: this.$('.rent-info'),
                model: model.paymentInfo,
                sourceModel: model
            });
            this.attachmentView = new AttachmentView({
                el: this.$('.attachment-info'),
                model: model.attachmentInfo,
                sourceModel: model
            });
        },
        initValidator: function() {
            this.validator = this.$('.downstream-leasee-form').validate({
                ignore: '.hide [name]',
                onsubmit: false, // 不绑定onsubmit事件
                rules: {
                    // provinceCode: 'required',
                    // cityCode: 'required',
                    // areaCode: 'required',
                    issueTime: 'required',
                    effectiveTime: 'required',
                    maturityTime: 'required',
                    certificateNo: {
                        varyCertificates: true
                    },
                    depositeAmount: {
                        money: true
                    },
                    mobile: {
                        mPhoneExt: true
                    }
                },
                success: function(error, element) {
                    var parent = $(element).parent();
                    if (parent.is('.imitate-datetimepicker-input')) {
                        if (!element.value) {
                            parent.addClass('error');
                        } else {
                            parent.removeClass('error');
                        }
                    }
                },
                errorPlacement: function(error, element) {
                    var parent = element.parent();
                    if (parent.is('.parcel-input')) {
                        error.insertAfter(parent);
                    } else if (parent.is('.imitate-datetimepicker-input')) {
                        // error.insertAfter(parent);
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        },
        initialize: function(leaseId) {
            AddRentalView.__super__.initialize.apply(this, arguments);

            this.model = new RentalContractModel();

            if (typeof leaseId == 'string') {
                this.model.set({ businessContractUuid: leaseId });
            }
            this.model.fillInternalModel(JSON.parse(this.$('#hiddenModelAttr').val()));

            this.initSubView();

            this.initValidator();

            this.succDialogView = new DialogView();
            this.listenTo(this.succDialogView, 'goahead', function(businessContractUuid) {
                location = '../rental-contract-view/' + businessContractUuid;
            }).listenTo(this.succDialogView, 'closedialog', function(businessContractUuid) {
                if (typeof businessContractUuid === 'undefined') {
                    location = '../rental-contract-list/index';
                } else {
                    location = '../rental-contract-view/' + businessContractUuid;
                }
            });
        },
        validate: function() {
            // return true;
            var basicValid = this.validator.form();
            var deadlineValid = this.contractDeadlineView.validate();
            var lesseeValid = this.lesseeView.validate();
            var rentValid = this.rentView.validate();
            var attachmentValid = this.attachmentView.validate();
            return deadlineValid && lesseeValid && rentValid && attachmentValid && basicValid;
        },
        submitHandler: function(e) {
            if(!this.validate()) {
                this.trigger('invalid');
                return;
            }

            this.contractDeadlineView.save();
            this.lesseeView.save();
            this.rentView.save();
            this.attachmentView.save();

            var succDialogView = this.succDialogView;
            this.model.saveAll({
                success: function(model, resp) {
                    if (resp.code == 0) {
                        succDialogView.show('成功录入租约!', resp.data.businessContractUuid);
                    } else {
                        popupTip.show(resp.message);
                    }
                }
            });
        }
    });

    exports.AddRentalView = AddRentalView;

});