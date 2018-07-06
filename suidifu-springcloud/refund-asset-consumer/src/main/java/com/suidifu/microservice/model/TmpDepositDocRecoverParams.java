package com.suidifu.microservice.model;

import java.io.Serializable;

public class TmpDepositDocRecoverParams implements Serializable{

	private static final long serialVersionUID = 2782216847012465643L;
	private boolean isFromTmpDepost=false;
	private String tmpDepositDocUuid;
	private String secondNo;
	public boolean isFromTmpDepost() {
		return isFromTmpDepost;
	}
	public void setFromTmpDepost(boolean isFromTmpDepost) {
		this.isFromTmpDepost = isFromTmpDepost;
	}
	public String getTmpDepositDocUuid() {
		return tmpDepositDocUuid;
	}
	public void setTmpDepositDocUuid(String tmpDepositDocUuid) {
		this.tmpDepositDocUuid = tmpDepositDocUuid;
	}
	public String getSecondNo() {
		return secondNo;
	}
	public void setSecondNo(String secondNo) {
		this.secondNo = secondNo;
	}
	
	
	public TmpDepositDocRecoverParams() {
		super();
	}
	public TmpDepositDocRecoverParams(boolean isFromTmpDepost, String tmpDepositDocUuid, String secondNo) {
		super();
		this.isFromTmpDepost = isFromTmpDepost;
		this.tmpDepositDocUuid = tmpDepositDocUuid;
		this.secondNo = secondNo;
	}
	
	public String getTmpDepositDocUuidFromTmpDepositRecover(){
		return isFromTmpDepost?tmpDepositDocUuid:"";
	}
	public String getSecondNoFromTmpDepositRecover(){
		return isFromTmpDepost?secondNo:"";
	}
}
