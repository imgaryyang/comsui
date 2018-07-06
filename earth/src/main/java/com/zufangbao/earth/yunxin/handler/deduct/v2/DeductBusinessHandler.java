package com.zufangbao.earth.yunxin.handler.deduct.v2;

import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;

/**
 * @author wukai
 *
 */
public interface DeductBusinessHandler {
	
	public void handleDeductBusiness(String preProcessUrl,DeductRequestModel deductRequestModel);

}
