define(function(require,exports,module){var t=require("baseView/tableContent"),n=require("component/popupTip"),e=require("baseView/baseFormView").FormDialogView,i=require("baseView/baseFormView"),a=(i.SketchItemView,require("entity/contract/leasingBillModel").BillingPlanModel),o=global_config.root,c=(global_config.resource,e.extend({template:_.template($("#accountInfoViewTmpl").html(),{variable:"obj"}),candicateAccountItemTemplate:_.template($("#candicateAccountItemTmpl").html()),initialize:function(t){c.__super__.initialize.apply(this,arguments),this.otherAccount=$('<div class="accounts clearfix" style="width:325px"/>')},events:{"click .btn-add-account":"onClickChooseAccount","click .account-item":"onClickAccountItem","click .add-other-account":"onClickAddOtherAccount"},render:function(){var t=this.model.toJSON();$.extend(this,t),this.$el.html(this.template(t))},validate:function(){return this.$(".real-value").val()?(this.$(".btn-add-account").removeClass("error"),!0):(this.$(".btn-add-account").addClass("error"),!1)},onClickChooseAccount:function(t){var n="BILL_ENTRY_TYPE_ACCOUNT_PAYABLE",e=function(t,e){var i=this.candicateAccountItemTemplate({billType:n,accounts:t,isWEG:this.isWEG(n)});this.otherAccount.html(i),this.$(".add-account").append(this.otherAccount),this.$(".accounts ").show()};this.fetchAccount(n,$.proxy(e,this))},fetchAccount:function(t,n){var e=o+"/billing-plan/leasing-bill-account",i=this;$.ajax({url:e,type:"post",dataType:"json",data:{billType:t,businessContractUuid:this.businessContractUuid},success:function(t){i.cachcAccounts=t.data.accounts,n(t.data.accounts,t)}})},isWEG:function(t){var n;return n="COURSEOFDEALING_LEASING_WATER_FEES"===t||"COURSEOFDEALING_LEASING_GAS_FEES"===t||"COURSEOFDEALING_LEASING_ELECTRICITY_FEES"===t},onClickAccountItem:function(t){var n=$(t.currentTarget).data("accountownername")+"";if(n){var e=this.getAccount(n);this.model.set("accountInfo",e),this.$(".cash-account").val(e.account),this.$(".cash-name").val(e.accountOwnerName),this.$(".accounts ").hide()}},getAccount:function(t){var n=_.findWhere(this.cachcAccounts,{accountOwnerName:t});return n},onClickAddOtherAccount:function(t){t.preventDefault(),this.otherAccount.remove();var n=$("#judge-leasingtype").val();"CONTRACT_LEASING_PROPERTY"==n?window.open(o+"/business-contract/leasing-contract/"+this.businessContractUuid):window.open(o+"/business-contract/rental-contract/"+this.businessContractUuid)},submitData:function(t){var t=JSON.stringify(this.model.get("accountInfo")),e=this.bilingplanUuid,i={url:o+"/billing-plan/"+e+"/add-account-info",type:"POST",dataType:"json",contentType:"application/json",data:t};i.success=function(t){n.show(t.message)},$.ajax(i)},submitHandler:function(t){this.validate()&&(this.submitData(),this.hide(),setTimeout(function(){location.reload()},2e3))}})),s=e.extend({template:_.template($("#batchPaymentFormTmpl").html()),initialize:function(t){s.__super__.initialize.apply(this,arguments),this.envelopeUuid=t.envelopeUuid,this.defineValidator()},validate:function(){return this.validator.form()},defineValidator:function(){this.validator=this.$(".form").validate({ignore:".hide [name]",rules:{applicationTime:"required"},success:function(t,n){var e=$(n).parent();e.is(".imitate-datetimepicker-input")&&(n.value?e.removeClass("error"):e.addClass("error"))},errorPlacement:function(t,n){var e=n.parent();e.is(".parcel-input")?t.insertAfter(e):e.is(".imitate-datetimepicker-input")||t.insertAfter(n)}})},save:function(){var t=this.extractDomData();t.envelopeUuid=this.envelopeUuid;var e={url:"./applicat-payment",type:"POST",dataType:"json",contentType:"application/json",data:JSON.stringify(t)};e.success=function(t){n.show(t.message)},$.ajax(e)},submitHandler:function(t){this.validate()&&(this.save(),this.hide(),setTimeout(function(){location.reload()},2e3))}}),r=t.extend({events:{"click .destroy-envelope":"onDestoryEnvelope","click .applicat-payment":"onApplicatPayment","click .ajustPayAccountInfo":"onAjustPayAccountInfo","click .modify":"onModifyDiscount"},initialize:function(t){r.__super__.initialize.apply(this,arguments)},onApplicatPayment:function(t){var n=$(t.target).attr("envelope-uuid"),e=new s({envelopeUuid:n});e.show()},onDestoryEnvelope:function(t){var e=[];e.push($(t.target).attr("envelope-uuid"));var i=this;i.asyncPost("./destroy-envelope",e,function(t){n.show(t.message),i.redirectPage("./payment-batch/index")})},onAjustPayAccountInfo:function(t){var n={},e=$(t.target).parent("td");n.businessContractUuid=e.attr("business-contract-uuid"),n.financialAccountNo=e.attr("financial-account-no"),n.financialAccountBankInfoDesc=e.attr("financial-account-bank-info-desc"),n.financialAccountName=$(t.target).prev().text(),n.bilingplanUuid=e.attr("bilingplan-uuid");var i=new Backbone.Model(n),a=new c({model:i});a.show()},onModifyDiscount:function(t){t.preventDefault();var n=$(t.target).parent("td"),e={};e.billUuid=n.attr("bilingplan-uuid"),e.carringAmount=n.attr("carring-amount");var i=new a(e),o=new u({model:i});o.show()},asyncPost:function(t,n,e){var i={url:t,type:"POST",dataType:"json",contentType:"application/json",data:JSON.stringify(n)};i.success=e,$.ajax(i)},refreshTable:function(){setTimeout(function(){location.reload()},2e3)},redirectPage:function(t){location.href=t}}),u=e.extend({template:_.template($("#editDiscountTmpl").html()),initialize:function(){u.__super__.initialize.apply(this,arguments),this.defineValidator()},remove:function(){delete $.validator.methods.lowCarringAmount,u.__super__.remove.apply(this,arguments)},defineValidator:function(){var t=this.model;$.validator.addMethod("lowCarringAmount",function(n,e){return this.optional(e)||t.get("carringAmount")>=+n},"折扣金额不能大于账单应收金额"),$.validator.addMethod("rightformatAmount",function(t,n){return this.optional(n)||/^([-]*([1-9]\d{0,5})|0)(\.\d{1,2})?$/.test(t)},"请输入正确的金额格式"),this.validator=this.$(".form").validate({rules:{amount:{rightformatAmount:!0,lowCarringAmount:!0}},errorPlacement:function(t,n){var e=n.parent();e.is(".parcel-input")?t.insertAfter(e):t.insertAfter(n)}})},validate:function(){return this.validator.form()},save:function(){var t=this.extractDomData();this.model.modifyDiscount(t)},submitHandler:function(t){this.validate()&&(this.save(),this.hide())}});exports.BatchPaymentDetailView=r});