define(function(require,exports,module){var e=require("baseView/tableContent"),t=require("baseView/baseFormView").FormDialogView,a=require("component/popupTip"),i=(require("component/fileUpload"),jQuery),o=global_config.root,n=t.extend({template:_.template(i("#importAssertTmpl").html(),{variable:"obj"}),actions:{create:o+"/assets-package/excel-create-assetData"},events:{"change [name=file]":"onChangeFile"},initialize:function(e){n.__super__.initialize.apply(this,arguments),this.defineValidator()},resetFormElement:function(e){e.wrap("<form>").parent("form").get(0).reset(),e.unwrap()},onChangeFile:function(e){/\.xlsx?$/.test(e.target.value)||(a.show("请上传Excel文件"),this.resetFormElement(i(e.target)))},defineValidator:function(){this.validator=this.$(".form").validate({rules:{financialContractNo:"required",file:"required"}})},validate:function(){return this.validator.form()},save:function(){var e=this,t={url:this.actions.create,type:"post",dataType:"json"};t.success=function(t){a.show(t.message),e.model.trigger("create",t)},t.error=function(e){a.show("网络错误！稍后再试！")},this.$(".form").ajaxSubmit(t)},submitHandler:function(e){this.validate()&&(this.save(),this.hide())}}),s=e.extend({events:{"click .delete":"onClickDelete","click .activate":"onClickActivate","click #importAssert":"onClickImportAssert"},initialize:function(){s.__super__.initialize.apply(this,arguments)},actions:{delete:o+"/loan-batch/delete-loan-batch",activate:o+"/loan-batch/activate"},getDomOptions:function(e){var t=i(e).parents("tr").data();return{loanBatchId:t.id,code:t.code}},delayReload:function(e){setTimeout(function(){location.reload()},e||1e3)},onClickDelete:function(e){e.preventDefault();var t=this,o=this.getDomOptions(e.target),n=confirm("是否确认删除！"+o.loanBatchId);if(1==n){var s={type:"POST",url:this.actions.delete,data:o,dataType:"json"};s.success=function(e){0==e.code?(a.show(e.message),t.delayReload()):a.show(e.message)},s.error=function(){a.show("网络错误！稍后再试！")},i.ajax(s)}},onClickActivate:function(e){e.preventDefault();var t=this,o=this.getDomOptions(e.target),n=confirm("是否确认激活！");if(1==n){var s={type:"POST",url:this.actions.activate,data:o,dataType:"json"};s.success=function(e){0==e.code?(a.show(e.message),t.refresh()):a.show(e.message)},s.error=function(){a.show("网络错误！稍后再试！")},i.ajax(s)}},onClickImportAssert:function(e){var t=this,a=new Backbone.Model,i=new n({model:a});a.once("create",function(e){0==e.code&&t.delayReload()}),i.show()}});module.exports=s});