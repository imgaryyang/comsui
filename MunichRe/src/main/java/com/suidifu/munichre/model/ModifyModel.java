package com.suidifu.munichre.model;

import java.math.BigDecimal;
import java.util.Date;

public class ModifyModel {
	private BigDecimal delta_suc_amount = BigDecimal.ZERO;
	private int delta_suc_num= 0;
	private int delta_fail_num = 0;
	private BigDecimal flag = BigDecimal.ZERO;
	private Date complete_time;
	
	public BigDecimal getDelta_suc_amount() {
		return delta_suc_amount;
	}
	public void setDelta_suc_amount(BigDecimal delta_suc_amount) {
		this.delta_suc_amount = delta_suc_amount;
	}
	public int getDelta_suc_num() {
		return delta_suc_num;
	}
	public void setDelta_suc_num(int delta_suc_num) {
		this.delta_suc_num = delta_suc_num;
	}
	public int getDelta_fail_num() {
		return delta_fail_num;
	}
	public void setDelta_fail_num(int delta_fail_num) {
		this.delta_fail_num = delta_fail_num;
	}
	public BigDecimal getFlag() {
		return flag;
	}
	public void setFlag(BigDecimal flag) {
		this.flag = flag;
	}
	public Date getComplete_time() {
		return complete_time;
	}
	public void setComplete_time(Date complete_time) {
		this.complete_time = complete_time;
	}
	
	public ModifyModel() {
		super();
	}
	
	public ModifyModel(Date complete_time) {
		super();
		this.complete_time = complete_time;
	}
	public void before_succ(BigDecimal planTotalAmount){
		flag.subtract(BigDecimal.ONE);
		delta_suc_num--;
		delta_suc_amount = delta_suc_amount.subtract(planTotalAmount);
	}
	public void before_fail(){
		delta_fail_num--;
	}
	public void after_succ(BigDecimal planTotalAmount){
		flag = flag.add(BigDecimal.ONE);
		delta_suc_num++;
		delta_suc_amount = delta_suc_amount.add(planTotalAmount);
	}
	public void after_fail(){
		delta_fail_num++;
	}
	public boolean to_save() {
		return complete_time!=null && isChanged();
				
	}
	
	public boolean isChanged(){
		return delta_suc_amount.compareTo(BigDecimal.ZERO)!=0 
				|| delta_suc_num!=0 || delta_fail_num!=0 
				|| flag.compareTo(BigDecimal.ZERO)!=0;
	}
	
	
	
	
	
}
