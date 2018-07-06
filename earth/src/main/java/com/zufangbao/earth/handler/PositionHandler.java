package com.zufangbao.earth.handler;

import java.util.List;
import java.util.Map;

import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.yunxin.entity.Position;
import com.zufangbao.sun.yunxin.entity.model.PositionDetailModel;
import com.zufangbao.sun.yunxin.entity.model.PositionQueryModel;

public interface PositionHandler {

	Map<String, Object> queryPosition(PositionQueryModel queryModel, Page page);

	List<PositionDetailModel> getPositionDetailModel(List<Position> positions);

	String createPosition(List<PositionDetailModel> positionDetailList, String financialContractUuid);

	String deletePosition(List<Position> positions);

}
