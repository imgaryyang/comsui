package com.zufangbao.earth.handler.impl.trade;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.util.DESUtil;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.entity.trade.ExternalTradeBatchDetail;
import com.zufangbao.sun.entity.trade.ExternalTradeBatchDetailModel;
import com.zufangbao.sun.utils.FinanceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Component("externalTradeFileAnalyzeFactory")
public class ExternalTradeFileAnalyzeFactory {
	
	private static final Log logger = LogFactory.getLog(ExternalTradeFileAnalyzeFactory.class);

	/** 金蛋交易結果 1：成功，2：失敗**/
	public final static String JINDAN_CONST_TRDE_RESULT_SUCC = "1";
	public final static String JINDAN_CONST_TRDE_RESULT_FAIL = "2";

	public final static String JINDAN_CONST_TRDE_TYPE_01 = "01";
	public final static String JINDAN_CONST_TRDE_TYPE_02 = "02";
	public final static String JINDAN_CONST_TRDE_TYPE_03 = "03";
	
	public final static String JINDAN_CONST_TRDE_CURRENCY = "CNY";
	
	public final static int CONST_BANK_CODE_TYPE_HENGSHENG = 0;
	public final static int CONST_BANK_CODE_TYPE_LIANHANGHAO = 1;
	public final static String CONST_DATE_FORMAT_TYPE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	/** 回盘文件解析策略 **/
	public final static String CONST_ANALYZE_STRATEGY_COMMON = "analyze_strategy_common";
	public final static String CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN = "analyze_strategy_special_jindan";
	public final static String JINDAN_ANALYZE_KEY="jinDan";

	public final static String CONST_FILE_INFO_PRODUCT_CODE = "product_no";
	public final static String CONST_FILE_INFO_BATCH_NO = "batch_no";
	public final static String CONST_FILE_INFO_TRADE_DATE = "trade_date";
	
	@Value("#{config['uploadPath']}")
	private String uploadPath;

