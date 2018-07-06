package com.zufangbao.earth.yunxin.api.handler.impl;

import com.zufangbao.earth.yunxin.api.handler.TradeListApiHandler;
import com.zufangbao.earth.yunxin.api.model.query.AccountTradeDetailModel;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.service.FinancialContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.Objects;

/**
 * Created by whb on 17-7-3.
 */
 public abstract class TradeListApiHandlerImpl implements TradeListApiHandler {
     @Value("#{config['tradeListQuerySizeOfPerPage']}")
     private int sizeOfPerPage;

    @Autowired
    protected FinancialContractService financialContractService;


    @Override
    public Integer verifyAccountTradeDetailModel(AccountTradeDetailModel accountTradeDetailModel){
        if (null == accountTradeDetailModel){
            return ApiResponseCode.WRONG_FORMAT;
        }

        int checkError = accountTradeDetailModel.hasError();
        if (! Objects.equals(0, checkError)){
             return checkError;
        }
        if (accountTradeDetailModel.hasErrorBetweenStartAndEnd()){
            return ApiResponseCode.START_TIME_MUST_LESS_THAN_END_TIME;
        }
        return 0;
    }


   
    public Boolean ifHasNextPage(Collection collection){
        return collection.size() > sizeOfPerPage;
    }

    public int getSizeOfPerPage() {
        return sizeOfPerPage;
    }

}
