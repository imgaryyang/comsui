package com.zufangbao.earth.yunxin.api.v2;

import com.zufangbao.earth.yunxin.api.BaseApiController;
import com.zufangbao.sun.utils.v2.MD5SignUtils;
import com.zufangbao.sun.yunxin.entity.DictionaryCode;
import com.zufangbao.wellsfargo.api.util.ApiSignUtils;
import com.zufangbao.wellsfargo.dictionary.handler.DictionaryHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class RSABaseApiController extends  BaseApiController{


    public static final String EMPTY = "";


    @Autowired
    private DictionaryHandler dictionaryHandler;

    private static final Log logger = LogFactory.getLog(RSABaseApiController.class);





    public String signResult(Object object) {

        try {
            Map<String, String> signedContentMap = MD5SignUtils.objectToMap(object);
            return signContentAndReturnResult(signedContentMap);
        } catch (Exception e) {
            logger.info("sign error");
            e.printStackTrace();
            return EMPTY;
        }
    }




    public String signResult(Map<String,String> signedContentMap) {

        try {
            return signContentAndReturnResult(signedContentMap);
        } catch (Exception e) {
            logger.info("sign error");
            e.printStackTrace();
            return EMPTY;
        }
    }

    private String signContentAndReturnResult(Map<String, String> signedContentMap) throws Exception {
        String signedContent = MD5SignUtils.sortParamsToSign(signedContentMap);
        String privateKey = dictionaryHandler.getPointPrivateKey(DictionaryCode.PLATFORM_PRI_KEY);
        String signData = ApiSignUtils.rsaSign(signedContent, privateKey);
        return  MD5SignUtils.appendSignData(signedContent, signData);
    }


}
