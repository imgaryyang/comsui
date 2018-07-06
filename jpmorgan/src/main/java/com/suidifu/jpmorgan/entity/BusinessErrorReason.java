package com.suidifu.jpmorgan.entity;

/**
 * Created by zsh2014 on 17-5-8.
 */
public enum BusinessErrorReason {
    /** 余额不足 **/
    INSUFFICIENTBALANCE("enum.business-error-reason.insufficientbalance"),
    /** 银行系统维护中 **/
    SYSTEMMAINTAINING("enum.business-error-reason.systemmaintaining"),
    /** 扣款银行卡号错误 **/
    CARDNOERROR("enum.business-error-reason.cardnoerror"),
    /** 其他 **/
    OTHERS("enum.business-error-reason.others");
    private String key;

    /**
     * @param key
     */
    private BusinessErrorReason(String key) {
        this.key = key;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Get alias of order status
     *
     * @return
     */
    public String getAlias() {
        return this.key.substring(this.key.lastIndexOf(".") + 1);
    }


    public static BusinessErrorReason fromValue(int value){

        for(BusinessErrorReason item : BusinessErrorReason.values()){
            if(item.ordinal() == value){
                return item;
            }
        }
        return null;
    }
}
