package com.zufangbao.earth.kaptcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VerificationCodeConfigProperties {
	
	private @Value("#{verificationCodeConfig['isOpen']}") String isOpen;
	
	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
}
