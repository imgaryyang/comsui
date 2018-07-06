/**
 * 
 */
package com.zufangbao.earth.cucumber.demo.steps;

import java.math.BigDecimal;

/**
 * @author wukai
 *
 */
public class Account {
	
	private BigDecimal accountBalance;
	
	private BigDecimal withdrawAmount;
	
	private BigDecimal remaingBalance;
	
	public Account(BigDecimal accountBalance) {
		super();
		this.accountBalance = accountBalance;
	}
	public Account(String accountBalanceStr) {
		this(new BigDecimal(accountBalanceStr));
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public BigDecimal getWithdrawAmount() {
		return withdrawAmount;
	}

	private void setWithdrawAmount(String withdrawAmountStr) {
		this.withdrawAmount = new BigDecimal(withdrawAmountStr);
	}
	public BigDecimal getReceivedAmount(){
		return accountBalance.compareTo(withdrawAmount) >= 0 ? withdrawAmount : BigDecimal.ZERO;
 	}
	public void withDraw(String withdrawAmountStr){
		setWithdrawAmount(withdrawAmountStr);
		changeReamingBalance();
		
	}
	private void changeReamingBalance(){
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				BigDecimal remaingBalance = getAccountBalance().subtract(getReceivedAmount());
				
				setRemaingBalance(remaingBalance);
			}
		}).start();
		
	}
	
	public BigDecimal getRemaingBalance(){
		return this.remaingBalance;
	}
	public void setRemaingBalance(BigDecimal remaingBalance){
		this.remaingBalance = remaingBalance;
	}
	
	

}
