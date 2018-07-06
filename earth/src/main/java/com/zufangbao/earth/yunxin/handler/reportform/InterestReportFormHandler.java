package com.zufangbao.earth.yunxin.handler.reportform;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.model.reportform.InterestQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.InterestShowModel;

import java.util.List;

public interface InterestReportFormHandler {

	List<InterestShowModel> query(InterestQueryModel queryModel, Page page);
	
}
