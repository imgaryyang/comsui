package com.suidifu.dowjones.service;


import java.io.IOException;
import java.util.Date;

public interface FileGenerationService {

    void generateFileReport(String financialContractUuid, Date doDate)
            throws IOException;

}
