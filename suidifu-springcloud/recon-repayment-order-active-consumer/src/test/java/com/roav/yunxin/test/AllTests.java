package com.roav.yunxin.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by MieLongJun on 18-3-8.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestForActive.class,
        TestForLedgerBookV2HandlerImpl.class,
        TestForUpdateAssetStatusLedgerBookHandler.class,
        TestForVirtualAccountHandler.class
})
public class AllTests {
}
