define(function(require,exports,module){var t=require("component/pageControl"),e=require("baseView/baseFormView").FormDialogView,a=global_const.loadingImg,s=['<div class="modal-dialog">','<div class="modal-content">','<div class="modal-header">','<button type="button" class="close close-dialog" data-dismiss="modal"><span>×</span></button>','<h4 class="modal-title">导出预览</h4>',"</div>",'<div class="modal-body">','<div class="horizontal-scroll-bar">','<table class="data-list" style="min-height: 85px;">',"</table>","</div>","</div>",'<div class="modal-footer">','<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>','<button type="button" class="btn btn-success download">下载</button>',"</div>","</div>","</div>"].join(""),i=e.extend({className:"modal fade dialog-export-preview",events:{"click .redirect,.prev,.next,.first-page,.last-page":"onClickPageNavigateBtn","click .download":"onDownload"},template:_.template(s),initialize:function(e){$.extend(this,e),this.$el.html(this.template()),this.tableListEl=this.$(".data-list"),this.rowNum=this.tableListEl.find("thead th").length,this.pageControl=new t({el:this.$(".page-control").get(0),url:this.queryAction}),this.listenTo(this.pageControl,"next:pagecontrol prev:pagecontrol redirect:pagecontrol",this.refreshTableDataList),this.listenToOnce(this.pageControl,"request",this.onRequest),this.pageControl.redirect(1)},polish:function(t){return t},refreshTableDataList:function(t,e,a,s){if(this.itemTemplate){var i;i=t.length<1?'<tr class="nomore"><td style="text-align: center; padding: 15px 0;" colspan="'+this.rowNum+'">没有更多数据</td></tr>':this.itemTemplate({list:this.polish(t)}),this.tableListEl.html(i)}},onRequest:function(){var t='<tr><td style="text-align: center; padding: 15px 0;" colspan="'+this.rowNum+'"></td></tr>',e=$(t).find("td").html(a.clone());this.tableListEl.html(e)},onClickPageNavigateBtn:function(t){t.preventDefault();var e=$(t.target);e.hasClass("prev")?this.pageControl.prev():e.hasClass("next")?this.pageControl.next():e.hasClass("redirect")?this.pageControl.importPageIndexRedirect():e.hasClass("first-page")?this.pageControl.first():e.hasClass("last-page")&&this.pageControl.last()},onDownload:function(t){window.open(this.downloadAction,"_download"),this.hide()}});module.exports=i});