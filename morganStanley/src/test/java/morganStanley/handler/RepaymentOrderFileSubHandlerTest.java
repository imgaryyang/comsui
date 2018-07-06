/**
 *
 */
package morganStanley.handler;

import com.suidifu.morganstanley.TestMorganStanley;
import com.suidifu.morganstanley.configuration.bean.yntrust.YntrustFileTask;
import com.suidifu.morganstanley.exception.MorganStanleyException;
import com.suidifu.morganstanley.handler.FileProcessHandler;
import com.suidifu.morganstanley.tasks.FileProcessTask;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.zufangbao.gluon.spec.earth.v3.ApiConstant;
import com.zufangbao.sun.utils.FileUtils;
import com.zufangbao.sun.utils.FilenameUtils;
import com.zufangbao.sun.yunxin.entity.files.FileRepository;
import com.zufangbao.sun.yunxin.entity.files.FileSubRepaymentOrder;
import com.zufangbao.sun.yunxin.service.files.FileRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author hjl
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestMorganStanley.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Slf4j
@ActiveProfiles(value = "test")
class RepaymentOrderFileSubHandlerTest {
    @Autowired
    private FileProcessHandler fileProcessHandler;
    @Autowired
    private FileProcessTask fileprocesstask;
    @Autowired
    private FileRepositoryService fileRepositoryService;

    /**
     * 测试还款订单文件提交
     * hjl
     * 2017年8月15日
     *
     * @throws MorganStanleyException
     */
    @Test
    public void buildRepaymentOrderFileSubNotifyApplicationSeccussTest() throws MorganStanleyException {
        FileRepository fileRepository = fileRepositoryService.get_by_uuid("b3067092-1bf1-40d9-b9ce-3f9c68add023");
        String privateKey = fileProcessHandler.getPrivateKey();
        String businessType = fileRepository.getProductCode() + FilenameUtils.LOG_SPLIT + fileRepository.getFileTypeCode();
        List<FileSubRepaymentOrder> planList = FileUtils.readJsonList(fileRepository.getPath(), FileSubRepaymentOrder.class);

        FileSubRepaymentOrder plan1 = planList.get(0);
        String bizId = fileRepository.getProductCode() + FilenameUtils.LOG_SPLIT + fileRepository.getUuid() + FilenameUtils.LOG_SPLIT + plan1.getOrderRequestNo() +
                FilenameUtils.LOG_SPLIT + getMerId() + FilenameUtils.LOG_SPLIT + plan1.getOrderUniqueId();
        NotifyApplication notifyApplication = fileProcessHandler.buildRepaymentOrderFileSubNotifyApplication(businessType, fileRepository, bizId, plan1,
                privateKey, "ss");
        HashMap<String, String> map = notifyApplication.getRequestParameters();

        assertEquals(notifyApplication == null, false);
        assertEquals(notifyApplication.getRequestUrl(), "http://127.0.0.1:7778/api/v2/repaymentOrder");
        assertEquals(notifyApplication.getRequestMethod(), "post");
        assertEquals(map == null, false);
        assertEquals(map.get(ApiConstant.ORDER_REQUEST_NO), "868fc81c-5864-4fb6-b40a-4cfc9f941120");
        assertEquals(map.get(ApiConstant.ORDER_UNIQUE_ID), "dfsghsfghdfghsfghdfghfghdf");
        assertEquals(map.get(ApiConstant.TRANS_TYPE), "0");
        assertEquals(map.get(ApiConstant.FINANCIAL_CONTRACT_NO), "spdbank");
        assertEquals(map.get(ApiConstant.ORDER_AMOUNT), "980");
        assertEquals(map.get(ApiConstant.REPAYMENT_ORDER_DETAIL), "[{\"contractNo\":\"妹妹你大胆的往前走201\",\"contractUniqueId\":\"妹妹你大胆的往前走201\"," +
                "\"detailsAmountJsonList\":[{\"actualAmount\":900,\"feeType\":1000},{\"actualAmount\":20,\"feeType\":1001},{\"actualAmount\":20,\"feeType\":1002}," +
                "{\"actualAmount\":20,\"feeType\":1003},{\"actualAmount\":20,\"feeType\":1004}],\"detailsTotalAmount\":980,\"plannedDate\":\"2017-7-11 00:00:00\"," +
                "\"repaymentBusinessNo\":\"ZC95941724935032832\",\"repaymentWay\":2001}]");
        assertEquals(map.get(ApiConstant.ORDER_NOTIFY_URL), null);
    }

