package com.zufangbao.earth.task;


import com.zufangbao.earth.handler.impl.trade.FileAnalyzaFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("externalTradeImportForJinDanTask")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExternalTradeImportForJinDanTask extends BasicTask {

	private final static String FILE_DOWNLOAD_FLODER = "/download/";
	private final static String FILE_STORAGE_FLODER = "/storage/";
	private final static String FILE_DOUBT_FLODER = "/doubt/";

	private final static String PARAM_FILE_PATH = "filePath";
	private final static String PARAM_PRODUCT_Code = "productCode";

	@Autowired
	private FileAnalyzaFactory fileAnalyzaFactory;


	private static final Log logger = LogFactory.getLog(ExternalTradeImportForJinDanTask.class);

	@Override
    public void run() {
		String filePath = this.getWorkParam(PARAM_FILE_PATH);
		String productCode = this.getWorkParam(PARAM_PRODUCT_Code);
		String downLoadFolder = filePath + productCode + FILE_DOWNLOAD_FLODER;
		String storageFolder = filePath + productCode + FILE_STORAGE_FLODER;
		String doubtFolder = filePath + productCode + FILE_DOUBT_FLODER;
		try{
			long start = System.currentTimeMillis();
			logger.info("#"+getTaskId()+" start ");
			fileAnalyzaFactory.doCheckAndSaveJinDanFiles(storageFolder, downLoadFolder, doubtFolder);
			logger.info("#"+getTaskId()+" success, usedTime["+(System.currentTimeMillis() - start)+"].");
		}catch(Exception e){
			logger.error("#"+getTaskId()+": 回盘文件导入时数据验真发生异常！ 系統错误");
			e.printStackTrace();
		}
	}

}
