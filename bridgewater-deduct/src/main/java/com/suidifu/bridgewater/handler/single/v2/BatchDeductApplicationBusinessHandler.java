package com.suidifu.bridgewater.handler.single.v2;

/**
 * Created by qinweichao on 2017/9/5.
 */
public interface BatchDeductApplicationBusinessHandler {

    public void updateBatchDeductApplicationAccordingPartDeductApplication(String batchDeductApplicationUuid);

    public void updateBatchDeductApplicationByUuid(String batchDeductApplicationUuid,int actualCount);

    public void updateBatchDeductApplicationSetCountFail(String batchDeductApplicationUuid, int failCount);

}
