package com.suidifu.owlman.microservice.enumation;

import com.zufangbao.sun.BasicEnum;
import org.apache.commons.lang.StringUtils;

public enum SourceDocumentDetailCheckState implements BasicEnum {
    /**
     * 未校验
     */
    UNCHECKED("enum.source-document-detail-check-state.unchecked"),
    /**
     * 校验失败
     */
    CHECK_FAILS("enum.source-document-detail-check-state.check-fails"),
    /**
     * 校验成功
     */
    CHECK_SUCCESS("enum.source-document-detail-check-state.check-success"),
    /**
     * 未提交
     */
    UNCOMMITTED("enum.source-document-detail-check-state.uncommitted");
    private String key;

    private SourceDocumentDetailCheckState(String key) {
        this.key = key;
    }

    public static SourceDocumentDetailCheckState fromKey(String key) {
        for (SourceDocumentDetailCheckState item : SourceDocumentDetailCheckState.values()) {
            if (StringUtils.equals(item.getKey(), key)) {
                return item;
            }
        }
        return null;
    }

    public static SourceDocumentDetailCheckState fromValue(int value) {
        for (SourceDocumentDetailCheckState item : SourceDocumentDetailCheckState.values()) {
            if (item.ordinal() == value) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getKey() {
        return key;
    }
}
