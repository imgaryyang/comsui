define(function(require,exports,module){var e=require("scaffold/util").validateUtil,i=require("component/dialogView"),t=require("component/popupTip"),a=require("entity/contract/leasingContractModel").LeasingContractModel,n=require("baseView/baseFormView").BaseFormView,s=require("./attachmentInfo"),o=require("./lesseeInfo"),r=require("./rentInfo"),l=require("./contractDeadline"),c=jQuery;c.validator.addMethod("varyCertificates",function(i,t){var a=c(t).prev("[name=certificateType]").val();return 0!=a||(this.optional(t)||e.isIDCardValid(i))},"请输入正确的证件号");var d=n.extend({el:".lease-form-wrapper",initSubView:function(){var e=this.model,i=this;this.contractDeadlineView=new l({el:this.$(".contract-deadline-info"),model:e.contractBasicInfo,sourceModel:e}),this.contractDeadlineView.on("payperiod:clear",function(){i.rentView.clearWritedStage()}),this.lesseeView=new o({el:this.$(".lessee-info"),model:e.renterInfo,sourceModel:e}),this.rentView=new r({el:this.$(".rent-info"),model:e.paymentInfo,sourceModel:e}),this.attachmentView=new s({el:this.$(".attachment-info"),model:e.attachmentInfo,sourceModel:e})},initValidator:function(){this.validator=this.$(".downstream-leasee-form").validate({ignore:".hide [name]",onsubmit:!1,rules:{issueTime:"required",effectiveTime:"required",maturityTime:"required",certificateNo:{varyCertificates:!0},depositeAmount:{money:!0},mobile:{mPhoneExt:!0}},success:function(e,i){var t=c(i).parent();t.is(".imitate-datetimepicker-input")&&(i.value?t.removeClass("error"):t.addClass("error"))},errorPlacement:function(e,i){var t=i.parent();t.is(".parcel-input")?e.insertAfter(t):t.is(".imitate-datetimepicker-input")||e.insertAfter(i)}})},initialize:function(e){d.__super__.initialize.apply(this,arguments),this.model=new a,"string"==typeof e&&this.model.set({businessContractUuid:e}),this.model.fillInternalModel(JSON.parse(this.$("#hiddenModelAttr").val())),this.initSubView(),this.initValidator(),this.succDialogView=new i,this.listenTo(this.succDialogView,"goahead",function(e){location="../../billing-plan/leasing-bill-list/index?businessContractUuid="+e}).listenTo(this.succDialogView,"closedialog",function(e){"undefined"==typeof e?location="../leasing-contract-list/index":location="../leasing-contract-view/"+e})},validate:function(){var e=this.validator.form(),i=this.contractDeadlineView.validate(),t=this.lesseeView.validate(),a=this.rentView.validate(),n=this.attachmentView.validate();return i&&t&&a&&n&&e},submitHandler:function(e){if(!this.validate())return void this.trigger("invalid");this.contractDeadlineView.save(),this.lesseeView.save(),this.rentView.save(),this.attachmentView.save();var i=this.succDialogView;this.model.saveAll({success:function(e,a){if(0==a.code)i.show("成功录入租约！已生成对应账单，是否前去确认业主账单",a.data.businessContractUuid);else if(a.code==-1)for(var n in a.data)a.data.hasOwnProperty(n)&&t.show(a.data[n])}})}});exports.AddLeasingView=d});