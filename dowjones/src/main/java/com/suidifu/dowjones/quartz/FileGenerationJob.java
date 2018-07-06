package com.suidifu.dowjones.quartz;

import com.suidifu.dowjones.dao.FinancialContractDAO;
import com.suidifu.dowjones.model.Const;
import com.suidifu.dowjones.service.FileGenerationService;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class FileGenerationJob extends QuartzJobBean {

    @Autowired
    private FinancialContractDAO financialContractDAO;

    @Autowired
    private FileGenerationService fileGenerationService;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Long startTime = System.currentTimeMillis();
        Date yesterday = DateUtils.yesterday();
        List<String> financialContractUuids = financialContractDAO.getAllFinancialContractUuid();

//        financialContractUuids = Arrays
//            .asList("3b12ac75-4c58-4375-a733-78c7810efebb", "429d5892-51dd-42fc-aa7e-e333778fd32f","e0970224-5e9b-477a-82f3-33ab252545b8");

        financialContractUuids = Const.CLIENT_PRODUCT;
        for (String uuid : financialContractUuids) {
            try {
                log.info("Start generate file: params [{}, {}]", uuid, DateUtils.getDateFormatYYMMDD(yesterday));
                fileGenerationService.generateFileReport(uuid, yesterday);
                log.info("End generate file: params [{}, {}]", uuid, DateUtils.getDateFormatYYMMDD(yesterday));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("FileGenerationJob occur error");
            }
        }
        Long endTime = System.currentTimeMillis();
        log.info("Generate file use time {}ms", endTime - startTime);
    }
}
