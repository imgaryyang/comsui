package com.zufangbao.earth.api;

import com.zufangbao.earth.api.exception.UnknownUkeyException;

import java.util.List;

/**
 * 
 * @author zjm
 *
 */
public interface ISuperBankHandler {

	/**
	 * 批量贷记
	 * @param creditedDetailList
	 * @return
	 * @throws ResponseParseException 
	 * @throws UnknownUkeyException 
	 */
    List<CreditedResult> batchCredited(List<CreditedModel> creditedDetailList) throws ResponseParseException, UnknownUkeyException;

    /**
	 * 查询交易
	 * @param queryTransactionModel
	 * @return
	 * @throws ResponseParseException 
	 * @throws UnknownUkeyException 
	 */
    List<QueryTransactionResult> queryTransaction(QueryTransactionModel queryTransactionModel) throws ResponseParseException, UnknownUkeyException;
}
