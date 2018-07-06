package com.suidifu.dowjones.utils;

import com.suidifu.dowjones.Dowjones;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/4 <br>
 * @time: 15:12 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Dowjones.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Slf4j
public class FTPUtilsTest {
    @Resource
    private FTPUtils ftpUtils;
    private File file;
    private String path;

    @Before
    public void setUp() {
        file = new File("/Users/wujunshen/Downloads/csv/data.csv");
        path = "/csv1";
    }

    @After
    public void tearDown() {
        ftpUtils = null;
        file = null;
        path = null;
    }

    @Test
    public void uploadFile() {
        boolean flag = ftpUtils.uploadFile(file, path);

        assertThat(flag, equalTo(true));
    }

    @Test
    public void listFiles() {
        List<String> listFiles = ftpUtils.listFiles(path, Constants.CSV);

        for (String fileName : listFiles) {
            assertThat(fileName, containsString(Constants.CSV));
        }
    }
}