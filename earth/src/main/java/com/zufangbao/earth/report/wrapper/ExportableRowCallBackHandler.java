package com.zufangbao.earth.report.wrapper;

import com.zufangbao.earth.report.util.CSVUtil;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 支持导出的查询结果回调处理器
 * @author zhanghongbing
 *
 * @param <T>
 */
public class ExportableRowCallBackHandler<T> implements RowCallbackHandler {

	private PrintWriter printWriter;
	
	private ReportVOBuilder<T> reportVOBuilder;
	
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

	public ExportableRowCallBackHandler(Class<T> clazz,
			PrintWriter printWriter, ReportVOBuilder<T> reportVOBuilder) {
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
		T csvVo = reportVOBuilder.buildRow(rs);
		
		//写出单行数据
		printWriter.println(csvUtil.buildCsvSingleRowData(csvVo));
		
		if(rs.isLast()) {
			printWriter.flush();
		}

		resultSize++;
	}
	
}
