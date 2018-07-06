define(function(require, exports, module) {
    var TableContentView = require('baseView/tableContent');
    var DialogView = require('component/dialogView');
    var popupTip = require('component/popupTip');
    var UserRoleListView = TableContentView.extend({
        events: {
            'click .closeUserBtn': 'onClickCloseUser'
        },
        onClickCloseUser: function(e) {
            e.preventDefault();
            var self = this;
            var id = $(e.target).data('id');
            var dialog = new DialogView({
                bodyInnerTxt: '确认关闭此用户？'
            });
            dialog.on('goahead', function() {
                self.deleteUser(id);
            });
            dialog.show();
        },
        deleteUser: function(id) {
            var opt = {
                dataType: 'json',
                url: global_config.root + '/delete-user-role/' + id
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    location.reload();
                } else {
                    popupTip.show(resp.message);
                }
            };
            $.ajax(opt);
        }
    });

    module.exports = UserRoleListView;

});