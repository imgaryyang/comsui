package com.zufangbao.earth.yunxin.handler;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.zufangbao.earth.yunxin.api.dataSync.task.DataSyncTask;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.wellsfargo.silverpool.cashauditing.entity.voucher.JournalVoucher;
import com.zufangbao.wellsfargo.silverpool.cashauditing.service.JournalVoucherService;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.DataSyncHandler;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by wq on 17-5-17.
 */



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {

        "classpath:/local/applicationContext-*.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class DataSyncHandlerTest {


    @Autowired
    private DataSyncHandler dataSyncHandler;

    @Autowired
    private JournalVoucherService journalVoucherService;

    @Autowired
    private DataSyncTask dataSyncTask;

    @Autowired
    protected GenericDaoSupport genericDaoSupport;

    @Test
    @Sql("classpath:test/yunxin/prepayment/test_data_sync_handler.sql")
    public void test_data_sync_handler(){
        dataSyncTask.fillHandleMissingJV();
    }

    @Test
    @Sql("classpath:test/yunxin/dataSyncHandler/test_getJournalVoucherIdByTimeAndId.sql")
    public void test_getJournalVoucherIdByTimeAndId(){
        List<String> list = journalVoucherService.getJournalVoucherIdByTimeAndId(213953, DateUtils.asDay("2017-07-01"), DateUtils.asDay("2017-07-19"));
        assertEquals(true,CollectionUtils.isNotEmpty(list));
        assertEquals(2,list.size());

        assertEquals("0fceb038-37a1-416e-ad6a-e57f0c11d524",list.get(0));
        assertEquals("3455935d-da98-4044-a1bd-c96acae9c0f4",list.get(1));
        for(String s : list){
            System.out.println(s);
        }
    }

    @Test
    public void test_getJournalVoucherIdByTimeAndIdForNull(){
        List<String> list = journalVoucherService.getJournalVoucherIdByTimeAndId(213953, DateUtils.asDay("2017-07-01"), null);
        assertEquals(true,CollectionUtils.isEmpty(list));
    }

    @Test
    @Sql("classpath:test/yunxin/dataSyncHandler/test_getJournalVoucherIdByTimeAndIdForEmpty.sql")
    public void test_getJournalVoucherIdByTimeAndIdForEmpty(){
        List<String> list = journalVoucherService.getJournalVoucherIdByTimeAndId(213953, DateUtils.asDay("2017-08-14"), DateUtils.asDay("2017-08-17"));
        assertEquals(true,CollectionUtils.isEmpty(list));
    }

    @Test
    @Sql("classpath:test/yunxin/dataSyncHandler/test_getCsvSyncData.sql")
    public void test_getCsvSyncJVData() throws IOException {
        List<String> list = dataSyncHandler.getCsvSyncJVData(DateUtils.asDay("2017-08-14"),DateUtils.asDay("2017-08-17"), "/tmp");
        assertEquals(1,list.size());
        String path = list.get(0);
        File file = new File(path);
        List<String> values = FileUtils.readLines(file, "utf-8");
        assertEquals("ZC1992699837763481600,,,2017-07-01 17:28:14,拍拍贷测试,500.00,3455935d-da98-4044-a1bd-c96acae9c0f4",values.get(1));
    }

    @Test
    @Sql("classpath:test/yunxin/dataSyncHandler/test_updateJournalVoucherHasDataSyncLog.sql")
    public void test_updateJournalVoucherHasDataSyncLog()throws Exception{
        long id = 214460;
        String jvUuid = "48348411-e705-4970-b477-97ef23e29bbd";
        journalVoucherService.updateJournalVoucherHasDataSyncLog(jvUuid);

        JournalVoucher journalVoucher = this.genericDaoSupport.load(JournalVoucher.class, id);
        Date date = new Date();
        assertNotNull(journalVoucher);

        assertEquals(214460 + "", journalVoucher.getId() + "");
        assertEquals(1 + "", journalVoucher.getIsHasDataSyncLog() + "");
        assertTrue(date.after(journalVoucher.getLastModifiedTime()));
    }

    @Test
    @Sql("classpath:test/yunxin/dataSyncHandler/data_sync.sql")
    public void test_getCsvSyncJVDataForEveryDay() throws IOException {
        List<String> fileNameList = this.dataSyncHandler.getCsvSyncJVDataForEveryDay(com.demo2do.core.utils.DateUtils.asDay("2017-08-15"),com.demo2do.core.utils.DateUtils.asDay("2017-08-17"),"/tmp");
        System.out.println(fileNameList);
        assertEquals(2, fileNameList.size());
        String path = fileNameList.get(0);
        File file = new File(path);
        List<String> values = FileUtils.readLines(file, "utf-8");
        assertEquals("ZC94839532562599936,,,2017-08-15 00:00:00,拍拍贷测试,10.00,d314f4cd-2d4c-481b-83e0-18c59bb7dcc9",values.get(1));
        assertEquals("ZC92635511481880576,,,2017-08-15 12:22:01,fgz测试,1000.00,5694d862-c065-4d5f-8d0f-8d88c715531c",values.get(11));
    }

    @Test
    @Sql("classpath:test/yunxin/dataSyncHandler/test_getCsvSyncData.sql")
    public void test_getCsvSyncData() throws IOException {
        List<String> fileNameList = this.dataSyncHandler.getCsvSyncJVDataForEveryDay(com.demo2do.core.utils.DateUtils.asDay("2017-08-15"),com.demo2do.core.utils.DateUtils.asDay("2017-08-17"),"/tmp");
        System.out.println(fileNameList);
        assertEquals(2, fileNameList.size());
        String path = fileNameList.get(0);
        File file = new File(path);
        List<String> values = FileUtils.readLines(file, "utf-8");
        assertEquals("ZC2749BA4A53D5AED5,,,2017-08-15 11:51:02,用钱宝测试,0.10,cf9839e1-5c3b-4914-87ea-a8f4637e2104", values.get(1));
        path = fileNameList.get(1);
        file = new File(path);
        values = FileUtils.readLines(file, "utf-8");
        assertEquals("ZC94887009758924800,,,2017-08-16 00:00:00,拍拍贷测试,980.00,4126c042-6d72-4394-88c4-40fa8287196e", values.get(1));
    }
}
