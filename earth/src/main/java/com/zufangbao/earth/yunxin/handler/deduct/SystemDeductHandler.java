package com.zufangbao.earth.yunxin.handler.deduct;

import com.demo2do.core.entity.Result;
import com.zufangbao.gluon.api.bridgewater.deduct.model.DeductInfoModel;
import com.zufangbao.sun.yunxin.entity.OrderSource;

import java.io.Serializable;
import java.util.List;

/**
 * 系统扣款处理
 * @author zhanghongbing
 *
 */
public interface SystemDeductHandler {

	/**
	 * 获取结算单id
	 * 1、结算日为计划还款日
	 * 2、系统允许扣款（计划还款日）
	 */
    List<Long> getTodayWaitingDeductOrderOnPlannedRepaymentDate(OrderSource orderSource);

    /**
	 * 获取结算单id
	 * 1、结算日为计划还款日后（逾期）
	 * 2、系统允许扣款（物理逾期日起）
	 */
    List<Long> getTodayWaitingDeductOrderAfterPlannedRepaymentDate(OrderSource orderSource);

    /**
	 * 获取结算单id
	 * 1、结算日为提前还款日
	 */
    List<Long> getTodayWaitingDeductOrderForPrepaymentApplication(OrderSource orderSource);

    /**
	 * 获取结算单id
	 * 1、手工生成且未执行的结算单
	 */
    List<Long> getTodayEffectiveUnexecutedManualNormalOrder();

    /**
	 * 获取扣款信息模型（系统扣款用）
	 * @param orderId 结算单Id
	 */
    DeductInfoModel getDeductInfoModelBy(Long orderId);

    /**
	 * 判断是否存在处理中的扣款申请
	 * @param orderId 结算单id
	 */
    boolean existSuccessOrProcessingDeductApplication(Long orderId);

    /**
	 * 系统扣款通信完成后，更新日志&结算单状态
	 * @param orderId 结算单id
	 */
    void updateDeductResultAfterDeductProcessing(Long orderId, Serializable logId, Result result);

	/**
	 * 获取未结清，处理中的结算单（系统扣款）
	 */
    List<Long> getUnclearAndProcessingNormalOrderListForSystemDeduct();

    /**
	 * 同步处理中的结算单（系统扣款）
	 * @param orderId 结算单id
	 */
    void syncProcessingNormalOrderForSystemDeduct(Long orderId);

}
