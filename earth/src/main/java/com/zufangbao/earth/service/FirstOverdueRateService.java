package com.zufangbao.earth.service;

import com.demo2do.core.service.GenericService;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.entity.firstOverdueRate.FirstOverdueRate;
import com.zufangbao.sun.entity.firstOverdueRate.FirstOverdueRateShowModel;

import java.util.Date;
import java.util.List;

/**
 * Created by zxj on 2018/3/19.
 */
public interface FirstOverdueRateService extends GenericService<FirstOverdueRate> {

    List<FirstOverdueRate> queryList(String uuids, Date date, Page page);

    int countList(String uuids, Date date);

    void update(String uuid, Date date, String userName);

    List<FirstOverdueRate> showHistory(String uuid, Date date);

    FirstOverdueRate queryFirstOverdueRate(String uuids, Date date);

}
