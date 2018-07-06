package com.suidifu.owlman.microservice.enumation;

public enum PaymentCondition {
    还款订单支付成功("0"), 还款订单支付成功但是流水无法自动充值到虚户("1"), 还款订单支付成功流水自动充值到虚户中("2");
    private String code;

    private PaymentCondition(String code) {
        this.code = code;
    }

    public static PaymentCondition fromCode(String code) {
        for (PaymentCondition p : PaymentCondition.values()) {
            if (p.getCode().equals(code)) {
                return p;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
