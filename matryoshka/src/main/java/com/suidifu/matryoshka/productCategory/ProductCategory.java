package com.suidifu.matryoshka.productCategory;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品类型配置表
 *
 * @author louguanyang
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "t_product_category")
public class ProductCategory {
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * uuid
	 */
	private String uuid;
	/**
	 * 一级产品目录代码
	 */
	private String productLv1Code;

	/**
	 * 一级产品目录名称
	 */
	private String productLv1Name;

	/**
	 * 二级产品目录代码
	 */
	private String productLv2Code;

	/**
	 * 二级产品目录名称
	 */
	private String productLv2Name;

	/**
	 * 三级产品目录代码
	 */
	private String productLv3Code;

	/**
	 * 三级产品目录名称
	 */
	private String productLv3Name;

	/**
	 * 前置接口URL
	 */
	private String preProcessInterfaceUrl;

	/**
	 * 后置接口URL
	 */
	private String postProcessInterfaceUrl;

	/**
	 * 外部业务逻辑脚本
	 */
	private String preProcessScript;

	/**
	 * 可用控制状态:1可用 0不可用
	 */
	@Enumerated(EnumType.ORDINAL)
	private ProductCategoryStatus status = ProductCategoryStatus.INVALID;
	/**
	 * 脚本MD5Version
	 */
	private String scriptMd5Version;

	/**
	 * 后置处理任务uuid
	 */
	private String delayTaskConfigUuid;

	/**
	 * 预留字段
	 **/
	private Date dateFieldOne;

	private Date dateFieldTwo;

	private Date dateFieldThree;

	private Long longFieldOne;

	private Long longFieldTwo;

	private Long longFieldThree;

	private String stringFieldOne;

	private String stringFieldTwo;

	private String stringFieldThree;

	private BigDecimal decimalFieldOne;

	private BigDecimal decimalFieldTwo;

	private BigDecimal decimalFieldThree;

	public ProductCategory(String productLv1Code, String productLv1Name, String productLv2Code, String productLv2Name,
	                       String productLv3Code, String productLv3Name, String preProcessInterfaceUrl, String
			                       preProcessScript, ProductCategoryStatus status, String scriptMd5Version) {
		super();
		this.productLv1Code = productLv1Code;
		this.productLv1Name = productLv1Name;
		this.productLv2Code = productLv2Code;
		this.productLv2Name = productLv2Name;
		this.productLv3Code = productLv3Code;
		this.productLv3Name = productLv3Name;
		this.preProcessInterfaceUrl = preProcessInterfaceUrl;
		this.preProcessScript = preProcessScript;
		this.status = null == status ? ProductCategoryStatus.INVALID : status;
		this.scriptMd5Version = scriptMd5Version;
	}

	@JSONField(serialize = false)
	public Product3lvl transfer() {
		try {
			return new Product3lvl(this.getProductLv1Code(), this.getProductLv2Code(), this.getProductLv3Code());
		} catch (Exception e) {
			e.printStackTrace();
			return new Product3lvl();
		}
	}
}
