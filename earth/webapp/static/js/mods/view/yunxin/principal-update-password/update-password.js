define(function(require, exports, module) {
    var popupTip = require('component/popupTip');

    var UpdatePasswordView = Backbone.View.extend({
        el: '.update-password-form',
        events: {
            'click #submit': 'onClickSubmit',
            'change #oldpass': 'onChangeOldPass',
            'change #newpass': 'onChangeNewPass',
            'change #newpassAgain': 'onChangeNewPassAgain'
        },
        initialize: function() {
            this.modifyPasswordTime = $('#modifyPasswordTime');
            this.showFirstLoginPopupTip();
            this.error = true;
        },
        showFirstLoginPopupTip: function() {
            if (this.modifyPasswordTime.val() == 0) {
                popupTip.show('首次登陆，请修改密码！', '', [{
                    text: '确定',
                    style: 'success',
                    handler: function() {
                        this.hide();
                    }
                }]);
            }
        },
        onChangeOldPass: function(e) {
            var oldpass = $('#oldpass').val();
            if (oldpass == '') {
                this.showError('oldpass', '密码不能为空');
                this.error = true;
            } else {
                this.error = false;
                $('#oldpass').css({
                    'border-color': 'green'
                });
                $('#oldpassTip').css({
                    'display': 'none'
                });
            }
        },
        onChangeNewPass: function(e) {
            var newpass = $('#newpass').val();
            if (newpass == '') {
                this.showError('newpass', '新密码不能为空');
                this.error = true;
            } else {
                this.error = false;

                $('#newpass').css({
                    'border-color': 'green'
                });
                $('#newpassTip').css({
                    'display': 'none'
                });
            }
        },
        onChangeNewPassAgain: function(e) {
            var newpass = $('#newpass').val();
            if (newpass == '') {
                this.showError('newpass', '新密码不能为空');
                this.error = true;
                return;
            }

            var newpassAgain = $('#newpassAgain').val();
            if (newpassAgain != newpass) {
                this.showError('newpassAgain', '与输入的新密码不一致');
                this.error = true;
            } else {
                this.error = false;
                $('#newpassAgain').css({
                    'border-color': 'green'
                });
                $('#newpassAgainTip').css({
                    'display': 'none'
                });
            }
        },
        onClickSubmit: function(e) {
            e.preventDefault();
            this.onChangeOldPass();
            this.onChangeNewPass();
            this.onChangeNewPassAgain();

            if (!this.error) {
                var username = $('#username').val();
                var newpass = $('#newpass').val();
                var oldpass = $('#oldpass').val();

                $.ajax({
                    url: global_config.root + '/update-password',
                    type: 'post',
                    data: {
                        userName: username,
                        newPassword: newpass,
                        oldPassword: oldpass
                    },
                    dataType: 'json',
                    success: function(resp) {
                        if (resp.code == 0) {
                            popupTip.show('修改成功，正在重新登录');
                            setTimeout(function() {
                                location.assign('/j_spring_security_logout');
                            }, 1000);
                        } else {
                            popupTip.show(resp.message);
                        }
                    }
                });
            }
        },
        showError: function(formSpan, errorText) {
            $('#' + formSpan).css({
                'border-color': 'red'
            });
            $('#' + formSpan + 'Tip').empty();
            $('#' + formSpan + 'Tip').append(errorText);
            $('#' + formSpan + 'Tip').css({
                'display': 'inline'
            });
        }
    });

    exports.UpdatePasswordView = UpdatePasswordView;
});