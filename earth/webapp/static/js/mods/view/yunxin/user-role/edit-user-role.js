define(function(require, exports, module) {
    var popupTip = require('component/popupTip');
    var baseView = require('baseView/baseFormView');
    var Pagination = require('component/pagination');
    var DialogView = require('component/dialogView');
    var FormDialogView = baseView.FormDialogView;
    var BaseFormView = baseView.BaseFormView;

    var UserRoleModel = require('view/yunxin/user-role/user-role-entity').UserRoleModel;

    var root = global_config.root;

    var DialogPagination = Pagination.extend({
        queryAction: root + '/bind-financial-contract/query',
        template: _.template($('#template2').html()),
        refreshTableDataList: function(data, opepration, response, query) {
            if (!this.template) return;

            var htm;
            if (data.length < 1) {
                if (query.pageIndex != 1) {
                    this.queryPrevPage();
                } else {
                    htm = '<thead><tr><th class="hide">全选</th><th>信托产品代码</th><th>信托合同名称</th><th>信托商户名称</th><th>信托合同类型</th><th>状态</th><th width="60">操作</th></tr></thead>' + '<tbody><tr class="nomore"><td style="text-align: center;" colspan="10">没有更多数据</td></tr></tbody>';
                }
            } else {
                htm = this.template({
                    list: this.polish(data)
                });
            }
            if (htm === null) return;
            this.tableListEl.html(htm);
            this.trigger('refresh', data, opepration, response, query);
        }
    });

    var AddBindDialogView = FormDialogView.extend({
        template: _.template($('#businessBindTmpl').html()),
        className: 'modal fade form-modal large-form-modal',
        initialize: function() {
            AddBindDialogView.__super__.initialize.apply(this, arguments);
            this.idList = [];

            this.pagination = new DialogPagination({
                el: this.$('.bind-list')
            });
            this.listenTo(this.pagination, 'page:change', function() {
                this.idList = [];
            });
            this.listenTo(this.pagination, 'checkbox.item', function(isChecked, target) {
                var id = target.parents('tr').data('id');
                if (isChecked) {
                    this.idList.push(id);
                } else {
                    this.idList = _.without(this.idList, id);
                }
            });
            this.listenTo(this.pagination, 'checkbox.all', function(isChecked, target) {
                this.idList = [];
                if (isChecked) {
                    var checkboxs = this.$('.check-box');
                    for (var i = 1; i < checkboxs.length; i++) {
                        var id = checkboxs.eq(i).parents('tr').data('id');
                        this.idList.push(id);
                    }
                }
            });
            this.listenTo(this.model, 'model:bind', function(resp) {
                if (resp.code == 0) {
                    this.idList = [];
                    this.pagination.queryCurrentPage();
                    this.model.trigger('model:bind-result');
                } else {
                    popupTip.show(resp.message);
                }
            });
        },
        events: {
            'click .bind': 'onClickBind',
            'click .bind-all-item': 'onClickBindAllItem'
        },
        onClickBindAllItem: function() {
            if (this.idList.length > 0) {
                this.model.bind(this.idList);
            } else {
                popupTip.show('请先选择需要解除绑定项');
            }
        },
        onClickBind: function(e) {
            this.idList = [];
            var financialContractIds = $(e.target).parents('tr').data('id');

            this.idList.push(financialContractIds);

            this.model.bind(this.idList);
        }
    });

    var BodyPagination = Pagination.extend({
        refreshTableDataList: function(data, opepration, response, query) {
            if (!this.template) return;

            var htm;
            if (data.length < 1) {
                if (query.pageIndex != 1) {
                    this.queryPrevPage();
                } else {
                    htm = '<thead><tr><th class="hide">全选</th><th>信托产品代码</th><th>信托合同名称</th><th>信托商户名称</th><th>信托合同类型</th><th>状态</th><th width="60">操作</th></tr></thead>' + '<tbody><tr class="nomore"><td style="text-align: center;" colspan="10">没有更多数据</td></tr></tbody>';
                    $('.has-bind').hide();
                }
            } else {
                $('.has-bind').show();
                htm = this.template({
                    list: this.polish(data)
                });
            }
            if (htm === null) return;
            this.tableListEl.html(htm);
            this.trigger('refresh', data, opepration, response, query);
        }
    });

    var EditUserRoleView = BaseFormView.extend({
        el: '.content',
        events: {
            'click .add-bind': 'onClickAddBind',
            'click .drawer': 'onClickDrawer',
            'click .unbind': 'onClickUnbind',
            'click .unbind-all-item': 'onClickUnbindAll',
            'change [name = role]': 'onChangeRole'
        },
        initialize: function(userId) {
            EditUserRoleView.__super__.initialize.apply(this, arguments);
            this.userId = userId;
            this.idList = [];

            this.defineValidator();
            this.model = new UserRoleModel({
                userId: this.userId
            });

            this.pagination = new BodyPagination({
                el: '.has-bind'
            });

            this.listenTo(this.model, 'model:unbind', function(resp) {
                if (resp.code == 0) {
                    this.pagination.queryCurrentPage();
                } else {
                    popupTip.show(resp.message);
                }
            });
            this.listenTo(this.model, 'model:submit', function(resp) {
                var self = this;
                if (resp.code == 0) {

                    var dialog = new DialogView({
                        bodyInnerTxt: '编辑成功',
                        isShowCancelBtn: false
                    });
                    dialog.on('goahead', function() {
                        window.location.href = root + '/show-user-role/' + self.userId;
                    });
                    dialog.show();

                } else {
                    popupTip.show(resp.message);
                }
            });

            this.listenTo(this.pagination, 'checkbox.item', function(isChecked, target) {
                var id = target.parents('tr').data('id');
                if (isChecked) {
                    this.idList.push(id);
                } else {
                    this.idList = _.without(this.idList, id);
                }
            });

            this.listenTo(this.pagination, 'checkbox.all', function(isChecked, target) {
                this.idList = [];
                if (isChecked) {
                    var checkboxs = this.$('.check-box');
                    for (var i = 1; i < checkboxs.length; i++) {
                        var id = checkboxs.eq(i).parents('tr').data('id');
                        this.idList.push(id);
                    }
                }
            });
            this.listenTo(this.pagination, 'page:change', function() {
                this.idList = [];
            });
            this.listenTo(this.model, 'model:bind-result', function() {
                this.pagination.query();
            });
        },
        onClickUnbindAll: function() {
            if (this.idList.length > 0) {
                this.showUnbindDialog(this.idList);
            } else {
                popupTip.show('请先选择需要解除绑定项');
            }
        },
        onClickUnbind: function(e) {
            var financialContractIds = $(e.target).parents('tr').data('id');
            this.idList.splice(0);
            this.idList.push(financialContractIds);

            this.showUnbindDialog(this.idList);
        },
        showUnbindDialog: function(idList) {
            var self = this;
            var dialog = new DialogView({
                bodyInnerTxt: '确认解除绑定？'
            });
            dialog.on('goahead', function() {
                self.model.unbind(idList);
                this.hide();
            });
            dialog.show();
        },
        defineValidator: function() {
            this.validator = this.$('form').validate({
                rules: {
                    realname: 'required',
                    companyId: 'required',
                    role: 'required'
                }
            });
        },
        validate: function() {
            return this.validator.form();
        },
        onClickAddBind: function(e) {
            e.preventDefault();

            var view = new AddBindDialogView({
                model: this.model
            });
            view.show();
        },
        onClickDrawer: function(e) {
            e.preventDefault();
            var $tar = $(e.target);
            if ($tar.hasClass('msg')) {
                var $next = $tar.next();
                if ($next.hasClass('active')) {
                    $tar.text('展开');
                } else {
                    $tar.text('收起');
                }
                $next.toggleClass('active');
                $tar.closest('div').next().toggle();
            } else if ($tar.hasClass('icon-up-down')) {
                if ($tar.hasClass('active')) {
                    $tar.prev().text('展开');
                } else {
                    $tar.prev().text('收起');
                }
                $tar.toggleClass('active');
                $tar.closest('div').next().toggle();
            } else {
                var $icon = $tar.find('.icon-up-down').first();
                var isActive = $icon.hasClass('active');
                var $msg = $tar.find('.msg').first();

                if (isActive) {
                    $msg.text('展开');
                } else {
                    $msg.text('收起');
                }
                $icon.toggleClass('active');
                $tar.parent().next().toggle();
            }
        },
        submitHandler: function() {
            if (!this.validate()) return;
            this.model.submit(this.extractDomData());
        },
        onChangeRole: function(e) {
            var selected = $(e.target).find('option:selected').val();

            if (selected == 'ROLE_TRUST_OBSERVER') {
                $('.bind-view').removeClass('hide');
            } else {
                $('.bind-view').addClass('hide');
            }
        }
    });

    module.exports = EditUserRoleView;

});