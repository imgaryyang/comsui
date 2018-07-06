define(function(require, exports, module) {
    var util = require('scaffold/util');
    var popupTip = require('component/popupTip');
    var RModel = require('./repurchase-model').RepurchaseModel;
    var baseView = require('baseView/baseFormView');
    var FormDialogView = baseView.FormDialogView;

    var RView = Backbone.View.extend({
        el: '.content',
        events: {
            'click .activate': 'onClickActivate',
            'click .nullify': 'onClickNullify',
            'click .default': 'onClickDefault'
        },
        initialize: function(rduid) {
            this.model = new RModel({
                rduid: rduid
            });
            this.listenTo(this.model, 'nullify:success activate:success', this.handlerAjax);
        },
        handlerAjax: function(resp) {
            if (resp.code != 0) {
                popupTip.show(_.isEmpty(resp.message) ? resp : resp.message);
            } else {
                util.delayReload(0);
            }
        },
        onClickActivate: function() {
            this.model.activate();
        },
        onClickNullify: function() {
            this.model.set('type', 'nullify');
            var confirmDialog = new ConfirmDialog({
                model: this.model
            });
            confirmDialog.show();
        },
        onClickDefault: function(e) {
            e.preventDefault();
            this.model.set('type', 'default');
            var confirmDialog = new ConfirmDialog({
                model: this.model
            });
            confirmDialog.show();
        }
    });

    var ConfirmDialog = FormDialogView.extend({
        template: _.template($('#confirmDialog').html() || '', { variable: 'obj' }),
        initialize: function() {
            ConfirmDialog.__super__.initialize.apply(this, arguments);

            this.listenTo(this.model, 'model:default', function(resp) {
                if (resp.code == 0) {
                    this.close();
                    popupTip.show('操作成功');

                    setTimeout(function() {
                        location.reload();
                    }, 1000);
                } else {
                    popupTip.show(resp.message);
                }
            });
        },
        submitHandler: function() {
            if (this.model.type == 'default') {
                this.model.default();
            } else {
                this.model.nullify();
            }
        }
    });

    module.exports = RView;

});