package com.zufangbao.earth.yunxin.handler.reportform;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.model.reportform.LoansQueryModel;
import com.zufangbao.sun.yunxin.entity.model.reportform.LoansShowModel;

import java.util.List;

public interface LoansReportFormHandler {

	List<LoansShowModel> query(LoansQueryModel queryModel, Page page);

}
