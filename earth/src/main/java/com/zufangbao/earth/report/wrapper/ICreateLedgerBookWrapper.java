package com.zufangbao.earth.report.wrapper;

import com.zufangbao.sun.exception.LedgerItemCreateException;

/**
 * Created by zj on 17-12-6.
 */
public interface ICreateLedgerBookWrapper {
    /***
     * 根据id名 以级你需要创建的表名的参数获得创建sql
     * @param id
     * @param tabelName
     * @return
     */
    public String getCreateSqlByTableName(String id,String tabelName) throws Exception;
}
