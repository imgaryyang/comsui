package com.zufangbao.earth.report.wrapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 报表VO构建器
 * @author zhanghongbing
 *
 * @param <T> VO类
 */
public interface ReportVOBuilder<T> {
	
	public T buildRow(ResultSet rs) throws SQLException;

}
