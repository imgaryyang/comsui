package com.suidifu.munichre.util;

import com.demo2do.core.utils.StringUtils;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author louguanyang at 2018/4/18 11:56
 * @mail louguanyang@hzsuidifu.com
 */
public class AbsUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AbsUtils.class);

    public static boolean notAbsFinancialContract(String financialContractUuidList, String financialContractUuid) {
        try {
            if (StringUtils.isEmpty(financialContractUuidList)) {
                LOG.error("financialContractUuidList is empty.");
                return true;
            }
            List<String> validFinancialContractUuidList = Arrays.asList(financialContractUuidList.split(","));
            return !validFinancialContractUuidList.contains(financialContractUuid);
        } catch (Exception e) {
            LOG.error("check AbsFinancialContract error.", e);
            return true;
        }
    }
}
