package morganStanley.handler;

import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.handler.FileProcessHandler;
import com.suidifu.morganstanley.servers.FileRepositoryRedisPersistence;
import com.suidifu.morganstanley.tasks.FileProcessTask;
import com.suidifu.morganstanley.utils.ApiSignUtils;
import com.zufangbao.sun.api.model.modify.ModifyOverDueFeeDetail;
import com.zufangbao.sun.entity.customer.CustomerPerson;
import com.zufangbao.sun.service.PersonService;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.uuid.UUIDUtil;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;
import com.zufangbao.sun.yunxin.entity.files.FileSignal;
import com.zufangbao.sun.yunxin.entity.files.FileTmp_OverdueFee;
import com.zufangbao.sun.yunxin.entity.model.api.modify.SPDBankRepaymentPlanModifyRequestDataModel;
import com.zufangbao.sun.yunxin.entity.repayment.FeeDetail;
import com.zufangbao.sun.yunxin.entity.repayment.OverduePlanDetail;
import com.zufangbao.sun.yunxin.entity.repayment.RepaymentPlanDetail_SPDBank;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zufangbao.sun.yunxin.entity.files.FileExecuteStatus.EXECUTED;
import static com.zufangbao.sun.yunxin.entity.files.FileExecuteStatus.UNEXECUTE;
import static com.zufangbao.sun.yunxin.entity.files.FileProcessStatus.PROCESSED;
import static com.zufangbao.sun.yunxin.entity.files.FileSendStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
public class FileProcessHandlerTest {

    @Autowired
    private FileProcessHandler fileProcessHandler;

    @Autowired
    private FileRepositoryService fileRepositoryService;

    @Autowired
    @Qualifier("FileRepositoryRedisPersistence")
    private FileRepositoryRedisPersistence fileRepositoryRedisPersistence;

    @Autowired
    private FileProcessTask fileProcessTask;

    @Autowired
    private PersonService personService;

    @Test
    public void scan_signal_file() throws Exception {
        String scanPath = new ClassPathResource("/data/signal_file/empty.txt").getFile().getParent();
        String productCode= "G31700";
        String pathname = scanPath + File.separator + productCode + File.separator + DateUtils.today() + File.separator;
//        File file = new File(pathname);
//        FileUtils.cleanDirectory(file);

        List<String> biz_path_list = new ArrayList<>();

        String uuid = UUIDUtil.random32UUID();

        for (int index = 0; index < 10; index ++) {
            String biz_file_name = "repaymentPlan_" + "pufa_" + uuid + DateUtils.getNowFullDateTime() + "_" + index + "-10" + FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils
                    .SUFFIX_TXT;

            String biz_file_path = scanPath + File.separator + biz_file_name;
            parallel_write_file(biz_file_path, true);

            biz_path_list.add(biz_file_name);
        }

        String tradeDate = DateUtils.today();
        fileProcessHandler.scan_signal_file(scanPath, tradeDate);
        List<FileRepository> all = fileRepositoryService.loadAll(FileRepository.class);
        Assert.assertEquals(10, all.size());

        for (int index = 0; index < 10; index ++) {
            String expected_path = pathname +  biz_path_list.get(index) + FilenameUtils.SIGNAL_FILE_SPLIT;
            FileRepository fileRepository = all.get(index);
            String act_path = fileRepository.getPath();
            Assert.assertEquals(expected_path, act_path);
        }
    }

    private void parallel_write_file(String biz_file_path, boolean hasBizFile) throws IOException {
        String ok_file_path = biz_file_path + FilenameUtils.EXTENSION_SEPARATOR + FilenameUtils.SUFFIX_OK;

        File biz_file = new File(biz_file_path);
        File ok_file = new File(ok_file_path);
        for (int index = 0; index < 10; index ++) {
            String uuid = "业务文件内容##########" + index + "\n";
            FileUtils.writeStringToFile(biz_file, uuid, FilenameUtils.UTF_8, true);
        }
        System.out.println("业务文件地址----->" + biz_file.getAbsolutePath());

        for (int index = 0; index < 5; index ++) {
            FileSignal fileSignal = new FileSignal();
            fileSignal.setMerId("pufa");
            fileSignal.setFinancialProductCode("HC73100");
            String jsonString = JsonUtils.toJSONString(fileSignal);
            FileUtils.writeStringToFile(ok_file, jsonString, FilenameUtils.UTF_8, true);
        }
        System.out.println("信号文件地址----->" + ok_file.getAbsolutePath());
    }

