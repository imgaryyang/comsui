define(function(require, exports, module) {
    var popupTip = require('component/popupTip');
    var TableContentView = require('baseView/tableContent');
    var FormDialogView = require('baseView/baseFormView').FormDialogView;
    var $ = jQuery;
    var root = global_config.root;
    var helpTip = window.helpTip;

    var AddNoticeView = FormDialogView.extend({
        className: 'modal fade form-modal',
        template: _.template($('#addNoticeTmpl').html(), {
            variable: 'obj'
        }),
        initialize: function() {
            AddNoticeView.__super__.initialize.apply(this, arguments);
            this.defineValidator();
        },
        defineValidator: function() {
            this.validator = this.$('.form').validate({
                rules: {
                    title:'required',
                    content: 'required'
                }
            });
        },
        validate: function() {
            return this.validator.form();
        },
        extractDomData: function() {
        	var attr = this._extractDomData(this.$('.real-value'));
        	attr.title = _.escape(attr.title);
        	attr.content = _.escape(attr.content);
        	return attr;
        },
        save: function() {
            var data = this.extractDomData();
            var self = this;
            var opt = {
                url: root + '/notice/notice-add',
                data: data,
                type:'post',
                dataType: 'json'
            };

            opt.success = function(resp) {
                if(resp.code == 0){
                    self.hide();
                    self.trigger('add');
                }else{
                    popupTip.show(resp.message);
                }
            };

            $.ajax(opt);
        },
        submitHandler: function() {
            if(!this.validate()) return;
            this.save();
        }
    });
    var EditNoticeView = FormDialogView.extend({
       //el: $('#editNoticeTmpl').get(0),
        className: 'modal fade form-modal',
        template: _.template($('#editNoticeTmpl').html(), {
            variable: 'obj'
        }),
        events: {
            'click .menu-item' : 'onClickMenu',
            'click .save' : 'onClickSave',
            'click .submit' : 'onClickSubmit',
        },
        initialize: function() {
            EditNoticeView.__super__.initialize.apply(this, arguments);

            this.defineValidator();

            var self = this;

            var QPagination = Pagination.extend({
                refreshTableDataList: function(data, opepration, response, query) {
                    if (!this.template) return;

                    var htm;
                    if (data.length < 1) {
                        htm = '<tr class="nomore"><td style="text-align: center;" colspan="' + this.rowNum + '">没有更多数据</td></tr>';
                    } else {
                        var template = _.template($('#operateTable').html());
                        var htm = template({
                            list: data
                        });
                        
                   }
                    this.tableListEl.find('tbody').html(htm);
                    this.trigger('refresh', data, opepration, response, query);
                },
            });

            this.pagination = new QPagination({
                el: this.$('.sys-log'),
                pageRecordNum: 5
            });  
            
            //var helpTip =new helpTip();

            $(document).on('mouseover', '.log-recordContent', function() {
                var tar = $(this);
                helpTip.setOptions({
                    el: tar,
                    txt: $(this).html()
                });
                helpTip.show();
            }).on('mouseleave', '.log-recordContent', function() {
                helpTip.hide();
            });

            //this.pagination = new QPagination({el: this.$('.sys-log')});
        },
        defineValidator: function() {
            this.validator = this.$('.form').validate({
                rules: {
                    title:'required',
                    content: 'required'
                }
            });
        },
        validate: function() {
            return this.validator.form();
        },
        onClickMenu: function(e) {
            e.preventDefault();
            var target = $(e.target);
            var item = this.$('.menu-item');
            var form = this.$('.form');
            if(target.hasClass('active')){
                return;
            } else {
                item.removeClass('active');
                form.removeClass('hide');
                target.addClass('active');
                if(target.hasClass('notice')){
                    this.$('.notice').show();
                    this.$('.sys-log').addClass('hide');
                } else if(target.hasClass('operation')){
                    this.$('.sys-log').show();
                    this.$('.form.adapt.notice').addClass('hide');
                }
            }
        },
        extractDomData: function() {
        	var attr = this._extractDomData(this.$('.real-value'));
        	attr.title = _.escape(attr.title);
        	attr.content = _.escape(attr.content);
        	return attr;
        },
        save: function() {
            var data = this.extractDomData();
            data.noticeUuid = this.model.get('uuid');
            return data;
        },
        onClickSave: function() {
            if(!this.validate()) return;
            var self = this;
            var opt = {
                url: root + '/notice/notice-edit',
                data: self.save(),
                type:'post',
                dataType: 'json'
            };

            opt.success = function(resp) {
                if(resp.code == 0){
                    self.hide();
                    self.trigger('add');
                }else {
                    popupTip.show(resp.message);
                }
            };

            $.ajax(opt);
        },
        onClickSubmit: function() {
            var self = this;
            var opt = {
                url: root + '/notice/notice-release',
                data: self.save(),
                type:'post',
                dataType: 'json'
            };

            opt.success = function(resp) {
                if(resp.code == 0){
                   self.hide();
                   self.trigger('add');
                }else {
                    popupTip.show(resp.message);
                }
            };

            $.ajax(opt);
        }
    });
    var ObsoleteEditView = FormDialogView.extend({
        template: _.template($('#obsoleteTmpl').html()),
        submitHandler: function() {
            var remarks = this.$('[name=remarks]').val();
            this.trigger('obsolete',remarks);
        }
    });
    var NoticeListView = TableContentView.extend({
        events: {
            'click .btn-primary': 'onClickAddNotice',
            'click .edit,.check': 'onClickEdit',
            'click .obsolete' : 'onClickObsolete'
        },
        initialize: function() {
            NoticeListView.__super__.initialize.apply(this, arguments);
        },

        onClickAddNotice: function(e) {
            var self = this;
            var addNoticeView = new AddNoticeView();
            addNoticeView.show();
            addNoticeView.on('add',function() {
                self.refresh();
            });
        },
        onClickEdit: function(e) {
            e.preventDefault();
            var self = this;
            var target = $(e.target);
            var data ={};
            var $tr = target.parents('tr');
            data.title = _.escape($tr.find('.title').html());
            data.content = _.escape($tr.find('.notice-content').html());
            data.noticestatus = $tr.data('noticestatus');
            data.uuid = $tr.data('uuid');
            var model = new Backbone.Model(data);
            var editNoticeView = new EditNoticeView({
                model:model
              });
            editNoticeView.show();
            editNoticeView.on('add',function() {
                self.refresh();
            });
        },
        onClickObsolete: function(e) {
            e.preventDefault();
            target = $(e.target);
            var obsoleteEditView = new ObsoleteEditView();
            obsoleteEditView.show();        
            var self = this;
            obsoleteEditView.on('obsolete',function(remarks) {
                var data = {};
                var noticeUuid = target.parents('tr').data('uuid');
                data.noticeUuid = noticeUuid;
                data.remarks = remarks;
                var opt = {
                    url: root + '/notice/notice-invalid',
                    data:data,
                    dataType:'json',
                    type:'post'
                };

                opt.success = function(resp) {
                    if(resp.code ==0 ){
                        obsoleteEditView.hide();
                        self.refresh();
                    }   
                };
                $.ajax(opt);  
            })    

        }
    });
    exports.NoticeListView = NoticeListView;
});