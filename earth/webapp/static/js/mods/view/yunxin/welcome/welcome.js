define(function(require, exports, module) {
    var pathHelper = require('scaffold/path');
    var dashboard = require('component/dashboard');
    var SStorage = sessionStorage;
    var loadingImg = global_const.loadingImg;
    var root = global_config.root;
    var WelcomeView = Backbone.View.extend({
        el:'.welcome',
        events:{
            'click .goback' : 'onClickBack'
        },
        initialize: function(opt) {
            var opt ={
                url:root+'/notice/notice-released-list',
                type:'get',
                dataType:'JSON'
            };

            var self = this;
            opt.success = function(resp) {
                if(resp.code == 0){
                	var list = resp.data.list;
                    if(list.length > 0){
                        var ul='';
                        for( var i=0; i<list.length; i++) {
                            if(i<3) {
                                ul += '<li><a herf="#" data-dashboard-id="notice" data-mtitle="'+unescape(list[i].title)+'" '+'" data-releaseTime="'+list[i].releaseTime+'">'+ unescape(list[i].title)+'</a>'+'<span class="content hide">'+list[i].content+'</span>'+'</li>';
                            }
                        }
                        ul = ul + '<li><a herf="#" data-dashboard-id="notice" data-mtitle="'+unescape(list[0].title)+'" data-releaseTime="'+list[0].releaseTime+'">'+ unescape(list[0].title) +'</a>'+'<span class="content hide">'+list[0].content+'</span>'+'</li>';
                        $('.notice-block').find('ul').html(ul);
                        self.$('.welcome-notice').removeClass('hide');
                        self.circle();
                    } 
                }
            };

            $.ajax(opt);
            this.index = 0;
            var self = this;
            var $btnToggleDashboard = $('.web-g-hd .btn-toggle-dashboard');  
            $btnToggleDashboard.on('click', function(e) {
                view = $(e.target).parent().attr('class');
                self.toggle(view);
            });
            $(document).on('mouseover','[data-dashboard-id="notice"]', function(e) {
                window.clearInterval(self.time);
            });
            $(document).on('mouseleave','[data-dashboard-id="notice"]', function(e) {
                self.circle();
            });
            $(document).on('click','[data-dashboard-id="notice"]', function(e) {
                var target = $(e.target);
                var data = target.data();
                data.content = target.parents('li').find('.content').html();
                dashboard.showView('noticeDetail', data);
            });
        },
        circle: function() {
            var ul = $(".notice-block>ul");
            var self = this;
            if(ul.find('li').length >2){
            	self.time = setInterval(function() {
                    self.index ++ ;
                    if (self.index == ul.find('li').length) {
                        self.index = 1;
                        ul.css({top:'0px'});
                    }
                        ul.animate({top:-(self.index)*20+'px'});
                }, 2500);
            }
        },
        toggle: function (view) {
            if(view == 'item-todo'){
                dashboard.showView('toDo');
            } else {
                dashboard.showView('notice');
            }
        }

    });
    exports.WelcomeView = WelcomeView;

});
