package com.suidifu.microservice.handler;

import com.suidifu.giotto.exception.GiottoException;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import java.util.List;

/**
 * @author louguanyang at 2018/3/14 11:24
 * @mail louguanyang@hzsuidifu.com
 */
public interface RepaymentPlanRecoverHandler {

    public void recover_received_in_advance(List<AssetSet> assetSets, String financialContractUuid);
    public void recover_received_in_advance(AssetSet assetSet) throws GiottoException;

}
