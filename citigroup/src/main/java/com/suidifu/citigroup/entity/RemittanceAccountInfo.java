package com.suidifu.citigroup.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemittanceAccountInfo {
	private String firstAccountName;

	private String firstAccountUuid;

	private String secondAccountName;

	private String secondAccountUuid;

	private String thirdAccountName;

	private String thirdAccountUuid;
	
    private int accountSide;

}
