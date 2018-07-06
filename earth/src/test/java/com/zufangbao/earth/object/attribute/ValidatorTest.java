package com.zufangbao.earth.object.attribute;

import com.zufangbao.sun.utils.AttributeAnnotationValidatorUtils;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherModel;
import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherRepayDetailModel;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;


public class ValidatorTest {

	
	@Test
	public void testObjectAttributeValidator(){
		ThirdPartVoucherModel model = new ThirdPartVoucherModel();
		model.setFn("30006");
		model.setFinancialContractNo("");
		model.setRequestNo("requesytno1");
		List<String> errorFieldsName = AttributeAnnotationValidatorUtils.getPointTypeErrorFieldsName(model, "String");
		
		Assert.assertEquals(1, errorFieldsName.size());
		Assert.assertEquals("financialContractNo", errorFieldsName.get(0));
	}	
	
	
	
	@Test
	public void testObjectAttributeValidatorAmount(){
		ThirdPartVoucherRepayDetailModel detailModel = new ThirdPartVoucherRepayDetailModel();
		detailModel.setPrincipal(new BigDecimal("1.00"));
		detailModel.setInterest(new BigDecimal("1.00"));
		detailModel.setLateFee(new BigDecimal("1.00"));
		detailModel.setAmount(new BigDecimal("1.00"));
		detailModel.setLateOtherCost(new BigDecimal("1.00"));
		detailModel.setMaintenanceCharge(new BigDecimal("1.00"));
		detailModel.setOtherCharge(new BigDecimal("1.00"));
		detailModel.setPenaltyFee(new BigDecimal("1.00"));
		detailModel.setServiceCharge(new BigDecimal("1.00"));
		//detailModel.setLatePenalty(new BigDecimal("1.00"));
		List<String> errorFieldsName = AttributeAnnotationValidatorUtils.getPointTypeErrorFieldsName(detailModel, "BigDecimal");
		Assert.assertEquals(1, errorFieldsName.size());
		Assert.assertEquals("latePenalty", errorFieldsName.get(0));
	}
	
}