    /** t_merchant的私钥 **/
    public  String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOXUGOKdEkssOI1zqNw4pb6emH1o1JYxooyTQ7FN1IBNqTJLuvA3GsswXIkuQj0imce6ywG/XOCwc9R1l5FwcORtwx2FihGCl7eBkhUwnT0EwGOEARPh96ey+TfvsvRaHOn672v1TEhajAftgm4l7fJDtHdGBjHOs+5Mlir9Z65RAgMBAAECgYEArtAiUZR5yrYLGgTEhyWLZK+Le7CWKtv8MQL+tUlm/mST8s7JlVfEyJKzgCCwf4HnCJXbPiwJgFqW8B61uAmXw6cEoPftEnzvKBTyISt/iEf7DTWKGkDBnlYM9sFU6pU61jw17XEDQRtSBG6cfrlGSelqf25+c8onxu4YwTeLH/ECQQD/H69tPy0FYRvCJ5yXdXEVCKshNN01P+UdDzGtyysE/gmpalbewT+uznApa0qFntcYb8eSpUJzrUlItSCBGUpdAkEA5p4r3qF+4g5V7MBHm3+v1l9JKxYK76990AQJa122rfkY2EEVuvU+8KIAQpVflu/HpDe8QH4mQZTsZj24Skt8hQJAL5j2vrgRqzZB2ohPY8aKcXUrkEdvmdaw5SoHh7gm74iBvvTS/j4ppnBnZqLYxXMsCCgaoNZqPnCvAnygctWIFQJAHm2KLkKyohLwJV+tUwgC5E8IMWYkJUHLYNHXiFICE2xFaesUeel313oYfLCGvzx9493yubOrSoXitw63rR3OnQJBALwGSnGYodmJB5k7un0X6LlO4nSu/+SX9lweloZ1AUg15IuCIYxHAFKwOtOJmx/eMcITaLq8l1qzZ907UXY+Mfs=";

    public String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDl1BjinRJLLDiNc6jcOKW+nph9aNSWMaKMk0OxTdSATakyS7rwNxrLMFyJLkI9IpnHussBv1zgsHPUdZeRcHDkbcMdhYoRgpe3gZIVMJ09BMBjhAET4fensvk377L0Whzp+u9r9UxIWowH7YJuJe3yQ7R3RgYxzrPuTJYq/WeuUQIDAQAB";

    @Test
    public void test_rsa_json_object() throws IOException {
        String filePath = new ClassPathResource("/data/signal_file/empty.txt").getFile().getAbsolutePath();
        File bizFile = new File(filePath);


        String content = FileUtils.readFileToString(bizFile, FilenameUtils.UTF_8);

        String sign = ApiSignUtils.rsaSign(content, privateKey);

        System.out.println(sign);

        boolean result = ApiSignUtils.rsaCheckContent(content, sign, publicKey);

        Assert.assertTrue(result);
    }

//    @Test
//    public void test_process_spdbank_modify_repayment_file() throws IOException {
//        Product3lvl product3lvl = new Product3lvl(ProductLv1Code.SPDBANK.getCode(), ProductLv2Code.UPLOAD.getCode(), FileType.MODIFY_REPAYMENT.getTypeCode());
//        ProductCategory category = productCategoryCacheHandler.get(product3lvl);
//        if (null == category) {
//            return;
//        }
//        String productCode = category.getProductLv1Code();
//        String businessType = category.transfer().shortName();
//
//        String filePath = new ClassPathResource("/data/signal_file/2017-08-10.txt").getFile().getAbsolutePath();
//        File file = new File(filePath);
//        Assert.assertTrue(file.exists());
//
//        FileRepository fileRepository = new FileRepository(productCode,"1",file.getAbsolutePath(),new Date());
//        fileRepository.setUuid("d2d3dd3d3dd3d3d3d3d3");
//        fileRepository.setStatus(1);
//
////        boolean result = spdBankBatchProcessHandler.process_file_send_notify_server(businessType, fileRepository);
////        Assert.assertTrue(result);
//    }

