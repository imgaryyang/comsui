package com.zufangbao.checkRepaymentOrder;

import com.zufangbao.aboutRepaymentOrder.CheckRepaymentOrderOneContract;
import com.zufangbao.aboutRepaymentOrder.CheckRepaymentOrderParams;
import com.zufangbao.aboutRepaymentOrder.CheckRepaymentOrderTwoContract;
import com.zufangbao.aboutRepaymentVerification.CheckOrderPayment;
import com.zufangbao.aboutRepaymentVerification.VerificationSmokingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CheckRepaymentOrderOneContract.class,
        CheckRepaymentOrderParams.class,
        CheckRepaymentOrderTwoContract.class,
        CheckOrderPayment.class,
        VerificationSmokingTest.class
})
public class RefactorRepaymentOrder {
}
