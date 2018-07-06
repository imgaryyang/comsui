package com.suidifu.matryoshka.cache;


import java.io.Serializable;

import com.suidifu.matryoshka.productCategory.ProductCategory;

/**
 * 产品类型配置Cache
 * Created by louguanyang on 2017/4/26.
 */
public class ProductCategoryCache implements Serializable {
    private static final long serialVersionUID = 5405531981307548362L;
    private ProductCategory productCategory;
    private long timeStamp;

    public ProductCategoryCache() {
        super();
    }

    public ProductCategoryCache(ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.timeStamp = System.currentTimeMillis();
    }

    public ProductCategoryCache(ProductCategory productCategory, long timeStamp) {
        this.productCategory = productCategory;
        this.timeStamp = timeStamp;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    private boolean isExpired(long timeout) {
        return (System.currentTimeMillis() - timeStamp) > timeout;
    }

    public boolean needUpdate(long timeout) {
        return isExpired(timeout);
    }
}
