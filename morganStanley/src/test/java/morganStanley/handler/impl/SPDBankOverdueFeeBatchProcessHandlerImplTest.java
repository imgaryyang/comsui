package morganStanley.handler.impl;

import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.handler.FileProcessHandler;
import com.suidifu.morganstanley.handler.impl.OverdueFeeBatchProcessHandlerImpl;
import com.suidifu.morganstanley.servers.FileRepositoryRedisPersistence;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.files.FileProcessStatus;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_OverdueFee;
import com.zufangbao.sun.yunxin.entity.model.api.modify.ModifyOverDueFeeDetailsRequestDataModel;
import com.zufangbao.sun.yunxin.entity.repayment.FeeDetail;
import com.zufangbao.sun.yunxin.entity.repayment.OverduePlanDetail;
import com.zufangbao.sun.yunxin.service.TMerConfigService;
import com.zufangbao.sun.yunxin.service.files.FileRepositoryService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;

import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_API_V3;
import static com.zufangbao.gluon.spec.earth.v3.ApiConstant.ApiUrlConstant.URL_MODIFY_OVER_DUE_FEE;
import static com.zufangbao.sun.yunxin.entity.files.FileExecuteStatus.UNEXECUTE;
import static com.zufangbao.sun.yunxin.entity.files.FileProcessStatus.PROCESSED;
import static com.zufangbao.sun.yunxin.entity.files.FileSendStatus.ABANDON;
import static com.zufangbao.sun.yunxin.entity.files.FileSendStatus.UNSEND;

