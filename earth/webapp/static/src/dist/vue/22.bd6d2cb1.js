webpackJsonp([22,23],{673:function(e,r,t){"use strict";function n(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(r,"__esModule",{value:!0});var o=t(1),a=t(2),l=n(a);r.default={activated:function(){this.currentModel={companyFullName:"",appName:"",appId:"",address:"",legalPerson:"",businessLicence:""}},data:function(){return{currentModel:{companyFullName:"",appName:"",appId:"",address:"",legalPerson:"",businessLicence:""},rules:{companyFullName:{required:!0,message:" ",trigger:"blur"},appName:{required:!0,message:" ",trigger:"blur"},appId:{required:!0,message:" ",trigger:"blur"},address:{required:!0,message:" ",trigger:"blur"}}}},methods:{submit:function(){var e=this;this.$refs.form.validate(function(r){r&&(0,o.ajaxPromise)({url:"/app/create",type:"post",data:e.currentModel}).then(function(r){l.default.once("closed",function(){return location.assign(e.ctx+"#/financial/customer/financial-app?t="+(new Date).getTime())}),l.default.open("操作成功")}).catch(function(e){l.default.open(e)})})}}}},1224:function(e,r){},1553:function(e,r,t){var n,o;t(1224),n=t(673);var a=t(1809);o=n=n||{},"object"!=typeof n.default&&"function"!=typeof n.default||(o=n=n.default),"function"==typeof o&&(o=o.options),o.render=a.render,o.staticRenderFns=a.staticRenderFns,e.exports=n},1809:function(e,r){e.exports={render:function(){var e=this,r=e.$createElement,t=e._self._c||r;return t("div",{staticClass:"content"},[t("div",{staticClass:"scroller"},[t("Breadcrumb",{attrs:{routes:[{title:"信托商户管理"},{title:"信托商户新增"}]}}),e._v(" "),t("el-form",{ref:"form",staticClass:"sdf-form",attrs:{rules:e.rules,"label-width":"120px",model:e.currentModel}},[t("el-form-item",{staticClass:"form-item-legend",attrs:{label:"商户公司信息"}}),e._v(" "),t("el-form-item",{attrs:{label:"商户公司全称",prop:"companyFullName",required:""}},[t("el-input",{directives:[{name:"model",rawName:"v-model.trim",value:e.currentModel.companyFullName,expression:"currentModel.companyFullName",modifiers:{trim:!0}}],staticClass:"long",domProps:{value:e.currentModel.companyFullName},on:{input:function(r){e.currentModel.companyFullName="string"==typeof r?r.trim():r},blur:function(r){e.$forceUpdate()}}})]),e._v(" "),t("el-form-item",{attrs:{label:"商户简称",prop:"appName",required:""}},[t("el-input",{directives:[{name:"model",rawName:"v-model.trim",value:e.currentModel.appName,expression:"currentModel.appName",modifiers:{trim:!0}}],staticClass:"long",domProps:{value:e.currentModel.appName},on:{input:function(r){e.currentModel.appName="string"==typeof r?r.trim():r},blur:function(r){e.$forceUpdate()}}})]),e._v(" "),t("el-form-item",{attrs:{label:"商户代码",prop:"appId",required:""}},[t("el-input",{directives:[{name:"model",rawName:"v-model.trim",value:e.currentModel.appId,expression:"currentModel.appId",modifiers:{trim:!0}}],staticClass:"long",domProps:{value:e.currentModel.appId},on:{input:function(r){e.currentModel.appId="string"==typeof r?r.trim():r},blur:function(r){e.$forceUpdate()}}})]),e._v(" "),t("el-form-item",{attrs:{label:"地址",prop:"address",required:""}},[t("el-input",{directives:[{name:"model",rawName:"v-model.trim",value:e.currentModel.address,expression:"currentModel.address",modifiers:{trim:!0}}],staticClass:"long",domProps:{value:e.currentModel.address},on:{input:function(r){e.currentModel.address="string"==typeof r?r.trim():r},blur:function(r){e.$forceUpdate()}}})]),e._v(" "),t("el-form-item",{attrs:{label:"公司法人",prop:"legalPerson"}},[t("el-input",{directives:[{name:"model",rawName:"v-model.trim",value:e.currentModel.legalPerson,expression:"currentModel.legalPerson",modifiers:{trim:!0}}],staticClass:"long",domProps:{value:e.currentModel.legalPerson},on:{input:function(r){e.currentModel.legalPerson="string"==typeof r?r.trim():r},blur:function(r){e.$forceUpdate()}}})]),e._v(" "),t("el-form-item",{attrs:{label:"公司营业执照",prop:"businessLicence"}},[t("el-input",{directives:[{name:"model",rawName:"v-model.trim",value:e.currentModel.businessLicence,expression:"currentModel.businessLicence",modifiers:{trim:!0}}],staticClass:"long",domProps:{value:e.currentModel.businessLicence},on:{input:function(r){e.currentModel.businessLicence="string"==typeof r?r.trim():r},blur:function(r){e.$forceUpdate()}}})]),e._v(" "),t("el-form-item",{staticStyle:{"margin-top":"20px"}},[t("el-button",{on:{click:function(r){e.$router.go(-1)}}},[e._v("取消")]),e._v(" "),t("el-button",{attrs:{type:"primary"},on:{click:e.submit}},[e._v("提交")])])])])])},staticRenderFns:[]}}});