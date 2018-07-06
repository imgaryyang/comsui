package com.zufangbao.earth.web.controller.system;

import com.demo2do.core.entity.Result;
import com.demo2do.core.web.resolver.Page;
import com.zufangbao.sun.api.model.systemlogs.QuerySystemLogModel;
import com.zufangbao.sun.api.model.systemlogs.SystemOperateLogModel;
import com.zufangbao.sun.utils.JsonUtils;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * 系统日志页面查询测试
 * @author wjh on 17-12-11.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {
        "classpath:/local/applicationContext-*.xml",
        "classpath:/DispatcherServlet.xml" })
@WebAppConfiguration(value="webapp")
public class LogsControllerTest {
    private static Log log = LogFactory.getLog(LogsControllerTest.class);
    @Autowired
    private LogsController logsController;

    @Test
    public void test(){
        System.out.println("hello, world");
    }

    @Test
    @Sql("classpath:test/yunxin/systemOperateLog/testSystemLogOperate.sql")
    public void testQueryLogsOperators(){
        Page page = new Page();
        page.setBeginIndex(0);
        page.setEveryPage(10);
        QuerySystemLogModel model = new QuerySystemLogModel();
//        model.setUserName("zhushiyun");

        String logs = logsController.queryLogsOperators(page, model,true);
        log.info("logs is: " + logs);
        Assert.assertTrue(logs.contains("list"));
        Assert.assertTrue(logs.contains("size"));

        Result result = JsonUtils.parse(logs,Result.class);
        Assert.assertNotNull(result);
        Map<String,Object> objectMap =  result.getData();
        List<SystemOperateLogModel> list = (List<SystemOperateLogModel>)objectMap.get("list");
        Assert.assertTrue(CollectionUtils.isNotEmpty(list));
        Assert.assertEquals(4, objectMap.get("size"));
    }

    @Test
    @Sql("classpath:test/yunxin/systemOperateLog/testSystemLogOperate.sql")
    public void testGetMenuOptions(){
        String result = logsController.getMenuOptions();
        log.info("result is: " + result);

        Assert.assertTrue(result.contains("companies"));
        Assert.assertTrue(result.contains("logOperateTypes"));
    }
}
