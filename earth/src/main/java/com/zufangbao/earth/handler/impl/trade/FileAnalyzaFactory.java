package com.zufangbao.earth.handler.impl.trade;

import com.demo2do.core.utils.DateUtils;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.trade.ExternalTradeBatchDetail;
import com.zufangbao.sun.entity.trade.ExternalTradeBatchDetailModel;
import com.zufangbao.sun.entity.trade.ExternalTradeType;
import com.zufangbao.sun.handler.trade.ExternalTradeBatchCashHandler;
import com.zufangbao.sun.handler.trade.ExternalTradeBatchHandler;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.trade.ExternalTradeBatchDetailService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Map;

import static com.zufangbao.earth.handler.impl.trade.ExternalTradeFileAnalyzeFactory.*;

@Component("fileAnalyzaFactory")
public class FileAnalyzaFactory {
	
	@Autowired
	private ExternalTradeBatchDetailService externalTradeBatchDetailService;
	
	@Autowired
	private ExternalTradeBatchHandler externalTradeBatchHandler;
	
	
	
	@Autowired
	private ExternalTradeBatchCashHandler externalTradeBatchCashHandler;

	
	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private ExternalTradeFileAnalyzeFactory analyzeFactory;

	private static final Log logger = LogFactory.getLog(FileAnalyzaFactory.class);

	// 本地解析文件并处理的方式
	/**
	 * 
	 * @param storageFolder
	 *            本地保存的不存疑的文件夹
	 * @param downLoadFolder
	 *            JinDanFTp文件存放文件夹
	 * @param doubtFolder
	 *            存疑文件夹
	 */
	public void doCheckAndSaveJinDanFiles(String storageFolder, String downLoadFolder, String doubtFolder) {
		long totalAmount = 0;
		long correctHandledFileCount = 0;
		String fileName = null;
		
		File fileFolder = new File(downLoadFolder);
		judeDirExists(fileFolder);
		File[] files = fileFolder.listFiles();
		for (File file : files) {
			long start = System.currentTimeMillis();
			fileName = file.getName();
			logger.info("#singleCheckAndSaveJinDanFiles start, file["+fileName+"].");
			try {
				InputStream ins = new FileInputStream(file);
				Map<String, String> fileInfos = analyzeFactory.analyzeFileName(fileName, CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
				String productNo = fileInfos.get(CONST_FILE_INFO_PRODUCT_CODE);
				String batchNo = fileInfos.get(CONST_FILE_INFO_BATCH_NO);
				String tradeDate = fileInfos.get(CONST_FILE_INFO_TRADE_DATE);
				FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(productNo);

				if (financialContract == null) { 											// 异常 文件转移到doubtFolder目录下
					logger.error("#doCheckAndSaveJinDanFiles: 与回盘文件相对应的项目不存在, 请查证, contractNo[" + productNo + "], file[" + fileName+"].");
					this.writeToFile(doubtFolder, file);									// 转移文件到doubtFolder文件夹下
					continue;
				}

				List<ExternalTradeBatchDetailModel> list = analyzeFactory.analyzeDetail(ins,
						CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
				if (CollectionUtils.isEmpty(list)) { 										// 异常 文件转移到doubtFolder目录下
					logger.error("#doCheckAndSaveJinDanFiles: 回盘文件解析出现异常, 请查看回盘文件中的数据格式, file[" + fileName+"].");
					this.writeToFile(doubtFolder, file); 									// 转移文件到doubtFolder文件夹下
					continue;
				}
				this.writeToFileForStorageFolder(storageFolder, doubtFolder, file);
				correctHandledFileCount++;
				externalTradeBatchHandler.importDataForExternalTradeBatch(list, productNo, batchNo, null, null,
						tradeDate); 														// 保存至数据库
			} catch (CommonException e) {
				String message = e.getErrorMsg();
				this.writeToFile(doubtFolder, file);
				logger.error("#doCheckAndSaveJinDanFiles: 回盘文件["+fileName+"]处理出现问题, 请查证, 问题描述为:" + message);
			} catch (Exception e) {
				logger.error("#doCheckAndSaveJinDanFiles: 系统错误, file["+fileName+"].");
			} finally {
				this.deleteFile(downLoadFolder, fileName); 								// 删除文件
				logger.info("#singleCheckAndSaveJinDanFiles success, file["+fileName+"], usedTime["+(System.currentTimeMillis() - start)+"].");
			}
		}
		totalAmount = files.length;
		logger.info("#doCheckAndSaveJinDanFiles:共有(" + totalAmount + ")个回盘文件, 其中共(" + correctHandledFileCount + ")个回盘文件存入本地数据库");
	}

	private void deleteFile(String path, String fileName){
		File file = new File(path + fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {

			} else {
				logger.error("#系统错误:从ftp服务器文件夹中删除文件失败, file[" + fileName+"].");
			}
		} else {
			logger.info("#removeFileFromFtp:  file["+ fileName+"] not exists");
		}
	}

	private void writeToFile(String path, File file){
		String fileName = file.getName();
		File saveFile = new File(path + fileName);
		if (saveFile.exists() && saveFile.isFile()) {
			logger.info("#"+path+"文件夹层中 " + fileName + "已存在！  不予存储");
		} else {
			try {
				FileUtils.copyFile(file, saveFile);
			} catch (IOException e) {
				logger.error("#系统错误;"+path+"文件夹中存入文件失败! file["+fileName+"]");
			}

		}
	}
	
	private void writeToFileForStorageFolder(String path,String doubtFolder, File file) throws CommonException {
		String fileName = file.getName();
		File saveFile = new File(path + fileName);
		if (saveFile.exists() && saveFile.isFile()) {
			this.writeToFile(doubtFolder, file);
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, path+"文件夹中已经存在文件[" + fileName+"]  请查证!");
		} else {
			try {
				FileUtils.copyFile(file, saveFile);
			} catch (IOException e) {
				throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "系统错误: 文件移动失败, file[" + fileName+"].");
			}

		}
	}
	
	
	/**
	 * 
	 * @param externalTradeBatchUuid  回盘文件uuid
	 * @param folder 存储目标文件夹
	 * @param contractNo  项目code
	 * @param externalBatchNo  批次号
	 * @return
	 */
	
