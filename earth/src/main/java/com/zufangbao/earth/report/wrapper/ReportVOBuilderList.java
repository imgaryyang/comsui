package com.zufangbao.earth.report.wrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 报表VO构建器
 * @author ww
 *
 * @param <T> VO类
 */
public interface ReportVOBuilderList<T> {
	
	public List<T> buildRow(ResultSet rs) throws SQLException;

}
