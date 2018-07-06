package com.zufangbao.earth.cucumber.BatchQueryAssertPackage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dzz on 17-4-27.
 */
public class ThreadUtil {
        public static ExecutorService executorService = Executors.newFixedThreadPool(1000);
}
