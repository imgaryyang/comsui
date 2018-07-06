package com.zufangbao.earth.model;

public class CustomizeScriptModel {
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
	 * 后置处理任务uuid
	 */
	private String delayTaskConfigUuid;

	/**
	 * 编译导入依赖包
	 */
	private String compileImport;

	public String getProductLv1Code() {
		return productLv1Code;
	}

	public void setProductLv1Code(String productLv1Code) {
		this.productLv1Code = productLv1Code;
	}

	public String getProductLv1Name() {
		return productLv1Name;
	}

	public void setProductLv1Name(String productLv1Name) {
		this.productLv1Name = productLv1Name;
	}

	public String getProductLv2Code() {
		return productLv2Code;
	}

	public void setProductLv2Code(String productLv2Code) {
		this.productLv2Code = productLv2Code;
	}

	public String getProductLv2Name() {
		return productLv2Name;
	}

	public void setProductLv2Name(String productLv2Name) {
		this.productLv2Name = productLv2Name;
	}

	public String getProductLv3Code() {
		return productLv3Code;
	}

	public void setProductLv3Code(String productLv3Code) {
		this.productLv3Code = productLv3Code;
	}

	public String getProductLv3Name() {
		return productLv3Name;
	}

	public void setProductLv3Name(String productLv3Name) {
		this.productLv3Name = productLv3Name;
	}

	public String getPreProcessInterfaceUrl() {
		return preProcessInterfaceUrl;
	}

	public void setPreProcessInterfaceUrl(String preProcessInterfaceUrl) {
		this.preProcessInterfaceUrl = preProcessInterfaceUrl;
	}

	public String getPostProcessInterfaceUrl() {
		return postProcessInterfaceUrl;
	}

	public void setPostProcessInterfaceUrl(String postProcessInterfaceUrl) {
		this.postProcessInterfaceUrl = postProcessInterfaceUrl;
	}

	public String getDelayTaskConfigUuid() {
		return delayTaskConfigUuid;
	}

	public void setDelayTaskConfigUuid(String delayTaskConfigUuid) {
		this.delayTaskConfigUuid = delayTaskConfigUuid;
	}

	public String getCompileImport() {
		return compileImport;
	}

	public void setCompileImport(String compileImport) {
		this.compileImport = compileImport;
	}

}