    @Resource
    private YntrustFileTask yntrustFileTask;

    public String getMerId() {
        return yntrustFileTask.getMerId();
    }

    @Test
    public void getApiTest() {
        FileRepository fileRepository = fileRepositoryService.get_by_uuid("7ac9bf17-b4d5-4aba-8ebf-f3ff3e380899");
        String privateKey = fileProcessHandler.getPrivateKey();
        String businessType = fileRepository.getProductCode() + FilenameUtils.LOG_SPLIT + fileRepository.getFileTypeCode();
        List<FileSubRepaymentOrder> planList = FileUtils.readJsonList(fileRepository.getPath(), FileSubRepaymentOrder.class);

        FileSubRepaymentOrder plan1 = planList.get(0);
        String bizId = fileRepository.getProductCode() + FilenameUtils.LOG_SPLIT + fileRepository.getUuid() + FilenameUtils.LOG_SPLIT + plan1.getOrderRequestNo() +
                FilenameUtils.LOG_SPLIT + getMerId() + FilenameUtils.LOG_SPLIT + plan1.getOrderUniqueId();
        NotifyApplication notifyApplication = fileProcessHandler.buildRepaymentOrderFileSubNotifyApplication(businessType, fileRepository, bizId, plan1,
                privateKey, "ss");
        assertEquals(notifyApplication, null);
    }

//		@Sql("classpath:test/FileSubRepaymentOrder.sql")
//		   public void fileSubRepaymentOrderHandlerSeccussandGenerationRepayment(){
//					fileprocesstask.fileSendServers();
//					RepaymentOrder repaymentOrder=repaymentorderservice.getRepaymentOrderByUniqueId("dfsghsfghdfghsfghdfghfghdf", "systemdeduct");
//					assertEquals(repaymentOrder==null, false);
//					assertEquals(repaymentOrder.getOrderAmount(), new BigDecimal("980"));
//					assertEquals(repaymentOrder.getOrderRequestNo(), "868fc81c-5864-4fb6-b40a-4cfc9f941120");
//					assertEquals(repaymentOrder.transferToRepaymentStatus(), RepaymentStatus.VERIFICATION_SUCCESS);
//					List<RepaymentOrderItem> repaymentOrderItems=repaymentorderitemservice.getRepaymentOrderItems(repaymentOrder.getOrderUuid());
//					assertEquals(repaymentOrderItems==null, false);
//					assertEquals(repaymentOrderItems.size(), 1);
//					RepaymentOrderItem repaymentOrderItem1=repaymentOrderItems.get(0);
//					assertEquals(repaymentOrderItem1.getContractNo(), "妹妹你大胆的往前走201");
//					assertEquals(repaymentOrderItem1.getContractUniqueId(), "妹妹你大胆的往前走201");
//					assertEquals(repaymentOrderItem1.getAmount(), new BigDecimal("980"));
//					assertEquals(repaymentOrderItem1.getRepaymentBusinessNo(), "ZC95941724935032832");
//					assertEquals(repaymentOrderItem1.getRepaymentWay(), RepaymentWay.MERCHANT_TRANSFER);
//			}

    /**
     * 测试扫描文件，并保存业务文件
     * hjl
     * 2017年8月15日
     */
    @Test
    public void scanSftpFileTest() {
        fileprocesstask.scanSftpFile();
    }

    @Test
    public void testsss() {
        fileprocesstask.fileSendServers();
    }

    /**
     * hjl
     * 2017年8月15日
     * 没有创建,处理中,发送的文件
     */
    @Test
    @Sql("classpath:test/FileTmpRepaymentOrderHandlerSeccussNotFile.sql")
    public void filetmpRepaymentOrderHandlerSeccuss_notFile() {
//							fileprocesstask.process_spdbank_modify_repaymentorder_file();
    }

}
