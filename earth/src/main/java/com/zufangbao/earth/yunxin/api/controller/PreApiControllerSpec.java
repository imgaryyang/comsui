package com.zufangbao.earth.yunxin.api.controller;

/**
 * 前置接口Spec
 * @author louguanyang on 2017/5/18.
 */
public class PreApiControllerSpec {
    static final String PRE_API = "/pre/api/";
    private static final String CHANNEL_CODE_SERVICE_CODE = "/{channelCode}/{serviceCode}";
    static final String PRE_URL_MODIFY_REPAYMENT_PLAN = "modify-repaymentPlan" + CHANNEL_CODE_SERVICE_CODE;
    static final String PRE_URL_REPURCHASE = "repurchase" + CHANNEL_CODE_SERVICE_CODE;
    static final String PRE_URL_UPLOAD_FILE = "upload" + CHANNEL_CODE_SERVICE_CODE;
}
