package com.suidifu.matryoshka.productCategory;


import com.suidifu.hathaway.BasicEnum;

/**
 * 外部接口可用状态
 * @author fanxiaofan
 *
 */
public enum ProductCategoryStatus implements BasicEnum {
	/** 无效 */
	INVALID("enum.external-service-status.invalid"),
	/** 可用 */
	VALID("enum.external-service-status.valid");
	private String key;

	ProductCategoryStatus(String key) {
		this.key = key;
	}

	@Override
    public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getOrdinal() {

		return this.ordinal();
	}

	public static ProductCategoryStatus fromOrdinal(int ordinal) {

		for (ProductCategoryStatus item : ProductCategoryStatus.values()) {

			if (ordinal == item.getOrdinal()) {

				return item;
			}
		}
		return null;
	}

}