    @Test
    public void test_RepaymentPlanDetail_SPDBank_parse(){
        RepaymentPlanDetail_SPDBank repaymentPlanDetail_spdBank = new RepaymentPlanDetail_SPDBank();
        repaymentPlanDetail_spdBank.setRepayScheduleNo("abc1");
        repaymentPlanDetail_spdBank.setRepaymentDate("2017-07-18");
        List<FeeDetail> feeDetails = new ArrayList<>();
        FeeDetail feeDetail = new FeeDetail();
        feeDetail.setAmount("0.00");
        feeDetail.setFeeType("1000");
        feeDetails.add(feeDetail);
        FeeDetail feeDetail1 = new FeeDetail();
        feeDetail1.setAmount("10.00");
        feeDetail1.setFeeType("1001");
        feeDetails.add(feeDetail1);
        repaymentPlanDetail_spdBank.setFeeDetail(feeDetails);
        SPDBankRepaymentPlanModifyRequestDataModel model = repaymentPlanDetail_spdBank.parse("");
        Assert.assertNotNull(model);
        Assert.assertEquals("10.00",model.getAssetInterest().toString());
        Assert.assertEquals("0.00",model.getAssetPrincipal().toString());
//        Assert.assertEquals("abc1",model.getOuterRepaymentPlanNo());
        Assert.assertEquals("2017-07-18",model.getAssetRecycleDate());
    }

    @Test
    public void test_FileTmp_OverdueFee_parse(){
        FileTmp_OverdueFee file = new FileTmp_OverdueFee();
        file.setContractNo("");
        file.setUniqueId("uniqueId1");
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
        String parse = JsonUtils.toJsonString(models);
        file.setOverduePlanDetail(JsonUtils.toJsonString(models));
        List<ModifyOverDueFeeDetail> result = file.parse();
        Assert.assertNotNull(result);
        Assert.assertEquals(1,result.size());
        for(ModifyOverDueFeeDetail detail :result){
            Assert.assertEquals("00123456789",detail.getRepayScheduleNo());
            Assert.assertEquals("2017-07-17", detail.getOverDueFeeCalcDate());
            Assert.assertEquals("10.00", detail.getPenaltyFee());
            Assert.assertEquals("10.00", detail.getLatePenalty());
            Assert.assertEquals("0.00", detail.getLateOtherCost());
            Assert.assertEquals("0.00", detail.getLateFee());
            Assert.assertEquals("20.00", detail.getTotalOverdueFee());
        }

    }

//    @Test
//    @Sql("classpath:test/file/test_modify_overdue_fee_file.sql")
//    public void process_spdbank_modify_overdue_fee_file() throws IOException {
//        Product3lvl product3lvl = new Product3lvl(ProductLv1Code.SPDBANK.getCode(), ProductLv2Code.UPLOAD.getCode(), FileType.OVERDUE_FEE.getTypeCode());
//        ProductCategory category = productCategoryCacheHandler.get(product3lvl);
//        if (null == category) {
//            return;
//        }
//        String productCode = category.getProductLv1Code();
//        String businessType = category.transfer().shortName();
//
//        String filePath = new ClassPathResource("/data/signal_file/2017-08-10_overfee.txt").getFile().getAbsolutePath();
//        File file = new File(filePath);
//        Assert.assertTrue(file.exists());
//
//        FileRepository fileRepository = new FileRepository(productCode,"1",file.getAbsolutePath(),new Date());
//        fileRepository.setUuid("d2d3dd3d3dd3d3d3d3d3");
//        fileRepository.setStatus(1);
//
////        boolean result = overdueFeeBatchProcessHandler.process_file_send_notify_server(businessType, fileRepository);
////        Assert.assertTrue(result);
//    }

    @Test
    public void test_update_fileRepository_abandon() throws MorganStanleyException {
        String uuid = UUIDUtil.random32UUID();
        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setProductCode("productCode");
        fileProcessHandler.update_fileRepository_abandon(fileRepository);

        Assert.assertEquals(PROCESSED.getCode(), fileRepository.getProcessStatus());
        Assert.assertEquals(ABANDON.getCode(), fileRepository.getSendStatus());
        Assert.assertEquals(UNEXECUTE.getCode(), fileRepository.getExecuteStatus());

        FileRepository fileRepositoryInDB = fileRepositoryService.get_by_uuid(uuid);
        Assert.assertEquals(uuid, fileRepositoryInDB.getUuid());
        Assert.assertEquals(PROCESSED.getCode(), fileRepositoryInDB.getProcessStatus());
        Assert.assertEquals(ABANDON.getCode(), fileRepositoryInDB.getSendStatus());
        Assert.assertEquals(UNEXECUTE.getCode(), fileRepositoryInDB.getExecuteStatus());
    }

