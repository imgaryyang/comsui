package com.zufangbao.earth.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.zufangbao.sun.yunxin.entity.Position;

public interface PositionService extends GenericService<Position>{

	List<Position> getPositionListBy(String financialContractUuid);

	List<Position> getPositionListByName(String PositionName);

}
