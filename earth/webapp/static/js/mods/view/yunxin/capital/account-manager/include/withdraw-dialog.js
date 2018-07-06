define(function(require, exports, module) {
    var popupTip = require('component/popupTip');
    var FormDialogView = require('baseView/baseFormView').FormDialogView;
    var $ = jQuery;
    var root = global_config.root;

    var WithdrawDialogView = FormDialogView.extend({
        template: _.template($('#WithdrawDialogTmpl').html()),
        actions: {
            withdraw: root + ''
        },
        initialize: function() {
            WithdrawDialogView.__super__.initialize.call(this, arguments);
            this.defineValidator();
        },
        defineValidator: function() {
            this.validator = this.$('.form').validate({
                rules: {}
            });
        },
        validate: function() {
            return true;
        },
        save: function() {
            var data = this.extractDomData();

            var opt = {
                dataType: 'json',
                method: 'get',
                url: this.actions.withdraw,
                data: data
            };
            opt.success = function(resp) {
                if (resp.code == 0) {
                    popupTip.show();
                } else {
                    popupTip.show(resp.message);
                }
            };

            $.ajax(opt);
        },
        submitHandler: function(e) {
            e.preventDefault();
            if (!this.validate()) return;
            this.save();
        }
    });
    module.exports = WithdrawDialogView;
});