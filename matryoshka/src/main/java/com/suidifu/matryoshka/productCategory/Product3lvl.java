package com.suidifu.matryoshka.productCategory;

import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.utils.StringUtils;

public class Product3lvl {
    private String lv1Code;
    private String lv2Code;
    private String lv3Code;

    public Product3lvl() {
        super();
    }

    public Product3lvl(String lv1Code, String lv2Code, String lv3Code) {
        this.lv1Code = lv1Code;
        this.lv2Code = lv2Code;
        this.lv3Code = lv3Code;
    }

    public String getLv1Code() {
        return lv1Code;
    }

    public String getLv2Code() {
        return lv2Code;
    }

    public String getLv3Code() {
        return lv3Code;
    }

    public boolean isEmpty() {
        try {
            return StringUtils.isEmpty(lv1Code) && StringUtils.isEmpty(lv2Code) && StringUtils.isEmpty(lv3Code);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String shortName() {
        return lv1Code + FilenameUtils.LOG_SPLIT + lv2Code + FilenameUtils.LOG_SPLIT + lv3Code;
    }
}
