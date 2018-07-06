package com.zufangbao.earth.model.report;

import com.zufangbao.earth.report.wrapper.ExportableRowCallBackHandler;
import com.zufangbao.earth.report.wrapper.ExportableRowCallBackHandlerList;
import com.zufangbao.sun.entity.security.Principal;

import java.util.Date;
import java.util.UUID;

/**
 * 导出事件日志模型
 * 
 * @author zhanghongbing
 *
 */
public class ExportEventLogModel {

	/**
	 * 请求唯一编号
	 */
	private String requestId;
	
	/**
	 * 用户帐号
	 */
	private String userAccount;
	
	/**
	 * 报表编号
	 */
	private String reportId;
	
	/**
	 * 请求接收时间
	 */
	private Date requestAcceptTime;
	
	/**
	 * 开始加载数据时间
	 */
	private Date startLoadDataTime;
	
	/**
	 * 结束加载数据时间
	 */
	private Date endLoadDataTime;
	
	/**
	 * 加载数据条数
	 */
	private Integer[] loadDataSizes;
	
	/**
	 * 开始报表写出时间
	 */
	private Date startWriteOutTime;
	
	/**
	 * 结束报表写出时间
	 */
	private Date endWriteOutTime;
	
	/**
	 * 错误信息
	 */
	private String errorMsg;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public Date getRequestAcceptTime() {
		return requestAcceptTime;
	}

	public void setRequestAcceptTime(Date requestAcceptTime) {
		this.requestAcceptTime = requestAcceptTime;
	}

	public Date getStartLoadDataTime() {
		return startLoadDataTime;
	}

	public void setStartLoadDataTime(Date startLoadDataTime) {
		this.startLoadDataTime = startLoadDataTime;
	}

	public Date getEndLoadDataTime() {
		return endLoadDataTime;
	}

	public void setEndLoadDataTime(Date endLoadDataTime) {
		this.endLoadDataTime = endLoadDataTime;
	}
	
	public Integer[] getLoadDataSizes() {
		return loadDataSizes;
	}

	public void setLoadDataSizes(Integer... loadDataSizes) {
		this.loadDataSizes = loadDataSizes;
	}

	public Date getStartWriteOutTime() {
		return startWriteOutTime;
	}

	public void setStartWriteOutTime(Date startWriteOutTime) {
		this.startWriteOutTime = startWriteOutTime;
	}

	public Date getEndWriteOutTime() {
		return endWriteOutTime;
	}

	public void setEndWriteOutTime(Date endWriteOutTime) {
		this.endWriteOutTime = endWriteOutTime;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public void recordStartLoadDataTime() {
		this.startLoadDataTime = new Date();
	}
	
	public void recordEndLoadDataTime() {
		this.endLoadDataTime = new Date();
	}
	
	public void recordStartWriteOutTime() {
		this.startWriteOutTime = new Date();
	}
	
	public void recordEndWriteOutTime() {
		this.endWriteOutTime = new Date();
	}
	
	public String getLoadDataUsedTime() {
		if (this.startLoadDataTime != null && this.endLoadDataTime != null) {
			return (endLoadDataTime.getTime() - startLoadDataTime.getTime()) + "ms";
		}
		return "0ms";
	}
	
	public String getWriteOutUsedTime() {
		if (this.startWriteOutTime != null && this.endWriteOutTime != null) {
			return endWriteOutTime.getTime() - startWriteOutTime.getTime() + "ms";
		}
		return "0ms";
	}
	
	public String getTotalUsedTime() {
		if (this.requestAcceptTime != null && this.endWriteOutTime != null) {
			return endWriteOutTime.getTime() - requestAcceptTime.getTime() + "ms";
		}
		return "0ms";
	}
	
	public void recordAfterLoadDataComplete() {
		this.recordEndLoadDataTime();
		this.recordStartWriteOutTime();
	}
	
	public void recordAfterLoadDataComplete(Integer... loadDataSizes) {
		this.recordEndLoadDataTime();
		this.recordStartWriteOutTime();
		this.setLoadDataSizes(loadDataSizes);
	}
	
	public void recordAfterLoadDataComplete(ExportableRowCallBackHandler<?> callBack) {
		this.setEndLoadDataTime(callBack.getEndLoadDataTime());
		this.setStartWriteOutTime(callBack.getEndLoadDataTime());
		this.setLoadDataSizes(callBack.getResultSize());
	}
	
	public void recordAfterLoadDataComplete(ExportableRowCallBackHandlerList<?> callBack) {
		this.setEndLoadDataTime(callBack.getEndLoadDataTime());
		this.setStartWriteOutTime(callBack.getEndLoadDataTime());
		this.setLoadDataSizes(callBack.getResultSize());
	}
	
	public ExportEventLogModel() {
		super();
	}

	public ExportEventLogModel(String reportId, Principal principal) {
		super();
		this.requestId = UUID.randomUUID().toString();
		if(principal != null) {
			this.userAccount = principal.getName();
		}
		this.reportId = reportId;
		this.requestAcceptTime = new Date();
	}

}
