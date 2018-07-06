package com.zufangbao.earth.report.wrapper;

import com.zufangbao.earth.report.util.CSVUtil;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 支持导出的查询结果回调处理器
 * @author ww
 *
 * @param <T>
 */
public class ExportableRowCallBackHandlerList<T> implements RowCallbackHandler {

	private PrintWriter printWriter;
	
	private ReportVOBuilderList<T> reportVOBuilder;
	
	private CSVUtil<T> csvUtil;
	
	private int resultSize;
	
	private Date endLoadDataTime;
	
	public Integer getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	
	public Date getEndLoadDataTime() {
		return endLoadDataTime;
	}

	public void setEndLoadDataTime(Date endLoadDataTime) {
		this.endLoadDataTime = endLoadDataTime;
	}

	public ExportableRowCallBackHandlerList(Class<T> clazz,
			PrintWriter printWriter, ReportVOBuilderList<T> reportVOBuilder) {
		super();
		this.reportVOBuilder = reportVOBuilder;
		this.printWriter = printWriter;
		csvUtil = new CSVUtil<T>(clazz);
		
		//写出csv头部
		printWriter.println(csvUtil.buildCsvHeader());
		printWriter.flush();
	}

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		if(rs.isFirst()) {
			this.endLoadDataTime = new Date();
			//数据加载完进行冲刷
			printWriter.flush();
		}
		
		//单行数据VO构建接口
		List<T> csvVoList = reportVOBuilder.buildRow(rs);
		
		//写出单行数据
		for(T csvVo:csvVoList){
			printWriter.println(csvUtil.buildCsvSingleRowDataV2(csvVo));
			resultSize++;
		}
		
		if(rs.isLast()) {
			printWriter.flush();
		}
		
	}
	
}
