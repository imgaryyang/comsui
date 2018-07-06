package morganStanley.task;

import com.suidifu.morganstanley.tasks.BQFileCreateTask;
import com.suidifu.morganstanley.MorganStanley;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by hwr on 17-11-29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@ActiveProfiles(value = "test")
public class BQFileCreateTaskTest {
    @Autowired
    private BQFileCreateTask bqFileCreateTask;

    @Autowired
    private FinancialContractConfigurationService financialContractConfigurationService;
    @Test
    @Sql("classpath:test/task/createFileTaskTest.sql")
    public void test(){
        bqFileCreateTask.BQCreateFileV2();
    }

    @Test
    public void CompressedFiles_Gzip() throws IOException {
        String filePath = new ClassPathResource("/test/task/test/test.txt").getFile().getParentFile()
                .getPath();
        System.out.println(filePath);
        BQFileCreateTask.CompressedFiles_Gzip(filePath, filePath+".tar", filePath+".tar.gz");
    }
}