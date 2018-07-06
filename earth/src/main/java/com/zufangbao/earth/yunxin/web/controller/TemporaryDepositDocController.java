/**
 * 
 */
package com.zufangbao.earth.yunxin.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.web.interceptor.MenuSetting;
import com.demo2do.core.web.resolver.Page;
import com.suidifu.hathaway.job.Priority;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.voucher.TmpDepositDetailCreateException;
import com.zufangbao.gluon.resolver.JsonViewResolver;
import com.zufangbao.sun.entity.customer.CustomerType;
import com.zufangbao.sun.entity.refund.QueryRepaymentPlanModel;
import com.zufangbao.sun.entity.refund.QueryRepurchaseModel;
import com.zufangbao.sun.entity.refund.TmpDepositUseStatus;
import com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.model.QueryTemporaryDepositDocModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.TmpDepositRecoverModel;
import com.zufangbao.sun.yunxin.entity.model.voucher.VoucherTmpDepositCreateRepaymentModel;
import com.zufangbao.sun.yunxin.service.refund.TemporaryDepositDocService;
import com.zufangbao.wellsfargo.yunxin.handler.ActivePaymentVoucherProxy;
import com.zufangbao.wellsfargo.yunxin.handler.TemporaryDepositDocHandler;
import com.zufangbao.wellsfargo.yunxin.model.QueryRepaymentPlanShowModel;
import com.zufangbao.wellsfargo.yunxin.model.QueryRepurchaseShowModel;
import com.zufangbao.wellsfargo.yunxin.model.TemDepositDocShowModel;
import com.zufangbao.wellsfargo.yunxin.model.TmpDepositDocRepayRecordModel;
import com.zufangbao.wellsfargo.yunxin.model.TmpDepositDocRepurchaseModel;

/**
 * @author hjl
 *　滞留单controller
 */
@Controller
@RequestMapping(value = "temporary-deposit-doc")
public class TemporaryDepositDocController {
	
	@Autowired
	private JsonViewResolver jsonViewResolver;
	
	@Autowired
	private TemporaryDepositDocHandler temporaryDepositDocHandler;
	
	@Autowired
	private ActivePaymentVoucherProxy activePaymentVoucherProxy;
	
	@Autowired
	private TemporaryDepositDocService temporaryDepositDocService;

	/**
	 * 获取下拉列表值
	 */
	@RequestMapping(value = "gain-drop-down-parameters" , method = RequestMethod.GET)
	public @ResponseBody String gainDropDownParameters(){
		try {
			Map<String , Object > data = new HashMap<>();
			//TODO delete feildParameters
			List<String> feildParameters= new ArrayList<>();
			feildParameters.add("客户姓名");
			feildParameters.add("滞留单号");
			
			data.put("customerParameters", EnumUtil.getKVList(CustomerType.class));
			data.put("tmpDepositUseStatusParameters", EnumUtil.getKVList(TmpDepositUseStatus.class));
			data.put("feildParameters", feildParameters);
			
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();

			return jsonViewResolver.errorJsonResult(e,"系统错误");
		}
	}
	
	/**
	 * 滞留单查询
	 */
	@MenuSetting("submenu-specical-ccount-retention-voucher")
	@RequestMapping(value = "query-temporary-deposit-doc" , method = RequestMethod.GET)
	public @ResponseBody String queryTemporaryDepositDoc(@ModelAttribute QueryTemporaryDepositDocModel queryTemporaryDepositDocModel,Page page){
		try {
			Map<String, Object> data = new HashMap<>();
			if(queryTemporaryDepositDocModel==null){
				throw new ApiException("查询Model为null");
			}
			data = temporaryDepositDocHandler.queryTemDepositDocByQueryModel(queryTemporaryDepositDocModel,page);
			
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			
			return jsonViewResolver.errorJsonResult(e,"系统错误");
		}
	}
	
	/**
	 * 查询滞留单详情
	 */
	@RequestMapping(value = "query-tem-deporary-doc-information", method = RequestMethod.POST)
	public @ResponseBody String showTmpDepositDocInfo(@RequestParam("temDepositDocUuid") String temDepositDocUuid) {
		try{
			
			if(StringUtils.isEmpty(temDepositDocUuid)){
				throw new ApiException("传入滞留单Uuid为空");
			}
			//基本信息和资金来源信息
			TemDepositDocShowModel temDepositDocShowModel = temporaryDepositDocHandler.queryTemDeporaryDocInformation(temDepositDocUuid);
			return jsonViewResolver.sucJsonResult("temDepositDocShowModel", temDepositDocShowModel);
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统异常");
		}
	}	
	
	/**
	 * 查询滞留单详情-计划还款信息
	 */
	@RequestMapping(value = "query-repayment-plan-information", method = RequestMethod.POST)
	public @ResponseBody String showRepaymentPlanInfo(@RequestParam("temDepositDocUuid") String temDepositDocUuid,Page page) {
		try{
			
			if(StringUtils.isEmpty(temDepositDocUuid)){
				throw new ApiException("传入滞留单Uuid为空");
			}
			//基本信息和资金来源信息
			List<TmpDepositDocRepayRecordModel> tmpDepositDocRepayRecordModelList = temporaryDepositDocHandler.queryTmpDepositDocRepayRecordModel(temDepositDocUuid,page);

			Map<String, Object> data = new HashMap<>();
			
			data.put("repayRecordModelList", tmpDepositDocRepayRecordModelList);
			
			return jsonViewResolver.sucJsonResult(data);
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统异常");
		}
		
	}

