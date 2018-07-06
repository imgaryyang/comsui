define(function(require, exports, module) {
    var popupTip = require('component/popupTip');
    var DialogView = require('component/dialogView');
    var FormDialogView = require('baseView/baseFormView').FormDialogView;
    var $ = jQuery;
    var root = global_config.root;

    var AgainNotifyDialog = FormDialogView.extend({
        template: _.template($('#againNotifyTmpl').html()),
        initialize: function(options) {
            AgainNotifyDialog.__super__.initialize.apply(this, arguments);
            this.remittanceApplicationUuid = options.remittanceApplicationUuid;
        },
        action: {
            submit: function(remittanceApplicationUuid) {
                return root + '/remittance/application/details/updateplannotifynumber/' + remittanceApplicationUuid;
            }
        },
        save: function() {
            var confirmDialog = new DialogView({
                title: '操作成功',
                bodyInnerTxt: '计划回调次数已增加，稍后系统会自动回调结果',
                excludeGoahed: true
            });
            var opt = {
                type: 'post',
                url: this.action.submit(this.remittanceApplicationUuid),
                dataType: 'json',
                data: this.extractDomData()
            };
            opt.success = function(resp) {
                if (resp.code == 0) {
                    confirmDialog.on('closedialog', function() {
                        location.reload();
                    });
                    confirmDialog.show();
                } else {
                    popupTip.show(resp.message);
                }
            };
            $.ajax(opt);
        },
        submitHandler: function() {
            this.save();
            this.hide();
        }
    });

    var OrderDetatilView = Backbone.View.extend({
        el: '.content',
        events: {
            'mouseover .showPopover': 'showPopover',
            'mouseout .showPopover': 'showPopover',
            'click .sort': 'onClickSortBtn',
            'click .againNotifyResult': 'onClickAgainNotifyResult'
        },
        showPopover: function(e) {
            e.preventDefault();
            var el = $(e.target);
            var popoverContent = el.siblings('.account').html();
            el.popover({
                content: popoverContent
            });
            el.popover('toggle');
        },
        onClickAgainNotifyResult: function(e) {
            var remittanceApplicationUuid = $(e.target).data('remittanceapplicationuuid');
           
            var dialog = new AgainNotifyDialog({
                remittanceApplicationUuid: remittanceApplicationUuid
            });
            dialog.show();
        }
    });

    module.exports = OrderDetatilView;
});