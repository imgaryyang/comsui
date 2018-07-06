define(function(require, exports, module) {
    var PageControl = require('component/pageControl');
    var FormDialogView = require('baseView/baseFormView').FormDialogView;

    var loadingImg = global_const.loadingImg;
    var dialogTmplStr = [
        '<div class="modal-dialog">',
            '<div class="modal-content">',
                '<div class="modal-header">',
                    '<button type="button" class="close close-dialog" data-dismiss="modal"><span>×</span></button>',
                    '<h4 class="modal-title">导出预览</h4>',
                '</div>',
                '<div class="modal-body">',
                    '<div class="horizontal-scroll-bar">',
                        '<table class="data-list" style="min-height: 85px;">',
                        '</table>',
                    '</div>',
                '</div>',
                '<div class="modal-footer">',
                    '<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>',
                    '<button type="button" class="btn btn-success download">下载</button>',
                '</div>',
            '</div>',
        '</div>'
    ].join("");

    var ExportPreviewDialogView = FormDialogView.extend({
        className: 'modal fade dialog-export-preview',
        events: {
            'click .redirect,.prev,.next,.first-page,.last-page': 'onClickPageNavigateBtn',
            'click .download': 'onDownload'
        },
        template: _.template(dialogTmplStr),
        initialize: function(options) {
            $.extend(this, options);

            this.$el.html(this.template());

            this.tableListEl = this.$('.data-list');
            this.rowNum = this.tableListEl.find('thead th').length;

            this.pageControl = new PageControl({
                el: this.$('.page-control').get(0),
                url: this.queryAction
            });

            this.listenTo(this.pageControl, 'next:pagecontrol prev:pagecontrol redirect:pagecontrol', this.refreshTableDataList);
            this.listenToOnce(this.pageControl, 'request', this.onRequest);

            this.pageControl.redirect(1);
        },
        polish: function(list) {
            return list;
        },
        refreshTableDataList: function(data, opepration, response, query) {
            if (!this.itemTemplate) return;
            var htm;
            if (data.length < 1) {
                htm = '<tr class="nomore"><td style="text-align: center; padding: 15px 0;" colspan="' + this.rowNum + '">没有更多数据</td></tr>';
            } else {
                htm = this.itemTemplate({
                    list: this.polish(data)
                });
            }
            this.tableListEl.html(htm);
        },
        onRequest: function() {
            var htm = '<tr><td style="text-align: center; padding: 15px 0;" colspan="' + this.rowNum + '"></td></tr>';
            var el = $(htm).find('td').html(loadingImg.clone());
            this.tableListEl.html(el);
        },
        onClickPageNavigateBtn: function(e) {
            e.preventDefault();

            var tar = $(e.target);

            if (tar.hasClass('prev')) {
                this.pageControl.prev();
            } else if (tar.hasClass('next')) {
                this.pageControl.next();
            } else if (tar.hasClass('redirect')) {
                this.pageControl.importPageIndexRedirect();
            } else if (tar.hasClass('first-page')) {
                this.pageControl.first();
            } else if (tar.hasClass('last-page')) {
                this.pageControl.last();
            }
        },
        onDownload: function(e) {
            window.open(this.downloadAction, '_download');
            this.hide();
        }
    });

    module.exports = ExportPreviewDialogView;

});
