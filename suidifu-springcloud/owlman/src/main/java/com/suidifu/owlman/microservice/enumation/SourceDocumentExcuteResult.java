package com.suidifu.owlman.microservice.enumation;

import com.zufangbao.sun.BasicEnum;

/**
 * 收款单类型
 *
 * @author louguanyang
 */
public enum SourceDocumentExcuteResult implements BasicEnum {
    /**
     * 未成功
     */
    UNSUCCESS("enum.source-document-excute-result.unsuccess"),
    /**
     * 成功
     */
    SUCCESS("enum.source-document-excute-result.success");

    private String key;

    SourceDocumentExcuteResult(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
