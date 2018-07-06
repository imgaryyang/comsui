define(function(require, exports, module) {
    var popupTip = require('component/popupTip');
    var baseView = require('baseView/baseFormView');
    var DialogView = require('component/dialogView');
    var FormDialogView = baseView.FormDialogView;
    var BaseFormView = baseView.BaseFormView;
    var Pagination = require('component/pagination');

    var root = global_config.root;

    // var AddBindDialogView = FormDialogView.extend({
    //     el: $('#businessBindTmpl').get(0),
    //     initialize: function(attr) {
    //         AddBindDialogView.__super__.initialize.apply(this, arguments);

    //         this.idList = [];
    //         var self = this;
    //         this.hasAddCollection = attr.hasAddCollection;
    //         self.unAddCollection = new Backbone.Collection();
    //         var QPagination = Pagination.extend({
    //             refreshTableDataList: function(data, opepration, response, query) {
    //                 if (!this.template) return;

    //                 var htm;
    //                 if (data.length < 1) {
    //                     htm = '<thead><tr><th class="hide">全选</th><th>信托产品代码</th><th>信托合同名称</th><th>信托商户名称</th><th>信托合同类型</th><th>状态</th><th width="60">操作</th></tr></thead>' + '<tbody><tr class="nomore"><td style="text-align: center;" colspan="10">没有更多数据</td></tr></tbody>';
    //                 } else {
    //                     self.unAddCollection.reset(data);

    //                     htm = this.template({
    //                         list: self.unAddCollection.models
    //                     });
    //                 }
    //                 this.tableListEl.html(htm);
    //                 this.trigger('refresh', data, opepration, response, query);
    //             }
    //         });

    //         this.pagination = new QPagination({
    //             el: '.bind-list'
    //         });
    //     },
    //     actions: {
    //         bind: root + '/bind-financial-contract/bind',
    //         unbind: root + '/bind-financial-contract/unbind'
    //     },
    //     events: {
    //         'click .bind': 'onClickBind',
    //         'click .unbind': 'onClickUnbind'
    //     },
    //     onClickBind: function(e) {
    //         var selectedId = $(e.target).parents('tr').data('id');
    //         $(e.target).text('移除');
    //         $(e.target).removeClass('bind');
    //         $(e.target).addClass('unbind');

    //         var self = this;
    //         this.unAddCollection.each(function(model) {
    //             if (model.get('id') == selectedId) {
    //                 self.hasAddCollection.add(model);
    //             }
    //         });
    //     },
    //     onClickUnbind: function(e) {
    //         var self = this;
    //         var selectedId = $(e.target).parents('tr').data('id');

    //         $(e.target).text('添加');
    //         $(e.target).removeClass('unbind');
    //         $(e.target).addClass('bind');
    //         self.unAddCollection.each(function(model) {
    //             if (model.get('id') == selectedId) {
    //                 self.hasAddCollection.remove(model);
    //             }
    //         });
    //     }
    // });

    // var BindItemView = Backbone.View.extend({
    //     tagName: 'tr',
    //     template: _.template($('.table .bind-item-view').html(), {
    //         variable: 'item'
    //     }),
    //     render: function() {
    //         var data = this.model.toJSON();
    //         var htm = this.template(data);
    //         this.$el.html(htm);
    //         return this;
    //     }
    // });

    var CreateUserRoleView = BaseFormView.extend({
        el: '.content',
        events: {
            'click .add-bind': 'onClickAddBind',
            'click .drawer': 'onClickDrawer',
            'click .remove': 'onClickRemove'
        },
        initialize: function() {
            this.defineValidator();
            this.model = new Backbone.Model();
            // this.hasAddCollection = new Backbone.Collection();

            // this.listenTo(this.hasAddCollection, 'add', this.addOneItemView);
            // this.listenTo(this.hasAddCollection, 'reset', this.addAllItemView);
            // this.listenTo(this.hasAddCollection, 'remove', this.addAllItemView);

        },
        defineValidator: function() {
            $.validator.addMethod('rightUserName', function(value, el) {
                return this.optional(el) || (/^[a-zA-Z][a-zA-Z0-9_]{5,15}$/.test(value));
            }, '请输入英文字母开头、可选择英文字母数字以及下划线组合的用户名');

            this.validator = this.$('form').validate({
                rules: {
                    username: {
                        required: true,
                        minlength: 6,
                        maxlength: 16,
                        rightUserName: true,
                        remote: {
                            type: 'get',
                            url: root + 'create-user-role/username',
                            dataType: 'json',
                            dataFilter: function(data, type) {
                                var result = JSON.parse(data);
                                return result.code == 0 ? true : false;
                            }
                        }
                    },
                    realname: 'required',
                    companyId: 'required',
                    role: 'required'
                },
                messages: {
                    username: {
                        remote: '当前用户名已存在',
                        minlength: '用户名不得少于6个字符',
                        maxlength: '用户名最多16个字符'
                    }
                }
            });
        },
        validate: function() {
            return this.validator.form();
        },
        // addOneItemView: function(model) {
        //     this.$('.nomore').empty();
        //     var view = new BindItemView({
        //         model: model
        //     });
        //     this.$('.bd tbody').append(view.render().$el);
        // },
        // addAllItemView: function() {
        //     this.$('.bd tbody').empty();
        //     var list = this.hasAddCollection;
        //     for (var i = 0, len = list.models.length; i < len; i++) {
        //         var model = list.at(i);
        //         this.addOneItemView(model);
        //     }
        // },
        onClickRemove: function(e) {
            // var selectedId = $(e.target).data('id');
            // var self = this;
            // this.hasAddCollection.each(function(model) {
            //     if (selectedId == model.get('id')) {
            //         self.hasAddCollection.remove(model);
            //     }
            // });
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
        onClickAddBind: function(e) {
            // var view = new AddBindDialogView({
            //     hasAddCollection: this.hasAddCollection
            // });
            // view.show();
        },
        submitHandler: function() {
            if (!this.validate()) return;
            this.save();
        },
        extractDomData: function() {
            var attr = this._extractDomData(this.$('.real-value'));
            // var addIdList = [];
            // this.hasAddCollection.each(function(model) {
            //     addIdList.push(model.get('id') + '');
            // });
            // attr.addIds = JSON.stringify(addIdList);
            return attr;
        },
        save: function() {
            var self = this;
            var opt = {
                type: 'post',
                dataType: 'json',
                url: root + '/create-user-role',
                data: self.extractDomData()
            };

            opt.success = function(resp) {
                if (resp.code == 0) {
                    var dialog = new DialogView({
                        bodyInnerTxt: resp.message,
                        isShowCancelBtn: false
                    });
                    dialog.on('goahead', function() {
                        window.location.href = root + '/show-user-list';
                        this.hide();
                    });
                    dialog.show();

                } else {
                    popupTip.show(resp.message);
                }
            };
            $.ajax(opt);
        }
    });

    module.exports = CreateUserRoleView;

});