	public Map<String, String> analyzeFileName(String fileName, String analyzeStrategy) throws CommonException {
		switch (analyzeStrategy) {
		case CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN:
			return analyzeFileNameForJinDan(fileName);
		default:
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "回盘文件解析失败，未知策略！");
		}
	}
	
	private Map<String, String> analyzeFileNameForJinDan(String fileName) throws CommonException {
		try {
			String[] judgeFile=fileName.split("\\u002E");
			if(!StringUtils.equals(judgeFile[1], "data")){
				throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "回盘文件解析失败，文件类型只限.data类型！");	
			}
			String[] fileInfos = fileName.split("-");
			String tradeDate=null;
			Map<String, String> result = new HashMap<>();
			result.put(CONST_FILE_INFO_PRODUCT_CODE, fileInfos[1]);
			result.put(CONST_FILE_INFO_BATCH_NO, fileInfos[2]);
			if(StringUtils.isNotEmpty(fileInfos[3])){
				String[] date=fileInfos[3].split("\\u002E");
				tradeDate=date[0];
			}
			result.put(CONST_FILE_INFO_TRADE_DATE, tradeDate);
			return result;
		} catch (Exception e) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "回盘文件解析失败，文件名与策略不符！");
		}
	}
	
	public List<ExternalTradeBatchDetailModel> analyzeDetail(InputStream ins, String analyzeStrategy) throws CommonException {
		switch (analyzeStrategy) {
		case CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN:
			return analyzeDetailForJinDan(ins);
		default:
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "回盘文件解析失败，未知策略！");
		}
	}
	
	public String creatFileContent(ExternalTradeBatchDetail detail,String financialContractName,String cashFlows,String analyzeStrategy) throws CommonException{
		switch (analyzeStrategy) {
		case CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN:
			return creatContentForJinDan(detail, financialContractName, cashFlows);
		default:
			throw new  CommonException(GlobalCodeSpec.CODE_FAILURE, "回盘文件内容创建失败，未知策略！");
		}
	}
	
	private List<ExternalTradeBatchDetailModel> analyzeDetailForJinDan(InputStream ins) throws CommonException{
		BufferedReader reader = null;
		if(ins == null) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "回盘文件解析异常！");
		}
		try {
			reader = new BufferedReader(new InputStreamReader(ins));
			List<ExternalTradeBatchDetailModel> list = new ArrayList<ExternalTradeBatchDetailModel>();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if(StringUtils.isEmpty(line)){
					break;
				}
				ExternalTradeBatchDetailModel batchNode = convertForJinDan(line);
				if(batchNode != null) {
					list.add(batchNode);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("#analyzeDetailForJinDan: 回盘文件读入流，关闭异常！");
				}
			}
		}
	}
	
	private ExternalTradeBatchDetailModel convertForJinDan(String line) throws CommonException {
		StringBuffer detailVerifyResultRemark=new StringBuffer();
		ExternalTradeBatchDetailModel batchNode = new ExternalTradeBatchDetailModel() ; 			// 明细实体类
		String[] dataList = trim(line.split("\\|")); 												// 获取文件中的数据
		if(dataList.length != 17) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "回盘文件解析异常，列数不符！");
		}
		if (JINDAN_CONST_TRDE_RESULT_SUCC.equals(dataList[14])) { 									// 成功的进行录入
			batchNode.setExternalRecordSn(dataList[0]); 											// 批次内记录序号
			batchNode.setExternalBatchNo(dataList[1]); 												// 外部批次号
			batchNode.setExternalRecordUniqueId(dataList[4]); 										// 外部记录唯一编号
			
			BigDecimal tmpAmount = new BigDecimal(dataList[5]);
			BigDecimal convertedAmount = FinanceUtils.convert_cent_to_yuan(tmpAmount);
			
			batchNode.setAmount(convertedAmount); 													// 交易金额
			batchNode.setCurrency(JINDAN_CONST_TRDE_CURRENCY); 										// 交易币种
			String cpName=null;
			String cpBankCardNo=null;
			try {
				cpName = DESUtil.decode(dataList[7], JINDAN_ANALYZE_KEY);
			} catch (Exception e) {
				batchNode.setDetailVerifyResult(2); 												// 数据检验存疑
				detailVerifyResultRemark.append("姓名解析出现错误！");
			}
			try {
			    cpBankCardNo=DESUtil.decode(dataList[8],JINDAN_ANALYZE_KEY);
			} catch (Exception e) {
				batchNode.setDetailVerifyResult(2); 												// 数据检验存疑
				detailVerifyResultRemark.append("银行卡号解析出现错误！");
			}
			
			batchNode.setCpName(cpName); 															// 姓名
			batchNode.setCpBankCardNo(cpBankCardNo); 												// 交易对手方银行卡号
			batchNode.setCpBankCodeType(CONST_BANK_CODE_TYPE_LIANHANGHAO);							//银行编码类型
			batchNode.setCpBankCode(dataList[9]);
			batchNode.setContractUniqueId(dataList[12]); 											// 合同uniqueId
			batchNode.setContractNo(dataList[12]); 													// 合同号
			batchNode.setTradeTime(DateUtils.parseDate(dataList[13], CONST_DATE_FORMAT_TYPE_YYYYMMDDHHMMSS));
			batchNode.setPaymentGateway(PaymentInstitutionName.JIN_YUN_TONG); 						//映射网关(默认金运通)
			String externalTradeSn = dataList[15];
			if (!StringUtils.isEmpty(dataList[15])) {
				String tradeType = externalTradeSn.substring(0, 2);
				String tradeSn = externalTradeSn.substring(2);
				switch (tradeType) {
				case JINDAN_CONST_TRDE_TYPE_01:	
					batchNode.setExternalBusinessOrderNo(tradeSn);									// 订单号
					batchNode.setTradeType(0); 														// 0:三方代扣
					break;
				case JINDAN_CONST_TRDE_TYPE_02:
					batchNode.setExternalTradeSerialNo(tradeSn); 									// 转账流水号
					batchNode.setTradeType(1); 														// 1:转账
					break;
				case JINDAN_CONST_TRDE_TYPE_03:
					batchNode.setTradeType(2); 														// 2:现金
					break;
				default:
					batchNode.setDetailVerifyResult(2); 											// 数据检验存疑
					detailVerifyResultRemark.append("交易平台流水号格式不符！");
					break;
				}
			} else {
				batchNode.setDetailVerifyResult(2); 												// 数据检验存疑
				detailVerifyResultRemark.append("交易平台流水号格式不符！");
			}
			batchNode.setTradeRemark(dataList[16]);
			batchNode.setDetailVerifyResultRemark(detailVerifyResultRemark.toString());				//数据检验存疑原因
			return batchNode;
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param detail  回盘文件明细
	 * @param financialContractName 渠道来源
	 * @param cashFlows   回盘文件对应的资金流水
	 * @return
	 */
	private String creatContentForJinDan(ExternalTradeBatchDetail detail,String financialContractName,String cashFlows){
		StringBuffer line=new StringBuffer();
		StringBuffer errorMessage=new StringBuffer("");
		line.append(detail.getExternalRecordSn());		//序号
		line.append("|");
		line.append(getOrDefault(detail.getExternalRecordUniqueId()));//系统内唯一编号
		line.append("|");
		line.append(getOrDefault(detail.getExternalBusinessOrderNo()));//订单号
		line.append("|");
		String convertedAmount = FinanceUtils.convert_yuan_to_cent(detail.getAmount());
		line.append(convertedAmount);					//订单金额
		line.append("|");
		line.append(detail.getCurrency());				//交易币种
		line.append("|");
		try {
			line.append(DESUtil.encode(detail.getCpName(), JINDAN_ANALYZE_KEY)); //姓名
		} catch (Exception e) {
			e.printStackTrace();
			line.append("");
			errorMessage.append("姓名加密错误!");
		}				
		line.append("|");
		try {
			line.append(DESUtil.encode(detail.getCpBankCardNo(),JINDAN_ANALYZE_KEY));//银行账号
		} catch (Exception e) {
			e.printStackTrace();
			line.append("");
			errorMessage.append("银行账号加密错误!");
		}			
		line.append("|");
		line.append(detail.getCpBankCode());			//开户行联行号
		line.append("|");
		line.append(financialContractName);				//渠道来源
		line.append("|");
		line.append(detail.getContractNo());			//借据号
		line.append("|");
		line.append(detail.getTradeTime());				//交易时间
		line.append("|");
		line.append(detail.getTradeType().ordinal());	//交易状态
		line.append("|");
		line.append(getOrDefault(detail.getExternalTradeSerialNo()));//清算平台流水号
		line.append("|");
		line.append(detail.getTradeRemark());			//交易摘要
		line.append("|");
		line.append(getOrDefault(detail.getCheckState()));//校验状态
		line.append("|");
		line.append(getOrDefault(cashFlows));			//资金流水
		line.append("|");
		line.append(detail.getDetailVerifyResultRemark());//备注
		line.append("|");
		line.append(errorMessage.toString());//返回文件存在的错误信息
		return line.toString();
	}
	
	
	private Object  getOrDefault(Object o){
		if(o==null){
			return "";
		}
		return o;
	}
	
	
	
	// 保存文件
	public void saveFile(File file, String localPath) throws CommonException {
		BufferedWriter writer = null;
		BufferedReader reader = null;
		try {
			File newFile = null;
			reader = new BufferedReader(new FileReader(file));
			newFile = new File(localPath + file.getName()); 	// 文件
			judeFileExists(newFile); 							// 判断文件是否存在
			writer = new BufferedWriter(new FileWriter(newFile, true));
			String line = null;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine();
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("#回盘文件存入解析异常，系统错误！");
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("#回盘文件读入流，关闭流异常！");
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("#回盘文件写入流，关闭流异常！");
				}
			}
			
		}

	}
	
	// 判断文件是否存在
	private  void judeFileExists(File file) throws CommonException {

		if (!file.exists()) {
			logger.info("file not exists, create it ...");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("ExternalTradeBatchHandlerImpl：文件创建失败！");
			}
		}else{
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "回盘文件已存在，请查看！");
		}

	}

	private String[] trim(String[] values) {
		String[] tmpValues = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			if (StringUtils.isNotEmpty(value)) {
				tmpValues[i] = value.trim();
			} else {
				tmpValues[i] = StringUtils.EMPTY;
			}
		}
		return tmpValues;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String fileUpload) {
		this.uploadPath = fileUpload;
	}
	
}
