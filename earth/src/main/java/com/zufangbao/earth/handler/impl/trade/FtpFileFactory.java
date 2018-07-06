package com.zufangbao.earth.handler.impl.trade;

import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.trade.ExternalTradeBatchDetailModel;
import com.zufangbao.sun.handler.trade.ExternalTradeBatchHandler;
import com.zufangbao.sun.service.FinancialContractService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import static com.zufangbao.earth.handler.impl.trade.ExternalTradeFileAnalyzeFactory.*;

@Component("ftpFileFactory")
public class FtpFileFactory {

	public static long TOTAL_FILE_COUNT = 0;
	public static long CORRECT_HANDLEED_FILE_COUNT = 0;
	public static long DOUBT_FILE_COUNT = 0;
	@Autowired
	private ExternalTradeBatchHandler externalTradeBatchHandler;
	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private ExternalTradeFileAnalyzeFactory analyzeFactory;

	@Autowired
	private FileAnalyzaFactory fileAnalyzaFactory;

	private static final Log logger = LogFactory.getLog(FtpFileFactory.class);

	// 业务逻辑处理
	public void checkAndRemoveFile(FTPFile[] ftpFiles, FTPClient ftpClient, String localFolder, String doubtFolder,
			String uploadFolder, String transforFolder) {
		OutputStream os = null;
		long correctHandledFileCount = 0;
		long doubtFileCount = 0;

		for (FTPFile file : ftpFiles) {
			String fileName = file.getName();
			try {
				// InputStream ins = ftpClient.retrieveFileStream(fileName);
				// url url=new
				// URL("ftpfile/filePath/jinDan/uploadFolder/"+fileName);
				// URL url = new
				// URL("ftp://192.168.0.204/ftpfile/filePath/jinDan/uploadFolder/"
				// + fileName);
				URL url = new URL(
						"ftp://userftp:develop69fc@192.168.0.204/ftpfile/filePath/jinDan/uploadFolder/" + fileName);
				URLConnection con = url.openConnection();
				InputStream ins = con.getInputStream();
				this.moveToFileFolder(ftpClient, transforFolder, fileName, ins); // 转移文件到集中存储文件夹下
				Map<String, String> fileInfos = analyzeFactory.analyzeFileName(fileName,
						CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
				String productNo = fileInfos.get(CONST_FILE_INFO_PRODUCT_CODE);
				String batchNo = fileInfos.get(CONST_FILE_INFO_BATCH_NO);
				String tradeDate = fileInfos.get(CONST_FILE_INFO_TRADE_DATE);
				FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(productNo);

				if (financialContract == null) {
					this.moveToFileFolder(ftpClient, doubtFolder, fileName, ins); // 转移文件到doubtFolder文件夹下
					logger.error("与回盘文件相对应的项目不存在  请查证  项目code为:!" + productNo);
					doubtFileCount++;
					continue;
				}

				List<ExternalTradeBatchDetailModel> list = analyzeFactory.analyzeDetail(ins,
						CONST_ANALYZE_STRATEGY_SPECIAL_JINDAN);
				if (CollectionUtils.isEmpty(list)) { // 异常 文件转移到doubtFolder目录下
					this.moveToFileFolder(ftpClient, doubtFolder, fileName, ins); // 转移文件到doubtFolder文件夹下
					logger.error("回盘文件解析出现异常 请查看回盘文件中的数据格式 回盘文件为" + fileName);
					doubtFileCount++;
					continue;
				}
				if (this.moveFileToStorageFolder(ftpClient, localFolder, fileName, ins, os)) {
					externalTradeBatchHandler.importDataForExternalTradeBatch(list, productNo, batchNo, null,
							null, tradeDate); // 保存至数据库
					correctHandledFileCount++;
				}
			} catch (Exception e) {
				String message = e.getMessage();
				logger.error("回盘文件处理出现问题  请查证 问题描述为:!" + message);
			} finally {
				try {
					this.deleteFile(ftpClient, uploadFolder, fileName); // 删除文件
					if (os != null) {
						os.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("输出流没有正常关闭 原因为" + e.getMessage());
				}

			}
		}
		TOTAL_FILE_COUNT = ftpFiles.length;
		CORRECT_HANDLEED_FILE_COUNT = correctHandledFileCount;
		DOUBT_FILE_COUNT = doubtFileCount;

	}

	// 转移文件
	/**
	 * 
	 * @param ftpClient
	 *            Ftp连接实例
	 * @param pathname
	 *            转移至目标文件夹
	 * @param ins
	 *            输出流
	 * @throws CommonException
	 */
	public void moveToFileFolder(FTPClient ftpClient, String pathname, String fileName, InputStream ins)
			throws CommonException {
		ftpClient = this.openFtp("192.168.0.204", 21, "userftp", "develop69fc");
		ftpClient.setControlEncoding("UTF-8");
		try {
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.makeDirectory(pathname);
			ftpClient.changeWorkingDirectory(pathname);
			FTPFile[] ftpFiles = ftpClient.listFiles(fileName);
			long length = ftpFiles.length;
			if (length != 0) {
				logger.error("ftp文件移动失败,,,,,文件已经存在 文件名为" + fileName);
			} else {
				ftpClient.storeFile(fileName, ins);
			}
		} catch (Exception e) {
			logger.error("ftp文件移动失败,,,,,未知原因 文件名为:" + fileName);
		}
	}

	public boolean moveFileToStorageFolder(FTPClient ftpClient, String path, String fileName, InputStream ins,
			OutputStream os) {
		boolean result = true;
		try {

			FTPFile[] files = this.getFtpFiles(ftpClient, path);
			for (FTPFile ftpFile : files) {
				if (StringUtils.equals(fileName, ftpFile.getName())) {
					result = false;
				}
			}
			if (result) {
				File localFile = new File(path + "/" + fileName);
				os = new FileOutputStream(localFile);
				ftpClient.retrieveFile(fileName, os); // 下载文件到本地目录下
				os.flush();
				os.close();
			}
			return result;
		} catch (Exception e) {
			logger.error("回盘文件保存到本地失败  文件名为:" + fileName);
			return false;
		}

	}

	// 读取文件
	/**
	 * 
	 * @param ftpClient
	 *            Ftp连接实例
	 * @param pathname
	 *            获取文件目标文件夹
	 * @return ftp文件实例集合
	 * @throws CommonException
	 */
	public FTPFile[] getFtpFiles(FTPClient ftpClient, String pathname) throws CommonException {
		try {
			ftpClient.enterLocalActiveMode();
			ftpClient.changeWorkingDirectory(pathname); // 切换FTP目录
			FTPFile[] ftpFiles = ftpClient.listFiles();
			return ftpFiles;
		} catch (IOException e) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "ftp文件获取失败,,,,," + e.getMessage());
		}

	}

	/**
	 * 
	 * @param hostname
	 *            服务器地址
	 * @param port
	 *            端口号
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return ftpClient ftp连接实例
	 * @throws CommonException
	 */
	public FTPClient openFtp(String hostname, int port, String username, String password) throws CommonException {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(hostname, port); // 连接FTP服务器
			ftpClient.login(username, password); // 登录FTP服务器
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				flag = true;
			}
		} catch (IOException e) {
			flag = true;
		}
		if (flag == true) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "ftp服务连接失败,,,,,请查看服务器状态!");
		}
		return ftpClient;
	}

	/**
	 * 
	 * @param ftpClient
	 *            ftp服务器连接实例
	 * @param pathname
	 *            要删除文件的所在文件夹
	 * @param filename
	 *            文件名
	 * @throws CommonException
	 */
	public void deleteFile(FTPClient ftpClient, String pathname, String filename) throws CommonException {
		try {
			ftpClient.enterLocalActiveMode();
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.deleteFile(pathname);
		} catch (Exception e) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "ftp文件删除失败,,,,,未知原因! 文件名为:" + filename);
		}
	}

}
