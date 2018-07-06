package com.suidifu.jpmorgan.handler.impl;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.entity.Result;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.GlobalSpec.ElecPayment;
import com.suidifu.coffer.GlobalSpec.ErrorMsg;
import com.suidifu.coffer.entity.*;
import com.suidifu.coffer.entity.cmb.AddressCodeMap;
import com.suidifu.coffer.entity.cmb.UnionPayBankCodeMap;
import com.suidifu.coffer.entity.unionpay.gz.*;
import com.suidifu.coffer.entity.unionpay.gz.constant.GZUnionPayConstants.GZUnionPayResponseCode;
import com.suidifu.coffer.handler.unionpay.GZUnionpayHandler;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.spec.PaymentHandlerSpec;
import com.suidifu.jpmorgan.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("gzUnionPaymentHandlerImpl")
public class GZUnionPaymentHandlerImpl implements PaymentHandler {
	private Log logger = LogFactory.getLog(GZUnionPaymentHandlerImpl.class);

	@Autowired
	private GZUnionpayHandler gzUnionpayHandler;

	@Override
	public CreditResult executeSinglePay(PaymentOrder paymentOrder,
			WorkingContext context) {
		try {
			Map<String, String> workParms = context.getWorkingParameters();
			CreditResult creditResult = new CreditResult();
			creditResult.setRequestStatus(BusinessRequestStatus.FINISH);

			if (AccountSide.DEBIT.ordinal() == paymentOrder.getAccountSide()) {
				String debitMode = workParms.getOrDefault("debitMode",
						StringUtils.EMPTY);
				if (ElecPayment.GZUNION_DEBITMODE_BATCH
						.equalsIgnoreCase(debitMode)) {
					GZUnionPayBatchDebitModel batchDebitModel = generateBatchDebitModel(
							paymentOrder, workParms);
					gzUnionpayHandler.batchDebit(batchDebitModel, workParms);
				} else if (ElecPayment.GZUNION_DEBITMODE_REALTIME
						.equalsIgnoreCase(debitMode)) {
					GZUnionPayRealTimeDebitModel realTimeDebitModel = generateRealTimeDebitModel(
							paymentOrder, workParms);
					gzUnionpayHandler.realTimeDebit(realTimeDebitModel,
							workParms);
				}else {
					return new CreditResult(ErrorMsg.ERR_NOT_SUPPORT_MODE);
				}

				return creditResult;
			} else {
				GZUnionPayRealTimePaymentModel reanTimePaymentModel = generateRealTimePaymentModel(paymentOrder, workParms);
				String creditMode = workParms.getOrDefault(ElecPayment.GZUNION_CREDITMODE, "");
				DebitResult debitResult;
				if (ElecPayment.GZUNION_CREDITMODE_BATCH.equals(creditMode)) {
				    GzUnionPayBatchPaymentModel gzUnionPayBatchPaymentModel =
                            new GzUnionPayBatchPaymentModel(reanTimePaymentModel);
				    debitResult = gzUnionpayHandler.batchPayment(gzUnionPayBatchPaymentModel, workParms);
                } else {
                    debitResult = gzUnionpayHandler.realTimePayment(reanTimePaymentModel, workParms);
                }
				if (!GZUnionPayResponseCode.PROCESSED.equals(debitResult
					.getRetCode())) {
					logger.warn("gzUnion realTimePayment responseXml:"+ debitResult.getResponsePacket());
				}
				
				return creditResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new CreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
	}

	@SuppressWarnings("unchecked")
	private GZUnionPayBatchDebitModel generateBatchDebitModel(
			PaymentOrder paymentOrder, Map<String, String> workParms) {
		GZUnionPayBatchDebitModel batchDebitModel = new GZUnionPayBatchDebitModel();
		batchDebitModel.setReqNo(paymentOrder.getUuid());
		batchDebitModel.setBusiCode(workParms.getOrDefault("businessCode",
				ElecPayment.GZUNION_DEFAULT_BUSINESSCODE));
		batchDebitModel.setTotalItem(1);
		batchDebitModel.setTotalSum(paymentOrder.getTransactionAmount());
		List<GZUnionPayDebitInfoModel> debitInfoModelList = new ArrayList<GZUnionPayDebitInfoModel>();

		Map<String, String> destinationBankInfo = new HashMap<String, String>();
		try {
			if (!StringUtils.isEmpty(paymentOrder.getDestinationBankInfo())) {
				destinationBankInfo = JSON.parseObject(
						paymentOrder.getDestinationBankInfo(), Map.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, String> destinationAccountAppendix = new HashMap<String, String>();
		try {
			if (!StringUtils.isEmpty(paymentOrder
					.getDestinationAccountAppendix())) {
				destinationAccountAppendix = JSON
						.parseObject(
								paymentOrder.getDestinationAccountAppendix(),
								Map.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String bankCode = destinationBankInfo.getOrDefault("bankCode",
				StringUtils.EMPTY);
		String idCardNum = destinationAccountAppendix.getOrDefault("idNumber",
				StringUtils.EMPTY);
		String province = destinationBankInfo.getOrDefault("bankProvince",
				StringUtils.EMPTY);
		String city = destinationBankInfo.getOrDefault("bankCity",
				StringUtils.EMPTY);
		GZUnionPayDebitInfoModel debitInfoModel = new GZUnionPayDebitInfoModel(
				paymentOrder.getUuid(),
				UnionPayBankCodeMap.BANK_CODE_MAP.getOrDefault(bankCode,
						StringUtils.EMPTY),
				paymentOrder.getDestinationAccountNo(),
				paymentOrder.getDestinationAccountName(),
				paymentOrder.getTransactionAmount(), String.format(
						PaymentHandlerSpec.TRANSACTION_POSTSCRIPT_TEMPLATE,
						paymentOrder.getUuid(),//TODO 2016.11.21改，在只用fst_slot的情况下，备注中用uuid
						paymentOrder.getPostscript()), idCardNum,
				AddressCodeMap.PROVINCE_CODE_MAP.getOrDefault(province,
						StringUtils.EMPTY),
				AddressCodeMap.CITY_CODE_MAP.getOrDefault(city,
						StringUtils.EMPTY), workParms.getOrDefault(
						"reckonAccount", StringUtils.EMPTY));

		debitInfoModelList.add(debitInfoModel);
		batchDebitModel.setDetailInfos(debitInfoModelList);
		return batchDebitModel;
	}

	@SuppressWarnings("unchecked")
	private GZUnionPayRealTimeDebitModel generateRealTimeDebitModel(
			PaymentOrder paymentOrder, Map<String, String> workParms) {
		GZUnionPayRealTimeDebitModel realTimeDebitModel = new GZUnionPayRealTimeDebitModel();
		realTimeDebitModel.setReqNo(paymentOrder.getUuid());
		realTimeDebitModel.setBusiCode(workParms.getOrDefault("businessCode",
				ElecPayment.GZUNION_DEFAULT_BUSINESSCODE));
		Map<String, String> destinationBankInfo = new HashMap<String, String>();
		try {
			if (!StringUtils.isEmpty(paymentOrder.getDestinationBankInfo())) {
				destinationBankInfo = JSON.parseObject(
						paymentOrder.getDestinationBankInfo(), Map.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, String> destinationAccountAppendix = new HashMap<String, String>();
		try {
			if (!StringUtils.isEmpty(paymentOrder
					.getDestinationAccountAppendix())) {
				destinationAccountAppendix = JSON
						.parseObject(
								paymentOrder.getDestinationAccountAppendix(),
								Map.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String bankCode = destinationBankInfo.getOrDefault("bankCode",
				StringUtils.EMPTY);
		String idCardNum = destinationAccountAppendix.getOrDefault("idNumber",
				StringUtils.EMPTY);
		String province = destinationBankInfo.getOrDefault("bankProvince",
				StringUtils.EMPTY);
		String city = destinationBankInfo.getOrDefault("bankCity",
				StringUtils.EMPTY);

		GZUnionPayDebitInfoModel debitInfoModel = new GZUnionPayDebitInfoModel(
				paymentOrder.getUuid(),
				UnionPayBankCodeMap.BANK_CODE_MAP.getOrDefault(bankCode,
						StringUtils.EMPTY),
				paymentOrder.getDestinationAccountNo(),
				paymentOrder.getDestinationAccountName(),
				paymentOrder.getTransactionAmount(), String.format(
						PaymentHandlerSpec.TRANSACTION_POSTSCRIPT_TEMPLATE,
						paymentOrder.getUuid(),//TODO 2016.11.21改，在只用fst_slot的情况下，备注中用uuid
						paymentOrder.getPostscript()), idCardNum,
				AddressCodeMap.PROVINCE_CODE_MAP.getOrDefault(province,
						StringUtils.EMPTY),
				AddressCodeMap.CITY_CODE_MAP.getOrDefault(city,
						StringUtils.EMPTY), workParms.getOrDefault(
						"reckonAccount", StringUtils.EMPTY));

		realTimeDebitModel.setInfoModel(debitInfoModel);
		return realTimeDebitModel;
	}
	
	@SuppressWarnings("unchecked")
	public GZUnionPayRealTimePaymentModel generateRealTimePaymentModel(PaymentOrder paymentOrder,
			Map<String, String> workParms) {
		GZUnionPayRealTimePaymentModel realTimePaymentModel = new GZUnionPayRealTimePaymentModel();
		realTimePaymentModel.setReqNo(paymentOrder.getUuid());
		realTimePaymentModel.setTotalItem(1);
		realTimePaymentModel.setTotalSum(paymentOrder.getTransactionAmount());
		realTimePaymentModel.setBusiCode(workParms.getOrDefault("businessCode",
				ElecPayment.GZUNION_CREDIT_BUSINESSCODE));
		List<GZUnionPayPaymentDetailInfoModel> detailInfos = new ArrayList<GZUnionPayPaymentDetailInfoModel>();
		Map<String, String> destinationBankInfo = new HashMap<String, String>();
		Map<String, String> destinationAccountAppendix = new HashMap<String, String>();
		Map<String, String> transactionAppendix = new HashMap<String, String>();
		try {
			if (!StringUtils.isEmpty(paymentOrder.getDestinationBankInfo())) {
				destinationBankInfo = JSON.parseObject(paymentOrder.getDestinationBankInfo(), Map.class);
			}
			if (!StringUtils.isEmpty(paymentOrder.getDestinationAccountAppendix())) {
				destinationAccountAppendix = JSON.parseObject(paymentOrder.getDestinationAccountAppendix(), Map.class);
			}
			if (StringUtils.isNotEmpty(paymentOrder.getGatewayRouterInfo())) {
				transactionAppendix = JSON.parseObject(paymentOrder.getGatewayRouterInfo(), Map.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		String bankCode = destinationBankInfo.getOrDefault("bankCode", StringUtils.EMPTY);
		if (StringUtils.isNotEmpty(bankCode)) {
			bankCode = bankCode.substring(0, 3);
		}
		String bankName = destinationBankInfo.getOrDefault("bankName", StringUtils.EMPTY);
		String province = destinationBankInfo.getOrDefault("bankProvince", StringUtils.EMPTY);
		String city = destinationBankInfo.getOrDefault("bankCity", StringUtils.EMPTY);
		String idCardNum = destinationAccountAppendix.getOrDefault("idNumber", StringUtils.EMPTY);
		String reckonAccount = transactionAppendix.getOrDefault("reckonAccount", StringUtils.EMPTY);
		GZUnionPayPaymentDetailInfoModel detailInfo = new GZUnionPayPaymentDetailInfoModel(paymentOrder.getUuid(),
				bankCode, paymentOrder.getDestinationAccountNo(), paymentOrder.getDestinationAccountName(),
				AddressCodeMap.PROVINCE_CODE_MAP.getOrDefault(province, StringUtils.EMPTY),
				AddressCodeMap.CITY_CODE_MAP.getOrDefault(city, StringUtils.EMPTY), bankName,
				paymentOrder.getTransactionAmount(), paymentOrder.getPostscript(), idCardNum, reckonAccount);

		detailInfos.add(detailInfo);
		realTimePaymentModel.setDetailInfos(detailInfos);
		return realTimePaymentModel;
	}

	@Override
	public Result executeBatchPay(List<PaymentOrder> paymentOrderList,
			WorkingContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryCreditResult executeQueryPaymentStatus(
			PaymentOrder paymentOrder, WorkingContext context) {
		try {

			GZUnionPayDebitQueryModel debitQueryModel = generateQueryCreditModel(paymentOrder);

			GZUnionPayResult debitResult = (GZUnionPayResult) gzUnionpayHandler
					.debitQuery(debitQueryModel, context.getWorkingParameters());

			QueryCreditResult queryCreditResult = new QueryCreditResult();

			if (!debitResult.isValid()) {
				return new QueryCreditResult(debitResult.getMessage());
			}

			if (!GZUnionPayResponseCode.PROCESSED.equals(debitResult
					.getRetCode())) {
				if (GZUnionPayResponseCode.QUERY_REQ_NO_NOT_EXIST
						.equals(debitResult.getRetCode())) {
					queryCreditResult
							.setRequestStatus(BusinessRequestStatus.NOTRECEIVE);
					queryCreditResult.setBusinessResultMsg(debitResult
							.getRetMsg());
					return queryCreditResult;
				} else {
					queryCreditResult
							.setRequestStatus(BusinessRequestStatus.FINISH);
					queryCreditResult
							.setProcessStatus(BusinessProcessStatus.INPROCESS);
					return queryCreditResult;
				}
			}

			String rspPacket = debitResult.getResponsePacket();
			try {
				Document document = DocumentHelper.parseText(rspPacket);
				Node completeTimeNode = document
						.selectSingleNode("GZELINK/BODY/RET_DETAILS/RET_DETAIL/COMPLETE_TIME");
				Node retCodeNode = document
						.selectSingleNode("GZELINK/BODY/RET_DETAILS/RET_DETAIL/RET_CODE");
				Node errMsgNode = document
						.selectSingleNode("GZELINK/BODY/RET_DETAILS/RET_DETAIL/ERR_MSG");

				String retCode = null == retCodeNode ? StringUtils.EMPTY
						: retCodeNode.getText();
				String errMsg = null == errMsgNode ? StringUtils.EMPTY
						: errMsgNode.getText();

				String businessResultMsg = String.format(GlobalSpec.BUSINESS_RESULT_CODE_TEMPLATE, retCode, errMsg);

				if (GZUnionPayResponseCode.PROCESSING.equals(retCode)
						|| GZUnionPayResponseCode.DIFFERENT_BANK_PROCESSING
								.equals(retCode)) {
					queryCreditResult
							.setRequestStatus(BusinessRequestStatus.FINISH);
					queryCreditResult
							.setProcessStatus(BusinessProcessStatus.INPROCESS);
					return queryCreditResult;
				}

				if (GZUnionPayResponseCode.PROCESSED.equals(retCode)) {
					queryCreditResult
							.setRequestStatus(BusinessRequestStatus.FINISH);
					queryCreditResult
							.setProcessStatus(BusinessProcessStatus.SUCCESS);
					queryCreditResult.setBusinessResultMsg(businessResultMsg);
					Date businessSuccessTime = null == completeTimeNode ? null
							: DateUtils
									.parseFullDateTimeToDate(completeTimeNode
											.getText());
					queryCreditResult
							.setBusinessSuccessTime(businessSuccessTime);
					return queryCreditResult;
				} else {
					queryCreditResult
							.setRequestStatus(BusinessRequestStatus.FINISH);
					queryCreditResult
							.setProcessStatus(BusinessProcessStatus.FAIL);
					queryCreditResult.setBusinessResultMsg(businessResultMsg);
					return queryCreditResult;
				}

			} catch (DocumentException e) {
				e.printStackTrace();
				return new QueryCreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new QueryCreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
	}

	private GZUnionPayDebitQueryModel generateQueryCreditModel(
			PaymentOrder paymentOrder) {
		GZUnionPayDebitQueryModel debitQueryModel = new GZUnionPayDebitQueryModel();
		debitQueryModel.setReqNo(UUID.randomUUID().toString());
		debitQueryModel.setQueryReqNo(paymentOrder.getUuid());
		return debitQueryModel;
	}

	@Override
	public Result isTerminated(int remains) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryCreditResult handleCallback(Map<String, String> callbackParms) {
		// TODO Auto-generated method stub
		return null;
	}
	
}