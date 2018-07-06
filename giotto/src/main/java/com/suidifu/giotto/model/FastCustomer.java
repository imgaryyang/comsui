/**
 * 
 */
package com.suidifu.giotto.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.giotto.keyenum.FastCustomerKeyEnum;
import com.suidifu.giotto.keyenum.FastKey;
import com.suidifu.giotto.util.SqlAndParamTuple;
import com.zufangbao.gluon.opensdk.Md5Util;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lute
 *
 */

public class FastCustomer extends FastCacheObject {

	private Long id;
	
	private String customerUuid;
	/**
	 * 客户姓名
	 */
	private String name;
	
	private String mobile;
	/**
	 * 客户身份证号
	 */
	private String account;
	/**
	 * 客户编号
	 */
	private String source;
	/**
	 * 所属商户
	 */
	private long appId;
	
	/**
	 * 客户类型:0. 个人 ;1. 公司 
	 */
	private Integer customerType;

	@Override
	public String obtainAddCacheKey() throws GiottoException {
		if (StringUtils.isBlank(getCustomerUuid()) && StringUtils.isBlank(getAccount())) {
			throw new GiottoException("all keys value is null.");
		}
		String result = FastCustomerKeyEnum.PREFIX_KEY;
		if (StringUtils.isBlank(getCustomerUuid())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getCustomerUuid()).concat(":");
		}
		if (StringUtils.isBlank(getAccount())) {
			result = result.concat(":");
		} else {
			result = result.concat(this.getAccount());
		}
		return result;
	}

	@Override
	public List<String> obtainAddCacheKeyList() {
		return new ArrayList<String>() {{
			if (StringUtils.isNotBlank(getCustomerUuid())) {
				add(FastCustomerKeyEnum.PREFIX_KEY.concat(getCustomerUuid()));
			}
			if (StringUtils.isNotBlank(getAccount())) {
				add(FastCustomerKeyEnum.PREFIX_KEY.concat(getAccount()));
			}
		}};
	}

	@Override
	public SqlAndParamTuple obtainInsertSqlAndParam() {
		String addSql = "insert into customer (id, account, mobile, name, source," +
				"app_id, customer_uuid, customer_type) values (" +
				":id, :account, :mobile, :name, :source," +
				":appId, :customerUuid, :customerType)";
		String fastJson = JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss",SerializerFeature.WriteMapNullValue);
        return new SqlAndParamTuple(addSql, JsonUtils.parse(fastJson));
	}

	@Override
	public String obtainQueryCheckMD5Sql(String updateSql) {
		int startIndex = updateSql.indexOf("where");
		String selectSql = "select count(1) from customer ".concat(updateSql.substring(startIndex, updateSql.length()));
		String cName = StringUtils.isBlank(this.getName()) ? "" : this.getName();
		String cMobile = StringUtils.isBlank(this.getMobile()) ? "" : this.getMobile();
		String cAccount = StringUtils.isBlank(this.getAccount()) ? "" : this.getAccount();
		String cType = this.getCustomerType() == null ? "" : this.getCustomerType().toString();

		String md5Val = Md5Util.encode(cName.concat(cMobile).concat(cAccount).concat(cType));
		String md5Sql = "MD5(concat(IFNULL(name,''),IFNULL(mobile,''),IFNULL(account,''),IFNULL(customer_type,'')))";
		return selectSql.concat(" and '").concat(md5Val).concat("'=").concat(md5Sql);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(String customerUuid) {
		this.customerUuid = customerUuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	@Override
	public FastKey getColumnName() {
		return FastCustomerKeyEnum.CUSTOMER_UUID;
	}

	@Override
	public String getColumnValue() {
		return customerUuid;
	}

	public FastCustomer(Long id, String customerUuid, String name, String mobile, String account, String source,
			long appId, Integer customerType) {
		super();
		this.id = id;
		this.customerUuid = customerUuid;
		this.name = name;
		this.mobile = mobile;
		this.account = account;
		this.source = source;
		this.appId = appId;
		this.customerType = customerType;
	}

	public FastCustomer() {
	}
}
