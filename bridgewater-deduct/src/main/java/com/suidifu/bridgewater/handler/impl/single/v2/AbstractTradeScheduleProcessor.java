package com.suidifu.bridgewater.handler.impl.single.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zufangbao.sun.yunxin.service.UnionpayBankConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhenghangbo on 02/05/2017.
 */
public abstract class AbstractTradeScheduleProcessor {



    @Autowired
    private UnionpayBankConfigService unionpayBankConfigService;



    public String assembleDestinationBankInfoMap(String standardBankCode,String provinceCode,String cityCode,String bankName) {
        Map<String, String> destinationBankInfoMap = new HashMap<String, String>();
        destinationBankInfoMap.put("bankCode", standardBankCode);
        destinationBankInfoMap.put("bankProvince", provinceCode);
        destinationBankInfoMap.put("bankCity",cityCode);
        destinationBankInfoMap.put("bankName", bankName);
        String destinationBankInfo = JSON.toJSONString(destinationBankInfoMap,
                SerializerFeature.DisableCircularReferenceDetect);
        return destinationBankInfo;
    }

    public String assembleDestinationAccountAppendixMap(String idCardNum, String mobile) {
        Map<String, String> destinationAccountAppendixMap = new HashMap<String, String>();
        destinationAccountAppendixMap.put("idNumber", idCardNum);
        destinationAccountAppendixMap.put("mobile", mobile);
        String destinationAccountAppendix = JSON.toJSONString(destinationAccountAppendixMap,
                SerializerFeature.DisableCircularReferenceDetect);
        return destinationAccountAppendix;
    }


    public String getFstGateWayRouterInfo(String bankCode,String standardBankCode, String clearingNo, String protocolNo) {

        String debitMode =  unionpayBankConfigService.isUseBatchDeduct(bankCode,standardBankCode)?"batchMode":"realTimeMode";


        Map<String,String> fstGateWayRouterInfoMap = new HashMap<String,String>();
        fstGateWayRouterInfoMap.put("debitMode",debitMode );
        if(StringUtils.isNotEmpty(clearingNo)) {
            fstGateWayRouterInfoMap.put("reckonAccount", clearingNo);
        }
        
        if(StringUtils.isNotEmpty(protocolNo)) {
        	fstGateWayRouterInfoMap.put("protocolNo", protocolNo);
        }

        return JSON.toJSONString(fstGateWayRouterInfoMap);
    }

}
