package com.suidifu.bridgewater.api.model;

import java.util.Collections;
import java.util.List;

import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Oct 12, 2016 10:28:46 PM 
* 类说明 
*/
public class ReDeductDataPackage {

	
	private boolean isReDeduct;
	
	private DeductPlan  needReDeductPlan;
	


	public boolean isReDeduct() {
		return isReDeduct;
	}

	public void setReDeduct(boolean isReDeduct) {
		this.isReDeduct = isReDeduct;
	}

	
	public DeductPlan getNeedReDeductPlan() {
		return needReDeductPlan;
	}

	public void setNeedReDeductPlan(DeductPlan needReDeductPlan) {
		this.needReDeductPlan = needReDeductPlan;
	}

	public ReDeductDataPackage(){
		this.isReDeduct = false;
		this.needReDeductPlan = null;
	}
	
	public ReDeductDataPackage(Boolean isReduct,DeductPlan reDeductPlan){
		this.isReDeduct = isReduct;
		this.needReDeductPlan = reDeductPlan;
	}
	
	
}
