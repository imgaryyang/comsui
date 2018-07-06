/**
 * 
 */
package com.suidifu.microservice.handler;

import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucher;
import com.zufangbao.sun.yunxin.entity.audit.ClearingVoucherStatus;
import com.zufangbao.sun.yunxin.entity.audit.TotalReceivableBills;
import java.util.List;

/**
 * @author hjl
 *
 */
public interface TotalReceivableBillsHandler {

	List<TotalReceivableBills> queryAllOfSameClearingVoucherByOne(TotalReceivableBills totalReceivableBills);
	
	ClearingVoucher queryClearingVoucherByTotalReceivableBills(TotalReceivableBills totalReceivableBills,ClearingVoucherStatus clearingVoucherStatus);
}
