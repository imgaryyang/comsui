define(function(require, exports) {
    var $ = jQuery;
    var root = global_config.root;
    var AccountInfoModel = Backbone.Model.extend({
        defaults: {
            trustsAccountName: '',
            trustsAccountNo: '',
            trustsBankCode: ''
        },
        initialize: function(attr, opts) {
            this.completeModel = opts.completeModel;
        }
    });
    var BaseInfoModel = Backbone.Model.extend({
        defaults: {
            financialContractNo: '',
            appId: '',
            financialContractName: '',
            advaStartDate: '',
            thruDate: '',
            financialContractType: '',
            capitalAccount: []
        },
        action: {
            submit: root + '/financialContract/edit-financialContractBasicInfo/' + this.financialContractId
        },
        validate: function(attrs) {
            if (attrs.capitalAccount.length < 1) {
                return new Error();
            }
            return null;
        },
        submit: function() {
            var opt = {
                type: 'post',
                url: this.action.save,
                dataType: 'json',
                data: this.toJSON()
            };
            opt.success = function(resp) {
                this.tirgger('baseinfomodel: submit', resp);
            };
            $.ajax(opt);
        },
        setAttr: function(attrs) {
            this.set(attrs);
        }

    });
    exports.AccountInfoModel = AccountInfoModel;
    exports.BaseInfoModel = BaseInfoModel;
});