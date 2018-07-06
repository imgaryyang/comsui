package com.zufangbao.earth;

import com.zufangbao.earth.web.controller.api.CommandApiControllerTest;
import com.zufangbao.earth.yunxin.api.AssetSetApiHandlerTest;
import com.zufangbao.earth.yunxin.api.RepaymentInformationApiHandlerTest;
import com.zufangbao.earth.yunxin.handler.PrepaymentHandlerTest;
import com.zufangbao.earth.yunxin.handler.impl.ContractApiHandlerTest;
import com.zufangbao.earth.yunxin.handler.impl.InterestReportFormHandlerTest;
import com.zufangbao.earth.yunxin.handler.impl.LoansReportFormHandlerTest;
import com.zufangbao.earth.yunxin.service.PrepaymentApplicationServiceTest;
import com.zufangbao.earth.yunxin.service.UpdateAssetLogServiceTest;
import com.zufangbao.earth.yunxin.web.ModifyApiControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	UpdateAssetLogServiceTest.class,
	ContractApiHandlerTest.class,
	ModifyApiControllerTest.class,
	AssetSetApiHandlerTest.class,
	CommandApiControllerTest.class,
//	ImportAssetPackageLogServiceTest.class,
	RepaymentInformationApiHandlerTest.class,
	PrepaymentApplicationServiceTest.class,
	PrepaymentHandlerTest.class,
	LoansReportFormHandlerTest.class,
	InterestReportFormHandlerTest.class,
	})
public class AllTestOfWangYuQiInEarth {

}
