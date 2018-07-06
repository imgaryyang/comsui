package com.zufangbao.earth.yunxin.handler.reportform;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.zufangbao.sun.yunxin.entity.model.reportform.OperationDataExportModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.OperationDataQueryModel;

public interface OperationReportFormHandler {

//	List<OperationalDataPageShowModel> query(OperationDataQueryModel queryModel, Page page);

	Map<String, Object> queryTotalAmount(OperationDataQueryModel queryModel);

	HSSFWorkbook buildExcel(OperationDataExportModel exportModel);

}
 