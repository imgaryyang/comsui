package morganStanley.handler.impl;

import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.handler.impl.ModifyRepaymentPlanBatchProcessHandlerImpl;
import com.suidifu.morganstanley.servers.FileRepositoryRedisPersistence;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.files.FileProcessStatus;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;
import com.zufangbao.sun.yunxin.entity.files.FileSendStatus;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import com.zufangbao.sun.yunxin.service.files.FileRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.zufangbao.sun.yunxin.entity.files.FileExecuteStatus.UNEXECUTE;
import static com.zufangbao.sun.yunxin.entity.files.FileProcessStatus.PROCESSED;
import static com.zufangbao.sun.yunxin.entity.files.FileSendStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class ModifyRepaymentPlanBatchProcessHandlerImplTest {

    @Autowired
    @Qualifier("ModifyRepaymentPlanBatchProcessHandlerImpl")
    private ModifyRepaymentPlanBatchProcessHandlerImpl spdBankBatchProcessHandler;

    @Autowired
    private TMerConfigService tMerConfigService;

    @Autowired
    private FileRepositoryService fileRepositoryService;

    @Autowired
    @Qualifier("FileRepositoryRedisPersistence")
    private FileRepositoryRedisPersistence fileRepositoryRedisPersistence;

    @Test
    @Sql("classpath:test/file/test_verifySign.sql")
    public void verifySign() throws Exception {
        String filePath = new ClassPathResource("/data/signal_file/repaymentPlan_test_20170717_123456_verify_succ.txt").getFile().getAbsolutePath();

        FileRepository fileRepository = new FileRepository();
        fileRepository.setPath(filePath);

        boolean verifySign = spdBankBatchProcessHandler.verifySign(fileRepository, tMerConfigService);
        Assert.assertTrue(verifySign);
    }

    @Test
    public void test_verifySignUpdateProcessStatus_error() {
        FileRepository fileRepository = null;
        String error_msg_1 = "fileRepository is null.";
        String error_msg_2 = "fileRepository 非(待处理)的, 跳过验签.";
        try {
            spdBankBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);
            Assert.fail();
        } catch (MorganStanleyException e) {
            Assert.assertEquals(error_msg_1, e.getMessage());
        }

        //已处理
        try {
            fileRepository = new FileRepository();
            fileRepository.setProcessStatus(FileProcessStatus.PROCESSED.getCode());

            spdBankBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);
            Assert.fail();
        } catch (MorganStanleyException e) {
            Assert.assertEquals(error_msg_2, e.getMessage());
        }
    }

    @Test
    @Sql("classpath:test/file/test_verifySignUpdateProcessStatus_verifySign_fail.sql")
    public void test_verifySignUpdateProcessStatus_verifySign_fail() throws IOException {
        String filePath = new ClassPathResource("/data/signal_file/repaymentPlan_test_20170717_123456_verify_fail.txt").getFile().getAbsolutePath();

        String uuid = UUIDUtil.random32UUID();

        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setPath(filePath);
        fileRepository.setProductCode("G31700");
        fileRepository.setProcessStatus(FileProcessStatus.UNPROCESSED.getCode());

        try {
            spdBankBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);

            Assert.assertEquals(PROCESSED.getCode(), fileRepository.getProcessStatus());
            Assert.assertEquals(ABANDON.getCode(), fileRepository.getSendStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepository.getExecuteStatus());

            FileRepository fileRepositoryInDB = fileRepositoryService.get_by_uuid(uuid);
            Assert.assertEquals(uuid, fileRepositoryInDB.getUuid());
            Assert.assertEquals(ABANDON.getCode(), fileRepositoryInDB.getSendStatus());
            Assert.assertEquals(PROCESSED.getCode(), fileRepositoryInDB.getProcessStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepositoryInDB.getExecuteStatus());
        } catch (MorganStanleyException e) {
            Assert.fail();
        }

    }

    @Test
    @Sql("classpath:test/file/test_verifySignUpdateProcessStatus_verifySign_succ.sql")
    public void test_verifySignUpdateProcessStatus_verifySign_succ() throws IOException {
        String filePath = new ClassPathResource("/data/signal_file/repaymentPlan_test_20170717_123456_verify_succ.txt").getFile().getAbsolutePath();

        String uuid = UUIDUtil.random32UUID();
        int tradeSize = 3;

        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setPath(filePath);
        fileRepository.setProductCode("G31700");
        fileRepository.setProcessStatus(FileProcessStatus.UNPROCESSED.getCode());

        try {
            spdBankBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);

            Assert.assertEquals(UNSEND.getCode(), fileRepository.getSendStatus());
            Assert.assertEquals(PROCESSED.getCode(), fileRepository.getProcessStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepository.getExecuteStatus());
            Assert.assertEquals(tradeSize, fileRepository.getTradeSize().intValue());

            FileRepository fileRepositoryInDB = fileRepositoryService.get_by_uuid(uuid);
            Assert.assertEquals(uuid, fileRepositoryInDB.getUuid());
            Assert.assertEquals(PROCESSED.getCode(), fileRepositoryInDB.getProcessStatus());
            Assert.assertEquals(UNSEND.getCode(), fileRepositoryInDB.getSendStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepositoryInDB.getExecuteStatus());
            Assert.assertEquals(tradeSize, fileRepositoryInDB.getTradeSize().intValue());
        } catch (MorganStanleyException e) {
            Assert.fail();
        }
    }

    @Test
    @Sql("classpath:test/file/test_verifySignUpdateProcessStatus_verifySign_empty.sql")
    public void test_verifySignUpdateProcessStatus_verifySign_empty() throws IOException {
        String filePath = new ClassPathResource("/data/signal_file/repaymentPlan_test_20170717_123456_verify_empty.txt").getFile().getAbsolutePath();

        String uuid = UUIDUtil.random32UUID();

        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setPath(filePath);
        fileRepository.setProductCode("G31700");
        fileRepository.setProcessStatus(FileProcessStatus.UNPROCESSED.getCode());

        try {
            spdBankBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);

            Assert.assertEquals(PROCESSED.getCode(), fileRepository.getProcessStatus());
            Assert.assertEquals(ABANDON.getCode(), fileRepository.getSendStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepository.getExecuteStatus());

            FileRepository fileRepositoryInDB = fileRepositoryService.get_by_uuid(uuid);
            Assert.assertEquals(uuid, fileRepositoryInDB.getUuid());
            Assert.assertEquals(ABANDON.getCode(), fileRepositoryInDB.getSendStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepositoryInDB.getExecuteStatus());
            Assert.assertEquals(PROCESSED.getCode(), fileRepositoryInDB.getProcessStatus());
        } catch (MorganStanleyException e) {
            Assert.fail();
        }
    }

    @Test
    public void test_noNeedSend() {
        String error_msg_1 = "fileRepository is null.";
        try {
            spdBankBatchProcessHandler.noNeedSend(null);
            Assert.fail();
        } catch (MorganStanleyException e) {
            Assert.assertEquals(error_msg_1, e.getMessage());
        }

        String error_msg_2 = "fileRepository 非(已处理, 未发送)的, 跳过发送.";
        try {
            FileRepository fileRepository1 = new FileRepository();
            fileRepository1.setProcessStatus(FileProcessStatus.UNPROCESSED.getCode());
            fileRepository1.setSendStatus(FileSendStatus.UNSEND.getCode());
            spdBankBatchProcessHandler.noNeedSend(fileRepository1);
            Assert.fail();
        } catch (MorganStanleyException e) {
            Assert.assertEquals(error_msg_2, e.getMessage());
        }

        try {
            FileRepository fileRepository2 = new FileRepository();
            fileRepository2.setProcessStatus(FileProcessStatus.PROCESSED.getCode());
            fileRepository2.setSendStatus(FileSendStatus.SEND.getCode());
            spdBankBatchProcessHandler.noNeedSend(fileRepository2);
            Assert.fail();
        } catch (MorganStanleyException e) {
            Assert.assertEquals(error_msg_2, e.getMessage());
        }

        try {
            FileRepository fileRepository3 = new FileRepository();
            fileRepository3.setProcessStatus(FileProcessStatus.PROCESSED.getCode());
            fileRepository3.setSendStatus(FileSendStatus.ABANDON.getCode());
            spdBankBatchProcessHandler.noNeedSend(fileRepository3);
            Assert.fail();
        } catch (MorganStanleyException e) {
            Assert.assertEquals(error_msg_2, e.getMessage());
        }

        try {
            FileRepository fileRepository4 = new FileRepository();
            fileRepository4.setProcessStatus(FileProcessStatus.PROCESSED.getCode());
            fileRepository4.setSendStatus(FileSendStatus.UNSEND.getCode());
            spdBankBatchProcessHandler.noNeedSend(fileRepository4);
        } catch (MorganStanleyException e) {
            Assert.fail();
        }
    }

    @Test
    public void test_sendToNotifyServer_empty() throws IOException {
        String filePath = new ClassPathResource("/data/signal_file/repaymentPlan_test_20170717_123456_verify_empty.txt").getFile().getAbsolutePath();

        String uuid = UUIDUtil.random32UUID();

        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setPath(filePath);
        fileRepository.setProductCode("G31700");
        fileRepository.setProcessStatus(FileProcessStatus.PROCESSED.getCode());
        fileRepository.setSendStatus(FileSendStatus.UNSEND.getCode());

        try {
            spdBankBatchProcessHandler.sendToNotifyServer(fileRepository);

            Assert.assertEquals(PROCESSED.getCode(), fileRepository.getProcessStatus());
            Assert.assertEquals(ABANDON.getCode(), fileRepository.getSendStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepository.getExecuteStatus());

            FileRepository fileRepositoryInDB = fileRepositoryService.get_by_uuid(uuid);
            Assert.assertEquals(uuid, fileRepositoryInDB.getUuid());
            Assert.assertEquals(PROCESSED.getCode(), fileRepositoryInDB.getProcessStatus());
            Assert.assertEquals(ABANDON.getCode(), fileRepositoryInDB.getSendStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepositoryInDB.getExecuteStatus());
        } catch (MorganStanleyException e) {
            Assert.fail();
        }
    }

    @Test
    @Sql("classpath:test/file/test_verifySignUpdateProcessStatus_verifySign_succ.sql")
    public void test_sendToNotifyServer_succ() throws IOException {
        String filePath = new ClassPathResource("/data/signal_file/repaymentPlan_test_20170717_123456_verify_succ.txt").getFile().getAbsolutePath();

        String uuid = UUIDUtil.random32UUID();

        try {

            fileRepositoryRedisPersistence.deleteAll(uuid);

            FileRepository fileRepository = new FileRepository();
            fileRepository.setUuid(uuid);
            fileRepository.setPath(filePath);
            fileRepository.setProductCode("G31700");
            fileRepository.setProcessStatus(FileProcessStatus.PROCESSED.getCode());
            fileRepository.setSendStatus(FileSendStatus.UNSEND.getCode());

            spdBankBatchProcessHandler.sendToNotifyServer(fileRepository);

            Assert.assertEquals(PROCESSED.getCode(), fileRepository.getProcessStatus());
            Assert.assertEquals(SEND.getCode(), fileRepository.getSendStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepository.getExecuteStatus());

            FileRepository fileRepositoryInDB = fileRepositoryService.get_by_uuid(uuid);
            Assert.assertEquals(uuid, fileRepositoryInDB.getUuid());
            Assert.assertEquals(PROCESSED.getCode(), fileRepositoryInDB.getProcessStatus());
            Assert.assertEquals(SEND.getCode(), fileRepositoryInDB.getSendStatus());
            Assert.assertEquals(UNEXECUTE.getCode(), fileRepositoryInDB.getExecuteStatus());

            List<String> idList = fileRepositoryRedisPersistence.peekJobsFromHead(uuid);
            Assert.assertTrue(CollectionUtils.isNotEmpty(idList));
            Assert.assertEquals(3, idList.size());

            fileRepositoryRedisPersistence.deleteAll(uuid);
        } catch (Exception e) {
            Assert.fail();
        }

    }

}