package com.zufangbao.earth.yunxin.api.handler;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.model.deduct.ThirdPartVoucherDeductShowModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherDetailShowModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherQueryModel;
import com.zufangbao.wellsfargo.thirdpartvoucher.model.ThirdPartVoucherQueryShowModel;

import java.util.List;

/** 
* @author 作者 zhenghangbo
* @version 创建时间：Nov 23, 2016 4:30:40 PM 
* 类说明 
*/
public interface ThirdPartVoucherFrontEndShowHandler {

	int countThirdVocuherSize(ThirdPartVoucherQueryModel queryModel);

	List<ThirdPartVoucherQueryShowModel> getThirdPartVoucherShowModel(ThirdPartVoucherQueryModel queryModel, Page page);

	ThirdPartVoucherDetailShowModel fetch_detail_show_model(String journalVoucherUuid);
	
	List<ThirdPartVoucherDeductShowModel> fetchThirdPartVoucherFrontEndShowModelByDeductInformation(String deductApplicationUuid);
}