/**
 * Created by hwr on 17-8-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class SPDBankOverdueFeeBatchProcessHandlerImplTest {
    @Autowired
    @Qualifier("OverdueFeeBatchProcessHandlerImpl")
    private OverdueFeeBatchProcessHandlerImpl overdueFeeBatchProcessHandler;

    @Autowired
    private TMerConfigService tMerConfigService;

    @Autowired
    private FileRepositoryService fileRepositoryService;

    @Autowired
    @Qualifier("FileRepositoryRedisPersistence")
    private FileRepositoryRedisPersistence fileRepositoryRedisPersistence;

    @Autowired
    private FileProcessHandler fileProcessHandler;

    public String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG" +
            "/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL" +
            "+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD" +
            "/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU" +
            "+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV" +
            "+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx" +
            "/eMcITaLq8l1qzZ907UXY+Mfs=";

    @Test
    @Sql("classpath:test/file/test_overduefee_verifySign.sql")
    public void verifySign() throws Exception {
        String filePath = new ClassPathResource("/data/signal_file/overdueFee_test_20170815_baaaf62a-6b98-11e7-96e7-dec95554322_succ.txt").getFile()
                .getAbsolutePath();

        FileRepository fileRepository = new FileRepository();
        fileRepository.setPath(filePath);

        boolean verifySign = overdueFeeBatchProcessHandler.verifySign(fileRepository, tMerConfigService);
        Assert.assertTrue(verifySign);
    }

    @Test
    public void test_verifySignUpdateProcessStatus_error() {
        FileRepository fileRepository = null;
        String error_msg_1 = "fileRepository is null.";
        String error_msg_2 = "fileRepository 非(待处理)的, 跳过验签.";
        try {
            overdueFeeBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);
            Assert.fail();
        } catch (MorganStanleyException e) {
            Assert.assertEquals(error_msg_1, e.getMessage());
        }

        //已处理
        try {
            fileRepository = new FileRepository();
            fileRepository.setProcessStatus(FileProcessStatus.PROCESSED.getCode());

            overdueFeeBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);
            Assert.fail();
        } catch (MorganStanleyException e) {
            Assert.assertEquals(error_msg_2, e.getMessage());
        }
    }

    @Test
    @Sql("classpath:test/file/test_overduefee_verifySignUpdateProcessStatus_verifySign_fail.sql")
    public void test_verifySignUpdateProcessStatus_verifySign_fail() throws IOException {
        String filePath = new ClassPathResource("/data/signal_file/overdueFee_test_20170815_baaaf62a-6b98-11e7-96e7-dec95554322_fail.txt").getFile()
                .getAbsolutePath();

        String uuid = UUIDUtil.random32UUID();

        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setPath(filePath);
        fileRepository.setProductCode("G31700");
        fileRepository.setProcessStatus(FileProcessStatus.UNPROCESSED.getCode());

        try {
            overdueFeeBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);

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
    @Sql("classpath:test/file/test_overduefee_verifySignUpdateProcessStatus_verifySign_succ.sql")
    public void test_verifySignUpdateProcessStatus_verifySign_succ() throws IOException {
        String filePath = new ClassPathResource("/data/signal_file/overdueFee_test_20170815_baaaf62a-6b98-11e7-96e7-dec95554322_succ.txt").getFile()
                .getAbsolutePath();

        String uuid = UUIDUtil.random32UUID();
        int tradeSize = 1;

        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setPath(filePath);
        fileRepository.setProductCode("G31700");
        fileRepository.setProcessStatus(FileProcessStatus.UNPROCESSED.getCode());

        try {
            overdueFeeBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);

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
    @Sql("classpath:test/file/test_overduefee_verifySignUpdateProcessStatus_verifySign_empty.sql")
    public void test_verifySignUpdateProcessStatus_verifySign_empty() throws IOException {
        String filePath = new ClassPathResource("/data/signal_file/repaymentPlan_test_20170717_123456_verify_empty.txt").getFile().getAbsolutePath();

        String uuid = UUIDUtil.random32UUID();

        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setPath(filePath);
        fileRepository.setProductCode("G31700");
        fileRepository.setProcessStatus(FileProcessStatus.UNPROCESSED.getCode());

        try {
            overdueFeeBatchProcessHandler.verifySignUpdateProcessStatus(fileRepository);

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
    @Sql("classpath:test/file/test_buildSPDBankOverDueFeeNotifyApplication.sql")
    public void test_buildSPDBankOverDueFeeNotifyApplication() throws IOException {

        FileTmp_OverdueFee file = new FileTmp_OverdueFee();
        file.setTradeNo("1");
        file.setContractNo("");
        file.setUniqueId("uniqueId1");
        file.setFinancialProductCode("G31700");
        List<OverduePlanDetail> models = new ArrayList<>();
        OverduePlanDetail model = new OverduePlanDetail();
        model.setOverDueFeeCalcDate("2017-07-17");
        model.setRepaymentPlanNo("");
        model.setRepayScheduleNo("00123456789");
        List<FeeDetail> feeDetails = new ArrayList<>();
        FeeDetail feeDetail = new FeeDetail();
        feeDetail.setFeeType("1005");
        feeDetail.setAmount("10.00");
        feeDetails.add(feeDetail);
        feeDetail = new FeeDetail();
        feeDetail.setFeeType("1006");
        feeDetail.setAmount("10.00");
        feeDetails.add(feeDetail);
        feeDetail = new FeeDetail();
        feeDetail.setFeeType("1007");
        feeDetail.setAmount("0.00");
        feeDetails.add(feeDetail);
        feeDetail = new FeeDetail();
        feeDetail.setFeeType("1008");
        feeDetail.setAmount("0.00");
        feeDetails.add(feeDetail);
        model.setFeeDetail(feeDetails);
        models.add(model);
        file.setOverduePlanDetail(JsonUtils.toJSONString(models));
        String uuid = UUIDUtil.random32UUID();

        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setPath("/data/signal_file/overdueFee_test_20170815_baaaf62a-6b98-11e7-96e7-dec95554322_succ.txt");
        fileRepository.setProductCode("G31700");
        fileRepository.setProcessStatus(FileProcessStatus.UNPROCESSED.getCode());
        NotifyApplication notifyApplication = fileProcessHandler.buildSPDBankOverDueFeeNotifyApplication(fileRepository, file, privateKey, "group0");
        Assert.assertNotNull(notifyApplication);
        Assert.assertEquals("http://127.0.0.1:7778" + URL_API_V3 + URL_MODIFY_OVER_DUE_FEE, notifyApplication.requestUrl);
        Assert.assertEquals(5, notifyApplication.getRequestParameters().size());
        Assert.assertEquals("G31700#1", notifyApplication.getRequestParameters().get(ApiConstant.REQUEST_NO));
        Assert.assertEquals("G31700", notifyApplication.getRequestParameters().get(ApiConstant.FINANCIAL_PRODUCT_CODE));
        System.out.println(notifyApplication.getRequestParameters().get(ApiConstant.MODIFY_OVERDUEFEE_DETAILS));
        List<ModifyOverDueFeeDetailsRequestDataModel> lists = JsonUtils.parseArray(notifyApplication.getRequestParameters().get(ApiConstant
                .MODIFY_OVERDUEFEE_DETAILS), ModifyOverDueFeeDetailsRequestDataModel.class);
        Assert.assertEquals("20.00", lists.get(0).getTotalOverdueFee());

    }
}
