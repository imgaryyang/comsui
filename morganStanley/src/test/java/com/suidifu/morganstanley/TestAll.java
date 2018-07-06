package com.suidifu.morganstanley;

import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustFileTaskTest;
import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustPropertiesTest;
import com.suidifu.morganstanley.configuration.bean.zhonghang.ZhongHangPropertiesTest;
import com.suidifu.morganstanley.controller.BaseUnitTest;
import com.suidifu.morganstanley.controller.api.pre.FileUploadControllerTest;
import com.suidifu.morganstanley.controller.api.pre.ModifyRepaymentPlanPreControllerTest;
import com.suidifu.morganstanley.controller.api.repayment.ImportAssetPackageControllerTest;
import com.suidifu.morganstanley.controller.api.repayment.ModifyOverdueControllerTest;
import com.suidifu.morganstanley.controller.api.repayment.ModifyRepaymentInformationControllerTest;
import com.suidifu.morganstanley.controller.api.repayment.ModifyRepaymentPlanControllerTest;
import com.suidifu.morganstanley.controller.api.repayment.MutableFeeControllerTest;
import com.suidifu.morganstanley.controller.api.repayment.PrepaymentControllerTest;
import com.suidifu.morganstanley.controller.api.voucher.ActivePaymentVoucherControllerTest;
import com.suidifu.morganstanley.controller.api.voucher.BusinessPaymentVoucherControllerTest;
import com.suidifu.morganstanley.controller.api.voucher.ThirdPartyVoucherControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Author:frankwoo(吴峻申) <br>
 * Date:2017/9/27 <br>
 * Time:下午12:33 <br>
 * Mail:frank_wjs@hotmail.com <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		BaseUnitTest.class,
		ModifyRepaymentPlanPreControllerTest.class,
		ImportAssetPackageControllerTest.class,
		ModifyOverdueControllerTest.class,
		ModifyRepaymentInformationControllerTest.class,
		ModifyRepaymentPlanControllerTest.class,
		MutableFeeControllerTest.class,
		PrepaymentControllerTest.class,
		ActivePaymentVoucherControllerTest.class,
		BusinessPaymentVoucherControllerTest.class,
		ThirdPartyVoucherControllerTest.class,

		FileUploadControllerTest.class,

        YntrustFileTaskTest.class,
//        YntrustFileNotifyTest.class,
        YntrustPropertiesTest.class,
        ZhongHangPropertiesTest.class,
})
public class TestAll {
}

