package com.suidifu.owlman.microservice.enumation;

import org.apache.commons.lang.StringUtils;

/**
 * @author wukai
 */
public enum Currency {

    /**
     * 人民币
     */
    CNY("enum.currency.cny"),

    /**
     * 美元
     */
    USD("enum.currency.usd"),;

    private String key;

    Currency(String key) {

        this.key = key;
    }

    public static Currency fromName(String name) {

        for (Currency item : Currency.values()) {

            if (StringUtils.equalsIgnoreCase(name, item.name())) {

                return item;
            }
        }
        return null;
    }

    public static Currency fromValue(int value) {

        for (Currency item : Currency.values()) {

            if (value == item.ordinal()) {

                return item;
            }
        }
        return null;
    }

    public String getValue() {

        return this.getKey().substring(this.getKey().lastIndexOf(".") + 1);
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return this.name();
    }

}
