package com.zufangbao.earth.test.scripts;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.zufangbao.earth.BaseTest;
import com.zufangbao.sun.api.model.deduct.PaymentChannelAndSignUpInfo;
import com.zufangbao.sun.yunxin.entity.v2.SignUp;
import com.zufangbao.sun.yunxin.entity.v2.SignUpStatus;
import com.zufangbao.sun.yunxin.service.v2.SignUpService;

public class NormalSandboxDataSetHandlerTest extends BaseTest{
	
	@Resource(name="normalPreSandboxHandler")
	private SandboxDataSetHandler sandboxDataSetHandler;
	
	@Autowired
	private SignUpService signUpService;

	@Test
	public void testGetPaymentChannelAndSignUpInfoBy() {
		
		String financialContractCode = "11111111";
		
		String financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
		
		List<SignUp> signUpList = signUpService.getSignUps("范腾1111111", "6214855712106564", SignUpStatus.SUCCESS, financialContractCode);
		
		List<PaymentChannelAndSignUpInfo> paymentChannelAndSignUpInfos =  (List<PaymentChannelAndSignUpInfo>) sandboxDataSetHandler.getPaymentChannelAndSignUpInfoBy(financialContractUuid, signUpList);
		
		assertEquals(1,paymentChannelAndSignUpInfos.size());
	}

}
