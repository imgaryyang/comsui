package com.suidifu.owlman.microservice.enumation;

/**
 * @author zjm
 */
public enum SourceDocumentType {

    /**
     * 执行
     */
    EXECUTE("enum.sourceDocument-type.execute"),

    /**
     * 通知
     */
    NOTIFY("enum.sourceDocument-type.notify");

    private String key;

    SourceDocumentType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getAlias() {
        return this.key.substring(this.key.lastIndexOf(".") + 1);
    }
}
