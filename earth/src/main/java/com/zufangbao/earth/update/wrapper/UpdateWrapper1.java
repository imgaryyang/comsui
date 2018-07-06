package com.zufangbao.earth.update.wrapper;

import com.zufangbao.earth.report.util.FreemarkerUtil;
import com.zufangbao.earth.update.model.UpdateWrapperModel;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.utils.GeneratorUtils;
import com.zufangbao.sun.yunxin.entity.api.VoucherSource;
import com.zufangbao.sun.yunxin.entity.api.VoucherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.zufangbao.earth.update.util.UpdateConfiguration.UPDATE_CODES_WRAPPER_FILE_MAPPER;

@Component
public class UpdateWrapper1 extends IUpdateBaseWraper implements IUpdateWrapper<UpdateWrapperModel> {

	private final String selectCashFlow = "cashFlow";
	private final String selectSourceDocument = "sourceDocument";
	private final String selectContract = "contract";

	private final String insertTVoucher = "tVoucher";
	private final String insertSourceDocumentDetail = "sourceDocumentDetail";
	
	@Autowired
	private UpdateSqlCacheManager updateSqlCacheManager;
	
	@Override
	public String wrap(UpdateWrapperModel paramsBean) throws Exception {
		try {
			StringBuffer montageSql = new StringBuffer();
			String uuid = UUID.randomUUID().toString();
			String firstNo = UUID.randomUUID().toString();
			String voucherNo = GeneratorUtils.generateVoucherNo();
			
			
			Map<String, Object> sqlMap = updateSqlCacheManager.getSqlParam().get(UPDATE_CODES_WRAPPER_FILE_MAPPER.get("1"));
			Map<String, Object> slSqlMap = this.getParamMap(paramsBean);
			slSqlMap.put("contractId",Arrays.asList(paramsBean.getContractId().split(",")));
			slSqlMap.put("uuid",uuid);
			slSqlMap.put("voucherNo",voucherNo);
			slSqlMap.put("firstNo",firstNo);
			
			//从模板中获取insert语句
			String insertSql1 = (String)sqlMap.get(insertTVoucher);
			String insertSql2 = (String)sqlMap.get(insertSourceDocumentDetail);

			//从模板中获取查询语句进行填充和执行
			List<Map<String, Object>> param1 = getSql(selectCashFlow, slSqlMap, sqlMap);
			if (null == param1 || 0 == param1.size()) {
				return null;
			}
			slSqlMap.putAll(param1.get(0));
			
			//从模板中获取查询语句进行填充和执行
			List<Map<String, Object>> param2 =getSql(selectSourceDocument, slSqlMap, sqlMap);
			if (null == param2 || 0 == param2.size()) {
				return null;
			}
			slSqlMap.putAll(param2.get(0));
			slSqlMap.put("firstType", VoucherSource.BUSINESS_PAYMENT_VOUCHER.getKey());
			slSqlMap.put("secondType", VoucherType.REPURCHASE.getKey());
			
			//从模板中获取查询语句进行填充和执行
			montageSql.append(FreemarkerUtil.process(insertSql1, slSqlMap));
			List<Map<String, Object>> param3 = getSql(selectContract, slSqlMap, sqlMap);
			if (null == param3 || 0 == param3.size()) {
				return null;
			}
			for (Map<String, Object> map : param3) {
				slSqlMap.putAll(map);
				slSqlMap.put("uuid1", UUID.randomUUID().toString());
				montageSql.append(FreemarkerUtil.process(insertSql2, slSqlMap));
			}

			return montageSql.toString();

		} catch (Exception e) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, e.getMessage());
		}
	}

}