	/**
	 * 滞留单详情-回购单数据
	 */
	@RequestMapping(value = "query-repurchase-doc-information", method = RequestMethod.POST)
	public @ResponseBody String showRepurchaseDocInfo(@RequestParam("temDepositDocUuid") String temDepositDocUuid,Page page) {
		try{
			
			if(StringUtils.isEmpty(temDepositDocUuid)){
				throw new ApiException("传入滞留单Uuid为空");
			}
			List<TmpDepositDocRepurchaseModel> repuchaseShowModelList = temporaryDepositDocHandler.queryTmpDepositDocRepurchaseModel(temDepositDocUuid,page);

			Map<String, Object> data = new HashMap<>();

			data.put("repuchaseShowModel", repuchaseShowModelList);

			return jsonViewResolver.sucJsonResult(data);
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统异常");
		}
		
	}
	
	/**
	 * 滞留单详情-提交还款数据
	 */
	@RequestMapping(value = "post-repayment_order", method = RequestMethod.POST,params="repaymentBusinessType=0")
	public @ResponseBody String submit_repayment_info(@RequestParam("repaymentModelListJson") String repaymentModelListJson,@RequestParam("temDepositDocUuid") String temDepositDocUuid) {
		try{
			submitRepaymentInfo(repaymentModelListJson, temDepositDocUuid, RepaymentBusinessType.REPAYMENT_PLAN);
			return jsonViewResolver.sucJsonResult();
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统异常");
		}
	}

	private void submitRepaymentInfo(String repaymentModelListJson, String temDepositDocUuid, RepaymentBusinessType repaymentBusinessType)
			throws TmpDepositDetailCreateException {
		if(StringUtils.isEmpty(temDepositDocUuid)){
			throw new ApiException("滞留单Uuid为空");
		}
		List<VoucherTmpDepositCreateRepaymentModel> repaymentModelList = JsonUtils.parseArray(repaymentModelListJson,VoucherTmpDepositCreateRepaymentModel.class);
		TmpDepositRecoverModel recoverModel = temporaryDepositDocHandler.createSourceDocumentDetail(repaymentModelList, temDepositDocUuid, repaymentBusinessType);
		if(recoverModel.getOwnerType()==CustomerType.PERSON){
			activePaymentVoucherProxy.recover_tmp_deposit_doc(recoverModel.getBusinessUuid(), recoverModel.getTmpDepositDocUuid(), recoverModel.getSourceDocumentUuid(), Priority.High.getPriority(), recoverModel.getSecondNo());
		}
		if(recoverModel.getOwnerType()==CustomerType.COMPANY){
			temporaryDepositDocHandler.create_job_source_document_detail(recoverModel.getTmpDepositDocUuid(), recoverModel.getSecondNo());
		}
	}
	
	
	@RequestMapping(value = "post-repayment_order", method = RequestMethod.POST,params="repaymentBusinessType=1")
	public @ResponseBody String submit_repurchase_info(@RequestParam("repaymentModelListJson") String repaymentModelListJson,@RequestParam("temDepositDocUuid") String temDepositDocUuid) {
		try{
			submitRepaymentInfo(repaymentModelListJson, temDepositDocUuid, RepaymentBusinessType.REPURCHASE);
			return jsonViewResolver.sucJsonResult();
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统异常");
		}
	}
	
	/**
	 * 还款弹框查询还款计划
	 */
	@RequestMapping(value = "query-repayment-plan" , method = RequestMethod.POST)
	public @ResponseBody String queryRepaymentPlan(@ModelAttribute QueryRepaymentPlanModel queryRepaymentPlanModel,Page page){
		try{
			Map<String,Object> data= new HashMap<>();
			if(queryRepaymentPlanModel == null){
				return jsonViewResolver.errorJsonResult(data);
			}
			List<QueryRepaymentPlanShowModel> queryRepaymentPlanShowModels= temporaryDepositDocHandler.queryRepaymentPlanShowModels(queryRepaymentPlanModel, page);

			data.put("queryRepaymentPlanShowModels", queryRepaymentPlanShowModels);
			return jsonViewResolver.sucJsonResult(data);
		}catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统错误");
		}
	}

	/**
	 * 回购弹框查询
	 */
	@RequestMapping(value = "query-repurchase-doc" , method = RequestMethod.POST)
	public @ResponseBody String queryRepurchaseDoc(@ModelAttribute QueryRepurchaseModel queryRepurchaseModel,Page page){
		try {
			Map<String,Object> data= new HashMap<>();
			if(queryRepurchaseModel == null){
				return jsonViewResolver.sucJsonResult(data);
			}
			List<QueryRepurchaseShowModel> queryRepurchaseShowModels = temporaryDepositDocHandler.queryRepurchaseShowModel(queryRepurchaseModel, page);

			data.put("queryRepurchaseShowModels", queryRepurchaseShowModels);
			return jsonViewResolver.sucJsonResult(data);
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统错误");
		}
	}
	
	/**
	 * 修改备注
	 */
	@RequestMapping(value = "update-temporary-deposit-doc-remark" , method = RequestMethod.POST)
	public @ResponseBody String updateTemporaryDepositDocRemark(@RequestParam("temporaryDepositDocUuid") String temporaryDepositDocUuid,@RequestParam("remark") String remark){
		try{
			if(StringUtils.isEmpty(temporaryDepositDocUuid) || StringUtils.isEmpty(remark)){
				throw new ApiException("传入参数不能为空");
			}
			temporaryDepositDocService.updateTemDstDocRemark(temporaryDepositDocUuid, remark);
			
			return jsonViewResolver.sucJsonResult();
		}catch(Exception e){
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult(e,"系统异常");
		}
	}
}
