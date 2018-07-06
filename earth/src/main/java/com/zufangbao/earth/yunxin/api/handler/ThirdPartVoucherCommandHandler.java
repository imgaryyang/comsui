package com.zufangbao.earth.yunxin.api.handler;

import com.zufangbao.sun.yunxin.entity.model.thirdPartVoucher.ThirdPartVoucherModel;
import com.zufangbao.sun.yunxin.entity.thirdPartVoucher.ThirdPartyVoucherCommandLog;

import java.util.Map;

public interface ThirdPartVoucherCommandHandler {

	void commandRequestLogicValidate(ThirdPartVoucherModel model);

	/**
	 * 第三方凭证唯一性校验
	 * 重传更新以及保存历史明细至文件
	 * @date 17.6.29
	 */
    Map<String, Object> generateThirdPartVoucherCommandLog(ThirdPartVoucherModel model, String ipAddress);
	
	/**
	 * 获得历史第三方还款明细列表
	 * @date 17.6.30
	 * ZFJ
	 */
    Map<String,Object> getThirdPartVoucherRepayDetailList(String tradeUuid);
	
	/**
	 * 读取确定版本的还款明细
	 * ZFJ
	 */
    ThirdPartyVoucherCommandLog redThirdPartVoucherRepayDetail(String treadUuid, String versionNo);
	/**
	 *  读取明细及凭证
	 *  ZFJ
	 */
    Map<String,Object> getRepayDetailAndVoucher(String tradeUuid, String versionName);
	/**
	 * 保存历史明细
	 * 更新明细
	 * ZFJ
	 */
    void saveHistoryLogAndUpdateLog(ThirdPartyVoucherCommandLog log, String tradeuuid);

	/**
	 *  重新核销校验
	 *  ZFJ
	 */
    Map<String, Object> reLoadThirdPartVoucherCommandLog(String treadUuid);
}