    @Test
    public void test_update_fileRepository_processed() throws MorganStanleyException {
        String uuid = UUIDUtil.random32UUID();
        int tradeSize = 10;
        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setProductCode("productCode");
        fileProcessHandler.update_fileRepository_processed(fileRepository, tradeSize);

        Assert.assertEquals(PROCESSED.getCode(), fileRepository.getProcessStatus());
        Assert.assertEquals(UNSEND.getCode(), fileRepository.getSendStatus());
        Assert.assertEquals(UNEXECUTE.getCode(), fileRepository.getExecuteStatus());
        Assert.assertEquals(tradeSize, fileRepository.getTradeSize().intValue());

        FileRepository fileRepositoryInDB = fileRepositoryService.get_by_uuid(uuid);
        Assert.assertEquals(uuid, fileRepositoryInDB.getUuid());
        Assert.assertEquals(PROCESSED.getCode(), fileRepositoryInDB.getProcessStatus());
        Assert.assertEquals(UNSEND.getCode(), fileRepositoryInDB.getSendStatus());
        Assert.assertEquals(UNEXECUTE.getCode(), fileRepositoryInDB.getExecuteStatus());
        Assert.assertEquals(tradeSize, fileRepositoryInDB.getTradeSize().intValue());
    }

    @Test
    public void test_update_fileRepository_send() throws MorganStanleyException {
        String uuid = UUIDUtil.random32UUID();

        fileRepositoryRedisPersistence.deleteAll(uuid);

        List<String> bizIdList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            bizIdList.add(i + "");
        }

        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setProductCode("productCode");

        fileProcessHandler.update_fileRepository_send(fileRepository, bizIdList);

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
        Assert.assertEquals(1000, idList.size());

        fileRepositoryRedisPersistence.deleteAll(uuid);
    }

    @Test
    public void test_update_fileRepository_executed() throws MorganStanleyException {
        String uuid = UUIDUtil.random32UUID();
        int processSize = 100;
        FileRepository fileRepository = new FileRepository();
        fileRepository.setUuid(uuid);
        fileRepository.setProductCode("productCode");

        fileProcessHandler.update_fileRepository_executed(fileRepository, processSize);

        Assert.assertEquals(PROCESSED.getCode(), fileRepository.getProcessStatus());
        Assert.assertEquals(SEND.getCode(), fileRepository.getSendStatus());
        Assert.assertEquals(EXECUTED.getCode(), fileRepository.getExecuteStatus());
        Assert.assertEquals(processSize, fileRepository.getProcessSize().intValue());

        FileRepository fileRepositoryInDB = fileRepositoryService.get_by_uuid(uuid);
        Assert.assertEquals(uuid, fileRepositoryInDB.getUuid());
        Assert.assertEquals(PROCESSED.getCode(), fileRepositoryInDB.getProcessStatus());
        Assert.assertEquals(SEND.getCode(), fileRepositoryInDB.getSendStatus());
        Assert.assertEquals(EXECUTED.getCode(), fileRepositoryInDB.getExecuteStatus());
        Assert.assertEquals(processSize, fileRepositoryInDB.getProcessSize().intValue());
    }

    @Test
    @Sql("classpath:test/task/testProcessBaiduDetail.sql")
    public void testProcessBaiDuFile() {
        fileProcessTask.processBaiDuFile_src();
        String customerUuid = "e67ba4bc-48ee-4f23-b8cd-b4fea2448a1c";
        CustomerPerson customerPerson = personService.getCustomerPersonBy(customerUuid);
        Assert.assertEquals("18621061954", customerPerson.getMobile());
    }

    @Test
    public void testProcessBaiDuFileFromFTP() {
        fileProcessTask.processBaiDuFile();
        String customerUuid = "e67ba4bc-48ee-4f23-b8cd-b4fea2448a1c";
        CustomerPerson customerPerson = personService.getCustomerPersonBy(customerUuid);
        Assert.assertEquals("18621061954", customerPerson.getMobile());
        Assert.assertEquals(6, customerPerson.getMaritalStatusOrdinal());
        System.out.println(customerPerson.getMaritalStatusOrdinal());
    }
}