	public boolean doReturnFtpFile(String externalTradeBatchUuid, String folder, String contractNo,
			String externalBatchNo) {
		List<ExternalTradeBatchDetail> details = externalTradeBatchDetailService
				.getDetailsByBatchUuid(externalTradeBatchUuid);
		if (CollectionUtils.isEmpty(details)) {
			return false;
		}
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(contractNo);
		if (financialContract == null) {
			logger.error("#doReturnFtpFile: 没有此回盘文件对应的金融项目, contractNo[" + contractNo + "],externalTradeBatchUuid[" + externalTradeBatchUuid+"].");
			return false;
		}
		BufferedWriter writer = null;
		String fileName = contractNo + "-" + externalBatchNo + "-" + DateUtils.getCurrentTimeMillis() + ".data";
		judeDirExistsForFolder(folder);
		File file = new File(folder + fileName);
		judeFileExists(file);
		try {
			writer = new BufferedWriter(new FileWriter(file, true));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("#doReturnFtpFile: 系统错误, 回盘文件数据明细生成上传至ftp失败, externalTradeBatchUuid[" + externalTradeBatchUuid+"].");
			return false;
		}

		Map<ExternalTradeType,List<String>> cashFlows=externalTradeBatchCashHandler.getExternalTradeBatchCashBankSequence(externalTradeBatchUuid);
		for (ExternalTradeBatchDetail externalTradeBatchDetail : details) {
			String externalTradeBatchDetailUuid=null;
			try {
				ExternalTradeType tradeType = externalTradeBatchDetail.getTradeType();
				List<String> cashFlowsList = cashFlows.get(tradeType);
				String cashFlow = StringUtils.join(cashFlowsList, ",");
				externalTradeBatchDetailUuid=externalTradeBatchDetail.getExternalTradeBatchDetailUuid();
				String financialContractName = financialContract.getContractName();
				String line = analyzeFactory.creatFileContent(externalTradeBatchDetail, financialContractName,
						cashFlow, CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
				writer.write(line);
				writer.newLine();
				writer.flush();
			} catch (CommonException e) {
				e.printStackTrace();
				logger.error("#doReturnFtpFile: 回盘文件明细数据生成上传至ftp失败，未知策略:externalTradeBatchDetailUuid[" + externalTradeBatchDetailUuid+"].");
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("#doReturnFtpFile: 回盘文件数据明细生成上传至ftp失败, 系统错误:externalTradeBatchUuid[" + externalTradeBatchUuid+"].");
				return false;
			}
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("#doReturnFtpFile: 系统错误, 文件输入流没有正常关闭!");
		}
		return true;	
	}
	
	private void judeDirExistsForFolder(String  folder) {
		File fileFolder = new File(folder);
		judeDirExists(fileFolder);
	}
	
	
	private void judeDirExists(File file) {

		if (!file.exists() && !file.isDirectory()) { 		// 目录不存在，需要创建
			logger.info("#dir not exists, create it");
			file.mkdirs();  									//创建目录
		}
	}
	
	// 判断文件是否存在
	private void judeFileExists(File file) {

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("#doReturnFtpFile: 文件创建失败, file["+file.getName()+"].");
			}
		}

	}

	
}
